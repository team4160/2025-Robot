// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.superstructure;

public class SuperstructureState {

	public enum State {

		// Generic
		IDLE(0.05, -15, 0.0),
		CLIMB(0.5, 0, 0.0),


		// Coral
		CORAL_STATION(1.0, 120, 0.8),
		L1_SCORING(0.5, 30, -0.8),
		L2_SCORING(0.8, 0, 0.0),
		L3_SCORING(1.2, 0, 0.0),
		L2_L3_SCORING(0.0, 45, -0.8),
		L4_SCORING(1.85, 30, -0.8),

		// Algae
		ALGAE_GROUND(0.1, 45, -1.0),
		ALGAE_PROCESSOR(0.1, 85, -1.0),
		ALGAE_BARGE(2.35, 135, -0.25),
		ALGAE_L2(0.0, 85, -0.8),
		ALGAE_L3(0.0, 85, -0.8);

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
