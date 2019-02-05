/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.robot;

import java.util.Map;

import javax.lang.model.element.VariableElement;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Scoring;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // Rename cameras to more fun names
  UsbCamera VisionCam;
  UsbCamera PythonCam;
  boolean prevTrigger = false;
  CameraServer VisionCamServer;
  CameraServer PythonCamServer;
  VideoSink server;
  Joystick1038 Joystick1 = new Joystick1038(1);
  Spark1038 testMotor = new Spark1038(0);
  NetworkTableEntry example = Shuffleboard.getTab("My Tab").add("My Number", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min", -1, "Max", 2, "Block increment", 0.5)).getEntry();
  NetworkTableEntry example2 = Shuffleboard.getTab("My Tab").add("My Dial", 10).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("Min", 10, "Max", 110, "Show value", true)).getEntry();

  public static final String JOY_TEST = "joytest";
  public static final String RECORD = "record";

  private XboxJoystick1038 stick;
  private DriveTrain driveTrain;
  private String autonSelected;
  private Auton auton;

  public Compressor c;
  public DoubleSolenoid a;
  public DoubleSolenoid b;
  

  // Drive
  public static DriveTrain robotDrive = DriveTrain.getInstance();

  // Joystick
  private XboxJoystick1038 driverJoystick = new XboxJoystick1038(0);
  public boolean previousStartButtonState = driverJoystick.getLineButton();

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    
  }

  public void teleopInit(){
  }

  public void teleopPeriodic() {
    example.setNumber(2);
    example2.setNumber(100);
    testMotor.set(0.5);
  }

  public void autonomousInit(){

  }

  public void autonomousPeriodic(){

  }

  public void disabledInit(){

  }

  public void driver(){

  }

  /**
   * This function wasn't here before.
   */
  @Override
  public void teleopInit() {
    driveTrain.resetEncoder();
    switch (autonSelected) {
    case RECORD:
      auton.recordInit();
      break;
    }
  } // TODO: make methods that run in less than 20 milliseconds to fix the screaming
    // differential drive

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    switch (autonSelected) {
    case JOY_TEST:
      break;
    case RECORD:
      auton.recordPeriodic();
      break;
    /*
     * driver(); if (driverJoystick.getStartButton() != previousStartButtonState &&
     * previousStartButtonState == false) { robotDrive.driveModeToggler(); }
     * previousStartButtonState = driverJoystick.getStartButton();
     * System.out.println(driverJoystick.getStartButton());
     * System.out.println(robotDrive.currentDriveMode);
     */ // what is all this
    }
  }

  public void disabledInit() {
    auton.disabledInit();
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
      robotDrive.singleAracadeDrive(driverJoystick.getLeftJoystickVertical(),
          driverJoystick.getLeftJoystickHorizontal());
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

}