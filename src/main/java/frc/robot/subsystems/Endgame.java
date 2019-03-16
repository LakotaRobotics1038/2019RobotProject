package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.robot.ArduinoReader;
import frc.robot.robot.Encoder1038;

public class Endgame extends Subsystem {

    private final int FRONT_ENCODER_CHANNEL_A = 4;
    private final int FRONT_ENCODER_CHANNEL_B = 5;
    private final int COUNTS_PER_REVOLUTION = 500;
    private final int WHEEL_DIAMETER = 4;

    private boolean frontDeployed = false;
    private boolean rearDeployed = false;

    private DoubleSolenoid frontCylinders = new DoubleSolenoid(0, 1);
    // private DoubleSolenoid rearCylinders = new DoubleSolenoid(2, 3);
    private static CANSparkMax rearMotor = new CANSparkMax(57, CANSparkMaxLowLevel.MotorType.kBrushed);
    // private CANSparkMax leadScrewMotor = new CANSparkMax(60, CANSparkMaxLowLevel.MotorType.kBrushed);
    private Encoder1038 rearMotorEncoder = new Encoder1038(FRONT_ENCODER_CHANNEL_A, FRONT_ENCODER_CHANNEL_B, false,
            COUNTS_PER_REVOLUTION, WHEEL_DIAMETER);

    private static Endgame endgame;
    private static ArduinoReader arduinoReader = ArduinoReader.getInstance();

    /**
     * Returns the endgame instance created when the robot starts
     * 
     * @return Endgame instance
     */
    public static Endgame getInstance() {
        if (endgame == null) {
            System.out.println("Creating new Endgame");
            endgame = new Endgame();
        }
        return endgame;
    }

    /**
     * Instantiates endgame object
     */
    private Endgame() {
        deployFront();
        retractRear();
        rearMotor.restoreFactoryDefaults();
        rearMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }

    /**
     * Deploys front cylinders by switching the solenoid state and sets
     * frontDeployed boolean to true
     */
    public void deployFront() {
        frontCylinders.set(DoubleSolenoid.Value.kReverse);
        frontDeployed = true;
    }

    /**
     * Deploys rear cylinders by switching the solenoid state and sets rearDeployed
     * boolean to true
     */
    public void deployRear() {
        // rearCylinders.set(DoubleSolenoid.Value.kReverse);
        // leadScrewMotor.set(.5);
        rearDeployed = true;
    }

    public void stopRear() {
        // leadScrewMotor.set(0);
    }

    /**
     * Retracts front cylinders by switching the solenoid state and sets
     * frontDeployed boolean to false
     */
    public void retractFront() {
        frontCylinders.set(DoubleSolenoid.Value.kForward);
        frontDeployed = false;
    }

    /**
     * Retracts rear cylinders by switching the solenoid state and sets rearDeployed
     * boolean to false
     */
    public void retractRear() {
        // rearCylinders.set(DoubleSolenoid.Value.kForward);
        rearDeployed = false;
    }

    /**
     * Sets rear endgame motor to given motor power
     * 
     * @param power Motor power between -1 and 1
     */
    public void setRearMotor(double power) {
        rearMotor.set(-power);
    }

    /**
     * Whether the front is deployed
     * 
     * @return Returns true when the front is deployed and false when it is not
     */
    public boolean getIsFrontDeployed() {
        return frontDeployed;
    }

    /**
     * Whether the rear is deployed
     * 
     * @return Returns true when the rear is deployed and false when it is not
     */
    public boolean getIsRearDeployed() {
        return rearDeployed;
    }

    /**
     * Gets encoder counts from endgame motor
     * 
     * @return Encoder counts the endgame motor is currently at
     */
    public int getEncoderCounts() {
        return rearMotorEncoder.get();
    }

    /**
     * Gets the distance the encoder has traveled in the units of the wheel diameter
     * 
     * @return Distance encoder has traveled based on wheel diameter (in inches)
     */
    public double getEncoderDistance() {
        return rearMotorEncoder.getDistance();
    }

    /**
     * Encoder velocity in wheel diameter units per second
     * 
     * @return Velocity in wheel diameter units per second
     */
    public double getEncoderVelocity() {
        return rearMotorEncoder.getRate();
    }

    /**
     * Elevation of the front from the laser sensor to the ground
     * 
     * @return Elevation of the front laser sensor to the ground in cm
     */
    public int getFrontElevation() {
        return arduinoReader.getFrontBottomLaserVal();
    }

    /**
     * Elevation of the rear from the laser sensor to the ground
     * 
     * @return Elevation of the rear laser sensor to the ground in cm
     */
    public int getRearElevation() {
        return arduinoReader.getRearBottomLaserVal();
    }

    @Override
    protected void initDefaultCommand() {

    }

}