package frc.robot.depricated;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.depricated.DriveStraightCommand;
import frc.robot.auton.EndgameCylinderRetract;
import frc.robot.auton.EndgameCylindersDeploy;
import frc.robot.depricated.TurnEndgameMotor;

public class EndgameSequence {

    private double deployTime = 20;
    CommandGroup group = new CommandGroup();

    /**
     * The command sequence for endgame
     * 
     * @return The command group of the commands to run the endgame sequence
     */
    public CommandGroup getEndgameSequenceGroup() {
        group.addSequential(new EndgameCylindersDeploy(18));
        group.addSequential(new TurnEndgameMotor(0.3));
        group.addSequential(new EndgameCylinderRetract(deployTime, EndgameCylinderRetract.Value.front));
        group.addSequential(new TurnEndgameMotor(0.3));
        group.addSequential(new EndgameCylinderRetract(deployTime, EndgameCylinderRetract.Value.rear));
        group.addSequential(new DriveStraightCommand(10));
        return group;
    }
}