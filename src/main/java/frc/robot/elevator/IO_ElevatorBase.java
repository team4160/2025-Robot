// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.elevator;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

///
///
/// //
public interface IO_ElevatorBase {

	@AutoLog
	public static class ElevatorInputs implements LoggableInputs {

		public double heightM = 0.0;
		public double velocityMPS = 0.0;
		public double leftMotorCurrent = 0.0;
		public double rightMotorCurrent = 0.0;

		@Override
		public void toLog(LogTable table) {
			table.put("heightM", heightM);
			table.put("velocityMPS", velocityMPS);
			table.put("leftMotorCurrent", leftMotorCurrent);
			table.put("rightMotorCurrent", rightMotorCurrent);
		}

		@Override
		public void fromLog(LogTable table) {
			heightM = table.get("heightM", heightM);
			velocityMPS = table.get("velocityMPS", velocityMPS);
			leftMotorCurrent = table.get("leftMotorCurrent", leftMotorCurrent);
			rightMotorCurrent = table.get("rightMotorCurrent", rightMotorCurrent);
		}
	}

	/** Updates the set of loggable inputs. */
	public void updateInputs(ElevatorInputs inputs);

	public void setPositionM(double positionM);
}
