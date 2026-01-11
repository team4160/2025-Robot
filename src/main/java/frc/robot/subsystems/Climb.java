package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.util.datalog.IntegerLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.ClimbConstants;
import frc.robot.Utils;

public class Climb extends SubsystemBase {
    private final TalonFX climbMotor = new TalonFX(ClimbConstants.CLIMB_MOTOR_ID);
    private final TalonFXConfiguration climbConfig = new TalonFXConfiguration();
    private final LaserCan laserCan = new LaserCan(1);
    private final IntegerLogEntry laserLog = new IntegerLogEntry(DataLogManager.getLog(), "/climb/laserHeight");

    public Climb() {
        climbConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        climbConfig.CurrentLimits.SupplyCurrentLimit = 40;
        climbConfig.CurrentLimits.SupplyCurrentLowerLimit = 80;
        climbConfig.CurrentLimits.SupplyCurrentLowerTime = 0.1;

        climbConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.25;

        climbConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        climbConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        climbConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = false;
        climbConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = false;
        climbConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold = ClimbConstants.FORWARD_LIMIT;
        climbConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold = ClimbConstants.REVERSE_LIMIT;
        
        climbConfig.Slot0.kG = ClimbConstants.kG;
        climbConfig.Slot0.kV = ClimbConstants.kV;
        climbConfig.Slot0.kA = ClimbConstants.kA;
        climbConfig.Slot0.kP = ClimbConstants.kP;
        climbConfig.Slot0.kI = ClimbConstants.kI;
        climbConfig.Slot0.kD = ClimbConstants.kD;

        climbMotor.getConfigurator().apply(climbConfig);
    }

    public void setClimb(double speed) {
        climbMotor.setControl(new VoltageOut(speed * 12));
    }

    public Command manualClimb(CommandXboxController xbox){
        return run(() -> {
            double speed = xbox.getLeftTriggerAxis() - xbox.getRightTriggerAxis();
            setClimb(Utils.sensitivity(speed, 0.69));
        });
    }

    public TalonFX getMotor() {
        return climbMotor;
    }

    public int getHeight() {
        return laserCan.getMeasurement().distance_mm;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Climb position", climbMotor.getPosition().getValueAsDouble());
        SmartDashboard.putNumber("Climb Current", climbMotor.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Climb Height", laserCan.getMeasurement().distance_mm);
        DataLogManager.getLog().appendInteger(0, 0, 0);
    }
}
