package frc.robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;

public class DriveStraightCommand extends PIDCommand {

    private static final double dP = 0.000;
    private static final double dI = 0.000;
    private static final double dD = 0.000;
    private static final double MAX_OUTPUT_RANGE = 1.00;
    private static final double TOLERANCE = 0;
    private DriveTrain drive = DriveTrain.getInstance();
    private PIDController rightDrivePID = getPIDController();
    private PIDController leftDrivePID = getPIDController();

    public DriveStraightCommand(double setpoint){
        super(dP, dI, dD);
        requires(drive);
        setSetpoint(setpoint);
        rightDrivePID.setAbsoluteTolerance(TOLERANCE);
        rightDrivePID.setOutputRange(-MAX_OUTPUT_RANGE, MAX_OUTPUT_RANGE);
        rightDrivePID.setContinuous(false);
        leftDrivePID.setAbsoluteTolerance(TOLERANCE);
        leftDrivePID.setOutputRange(-MAX_OUTPUT_RANGE, MAX_OUTPUT_RANGE);
        leftDrivePID.setContinuous(false);
    }

    public void initialize(){
        drive.resetEncoders();
    }

    public void execute(){
        rightDrivePID.enable();
        leftDrivePID.enable();
        usePIDOutput(leftDrivePID.get(), rightDrivePID.get());
    }

    public void interrupted() {
        end();
    }

    public void end(){
        rightDrivePID.reset();
        leftDrivePID.reset();
        drive.tankDrive(0, 0);
    }

    public boolean isFinished(){
        return rightDrivePID.onTarget() && leftDrivePID.onTarget();
    }

    @Override
    protected double returnPIDInput() {
        return drive.getRightDriveEncoderDistance();
    }

    protected void usePIDOutput(double leftDrivePower, double rightDrivePower){
        drive.tankDrive(leftDrivePower, rightDrivePower);
    }
    @Override
    protected void usePIDOutput(double output) {
        usePIDOutput(output, rightDrivePID.get());
    }

}