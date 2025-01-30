// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.elevator.SUB_Elevator;
import frc.robot.misc.RobotState;
import frc.robot.misc.SUB_Led;

public class CMD_ElevatorDownCoral extends Command {
	private final SUB_Elevator elevator;
	private final SUB_Led led;

	public CMD_ElevatorDownCoral(SUB_Elevator elevator, SUB_Led led) {
		this.elevator = elevator;
		this.led = led;

		addRequirements(elevator);
	}

	@Override
	public void initialize() {

		RobotState.State currentState = elevator.getCurrentState();

		RobotState.State newState = RobotState.State.L1_SCORING;

		if (currentState == RobotState.State.L4_SCORING) {
			newState = RobotState.State.L3_SCORING;

		} else if (currentState == RobotState.State.L3_SCORING) {
			newState = RobotState.State.L2_SCORING;

		} else if (currentState == RobotState.State.L2_SCORING) {
			newState = RobotState.State.L1_SCORING;
		}

		elevator.updateState(newState);
		led.updateState(newState);
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
