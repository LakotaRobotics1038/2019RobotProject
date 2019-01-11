/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  

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
    camera.setExposureManual(50);
    camera.setFPS(30);
    camera.setResolution(300, 200);
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
    
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    driver();
    if (driverJoystick.getStartButton() != previousStartButtonState && previousStartButtonState == false) {
      robotDrive.driveModeToggler();
    }
    previousStartButtonState = driverJoystick.getStartButton();
    System.out.println(driverJoystick.getStartButton());
    System.out.println(robotDrive.currentDriveMode);
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
  }
}
