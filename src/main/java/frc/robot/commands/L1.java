package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;

public class L1 extends Command{

    private final Elevator elevator;
    private final Arm arm;

    //values are the same as GoToIntake (L1 wasnt used so another intake button without auto wheels is nice)
    private double elevatorHeight = 0;
    private double armAngle = 0.472;
    
    public L1(Elevator elevator, Arm arm) {
        this.elevator = elevator;
        this.arm = arm;
        addRequirements(elevator, arm);
    }

    @Override
    public void initialize()
    {
        elevator.reachGoal(elevatorHeight);
        arm.setArmAngle(armAngle);
    }

    @Override
    public void execute()
    {

    }

    @Override
    public boolean isFinished()
    {
        return elevator.aroundHeight(elevatorHeight) && arm.aroundAngle(armAngle);
    }

    @Override
    public void end(boolean interrupted)
    {
        System.out.println("L1 finished");
    }
}
