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

    public DriveStraightCommand(double setpoint){
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