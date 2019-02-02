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
 * Uses the CameraServer class to automatically capture video from a USB webcam
 * and send it to the FRC dashboard without doing any vision processing. This is
 * the easiest way to get camera images to the dashboard. Just add this to the
 * robotInit() method in your program.
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

  public void operator() {

  }
}
