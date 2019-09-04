package frc.robot.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class CANSpark1038 extends CANSparkMax {
    public CANSpark1038(int id, CANSparkMaxLowLevel.MotorType type) {
        super(id, type);
    }
}