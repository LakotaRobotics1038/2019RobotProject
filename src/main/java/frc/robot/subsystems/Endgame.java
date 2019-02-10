package frc.robot.subsystems;

import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.robot.CANSpark1038;
import frc.robot.robot.Encoder1038;

public class Endgame extends Subsystem{

    private final int FRONT_ENCODER_CHANNEL_A = 5; //Placeholder
    private final int FRONT_ENCODER_CHANNEL_B = 6; //Placeholder
    private final int COUNTS_PER_REVOLUTION = 220; //Placeholder
    private final int WHEEL_DIAMETER = 5; //Placeholder
    private DoubleSolenoid frontCylinders = new DoubleSolenoid(0, 1); //Placeholder
    private DoubleSolenoid backCylinders = new DoubleSolenoid(2, 3); //Placeholder
    private CANSpark1038 frontMotor = new CANSpark1038(2); //Placeholder
    private Encoder1038 frontMotorEncoder = new Encoder1038(FRONT_ENCODER_CHANNEL_A, FRONT_ENCODER_CHANNEL_B, false, COUNTS_PER_REVOLUTION , WHEEL_DIAMETER);
    private static Endgame endgame;

    public static Endgame getInstance(){
        if(endgame == null){
            System.out.println("Creating new Endgame");
            endgame = new Endgame();
        }
        return endgame;
    }

    private Endgame(){

    }

    public void lowerFront(){
        frontCylinders.set(DoubleSolenoid.Value.kForward);
    }

    public void lowerBack(){
        backCylinders.set(DoubleSolenoid.Value.kForward);
    }

    public void raiseFront(){
        frontCylinders.set(DoubleSolenoid.Value.kReverse);
    }

    public void raiseBack(){
        backCylinders.set(DoubleSolenoid.Value.kReverse);
    }

    public void setFrontMotor(double power){
        frontMotor.set(power);
    }

    public int getEncoderCounts(){
        return frontMotorEncoder.get();
    }

    public double getEncoderDistance(){
        return frontMotorEncoder.getDistance();
    }

    // public double getFrontElevation(){
    //     return;
    // }

    // public double getBackElevation(){
    //     return;
    // }

    @Override
    protected void initDefaultCommand() {

    }

}