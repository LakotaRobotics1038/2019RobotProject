/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.robot.robot.ArduinoReader;
import frc.robot.robot.CANSpark1038;

public class Scoring extends PIDSubsystem {

    //Variables
    private static Scoring scoring;
    private final int SCORING_TOLERANCE = 2;
    public static double MAX_SCORING_OUTPUT = 0.6;
    public final static double MIN_SCORING_OUTPUT = -.5;
    public boolean goingDown;
    public boolean isEnabled = false;

    //PID
    public final static double P_UP1 = .001;
    public final static double I_UP1 = .000;
    public final static double D_UP1 = .0001;
    public final static double P_UPBALL = .0005;
    public final static double I_UPBALL = .0001;
    public final static double D_UPBALL = .000;
    public final static double P_UP2 = .008;
    public final static double I_UP2 = .0001;
    public final static double D_UP2 = .000;
    public final static double P_UP3 = .008;
    public final static double I_UP3 = .000;
    public final static double D_UP3 = .000;
    public final static double P_DOWN0 = .0001;
    public final static double I_DOWN0 = .000;
    public final static double D_DOWN0 = .000;
    public final static double P_DOWN1 = .012;
    public final static double I_DOWN1 = .0005;
    public final static double D_DOWN1 = .000;
    public final static double P_DOWNBALL = .0001;
    public final static double I_DOWNBALL = .0001;
    public final static double D_DOWNBALL = .0001;
    public final static double P_DOWN2 = .004;
    public final static double I_DOWN2 = .000;
    public final static double D_DOWN2 = .000;

    //Motors
    private CANSpark1038 fourBarMotor = new CANSpark1038(56, MotorType.kBrushless);

    //Pneumatics
    private DoubleSolenoid armBrake = new DoubleSolenoid(2, 3);

    //Sensors
    private AnalogInput armPot = new AnalogInput(0);

    //Objects
    private PIDController scoringPID = getPIDController();
    private ArduinoReader arduinoReader = ArduinoReader.getInstance();

    /**
     * Returns the scoring instance created when the robot starts
     * 
     * @return Scoring instance
     */
    public static Scoring getInstance() {
        if (scoring == null) {
            System.out.println("Creating new Scoring");
            scoring = new Scoring();
        }
        return scoring;
    }

    /**
     * Instantiates scoring object
     */
    private Scoring() {
        super(P_UP2, I_UP2, D_UP2);
        System.out.println("creating PID");
        scoringPID.setAbsoluteTolerance(SCORING_TOLERANCE);
        scoringPID.setOutputRange(MIN_SCORING_OUTPUT, MAX_SCORING_OUTPUT);
        scoringPID.setInputRange(-60, 70);
        scoringPID.setContinuous(false);
        scoringPID.setSetpoint(-50);
        fourBarMotor.setInverted(false);
        armBrake.set(Value.kForward);
    }

    /**
     * Sets level scoring arms should go to
     * 
     * @param angle The angle in degrees the scoring arm should be at relative the
     *              horizontal plane
     */
    public void setLevel(int angle) {
        MAX_SCORING_OUTPUT = 0.3;
        setSetpoint(angle);
        if (isGoingDown(angle) && angle == 7) {
            scoringPID.setPID(P_DOWN2, I_DOWN2, D_DOWN2);
        } else if (isGoingDown(angle) && angle == -41) {
            scoringPID.setPID(P_DOWN1, I_DOWN1, D_DOWN1);
        } else if (isGoingDown(angle) && angle == -55) {
            scoringPID.setPID(P_DOWN0, I_DOWN0, D_DOWN0);
        }else if (isGoingDown(angle) && angle == -18){
            scoringPID.setPID(P_DOWNBALL, I_DOWNBALL, D_DOWNBALL);
        } else if (!isGoingDown(angle) && angle == -41) {
            scoringPID.setPID(P_UP1, I_UP1, D_UP1);
        } else if (!isGoingDown(angle) && angle == 7) {
            scoringPID.setPID(P_UP2, I_UP2, D_UP2);
        } else if (!isGoingDown(angle) && angle == 50) {
            scoringPID.setPID(P_UP3, I_UP3, D_UP3);
        } else if (!isGoingDown(angle) && angle == -18) {
            scoringPID.setPID(P_UPBALL, I_UPBALL, D_UPBALL);
        }
        System.out.println("enabling PID");
        enable();
        isEnabled = true;
        armBrake.set(Value.kForward);
    }

    /**
     * Whether the scoring arm is going down
     * 
     * @param newSetpoint The new setpoint for the scoring arm
     * @return Returns whether the scoring arm is going down
     */
    public boolean isGoingDown(int newSetpoint) {
        if (arduinoReader.getScoringAccelerometerVal() > newSetpoint) {
            goingDown = true;
            return true;
        } else {
            goingDown = false;
            return false;
        }
    }

    /**
     * Should be used to manually controll the scoring arm
     * 
     * @param joystickValue The joystick value between -1 and 1
     */
    public void move(double joystickValue) {
        disable();
        isEnabled = false;
        fourBarMotor.set(joystickValue * 0.5);
        armBrake.set(Value.kForward);
    }

    /**
     * Enables or disables the brake depending on what state it is in
     */
    public void deployBrake() {
        if(armBrake.get() == Value.kForward) {
            armBrake.set(Value.kReverse);
        }
        else{
            armBrake.set(Value.kForward);
        }
    }

    /**
     * Returns the angle the four bar is at based on the volts recieved from the prox
     * @return The angle of the four bar in degrees (relative to horizontal)
     */
    public double returnArmPot() {
        double volts = armPot.getAverageVoltage();
        System.out.println(volts);
        double angle = ((volts/-2) * 53.11443747) + 70.77305649;
        return angle;
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected double returnPIDInput() {
        double volts = armPot.getAverageVoltage();
        double angle = ((volts/-2) * 53.11443747) + 70.77305649;
        return angle;
    }

    @Override
    protected void usePIDOutput(double output) {
        if (output < 0 && getSetpoint() == 2 && !goingDown) {
            output = output * .05;
        }
        System.out.println(output);
        fourBarMotor.set(output);
        int angle = Math.toIntExact(Math.round(this.returnPIDInput()));
        if(angle == getSetpoint()) {
            armBrake.set(Value.kReverse);
            System.out.println("angle=setpoint");
        }
    }

    /**
     * Returns if the scoring PID system in on target
     * @return True if on target, false otherwise
     */
    public boolean atTarget(){
        return scoringPID.onTarget();
    }

    @Override
    public void disable() {
        if(scoringPID.isEnabled()){
            System.out.println("killing scoring");
            super.disable();
        }
    }
}
