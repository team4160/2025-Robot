// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.misc;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.StrobeAnimation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.state.GlobalRobotState;

public class SUB_Led extends SubsystemBase {
	private CANdle candle;
	private static final int LED_COUNT = 300;
	private static final int DEFAULT_ANIMATION_SLOT = 0;

	private GlobalRobotState.State localState = GlobalRobotState.State.STOWED;

	public SUB_Led() {
		candle = new CANdle(11, "canivore");
		CANdleConfiguration config = new CANdleConfiguration();
		config.stripType = LEDStripType.RGB;
		config.brightnessScalar = 1.0;
		candle.configAllSettings(config);
		setFullStripColor(255, 0, 0); // Default red
	}

	@Override
	public void periodic() {}

	private void setFullStripColor(int r, int g, int b) {
		candle.clearAnimation(DEFAULT_ANIMATION_SLOT);
		candle.setLEDs(r, g, b, 0, 0, LED_COUNT);
	}

	public void updateLocalState(GlobalRobotState.State newLocalState) {
		switch (localState) {
			case STOWED_CORAL:
				setFullStripColor(255, 255, 255);
				break;
			case STOWED:
				setFullStripColor(255, 0, 0);
				break;
			case L1_SCORING:
				setFullStripColor(255, 0, 0);
				break;
			case L2_SCORING:
				setFullStripColor(245, 179, 66);
				break;
			case L3_SCORING:
				setFullStripColor(255, 255, 0);
				break;
			case L4_SCORING:
				setFullStripColor(0, 255, 0);
				break;
			case CORAL_STATION:
				setFullStripColor(0, 0, 255);
				break;
			case CLIMB:
				candle.clearAnimation(DEFAULT_ANIMATION_SLOT);
				candle.animate(
						new StrobeAnimation(240, 10, 180, 0, 98.0 / 256.0, LED_COUNT), DEFAULT_ANIMATION_SLOT);
				break;
			default:
				setFullStripColor(255, 0, 0);
				break;
		}
	}

	public GlobalRobotState.State getCurrentLocalState() {
		return localState;
	}
}
