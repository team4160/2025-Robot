// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.swerve;

import static edu.wpi.first.units.Units.Meter;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathfindingCommand;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.constants.RobotConstants;
import java.io.File;
import swervelib.SwerveController;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;

public class IO_SwerveReal implements IO_SwerveBase {

	private final SwerveDrive swerveDrive;
	private final SwerveInputs inputs = new SwerveInputs();

	public IO_SwerveReal(File directory) {
		// Configure the Telemetry before creating the SwerveDrive
		SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;
		try {
			swerveDrive =
					new SwerveParser(directory)
							.createSwerveDrive(
									RobotConstants.MAX_SPEED,
									new Pose2d(
											new Translation2d(Meter.of(0.01), Meter.of(0.01)),
											Rotation2d.fromDegrees(0)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Configure SwerveDrive settings
		// swerveDrive.setHeadingCorrection(false);
		//	swerveDrive.setCosineCompensator(true);
		//	swerveDrive.setAngularVelocityCompensation(true, false, 0.1);
		//	swerveDrive.setModuleEncoderAutoSynchronize(false, 1);
		// swerveDrive.pushOffsetsToEncoders();
	}

	@Override
	public void updateInputs(SwerveInputs inputs) {
		inputs.robotPose = swerveDrive.getPose();
		inputs.gyroYaw = swerveDrive.getYaw();
		inputs.gyroPitch = swerveDrive.getPitch();
		inputs.moduleStates = swerveDrive.getStates();
		inputs.modulePositions = swerveDrive.getModulePositions();
		inputs.fieldSpeeds = swerveDrive.getFieldVelocity();
		inputs.robotSpeeds = swerveDrive.getRobotVelocity();
		inputs.isFieldRelative = this.inputs.isFieldRelative;
		inputs.isOpenLoop = this.inputs.isOpenLoop;
	}

	@Override
	public void drive(Translation2d translation, double rotation, boolean fieldRelative) {
		swerveDrive.drive(translation, rotation, fieldRelative, false);
	}

	@Override
	public void drive(ChassisSpeeds velocity) {
		swerveDrive.drive(velocity);
	}

	@Override
	public void setChassisSpeeds(ChassisSpeeds chassisSpeeds) {
		swerveDrive.setChassisSpeeds(chassisSpeeds);
	}

	@Override
	public void resetOdometry(Pose2d initialHolonomicPose) {
		swerveDrive.resetOdometry(initialHolonomicPose);
	}

	@Override
	public void zeroGyro() {
		swerveDrive.zeroGyro();
	}

	@Override
	public void setMotorBrake(boolean brake) {
		swerveDrive.setMotorIdleMode(brake);
	}

	@Override
	public Pose2d getPose() {
		return swerveDrive.getPose();
	}

	@Override
	public Rotation2d getHeading() {
		return getPose().getRotation();
	}

	@Override
	public Rotation2d getPitch() {
		return swerveDrive.getPitch();
	}

	@Override
	public ChassisSpeeds getFieldVelocity() {
		return swerveDrive.getFieldVelocity();
	}

	@Override
	public ChassisSpeeds getRobotVelocity() {
		return swerveDrive.getRobotVelocity();
	}

	@Override
	public SwerveDriveKinematics getKinematics() {
		return swerveDrive.kinematics;
	}

	@Override
	public SwerveController getSwerveController() {
		return swerveDrive.swerveController;
	}

	@Override
	public SwerveDriveConfiguration getSwerveDriveConfiguration() {
		return swerveDrive.swerveDriveConfiguration;
	}

	@Override
	public void lock() {
		swerveDrive.lockPose();
	}

	@Override
	public void postTrajectory(Trajectory trajectory) {
		swerveDrive.postTrajectory(trajectory);
	}

	@Override
	public Command setAllAngle(double angle) {
		return new InstantCommand(
				() -> {
					swerveDrive.getModules()[0].setAngle(angle);
					swerveDrive.getModules()[1].setAngle(angle);
					swerveDrive.getModules()[2].setAngle(angle);
					swerveDrive.getModules()[3].setAngle(angle);
				});
	}

	@Override
	public void setupPathPlanner(SUB_Swerve swerveSubsystem) {

		try {
			// Load PathPlanner config from GUI settings
			RobotConfig config = RobotConfig.fromGUISettings();

			final boolean enableFeedforward = true;

			// Configure AutoBuilder
			AutoBuilder.configure(
					this::getPose, // Robot pose supplier
					this::resetOdometry, // Method to reset odometry
					this::getRobotVelocity, // ChassisSpeeds supplier (MUST BE ROBOT RELATIVE)
					(speedsRobotRelative, moduleFeedForwards) -> {
						if (enableFeedforward) {
							swerveDrive.drive(
									speedsRobotRelative,
									swerveDrive.kinematics.toSwerveModuleStates(speedsRobotRelative),
									moduleFeedForwards.linearForces());
						} else {
							swerveDrive.setChassisSpeeds(speedsRobotRelative);
						}
					},
					new PPHolonomicDriveController(
							new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
							new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
							),
					config,
					() -> {
						// Boolean supplier that controls when the path will be mirrored for the red alliance
						var alliance = DriverStation.getAlliance();
						if (alliance.isPresent()) {
							return alliance.get() == DriverStation.Alliance.Red;
						}
						return false;
					},
					swerveSubsystem // Reference to this subsystem to set requirements
					);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Preload PathPlanner Path finding
		PathfindingCommand.warmupCommand().schedule();
	}

	/**
	 * Add a vision measurement to the swerve drive odometry.
	 *
	 * @param pose The measured pose
	 * @param timestamp The timestamp of the measurement in seconds
	 */
	@Override
	public void addVisionMeasurement(Pose2d pose, double timestamp) {
		swerveDrive.addVisionMeasurement(pose, timestamp);
	}

	/** Update odometry for the swerve drive. */
	public void updateOdometry() {
		swerveDrive.updateOdometry();
	}
}
