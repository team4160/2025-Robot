// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.swerve;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.RobotConstants;
import frc.robot.vision.SUB_Vision;
import org.littletonrobotics.junction.Logger;
import swervelib.SwerveController;
import swervelib.math.SwerveMath;
import swervelib.parser.SwerveDriveConfiguration;

public class SUB_Swerve extends SubsystemBase {
	private final IO_SwerveBase io;
	private final SUB_Vision vision;

	private final IO_SwerveBase.SwerveInputs inputs = new IO_SwerveBase.SwerveInputs();
	private final AprilTagFieldLayout aprilTagFieldLayout;

	public SUB_Swerve(SUB_Vision vision, IO_SwerveBase io) {
		this.io = io;
		this.vision = vision;

		try {
			this.aprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load AprilTag field layout", e);
		}

		io.setupPathPlanner(this);
	}

	@Override
	public void periodic() {

		// Update vision with current swerve pose
		vision.updatePoseEstimation(getPose());

		// Add new vision measurements to the swerve drive odometry if its available
		if (vision.getEstimatedGlobalPose().isPresent()) {
			io.addVisionMeasurement(
					vision.getEstimatedGlobalPose().get().estimatedPose.toPose2d(),
					vision.getEstimatedGlobalPose().get().timestampSeconds);

			// Record estimated pose output
			Logger.recordOutput(
					"Vision/CurrentEstimatedPose", vision.getEstimatedGlobalPose().get().estimatedPose);
		}

		// Update inputs
		io.updateInputs(inputs);

		Logger.processInputs("Swerve", inputs);
	}

	/**
	 * Get the distance to the speaker.
	 *
	 * @return Distance to speaker in meters.
	 */
	public double getDistanceToSpeaker() {
		int allianceAprilTag = DriverStation.getAlliance().get() == Alliance.Blue ? 7 : 4;
		Pose3d speakerAprilTagPose = aprilTagFieldLayout.getTagPose(allianceAprilTag).get();
		return getPose().getTranslation().getDistance(speakerAprilTagPose.toPose2d().getTranslation());
	}

	/**
	 * Get the yaw to aim at the speaker.
	 *
	 * @return {@link Rotation2d} of which you need to achieve.
	 */
	public Rotation2d getSpeakerYaw() {
		int allianceAprilTag = DriverStation.getAlliance().get() == Alliance.Blue ? 7 : 4;
		Pose3d speakerAprilTagPose = aprilTagFieldLayout.getTagPose(allianceAprilTag).get();
		Translation2d relativeTrl =
				speakerAprilTagPose.toPose2d().relativeTo(getPose()).getTranslation();
		return new Rotation2d(relativeTrl.getX(), relativeTrl.getY()).plus(getHeading());
	}

	/**
	 * Aim the robot at the speaker.
	 *
	 * @param tolerance Tolerance in degrees.
	 * @return Command to turn the robot to the speaker.
	 */
	public Command aimAtSpeaker(double tolerance) {
		SwerveController controller = getSwerveController();
		return run(() -> {
					ChassisSpeeds speeds =
							ChassisSpeeds.fromFieldRelativeSpeeds(
									0,
									0,
									controller.headingCalculate(
											getHeading().getRadians(), getSpeakerYaw().getRadians()),
									getHeading());
					drive(speeds);
				})
				.until(() -> Math.abs(getSpeakerYaw().minus(getHeading()).getDegrees()) < tolerance);
	}

	/**
	 * Get the path follower with events.
	 *
	 * @param pathName PathPlanner path name.
	 * @return {@link AutoBuilder#followPath(PathPlannerPath)} path command.
	 */
	public Command getAutonomousCommand(String pathName) {
		return new PathPlannerAuto(pathName);
	}

	/**
	 * Use PathPlanner Path finding to go to a point on the field.
	 *
	 * @param pose Target {@link Pose2d} to go to.
	 * @return PathFinding command
	 */
	public Command driveToPose(Pose2d pose) {
		PathConstraints constraints =
				new PathConstraints(
						getMaximumVelocity(), 4.0, getMaximumAngularVelocity(), Units.degreesToRadians(720));

		return AutoBuilder.pathfindToPose(
				pose, constraints, edu.wpi.first.units.Units.MetersPerSecond.of(0));
	}

	public void drive(Translation2d translation, double rotation, boolean fieldRelative) {
		io.drive(translation, rotation, fieldRelative);
	}

	public void drive(ChassisSpeeds velocity) {
		io.drive(velocity);
	}

	public SwerveDriveKinematics getKinematics() {
		return io.getKinematics();
	}

	public void resetOdometry(Pose2d initialHolonomicPose) {
		io.resetOdometry(initialHolonomicPose);
	}

	public Pose2d getPose() {
		return io.getPose();
	}

	public void setChassisSpeeds(ChassisSpeeds chassisSpeeds) {
		io.setChassisSpeeds(chassisSpeeds);
	}

	public void postTrajectory(Trajectory trajectory) {
		io.postTrajectory(trajectory);
	}

	public void zeroGyro() {
		io.zeroGyro();
	}

	public void zeroGyroWithAlliance() {
		if (isRedAlliance()) {
			zeroGyro();
			resetOdometry(new Pose2d(getPose().getTranslation(), Rotation2d.fromDegrees(180)));
		} else {
			zeroGyro();
		}
	}

	private boolean isRedAlliance() {
		var alliance = DriverStation.getAlliance();
		return alliance.isPresent() ? alliance.get() == DriverStation.Alliance.Red : false;
	}

	public void setMotorBrake(boolean brake) {
		io.setMotorBrake(brake);
	}

	public Rotation2d getHeading() {
		return getPose().getRotation();
	}

	public ChassisSpeeds getTargetSpeeds(
			double xInput, double yInput, double headingX, double headingY) {
		Translation2d scaledInputs = SwerveMath.cubeTranslation(new Translation2d(xInput, yInput));
		return getSwerveController()
				.getTargetSpeeds(
						scaledInputs.getX(),
						scaledInputs.getY(),
						headingX,
						headingY,
						getHeading().getRadians(),
						RobotConstants.MAX_SPEED);
	}

	public ChassisSpeeds getTargetSpeeds(double xInput, double yInput, Rotation2d angle) {
		Translation2d scaledInputs = SwerveMath.cubeTranslation(new Translation2d(xInput, yInput));
		return getSwerveController()
				.getTargetSpeeds(
						scaledInputs.getX(),
						scaledInputs.getY(),
						angle.getRadians(),
						getHeading().getRadians(),
						RobotConstants.MAX_SPEED);
	}

	public ChassisSpeeds getFieldVelocity() {
		return io.getFieldVelocity();
	}

	public ChassisSpeeds getRobotVelocity() {
		return io.getRobotVelocity();
	}

	public SwerveController getSwerveController() {
		return io.getSwerveController();
	}

	public SwerveDriveConfiguration getSwerveDriveConfiguration() {
		return io.getSwerveDriveConfiguration();
	}

	public void lock() {
		io.lock();
	}

	public Rotation2d getPitch() {
		return io.getPitch();
	}

	private double getMaximumVelocity() {
		return RobotConstants.MAX_SPEED;
	}

	private double getMaximumAngularVelocity() {
		return getSwerveController().config.maxAngularVelocity;
	}
}
