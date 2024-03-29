package frc.robot.depricated;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import frc.robot.robot.Encoder1038;
import frc.robot.subsystems.DriveTrain;

public class MotorTurnCommand extends PIDCommand {

    private static final double mP = 0.050;
    private static final double mI = 0.000;
    private static final double mD = 0.000;
    private DriveTrain drive = DriveTrain.getInstance();
    private static final double TOLERANCE = 0.5;
    private static final double MAX_OUTPUT = 1.0;
    private PIDController drivePID = getPIDController();
    private Encoder1038 motorEncoder = new Encoder1038(0, 1, false, 220, 1);

    /**
     * Instantiates the motor turn command with the designated distance to move the
     * motor
     * 
     * @param setpoint The distance the motor should move in the wheel diameter's
     *                 units
     */
    public MotorTurnCommand(double setpoint) {
        super(mP, mI, mD);
        setSetpoint(setpoint);
        drivePID.setAbsoluteTolerance(TOLERANCE);
        drivePID.setOutputRange(-MAX_OUTPUT, MAX_OUTPUT);
        drivePID.setContinuous(false);
        requires(drive);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        usePIDOutput(drivePID.get());
    }

    @Override
    public void interrupted() {
        end();
    }

    @Override
    public void end() {
        drivePID.reset();
    }

    @Override
    public boolean isFinished() {
        return drivePID.onTarget();
    }

    @Override
    protected double returnPIDInput() {
        return drive.getRightDriveEncoderDistance();
    }

    @Override
    protected void usePIDOutput(double output) {
        drive.tankDrive(0, output);
    }
}