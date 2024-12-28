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
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.Alert;
import frc.robot.constants.CameraConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.littletonrobotics.junction.Logger;
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
	private Optional<EstimatedRobotPose> lastEstimatedPose = Optional.empty();

	private PhotonPipelineResult lastLeftResult = new PhotonPipelineResult();
	private PhotonPipelineResult lastRightResult = new PhotonPipelineResult();
	private PhotonPipelineResult lastCenterResult = new PhotonPipelineResult();

	public IO_VisionReal() {
		for (CameraConstants.Camera cam : CameraConstants.Camera.values()) {
			cameras.put(cam, new PhotonCamera(cam.name()));

			Transform3d robotToCam = new Transform3d(cam.translation, cam.rotation);
			PhotonPoseEstimator estimator =
					new PhotonPoseEstimator(
							fieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, robotToCam);
			estimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
			poseEstimators.put(cam, estimator);
		}
	}

	@Override
	public void updateInputs(VisionInputs inputs) {

		// Update the result variables. THIS SHOULD ONLY BE CALLED ONCE PER LOOP,
		// OTHERWISE THERE WILL BE DATA LOSS.
		// NEVER CALL getAllUnreadResults() or getLatestResult() OUTSIDE THIS LOOP!
		List<PhotonPipelineResult> leftResults =
				cameras.get(CameraConstants.Camera.LEFT_CAM).getAllUnreadResults();
		lastLeftResult = leftResults.isEmpty() ? lastLeftResult : leftResults.get(0);

		List<PhotonPipelineResult> rightResults =
				cameras.get(CameraConstants.Camera.RIGHT_CAM).getAllUnreadResults();
		lastRightResult = rightResults.isEmpty() ? lastRightResult : rightResults.get(0);

		List<PhotonPipelineResult> centerResults =
				cameras.get(CameraConstants.Camera.CENTER_CAM).getAllUnreadResults();
		lastCenterResult = centerResults.isEmpty() ? lastCenterResult : centerResults.get(0);

		inputs.hasLeftTarget = lastLeftResult.hasTargets();
		inputs.hasRightTarget = lastRightResult.hasTargets();
		inputs.hasCenterTarget = lastCenterResult.hasTargets();

		if (inputs.hasLeftTarget) {
			inputs.leftBestTargetID = lastLeftResult.getBestTarget().getFiducialId();
		}

		if (inputs.hasRightTarget) {
			inputs.rightBestTargetID = lastRightResult.getBestTarget().getFiducialId();
		}

		if (inputs.hasCenterTarget) {
			inputs.centerBestTargetID = lastCenterResult.getBestTarget().getFiducialId();
		}
	}

	// TODO: Test this on a real robot for pose averaging. 
	@Override 
	public void updatePoseEstimation(Pose2d currentPose) {
		List<EstimatedRobotPose> validEstimates = new ArrayList<>();

		for (Map.Entry<CameraConstants.Camera, PhotonPoseEstimator> entry : poseEstimators.entrySet()) {
			PhotonPoseEstimator estimator = entry.getValue();
			String cameraName = entry.getKey().name;

			// Use the stored results based on camera 
			PhotonPipelineResult result;
			switch (entry.getKey()) {
				case LEFT_CAM:
					result = lastLeftResult;
					break;
				case RIGHT_CAM:
					result = lastRightResult;
					break;
				case CENTER_CAM:
					result = lastCenterResult;
					break;
				default:
					continue;
			}

			// Set last reference pose 
			estimator.setReferencePose(currentPose);

			// Get latest estimate result
			Optional<EstimatedRobotPose> poseResult = estimator.update(result);

			// Log individual camera estimates
			if (poseResult.isPresent()) {
				EstimatedRobotPose estimate = poseResult.get();
				validEstimates.add(estimate);

				// Log each camera's estimate
				Logger.recordOutput("Vision/" + cameraName + "/EstimatedPose", estimate.estimatedPose);
				Logger.recordOutput(
						"Vision/" + cameraName + "/TimestampSeconds", estimate.timestampSeconds);
				Logger.recordOutput("Vision/" + cameraName + "/TagCount", estimate.targetsUsed.size());
			}
		}

		// Only update if we have valid estimates
		if (!validEstimates.isEmpty()) {
			// Average the poses
			double avgX = 0;
			double avgY = 0;
			double avgRotationRadians = 0;
			double avgTimestamp = 0;

			for (EstimatedRobotPose estimate : validEstimates) {
				Pose3d pose = estimate.estimatedPose;
				avgX += pose.getX();
				avgY += pose.getY();
				avgRotationRadians += pose.getRotation().getZ();
				avgTimestamp += estimate.timestampSeconds;
			}

			int count = validEstimates.size();
			avgX /= count;
			avgY /= count;
			avgRotationRadians /= count;
			avgTimestamp /= count;

			// Create averaged pose
			Pose3d averagedPose =
					new Pose3d(avgX, avgY, 0.0, new Rotation3d(0.0, 0.0, avgRotationRadians));

			// Create new estimated pose with averaged values
			lastEstimatedPose =
					Optional.of(
							new EstimatedRobotPose(
									averagedPose,
									avgTimestamp,
									validEstimates.get(0)
											.targetsUsed, // Using first estimate's targets for simplicity
									validEstimates.get(0).strategy));

			// Log the final averaged pose
			Logger.recordOutput("Vision/AveragedEstimatedPose", lastEstimatedPose.get().estimatedPose);
			Logger.recordOutput("Vision/CamerasUsedInEstimate", count);
		} else {
			lastEstimatedPose = Optional.empty();
		}
	}

	@Override
	public Optional<EstimatedRobotPose> getEstimatedGlobalPose() {
		return lastEstimatedPose;
	}
}
