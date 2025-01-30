// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class SUB_Intake extends SubsystemBase {

	public enum State {
		STOWED(-15, 0.0),
		CORAL_STATION(90, 0.3),
		ALGAE_GROUND(45, 1.0),
		ALGAE_REMOVAL(85, -0.8),
		ALGAE_PROCESSOR(85, -1.0),
		ALGAE_BARGE(135, -0.25),
		L1_SCORING(30, -0.8),
		L2_L3_SCORING(45, -0.8),
		L4_SCORING(30, -0.8);

		private final int deg;
		private final double speed;

		State(int deg, double speed) {
			this.deg = deg;
			this.speed = speed;
		}

		public int getDeg() {
			return deg;
		}

		public double getSpeed() {
			return speed;
		}
	}

	private final IO_IntakeBase io;

	private final IO_IntakeBase.IntakeInputs inputs = new IO_IntakeBase.IntakeInputs();

	public SUB_Intake(IO_IntakeBase io) {
		this.io = io;
	}

	@Override
	public void periodic() {

		// Update inputs
		io.updateInputs(inputs);

		// Process inputs
		Logger.processInputs("Intake", inputs);
	}

	public Command setState(State state) {
		return new InstantCommand(
				() -> {
					io.setArmAngle(state.getDeg());
					io.setIntakeSpeed(state.getSpeed());
				});
	}
}
