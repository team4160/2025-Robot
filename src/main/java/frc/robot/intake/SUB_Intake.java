// SUB_Intake.java
package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.superstructure.SuperstructureState;
import org.littletonrobotics.junction.Logger;

public class SUB_Intake extends SubsystemBase {
    private final IO_IntakeBase io;
    private final IO_IntakeBase.IntakeInputs inputs = new IO_IntakeBase.IntakeInputs();
    private SuperstructureState.State localState = SuperstructureState.IDLE;

    public SUB_Intake(IO_IntakeBase io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.setArmAngle(localState.getDeg());
        io.setWheelSpeed(localState.getSpeed())
        io.updateInputs(inputs);
        Logger.processInputs("Intake", inputs);
    }

    public void updateLocalState(SuperstructureState.State newLocalState) {
        localState = newLocalState;
    }

    public SuperstructureState.State getCurrentLocalState() {
        return localState;
    }
}