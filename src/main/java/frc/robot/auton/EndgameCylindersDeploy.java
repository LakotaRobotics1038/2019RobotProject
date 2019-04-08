package frc.robot.auton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.robot.ArduinoReader;
import frc.robot.subsystems.Endgame;

public class EndgameCylindersDeploy extends Command {

    //Variables
    private double frontElevation;
    private double rearElevation;
    private double targetElevation;

    //Objects
    private Endgame endgame = Endgame.getInstance();
    private ArduinoReader arduinoReader = ArduinoReader.getInstance();

    /**
     * Instantiates endgame cylinder deploy command
     * 
     * @param setpoint The target elevation for endgame in cm
     */
    public EndgameCylindersDeploy(int setpoint) {
        targetElevation = setpoint;
        requires(endgame);
    }

    @Override
    protected void initialize() {
        endgame.deployFront();
        endgame.deployRear(-1);
    }

    @Override
    protected void execute() {
        frontElevation = arduinoReader.getFrontBottomLaserVal();
        rearElevation = arduinoReader.getRearBottomLaserVal();
        if(rearElevation - frontElevation >= 4) {
            endgame.stopRear();
        }
        else {
            endgame.deployRear(-1);
        }
    }

    @Override
    protected void interrupted() {
        end();
        System.out.println("Cylinder Deploy interrupted");
    }

    @Override
    protected void end() {
        endgame.retractRear();
        endgame.retractFront();
    }

    @Override
    public boolean isFinished() {
        return frontElevation >= targetElevation && rearElevation >= targetElevation;
    }

}