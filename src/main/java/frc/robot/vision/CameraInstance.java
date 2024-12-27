// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.util.AlertManager;
import java.io.IOException;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;

public class CameraInstance {

	private PhotonCamera camera;
	private PhotonPoseEstimator estimator;
	private AprilTagFieldLayout field;

	private CameraState currentState;

	public CameraInstance(String cameraName, Transform3d robotToCamera) {
		// Initialize camera
		camera = new PhotonCamera(cameraName);

		try {
			field = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2024Crescendo.m_resourceFile);
			AlertManager.setAlert(AlertManager.Alerts.APRILTAG_FIELD_LOAD_ERROR, false);
		} catch (IOException e) {
			AlertManager.setAlert(AlertManager.Alerts.APRILTAG_FIELD_LOAD_ERROR, true);
			e.printStackTrace();
		}

		estimator =
				new PhotonPoseEstimator(field, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, robotToCamera);

		if (estimator == null) {
			AlertManager.setAlert(AlertManager.Alerts.CAMERA_FAILED_CREATION, true);
		} else {
			AlertManager.setAlert(AlertManager.Alerts.CAMERA_FAILED_CREATION, false);
		}

		estimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);

		// Initialize currentState with camera name
		currentState = new CameraState(cameraName);
	}

	public void update() {
		var results = camera.getAllUnreadResults();

		// Process each result
		for (var result : results) {
			// Update all state at once
			currentState.updateState(
					camera.isConnected(),
					result.hasTargets(),
					result.getTimestampSeconds(),
					result.getBestTarget(),
					result.getTargets());
		}
	}

	public CameraState getState() {
		return currentState;
	}
}
