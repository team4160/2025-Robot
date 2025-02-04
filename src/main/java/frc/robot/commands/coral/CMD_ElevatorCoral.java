// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands.coral;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.superstructure.SUB_Superstructure;
import frc.robot.superstructure.SuperstructureState;

public class CMD_ElevatorCoral extends Command {
	private final SUB_Superstructure superstructure;
	private final boolean isMovingUp;

	public CMD_ElevatorCoral(SUB_Superstructure superstructure, boolean isMovingUp) {
		this.superstructure = superstructure;
		this.isMovingUp = isMovingUp;
		addRequirements(superstructure);
	}

	@Override
	public void initialize() {
		SuperstructureState.State currentState = superstructure.getCurrentSuperstructureState();
		SuperstructureState.State newState = SuperstructureState.State.L1_SCORING;

		if (isMovingUp) {
			if (currentState == SuperstructureState.State.L1_SCORING) {
				newState = SuperstructureState.State.L2_SCORING;
			} else if (currentState == SuperstructureState.State.L2_SCORING) {
				newState = SuperstructureState.State.L3_SCORING;
			} else if (currentState == SuperstructureState.State.L3_SCORING) {
				newState = SuperstructureState.State.L4_SCORING;
			}
		} else {
			if (currentState == SuperstructureState.State.L4_SCORING) {
				newState = SuperstructureState.State.L3_SCORING;
			} else if (currentState == SuperstructureState.State.L3_SCORING) {
				newState = SuperstructureState.State.L2_SCORING;
			} else if (currentState == SuperstructureState.State.L2_SCORING) {
				newState = SuperstructureState.State.L1_SCORING;
			}
		}

		superstructure.updateSuperstructureState(newState);
	}

	@Override
	public void execute() {}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void end(boolean interrupted) {}
}
