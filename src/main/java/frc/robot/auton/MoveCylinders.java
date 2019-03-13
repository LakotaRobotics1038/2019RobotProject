package frc.robot.auton;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Endgame;

public class MoveCylinders extends Command {

    public enum Value {
        up, down
    };

    private int frontElevation; // set to laser value
    private int backElevation; // set to laser value
    private int setpoint;
    private int upSetpoint = 5;
    private int downSetpoint = 20;
    public Value setPosition;
    private Endgame endgame = Endgame.getInstance();
    private boolean moveFront;

    /**
     * Instantiates the move cylinders command
     * 
     * @param pos     Whether the cylinders are moving up or down
     * @param isFront Whether you are moving the front cylinder (true) or the rear
     *                cylinder (false)
     */
    public MoveCylinders(final Value pos, boolean isFront) {
        setPosition = pos;
        moveFront = isFront;
    }

    @Override
    protected void initialize() {
        switch (setPosition) {
        case up:
            setpoint = upSetpoint;
            break;
        case down:
            setpoint = downSetpoint;
            break;
        }
    }

    @Override
    protected void execute() {
        switch (setPosition) {
        case up:
            if (moveFront && frontElevation < downSetpoint) {
                endgame.retractFront();
            } else if (!moveFront && backElevation < downSetpoint) {
                endgame.retractRear();
            }
            break;
        case down:
            if (moveFront) {
                endgame.deployFront();
            } else {
                endgame.deployRear();
            }
            break;
        }
    }

    @Override
    protected boolean isFinished() {
        switch (setPosition) {
        case up:
            if (moveFront) {
                return frontElevation > upSetpoint;
            } else {
                return backElevation > upSetpoint;
            }
        case down:
            if (moveFront) {
                return frontElevation > downSetpoint;
            } else {
                return backElevation > downSetpoint;
            }
        }
        return false;
    }

}