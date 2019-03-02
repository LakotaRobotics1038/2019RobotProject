package frc.robot.robot;

import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoReader {
    private static SerialPort arduinoPort;
    private static ArduinoReader arduinoReader;
    private String arduinoOutput;
    public String arduinoDataMap[];
    public int frontLaserSensorData = 0;
    public double lineFollowerData = 0;
    public int rearLaserSensorData = 0;
    public int frontLeftLaserSensorData = 0;
    public int frontRightLaserSensorData = 0;
    public int scoringAccelerometerData = 0;
    public int acquisitionAccelerometerData = 0;

    private ArduinoReader(){

    }

    public static ArduinoReader getInstance(){
        if(arduinoReader == null){
            arduinoReader = new ArduinoReader();
        }
        return arduinoReader;
    }

    public void initialize(){
        arduinoPort = new SerialPort(115200, SerialPort.Port.kUSB1);
        System.out.println("Created new arduino reader");
    }

    public void readArduino(){
        try{
            arduinoOutput = arduinoPort.readString();
            arduinoDataMap = arduinoOutput.split(",");
            frontLaserSensorData = Integer.parseInt(arduinoDataMap[0]);
            rearLaserSensorData = Integer.parseInt(arduinoDataMap[1]);
            frontLeftLaserSensorData = Integer.parseInt(arduinoDataMap[2]);
            frontRightLaserSensorData = Integer.parseInt(arduinoDataMap[3]);
            //lineFollowerData = Double.parseDouble(arduinoDataMap[2]);
            scoringAccelerometerData = Integer.parseInt(arduinoDataMap[4]);
        } catch (NumberFormatException e2) {
            //System.out.println(e2 + e2.getMessage());
        }
    }

    public int getFrontLaserVal() {
        return frontLaserSensorData;
    }

    public int getRearLaserVal() {
        return rearLaserSensorData;
    }

    public int getFrontLeftLaserVal() {
        return frontLeftLaserSensorData;
    }

    public int getFrontRightLaserVal() {
        return frontRightLaserSensorData;
    }

    public double getLineFollowerVal() {
        return lineFollowerData;
    }

    public int getScoringAccelerometerVal(){
        return scoringAccelerometerData;
    }

    public int getAcqAccelerometerVal(){
        return acquisitionAccelerometerData;
    }
}