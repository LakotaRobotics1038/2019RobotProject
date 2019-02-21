/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.robot.robot.CANSpark1038;

public class Acquisition extends PIDSubsystem {
    private final static double P = 0.00; // Placeholder
    private final static double I = 0.00; // Placeholder
    private final static double D = 0.00; // Placeholder
    private final int TOLERANCE = 3; // Placeholder
    private final int HATCH_ACQ = 7;
    private final int HATCH_DROP = 6;
    private final double MIN_ACQ_SPEED = 0.2;
    private final double MAX_ACQ_SPEED = 1.0;
    private double acqMotorSpeed;
    private final double PID_MAX_SPEED = 0.7;
    private enum SpeedModes {Aquire, Eject};
    private int upTilt = 0;
    private int downTilt = 90;
    private boolean hasHatch = false;
    private boolean isTiltedDown = false;
    private CANSpark1038 ballIntakeMotor = new CANSpark1038(59, MotorType.kBrushed);
    private CANSpark1038 groundAcqMotor = new CANSpark1038(60, MotorType.kBrushed);
    private CANSpark1038 vacuumGen = new CANSpark1038(58, MotorType.kBrushed); 
    //private DoubleSolenoid hatchAcq = new DoubleSolenoid(HATCH_ACQ, HATCH_DROP);
    private PIDController acquisitionTilt = getPIDController();
    private static Acquisition acquisition;

    public static Acquisition getInstance() {
        if (acquisition == null) {
            System.out.println("Creating new Acquisition");
            acquisition = new Acquisition();
        }
        return acquisition;
    }

    private Acquisition() {
        super(P, I, D);
        ballIntakeMotor.setInverted(false); // Placeholder
        groundAcqMotor.setInverted(false); // Placeholder
        acquisitionTilt.setAbsoluteTolerance(TOLERANCE);
        acquisitionTilt.setContinuous(false);
        acquisitionTilt.setOutputRange(-PID_MAX_SPEED, PID_MAX_SPEED);
    }

    // public void acqHatch() {
    //     hatchAcq.set(DoubleSolenoid.Value.kForward); // Placeholder
    // }

    // public void dropHatch() {
    //     hatchAcq.set(DoubleSolenoid.Value.kReverse); // Placeholder
    // }

    public static int getAcqTilt() {
        return 5; // replace
    }

    public void acquire() {
        ballIntakeMotor.set(MAX_ACQ_SPEED);
    }

    public void dispose() {
        ballIntakeMotor.set(-acqMotorSpeed);
    }

    public void stop() {
        ballIntakeMotor.set(0);
    }

    public void tiltDown(){
        isTiltedDown = true;
    }

    public void tiltUp(){
        isTiltedDown = false;
    }

    public void setAcqSpeed(SpeedModes mode) {
        if (acqMotorSpeed < MAX_ACQ_SPEED && mode == SpeedModes.Aquire)
            acqMotorSpeed += .2;
        else if (acqMotorSpeed > MIN_ACQ_SPEED && mode == SpeedModes.Eject)
            acqMotorSpeed -= .2;
}

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected double returnPIDInput() {
        return acquisitionTilt.get();
    }

    @Override
    protected void usePIDOutput(double output) {
        groundAcqMotor.set(output);
    }

    public void disable() {
        super.disable();
        ballIntakeMotor.set(0);
        groundAcqMotor.set(0);
    }
}
