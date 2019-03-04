package frc.robot.auton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.robot.ArduinoReader;
import frc.robot.subsystems.Endgame;


public class EndgameCylindersDeploy extends Command {

    private double frontElevation;
    private double rearElevation;
    private double targetElevation;
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
        timerRear.start();
        System.out.println("Timer started");
        endgame.deployFront();
        endgame.deployRear();
    }

    @Override
    protected void execute() {
        frontElevation = arduinoReader.getFrontLaserVal();
        rearElevation = arduinoReader.getRearLaserVal();
        System.out.println("Front elevation: " + frontElevation + ", Rear elevation: " + rearElevation);
        if(Math.abs(rearElevation - frontElevation) > 9) {
            System.out.println("i wanna retract them both");
            endgame.retractFront();
            endgame.retractRear();
        }
        // if(rearElevation > frontElevation && timerRear.get() < 0.1){
        //     endgame.retractRear();
        //     timerRear.reset();
        //     System.out.println("Retract Rear");
        // }else if(rearElevation < frontElevation){
        //     endgame.deployRear();
        //     timerRear.reset();
        //     System.out.println("Retract Rear");
        // }
    }

    @Override
    protected void interrupted() {
        end();
        System.out.println("Cylinder Deploy interrupted");
    }

    @Override
    protected void end(){
        endgame.retractRear();
        endgame.retractFront();
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