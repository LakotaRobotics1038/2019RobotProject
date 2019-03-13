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
    private Encoder1038 rearMotorEncoder = new Encoder1038(FRONT_ENCODER_CHANNEL_A, FRONT_ENCODER_CHANNEL_B, false, COUNTS_PER_REVOLUTION, WHEEL_DIAMETER);
    
    private static Endgame endgame;
    private static ArduinoReader arduinoReader = ArduinoReader.getInstance();

    public static Endgame getInstance() {
        if (endgame == null) {
            System.out.println("Creating new Endgame");
            endgame = new Endgame();
        }
        return endgame;
    }

    private Endgame() {
        retractFront();
        retractRear();
        rearMotor.restoreFactoryDefaults();
        rearMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }

    public void deployFront() {
        frontCylinders.set(DoubleSolenoid.Value.kReverse);
        frontDeployed = true;
    }

    public void deployRear() {
        // rearCylinders.set(DoubleSolenoid.Value.kReverse);
        rearDeployed = true;
    }

    public void retractFront() {
        frontCylinders.set(DoubleSolenoid.Value.kForward);
        frontDeployed = false;
    }

    public void retractRear() {
        // rearCylinders.set(DoubleSolenoid.Value.kForward);
        rearDeployed = false;
    }

    public void setRearMotor(double power) {
        rearMotor.set(-power);
    }

    public boolean getIsFrontDeployed() {
        return frontDeployed;
    }

    public boolean getIsRearDeployed() {
        return rearDeployed;
    }

    public int getEncoderCounts() {
        return rearMotorEncoder.get();
    }

    public double getEncoderDistance() {
        return rearMotorEncoder.getDistance();
    }

    public double getEncoderVelocity(){
        return rearMotorEncoder.getRate();
    }

    public int getFrontElevation() {
        return arduinoReader.getFrontLaserVal();
    }

    public int getRearElevation() {
        return arduinoReader.getRearLaserVal();
    }

    @Override
    protected void initDefaultCommand() {

    }

}