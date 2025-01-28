// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.elevator.SUB_Elevator;

public class CMD_SetElevator extends Command {
	private final SUB_Elevator elevator;
	private final SUB_Elevator.State state;

	public CMD_SetElevator(SUB_Elevator elevator, SUB_Elevator.State state) {
		this.elevator = elevator;
		this.state = state;
		addRequirements(elevator);
	}

	@Override
	public void initialize() {
		elevator.updateState(state);
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
