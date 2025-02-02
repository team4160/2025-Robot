// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.state.GlobalRobotState;
import frc.robot.state.Superstructure;

public class CMD_ElevatorUpCoral extends Command {
	private final Superstructure superstructure;

	public CMD_ElevatorUpCoral(Superstructure superstructure) {
		this.superstructure = superstructure;
		addRequirements(superstructure);
	}

	@Override
	public void initialize() {

		// Gets current robot state
		GlobalRobotState.State currentState = superstructure.getCurrentGlobalState();

		// Create new state for robot to go to. Default is L1_SCORING
		GlobalRobotState.State newState = GlobalRobotState.State.L1_SCORING;

		if (currentState == GlobalRobotState.State.L1_SCORING) {
			newState = GlobalRobotState.State.L2_SCORING;

		} else if (currentState == GlobalRobotState.State.L2_SCORING) {
			newState = GlobalRobotState.State.L3_SCORING;

		} else if (currentState == GlobalRobotState.State.L3_SCORING) {
			newState = GlobalRobotState.State.L4_SCORING;
		}

		superstructure.updateGlobalState(newState);
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
