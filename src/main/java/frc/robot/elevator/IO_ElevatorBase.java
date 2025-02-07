// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.elevator;

public interface IO_ElevatorBase {

	public static class ElevatorInputs {

		public double heightM = 0.0;
		public double velocityMPS = 0.0;
		public double accelerationMPS2 = 0.0;
		public double setpointM = 0.0;

		public double leftMotorVoltage = 0.0;
		public double rightMotorVoltage = 0.0;

		public double leftMotorCurrent = 0.0;
		public double rightMotorCurrent = 0.0;

		public double leftMotorPower = 0.0;
		public double rightMotorPower = 0.0;
	}

	/** Updates the set of loggable inputs. */
	public void updateInputs(ElevatorInputs inputs);

	public void setVoltage(double voltage);

	public void setPositionM(double newPositionM);
}
