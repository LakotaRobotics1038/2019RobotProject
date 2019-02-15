package frc.robot.auton;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Endgame;

public class EndgameCylinderRetract extends TimedCommand {

    //private DriveTrain drive = DriveTrain.getInstance();
    private Endgame endgame = Endgame.getInstance();
    private boolean isFront;

    public enum Value {
        front, rear
    };

    public EndgameCylinderRetract(double timeout, Value position) {
        super(timeout);
        requires(endgame);
        //requires(drive);
        switch (position) {
        case front:
            isFront = true;
            break;
        case rear:
            isFront = false;
            break;
        }
    }

    @Override
    public void execute() {
        if (isFront) {
            endgame.retractFront();
        } else if (!isFront) {
            endgame.retractRear();
        }
    }
}