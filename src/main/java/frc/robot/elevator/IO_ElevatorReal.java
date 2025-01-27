// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.elevator;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.DriverStation;

public class IO_ElevatorReal implements IO_ElevatorBase {

	private final TalonFX leftMotor_10;
	private final TalonFX rightMotor_9;
	private final MotionMagicVoltage motorRequest;

	// private SparkMax motorOne;
	// private SparkMax motorTwo;

	public IO_ElevatorReal() {
		
		leftMotor_10 = new TalonFX(10, "canivore");
		rightMotor_9 = new TalonFX(9, "canivore");

		motorRequest = new MotionMagicVoltage(0);

		var motorConfigs = new TalonFXConfiguration();

		/*
		Conversion factor explained:
		METERS_PER_MOTOR_ROTATION represents the linear travel of the elevator per motor rotation.
		It is derived using the formula:
			(1 / gearbox ratio) * (PI * pitch circle diameter of the sprocket) * stage multiplier
		- The gearbox ratio determines how much the motor turns relative to the output.
		- PI * pitch circle diameter gives the circumference of the sprocket, corresponding to
			the linear movement for one rotation.
		- The stage multiplier accounts for additional travel as the stage extends (e.g., stage 2
			doubles the first stage, stage 3 triples, etc.).
			For this calculation:
			(1 / 16.25) * (PI * 0.0444754) * 3
			= 0.0257951242902 meters per motor rotation.
		- For getting the exact middle position of the carriage, we add an offset.
		*/

		double METERS_PER_MOTOR_ROTATION = 0.0257951242902;
		motorConfigs.Feedback.SensorToMechanismRatio = METERS_PER_MOTOR_ROTATION;

		/*
		Motion Magic Elevator Tuning Guide:

		1. Initial Setup:
		- Start with all gains at 0 (kS, kV, kA, kP, kI, kD, kG)
		- Set soft limits for max height
		- Begin with low motion magic values:
			* Cruise Velocity: 0.5 m/s
			* Acceleration: 1.0 m/s²
			* Jerk: 10.0 m/s³

		2. Gravity Compensation (kG):
		- Manually raise elevator to mid-point
		- Increase kG until elevator holds position with minimal power
		- Good: Elevator maintains position with < 15% power
		- Too high: Elevator creeps upward
		- Too low: Elevator sags

		3. Static Friction (kS):
		- Start with 0.05V
		- Increase until elevator starts moving smoothly
		- Good: Smooth initial movement, no sticking
		- Too high: Jerky movement
		- Too low: Stiction, delayed movement

		4. Velocity Feed Forward (kV):
		- Calculate theoretical: (Voltage * 60) / (Free Speed * Gear Ratio)
		- Test with slow moves
		- Good: Smooth movement, follows trajectory
		- Too high: Overshoots, oscillates
		- Too low: Lags behind trajectory

		4.1. kV Calculation:
		- Theoretical kV represents the voltage required to achieve a linear velocity of 1 m/s.
		- It is calculated using the formula:
			kV = Voltage / Linear Velocity
		Where:
			Linear Velocity = (Free Speed in RPM / 60) / Gear Ratio * METERS_PER_MOTOR_ROTATION
				- Free Speed in RPM: 5800
				- Gear Ratio: 16.25
				- METERS_PER_MOTOR_ROTATION: 0.025795

		For KrakenX60 with our gear ratio:
			Free Speed (rps) = 5800 / 60 = 96.67 rps
			Output Speed (rps) = 96.67 / 16.25 ≈ 5.948 rps
			Linear Velocity (m/s) = 33.14 * 5.948 ≈ 197.11672 m/s
			kV = 12V / 197.11672 m/s ≈ 0.0608776363568 V per m/s

		5. Acceleration Feed Forward (kA):
		- Start very small (0.001)
		- Increase until acceleration matches desired profile
		- Good: Quick response to velocity changes
		- Too high: Jerky movement
		- Too low: Sluggish acceleration

		6. Position Feedback (kP):
		- Start small (0.1)
		- Increase until position error is minimal
		- Good: < 1cm steady-state error
		- Too high: Oscillation at target
		- Too low: Doesn't reach target

		7. Motion Magic Tuning:
		- Gradually increase velocity and acceleration
		- Watch for:
			* Smooth acceleration/deceleration
			* No oscillation
			* Current draw within limits
		*/

		// Apply slot 0 configs
		var slot0Configs = motorConfigs.Slot0;
		slot0Configs.kS = 0; // Static friction compensation (V)
		slot0Configs.kV = 0; // Velocity feed forward (V per m/s)
		slot0Configs.kA = 0; // Acceleration feed forward (V per m/s²)
		slot0Configs.kP = 0; // Position error gain (V per meter)
		slot0Configs.kI = 0; // Integral gain for steady-state error
		slot0Configs.kD = 0; // Derivative gain for damping
		slot0Configs.kG = 0.0; // Gravity compensation
		slot0Configs.GravityType = GravityTypeValue.Elevator_Static;

		/*
				slot0Configs.kS = 0.25; // Static friction compensation (V)
		slot0Configs.kV = 0.12; // Velocity feed forward (V per m/s)
		slot0Configs.kA = 0.01; // Acceleration feed forward (V per m/s²)
		slot0Configs.kP = 0.11; // Position error gain (V per meter)
		slot0Configs.kI = 0; // Integral gain for steady-state error
		slot0Configs.kD = 0; // Derivative gain for damping
		slot0Configs.kG = 0.1; // Gravity compensation
		slot0Configs.GravityType = GravityTypeValue.Elevator_Static;
		 */

		// Motion magic max rates
		var motionMagicConfigs = motorConfigs.MotionMagic;
		motionMagicConfigs.MotionMagicCruiseVelocity = 0.5; // meters per second
		motionMagicConfigs.MotionMagicAcceleration = 1.0; // meters per second squared
		motionMagicConfigs.MotionMagicJerk = 10.0; // meters per second cubed

		// Apply soft limits
		motorConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
		motorConfigs.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 1524; // Set to max height in milimeters (3ft for testing)
		motorConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
		motorConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0.0;

		// Needs CCW+ to bring elevator up
		var rightMotorConfig = motorConfigs;
		rightMotorConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
		var rightMotorConfigStatus = rightMotor_9.getConfigurator().apply(motorConfigs);

		// Check if the configuration was successful
		if (rightMotorConfigStatus != StatusCode.OK) {
			DriverStation.reportWarning("Failed to apply right motor configuration: " + rightMotorConfigStatus, false);
		}

		// needs CW+ to bring elevator up 
		var leftMotorConfig = motorConfigs;
		leftMotorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
		var leftMotorConfigStatus = leftMotor_10.getConfigurator().apply(motorConfigs);

		// Check if the configuration was successful
		if (leftMotorConfigStatus != StatusCode.OK) {
			DriverStation.reportWarning("Failed to apply left motor configuration: " + leftMotorConfigStatus, false);
		}

		// Reset encoder to zero
		leftMotor_10.setPosition(0);
		rightMotor_9.setPosition(0);
	}

