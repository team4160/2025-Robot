// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.elevator;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

public class IO_ElevatorRealKracken implements IO_ElevatorBase {

	private final TalonFX motorOne;
	private final TalonFX motorTwo;
	private final MotionMagicVoltage motorRequest;

	// private SparkMax motorOne;
	// private SparkMax motorTwo;

	public IO_ElevatorRealKracken() {
		motorOne = new TalonFX(0);
		motorTwo = new TalonFX(0);
		motorRequest = new MotionMagicVoltage(0);

		var motorConfigs = new TalonFXConfiguration();

		// One rotation = X meters of linear travel
		// Calculate: (Sprocket Circumference in meters) / (Total Gear Reduction)
		// Example: (0.1524 meters per rotation) / (5:1 gear ratio) = 0.03048 meters per motor rotation
		double METERS_PER_MOTOR_ROTATION = 0.03048;

		var slot0Configs = motorConfigs.Slot0;
		slot0Configs.kS = 0.25; // Add 0.25 V output to overcome static friction
		slot0Configs.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
		slot0Configs.kA = 0.01; // An acceleration of 1 rps/s requires 0.01 V output
		slot0Configs.kP = 0.11; // An error of 1 rps results in 0.11 V output
		slot0Configs.kI = 0; // no output for integrated error
		slot0Configs.kD = 0; // no output for error derivative

		// Convert to meters
		motorConfigs.Feedback.SensorToMechanismRatio = METERS_PER_MOTOR_ROTATION;

		var motionMagicConfigs = motorConfigs.MotionMagic;
		motionMagicConfigs.MotionMagicCruiseVelocity = 2.0; // meters per second
		motionMagicConfigs.MotionMagicAcceleration = 4.0; // meters per second squared
		motionMagicConfigs.MotionMagicJerk = 40.0; // meters per second cubed

		motorOne.getConfigurator().apply(motorConfigs);
		motorTwo.getConfigurator().apply(motorConfigs);
	}

	@Override
	public void updateInputs(ElevatorInputs inputs) {
		inputs.heightM = motorOne.getPosition().getValueAsDouble();
		inputs.velocityMPS = motorOne.getVelocity().getValueAsDouble();
		inputs.motorOneCurrent = motorOne.getSupplyCurrent().getValueAsDouble();
		inputs.motorTwoCurrent = motorTwo.getSupplyCurrent().getValueAsDouble();
	}

	@Override
	public void setPositionM(double positionM) {
		motorOne.setControl(motorRequest.withPosition(positionM));
		motorTwo.setControl(motorRequest.withPosition(positionM));
	}
}
