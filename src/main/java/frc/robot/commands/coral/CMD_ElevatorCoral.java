// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands.coral;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.generic.CMD_Superstructure;
import frc.robot.superstructure.SUB_Superstructure;
import frc.robot.superstructure.SuperstructureState;

public class CMD_ElevatorCoral extends SequentialCommandGroup {
	public CMD_ElevatorCoral(SUB_Superstructure superstructure, boolean isElevatorUp) {
		SuperstructureState.State currentState = superstructure.getCurrentSuperstructureState();
		SuperstructureState.State newState = determineNewState(currentState, isElevatorUp);

		// Add the command to update the superstructure state
		addCommands(new CMD_Superstructure(superstructure, newState));
	}

	private SuperstructureState.State determineNewState(
			SuperstructureState.State currentState, boolean isElevatorUp) {
		if (isElevatorUp) {
			// Elevator Up Logic
			switch (currentState) {
				case L1_SCORING:
					return SuperstructureState.State.L2_SCORING;
				case L2_SCORING:
					return SuperstructureState.State.L3_SCORING;
				case L3_SCORING:
					return SuperstructureState.State.L4_SCORING;
				default:
					return currentState;
			}
		} else {
			// Elevator Down Logic
			switch (currentState) {
				case L4_SCORING:
					return SuperstructureState.State.L3_SCORING;
				case L3_SCORING:
					return SuperstructureState.State.L2_SCORING;
				case L2_SCORING:
					return SuperstructureState.State.L1_SCORING;
				default:
					return currentState;
			}
		}
	}
}
