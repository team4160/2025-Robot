// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.util;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.superstructure.SuperstructureState;

public class SUB_Led extends SubsystemBase {
	private static final int LED_COUNT = 30;
	// private static final int LED_COUNT = 30 * 2;
	private static final Distance LedSpacing = Meters.of(1 / 30.0);
	private final AddressableLED led;
	private final AddressableLEDBuffer ledBuffer;
	private final AddressableLEDBufferView viewl;
	private final AddressableLEDBufferView viewr;
	private final LEDPattern rainbow =
			LEDPattern.rainbow(255, 128).scrollAtAbsoluteSpeed(MetersPerSecond.of(0.5), LedSpacing);
	private SuperstructureState.State localState = SuperstructureState.IDLE;

	public SUB_Led() {
		led = new AddressableLED(9);
		led.setBitTiming(369, 769, 769, 369); // timings for WS2813
		led.setLength(LED_COUNT);
		ledBuffer = new AddressableLEDBuffer(LED_COUNT);
		viewl = ledBuffer.createView(0, LED_COUNT / 2 - 1);
		viewr = ledBuffer.createView(LED_COUNT / 2, LED_COUNT - 1).reversed();

		led.start();
	}

	@Override
	public void periodic() {
		rainbow.applyTo(viewl);
		rainbow.applyTo(viewr);
		led.setData(ledBuffer);
	}

	public void updateLocalState(SuperstructureState.State newLocalState) {
		localState = newLocalState;
	}

	public SuperstructureState.State getCurrentLocalState() {
		return localState;
	}
}
