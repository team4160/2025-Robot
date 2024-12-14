// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.swerve.SUB_Swerve;
import frc.robot.util.InputMap;

public class CMD_Drive extends Command {
	private final SUB_Swerve swerve;
	private final CommandXboxController controller;
	private final InputMap controllerMap;

	public CMD_Drive(SUB_Swerve swerve, CommandXboxController controller, InputMap controllerMap) {
		this.swerve = swerve;
		this.controller = controller;
		this.controllerMap = controllerMap;
		addRequirements(swerve);
	}

	@Override
	public void execute() {
		double xVelocity =
				-MathUtil.applyDeadband(
						controller.getRawAxis(controllerMap.forwardAxis), controllerMap.driveDeadband);

		double yVelocity =
				-MathUtil.applyDeadband(
						controller.getRawAxis(controllerMap.strafeAxis), controllerMap.driveDeadband);

		double rotationVelocity =
				-MathUtil.applyDeadband(
						controller.getRawAxis(controllerMap.rotationAxis), controllerMap.driveDeadband);

		swerve.driveRobot(xVelocity, yVelocity, rotationVelocity);
	}
}
