/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.robot.robot.Spark1038;

public class Acquisition extends PIDSubsystem {
    private final static double P = 0.00; // Placeholder
    private final static double I = 0.00; // Placeholder
    private final static double D = 0.00; // Placeholder
    private final int TOLERANCE = 3; // Placeholder
    private final int HATCH_ACQ = 0; // Placeholder
    private final int HATCH_DROP = 0; // Placeholder
    private final int BALL_INTAKE_MOTOR_PORT = 5; // Placeholder
    private final int ACQUISITION_MOTOR_PORT = 6; // Placeholder
    private final double MIN_ACQ_SPEED = 0.6; // Placeholder
    private final double MAX_ACQ_SPEED = 0.9; // Placeholder
    private final double PID_MAX_SPEED = 0.7;
    private boolean hasHatch = false;
    private Spark1038 ballIntakeMotor = new Spark1038(BALL_INTAKE_MOTOR_PORT);
    private Spark1038 acquisitionMotor = new Spark1038(ACQUISITION_MOTOR_PORT);
    private DoubleSolenoid hatchAcq = new DoubleSolenoid(HATCH_ACQ, HATCH_DROP); // Placeholder
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
        acquisitionMotor.setInverted(false); // Placeholder
        acquisitionTilt.setAbsoluteTolerance(TOLERANCE);
        acquisitionTilt.setContinuous(false);
        acquisitionTilt.setOutputRange(-PID_MAX_SPEED, PID_MAX_SPEED);
    }

    public void acqHatch() {
        hatchAcq.set(DoubleSolenoid.Value.kForward); // Placeholder
    }

    public void dropHatch() {
        hatchAcq.set(DoubleSolenoid.Value.kReverse); // Placeholder
    }

    public static int getAcqTilt() {
        return 5; // replace
    }

    public void acquire() {
        acquisitionMotor.set(MAX_ACQ_SPEED);
    }

    public void dispose() {
        acquisitionMotor.set(-MAX_ACQ_SPEED);
    }

    public void stop() {
        acquisitionMotor.set(0);
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

    }

    public void disable() {
        super.disable();
        acquisitionMotor.set(0);
    }
}
