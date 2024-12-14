// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import frc.robot.constants.VisionConstants;
import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface IO_VisionBase {
	@AutoLog
	public static class VisionInputs implements LoggableInputs {

		public CameraState frontCamera =
				new CameraState("Front", VisionConstants.getCameraTransform("Front"));
		public CameraState leftCamera =
				new CameraState("Left", VisionConstants.getCameraTransform("Left"));
		public CameraState rightCamera =
				new CameraState("Right", VisionConstants.getCameraTransform("Right"));

		@Override
		public void toLog(LogTable table) {
			frontCamera.toLog(table);
			leftCamera.toLog(table);
			rightCamera.toLog(table);
		}

		@Override
		public void fromLog(LogTable table) {
			frontCamera.fromLog(table);
			leftCamera.fromLog(table);
			rightCamera.fromLog(table);
		}
	}

	/** Updates the inputs with the current values */
	public void updateInputs(VisionInputs inputs);
}
