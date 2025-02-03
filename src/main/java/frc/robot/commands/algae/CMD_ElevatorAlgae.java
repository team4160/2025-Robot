// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands.algae;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.generic.CMD_Superstructure;
import frc.robot.superstructure.SUB_Superstructure;
import frc.robot.superstructure.SuperstructureState;

public class CMD_ElevatorAlgae extends SequentialCommandGroup {
	public CMD_ElevatorAlgae(SUB_Superstructure superstructure, boolean isElevatorUp) {
		SuperstructureState.State currentState = superstructure.getCurrentSuperstructureState();
		SuperstructureState.State newState = determineNewState(currentState, isElevatorUp);

		// Add the command to update the superstructure state
		addCommands(new CMD_Superstructure(superstructure, newState));
	}

	private SuperstructureState.State determineNewState(
			SuperstructureState.State currentState, boolean isElevatorUp) {
		if (isElevatorUp) {
			// Elevator Up Logic for Algae states
			switch (currentState) {
				case ALGAE_GROUND:
					return SuperstructureState.State.ALGAE_REMOVAL;
				case ALGAE_REMOVAL:
					return SuperstructureState.State.ALGAE_PROCESSOR;
				case ALGAE_PROCESSOR:
					return SuperstructureState.State.ALGAE_BARGE;
				default:
					return currentState;
			}
		} else {
			// Elevator Down Logic for Algae states
			switch (currentState) {
				case ALGAE_BARGE:
					return SuperstructureState.State.ALGAE_PROCESSOR;
				case ALGAE_PROCESSOR:
					return SuperstructureState.State.ALGAE_REMOVAL;
				case ALGAE_REMOVAL:
					return SuperstructureState.State.ALGAE_GROUND;
				default:
					return currentState;
			}
		}
	}
}
