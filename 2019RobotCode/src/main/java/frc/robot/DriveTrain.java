package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrain {
    public enum driveModes { tankDrive, singleArcadeDrive, dualArcadeDrive };
	public driveModes currentDriveMode = driveModes.tankDrive;
    private driveModes prevDriveMode = currentDriveMode;
    
	private final int LEFT_ENCODER_CHANNEL_A = 0;
	private final int RIGHT_ENCODER_CHANNEL_A = 1;
	private final int LEFT_ENCODER_CHANNEL_B = 3;
	private final int RIGHT_ENCODER_CHANNEL_B = 2;
	public final int ENCODER_COUNTS_PER_REV = 205;
	public final double WHEEL_DIAMETER = 6;

	private final static int LEFT_DRIVE_PORT_1 = 0;
	private final static int LEFT_DRIVE_PORT_2 = 3;
	private final static int RIGHT_DRIVE_PORT_1 = 2;
	private final static int RIGHT_DRIVE_PORT_2 = 13;
	public Spark1038 leftDrive1 = new Spark1038(LEFT_DRIVE_PORT_1);
	private Spark1038 leftDrive2 = new Spark1038(LEFT_DRIVE_PORT_2);
	public Spark1038 rightDrive1 = new Spark1038(RIGHT_DRIVE_PORT_1);
	private Spark1038 rightDrive2 = new Spark1038(RIGHT_DRIVE_PORT_2);
	
	private Encoder1038 leftDriveEncoder = new Encoder1038(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B, false, ENCODER_COUNTS_PER_REV, WHEEL_DIAMETER);
	private Encoder1038 rightDriveEncoder = new Encoder1038(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B, false, ENCODER_COUNTS_PER_REV, WHEEL_DIAMETER);

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
        if (currentDriveMode == driveModes.tankDrive && prevDriveMode != driveModes.tankDrive) {
			currentDriveMode = driveModes.dualArcadeDrive;
			prevDriveMode = currentDriveMode;
		}	
		else if (currentDriveMode == driveModes.dualArcadeDrive && prevDriveMode != driveModes.dualArcadeDrive) {
			currentDriveMode = driveModes.singleArcadeDrive;
			prevDriveMode = currentDriveMode;
		}			
		else if (currentDriveMode == driveModes.singleArcadeDrive && prevDriveMode != driveModes.singleArcadeDrive) {
			currentDriveMode = driveModes.tankDrive;	
			prevDriveMode = currentDriveMode;
		}
    }

    // Drive robot with tank controls (input range -1 to 1 for each stick)
	public void tankDrive(double leftStickInput, double rightStickInput) {
        differentialDrive.tankDrive(leftStickInput, rightStickInput, true);
    }
    
    // Drive robot using a single stick (input range -1 to 1)
    public void singleAracadeDrive(double speed, double turnValue) {
            differentialDrive.arcadeDrive(speed, turnValue, true);
    }

    // Drive robot using 2 sticks (input ranges -1 to 1)
    public void dualArcadeDrive(double yaxis, double xaxis) {
			differentialDrive.arcadeDrive(yaxis, xaxis, true);
    }
}