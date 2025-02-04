// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.superstructure;

public class SuperstructureState {

	public enum State {

		// Generic
		IDLE(0.1, 18, 0.0),
		CLIMB(0.5, 0, 0.0),

		// Coral
		CORAL_STATION(0.38, 32, 0.3),

		L1_SCORING(0.5, 100, 0.0),
		L2_SCORING(0.8, 120, 0.0),
		L3_SCORING(1.4, 120, 0.0),
		L4_SCORING(2.415, 121, 0.0),

		// Algae
		ALGAE_GROUND(0.1, 135, 0.0),
		ALGAE_PROCESSOR(0.1, 90, 0.0),
		ALGAE_BARGE(0.1, 45, -0.25),
		ALGAE_L2(0.7, 90, -0.8),
		ALGAE_L3(1.85, 135, -0.8);

		// Climb
		private final double heightM;
		private final int deg;
		private final double speed;

		State(double heightM, int deg, double speed) {
			this.heightM = heightM;
			this.deg = deg;
			this.speed = speed;
		}

		public double getHeightM() {
			return heightM;
		}

		public int getDeg() {
			return deg;
		}

		public double getSpeed() {
			return speed;
		}
	}
}
