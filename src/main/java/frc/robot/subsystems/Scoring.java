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
import frc.robot.robot.ArduinoReader;
import frc.robot.robot.CANSpark1038;
import frc.robot.robot.Encoder1038;

/**
 * Add your docs here.
 */
public class Scoring extends PIDSubsystem {

    private static Scoring scoring;
    private final int SCORING_TOLERANCE = 5; // Placeholder
    public final static double P_UP1 = .001; // Placeholder
    public final static double I_UP1 = .000; // Placeholder
    public final static double D_UP1 = .000; // Placeholder
    public final static double P_UP2 = .01; // Placeholder
    public final static double I_UP2 = .0001; // Placeholder
    public final static double D_UP2 = .000; // Placeholder
    public final static double P_UP3 = .005; // Placeholder
    public final static double I_UP3 = .000; // Placeholder
    public final static double D_UP3 = .000; // Placeholder
    public final static double P_DOWN0 = .0001; // Placeholder
    public final static double I_DOWN0 = .000; // Placeholder
    public final static double D_DOWN0 = .000; // Placeholder
    public final static double P_DOWN1 = .001; // Placeholder
    public final static double I_DOWN1 = .0002; // Placeholder
    public final static double D_DOWN1 = .000; // Placeholder
    public final static double P_DOWN2 = .005; // Placeholder
    public final static double I_DOWN2 = .0002; // Placeholder
    public final static double D_DOWN2 = .000; // Placeholder
    public final static double MAX_SCORING_OUTPUT = .3; // Placeholder
    public final static double MIN_SCORING_OUTPUT = -.2; // Placeholder
    public boolean goingDown;
    private CANSpark1038 fourBarMotor = new CANSpark1038(56, MotorType.kBrushless);
    // private CANEncoder fourBarEncoder = fourBarMotor.getEncoder();
    private ArduinoReader arduinoReader = ArduinoReader.getInstance();
    private PIDController scoringPID = getPIDController();

    public static Scoring getInstance() {
        if (scoring == null) {
            System.out.println("Creating new Scoring");
            scoring = new Scoring();
        }
        return scoring;
    }

    private Scoring() {
        super(P_UP2, I_UP2, D_UP2);
        scoringPID.setAbsoluteTolerance(SCORING_TOLERANCE);
        scoringPID.setOutputRange(MIN_SCORING_OUTPUT, MAX_SCORING_OUTPUT);
        scoringPID.setInputRange(-60, 70);
        scoringPID.setContinuous(false);
        scoringPID.setSetpoint(-50);
        fourBarMotor.setInverted(false);
    }

    // public int getMotorRotations() {
    //     return fourBarEncoder.getPosition();
    // }

    // public double getScoringSpeed() {
    //     return fourBarEncoder.getVelocity();
    // }

    // public void scoringPeriodic() {
    //     if (scoringPID.isEnabled()) {
    //         double PIDVal = scoringPID.get();
    //         System.out.println("setpoint: " + getSetpoint() + ", angle: " + arduinoReader.getScoringAccelerometerVal() + ", motor power: " + PIDVal);
    //         usePIDOutput(PIDVal);
    //     }
    // }

    public void setLevel(int angle) {
        setSetpoint(angle);
        if(isGoingDown(angle) && angle == 2) {
            scoringPID.setPID(P_DOWN2, I_DOWN2, D_DOWN2);
        }
        else if (isGoingDown(angle) && angle == -45) {
            scoringPID.setPID(P_DOWN1, I_DOWN1, D_DOWN1);
        }
        else if (isGoingDown(angle) && angle == -50) {
            scoringPID.setPID(P_DOWN0, I_DOWN0, D_DOWN0);
        }
        else if(!isGoingDown(angle) && angle == -50) {
            scoringPID.setPID(P_UP1, I_UP1, D_UP2);
        }
        else if(!isGoingDown(angle) && angle == 2) {
            scoringPID.setPID(P_UP2, I_UP2, D_UP2);
        }
        else if(!isGoingDown(angle) && angle == 50) {
            scoringPID.setPID(P_UP3, I_UP3, D_UP3);
        }
        enable();
    }

