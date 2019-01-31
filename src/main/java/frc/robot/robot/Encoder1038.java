package frc.robot.robot;

import edu.wpi.first.wpilibj.Encoder;

public class Encoder1038 extends Encoder {
	public Encoder1038(int channelA, int channelB, boolean isInverted, int countsPerRevolution, double wheelDiameter) {
		super(channelA, channelB, isInverted);
		setDistancePerPulse(findDistancePerPulse(countsPerRevolution, wheelDiameter));
	}

	public static double findDistancePerPulse(double countsPerRevolution, double wheelDiameter) {
		return (1 / countsPerRevolution) * (wheelDiameter * Math.PI);
	}
}