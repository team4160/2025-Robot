package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class ExtakeCoral extends Command{

    private final Intake intake;
    private final Timer timer = new Timer();

    public ExtakeCoral(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void initialize()
    {
        System.out.println("ExtakeCoral");
        intake.set(-1);
        timer.start();
    }

    @Override
    public void execute()
    {

    }

    @Override
    public boolean isFinished()
    {
        return timer.hasElapsed(1);
    }

    @Override
    public void end(boolean interrupted)
    {
        intake.stopIntake();
        timer.restart();
    }
}
