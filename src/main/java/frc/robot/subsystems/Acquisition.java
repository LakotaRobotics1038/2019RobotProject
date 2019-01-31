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

public class Acquisition extends PIDSubsystem{
    private final static double P = 0.00; //Placeholder
    private final static double I = 0.00; //Placeholder
    private final static double D = 0.00; //Placeholder
    private final int TOLERANCE = 3; //Placeholder
    private final int HATCH_ACQ = 0; //Placeholder
    private final int HATCH_DROP = 0; //Placeholder
    private final int BALL_INTAKE_MOTOR_PORT = 5; //Placeholder
    private final int ACQUISITION_MOTOR_PORT = 6; //Placeholder
    private Spark1038 ballIntake = new Spark1038(BALL_INTAKE_MOTOR_PORT);
    private Spark1038 acquisitionMotor = new Spark1038(ACQUISITION_MOTOR_PORT);
    private DoubleSolenoid hatchAcq = new DoubleSolenoid(HATCH_ACQ, HATCH_DROP); //Placeholder
    private PIDController acquisitionTilt = getPIDController();
    private static Acquisition acquisition;

    public static Acquisition getInstance(){
        if(acquisition == null){
            System.out.println("Creating new Acquisition");
            acquisition = new Acquisition();
        }
        return acquisition;
    }

    private Acquisition(){
        super(P, I, D);
        acquisitionTilt.setAbsoluteTolerance(TOLERANCE);
        acquisitionTilt.setContinuous(false);
    }

    public void acqHatch(){
        hatchAcq.set(DoubleSolenoid.Value.kForward);
    }

    public void dropHatch(){
        hatchAcq.set(DoubleSolenoid.Value.kReverse);
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
