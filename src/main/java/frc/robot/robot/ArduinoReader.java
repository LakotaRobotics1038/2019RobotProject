package frc.robot.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import edu.wpi.first.hal.util.UncleanStatusException;
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
    public boolean stringRead = false;
    public BufferedReader bufferedReader;
    private static String inputBuffer;
    private String line;

    private ArduinoReader(){

    }

    public static ArduinoReader getInstance(){
        if(arduinoReader == null){
            arduinoReader = new ArduinoReader();
        }
        return arduinoReader;
    }

    public void initialize(){
        arduinoPort = new SerialPort(115200, SerialPort.Port.kMXP);
        System.out.println("Created new arduino reader");
    }

    public void readArduino(){
        try{
            stringRead = false;
            while(arduinoPort.getBytesReceived() != 0) {
                arduinoOutput = arduinoPort.readString();
                inputBuffer = inputBuffer + arduinoOutput;
                stringRead = true;
            }
            line = "";
            while(inputBuffer.indexOf("\r") != -1) {
                int point = inputBuffer.indexOf("\r");
                line = inputBuffer.substring(0, point);
                if(inputBuffer.length() > point+1){
                    inputBuffer = inputBuffer.substring(point + 2);
                }
                else {
                    inputBuffer = "";
                }
            }
            if(line != "") {
                arduinoDataMap = line.split(",");
                frontLaserSensorData = Integer.parseInt(arduinoDataMap[0]);
                rearLaserSensorData = Integer.parseInt(arduinoDataMap[1]);
                frontLeftLaserSensorData = Integer.parseInt(arduinoDataMap[2]);
                frontRightLaserSensorData = Integer.parseInt(arduinoDataMap[3]);
                //lineFollowerData = Double.parseDouble(arduinoDataMap[6]);
                acquisitionAccelerometerData = Integer.parseInt(arduinoDataMap[4]);
                scoringAccelerometerData = Integer.parseInt(arduinoDataMap[5]);
            }
        } catch (NumberFormatException e2) {
            //System.out.println(e2 + e2.getMessage());
        } catch (UncleanStatusException e) {
            //System.out.print("*");
        }
    }

    public void stopSerialPort() {
        System.out.println("im gonna close it");
        arduinoPort.close();
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