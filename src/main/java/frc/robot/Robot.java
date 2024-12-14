// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot;

import com.reduxrobotics.canand.CanandEventLoop;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.constants.RobotConstants;
import frc.robot.util.AlertManager;
import org.littletonrobotics.junction.LoggedRobot;

public class Robot extends LoggedRobot {
	private Command m_autonomousCommand;

	private final RobotContainer m_robotContainer;

	public Robot() {
		m_robotContainer = new RobotContainer();

		// Force the Redux server to start on port 7244 on the RoboRIO
		// To use download Redux Alchemist navigate to settings then enter RoboRIO IP:
		// roboRIO-2106-FRC.local
		if (RobotConstants.FORCE_REDUX_SERVER_ON) {
			CanandEventLoop.getInstance();
		}
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {
		// Check battery voltage when disabled
		double voltage = RobotController.getBatteryVoltage();
		if (voltage <= RobotConstants.BATTERY_VOLTAGE_CRITICAL) {
			AlertManager.setAlert(AlertManager.Alerts.CRITICAL_BATTERY_ON_END, true);
		}
	}

	@Override
	public void disabledPeriodic() {}

	@Override
	public void disabledExit() {}

	@Override
	public void autonomousInit() {
		m_autonomousCommand = m_robotContainer.getAutonomousCommand();

		if (m_autonomousCommand != null) {
			m_autonomousCommand.schedule();
		}
	}

	@Override
	public void autonomousPeriodic() {}

	@Override
	public void autonomousExit() {}

	@Override
	public void teleopInit() {
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}

		// Check battery voltage at teleop start
		double voltage = RobotController.getBatteryVoltage();
		if (voltage <= RobotConstants.BATTERY_VOLTAGE_CRITICAL) {
			AlertManager.setAlert(AlertManager.Alerts.CRITICAL_BATTERY_ON_START, true);
		}
	}

	@Override
	public void teleopPeriodic() {}

	@Override
	public void teleopExit() {}

	@Override
	public void testInit() {

		CommandScheduler.getInstance().cancelAll();
	}

	@Override
	public void testPeriodic() {}

	@Override
	public void testExit() {}
}
