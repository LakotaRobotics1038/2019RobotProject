package frc.robot.subsystems;

import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.auton.ArduinoReader;
import frc.robot.robot.CANSpark1038;
import frc.robot.robot.Encoder1038;

public class Endgame extends Subsystem {

    private final int FRONT_ENCODER_CHANNEL_A = 0; // Placeholder
    private final int FRONT_ENCODER_CHANNEL_B = 1; // Placeholder
    private final int COUNTS_PER_REVOLUTION = 220; // Placeholder
    private final int WHEEL_DIAMETER = 5; // Placeholder
    private boolean frontDeployed = false;
    private boolean rearDeployed = false;
    private DoubleSolenoid frontCylinders = new DoubleSolenoid(0, 1); // Placeholder
    private DoubleSolenoid rearCylinders = new DoubleSolenoid(3, 2); // Placeholder
    //private CANSpark1038 rearMotor = new CANSpark1038(2); // Placeholder
    //private Encoder1038 rearMotorEncoder = new Encoder1038(FRONT_ENCODER_CHANNEL_A, FRONT_ENCODER_CHANNEL_B, false, COUNTS_PER_REVOLUTION, WHEEL_DIAMETER);
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

    }

    public void deployFront() {
        frontCylinders.set(DoubleSolenoid.Value.kReverse);
        System.out.println("Deployed front");
        frontDeployed = true;
    }

    public void deployRear() {
        rearCylinders.set(DoubleSolenoid.Value.kReverse);
        System.out.println("Deployed rear");
        rearDeployed = true;
    }

    public void retractFront() {
        frontCylinders.set(DoubleSolenoid.Value.kForward);
        System.out.println("Retracted front");
        frontDeployed = false;
    }

    public void retractRear() {
        rearCylinders.set(DoubleSolenoid.Value.kForward);
        System.out.println("Retracted front");
        rearDeployed = false;
    }

    // public void setRearMotor(double power) {
    //     rearMotor.set(power);
    // }

    public boolean getIsFrontDeployed() {
        return frontDeployed;
    }

    public boolean getIsRearDeployed() {
        return rearDeployed;
    }
/*
    public int getEncoderCounts() {
        return rearMotorEncoder.get();
    }

    public double getEncoderDistance() {
        return rearMotorEncoder.getDistance();
    }
*/
    public int getFrontElevation() {
        return arduinoReader.returnArduinoFrontLaserValue();
    }

    public int getRearElevation() {
        return arduinoReader.returnArduinoRearLaserValue();
    }

    @Override
    protected void initDefaultCommand() {

    }

}