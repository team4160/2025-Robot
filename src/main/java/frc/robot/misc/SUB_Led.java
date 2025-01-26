// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.misc;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.RainbowAnimation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SUB_Led extends SubsystemBase {

	public enum State {
		STOWED(0, 0, 0),
		CORAL_STATION(255, 127, 80),
		ALGAE_GROUND(0, 255, 0),
		ALGAE_REMOVAL(0, 255, 0),
		ALGAE_PROCESSOR(0, 255, 0),
		ALGAE_BARGE(0, 255, 0),
		L1_SCORING(255, 255, 0),
		L2_L3_SCORING(255, 255, 0),
		L4_SCORING(255, 255, 0);
		private final int r, g, b;

		State(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}

	private CANdle candle;
	private static final int LED_COUNT = 300; // Adjust this to match your LED strip length
	private static final double BRIGHTNESS = 0.8;
	private static final double SPEED = 0.7;

	public SUB_Led() {
		candle = new CANdle(11, "canivore");
		CANdleConfiguration config = new CANdleConfiguration();
		config.stripType = LEDStripType.RGB;
		config.brightnessScalar = BRIGHTNESS;
		candle.configAllSettings(config);

		// Set rainbow animation by default
		RainbowAnimation rainbowAnim = new RainbowAnimation(BRIGHTNESS, SPEED, LED_COUNT);
		candle.animate(rainbowAnim);
	}

	@Override
	public void periodic() {}

	public Command setState(State state) {
		return new InstantCommand(
				() -> {
					candle.setLEDs(state.r, state.g, state.b);
				});
	}
}
