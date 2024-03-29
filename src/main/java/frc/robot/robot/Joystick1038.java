package frc.robot.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Joystick1038 extends Joystick {

	// Button Locations
	private final int X_BUTTON = 1;
	private final int A_BUTTON = 2;
	private final int B_BUTTON = 3;
	private final int Y_BUTTON = 4;
	private final int LEFT_BUTTON = 5;
	private final int RIGHT_BUTTON = 6;
	private final int LEFT_TRIGGER = 7;
	private final int RIGHT_TRIGER = 8;
	private final int BACK_BUTTON = 9;
	private final int START_BUTTON = 10;
	private final int LEFT_JOYSTICK_CLICK = 11;
	private final int RIGHT_JOYSTICK_CLICK = 12;

	// Joystick locations
	private final int LEFT_STICK_HORIZONTAL = 0;
	private final int LEFT_STICK_VERTICAL = 1;
	private final int RIGHT_STICK_HORIZONTAL = 2;
	private final int RIGHT_STICK_VERTICAL = 3;

	/**
	 * Creates a new logitech joystick object
	 * @param port USB port the joystick should be in
	 */
	public Joystick1038(int port) {
		super(port);
	}

	/**
	 * Returns the state of the X button on the controller
	 * 
	 * @return is the X button pressed
	 */
	public boolean getXButton() {
		return getRawButton(X_BUTTON);
	}

	/**
	 * Returns the state of the A button on the controller
	 * 
	 * @return is the A button pressed
	 */
	public boolean getAButton() {
		return getRawButton(A_BUTTON);
	}

	/**
	 * Returns the state of the B button on the controller
	 * 
	 * @return is the B button pressed
	 */
	public boolean getBButton() {
		return getRawButton(B_BUTTON);
	}

	/**
	 * Returns the state of the Y button on the controller
	 * 
	 * @return is the Y button pressed
	 */
	public boolean getYButton() {
		return getRawButton(Y_BUTTON);
	}

	/**
	 * Returns the state of the left button on the controller
	 * 
	 * @return is the left button pressed
	 */
	public boolean getLeftButton() {
		return getRawButton(LEFT_BUTTON);
	}

	/**
	 * Returns the state of the right button on the controller
	 * 
	 * @return is the right button pressed
	 */
	public boolean getRightButton() {
		return getRawButton(RIGHT_BUTTON);
	}

	/**
	 * Returns the state of the left trigger on the controller
	 * 
	 * @return is the left trigger pressed
	 */
	public boolean getLeftTrigger() {
		return getRawButton(LEFT_TRIGGER);
	}

	/**
	 * Returns the state of the right trigger on the controller
	 * 
	 * @return is the right trigger pressed
	 */
	public boolean getRightTrigger() {
		return getRawButton(RIGHT_TRIGER);
	}

	/**
	 * Returns the state of the back button on the controller
	 * 
	 * @return is the back button pressed
	 */
	public boolean getBackButton() {
		return getRawButton(BACK_BUTTON);
	}

	/**
	 * Returns the state of the start button on the controller
	 * 
	 * @return is the start button pressed
	 */
	public boolean getStartButton() {
		return getRawButton(START_BUTTON);
	}

	/**
	 * Returns the state of the left joystick click on the controller
	 * 
	 * @return is the left joystick button pressed
	 */
	public boolean getLeftJoystickClick() {
		return getRawButton(LEFT_JOYSTICK_CLICK);
	}

	/**
	 * Returns the state of the right joystick click on the controller
	 * 
	 * @return is the right joystick button pressed
	 */
	public boolean getRightJoystickClick() {
		return getRawButton(RIGHT_JOYSTICK_CLICK);
	}

	/**
	 * Returns the state of the left joystick on its vertical axis
	 * 
	 * @return value of the left joystick vertical axis, inverted so positive values
	 *         are joystick up
	 */
	public double getLeftJoystickVertical() {
		return getRawAxis(LEFT_STICK_VERTICAL) * -1;
	}

	/**
	 * Returns the state of the left joystick on its horizontal axis
	 * 
	 * @return value of the left joystick horizontal axis
	 */
	public double getLeftJoystickHorizontal() {
		return getRawAxis(LEFT_STICK_HORIZONTAL);
	}

	/**
	 * Returns the state of the right joystick on its vertical axis
	 * 
	 * @return value of the right joystick vertical axis, inverted so positive
	 *         values are joystick up
	 */
	public double getRightJoystickVertical() {
		return getRawAxis(RIGHT_STICK_VERTICAL) * -1;
	}

	/**
	 * Returns the state of the right joystick on its horizontal axis
	 * 
	 * @return value of the right joystick horizontal axis
	 */
	public double getRightJoystickHorizontal() {
		return getRawAxis(RIGHT_STICK_HORIZONTAL);
	}
}