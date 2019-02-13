package frc.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class EndgameSequence{

    private double deployTime = 20;
    CommandGroup group = new CommandGroup();

    public CommandGroup getEndgameSequenceGroup(){
        group.addParallel(new EndgameCylindersDeploy(18));
        group.addSequential(new EndgameCylindersDeploy(18));
        group.addSequential(new TurnEndgameMotor(0.3));
        group.addSequential(new EndgameCylinderRetract(deployTime, EndgameCylinderRetract.Value.front));
        group.addSequential(new TurnEndgameMotor(0.3));
        group.addSequential(new EndgameCylinderRetract(deployTime, EndgameCylinderRetract.Value.rear));
        group.addSequential(new DriveStraightCommand(10));
        return group;
    }
}