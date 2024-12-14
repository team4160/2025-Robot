// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import frc.robot.util.AlertManager;
import frc.robot.util.AlertManager.Alerts;
import java.io.File;
import java.io.IOException;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;

public class IO_SwerveReal implements IO_SwerveBase {
	private static final File SWERVE_JSON_DIRECTORY =
			new File(Filesystem.getDeployDirectory(), "swerve");
	private static final double MAX_SPEED = 4.8; // Meters per second

	private final SwerveDrive swerveDrive;
	private final SwerveInputs inputs = new SwerveInputs();

	public IO_SwerveReal() {
		try {
			swerveDrive = new SwerveParser(SWERVE_JSON_DIRECTORY).createSwerveDrive(MAX_SPEED);
			AlertManager.setAlert(Alerts.SWERVE_CONFIG, false);
		} catch (IOException e) {
			AlertManager.setAlert(Alerts.SWERVE_CONFIG, true);
			throw new RuntimeException(
					"Failed to initialize SwerveDrive. Are there configuration files in the deploy directory?",
					e);
		}
	}

	@Override
	public void updateInputs(SwerveInputs inputs) {
		// Update pose and orientation
		inputs.robotPose = swerveDrive.getPose();
		inputs.gyroYaw = swerveDrive.getYaw();

		// Update module states
		inputs.moduleStates = swerveDrive.getStates();
		inputs.modulePositions = swerveDrive.getModulePositions();

		// Update chassis speeds
		inputs.fieldSpeeds = swerveDrive.getFieldVelocity();
		inputs.robotSpeeds = swerveDrive.getRobotVelocity();

		// Update driving parameters
		inputs.isFieldRelative = this.inputs.isFieldRelative;
		inputs.isOpenLoop = this.inputs.isOpenLoop;
	}

	@Override
	public void drive(Pose2d pose) {
		swerveDrive.drive(
				new Translation2d(
						pose.getX(), pose.getY()), // Create Translation2d from pose in meters per second
				pose.getRotation().getRadians(), // Get rotation per second in radians
				inputs.isFieldRelative,
				inputs.isOpenLoop);
	}

	@Override
	public void resetOdometry(Pose2d pose) {
		swerveDrive.resetOdometry(pose);
	}

	@Override
	public void updateOdometry() {
		swerveDrive.updateOdometry();
	}

	@Override
	public void setFieldRelativeDrive(boolean fieldRelative) {
		inputs.isFieldRelative = fieldRelative;
	}

	@Override
	public void setOpenLoopDrive(boolean openLoop) {
		inputs.isOpenLoop = openLoop;
	}
}
