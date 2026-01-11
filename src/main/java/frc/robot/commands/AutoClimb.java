package frc.robot.commands;

import com.ctre.phoenix6.controls.VoltageOut;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climb;

public class AutoClimb extends Command {
    private final Climb climb;
    private final XboxController xbox;

    public AutoClimb(Climb climb, XboxController xbox) {
        this.climb = climb;
        this.xbox = xbox;
        addRequirements(climb);
    }

    @Override
    public void initialize()
    {
        climb.getMotor().setControl(new VoltageOut(12));
    }

    @Override
    public void execute()
    {

    }

    @Override
    public boolean isFinished()
    {
        if (Math.abs(xbox.getLeftTriggerAxis()-0.5) > 0.1 || Math.abs(xbox.getRightTriggerAxis()-0.5) > 0.1) return true;
        return climb.getHeight() > 50;
    }

    @Override
    public void end(boolean interrupted)
    {
        climb.setClimb(0);
    }
    
}
