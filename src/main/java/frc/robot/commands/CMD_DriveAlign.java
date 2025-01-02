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
import frc.robot.constants.InputConstants;
import frc.robot.constants.RobotConstants;
import frc.robot.swerve.SUB_Swerve;

public class CMD_DriveAlign extends Command {
	private final SUB_Swerve swerve;
	private final CommandXboxController controller;
	private final InputConstants controllerMap;

	// Values > 1 increase sensitivity
	// Values closer to 1 maintain original sensitivity
	// Values < 1 decrease sensitivity
	private final double ROTATION_SENSITIVITY = 0.5;

	public CMD_DriveAlign(
			SUB_Swerve swerve, CommandXboxController controller, InputConstants controllerMap) {
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

		// Apply forward and strafe inversions from InputConstants
		xVelocity *= controllerMap.forwardInverted ? -1 : 1;
		yVelocity *= controllerMap.strafeInverted ? -1 : 1;

		var alliance = DriverStation.getAlliance();

		// Additional alliance-based inversions
		if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Blue) {
			// Invert x and y velocities for blue alliance
			xVelocity *= -1;
			yVelocity *= -1;
		}

		double rotationVelocity;

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

			// Apply rotation inversion and sensitivity
			rotationVelocity *= controllerMap.rotationInverted ? -1 : 1;
			rotationVelocity *= ROTATION_SENSITIVITY;
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
