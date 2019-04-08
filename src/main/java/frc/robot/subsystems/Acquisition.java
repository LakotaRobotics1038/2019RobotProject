/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.robot.CANSpark1038;

public class Acquisition extends Subsystem {
    
    //Variables
    private final int HATCH_ACQ = 6;
    private final int HATCH_DROP = 7;
    private final double DISPOSE_ACQ_SPEED = -1.0;
    private final double ACQUIRE_ACQ_SPEED = 1.0;

    //Motors
    private CANSpark1038 ballIntakeMotor = new CANSpark1038(59, MotorType.kBrushed);
    private CANSpark1038 vacuumGen = new CANSpark1038(58, MotorType.kBrushed);

    //Pneumatics
    private DoubleSolenoid hatchAcq = new DoubleSolenoid(HATCH_ACQ, HATCH_DROP);

    //Objects
    private static Acquisition acquisition;

    /**
     * Returns the acquisition instance created when the robot starts
     * 
     * @return Acquisition instance
     */
    public static Acquisition getInstance() {
        if (acquisition == null) {
            System.out.println("Creating new Acquisition");
            acquisition = new Acquisition();
        }
        return acquisition;
    }

    /**
     * Instantiates acquisition object
     */
    private Acquisition() {
        ballIntakeMotor.setInverted(false);
        ballIntakeMotor.restoreFactoryDefaults();
        ballIntakeMotor.setIdleMode(IdleMode.kBrake);
        hatchAcq.set(Value.kForward);
    }

    /**
     * Retracts the eject and runs the choo choo
     */
    public void acqHatch() {
        hatchAcq.set(DoubleSolenoid.Value.kForward);
        vacuumGen.set(.5);
    }

    /**
     * Extends the eject and turns the choo choo off
     */
    public void dropHatch() {
        hatchAcq.set(DoubleSolenoid.Value.kReverse);
        vacuumGen.set(0);
    }

    /**
     * Retracts the eject and turns the choo choo off
     */
    public void stopHatch() {
        hatchAcq.set(Value.kForward);
        vacuumGen.set(0);
    }

    /**
     * Sets the ball intake motor to the set acquire speed
     */
    public void acqCargo() {
        ballIntakeMotor.set(ACQUIRE_ACQ_SPEED);
        System.out.println(ballIntakeMotor.getOutputCurrent());
    }

    /**
     * Sets the ball intake motor to the set dispose speed
     */
    public void disposeCargo() {
        ballIntakeMotor.set(DISPOSE_ACQ_SPEED);
        System.out.println(ballIntakeMotor.getOutputCurrent());
    }

    /**
     * Stops the ball intake motor
     */
    public void stop() {
        ballIntakeMotor.set(0);
    }

    @Override
    protected void initDefaultCommand() {

    }

    /**
     * Stops the ball intake motor
     */
    public void disable() {
        ballIntakeMotor.set(0);
    }
}
