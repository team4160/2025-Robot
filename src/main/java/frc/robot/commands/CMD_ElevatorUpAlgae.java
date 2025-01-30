// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.elevator.SUB_Elevator;
import frc.robot.misc.RobotState;

public class CMD_ElevatorUpAlgae extends Command {
	private final SUB_Elevator elevator;

	public CMD_ElevatorUpAlgae(SUB_Elevator elevator) {
		this.elevator = elevator;

		addRequirements(elevator);
	}

	@Override
	public void initialize() {

		RobotState.State currentState = elevator.getCurrentState();

		RobotState.State newState = RobotState.State.ALGAE_PROCESSOR;

		if (currentState == RobotState.State.ALGAE_PROCESSOR) {
			newState = RobotState.State.ALGAE_GROUND;

		} else if (currentState == RobotState.State.L3_SCORING) {
			newState = RobotState.State.L2_SCORING;

		} else if (currentState == RobotState.State.L2_SCORING) {
			newState = RobotState.State.L1_SCORING;
		}

		elevator.updateState(newState);
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
