/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;

/**
 * Uses the CameraServer class to automatically capture video from a USB webcam
 * and send it to the FRC dashboard without doing any vision processing. This
 * is the easiest way to get camera images to the dashboard. Just add this to
 * the robotInit() method in your program.
 */
public class Robot extends TimedRobot {
  //Rename cameras to more fun names
  UsbCamera VisionCam;
  UsbCamera PythonCam;
  boolean prevTrigger = false;
  CameraServer VisionCamServer;
  CameraServer PythonCamServer;
  operatorJoystick Joystick1038 = new operatorJoystick(1);
  public void robotInit() {
    //ports might have to be changed
    VisionCam = CameraServer.getInstance().startAutomaticCapture(0);
    PythonCam = CameraServer.getInstance().startAutomaticCapture(1);
  
  }
  void TeleopPeriodic() {
    //Switching the different cameras
    
    
  }
  public void operator() {
    public boolean getRightTrigger() {
      return getRawButton(RIGHT_TRIGGER);
    }

    if (Joystick1038.GetTrigger() && !prevTrigger) {
      System.out.println("Setting PythonCam\n");
      VisionCamServer.startAutomaticCapture(PythonCam);
    } else if (!Joystick1038.GetTrigger() && prevTrigger) {
      System.out.println("Setting VisionCam\n");
      PythonCamServer.startAutomaticCapture(VisionCam);
    }
    prevTrigger = Joystick1038.GetTrigger();
  }
}