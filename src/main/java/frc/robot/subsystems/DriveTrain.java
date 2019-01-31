package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.robot.Encoder1038;
import frc.robot.robot.Spark1038;

public class DriveTrain extends Subsystem {
    public enum driveModes {
        tankDrive, singleArcadeDrive, dualArcadeDrive
    };

    public driveModes currentDriveMode = driveModes.tankDrive;

    private final int LEFT_ENCODER_CHANNEL_A = 0;
    private final int RIGHT_ENCODER_CHANNEL_A = 1;
    private final int LEFT_ENCODER_CHANNEL_B = 3;
    private final int RIGHT_ENCODER_CHANNEL_B = 2;
    public final int ENCODER_COUNTS_PER_REV = 205;
    public final double WHEEL_DIAMETER = 6;

    private final static int LEFT_DRIVE_PORT = 0;
    private final static int RIGHT_DRIVE_PORT = 1;
    public Spark1038 leftDrive1 = new Spark1038(LEFT_DRIVE_PORT);
    public Spark1038 rightDrive1 = new Spark1038(RIGHT_DRIVE_PORT);

    private Encoder1038 leftDriveEncoder = new Encoder1038(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B, false,
            ENCODER_COUNTS_PER_REV, WHEEL_DIAMETER);
    private Encoder1038 rightDriveEncoder = new Encoder1038(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B, false,
            ENCODER_COUNTS_PER_REV, WHEEL_DIAMETER);

    private DifferentialDrive differentialDrive;
    private static DriveTrain driveTrain;

    public static DriveTrain getInstance() {
        if (driveTrain == null) {
            System.out.println("Creating a new DriveTrain");
            driveTrain = new DriveTrain();
        }
        return driveTrain;
    }

    private DriveTrain() {
        differentialDrive = new DifferentialDrive(leftDrive1, rightDrive1);
    }

    // Get and return distance driven by the left of the robot in inches
    public double getLeftDriveEncoderDistance() {
        return leftDriveEncoder.getDistance();
    }

    // Get and return distance driven by the right of the robot in inches
    public double getRightDriveEncoderDistance() {
        return rightDriveEncoder.getDistance();
    }

    // Reset drive encoders
    public void resetEncoder() {
        leftDriveEncoder.reset();
        rightDriveEncoder.reset();
        System.out.println("Encoders reset");
    }

    // Switch between drive modes
    public void driveModeToggler() {

        switch (currentDriveMode) {
        case tankDrive:
            currentDriveMode = driveModes.singleArcadeDrive;
            break;
        case singleArcadeDrive:
            currentDriveMode = driveModes.dualArcadeDrive;
            break;
        case dualArcadeDrive:
            currentDriveMode = driveModes.tankDrive;
            break;
        default:
            System.out.println("Help I have fallen and I can't get up!");
            break;
        }
    }

    // Drive robot with tank controls (input range -1 to 1 for each stick)
    public void tankDrive(double leftStickInput, double rightStickInput) {
        differentialDrive.tankDrive(leftStickInput * 0.57, rightStickInput * 0.57, true);
    }

    // Drive robot using a single stick (input range -1 to 1)
    public void singleAracadeDrive(double speed, double turnValue) {
        differentialDrive.arcadeDrive(speed, turnValue, true);
    }

    // Drive robot using 2 sticks (input ranges -1 to 1)
    public void dualArcadeDrive(double yaxis, double xaxis) {
        differentialDrive.arcadeDrive(yaxis, xaxis, true);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
