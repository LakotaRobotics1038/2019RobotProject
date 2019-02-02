/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.robot.robot.Encoder1038;
import frc.robot.robot.Prox;
import frc.robot.robot.Spark1038;

/**
 * Add your docs here.
 */
public class Scoring extends PIDSubsystem {

    private static Scoring scoring;
    private final int SCORING_TOLERANCE = 5; // Placeholder
    private final int SLED_TOLERANCE = 5; // Placeholder
    public final static double P_UP = .000; // Placeholder
    public final static double I_UP = .000; // Placeholder
    public final static double D_UP = .000; // Placeholder
    public final static double P_DOWN = .000; // Placeholder
    public final static double I_DOWN = .000; // Placeholder
    public final static double D_DOWN = .000; // Placeholder
    public final static double P_SLED = .000; // Placeholder
    public final static double I_SLED = .000; // Placeholder
    public final static double D_SLED = .000; // Placeholder
    public final static double MAX_SCORING_OUTPUT = 0.8; // Placeholder
    public final static double MIN_SCORING_OUTPUT = -0.1; // Placeholder
    public final static double MAX_SLED_OUTPUT = 0.8; // Placeholder
    public final static double MIN_SLED_OUTPUT = -0.8; // Placeholder
    private static final double SLED_MAX_DIST = 30; // Placeholder
    private static final double SLED_MIN_DIST = 2; // Placeholder
    public final static int SCORING_LVL3 = 600; // Placeholder
    public final static int SCORING_LVL2 = 400; // Placeholder
    public final static int SCORING_LVL1 = 20; // Placeholder
    public final static int SCORING_FLOOR = 0; // Placeholder
    public final static int SLED_LVL1_AND_3 = 20; // Placeholder
    public final static int SLED_LVL2 = 3; // Placeholder
    public final static int SLED_FLOOR = 30; // Placeholder
    private final int PROX_PORT = 0; // Placeholder
    private final int fourBarChannelA = 0; // Placeholder
    private final int fourBarChannelB = 1; // Placeholder
    private final int fourBarCountsPerRev = 220; // Placeholder
    private Spark1038 fourBarMotor = new Spark1038(0); // Placeholder
    private Encoder1038 fourBarEncoder = new Encoder1038(fourBarChannelA, fourBarChannelB, false, fourBarCountsPerRev, 6);
    private Spark1038 sledMotor = new Spark1038(1); // Placeholder
    private Prox bottomProx = new Prox(PROX_PORT); // Placeholder
    private PIDController scoringPID = getPIDController();
    private PIDController sledPID = getPIDController();

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
        sledPID.setAbsoluteTolerance(SLED_TOLERANCE);
        sledPID.setOutputRange(MIN_SLED_OUTPUT, MAX_SLED_OUTPUT);
        sledPID.setContinuous(false);
        fourBarMotor.setInverted(false); // Placeholder
        sledMotor.setInverted(false); // Placeholder
    }

    public boolean getBottomProx(){
        return bottomProx.get();
    }

    public int getEncoderCount(){
        return fourBarEncoder.get();
    }

    public void scoringPeriodic(){
        if(scoringPID.isEnabled()){
            double PIDVal = scoringPID.get();
            usePIDOutput(PIDVal);
        }
    }

    public void sledPeriodic(){
        if(sledPID.isEnabled()){
            double PIDVal = sledPID.get();
            usePIDOutput(PIDVal);
        }
    }

    public void moveToLvl3(){

    }

    public void moveToLvl2(){
        
    }

    public void moveToLvl1(){
        
    }

    public void moveToFloor(){
        
    }

    public void resetEncoder(){
        fourBarEncoder.reset();
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected double returnPIDInput() {
        return 0;
    }

    @Override
    protected void usePIDOutput(double output) {

    }
}
