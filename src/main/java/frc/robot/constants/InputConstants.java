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
			0.1 // Deadband
			),
	TX16S_MAIN(
			1, // Forward/Back Axis
			0, // Left/Right Axis
			3, // Rotation Axis
			0.1 // Deadband
			),
	KEYBOARD(
			0, // Forward/Back Axis
			1, // Left/Right Axis
			2, // Rotation Axis
			0.1 // Deadband
			);

	public final int forwardAxis;
	public final int strafeAxis;
	public final int rotationAxis;
	public final double driveDeadband;

	InputConstants(int forwardAxis, int strafeAxis, int rotationAxis, double driveDeadband) {
		this.forwardAxis = forwardAxis;
		this.strafeAxis = strafeAxis;
		this.rotationAxis = rotationAxis;
		this.driveDeadband = driveDeadband;
	}
}
