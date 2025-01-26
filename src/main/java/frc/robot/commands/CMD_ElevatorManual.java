// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.elevator.SUB_Elevator;

public class CMD_ElevatorManual extends Command {
	private final SUB_Elevator elevator;
	private final CommandXboxController controller;

	public CMD_ElevatorManual(SUB_Elevator elevator, CommandXboxController controller) {
		this.elevator = elevator;
		this.controller = controller;
		addRequirements(elevator);
	}

	@Override
	public void initialize() {}

	@Override
	public void execute() {

		double volt = controller.getRawAxis(1) * 12;
		SmartDashboard.putNumber(" Command Volt", volt);
		elevator.setVoltage(volt);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		// Stop the robot when the command ends
		elevator.setVoltage(0);
	}
}
