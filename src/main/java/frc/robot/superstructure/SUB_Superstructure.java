// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.superstructure;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.elevator.SUB_Elevator;
import frc.robot.intake.SUB_Intake;
import frc.robot.util.SUB_Led;

public class SUB_Superstructure extends SubsystemBase {

	private SuperstructureState.State currentSuperstructureState = SuperstructureState.State.IDLE;

	private SUB_Intake intake;
	private SUB_Elevator elevator;
	private SUB_Led led;

	public SUB_Superstructure(SUB_Intake intake, SUB_Elevator elevator, SUB_Led led) {
		this.intake = intake;
		this.elevator = elevator;
		this.led = led;
	}

	public void updateSuperstructureState(SuperstructureState.State newSuperstructureState) {
		// Sets current global robot state
		currentSuperstructureState = newSuperstructureState;

		// Update the subsystems with the new state
		elevator.updateLocalState(currentSuperstructureState);
		intake.updateLocalState(currentSuperstructureState);
		led.updateLocalState(currentSuperstructureState);
	}

	public SuperstructureState.State getCurrentSuperstructureState() {
		return currentSuperstructureState;
	}
}
