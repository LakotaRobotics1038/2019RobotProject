package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.robot.robot.ArduinoReader;
import frc.robot.robot.CANSpark1038;
import frc.robot.robot.Encoder1038;

public class Endgame extends PIDSubsystem {

    //Variables
    private final int FRONT_ENCODER_CHANNEL_A = 4;
    private final int FRONT_ENCODER_CHANNEL_B = 5;
    private final int COUNTS_PER_REVOLUTION = 500;
    private final int WHEEL_DIAMETER = 4;
    private boolean frontDeployed = false;
    private boolean rearDeployed = false;
    private int frontElevation;
    private int rearElevation;
    boolean override = false;

    //PID
    private final static double UP_P = .2;
    private final static double UP_I = .000;
    private final static double UP_D = .000;
    private final static double DOWN_P = .15;
    private final static double DOWN_I = .000;
    private final static double DOWN_D = .000;

    //Motors
    private static CANSpark1038 rearMotor = new CANSpark1038(57, CANSparkMaxLowLevel.MotorType.kBrushed);
    private CANSpark1038 leadScrewMotor = new CANSpark1038(60, CANSparkMaxLowLevel.MotorType.kBrushless);

    //Pneumatics
    private DoubleSolenoid frontCylinders = new DoubleSolenoid(0, 1);

    //Sensors
    private CANEncoder leadScrewEncoder = leadScrewMotor.getEncoder();
    private Encoder1038 rearMotorEncoder = new Encoder1038(FRONT_ENCODER_CHANNEL_A, FRONT_ENCODER_CHANNEL_B, false, COUNTS_PER_REVOLUTION, WHEEL_DIAMETER);

    //Objects
    private static Endgame endgame;
    private static ArduinoReader arduinoReader = ArduinoReader.getInstance();    
    private PIDController endgamePID = getPIDController();
    private double rearUpCounts = leadScrewEncoder.getPosition();

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
        super(UP_P, UP_I, UP_D);
        endgamePID.setPID(UP_P, UP_I, UP_D);
        endgamePID.setAbsoluteTolerance(0);
        endgamePID.setContinuous(false);
        endgamePID.setOutputRange(-1, 0);
        endgamePID.setSetpoint(-2);
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
    public void deployRear(double power) {
        if(leadScrewEncoder.getPosition() > (rearUpCounts - 146)){ //Change levelHeight to value if needed
            leadScrewMotor.set(power);
        }
        else{
            leadScrewMotor.set(0);
        }
        rearDeployed = true;
    }

    /**
     * Retracts front cylinders by switching the solenoid state and sets
     * frontDeployed boolean to false
     */
    public void retractFront() {
        frontCylinders.set(DoubleSolenoid.Value.kForward);
        frontDeployed = false;
        endgamePID.disable();
    }

    /**
     * Retracts rear cylinders by switching the solenoid state and sets rearDeployed
     * boolean to false
     */
    public void retractRear() {
        endgamePID.disable();
        if(leadScrewEncoder.getPosition() < rearUpCounts - 2){
            leadScrewMotor.set(.5);
        }
        else{
            leadScrewMotor.set(0);
        }
        rearDeployed = false;
    }

    /**
     * Stops rear lead screw from moving
     */
    public void stopRear() {
        leadScrewMotor.set(0);
        endgamePID.disable();
    }

    /**
     * Overrides the rear lead screw and allows the driver to manually retract the lead screw
     */
    public void overrideRear(){
        endgamePID.disable();
        leadScrewMotor.set(1);
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
     * Deploys the front cylinders
     */
    public void deployEndgame() {
        this.deployFront();
        enable();
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
        return leadScrewEncoder.getPosition();
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

    /**
     * @param rearUpCounts the rearUpCounts to set
     */
    public void setOverride() {
        override = true;
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected double returnPIDInput() {
        frontElevation = arduinoReader.getFrontBottomLaserVal();
        rearElevation = arduinoReader.getRearBottomLaserVal();
        if((frontElevation - rearElevation) < -5){
            endgamePID.setPID(DOWN_P, DOWN_I, DOWN_D);
            endgamePID.setOutputRange(-1, 0);
        }
        else if ((frontElevation - rearElevation) >= -5){
            endgamePID.setOutputRange(-1, 0);
            endgamePID.setPID(UP_P, UP_I, UP_D);
            if(frontElevation > 20 && rearElevation > 20){
                endgamePID.setOutputRange(-1, -1);
            }
        }
        return frontElevation - rearElevation;
    }

    @Override
    protected void usePIDOutput(double output) {
        this.deployRear(output);
    }

    public void disable() {
        super.disable();
    }

}