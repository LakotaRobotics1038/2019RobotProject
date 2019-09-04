package frc.robot.depricated;

import edu.wpi.first.wpilibj.DigitalInput;

public class Prox extends DigitalInput {

    public Prox(int port) {
        super(port);
    }

    /**
     * Returns the state of the prox
     * 
     * @return if there is metal in front of the prox
     */
    public boolean get() {
        return !super.get();
    }
}