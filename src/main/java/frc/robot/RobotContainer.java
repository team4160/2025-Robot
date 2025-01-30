// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CMD_Drive;
import frc.robot.commands.CMD_ElevatorDownCoral;
import frc.robot.commands.CMD_ElevatorUpCoral;
import frc.robot.commands.CMD_SetElevator;
import frc.robot.constants.InputConstants;
import frc.robot.elevator.IO_ElevatorReal;
import frc.robot.elevator.SUB_Elevator;
import frc.robot.intake.IO_IntakeReal;
import frc.robot.intake.SUB_Intake;
import frc.robot.misc.RobotState;
import frc.robot.misc.SUB_Led;
import frc.robot.swerve.IO_SwerveReal;
import frc.robot.swerve.SUB_Swerve;
import frc.robot.vision.IO_VisionReal;
import frc.robot.vision.IO_VisionSim;
import frc.robot.vision.SUB_Vision;
import java.io.File;

public class RobotContainer {

	private final CommandXboxController driverController;
	private final CommandXboxController operatorController;

	// private final WebServer webServer;

	private final SUB_Swerve swerve;
	private final SUB_Intake intake;
	private final SUB_Vision vision;
	private final SUB_Elevator elevator;
	// private final SUB_Intake intake;

	private final SUB_Led led;

	private final InputConstants globalInputMap;

	public RobotContainer() {

		//	webServer = new WebServer();

		driverController = new CommandXboxController(0); // port 0
		globalInputMap = InputConstants.TX16S_MAIN; // Set the global input map to Xbox Controller

		operatorController = new CommandXboxController(1);

		vision = new SUB_Vision(Robot.isSimulation() ? new IO_VisionSim() : new IO_VisionReal());

		swerve = new SUB_Swerve(new IO_SwerveReal(new File(Filesystem.getDeployDirectory(), "swerve")));

		intake = new SUB_Intake(new IO_IntakeReal());

		elevator = new SUB_Elevator(new IO_ElevatorReal());

		led = new SUB_Led();

		configureDefaultCommands();
		configureWebserverCommands();
		configurePathPlannerCommands();
		configureButtonBindings();
	}

	private void configureDefaultCommands() {

		// swerve.setDefaultCommand(
		//		new CMD_DriveAlign(swerve, driverController, globalInputMap)); // Drive and aim at speaker

		swerve.setDefaultCommand(new CMD_Drive(swerve, driverController, globalInputMap));

		// elevator.setDefaultCommand(new CMD_ElevatorManual(elevator, operatorController));

		// operatorController.a().onFalse(elevator.setVoltage(0));

		//	operatorController.b().onTrue(elevator.setVoltage(0));

		/*
		 * 		operatorController.a().onTrue(elevator.setV(SUB_Elevator.State.L1_SCORING)); // 0.5m
		operatorController.a().onFalse(elevator.setState(SUB_Elevator.State.STOWED));
		operatorController.b().onTrue(elevator.setState(SUB_Elevator.State.STOWED));
		 */
	}

	private void configureWebserverCommands() {
		//	webServer.registerCommand("T1", swerve.driveToPose(FieldConstants.BLUE_TOP_TOP_LEFT));
		//	webServer.registerCommand("T2", swerve.driveToPose(FieldConstants.BLUE_TOP_TOP_RIGHT));

		// webServer.registerCommand("TL1", swerve.driveToPose(FieldConstants.BLUE_TOP_LEFT_BOTTOM));
		// webServer.registerCommand("TL2", swerve.driveToPose(FieldConstants.BLUE_TOP_LEFT_TOP));
	}

	private void configurePathPlannerCommands() {

		NamedCommands.registerCommand("Intake_Algae", new PrintCommand("Intake Algae"));
	}

	private void configureButtonBindings() {

		// Move elevator up/down for coral scoring
		operatorController.povUp().onTrue(new CMD_ElevatorUpCoral(elevator, led));

		operatorController.povDown().onTrue(new CMD_ElevatorDownCoral(elevator, led));

		// Lower elevator to stowed position
		operatorController.b().onTrue(new CMD_SetElevator(elevator, led, RobotState.State.STOWED));

		operatorController.a().onTrue(intake.setState(SUB_Intake.State.CORAL_STATION));

		operatorController.b().onTrue(intake.setState(SUB_Intake.State.STOWED));

		// Pick up coral from the Source
		operatorController
				.a()
				.onTrue(new CMD_SetElevator(elevator, led, RobotState.State.CORAL_STATION));

		// Climb
		operatorController
				.rightBumper()
				.onTrue(new CMD_SetElevator(elevator, led, RobotState.State.CLIMB));

		// Aim at 0,0 for testing
		// driverController.a().onTrue(new CMD_AimAtPose(swerve, new Pose2d(), 0.1));

		// Aim at tag 16 for testing
		// driverController.b().onTrue(new CMD_AimAtAprilTag(swerve, 16, 0.1));

		/*SequentialCommandGroup
			driverController.a().onTrue(intake.setState(SUB_Intake.State.ALGAE_GROUND));
			driverController.a().toggleOnFalse(intake.setState(SUB_Intake.State.STOWED));
		}
			*/

		/*
		driverController
				.a()
				.onTrue(
						new ParallelCommandGroup(
								intake.setState(SUB_Intake.State.ALGAE_GROUND),
								swerve.driveToPose(FieldConstants.BLUE_TOP_TOP_LEFT)));
								*/
	}

	public Command getAutonomousCommand() {
		return swerve.getAutonomousCommand("R_3L4");
	}
}
