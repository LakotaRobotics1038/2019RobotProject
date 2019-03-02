package frc.robot.auton;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Endgame;

public class TurnEndgameMotor extends Command {

    private double motorPower;
    private Endgame endgame = Endgame.getInstance();
    private int currentElevation;
    private int goalElevation = 3; // Placeholder

    public TurnEndgameMotor(double motorSpeed) {
        motorPower = motorSpeed;
    }

    @Override
    protected void execute() {
        if (!endgame.getIsFrontDeployed()) {
            currentElevation = endgame.getRearElevation();
        } else {
            currentElevation = endgame.getFrontElevation();
        }
        //endgame.setRearMotor(motorPower);
    }

    @Override
    protected void end() {
        //endgame.setRearMotor(0);
    }

    @Override
    protected boolean isFinished() {
        return currentElevation < goalElevation;
    }

}