// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.constants.InputConstants;
import frc.robot.constants.RobotConstants;
import frc.robot.swerve.SUB_Swerve;

public class CMD_Drive extends Command {
	private final SUB_Swerve swerve;
	private final CommandXboxController controller;
	private final InputConstants controllerMap;

	public CMD_Drive(
			SUB_Swerve swerve, CommandXboxController controller, InputConstants controllerMap) {
		this.swerve = swerve;
		this.controller = controller;
		this.controllerMap = controllerMap;
		addRequirements(swerve);
	}

	@Override
	public void initialize() {
		// Set motor brake mode when command starts
		swerve.setMotorBrake(true);
	}

	@Override
	public void execute() {
		// Get joystick inputs with deadband applied
		double xVelocity =
				-MathUtil.applyDeadband(
						controller.getRawAxis(controllerMap.forwardAxis) * RobotConstants.MAX_SPEED,
						controllerMap.driveDeadband);

		double yVelocity =
				-MathUtil.applyDeadband(
						controller.getRawAxis(controllerMap.strafeAxis) * RobotConstants.MAX_SPEED,
						controllerMap.driveDeadband);

		double rotationVelocity =
				-MathUtil.applyDeadband(
						controller.getRawAxis(controllerMap.rotationAxis) * RobotConstants.MAX_SPEED,
						controllerMap.driveDeadband);

		xVelocity *= controllerMap.forwardInverted ? -1 : 1;
		yVelocity *= controllerMap.strafeInverted ? -1 : 1;
		rotationVelocity *= controllerMap.rotationInverted ? -1 : 1;

		// Create translation vector from x and y inputs
		Translation2d translation = new Translation2d(xVelocity, yVelocity);

		// Drive using the swerve subsystem's field-relative drive method
		swerve.drive(translation, rotationVelocity, true);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		// Stop the robot when the command ends
		swerve.drive(new Translation2d(), 0, true);
		// Disable motor brake mode
		swerve.setMotorBrake(false);
	}
}
