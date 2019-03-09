package frc.robot.auton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.robot.XboxJoystick1038;
import frc.robot.subsystems.DriveTrain;

public class Auton {

    private Object[][] autonInstructions;
    private String fileNow;
    private long startTime;
    private File autonFile;
    private String oldLine;
    private DriveTrain driveTrain;
    private XboxJoystick1038 stick;
    private double joystickPos1, joystickPos2;
    private int index;
    private int mode;
    private SendableChooser<String> autonList;

    public Auton(DriveTrain dt, XboxJoystick1038 s) {
        driveTrain = dt;
        stick = s;
        init();
    }

    private void init() {
        autonList = new SendableChooser();
        autonList.addDefault("Pick a code", null);
        for (File file : new File("/home/lvuser/autoncode").listFiles()) {
            if (!file.isDirectory()) {
                autonList.addObject(file.getName(), file.getAbsolutePath());
                System.out.println(file.getAbsolutePath());
            }
        }
        SmartDashboard.putData("Auton Code", autonList);
    }

    /**
     * 
     * This method should be called in autonomousInit()
     */
    public void playbackInit() {
        mode = 1; // playback (autonomous)
        File autonCode = new File(autonList.getSelected());
        System.out.println("Current Code: " + autonCode.getAbsolutePath());
        autonInstructions = frc.robot.auton.CSV.csv2table(autonFile);
        startTime = System.currentTimeMillis();
        index = 0;

    }

    public void printEncoders() {
        System.out.println("" + driveTrain.getCANSparkLeftEncoder() + "," + driveTrain.getCANSparkRightEncoder());
    }

    /**
     * 
     * This method should be called in autonomousPeriodic()
     */

    public void playbackPeriodic() {
        long currentTime = System.currentTimeMillis() - startTime;
        while (currentTime >= Long.parseLong((String) autonInstructions[index + 1][0])) {
            index++;
        }
        driveTrain.dualArcadeDrive(Double.parseDouble((String) autonInstructions[index][1]),
                Double.parseDouble((String) autonInstructions[index][2]));

         printEncoders();
        if (index == autonInstructions.length - 2) {
            // put anything here for when instruction is finished
        }
    }

    /**
     * 
     * This method should be called in teleopInit()
     */

    public void recordInit() {
        mode = 2; // record (teleop)
        fileNow = "/home/lvuser/autoncode/" + new SimpleDateFormat("yyyy.MM.dd.HHmmsszzz").format(new Date()) + ".csv";
        startTime = System.currentTimeMillis();
        autonFile = new File(fileNow);
        oldLine = "";
        try {
            System.out.println("Created file " + autonFile.getAbsolutePath());
            autonFile.createNewFile();
        } catch (IOException e) {
            System.out.println("IOException.");
            System.out.println(e + e.getMessage());
            e.printStackTrace();
            System.out.println(fileNow);
        }
    }

    /**
     * 
     * This method should be called in teleopPeriodic()
     */
    public void recordPeriodic() {
        joystickPos1 = stick.getLeftJoystickVertical();
        joystickPos2 = stick.getRightJoystickHorizontal();

        driveTrain.dualArcadeDrive(joystickPos1, joystickPos2);
        Object[] newLineArr = { System.currentTimeMillis() - startTime, joystickPos1, joystickPos2 };

        String newLine = combine(Arrays.copyOfRange(newLineArr, 1, newLineArr.length));

        if (0 != newLine.compareTo(oldLine)) {
            oldLine = newLine;
            frc.robot.auton.CSV.writeLine2csv(newLineArr, autonFile);

        }
         printEncoders();
    }

    public static String combine(Object[] arr) {
        String ret = "";
        for (int i = 0; i < arr.length; i++) {
            ret += arr[i].toString();
            if (i != arr.length - 1)
                ret += ",";
        }
        return ret;
    }

    /**
     * 
     * This method should be called in disabledInit()
     */
    public void disabledInit() {
        if (mode == 2) { // only execute code if we were just in teleop
            String[] finalLine = { "" + (System.currentTimeMillis() - startTime), "" + 0, "" + 0 };
            frc.robot.auton.CSV.writeLine2csv(finalLine, autonFile);
            finalLine[0] = "" + Long.MAX_VALUE;
            frc.robot.auton.CSV.writeLine2csv(finalLine, autonFile);
        }
        init();
    }
}