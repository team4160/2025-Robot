// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CMD_AimAtAprilTag;
import frc.robot.commands.CMD_AimAtPose;
import frc.robot.commands.CMD_DriveAlign;
import frc.robot.constants.InputConstants;
import frc.robot.swerve.IO_SwerveReal;
import frc.robot.swerve.SUB_Swerve;
import frc.robot.vision.IO_VisionReal;
import frc.robot.vision.IO_VisionSim;
import frc.robot.vision.SUB_Vision;
import java.io.File;

public class RobotContainer {
	private final CommandXboxController driverController;

	private final SUB_Swerve swerve;
	private final SUB_Vision vision;

	private final InputConstants globalInputMap;

	public RobotContainer() {
		driverController = new CommandXboxController(0); // port 0
		globalInputMap = InputConstants.XBOX; // Set the global input map to Xbox Controller

		vision = new SUB_Vision(Robot.isSimulation() ? new IO_VisionSim() : new IO_VisionReal());

		swerve =
				new SUB_Swerve(
						vision, new IO_SwerveReal(new File(Filesystem.getDeployDirectory(), "swerve")));

		configureDefaultCommands();
	}

	private void configureDefaultCommands() {
		// swerve.setDefaultCommand(new CMD_Drive(swerve, driverController, globalInputMap)); Drive
		// normal
		// swerve.setDefaultCommand(new CMD_AimAtSpeaker(swerve, 0.1)); Aim at speaker
		swerve.setDefaultCommand(
				new CMD_DriveAlign(swerve, driverController, globalInputMap)); // Drive and aim at speaker

		// Aim at 0,0 for testing
		driverController.a().onTrue(new CMD_AimAtPose(swerve, new Pose2d(), 0.1));

		// Aim at tag 16 for testing
		driverController.b().onTrue(new CMD_AimAtAprilTag(swerve, 16, 0.1));
	}

	public Command getAutonomousCommand() {
		return swerve.getAutonomousCommand("Test");
	}
}
