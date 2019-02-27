package frc.robot.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;

public class ArduinoReader {

    FileInputStream in;
    BufferedReader inputStreamReader;
    private static ArduinoReader arduinoReader;
    public String arduinoDataMap[];
    public int frontLaserSensorData = 0;
    public double lineFollowerData = 0;
    public int rearLaserSensorData = 0;
    public int frontLeftLaserSensorData = 0;
    public int frontRightLaserSensorData = 0;
    public int scoringAccelerometerData = 0;
    public int acquisitionAccelerometerData = 0;

    public ArduinoReader() {
    }

    public static ArduinoReader getInstance() {
        if (arduinoReader == null) {
            System.out.println("Creating a new Laser Reader");
            arduinoReader = new ArduinoReader();
        }
        return arduinoReader;
    }

    // Can parse from string into any other variable type. Just add return method
    public void getArduinoData() {
        try {
            if (inputStreamReader == null) {
                inputStreamReader = getBufferedReader(inputStreamReader);
            }
            String data = inputStreamReader.readLine();
            if (data != null) {
                // prevEncoderValue = currEncoderValue;
                // currEncoderValue = encoder.get();
                // lineFollowerPrev = lineFollowerData;
                arduinoDataMap = data.split(",");
                try{
                    frontLaserSensorData = Integer.parseInt(arduinoDataMap[0]);
                    rearLaserSensorData = Integer.parseInt(arduinoDataMap[1]);
                    frontLeftLaserSensorData = Integer.parseInt(arduinoDataMap[2]);
                    frontRightLaserSensorData = Integer.parseInt(arduinoDataMap[3]);
                    lineFollowerData = Double.parseDouble(arduinoDataMap[2]);
                    scoringAccelerometerData = Integer.parseInt(arduinoDataMap[4]);
                    acquisitionAccelerometerData = Integer.parseInt(arduinoDataMap[4]);
                    System.out.println(frontLaserSensorData + ", " + rearLaserSensorData + ", " + frontLeftLaserSensorData + ", " + frontRightLaserSensorData + ", " + lineFollowerData + ", " + scoringAccelerometerData + ", " + acquisitionAccelerometerData);
                }catch(NumberFormatException e){
                }
            }
        } catch (IOException e) {
            System.out.println(e + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e1) {
            System.out.println(e1 + e1.getMessage());
        } catch (NumberFormatException e2) {
            System.out.println(e2 + e2.getMessage());
        }
    }

    public BufferedReader getBufferedReader(BufferedReader bufferedReader) throws IOException {
        BufferedReader bf = bufferedReader;
        bf = new BufferedReader(new InputStreamReader(new FileInputStream("/dev/ttyUSB0")));
        return bf;
    }

    // Returns laser sensor value from arduinoDataMap
    public int returnArduinoFrontLaserValue() {
        //getArduinoData();
        return frontLaserSensorData;
    }

    public int returnArduinoRearLaserValue() {
       // getArduinoData();
        return rearLaserSensorData;
    }

    public int returnArduinoFrontLeftLaserValue() {
       // getArduinoData();
        return frontLeftLaserSensorData;
    }

    public int returnArduinoFrontRightLaserValue() {
        //getArduinoData();
        return frontRightLaserSensorData;
    }

    public double returnArduinoTapeValue() {
       // getArduinoData();
        // if(lineFollowerData != -1 && lineFollowerPrev != -1) {
        // double tapeDifference = lineFollowerData - lineFollowerPrev;
        // double pulseDifference = currEncoderValue - prevEncoderValue;
        // double distance = pulseDifference * encoder.getDistancePerPulse();
        // double tanInv = tapeDifference / distance;
        // double angle = java.lang.Math.tan(tanInv);
        // return angle;
        // }
        // else{
        // return -1;
        // }
        return lineFollowerData;
    }

    public int returnScoringAccelerometerValue(){
        //getArduinoData();
        return scoringAccelerometerData;
    }

    public int returnAcquisitionAccelerometerValue(){
        //getArduinoData();
        return acquisitionAccelerometerData;
    }

}