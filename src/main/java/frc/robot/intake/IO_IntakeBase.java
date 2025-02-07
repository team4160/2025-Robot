// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.intake;

public interface IO_IntakeBase {

	public static class IntakeInputs {

		public double armAngleDegrees = 0.0;
		public double armMotorCurrent = 0.0;
		public double armMotorVoltage = 0.0;
		public double wheelMotorCurrent = 0.0;
		public double wheelRPM = 0.0;
		public boolean toggleSensor = false;
		public double distanceSensorCM = 0.0;
		public double internalPIDSetpoint = 0.0;
	}

	/** Updates the set of loggable inputs. */
	public void updateInputs(IntakeInputs inputs);

	public void setArmAngle(double angle);

	public void setIntakeSpeed(double speed);
}
