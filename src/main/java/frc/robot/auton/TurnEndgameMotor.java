package frc.robot.auton;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Endgame;

public class TurnEndgameMotor extends Command {

    private double motorPower;
    private Endgame endgame = Endgame.getInstance();
    private int frontElevation; //read front laser
    private int goalElevation = 5; //Placeholder

    public TurnEndgameMotor(double motorSpeed){
        motorPower = motorSpeed;
    }

    @Override
    protected void execute() {
        endgame.setFrontMotor(motorPower);
    }

    @Override
    protected boolean isFinished() {
        return frontElevation < goalElevation;
    }

}