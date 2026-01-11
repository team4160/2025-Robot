package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;

public class GoToIntake extends Command{

    private final Elevator elevator;
    private final Arm arm;

    private double elevatorHeight = 0;
    private double armAngle = 0.485;

    public GoToIntake(Elevator elevator, Arm arm) {
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
        // return elevator.aroundHeight(elevatorHeight) && arm.aroundAngle(armAngle);
        return true;
    }

    @Override
    public void end(boolean interrupted)
    {
    }
}

