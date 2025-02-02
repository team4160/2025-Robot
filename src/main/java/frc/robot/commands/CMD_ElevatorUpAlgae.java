// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.state.GlobalRobotState;
import frc.robot.state.Superstructure;

public class CMD_ElevatorUpAlgae extends Command {
	private final Superstructure superstruture;

	public CMD_ElevatorUpAlgae(Superstructure superstructure) {
		this.superstruture = superstructure;

		addRequirements(superstructure);
	}

	@Override
	public void initialize() {

		GlobalRobotState.State currentState = superstruture.getCurrentGlobalState();

		GlobalRobotState.State newState = GlobalRobotState.State.ALGAE_PROCESSOR;

		if (currentState == GlobalRobotState.State.ALGAE_PROCESSOR) {
			newState = GlobalRobotState.State.ALGAE_GROUND;

		} else if (currentState == GlobalRobotState.State.L3_SCORING) {
			newState = GlobalRobotState.State.L2_SCORING;

		} else if (currentState == GlobalRobotState.State.L2_SCORING) {
			newState = GlobalRobotState.State.L1_SCORING;
		}

		superstruture.updateGlobalState(newState);
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
