// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CMD_Drive;
import frc.robot.swerve.IO_SwerveReal;
import frc.robot.swerve.SUB_Swerve;
import frc.robot.util.InputMap;

public class RobotContainer {
	private final CommandXboxController driverController;
	private final SUB_Swerve swerve;
	private final InputMap globalInputMap;

	public RobotContainer() {
		driverController = new CommandXboxController(0); // port 0
		globalInputMap = InputMap.XBOX; // Set the global input map to Xbox Controller

		swerve = new SUB_Swerve(new IO_SwerveReal());

		configureDefaultCommands();
	}

	private void configureDefaultCommands() {
		swerve.setDefaultCommand(new CMD_Drive(swerve, driverController, globalInputMap));
	}

	public Command getAutonomousCommand() {
		return Commands.print("No autonomous command enabled");
	}
}
