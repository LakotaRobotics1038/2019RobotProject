package frc.robot.robot;

import edu.wpi.first.wpilibj.Encoder;

public class Encoder1038 extends Encoder {
	public Encoder1038(int channelA, int channelB, boolean isInverted, int countsPerRevolution, double wheelDiameter) {
		super(channelA, channelB, isInverted);
		setDistancePerPulse(findDistancePerPulse(countsPerRevolution, wheelDiameter));
	}

	/**
	 * Calculates distance per pulse of the encoder
	 * 
	 * @param countsPerRevolution The counts in one revolution of the motor with the
	 *                            encoder
	 * @param wheelDiameter       The diameter of the wheel being turned
	 * @return The distance per pulse in units used the measure diameter per pulse
	 */
	public static double findDistancePerPulse(double countsPerRevolution, double wheelDiameter) {
		return (1 / countsPerRevolution) * (wheelDiameter * Math.PI);
	}
}