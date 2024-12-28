// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.CameraConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;

public class IO_VisionSim implements IO_VisionBase {
	private final VisionSystemSim visionSim;
	private final Map<CameraConstants.Camera, PhotonCameraSim> cameraSims = new HashMap<>();
	private Optional<EstimatedRobotPose> lastEstimatedPose = Optional.empty();

	public IO_VisionSim() {
		visionSim = new VisionSystemSim("Vision");
		visionSim.addAprilTags(AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo));

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

			Transform3d robotToCam = new Transform3d(cam.translation, cam.rotation);
			visionSim.addCamera(cameraSim, robotToCam);
		}
	}

	@Override
	public void updateInputs(VisionInputs inputs) {
		// Update sim inputs similar to real implementation
		for (Map.Entry<CameraConstants.Camera, PhotonCameraSim> entry : cameraSims.entrySet()) {
			PhotonCameraSim cameraSim = entry.getValue();

			// TODO: Replace getLatestResult with getAllUnreadResults
			var result = cameraSim.getCamera().getLatestResult();

			switch (entry.getKey()) {
				case LEFT_CAM:
					inputs.hasLeftTarget = result.hasTargets();
					if (inputs.hasLeftTarget) {
						inputs.leftBestTargetID = result.getBestTarget().getFiducialId();
					}
					break;
				case RIGHT_CAM:
					inputs.hasRightTarget = result.hasTargets();
					if (inputs.hasRightTarget) {
						inputs.rightBestTargetID = result.getBestTarget().getFiducialId();
					}
					break;
				case CENTER_CAM:
					inputs.hasCenterTarget = result.hasTargets();
					if (inputs.hasCenterTarget) {
						inputs.centerBestTargetID = result.getBestTarget().getFiducialId();
					}
					break;
			}
		}

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
