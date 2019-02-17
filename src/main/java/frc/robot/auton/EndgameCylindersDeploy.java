package frc.robot.auton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Endgame;


public class EndgameCylindersDeploy extends Command {

    private int frontElevation;
    private int rearElevation;
    private int targetElevation;
    private final int TOLERANCE = 6;
    private Endgame endgame = Endgame.getInstance();
    private ArduinoReader arduinoReader = ArduinoReader.getInstance();
    private Timer timerFront = new Timer();
    private Timer timerRear = new Timer();
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
        frontElevation = arduinoReader.returnArduinoFrontLaserValue();
        rearElevation = arduinoReader.returnArduinoRearLaserValue();
        System.out.println("Front elevation: " + frontElevation + ", Rear elevation: " + rearElevation);
        
        if(frontElevation > rearElevation + TOLERANCE|| frontElevation >= targetElevation && timerFront.get() < 20){
            endgame.retractFront();
            timerFront.start();
            endgame.deployRear();
        }else if(rearElevation > frontElevation + TOLERANCE|| rearElevation >= targetElevation && timerRear.get() < 20){
            endgame.retractRear();
            timerRear.start();
            endgame.deployFront();
        }else{
            System.out.println("Deploying both");
            endgame.deployRear();
            endgame.deployFront();
        }
        
        if(timerFront.get() > 20){
            endgame.deployFront();
            timerFront.reset();
        }
        if(timerRear.get() > 20){
            endgame.deployRear();
            timerRear.reset();
        }
    }

    @Override
    protected void interrupted() {
        endgame.retractRear();
        endgame.retractFront();
        end();
        System.out.println("Cylinder Deploy interrupted");
    }

    @Override
    protected void end(){
        endgame.deployRear();
        endgame.deployFront();
        System.out.println("Ended at: " + timer.get());
        timer.stop();
        timer.reset();
        timerFront.stop();
        timerFront.reset();
        timerRear.stop();
        timerRear.reset();
    }

    @Override
    protected boolean isFinished() {
        return frontElevation >= targetElevation && rearElevation >= targetElevation;
    }

}