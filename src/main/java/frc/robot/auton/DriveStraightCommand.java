package frc.robot.auton;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import frc.robot.subsystems.DriveTrain;

public class DriveStraightCommand extends PIDCommand {

    private static final double dP = 0.000;
    private static final double dI = 0.000;
    private static final double dD = 0.000;
    private static final double MAX_OUTPUT = 1.00;
    private static final double TOLERANCE = 0;
    private DriveTrain drive = DriveTrain.getInstance();
    private PIDController rightDrivePID = getPIDController();
    private PIDController leftDrivePID = getPIDController();

    /**
     * Creates a new instance of the drive straight command with a given setpoint
     * 
     * @param setpoint The setpoint in the wheel diameter's units
     */
    public DriveStraightCommand(double setpoint) {
        super(dP, dI, dD);
        setSetpoint(setpoint);
        rightDrivePID.setAbsoluteTolerance(TOLERANCE);
        rightDrivePID.setOutputRange(-MAX_OUTPUT, MAX_OUTPUT);
        rightDrivePID.setContinuous(false);
        leftDrivePID.setAbsoluteTolerance(TOLERANCE);
        leftDrivePID.setOutputRange(-MAX_OUTPUT, MAX_OUTPUT);
        leftDrivePID.setContinuous(false);
        requires(drive);
    }

    /**
     * Initializes the drive straight command
     */
    public void initialize() {
    }

    /**
     * Executes what should be looped through till the command is finished
     */
    public void execute() {
        rightDrivePID.enable();
        leftDrivePID.enable();
        usePIDOutput(leftDrivePID.get(), rightDrivePID.get());
    }

    /**
     * Runs if the command is interrupted
     */
    public void interrupted() {
        end();
    }

    /**
     * Runs when the command finishes
     */
    public void end() {
        rightDrivePID.reset();
        leftDrivePID.reset();
        drive.tankDrive(0, 0);
    }

    /**
     * Returns whether the command is finished
     */
    public boolean isFinished() {
        return rightDrivePID.onTarget() && leftDrivePID.onTarget();
    }

    @Override
    protected double returnPIDInput() {
        return drive.getRightDriveEncoderDistance();
    }

    /**
     * To be used to drive the robot with the calculated PID values
     * 
     * @param leftDrivePower  The calculated PID value for the left drive
     * @param rightDrivePower The calculated PID value for the right drive
     */
    protected void usePIDOutput(double leftDrivePower, double rightDrivePower) {
        drive.tankDrive(leftDrivePower, rightDrivePower);
    }

    @Override
    protected void usePIDOutput(double output) {
        usePIDOutput(output, rightDrivePID.get());
    }

}