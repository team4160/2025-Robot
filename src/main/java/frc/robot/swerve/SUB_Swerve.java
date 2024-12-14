// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.swerve.IO_SwerveBase.SwerveInputs;
import org.littletonrobotics.junction.Logger;

public class SUB_Swerve extends SubsystemBase {
	private final IO_SwerveBase io;
	public final SwerveInputs inputs = new SwerveInputs();

	public SUB_Swerve(IO_SwerveBase io) {
		this.io = io;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
		io.updateOdometry();
		Logger.processInputs("Swerve", inputs);
	}

	/**
	 * Drive the robot with given velocities
	 *
	 * @param xVelocity X velocity in m/s
	 * @param yVelocity Y velocity in m/s
	 * @param rotationVelocity Angular velocity in rad/s
	 */
	public void driveRobot(double xVelocity, double yVelocity, double rotationVelocity) {
		io.drive(new Pose2d(xVelocity, yVelocity, new Rotation2d(rotationVelocity)));
	}

	/**
	 * Reset the robot's position on the field
	 *
	 * @param pose The position to reset to
	 */
	public void resetPose(Pose2d pose) {
		io.resetOdometry(pose);
	}

	/**
	 * Get the robot's position on the field
	 *
	 * @return Current robot pose
	 */
	public Pose2d getPose() {
		return inputs.robotPose;
	}

	/**
	 * Set whether the robot drives field-relative
	 *
	 * @param enabled True for field-relative, false for robot-relative
	 */
	public void setFieldRelative(boolean enabled) {
		io.setFieldRelativeDrive(enabled);
	}

	/**
	 * Set whether to use open-loop control
	 *
	 * @param enabled True for open-loop, false for closed-loop
	 */
	public void setOpenLoop(boolean enabled) {
		io.setOpenLoopDrive(enabled);
	}
}
