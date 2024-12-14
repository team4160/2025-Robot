// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.constants.VisionConstants;
import frc.robot.util.AlertManager;

import java.io.IOException;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;

public class IO_VisionReal implements IO_VisionBase {
	// Cameras
	private final PhotonCamera cameraOne;
	private final PhotonCamera cameraTwo;
	private final PhotonCamera cameraThree;

	// Camera States
	private final CameraState cameraOneState;
	private final CameraState cameraTwoState;
	private final CameraState cameraThreeState;

	// Pose Estimators
	private PhotonPoseEstimator estimatorOne;
	private PhotonPoseEstimator estimatorTwo;
	private PhotonPoseEstimator estimatorThree;

	private AprilTagFieldLayout fieldLayout;

	public IO_VisionReal() {
		// Initialize cameras
		cameraOne = new PhotonCamera("Front");
		cameraTwo = new PhotonCamera("Left");
		cameraThree = new PhotonCamera("Right");

		// Initialize camera states
		cameraOneState = new CameraState("Front", VisionConstants.getCameraTransform("Front"));
		cameraTwoState = new CameraState("Left", VisionConstants.getCameraTransform("Left"));
		cameraThreeState = new CameraState("Right", VisionConstants.getCameraTransform("Right"));

		initializeEstimators();
	}

	private void initializeEstimators() {
		try {
			fieldLayout = AprilTagFieldLayout.loadFromResource(
				AprilTagFields.k2024Crescendo.m_resourceFile);

			// Initialize pose estimators
			estimatorOne = new PhotonPoseEstimator(
				fieldLayout,
				PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
				VisionConstants.getCameraTransform("Front"));
			
			estimatorTwo = new PhotonPoseEstimator(
				fieldLayout,
				PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
				VisionConstants.getCameraTransform("Left"));

			estimatorThree = new PhotonPoseEstimator(
				fieldLayout,
				PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
				VisionConstants.getCameraTransform("Right"));

			// Set fallback strategies
			estimatorOne.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
			estimatorTwo.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
			estimatorThree.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);

			AlertManager.setAlert(AlertManager.Alerts.APRILTAG_FIELD_LOAD_ERROR, false);
		} catch (IOException e) {
			AlertManager.setAlert(AlertManager.Alerts.APRILTAG_FIELD_LOAD_ERROR, true);
			DriverStation.reportError("Failed to load AprilTag field layout!", e.getStackTrace());
		}
	}

	private void updateCamera(
			PhotonCamera camera,
			PhotonPoseEstimator estimator,
			CameraState state) {

		// Get the latest result from the camera
		var result = camera.getLatestResult();
		
		// Update all state at once
		state.updateState(
			camera.isConnected(),
			!camera.getDriverMode(),
			result.hasTargets(),
			result.getTimestampSeconds(),
			result.getTargets());
	}

	@Override
	public void updateInputs(VisionInputs inputs) {
		// Update all cameras
		updateCamera(cameraOne, estimatorOne, cameraOneState);
		updateCamera(cameraTwo, estimatorTwo, cameraTwoState);
		updateCamera(cameraThree, estimatorThree, cameraThreeState);

		// Update inputs
		inputs.frontCamera = cameraOneState;
		inputs.leftCamera = cameraTwoState;
		inputs.rightCamera = cameraThreeState;
	}

}
