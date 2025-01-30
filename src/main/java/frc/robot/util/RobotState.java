// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.util;

public class RobotState {

	public enum State {
		STOWED(0.1),
		CORAL_STATION(1.0),
		ALGAE_GROUND(0.1),
		ALGAE_PROCESSOR(0.1),
		ALGAE_BARGE(2.35),
		L1_SCORING(0.5),
		L2_SCORING(0.8),
		L3_SCORING(1.2),
		L4_SCORING(1.85);

		private final double heightM;

		State(double heightM) {
			this.heightM = heightM;
		}

		public double getHeightM() {
			return heightM;
		}
	}
}
