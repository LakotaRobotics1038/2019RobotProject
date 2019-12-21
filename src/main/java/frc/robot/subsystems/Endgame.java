package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.robot.ArduinoReader;
import frc.robot.robot.CANSpark1038;
import frc.robot.robot.Encoder1038;

public class Endgame {

    private final int FRONT_ENCODER_CHANNEL_A = 4;
    private final int FRONT_ENCODER_CHANNEL_B = 5;
    private final int COUNTS_PER_REVOLUTION = 500;
    private final int WHEEL_DIAMETER = 4;

    private boolean frontDeployed = false;
    private boolean rearDeployed = false;

    // private PIDController endgamePID = getPIDController();
    // private final static double P = .15;
    // private final static double I = .000;
    // private final static double D = .000;

    // private DoubleSolenoid frontCylinders = new DoubleSolenoid(0, 1);
    // private DoubleSolenoid rearCylinders = new DoubleSolenoid(2, 3);
    private static CANSpark1038 rearMotor = new CANSpark1038(57, CANSparkMaxLowLevel.MotorType.kBrushed);
    private CANSpark1038 rearLeadScrewMotor = new CANSpark1038(60, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANEncoder rearLeadScrewEncoder = rearLeadScrewMotor.getEncoder();
    private CANSpark1038 frontLeadScrewMotor = new CANSpark1038(61, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANEncoder frontLeadScrewEncoder = frontLeadScrewMotor.getEncoder();
    private Encoder1038 rearMotorEncoder = new Encoder1038(FRONT_ENCODER_CHANNEL_A, FRONT_ENCODER_CHANNEL_B, false,
            COUNTS_PER_REVOLUTION, WHEEL_DIAMETER);

    private static Endgame endgame;
    private static ArduinoReader arduinoReader = ArduinoReader.getInstance();
    private double rearUpCounts = rearLeadScrewEncoder.getPosition();
    private double frontUpCounts = frontLeadScrewEncoder.getPosition();

    /**
     * Returns the endgame instance created when the robot starts
     * 
     * @return Endgame instance
     */
    public static Endgame getInstance() {
        if (endgame == null) {
            // System.out.println("Creating new Endgame");
            endgame = new Endgame();
        }
        return endgame;
    }

    /**
     * Instantiates endgame object
     */
    private Endgame() {
        // super(P, I, D);
        // endgamePID.setPID(P, I, D);
        // endgamePID.setAbsoluteTolerance(0);
        // endgamePID.setContinuous(false);
        // endgamePID.setOutputRange(-1, 0);
        // endgamePID.setSetpoint(0);
        // retractFront();
        // retractRear();
        rearMotor.restoreFactoryDefaults();
        rearMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }

    /**
     * Deploys front cylinders by switching the solenoid state and sets
     * frontDeployed boolean to true
     */
    public void deployFront(double power) {
        // frontCylinders.set(DoubleSolenoid.Value.kReverse);
        if(frontLeadScrewEncoder.getPosition() > (rearUpCounts - 397)){
            frontLeadScrewMotor.set(power);
        }
        else{
            frontLeadScrewMotor.set(0);
        }
        frontDeployed = true;
    }

    /**
     * Deploys rear cylinders by switching the solenoid state and sets rearDeployed
     * boolean to true
     */
    public void deployRear(double power) {
        // rearCylinders.set(DoubleSolenoid.Value.kReverse);
        // disable();
        if(rearLeadScrewEncoder.getPosition() > (rearUpCounts - 397)){
            rearLeadScrewMotor.set(power);
        }
        else{
            rearLeadScrewMotor.set(0);
            // disable();
        }
        //deployedCounter+=1;
        // System.out.println("deploying:" + deployedCounter);
        rearDeployed = true;
    }

    public void frontRetractUnlimited() {
        frontLeadScrewMotor.set(.5);
    }

    public void rearRetractUnlimited() {
        rearLeadScrewMotor.set(.5);
    }

    public void stopRear() {
        rearLeadScrewMotor.set(0);
        // disable();
        // deployedCounter+=1;
        // System.out.println("stopped: " + deployedCounter);
    }

    public void stopFront() {
        frontLeadScrewMotor.set(0);
        // disable();
    }

    /**
     * Retracts front cylinders by switching the solenoid state and sets
     * frontDeployed boolean to false
     */
    public void retractFront() {
        // frontCylinders.set(DoubleSolenoid.Value.kForward);
        if(frontLeadScrewEncoder.getPosition() < frontUpCounts - 2){
            frontLeadScrewMotor.set(.5);
        }
        else{
            frontLeadScrewMotor.set(0);
        }
        frontDeployed = false;
        // disable();
    }

    /**
     * Retracts rear cylinders by switching the solenoid state and sets rearDeployed
     * boolean to false
     */
    public void retractRear() {
        // rearCylinders.set(DoubleSolenoid.Value.kForward);
        // disable();
        if(rearLeadScrewEncoder.getPosition() < rearUpCounts - 2){
            rearLeadScrewMotor.set(.5);
        }
        else{
            rearLeadScrewMotor.set(0);
        }
        // retractCounter+=1;
        // System.out.println(retractCounter);
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

    public void deployEndgame(double power) {
        this.deployFront(power);
        // enable();
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

    public double getScrewCounts() {
        return rearLeadScrewEncoder.getPosition();
    }

    /**
     * Elevation of the front from the laser sensor to the ground
     * 
     * @return Elevation of the front laser sensor to the ground in cm
     */
    //public int getFrontElevation() {
      //  return arduinoReader.getFrontBottomLaserVal();
   // }

    /**
     * Elevation of the rear from the laser sensor to the ground
     * 
     * @return Elevation of the rear laser sensor to the ground in cm
     */
    public int getRearElevation() {
        return arduinoReader.getRearBottomLaserVal();
    }

    // @Override
    // protected void initDefaultCommand() {

    // }

    // @Override
    // protected double returnPIDInput() {
    //     int frontElevation = arduinoReader.getFrontBottomLaserVal();
    //     int rearElevation = arduinoReader.getRearBottomLaserVal();
    //     return frontElevation - rearElevation;
    // }

    // @Override
    // protected void usePIDOutput(double output) {
    //     this.deployRear(output);
    // }

    // public void disable() {
    //     super.disable();
    // }

}