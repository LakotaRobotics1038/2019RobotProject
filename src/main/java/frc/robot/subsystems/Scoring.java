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

    private static Scoring scoring;
    private final int SCORING_TOLERANCE = 2;
    public final static double P_UP1 = .004;
    public final static double I_UP1 = .000;
    public final static double D_UP1 = .0001;
    public final static double P_UPBALL = .007; // was .007
    public final static double I_UPBALL = .00012; // was .0001
    public final static double D_UPBALL = .000;
    public final static double P_UP2 = .008;
    public final static double I_UP2 = .0001;
    public final static double D_UP2 = .000;
    public final static double P_UP3 = .012;
    public final static double I_UP3 = .000;
    public final static double D_UP3 = .001;
    public final static double P_DOWN0 = .0001;
    public final static double I_DOWN0 = .000;
    public final static double D_DOWN0 = .000;
    public final static double P_DOWN1 = .012;
    public final static double I_DOWN1 = .0005;
    public final static double D_DOWN1 = .000;
    public final static double P_DOWNBALL = .002; // was .002
    public final static double I_DOWNBALL = .00012; // was .0001
    public final static double D_DOWNBALL = .000;
    public final static double P_DOWN2 = .008;
    public final static double I_DOWN2 = .000;
    public final static double D_DOWN2 = .000;
    public static double MAX_SCORING_OUTPUT = 0.6;
    public final static double MIN_SCORING_OUTPUT = -.5;
    private boolean isInManual = false;
    public boolean goingDown;
    private CANSpark1038 fourBarMotor = new CANSpark1038(56, MotorType.kBrushless);
    private ArduinoReader arduinoReader = ArduinoReader.getInstance();
    private AnalogInput armPot = new AnalogInput(0);
    private PIDController scoringPID = getPIDController();
    private DoubleSolenoid armBrake = new DoubleSolenoid(2, 3);
    double count = 1;
    double potAngle;

    /**
     * Returns the scoring instance created when the robot starts
     * 
     * @return Scoring instance
     */
    public static Scoring getInstance() {
        if (scoring == null) {
            // System.out.println("Creating new Scoring");
            scoring = new Scoring();
        }
        return scoring;
    }

    /**
     * Instantiates scoring object
     */
    private Scoring() {
        super(P_UP2, I_UP2, D_UP2);
        scoringPID.setAbsoluteTolerance(SCORING_TOLERANCE);
        scoringPID.setOutputRange(MIN_SCORING_OUTPUT, MAX_SCORING_OUTPUT);
        scoringPID.setInputRange(-60, 70);
        scoringPID.setContinuous(false);
        scoringPID.setSetpoint(-50);
        fourBarMotor.setInverted(false);
        armBrake.set(Value.kForward);
        double volts = armPot.getAverageVoltage();
        potAngle = (volts * -22.1310156);
    }

    /**
     * Sets level scoring arms should go to
     * 
     * @param angle The angle in degrees the scoring arm should be at relative the
     *              horizontal plane
     */
    public void setLevel(int angle) {
        // isInManual = false;
        MAX_SCORING_OUTPUT = 0.3;
        setSetpoint(angle);
        if (isGoingDown(angle) && angle == 17) {
            scoringPID.setPID(P_DOWN2, I_DOWN2, D_DOWN2);
        } else if (isGoingDown(angle) && angle == -28) { //used to be -37
            scoringPID.setPID(P_DOWN1, I_DOWN1, D_DOWN1);
        } else if (isGoingDown(angle) && angle == -48) { //used to be -48
            scoringPID.setPID(P_DOWN0, I_DOWN0, D_DOWN0);
        }else if (isGoingDown(angle) && angle == -2){
            scoringPID.setPID(P_DOWNBALL, I_DOWNBALL, D_DOWNBALL);
        } else if (!isGoingDown(angle) && angle == -28) { //used to be -37
            scoringPID.setPID(P_UP1, I_UP1, D_UP1);
        } else if (!isGoingDown(angle) && angle == 17) {
            scoringPID.setPID(P_UP2, I_UP2, D_UP2);
        } else if (!isGoingDown(angle) && angle == 45) { //used to be 55
            scoringPID.setPID(P_UP3, I_UP3, D_UP3);
        } else if (!isGoingDown(angle) && angle == -2) {
            scoringPID.setPID(P_UPBALL, I_UPBALL, D_UPBALL);
        }
        enable();
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

    // public void turnOffMotor() {
    //     if(armBrake.get() == Value.kReverse) {
    //         count = count++;
    //     }
    //     if(count >= 6 && scoringPID.isEnabled()) {
    //         disable();
    //         count = 0;
    //     }
    // }

    /**
     * Should be used to manually controll the scoring arm
     * 
     * @param joystickValue The joystick value between -1 and 1
     */
    public void move(double joystickValue) {
        // isInManual = true;
        // MAX_SCORING_OUTPUT = 0.75;
        // if (getSetpoint() <= 5 && joystickValue > 0.09) {
        //     scoringPID.setPID(P_UP2, I_UP2, D_UP2);
        //     enable();
        //     setSetpoint(getSetpoint() + 2);
        // } else if (getSetpoint() <= 50 && joystickValue > 0.09) {
        //     scoringPID.setPID(P_UP3, I_UP3, D_UP3);
        //     enable();
        //     setSetpoint(getSetpoint() + 2);
        // } else if (getSetpoint() > 5 && joystickValue < -0.09) {
        //     scoringPID.setPID(P_DOWN2, I_DOWN2, D_DOWN2); // Check if should be up or down
        //     enable();
        //     setSetpoint(getSetpoint() - 2);
        // } else if (getSetpoint() > -40 && joystickValue < -0.09) {
        //     scoringPID.setPID(P_DOWN1, I_DOWN1, D_DOWN1); // Check if should be up or down
        //     enable();
        //     setSetpoint(getSetpoint() - 2);
        // }
        disable();
        fourBarMotor.set(joystickValue * 0.5);
        armBrake.set(Value.kForward);
    }

    public void deployBrake() {
        if(armBrake.get() == Value.kForward) {
            armBrake.set(Value.kReverse);
        }
        else{
            armBrake.set(Value.kForward);
        }
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected double returnPIDInput() {
        double volts = armPot.getAverageVoltage();
        double angle = (volts * -23.41) + 62.2706; //used to by *53.11443747 - 64.77305649
        // System.out.println(angle);
        return angle;
        // System.out.println(arduinoReader.getScoringAccelerometerVal());
        // return arduinoReader.getScoringAccelerometerVal();
    }

    public double returnArmPot() {
        double volts = armPot.getAverageVoltage();
        double angle = (volts * -23.41) + 62.2706; //used to by *53.11443747 - 64.77305649
        // System.out.println(angle);
        return angle;
        // return armPot.getAverageVoltage();
    }

    @Override
    protected void usePIDOutput(double output) {
        if (output < 0 && getSetpoint() == 2 && !goingDown) {
            output = output * .05;
        }
        if(armBrake.get() == Value.kReverse) {
            count = count * .9;
            // System.out.println(count);
            output = output * count;
            // fourBarMotor.set(output);
        }
        fourBarMotor.set(output);
        int angle = Math.toIntExact(Math.round(this.returnPIDInput()));
        if(angle == getSetpoint()) {
            armBrake.set(Value.kReverse);
            // disable();
            // System.out.println("set brake");
        }
        if(count < .1) {
            count = 1;
            // System.out.println("disabling PID");
            disable();
        }
        //fourBarMotor.set(1.0/(1.0+Math.pow((Math.pow(3, -(8.0 *output))),Math.E))-0.5);
        // double volts = armPot.getAverageVoltage();
        // double angle = (volts * 53.11443747) - 64.77305649;
        // angle = Math.toIntExact(Math.round(angle));
        // System.out.println(getSetpoint());
        // int angle = Math.toIntExact(Math.round(this.returnPIDInput()));
        // if(angle == getSetpoint()) {
        //     armBrake.set(Value.kReverse);
        //     // disable();
        //     // System.out.println("set brake");
        // }
        // if(count >= 60) {
        //     count = 0;
        //     disable();
        // }
        //System.out.println("pot:" + armPot.getAverageVoltage());
        //System.out.println("angle: " + arduinoReader.getScoringAccelerometerVal());
    }

    public boolean atTarget(){
        return scoringPID.onTarget();
    }

    @Override
    public void disable() {
        super.disable();
        fourBarMotor.set(0);
    }
}
