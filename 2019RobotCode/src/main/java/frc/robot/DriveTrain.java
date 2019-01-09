import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrain {
    public enum driveModes { tankDrive, singleArcadeDrive, dualArcadeDrive };
	public driveModes currentDriveMode = driveModes.tankDrive;
    private driveModes prevDriveMode = currentDriveMode;
    
	private final int LEFT_ENCODER_CHANNEL_A = 0;
	private final int RIGHT_ENCODER_CHANNEL_A = 2;
	private final int LEFT_ENCODER_CHANNEL_B = 1;
	private final int RIGHT_ENCODER_CHANNEL_B = 3;
	public final int ENCODER_COUNTS_PER_REV = 205;
	public final double WHEEL_DIAMETER = 6;

	private final static int LEFT_DRIVE_PORT_1 = 10;
	private final static int LEFT_DRIVE_PORT_2 = 11;
	private final static int RIGHT_DRIVE_PORT_1 = 12;
	private final static int RIGHT_DRIVE_PORT_2 = 13;
	public Spark leftDrive1 = new Spark(LEFT_DRIVE_PORT_1);
	private Spark leftDrive2 = new Spark(LEFT_DRIVE_PORT_2);
	public Spark rightDrive1 = new Spark(RIGHT_DRIVE_PORT_1);
	private Spark rightDrive2 = new Spark(RIGHT_DRIVE_PORT_2);
	
	private Encoder1038 leftDriveEncoder = new Encoder1038(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B, false, ENCODER_COUNTS_PER_REV, WHEEL_DIAMETER);
	private Encoder1038 rightDriveEncoder = new Encoder1038(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B, false, ENCODER_COUNTS_PER_REV, WHEEL_DIAMETER);
    
    private boolean PTOisEngaged = false;

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
		leftDrive2.follow(leftDrive1);
		rightDrive2.follow(rightDrive1);
        differentialDrive = new DifferentialDrive(leftDrive1, rightDrive1);
	}
    
    // Get and return encoder counts driven by the left of the robot
    public int getLeftDriveEncoderCount() {
        return leftDriveEncoder.getCount();
        System.out.println("Left encoder count returned");
    }
    
    // Get and return encoder counts driven by the right of the robot
    public int getRightDriveEncoderCount() {
        return rightDriveEncoder.getCount();
        System.out.println("Right encoder count returned");
    }

    // Get and return distance driven by the left of the robot in inches
    public double getLeftDriveEncoderDistance() {
        return leftDriveEncoder.getDistance();
        System.out.println("Left drive distance returned");
    }

    // Get and return distance driven by the right of the robot in inches
    public double getRightDriveEncoderDistance() {
        return rightDriveEncoder.getDistance();
        System.out.println("Right drive distance returned");
    }

    // Reset drive encoders
    public void resetEncoder() {
        leftDriveEncoder.reset();
        rightDriveEncoder.reset();
        System.out.println("Encoders reset");
    }

    // Set drive train to brake
    public void setBrakeMode() {
		leftDrive1.setNeutralMode(NeutralMode.Brake);
		leftDrive2.setNeutralMode(NeutralMode.Brake);
		rightDrive1.setNeutralMode(NeutralMode.Brake);
		rightDrive2.setNeutralMode(NeutralMode.Brake);
		System.out.println("Brake Mode");
    }
    
    // Set drive train to coast mode
    public void setCoastMode() {
		leftDrive1.setNeutralMode(NeutralMode.Coast);
		leftDrive2.setNeutralMode(NeutralMode.Coast);
		rightDrive1.setNeutralMode(NeutralMode.Coast);
		rightDrive2.setNeutralMode(NeutralMode.Coast);
		System.out.println("Coast Mode");
    }
    
    // Switch between drive modes
    public void driveModeController() {

        if (currentDriveMode == driveModes.tankDrive && prevDriveMode != driveModes.tankDrive) {
			currentDriveMode = driveModes.dualArcadeDrive;
            prevDriveMode = currentDriveMode;
            System.out.println("Toggled dualAracadeDrive mode");
		} else if (currentDriveMode == driveModes.dualArcadeDrive && prevDriveMode != driveModes.dualArcadeDrive) {
			currentDriveMode = driveModes.singleArcadeDrive;
            prevDriveMode = currentDriveMode;
            System.out.println("Toggled singleAracadeDrive mode");
		} else if (currentDriveMode == driveModes.singleArcadeDrive && prevDriveMode != driveModes.singleArcadeDrive) {
			currentDriveMode = driveModes.tankDrive;	
            prevDriveMode = currentDriveMode;
            System.out.println("Toggled tankDrive mode");
		}
    }

    // Drive robot with tank controls (input range -1 to 1 for each stick)
	public void tankDrive(double leftStickInput, double rightStickInput) {
		if (PTOisEngaged) {
			differentialDrive.tankDrive(-Math.abs(leftStickInput), -Math.abs(leftStickInput), true);
        } else {
            differentialDrive.tankDrive(leftStickInput, rightStickInput, true);
        }
    }
    
    // Drive robot using a single stick (input range -1 to 1)
    public void singleAracadeDrive(double speed, double turnValue) {
        if (PTOisEngaged) {
            differentialDrive.arcadeDrive(-Math.abs(speed), 0, true);
        } else {
            differentialDrive.arcadeDrive(speed, turnValue, true);
        }
    }

    // Drive robot using 2 sticks (input ranges -1 to 1)
    public void dualArcadeDrive(double yaxis, double xaxis) {
		if (PTOisEngaged) {
			differentialDrive.arcadeDrive(-Math.abs(yaxis), 0, true);
        } else {
			differentialDrive.arcadeDrive(yaxis, xaxis, true);
        }
    }
}