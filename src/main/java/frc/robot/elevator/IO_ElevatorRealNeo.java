// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.elevator;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

public class IO_ElevatorRealNeo implements IO_ElevatorBase {

	private SparkMax motorOne;
	private SparkMax motorTwo;

	public IO_ElevatorRealNeo() {

		motorOne = new SparkMax(0, MotorType.kBrushless);
		motorTwo = new SparkMax(0, MotorType.kBrushless);
	}

	@Override
	public void updateInputs(ElevatorInputs inputs) {
		inputs.heightM = motorOne.getEncoder().getPosition();
		inputs.velocityMPS = motorOne.getEncoder().getVelocity();
		inputs.motorOneCurrent = motorOne.getOutputCurrent();
		inputs.motorTwoCurrent = motorTwo.getOutputCurrent();
	}

	@Override
	public void setPositionM(double speed) {
		// motorOne.set(speed);
		// motorTwo.set(speed);
		// Fix this later
	}
}
