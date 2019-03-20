package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.robot.CANSpark1038;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class DriveTrain extends Subsystem {
    public enum driveModes {
        tankDrive, singleArcadeDrive, dualArcadeDrive
    };

    public driveModes currentDriveMode = driveModes.dualArcadeDrive;

    public final double WHEEL_DIAMETER = 4;

    public DoubleSolenoid GearChangeSolenoid = new DoubleSolenoid(4, 5);
    public boolean isHighGear = false;

    public static CANSpark1038 CANSparkRightFront = new CANSpark1038(53, MotorType.kBrushless);
    public static CANSpark1038 CANSparkRightBack = new CANSpark1038(52, MotorType.kBrushless);
    public static CANSpark1038 CANSparkLeftFront = new CANSpark1038(51, MotorType.kBrushless);
    public static CANSpark1038 CANSparkLeftBack = new CANSpark1038(50, MotorType.kBrushless);

    public CANEncoder CANSparkRightEncoder = CANSparkRightBack.getEncoder();
    public CANEncoder CANSparkLeftEncoder = CANSparkLeftBack.getEncoder();

    private DifferentialDrive differentialDrive;
    private static DriveTrain driveTrain;

    /**
     * Returns the drive train instance created when the robot starts
     * 
     * @return Drive train instance
     */
    public static DriveTrain getInstance() {
        if (driveTrain == null) {
            // System.out.println("Creating a new DriveTrain");
            driveTrain = new DriveTrain();
        }
        return driveTrain;
    }

    /**
     * Instantiates drive train object
     */
    private DriveTrain() {
        CANSparkLeftBack.restoreFactoryDefaults();
        CANSparkLeftFront.restoreFactoryDefaults();
        CANSparkRightBack.restoreFactoryDefaults();
        CANSparkRightFront.restoreFactoryDefaults();
        CANSparkLeftBack.setInverted(true);
        CANSparkLeftFront.setInverted(true);
        CANSparkRightBack.setInverted(true);
        CANSparkRightFront.setInverted(true);
        CANSparkLeftBack.setIdleMode(IdleMode.kBrake);
        CANSparkLeftFront.setIdleMode(IdleMode.kBrake);
        CANSparkRightBack.setIdleMode(IdleMode.kBrake);
        CANSparkRightFront.setIdleMode(IdleMode.kBrake);
        CANSparkRightFront.follow(CANSparkRightBack);
        CANSparkLeftFront.follow(CANSparkLeftBack);
        differentialDrive = new DifferentialDrive(CANSparkLeftBack, CANSparkRightBack);

    }

    /**
     * Gets and returns distance driven by the left of the robot
     * 
     * @return Distance driven by the left of the robot in wheel diameter's units
     */
    public double getLeftDriveEncoderDistance() {
        return CANSparkLeftEncoder.getPosition() * Math.PI * WHEEL_DIAMETER;
    }

    /**
     * Gets and returns distance driven by the right of the robot
     * 
     * @return Distance driven by the right of the robot in wheel diameter's units
     */
    public double getRightDriveEncoderDistance() {
        return CANSparkRightEncoder.getPosition() * Math.PI * WHEEL_DIAMETER;
    }

    /**
     * Gets and returns revolutions driven by the right of the robot
     * 
     * @return Revolutions driven by the right of the robot
     */
    public double getCANSparkRightEncoder() {
        return CANSparkRightEncoder.getPosition() * -1;
    }

    /**
     * Gets and returns revolutions driven by the left of the robot
     * 
     * @return Revolutions driven by the left of the robot
     */
    public double getCANSparkLeftEncoder() {
        return CANSparkLeftEncoder.getPosition() * -1;
    }

    /**
     * Switches robot into high gear by changing solenoid state
     */
    public void highGear() {
        isHighGear = true;
        GearChangeSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Switches robot into low gear by changing solenoid state
     */
    public void lowGear() {
        isHighGear = false;
        GearChangeSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Toggles the drive mode between tank drive, single arcade drive, and dual
     * arcade drive
     */
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
            // System.out.println("Help I have fallen and I can't get up!");
            break;
        }
    }

    /**
     * Drives robot with tank controls (input range -1 to 1 for each stick)
     * 
     * @param leftStickInput  Left joystick input between -1 and 1
     * @param rightStickInput Right joystick input between -1 and 1
     */
    public void tankDrive(double leftStickInput, double rightStickInput) {
        differentialDrive.tankDrive(leftStickInput, rightStickInput, true);
    }

    /**
     * Drives robot with single arcade controls (input range -1 to 1 for each stick)
     * 
     * @param leftStickInput  Left joystick input between -1 and 1
     * @param rightStickInput Right joystick input between -1 and 1
     */
    public void singleAracadeDrive(double speed, double turnValue) {
        differentialDrive.arcadeDrive(speed, turnValue, true);
    }

    /**
     * Drives robot with dual arcade controls (input range -1 to 1 for each stick)
     * 
     * @param leftStickInput  Left joystick input between -1 and 1
     * @param rightStickInput Right joystick input between -1 and 1
     */
    public void dualArcadeDrive(double yaxis, double xaxis) {
        differentialDrive.arcadeDrive(yaxis, xaxis, true);
    }

    @Override
    protected void initDefaultCommand() {

    }
}