package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.robot.Encoder1038;
import frc.robot.robot.CANSpark1038;
import frc.robot.robot.Spark1038;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;


public class DriveTrain extends Subsystem {
    public enum driveModes {
        tankDrive, singleArcadeDrive, dualArcadeDrive
    };

    public driveModes currentDriveMode = driveModes.dualArcadeDrive;

    private final int LEFT_ENCODER_CHANNEL_A = 2;
    private final int RIGHT_ENCODER_CHANNEL_A = 1;
    private final int LEFT_ENCODER_CHANNEL_B = 3;
    private final int RIGHT_ENCODER_CHANNEL_B = 0;
    public final int ENCODER_COUNTS_PER_REV = 205;
    public final double WHEEL_DIAMETER = 6;

    public DoubleSolenoid GearChangeSolenoid = new DoubleSolenoid(4, 5);
    public boolean isHighGear = false;
    

    // private final static int LEFT_DRIVE_PORT = 1;
    // private final static int RIGHT_DRIVE_PORT = 0;
    // public Spark1038 leftDrive1 = new Spark1038(LEFT_DRIVE_PORT);
    // public Spark1038 rightDrive1 = new Spark1038(RIGHT_DRIVE_PORT);
    // public CANSpark1038 Drive1 = new CANSpark1038(0);
    // public CANEncoder Drive1Encoder = Drive1.getEncoder();

    public static CANSpark1038 CANSparkRightFront = new CANSpark1038(51, MotorType.kBrushless);
    public static CANSpark1038 CANSparkRightBack = new CANSpark1038(50, MotorType.kBrushless);
    public static CANSpark1038 CANSparkLeftFront = new CANSpark1038(53, MotorType.kBrushless);
    public static CANSpark1038 CANSparkLeftBack = new CANSpark1038(52, MotorType.kBrushless);

    public CANEncoder CANSparkRightEncoder = CANSparkRightBack.getEncoder();
    public CANEncoder CANSparkLeftEncoder = CANSparkLeftBack.getEncoder();


    /*public Encoder1038 leftDriveEncoder = new Encoder1038(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B, false,
            ENCODER_COUNTS_PER_REV, WHEEL_DIAMETER);
    public Encoder1038 rightDriveEncoder = new Encoder1038(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B, true,
            ENCODER_COUNTS_PER_REV, WHEEL_DIAMETER);
    */

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
        CANSparkLeftBack.restoreFactoryDefaults();
        CANSparkLeftFront.restoreFactoryDefaults();
        CANSparkRightBack.restoreFactoryDefaults();
        CANSparkRightFront.restoreFactoryDefaults();
        CANSparkLeftBack.setIdleMode(IdleMode.kBrake);
        CANSparkLeftFront.setIdleMode(IdleMode.kBrake);
        CANSparkRightBack.setIdleMode(IdleMode.kBrake);
        CANSparkRightFront.setIdleMode(IdleMode.kBrake);
        CANSparkRightFront.follow(CANSparkRightBack);
        CANSparkLeftFront.follow(CANSparkLeftBack);
        differentialDrive = new DifferentialDrive(CANSparkLeftBack, CANSparkRightBack);
       
    }

    // Get and return distance driven by the left of the robot in inches
    public double getLeftDriveEncoderDistance() {
        return CANSparkLeftEncoder.getPosition() * Math.PI * WHEEL_DIAMETER;
    }

    // Get and return distance driven by the right of the robot in inches
    public double getRightDriveEncoderDistance() {
        return CANSparkRightEncoder.getPosition() * Math.PI *WHEEL_DIAMETER;
    }
    
    public double getCANSparkRightEncoder() {
                return CANSparkRightEncoder.getPosition() * -1;
    }

    public double getCANSparkLeftEncoder() {
        return CANSparkLeftEncoder.getPosition() * -1;
}

    //Pneumatics
    public void highGear() {
		isHighGear = true;
        GearChangeSolenoid.set(DoubleSolenoid.Value.kForward);
        System.out.println("HIGH GEEEEEEEEEEAAAAAAAAAAARRRRRRRRRRRRR");
	}
	
	public void lowGear() {
		isHighGear = false;
        GearChangeSolenoid.set(DoubleSolenoid.Value.kReverse);
        System.out.println("LOW GEEEEEEEEEEAAAAAAAAAAARRRRRRRRRRRRR");
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