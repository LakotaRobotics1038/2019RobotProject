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

    /**
     * Instantiates the turn motor with PID command
     * 
     * @param setpoint The distance to turn the motorin counts
     * @param P        The P value
     * @param I        The I value
     * @param D        The D value
     */
    public TurnMotorPID(double setpoint, double P, double I, double D) {
        super(P, I, D);
        setSetpoint(setpoint);
        turnPID.setAbsoluteTolerance(tolerance);
        turnPID.setContinuous(false);
        turnPID.setOutputRange(-OUTPUT_MAX, OUTPUT_MAX);
        requires(robotDrive);
    }

    @Override
    protected void initialize() {
        System.out.println("Created new TurnMotorPID");
    }

    @Override
    protected void execute() {
        turnPID.enable();
        System.out.println(turnPID.get());
        this.usePIDOutput(turnPID.get());
    }

    @Override
    protected void end() {
        turnPID.reset();
        System.out.println("Destroyed TurnMotorPID" + " Ended at: " + robotDrive.getCANSparkLeftEncoder());
    }

    @Override
    protected double returnPIDInput() {
        return robotDrive.getCANSparkLeftEncoder();
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