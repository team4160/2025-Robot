// SUB_Led.java
package frc.robot.util;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.StrobeAnimation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.superstructure.SuperstructureState;

public class SUB_Led extends SubsystemBase {
    private CANdle candle;
    private static final int LED_COUNT = 300;
    private static final int DEFAULT_ANIMATION_SLOT = 0;
    private SuperstructureState.State localState = SuperstructureState.IDLE;

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

    public void updateLocalState(SuperstructureState.State newLocalState) {
        localState = newLocalState;
        
        if (localState == SuperstructureState.IDLE) {
            setFullStripColor(255, 0, 0);
        } else if (localState == SuperstructureState.L1_SCORING) {
            setFullStripColor(255, 0, 0);
        } else if (localState == SuperstructureState.L2_SCORING) {
            setFullStripColor(245, 179, 66);
        } else if (localState == SuperstructureState.L3_SCORING) {
            setFullStripColor(255, 255, 0);
        } else if (localState == SuperstructureState.L4_SCORING) {
            setFullStripColor(0, 255, 0);
        } else if (localState == SuperstructureState.CORAL_STATION) {
            setFullStripColor(0, 0, 255);
        } else if (localState == SuperstructureState.CLIMB) {
            candle.clearAnimation(DEFAULT_ANIMATION_SLOT);
            candle.animate(
                new StrobeAnimation(240, 10, 180, 0, 98.0 / 256.0, LED_COUNT), 
                DEFAULT_ANIMATION_SLOT
            );
        } else {
            setFullStripColor(255, 0, 0);
        }
    }

    public SuperstructureState.State getCurrentLocalState() {
        return localState;
    }
}