	@Override
	public void updateInputs(ElevatorInputs inputs) {
		inputs.heightM = leftMotor_10.getPosition().getValueAsDouble() / 1000;
		inputs.velocityMPS = leftMotor_10.getVelocity().getValueAsDouble() / 1000;
		inputs.accelerationMPS2 = leftMotor_10.getAcceleration().getValueAsDouble() / 1000;
		inputs.leftMotorVoltage = leftMotor_10.getMotorVoltage().getValueAsDouble();
		inputs.rightMotorVoltage = rightMotor_9.getMotorVoltage().getValueAsDouble();
		inputs.leftMotorCurrent = leftMotor_10.getSupplyCurrent().getValueAsDouble();
		inputs.rightMotorCurrent = rightMotor_9.getSupplyCurrent().getValueAsDouble();
		inputs.leftMotorPower = leftMotor_10.getDutyCycle().getValueAsDouble();
		inputs.rightMotorPower = rightMotor_9.getDutyCycle().getValueAsDouble();
	}

	@Override
	public void setPositionM(double positionM) {
		leftMotor_10.setControl(motorRequest.withPosition(positionM));
		rightMotor_9.setControl(motorRequest.withPosition(positionM));
	}

	@Override
	public void setVoltage(double voltage) {
		leftMotor_10.setVoltage(voltage);
		rightMotor_9.setVoltage(voltage);
	}
}
