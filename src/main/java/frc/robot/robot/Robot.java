/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.auton.Auton;
import frc.robot.subsystems.DriveTrain;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

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
    // UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    driveTrain = DriveTrain.getInstance();
    // camera.setExposureManual(50);
    // camera.setFPS(30);
    // camera.setResolution(300, 200);

    stick = new XboxJoystick1038(0);
    auton = new Auton(driveTrain, stick);
    autonSelected = RECORD;

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // if (driverJoystick.getXButton()) {
    // System.out.println("X button");
    // }
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    driveTrain.resetEncoder();
    auton.playbackInit();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (autonSelected) {
    case RECORD:
      auton.playbackPeriodic();
      break;
    }

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