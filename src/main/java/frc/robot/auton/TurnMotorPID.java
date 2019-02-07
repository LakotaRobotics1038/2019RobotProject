package frc.robot.auton;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import frc.robot.subsystems.DriveTrain;

public class TurnMotorPID extends PIDCommand {

    private final static double P_INIT = 0.00;
    private final static double I_INIT = 0.00;
    private final static double D_INIT = 0.00;
    private final double OUTPUT_MAX = 1;
    private final double target = 200;
    private final double tolerance = 5;
    private PIDController turnPID = getPIDController();
    private DriveTrain robotDrive = DriveTrain.getInstance();

    public TurnMotorPID(double setpoint, double P, double I, double D){
        super(P, I, D);
        setSetpoint(setpoint);
        turnPID.setAbsoluteTolerance(tolerance);
        turnPID.setContinuous(false);
        turnPID.setOutputRange(-OUTPUT_MAX, OUTPUT_MAX);
        requires(robotDrive);
    }

    @Override
    protected void execute() {
        turnPID.enable();
    }
    
    @Override
    protected void end(){
        turnPID.reset();
    }

    @Override
    protected double returnPIDInput() {
        return robotDrive.leftDriveEncoder.get();
    }

    @Override
    protected void usePIDOutput(double output) {
        robotDrive.tankDrive(0, output);
    }

    @Override
    protected boolean isFinished() {
        return turnPID.onTarget();
    }

}