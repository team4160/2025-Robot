// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.swerve.SUB_Swerve;

public class CMD_AimAtAprilTag extends Command {
	private final SUB_Swerve swerve;
	private final int tagId;
	private final double tolerance;

	/**
	 * Creates a command to aim the robot at a specific AprilTag.
	 *
	 * @param swerve The swerve drive subsystem
	 * @param tagId The ID of the AprilTag to aim at
	 * @param tolerance Tolerance in degrees for considering the aim complete
	 */
	public CMD_AimAtAprilTag(SUB_Swerve swerve, int tagId, double tolerance) {
		this.swerve = swerve;
		this.tagId = tagId;
		this.tolerance = tolerance;
		addRequirements(swerve);
	}

	@Override
	public void execute() {
		Rotation2d targetYaw = swerve.getAprilTagYaw(tagId);

		// If target yaw is null, tag wasn't found - stop rotating
		if (targetYaw == null) {
			swerve.drive(new ChassisSpeeds());
			return;
		}

		// Calculate rotation to face the tag
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
		Rotation2d targetYaw = swerve.getAprilTagYaw(tagId);

		// Finish if tag not found or within tolerance
		if (targetYaw == null) {
			return true;
		}

		return Math.abs(targetYaw.minus(swerve.getHeading()).getDegrees()) < tolerance;
	}

	@Override
	public void end(boolean interrupted) {
		swerve.drive(new ChassisSpeeds());
	}
}
