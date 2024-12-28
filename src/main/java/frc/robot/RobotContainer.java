// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CMD_Drive;
import frc.robot.swerve.IO_SwerveReal;
import frc.robot.swerve.SUB_Swerve;
import frc.robot.util.InputMap;
import frc.robot.vision.IO_VisionReal;
import frc.robot.vision.IO_VisionSim;
import frc.robot.vision.SUB_Vision;
import java.io.File;

public class RobotContainer {
	private final CommandXboxController driverController;

	private final SUB_Swerve swerve;
	private final SUB_Vision vision;

	private final InputMap globalInputMap;

	public RobotContainer() {
		driverController = new CommandXboxController(0); // port 0
		globalInputMap = InputMap.KEYBOARD; // Set the global input map to Xbox Controller

		vision = new SUB_Vision(Robot.isSimulation() ? new IO_VisionSim() : new IO_VisionReal());

		swerve =
				new SUB_Swerve(
						vision, new IO_SwerveReal(new File(Filesystem.getDeployDirectory(), "swerve")));

		configureDefaultCommands();
	}

	private void configureDefaultCommands() {
		swerve.setDefaultCommand(new CMD_Drive(swerve, driverController, globalInputMap));
	}

	public Command getAutonomousCommand() {
		return AutoBuilder.buildAuto("Test");
	}
}
