// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.constants;

public enum InputConstants {
	XBOX(
			4, // Forward/Back Axis
			3, // Left/Right Axis
			0, // Rotation Axis
			0.2, // Deadband
			false, // Forward Inverted
			false, // Left/Right Inverted
			false // Rotation Inverted
			),
	TX16S_MAIN(
			1, // Forward/Back Axis
			0, // Left/Right Axis
			3, // Rotation Axis
			0.1, // Deadband
			false, // Forward Inverted
			true, // Left/Right Inverted
			true // Rotation Inverted
			),
	KEYBOARD(
			0, // Forward/Back Axis
			1, // Left/Right Axis
			2, // Rotation Axis
			0.1, // Deadband
			false, // Forward Inverted
			false, // Left/Right Inverted
			false // Rotation Inverted
			);

	public final int forwardAxis;
	public final boolean forwardInverted;
	public final int strafeAxis;
	public final boolean strafeInverted;
	public final int rotationAxis;
	public final boolean rotationInverted;
	public final double driveDeadband;

	InputConstants(
			int forwardAxis,
			int strafeAxis,
			int rotationAxis,
			double driveDeadband,
			boolean forwardInverted,
			boolean strafeInverted,
			boolean rotationInverted) {
		this.forwardAxis = forwardAxis;
		this.strafeAxis = strafeAxis;
		this.rotationAxis = rotationAxis;
		this.driveDeadband = driveDeadband;
		this.forwardInverted = forwardInverted;
		this.strafeInverted = strafeInverted;
		this.rotationInverted = rotationInverted;
	}

	// Operator Controller Constants
	public static final class OperatorController {
		// Button Mappings
		public static final int ELEVATOR_CORAL_UP_POV = 0;
		public static final int ELEVATOR_CORAL_DOWN_POV = 180;
		public static final int STOWED_BUTTON = 2; // B Button
		public static final int INTAKE_CORAL_BUTTON = 1; // A Button
		public static final int CLIMB_BUTTON = 6; // Right Bumper
	}
}
