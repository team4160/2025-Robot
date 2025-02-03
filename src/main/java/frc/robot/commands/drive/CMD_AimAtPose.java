// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.swerve.SUB_Swerve;

public class CMD_AimAtPose extends Command {
	private final SUB_Swerve swerve;
	private final Pose2d targetPose;
	private final double tolerance;

	/**
	 * Creates a command to aim the robot at a specific pose on the field.
	 *
	 * @param swerve The swerve drive subsystem
	 * @param targetPose The pose to aim at
	 * @param tolerance Tolerance in degrees for considering the aim complete
	 */
	public CMD_AimAtPose(SUB_Swerve swerve, Pose2d targetPose, double tolerance) {
		this.swerve = swerve;
		this.targetPose = targetPose;
		this.tolerance = tolerance;
		addRequirements(swerve);
	}

	@Override
	public void execute() {
		// Calculate target yaw based on relative position to target pose
		Translation2d relativeTrl = targetPose.relativeTo(swerve.getPose()).getTranslation();
		Rotation2d targetYaw =
				new Rotation2d(relativeTrl.getX(), relativeTrl.getY()).plus(swerve.getHeading());

		// Calculate rotation to face the target
		double rotationVelocity =
				swerve
						.getSwerveController()
						.headingCalculate(swerve.getHeading().getRadians(), targetYaw.getRadians());

		// Create field-relative speeds with rotation only
		ChassisSpeeds fieldRelativeSpeeds =
				ChassisSpeeds.fromFieldRelativeSpeeds(0, 0, rotationVelocity, swerve.getHeading());

		swerve.drive(fieldRelativeSpeeds);
	}

	@Override
	public boolean isFinished() {
		Translation2d relativeTrl = targetPose.relativeTo(swerve.getPose()).getTranslation();
		Rotation2d targetYaw =
				new Rotation2d(relativeTrl.getX(), relativeTrl.getY()).plus(swerve.getHeading());

		return Math.abs(targetYaw.minus(swerve.getHeading()).getDegrees()) < tolerance;
	}

	@Override
	public void end(boolean interrupted) {
		swerve.drive(new ChassisSpeeds());
	}
}
