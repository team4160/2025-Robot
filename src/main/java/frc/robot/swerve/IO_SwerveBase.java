// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface IO_SwerveBase {
	@AutoLog
	public static class SwerveInputs implements LoggableInputs {

		// Robot pose and orientation
		public Pose2d robotPose = new Pose2d();
		public Rotation2d gyroYaw = new Rotation2d();

		// Module states
		public SwerveModuleState[] moduleStates = new SwerveModuleState[4];
		public SwerveModulePosition[] modulePositions = new SwerveModulePosition[4];

		// Chassis speeds
		public ChassisSpeeds fieldSpeeds = new ChassisSpeeds();
		public ChassisSpeeds robotSpeeds = new ChassisSpeeds();

		// Driving parameters
		public boolean isFieldRelative = true;
		public boolean isOpenLoop = false;

		@Override
		public void toLog(LogTable table) {
			table.put("RobotPose", robotPose);
			table.put("GyroYaw", gyroYaw);
			table.put("ModuleStates", moduleStates);
			table.put("ModulePositions", modulePositions);
			table.put("FieldSpeeds", fieldSpeeds);
			table.put("RobotSpeeds", robotSpeeds);
			table.put("IsFieldRelative", isFieldRelative);
			table.put("IsOpenLoop", isOpenLoop);
		}

		@Override
		public void fromLog(LogTable table) {
			robotPose = table.get("RobotPose", robotPose);
			gyroYaw = table.get("GyroYaw", gyroYaw);
			moduleStates = table.get("ModuleStates", moduleStates);
			modulePositions = table.get("ModulePositions", modulePositions);
			fieldSpeeds = table.get("FieldSpeeds", fieldSpeeds);
			robotSpeeds = table.get("RobotSpeeds", robotSpeeds);
			isFieldRelative = table.get("IsFieldRelative", isFieldRelative);
			isOpenLoop = table.get("IsOpenLoop", isOpenLoop);
		}
	}

	/** Updates the set of loggable inputs. */
	public void updateInputs(SwerveInputs inputs);

	/** Drive the swerve base with X, Y, and Theta velocity components. */
	public void drive(Pose2d pose);

	/** Reset the odometry to a specific pose. */
	public void resetOdometry(Pose2d pose);

	/** Update the odometry with latest measurements. */
	public void updateOdometry();

	/** Set the field relative mode. */
	public void setFieldRelativeDrive(boolean fieldRelative);

	/** Set the open loop mode. */
	public void setOpenLoopDrive(boolean openLoop);
}
