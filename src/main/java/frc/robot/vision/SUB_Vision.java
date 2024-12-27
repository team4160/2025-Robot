// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.VisionConstants;

@SuppressWarnings("unused")
public class SUB_Vision extends SubsystemBase {
	// Vision IO interface
	private final IO_VisionBase io;

	// Camera instances
	private final CameraInstance frontCamera;
	private final CameraInstance leftCamera;
	private final CameraInstance rightCamera;

	// Inputs for logging
	private final IO_VisionBase.VisionInputs inputs = new IO_VisionBase.VisionInputs();

	public SUB_Vision(IO_VisionBase io) {

		this.io = io;

		// Initialize camera instances (same as in IO_VisionReal)
		frontCamera =
				new CameraInstance("FrontCamera", VisionConstants.getCameraTransform("FrontCamera"));
		leftCamera = new CameraInstance("LeftCamera", VisionConstants.getCameraTransform("LeftCamera"));
		rightCamera =
				new CameraInstance("RightCamera", VisionConstants.getCameraTransform("RightCamera"));
	}

	@Override
	public void periodic() {
		// Update camera states
		io.updateInputs(inputs);
	}

	// Getter methods for camera states
	public CameraState getFrontCameraState() {
		return inputs.frontCameraState;
	}

	public CameraState getLeftCameraState() {
		return inputs.leftCameraState;
	}

	public CameraState getRightCameraState() {
		return inputs.rightCameraState;
	}
}
