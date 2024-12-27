// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import frc.robot.constants.VisionConstants;

public class IO_VisionReal implements IO_VisionBase {
	private final CameraInstance frontCamera;
	private final CameraInstance leftCamera;
	private final CameraInstance rightCamera;

	public IO_VisionReal() {
		frontCamera =
				new CameraInstance("FrontCamera", VisionConstants.getCameraTransform("FrontCamera"));
		leftCamera = new CameraInstance("LeftCamera", VisionConstants.getCameraTransform("LeftCamera"));
		rightCamera =
				new CameraInstance("RightCamera", VisionConstants.getCameraTransform("RightCamera"));
	}

	@Override
	public void updateInputs(VisionInputs inputs) {
		// Update camera states
		frontCamera.update();
		leftCamera.update();
		rightCamera.update();

		// Transfer states to inputs
		inputs.frontCameraState = frontCamera.getState();
		inputs.leftCameraState = leftCamera.getState();
		inputs.rightCameraState = rightCamera.getState();
	}
}
