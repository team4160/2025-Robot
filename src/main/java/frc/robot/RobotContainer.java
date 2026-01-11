// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.File;

import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ExtakeCoral;
import frc.robot.commands.GoToIntake;
import frc.robot.commands.IntakeCoral;
import frc.robot.commands.L1;
import frc.robot.commands.L2;
import frc.robot.commands.L3;
import frc.robot.commands.ZeroClimber;
import frc.robot.commands.AutoClimb;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import swervelib.SwerveInputStream;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{

  // Replace with CommandPS4Controller or CommandJoystick if needed
  final         CommandXboxController driverXbox = new CommandXboxController(0);
  final         CommandXboxController operatorXbox = new CommandXboxController(1);
  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem       drivebase  = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve/falcon"));
  private final Elevator              elevator   = new Elevator();
  private final Arm                   arm        = new Arm();
  private final Intake                intake     = new Intake();
  private final Climb                 climb      = new Climb();
  // private final Led                   led        = new Led();

  private final SendableChooser<String> autoChooser = new SendableChooser<>();
  private static final String autoSimple = "Simple";
  private static final String autoTaxi = "Taxi";
  private static final String autoLTaxi = "Long Taxi";
  private static final String threePieceRight = "3 Piece Right";
  private static final String threePiece = "3 piece";
  private static final String dance = "Robot Dance";

  /**
   * Converts driver input into a field-relative ChassisSpeeds that is controlled by angular velocity.
   */
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> Utils.sensitivity(driverXbox.getLeftY() * -1,0.75),
                                                                () -> Utils.sensitivity(driverXbox.getLeftX() * -1,0.75))
                                                            .withControllerRotationAxis(() -> Utils.sensitivity(driverXbox.getRightX() * -1,0.75))
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(1)
                                                            .allianceRelativeControl(true);

  /**
   * Clone's the angular velocity input stream and converts it to a fieldRelative input stream.
   */
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverXbox::getRightX,
                                                                                             driverXbox::getRightY)
                                                           .headingWhile(true);

  /**
   * Clone's the angular velocity input stream and converts it to a robotRelative input stream.
   */
  SwerveInputStream driveRobotOriented = driveAngularVelocity.copy().robotRelative(true)
                                                             .allianceRelativeControl(true);

  SwerveInputStream driveAngularVelocityKeyboard = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                        () -> -driverXbox.getLeftY(),
                                                                        () -> -driverXbox.getLeftX())
                                                                    .withControllerRotationAxis(() -> driverXbox.getRawAxis(
                                                                        2))
                                                                    .deadband(OperatorConstants.DEADBAND)
                                                                    .scaleTranslation(0.8)
                                                                    .allianceRelativeControl(true);
  // Derive the heading axis with math!
  SwerveInputStream driveDirectAngleKeyboard     = driveAngularVelocityKeyboard.copy()
                                                                               .withControllerHeadingAxis(() ->
                                                                                                              Math.sin(
                                                                                                                  driverXbox.getRawAxis(
                                                                                                                      2) *
                                                                                                                  Math.PI) *
                                                                                                              (Math.PI *
                                                                                                               2),
                                                                                                          () ->
                                                                                                              Math.cos(
                                                                                                                  driverXbox.getRawAxis(
                                                                                                                      2) *
                                                                                                                  Math.PI) *
                                                                                                              (Math.PI *
                                                                                                               2))
                                                                               .headingWhile(true)
                                                                               .translationHeadingOffset(true)
                                                                               .translationHeadingOffset(Rotation2d.fromDegrees(
                                                                                   0));

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer()
  {
    // Configure the trigger bindings
    configureBindings();
    DriverStation.silenceJoystickConnectionWarning(true);
    NamedCommands.registerCommand("test", Commands.print("I EXIST"));
    NamedCommands.registerCommand("L1", new L1(elevator, arm));
    NamedCommands.registerCommand("L2", new L2(elevator, arm));
    NamedCommands.registerCommand("L3", new L3(elevator, arm));
    NamedCommands.registerCommand("GoToIntake", new GoToIntake(elevator,arm));
    NamedCommands.registerCommand("ExtakeCoral", new ExtakeCoral(intake));
    NamedCommands.registerCommand("IntakeCoral", new IntakeCoral(intake, operatorXbox, driverXbox));
    autoChooser.setDefaultOption(autoSimple, autoSimple);
    autoChooser.addOption(autoTaxi, autoTaxi);
    autoChooser.addOption(autoLTaxi, autoLTaxi);
    autoChooser.addOption(threePieceRight, threePieceRight);
    autoChooser.addOption(threePiece, threePiece);
    autoChooser.addOption(dance, dance);
    SmartDashboard.putData("auto", autoChooser);
    RobotModeTriggers.teleop().onTrue(new ZeroClimber(climb));

    DataLogManager.start();
    DriverStation.startDataLog(DataLogManager.getLog());
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings()
  {
    Command driveFieldOrientedDirectAngle      = drivebase.driveFieldOriented(driveDirectAngle);
    Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
    Command driveRobotOrientedAngularVelocity  = drivebase.driveFieldOriented(driveRobotOriented);
    Command driveSetpointGen = drivebase.driveWithSetpointGeneratorFieldRelative(driveDirectAngle);
    Command driveFieldOrientedDirectAngleKeyboard      = drivebase.driveFieldOriented(driveDirectAngleKeyboard);
    Command driveFieldOrientedAnglularVelocityKeyboard = drivebase.driveFieldOriented(driveAngularVelocityKeyboard);
    Command driveSetpointGenKeyboard = drivebase.driveWithSetpointGeneratorFieldRelative(
        driveDirectAngleKeyboard);
    
    if (RobotBase.isSimulation())
    {
      drivebase.setDefaultCommand(driveFieldOrientedDirectAngleKeyboard);
    } else
    {
      drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
    }

    if (Robot.isSimulation())
    {
      Pose2d target = new Pose2d(new Translation2d(1, 4),
                                 Rotation2d.fromDegrees(90));
      //drivebase.getSwerveDrive().field.getObject("targetPose").setPose(target);
      driveDirectAngleKeyboard.driveToPose(() -> target,
                                           new ProfiledPIDController(5,
                                                                     0,
                                                                     0,
                                                                     new Constraints(5, 2)),
                                           new ProfiledPIDController(5,
                                                                     0,
                                                                     0,
                                                                     new Constraints(Units.degreesToRadians(360),
                                                                                     Units.degreesToRadians(180))
                                           ));
      driverXbox.start().onTrue(Commands.runOnce(() -> drivebase.resetOdometry(new Pose2d(3, 3, new Rotation2d()))));
      driverXbox.button(1).whileTrue(drivebase.sysIdDriveMotorCommand());
      driverXbox.button(3).whileTrue(drivebase.sysIdAngleMotorCommand());
      driverXbox.button(2).whileTrue(Commands.runEnd(() -> driveDirectAngleKeyboard.driveToPoseEnabled(true),
                                                     () -> driveDirectAngleKeyboard.driveToPoseEnabled(false)));

//      driverXbox.b().whileTrue(
//          drivebase.driveToPose(
//              new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0)))
//                              );

    }
    if (DriverStation.isTest())
    {
      drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity); // Overrides drive command above!

      driverXbox.x().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
      driverXbox.y().whileTrue(drivebase.driveToDistanceCommand(1.0, 0.2));
      driverXbox.start().onTrue((Commands.runOnce(drivebase::zeroGyro)));
      driverXbox.back().whileTrue(drivebase.centerModulesCommand());
      // driverXbox.leftBumper().onTrue(Commands.none());
      driverXbox.rightBumper().onTrue(Commands.none());
    } else
    {
      driverXbox.a().onTrue((Commands.runOnce(drivebase::zeroGyroWithAlliance)));
      // driverXbox.x().onTrue(Commands.runOnce(drivebase::addFakeVisionReading));
      // driverXbox.b().whileTrue(drivebase.sysIdDriveMotorCommand());
      // driverXbox.y().whileTrue(drivebase.sysIdAngleMotorCommand());
      // driverXbox.b().whileTrue(elevator.setElevatorHeight(0));
      // driverXbox.b().whileFalse(elevator.setElevator(driverXbox));
      operatorXbox.y().whileTrue(elevator.setElevatorHeight(0).alongWith(arm.moveToAngle(0.482)));
      // driverXbox.b().whileFalse(arm.setArmAngle(driverXbox));
      // driverXbox.b().whileTrue(arm.moveToAngle(0.5));
      // driverXbox.y().onTrue(arm.moveToAngle(0));
      // driverXbox.b().whileTrue(arm.moveIntake(1));
      // driverXbox.y().whileTrue(arm.moveIntake(-1));
      // driverXbox.b().negate().and(driverXbox.y().negate()).whileTrue(arm.moveIntake(0));
      driverXbox.start().whileTrue(Commands.none());
      driverXbox.back().whileTrue(Commands.none());
      driverXbox.rightBumper().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
     // driverXbox.b().onTrue(new driveToPose());
      // driverXbox.rightBumper().onTrue(Commands.none());
      // driverXbox.b().onTrue(new AbsoluteDrive(drivebase, null, null, null, null));
      climb.setDefaultCommand(climb.manualClimb(driverXbox));
      driverXbox.leftBumper().onTrue(new AutoClimb(climb));
      elevator.setDefaultCommand(elevator.manualElevator(operatorXbox));
      arm.setDefaultCommand(arm.manualArm(operatorXbox));
      intake.setDefaultCommand(intake.manualIntake(operatorXbox));
      // operatorXbox.b().onTrue(new GoToIntake(elevator, arm).andThen(new IntakeCoral(intake)));
      // operatorXbox.povDown().onTrue(new RelitiveDrive(drivebase));
      operatorXbox.b().whileTrue(new GoToIntake(elevator, arm));
      operatorXbox.a().whileTrue(new L1(elevator, arm));
      operatorXbox.x().whileTrue(new L2(elevator, arm));
      operatorXbox.y().whileTrue(new L3(elevator, arm));
      operatorXbox.povUp().onTrue(new ExtakeCoral(intake));
      operatorXbox.povDown().onTrue(new IntakeCoral(intake, operatorXbox, driverXbox));
      // operatorXbox.start().onTrue( CommandScheduler.getInstance().cancelAll());
    }

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand()
  {
    // An example command will be run in autonomous
    return drivebase.getAutonomousCommand(autoChooser.getSelected());
  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }
}
