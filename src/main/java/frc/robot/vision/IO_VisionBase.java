// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface IO_VisionBase {

	class VisionInputs implements LoggableInputs {
		public CameraState frontCameraState = new CameraState("Front");
		public CameraState leftCameraState = new CameraState("Left");
		public CameraState rightCameraState = new CameraState("Right");

		@Override
		public void toLog(LogTable table) {
			logCameraState(table, "/FrontCamera", frontCameraState);
			logCameraState(table, "/LeftCamera", leftCameraState);
			logCameraState(table, "/RightCamera", rightCameraState);
		}

		private void logCameraState(LogTable table, String prefix, CameraState state) {
			table.put(prefix + "/hasTarget", state.getHasTarget());
			table.put(prefix + "/isConnected", state.getIsConnected());
			table.put(prefix + "/lastFrameTimestamp", state.getLastFrameTimestamp());

			// Best target details
			table.put(prefix + "/bestTarget/id", state.getBestTargetId());
			table.put(prefix + "/bestTarget/yaw", state.getBestTargetYaw());
			table.put(prefix + "/bestTarget/pitch", state.getBestTargetPitch());

			// Additional target count
			table.put(prefix + "/targetCount", state.getTrackedTargets().size());
		}

		@Override
		public void fromLog(LogTable table) {
			restoreCameraState(table, "/FrontCamera", frontCameraState);
			restoreCameraState(table, "/LeftCamera", leftCameraState);
			restoreCameraState(table, "/RightCamera", rightCameraState);
		}

		private void restoreCameraState(LogTable table, String prefix, CameraState state) {
			state.updateState(
					table.get(prefix + "/isConnected", false),
					table.get(prefix + "/hasTarget", false),
					table.get(prefix + "/lastFrameTimestamp", 0.0),
					null, // Cannot fully restore PhotonTrackedTarget from log
					null);
		}
	}

	void updateInputs(VisionInputs inputs);
}
