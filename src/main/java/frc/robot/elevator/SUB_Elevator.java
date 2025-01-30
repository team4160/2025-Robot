// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.elevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.misc.RobotState;
import org.littletonrobotics.junction.Logger;

public class SUB_Elevator extends SubsystemBase {

	private final IO_ElevatorBase io;

	private final IO_ElevatorBase.ElevatorInputs inputs = new IO_ElevatorBase.ElevatorInputs();

	private RobotState.State currentState = RobotState.State.STOWED;

	public SUB_Elevator(IO_ElevatorBase io) {
		this.io = io;
	}

	@Override
	public void periodic() {

		// Update state
		io.setPositionM(currentState.getHeightM());

		// Update inputs
		io.updateInputs(inputs);

		// Process inputs
		Logger.processInputs("Elevator", inputs);
	}

	public void updateState(RobotState.State newState) {
		currentState = newState;
	}

	public RobotState.State getCurrentState() {
		return currentState;
	}
}
