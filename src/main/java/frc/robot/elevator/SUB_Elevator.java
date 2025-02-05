// SUB_Elevator.java
package frc.robot.elevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.superstructure.SuperstructureState;
import org.littletonrobotics.junction.Logger;

public class SUB_Elevator extends SubsystemBase {
    private final IO_ElevatorBase io;
    private final IO_ElevatorBase.ElevatorInputs inputs = new IO_ElevatorBase.ElevatorInputs();
    private SuperstructureState.State localState = SuperstructureState.IDLE;

    public SUB_Elevator(IO_ElevatorBase io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.setPositionM(localState.getHeightM());
        io.updateInputs(inputs);
        Logger.processInputs("Elevator", inputs);
    }

    public void updateLocalState(SuperstructureState.State newLocalState) {
        localState = newLocalState;
    }

    public SuperstructureState.State getCurrentLocalState() {
        return localState;
    }
}