// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.CameraConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.littletonrobotics.junction.Logger;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class IO_VisionSim implements IO_VisionBase {
	private final VisionSystemSim visionSim;
	private final Map<CameraConstants.Camera, PhotonCameraSim> cameraSims = new HashMap<>();
	private final Map<CameraConstants.Camera, PhotonPipelineResult> currentResults = new HashMap<>();
	private Optional<EstimatedRobotPose> lastEstimatedPose = Optional.empty();
	private final AprilTagFieldLayout fieldLayout;

	public IO_VisionSim() {
		fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo);
		visionSim = new VisionSystemSim("Vision");
		visionSim.addAprilTags(fieldLayout);

		// Set camera properties
		SimCameraProperties properties = new SimCameraProperties();
		properties.setCalibration(960, 720, new Rotation2d(Math.toRadians(100)));
		properties.setCalibError(0.25, 0.08);
		properties.setFPS(30);
		properties.setAvgLatencyMs(35);
		properties.setLatencyStdDevMs(5);

		// Add cameras
		for (CameraConstants.Camera cam : CameraConstants.Camera.values()) {
			PhotonCamera camera = new PhotonCamera(cam.name);
			PhotonCameraSim cameraSim = new PhotonCameraSim(camera, properties);
			cameraSim.enableDrawWireframe(true);
			cameraSims.put(cam, cameraSim);
			currentResults.put(cam, new PhotonPipelineResult()); // Empty initial result

			Transform3d robotToCam = new Transform3d(cam.translation, cam.rotation);
			visionSim.addCamera(cameraSim, robotToCam);
		}
	}

	@Override
	public void updateInputs(VisionInputs inputs) {
		List<Pose3d> visibleTagPoses = new ArrayList<>();

		// Update all camera results first
		for (Map.Entry<CameraConstants.Camera, PhotonCameraSim> entry : cameraSims.entrySet()) {
			CameraConstants.Camera cam = entry.getKey();
			PhotonCamera camera = entry.getValue().getCamera();

			List<PhotonPipelineResult> results = camera.getAllUnreadResults();
			if (!results.isEmpty()) {
				// Get the most recent result
				results.sort((a, b) -> Double.compare(b.getTimestampSeconds(), a.getTimestampSeconds()));
				currentResults.put(cam, results.get(0));
			}
		}

		// Process the results for each camera
		for (Map.Entry<CameraConstants.Camera, PhotonCameraSim> entry : cameraSims.entrySet()) {
			CameraConstants.Camera cam = entry.getKey();
			PhotonPipelineResult result = currentResults.get(cam);

			// Collect visible tag poses
			if (result.hasTargets()) {
				for (PhotonTrackedTarget target : result.getTargets()) {
					Optional<Pose3d> tagPose = fieldLayout.getTagPose(target.getFiducialId());
					tagPose.ifPresent(
							pose -> {
								if (!visibleTagPoses.contains(pose)) {
									visibleTagPoses.add(pose);
								}
							});
				}

				switch (cam) {
					case LEFT_CAM:
						inputs.hasLeftTarget = true;
						inputs.leftBestTargetID = result.getBestTarget().getFiducialId();
						break;
					case RIGHT_CAM:
						inputs.hasRightTarget = true;
						inputs.rightBestTargetID = result.getBestTarget().getFiducialId();
						break;
					case CENTER_CAM:
						inputs.hasCenterTarget = true;
						inputs.centerBestTargetID = result.getBestTarget().getFiducialId();
						break;
				}
			}
		}

		// Update inputs with visible tag poses
		inputs.visibleTagPoses = visibleTagPoses.toArray(new Pose3d[0]);
		Logger.recordOutput("Vision/TagPoses", inputs.visibleTagPoses);

		// Record estimated pose output
		SmartDashboard.putData("Vision/CurrentEstimatedPose", visionSim.getDebugField());
	}

	@Override
	public void updatePoseEstimation(Pose2d currentPose) {
		visionSim.update(currentPose);
	}

	@Override
	public Optional<EstimatedRobotPose> getEstimatedGlobalPose() {
		return lastEstimatedPose;
	}
}