    public boolean isGoingDown(int newSetpoint) {
        if (arduinoReader.getScoringAccelerometerVal() > newSetpoint) {
            System.out.println("Going down");
            goingDown = true;
            return true;
        } else {
            System.out.println("Going up");
            goingDown = false;
            return false;
        }
    }

    // public void manualOverride(double speed) {
    //     scoringPID.disable();
    //     fourBarMotor.set(speed);
    //     System.out.println("oh man! manual override");
    // }

    // public void moveToLvl3() {
    //     enable();
    //     if(isGoingDown(SCORING_LVL3)){
    //         scoringPID.setPID(P_DOWN, I_DOWN, D_DOWN);
    //     }
	// 	else{
	// 		scoringPID.setPID(P_UP, I_UP, D_UP);
    //     }
	// 	setSetpoint(SCORING_LVL3);
    // }

    // public void moveToLvl2() {
    //     enable();
    //     if(isGoingDown(SCORING_LVL2)){
    //         scoringPID.setPID(P_DOWN, I_DOWN, D_DOWN);
    //     }
	// 	else{
	// 		scoringPID.setPID(P_UP, I_UP, D_UP);
    //     }
	// 	setSetpoint(SCORING_LVL2);
    // }

    // public void moveToLvl1() {
    //     enable();
    //     if(isGoingDown(SCORING_LVL1)){
    //         scoringPID.setPID(P_DOWN, I_DOWN, D_DOWN);
    //     }
	// 	else{
	// 		scoringPID.setPID(P_UP, I_UP, D_UP);
    //     }
	// 	setSetpoint(SCORING_LVL1);
    // }

    // public void moveToGround() {
    //     enable();
    //     if(isGoingDown(SCORING_FLOOR)){
    //         scoringPID.setPID(P_DOWN, I_DOWN, D_DOWN);
    //     }
	// 	else{
	// 		scoringPID.setPID(P_UP, I_UP, D_UP);
    //     }
	// 	setSetpoint(SCORING_FLOOR);
    // }

    // public void resetEncoder() {
    //     fourBarEncoder.reset();
    // }

    public void move(double joystickValue) {
        if (getSetpoint() <= 5 && joystickValue > 0.09) {
            scoringPID.setPID(P_UP2, I_UP2, D_UP2);
            enable();
            setSetpoint(getSetpoint() + 2);
        }else if (getSetpoint() <= 50 && joystickValue > 0.09) {
            scoringPID.setPID(P_UP3, I_UP3, D_UP3);
            enable();
            setSetpoint(getSetpoint() + 2);
        } else if (getSetpoint() > 5 && joystickValue < -0.09) {
            scoringPID.setPID(P_DOWN2, I_DOWN2, D_DOWN2); // Check if should be up or down
            enable();
            setSetpoint(getSetpoint() - 2);
        } else if (getSetpoint() > -40 && joystickValue < -0.09) {
            scoringPID.setPID(P_DOWN1, I_DOWN1, D_DOWN1); // Check if should be up or down
            enable();
            setSetpoint(getSetpoint() - 2);
        }
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected double returnPIDInput() {
        return arduinoReader.getScoringAccelerometerVal();
    }

    @Override
    protected void usePIDOutput(double output) {
        // System.out.println("angle: " + arduinoReader.getScoringAccelerometerVal()+", setpoint: " + getSetpoint() + ", power: " + output + ", P: " + scoringPID.getP());
        if(output < 0 && getSetpoint() == 2 && !goingDown) {
            output = output * .05;
        }
        fourBarMotor.set(output);
    }

    @Override
    public void disable() {
        super.disable();
    }
}
