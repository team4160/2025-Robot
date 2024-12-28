// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.Alert;
import frc.robot.constants.CameraConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

public class IO_VisionReal implements IO_VisionBase {
	private final AprilTagFieldLayout fieldLayout =
			AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo);
	private final Map<CameraConstants.Camera, PhotonCamera> cameras = new HashMap<>();
	private final Map<CameraConstants.Camera, PhotonPoseEstimator> poseEstimators = new HashMap<>();
	private final Map<CameraConstants.Camera, Alert> latencyAlerts = new HashMap<>();
	private Optional<EstimatedRobotPose> lastEstimatedPose = Optional.empty();

	public IO_VisionReal() {
		for (CameraConstants.Camera cam : CameraConstants.Camera.values()) {
			cameras.put(cam, new PhotonCamera(cam.name));

			Transform3d robotToCam = new Transform3d(cam.translation, cam.rotation);

			PhotonPoseEstimator estimator =
					new PhotonPoseEstimator(
							fieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, robotToCam);
			estimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
			poseEstimators.put(cam, estimator);

			latencyAlerts.put(
					cam,
					new Alert(
							"'" + cam.name + "' Camera experiencing high latency", Alert.AlertType.kWarning));
		}
	}

	@Override
	public void updateInputs(VisionInputs inputs) {
		PhotonPipelineResult leftResult =
				cameras.get(CameraConstants.Camera.LEFT_CAM).getLatestResult();
		PhotonPipelineResult rightResult =
				cameras.get(CameraConstants.Camera.RIGHT_CAM).getLatestResult();
		PhotonPipelineResult centerResult =
				cameras.get(CameraConstants.Camera.CENTER_CAM).getLatestResult();

		inputs.hasLeftTarget = leftResult.hasTargets();
		inputs.hasRightTarget = rightResult.hasTargets();
		inputs.hasCenterTarget = centerResult.hasTargets();

		inputs.leftLatencyMS = -1.0;
		inputs.rightLatencyMS = -1.0;
		inputs.centerLatencyMS = -1.0;

		if (inputs.hasLeftTarget) {
			inputs.leftBestTargetID = leftResult.getBestTarget().getFiducialId();
		}
		if (inputs.hasRightTarget) {
			inputs.rightBestTargetID = rightResult.getBestTarget().getFiducialId();
		}
		if (inputs.hasCenterTarget) {
			inputs.centerBestTargetID = centerResult.getBestTarget().getFiducialId();
		}
	}

	@Override
	public void updatePoseEstimation(Pose2d currentPose) {
		for (Map.Entry<CameraConstants.Camera, PhotonPoseEstimator> entry : poseEstimators.entrySet()) {
			PhotonPoseEstimator estimator = entry.getValue();
			PhotonCamera camera = cameras.get(entry.getKey());

			estimator.setReferencePose(currentPose);
			Optional<EstimatedRobotPose> result = estimator.update(camera.getLatestResult());
			if (result.isPresent()) {
				lastEstimatedPose = result;
			}
		}
		;
	}

	@Override
	public Optional<EstimatedRobotPose> getEstimatedGlobalPose() {
		return lastEstimatedPose;
	}
}
