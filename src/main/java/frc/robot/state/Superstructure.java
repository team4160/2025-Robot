// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.state;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.elevator.SUB_Elevator;
import frc.robot.intake.SUB_Intake;
import frc.robot.misc.SUB_Led;

public class Superstructure extends SubsystemBase {

	private GlobalRobotState.State currentGlobalRobotState = GlobalRobotState.State.STOWED;

	private SUB_Intake intake;
	private SUB_Elevator elevator;
	private SUB_Led led;

	public Superstructure(SUB_Intake intake, SUB_Elevator elevator, SUB_Led led) {
		this.intake = intake;
		this.elevator = elevator;
		this.led = led;
	}

	public void updateGlobalState(GlobalRobotState.State newGlobalState) {
		// Sets current global robot state
		currentGlobalRobotState = newGlobalState;

		// Update the subsystems with the new state
		elevator.updateLocalState(currentGlobalRobotState);
		intake.updateLocalState(currentGlobalRobotState);
		led.updateLocalState(currentGlobalRobotState);
	}

	public GlobalRobotState.State getCurrentGlobalState() {
		return currentGlobalRobotState;
	}
}
