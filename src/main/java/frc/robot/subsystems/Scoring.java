/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.robot.robot.CANSpark1038;
import frc.robot.robot.Encoder1038;

/**
 * Add your docs here.
 */
public class Scoring extends PIDSubsystem {

    private static Scoring scoring;
    private final int SCORING_TOLERANCE = 5; // Placeholder
    public final static double P_UP = .000; // Placeholder
    public final static double I_UP = .000; // Placeholder
    public final static double D_UP = .000; // Placeholder
    public final static double P_DOWN = .000; // Placeholder
    public final static double I_DOWN = .000; // Placeholder
    public final static double D_DOWN = .000; // Placeholder
    public final static double MAX_SCORING_OUTPUT = 0.8; // Placeholder
    public final static double MIN_SCORING_OUTPUT = -0.1; // Placeholder
    public final static int SCORING_LVL3 = 600; // Placeholder
    public final static int SCORING_LVL2 = 400; // Placeholder
    public final static int SCORING_LVL1 = 20; // Placeholder
    public final static int SCORING_FLOOR = 50; // Placeholder
    // private CANSpark1038 fourBarMotor = new CANSpark1038(56, MotorType.kBrushed);
    // private CANEncoder fourBarEncoder = fourBarMotor.getEncoder();
    private PIDController scoringPID = getPIDController();

    public static Scoring getInstance() {
        if (scoring == null) {
            System.out.println("Creating new Scoring");
            scoring = new Scoring();
        }
        return scoring;
    }

    private Scoring() {
        super(P_UP, I_UP, D_UP);
        scoringPID.setAbsoluteTolerance(SCORING_TOLERANCE);
        scoringPID.setOutputRange(MIN_SCORING_OUTPUT, MAX_SCORING_OUTPUT);
        scoringPID.setContinuous(false);
        //fourBarMotor.setInverted(false);
    }

    // public int getMotorRotations() {
    //     return fourBarEncoder.getPosition();
    // }

    // public double getScoringSpeed() {
    //     return fourBarEncoder.getVelocity();
    // }

    public void scoringPeriodic() {
        if (scoringPID.isEnabled()) {
            double PIDVal = scoringPID.get();
            usePIDOutput(PIDVal);
        }
    }

    public boolean isGoingDown(int newSetpoint) {
        if (getSetpoint() > newSetpoint) {
            System.out.println("Going down");
            return true;
        } else {
            System.out.println("Going up");
            return false;
        }
    }

    public void moveToLvl3() {
        enable();
        if(isGoingDown(SCORING_LVL3)){
            scoringPID.setPID(P_DOWN, I_DOWN, D_DOWN);
        }
		else{
			scoringPID.setPID(P_UP, I_UP, D_UP);
        }
		setSetpoint(SCORING_LVL3);
    }

    public void moveToLvl2() {
        enable();
        if(isGoingDown(SCORING_LVL2)){
            scoringPID.setPID(P_DOWN, I_DOWN, D_DOWN);
        }
		else{
			scoringPID.setPID(P_UP, I_UP, D_UP);
        }
		setSetpoint(SCORING_LVL2);
    }

    public void moveToLvl1() {
        enable();
        if(isGoingDown(SCORING_LVL1)){
            scoringPID.setPID(P_DOWN, I_DOWN, D_DOWN);
        }
		else{
			scoringPID.setPID(P_UP, I_UP, D_UP);
        }
		setSetpoint(SCORING_LVL1);
    }

    public void moveToGround() {
        enable();
        if(isGoingDown(SCORING_FLOOR)){
            scoringPID.setPID(P_DOWN, I_DOWN, D_DOWN);
        }
		else{
			scoringPID.setPID(P_UP, I_UP, D_UP);
        }
		setSetpoint(SCORING_FLOOR);
    }

    // public void resetEncoder() {
    //     fourBarEncoder.reset();
    // }

    public void move(double joystickValue) {
        if (getSetpoint() <= SCORING_LVL3 && joystickValue > 0.09) {
            scoringPID.setPID(P_UP, I_UP, D_UP);
            enable();
            setSetpoint(getSetpoint() + 2);
        } else if (getSetpoint() > 0 && joystickValue < -0.09) {
            scoringPID.setPID(P_DOWN, I_DOWN, D_DOWN); // Check if should be up or down
            enable();
            setSetpoint(getSetpoint() - 2);
        }
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected double returnPIDInput() {
        return 0;
        // return fourBarEncoder.getPosition();
    }

    @Override
    protected void usePIDOutput(double output) {
        // fourBarMotor.set(output);
    }

    @Override
    public void disable() {
        super.disable();
        //fourBarMotor.set(0);
    }
}
