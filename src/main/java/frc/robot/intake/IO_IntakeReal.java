// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.intake;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.util.IRBeamBreak;

public class IO_IntakeReal implements IO_IntakeBase {

	private SparkMax armMotor;
	private SparkMax wheelMotor;

	private IRBeamBreak toggleSensor;

	public IO_IntakeReal() {

		armMotor = new SparkMax(21, MotorType.kBrushless);
		wheelMotor = new SparkMax(22, MotorType.kBrushless);
		toggleSensor = new IRBeamBreak(0);

		SparkMaxConfig sparkMaxConfig = new SparkMaxConfig();
		sparkMaxConfig.encoder.positionConversionFactor(5.142);
		sparkMaxConfig.inverted(true);

		ClosedLoopConfig closedLoopConfig = new ClosedLoopConfig();

		closedLoopConfig.p(0);

		closedLoopConfig.i(0);
		closedLoopConfig.d(0);

		sparkMaxConfig.apply(closedLoopConfig);

		armMotor.configure(
				sparkMaxConfig,
				SparkBase.ResetMode.kNoResetSafeParameters,
				SparkBase.PersistMode.kPersistParameters);

		armMotor.getEncoder().setPosition(0);
	}

	@Override
	public void updateInputs(IntakeInputs inputs) {

		inputs.armAngleDegrees = armMotor.getEncoder().getPosition();
		inputs.armMotorVoltage = armMotor.getAppliedOutput();
		inputs.armMotorCurrent = armMotor.getOutputCurrent();
		inputs.wheelMotorCurrent = wheelMotor.getOutputCurrent();
		inputs.wheelRPM = wheelMotor.getEncoder().getVelocity();
		inputs.toggleSensor = toggleSensor.getState();
		inputs.distanceSensorCM = 0;
	}

	@Override
	public void setArmAngle(double angle) {
		armMotor.getClosedLoopController().setReference(angle, ControlType.kPosition);
	}

	@Override
	public void setIntakeSpeed(double speed) {
		wheelMotor.set(speed);
	}
}
