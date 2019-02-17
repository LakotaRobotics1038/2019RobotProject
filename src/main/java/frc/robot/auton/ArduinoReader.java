package frc.robot.auton;

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
                frontLaserSensorData = Integer.parseInt(arduinoDataMap[0]);
                rearLaserSensorData = Integer.parseInt(arduinoDataMap[1]);
                //lineFollowerData = Double.parseDouble(arduinoDataMap[2]);
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
        bf = new BufferedReader(new InputStreamReader(new FileInputStream("/dev/ttyS1")));
        return bf;
    }

    // Returns laser sensor value from arduinoDataMap
    public int returnArduinoFrontLaserValue() {
        getArduinoData();
        return frontLaserSensorData;
    }

    public int returnArduinoRearLaserValue() {
        getArduinoData();
        return rearLaserSensorData;
    }

    public double returnArduinoTapeValue() {
        getArduinoData();
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

}