package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Intake;


public class IntakeCoral extends Command{

    private final Intake intake;
    private final CommandXboxController operatorController;
    private final CommandXboxController driverController;
    private final Timer timer = new Timer();

    public IntakeCoral(Intake intake, CommandXboxController operatorController, CommandXboxController driverController) {
        this.intake = intake;
        this.operatorController = operatorController;
        this.driverController = driverController;
        addRequirements(intake);
    }

    @Override
    public void initialize()
    {
        intake.set(1);
        timer.restart();
    }

    @Override
    public void execute()
    {
        System.out.println("Intake Current " + intake.getCurrentDrawAmps());
    }

    @Override
    public boolean isFinished()
    {
        if (!timer.hasElapsed(0.5)) return false;
        return intake.getCurrentDrawAmps() > 10;
    }

    @Override
    public void end(boolean interrupted)
    {
        intake.stopIntake();
        timer.stop();
        if (!interrupted) {
            driverController.setRumble(RumbleType.kBothRumble, 0.75);
            operatorController.setRumble(RumbleType.kBothRumble, 0.75);
            new WaitCommand(0.5)
            .andThen(() -> {
                driverController.setRumble(RumbleType.kBothRumble, 0);
                operatorController.setRumble(RumbleType.kBothRumble, 0);
            }).schedule();
        }
    }
}
