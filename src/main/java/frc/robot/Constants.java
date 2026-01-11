// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.Matter;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean constants. This
 * class should not be used for any other purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants
{
  public static final AprilTagFieldLayout fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded);
  public static final double ROBOT_MASS = (148 - 20.3) * 0.453592; // 32lbs * kg per pound
  public static final Matter CHASSIS    = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
  public static final double LOOP_TIME  = 0.13; //s, 20ms + 110ms sprk max velocity lag
  public static final double MAX_SPEED  = Units.feetToMeters(14.5);
  // Maximum speed of the robot in meters per second, used to limit acceleration.

  public static final boolean alwaysManual = false;

//  public static final class AutonConstants
//  {
//
//    public static final PIDConstants TRANSLATION_PID = new PIDConstants(0.7, 0, 0);
//    public static final PIDConstants ANGLE_PID       = new PIDConstants(0.4, 0, 0.01);
//  }

public final class ElevatorConstants{
  public static final int LEFT_ELEVATOR_MOTOR_ID = 11;
  public static final int RIGHT_ELEVATOR_MOTOR_ID = 12;
  public static final int ELEVATOR_CANCODER_ID = 2;
  public static final double kG = 0.01;
  public static final double kV = 0.0;
  public static final double kA = 0.0;
  public static final double kP = 0.5;
  public static final double kI = 0.0;
  public static final double kD = 0.0;
  public static final double kElevatorGearing = 21.7777777777;
  public static final double kElevatorDrumRadius = Units.inchesToMeters(1.79); // meters
  public static final double kElevatorCarriageMass = Units.lbsToKilograms(16); // kg
  public static final double kElevatorMinHeightMeters = 0.0; // meters
  public static final double kElevatorMaxHeightMeters = Units.inchesToMeters(69); // meters
  public static final double kElevatorDefaultTolerance = Inches.of(1).in(Meters);
}

  public static final class DrivebaseConstants
  {

    // Hold time on motor brakes when disabled
    public static final double WHEEL_LOCK_TIME = 10; // seconds
  }

  public static class OperatorConstants
  {

    // Joystick Deadband
    public static final double DEADBAND        = 0.03;
    public static final double LEFT_Y_DEADBAND = 0.1;
    public static final double RIGHT_X_DEADBAND = 0.1;
    public static final double TURN_CONSTANT    = 6;
  }

  public static final class ArmConstants
  {
    public static final int ARM_MOTOR_ID = 21;
    public static final double ARM_GEAR_RATIO = 1;
    public static final double ARM_LENGTH_METERS = Units.inchesToMeters(0);
    public static final double ARM_MAX_ANGLE = 180.0; // degrees
    public static final double ARM_MIN_ANGLE = 0.0; // degrees
    public static final double kG = 0.01;
    public static final double kV = 0.0;
    public static final double kA = 0.0;
    public static final double kP = 13.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double FORWARD_LIMIT = 0.6;
    public static final double REVERSE_LIMIT = -0.136;
    public static final int ARM_CANCODER_ID = 1;
    public static final double INTAKE_SPEED = 0;
    public static final double kArmDefaultTolerance = 0.01;
  }

  public static final class ClimbConstants
  {
    public static final int CLIMB_MOTOR_ID = 31;
    public static final double CLIMB_GEAR_RATIO = 1;
    public static final double CLIMB_LENGTH_METERS = Units.inchesToMeters(0);
    public static final double CLIMB_MAX_ANGLE = 180.0; // degrees
    public static final double CLIMB_MIN_ANGLE = 0.0; // degrees
    public static final double kG = 0.01;
    public static final double kV = 0.0;
    public static final double kA = 0.0;
    public static final double kP = 0.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double FORWARD_LIMIT = -5;
    public static final double REVERSE_LIMIT = -425;
    public static final double INTAKE_SPEED = 0;
    public static final double kCLIMBDefaultTolerance = 0.01;
  }

  public static final class IntakeConstants
  {
    public static final int INTAKE_MOTOR_ID = 21;
    public static final double INTAKE_SPEED = 0.5;
    public static final double INTAKE_REVERSE_SPEED = -0.5;
  }
}
