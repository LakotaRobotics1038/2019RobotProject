package frc.robot.auton;

import com.sun.jdi.Value;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Endgame;

public class MoveCylinders extends Command {

    public enum Value {
        up, down
    };

    private int frontElevation; //set to laser value
    private int backElevation; //set to laser value
    private int setpoint;
    private int upSetpoint = 5; // Placeholder
    private int downSetpoint = 20; // Placeholder
    public Value setPosition;
    private Endgame endgame = Endgame.getInstance();
    private boolean moveFront;

    /**
     * @param pos     - either up or down, move cylinders up or down respectively
     * @param isFront - true if the front cylinders should move
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
                endgame.raiseFront();
            } else if (!moveFront && backElevation < downSetpoint) {
                endgame.raiseBack();
            }
            break;
        case down:
            if (moveFront) {
                endgame.lowerFront();
            } else {
                endgame.lowerBack();
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