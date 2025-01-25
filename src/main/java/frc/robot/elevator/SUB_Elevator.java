// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.elevator;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class SUB_Elevator extends SubsystemBase {

	public enum State {
		STOWED(0.0),
		CORAL_STATION(1.0),
		ALGAE_GROUND(0.1),
		ALGAE_PROCESSOR(0.1),
		ALGAE_BARGE(2.9),
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

	private final IO_ElevatorBase io;

	private final IO_ElevatorBase.ElevatorInputs inputs = new IO_ElevatorBase.ElevatorInputs();

	public SUB_Elevator(IO_ElevatorBase io) {
		this.io = io;
	}

	@Override
	public void periodic() {

		// Update inputs
		io.updateInputs(inputs);

		// Process inputs
		Logger.processInputs("Elevator", inputs);
	}

	public Command setState(State state) {
		return new InstantCommand(
				() -> {
					io.setPositionM(state.getHeightM());
				});
	}
}
