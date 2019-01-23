/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  
  private SendableChooser<String> autonList;
  private Object[][] autonInstructions;
  private String fileNow;
  private long startTime;
  private File autonFile;
  private String oldLine;
  private Joystick joystick;
  private Spark spark0;
  private Spark spark1;
  private DriveTrain driveTrain;

  

  // Drive
  public static DriveTrain robotDrive = DriveTrain.getInstance();
  
  // Joystick
  private Joystick1038 driverJoystick = new Joystick1038(0);
  public boolean previousStartButtonState = driverJoystick.getStartButton();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    driveTrain = DriveTrain.getInstance();
    camera.setExposureManual(50);
    camera.setFPS(30);
    camera.setResolution(300, 200);
    autonList = new SendableChooser();
    autonList.addDefault("Pick a code", null);
    for (File file : new File("/home/lvuser/autoncode").listFiles()) {
      if (!file.isDirectory()) {
        autonList.addObject(file.getName(), file.getAbsolutePath());
        System.out.println(file.getAbsolutePath());
      }
    }
    SmartDashboard.putData("Auton Code", autonList);

    joystick = new Joystick(0);

    //spark0 = new Spark1038(0);
    //  spark1 = new Spark1038(1);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    File autonCode = new File(autonList.getSelected());
    System.out.println("Current Code: " + autonCode.getAbsolutePath());
    autonInstructions = CSV.csv2table(autonCode);
    // TODO: run autonomous from autonInstructions;
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
  }


  /**
   * This function wasn't here before.
   */
  @Override
  public void teleopInit() {
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
  }  // TODO: make methods that run in less than 20 milliseconds to fix the screaming differential drive


  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    
    double joystickPos1, joystickPos2;
    joystickPos1 = joystick.getY();
    joystickPos2 = joystick.getTwist();
    
    //spark0.setSpeed(joystickPos1 * .5);
    //spark1.setSpeed(joystickPos2 * .5);
    driveTrain.dualArcadeDrive(joystickPos1, joystickPos2);
    
    Object[] newLineArr = {System.currentTimeMillis() - startTime, joystickPos1, joystickPos2};
    //Object[] newLineArr = {System.currentTimeMillis() - startTime, spark0.getSpeed(), spark1.getSpeed()};
    // uncomment later //System.out.println(Arrays.toString(newLineArr));
  
    String newLine = combine(Arrays.copyOfRange(newLineArr, 1, newLineArr.length));
    if (0 == newLine.compareTo(oldLine)) {
      System.out.println("Upadting file!");
      oldLine = newLine;
      CSV.writeLine2csv(newLineArr, autonFile);
    }
    
    
    /*
    driver();
    if (driverJoystick.getStartButton() != previousStartButtonState && previousStartButtonState == false) {
      robotDrive.driveModeToggler();
    }
    previousStartButtonState = driverJoystick.getStartButton();
    System.out.println(driverJoystick.getStartButton());
    System.out.println(robotDrive.currentDriveMode);
    */ // what is all this 
  }

  // Handle driver input
  public void driver() {
    
		switch (robotDrive.currentDriveMode) {
		case tankDrive:
			robotDrive.tankDrive(driverJoystick.getLeftJoystickVertical(), driverJoystick.getRightJoystickVertical());			
			break;
		case dualArcadeDrive:
			robotDrive.dualArcadeDrive(driverJoystick.getLeftJoystickVertical(), driverJoystick.getRightJoystickHorizontal());
			break;
		case singleArcadeDrive:
			robotDrive.singleAracadeDrive(driverJoystick.getLeftJoystickVertical(), driverJoystick.getLeftJoystickHorizontal());
			break;
		}	
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    System.out.println("Get out of Test");
  }


public static String combine(Object[] arr) {
  String ret = "";
  for (int i=0; i<arr.length; i++) {
    ret += arr[i].toString();
    if (i != arr.length - 1)
      ret += ",";
  }
  return ret;
}
}