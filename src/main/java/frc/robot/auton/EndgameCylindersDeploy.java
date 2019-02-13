package frc.robot.auton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Endgame;

public class EndgameCylindersDeploy extends Command {

    private int frontElevation;
    private int rearElevation;
    private int targetElevation;
    private final int TOLERANCE = 4;
    private Endgame endgame = Endgame.getInstance();
    private Timer timer = new Timer();

    public EndgameCylindersDeploy(int setpoint){
        targetElevation = setpoint;
        requires(endgame);
    }

    @Override
    protected void initialize(){
        timer.start();
        System.out.println("Timer started");
    }

    @Override
    protected void execute() {
        if(frontElevation > rearElevation + TOLERANCE || frontElevation >= targetElevation){
            endgame.stopFront();
        }else if(rearElevation > frontElevation + TOLERANCE || rearElevation >= targetElevation){
            endgame.stopRear();
        }else{
            endgame.deployRear();
            endgame.deployFront();
        }
    }

    @Override
    protected void interrupted() {
        endgame.retractRear();
        endgame.retractFront();
    }

    @Override
    protected void end(){
        endgame.stopRear();
        endgame.stopFront();
        System.out.println("Ended at: " + timer.get());
        timer.stop();
        timer.reset();
    }

    @Override
    protected boolean isFinished() {
        return frontElevation >= targetElevation && rearElevation >= targetElevation;
    }

}