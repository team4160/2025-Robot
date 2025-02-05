// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.commands.generic;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.superstructure.SUB_Superstructure;
import frc.robot.superstructure.SuperstructureState;

public class CMD_Intake extends SequentialCommandGroup {
	public CMD_Intake(SUB_Superstructure superstructure) {

		// boolean isAtAnyCoralState = superstructure.getCurrentSuperstructureState();

		addCommands(
				// Move to Coral Station state
				new CMD_Superstructure(superstructure, SuperstructureState.CORAL_STATION));

		// TODO: Auto idle intake after coral is sensed by the sensor
		// new CMD_Superstructure(superstructure, SuperstructureState.IDLE));
	}
}
