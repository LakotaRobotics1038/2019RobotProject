package frc.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class EndgameSequence{

    CommandGroup group = new CommandGroup();

    public CommandGroup getEndgameSequenceGroup(){
        group.addParallel(new MoveCylinders(MoveCylinders.Value.down, true));
        group.addSequential(new MoveCylinders(MoveCylinders.Value.down, false));
        group.addSequential(new TurnEndgameMotor(0.3));
        group.addSequential(new MoveCylinders(MoveCylinders.Value.up, true));
        group.addSequential(new TurnEndgameMotor(0.3));
        group.addSequential(new MoveCylinders(MoveCylinders.Value.up, false));
        group.addSequential(new DriveStraightCommand(10));
        return group;
    }
}