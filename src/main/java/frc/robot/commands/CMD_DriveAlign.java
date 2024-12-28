// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.constants.RobotConstants;
import frc.robot.swerve.SUB_Swerve;
import frc.robot.util.InputMap;

public class CMD_DriveAlign extends Command {
	private final SUB_Swerve swerve;
	private final CommandXboxController controller;
	private final InputMap controllerMap;

	public CMD_DriveAlign(
			SUB_Swerve swerve, CommandXboxController controller, InputMap controllerMap) {
		this.swerve = swerve;
		this.controller = controller;
		this.controllerMap = controllerMap;
		addRequirements(swerve);
	}

	@Override
	public void initialize() {
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

		double rotationVelocity;

		var alliance = DriverStation.getAlliance();

		// Check for alignment controls
		if (controller.leftBumper().getAsBoolean() && alliance.isPresent()) {
			// Speaker alignment with left bumper
			rotationVelocity =
					swerve
							.getSwerveController()
							.headingCalculate(
									swerve.getHeading().getRadians(), swerve.getSpeakerYaw().getRadians());
		} else if (controller.rightBumper().getAsBoolean() && alliance.isPresent()) {
			// AMP alignment with right bumper
			rotationVelocity =
					swerve
							.getSwerveController()
							.headingCalculate(swerve.getHeading().getRadians(), swerve.getAmpYaw().getRadians());
		} else {
			// Manual rotation control if no alignment buttons pressed
			rotationVelocity =
					-MathUtil.applyDeadband(
							controller.getRawAxis(controllerMap.rotationAxis)
									* swerve.getMaximumAngularVelocity(),
							controllerMap.driveDeadband);
		}

		// Create field-relative ChassisSpeeds
		ChassisSpeeds fieldRelativeSpeeds =
				ChassisSpeeds.fromFieldRelativeSpeeds(
						xVelocity, yVelocity, rotationVelocity, swerve.getHeading());

		// Drive using field-relative speeds
		swerve.drive(fieldRelativeSpeeds);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		swerve.drive(new ChassisSpeeds());
		swerve.setMotorBrake(false);
	}
}
