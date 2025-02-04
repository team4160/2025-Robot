// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands.generic;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.intake.SUB_Intake;

public class CMD_IntakeWheel extends Command {
	private final SUB_Intake intake;
	private final double newIntakeWheelSpeed;

	public CMD_IntakeWheel(SUB_Intake intake, double newIntakeWheelSpeed) {
		this.intake = intake;
		this.newIntakeWheelSpeed = newIntakeWheelSpeed;
		addRequirements(intake);
	}

	@Override
	public void initialize() {
		intake.updateLocalWheelSpeed(newIntakeWheelSpeed);
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
