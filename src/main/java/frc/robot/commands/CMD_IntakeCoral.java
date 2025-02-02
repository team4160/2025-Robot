// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.state.GlobalRobotState;
import frc.robot.state.Superstructure;

public class CMD_IntakeCoral extends Command {
	private final Superstructure superstructure;

	public CMD_IntakeCoral(Superstructure superstructure) {
		this.superstructure = superstructure;
		addRequirements(superstructure);
	}

	@Override
	public void initialize() {
		superstructure.updateGlobalState(GlobalRobotState.State.CORAL_STATION);
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
