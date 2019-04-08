/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Dashboard;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Endgame;
import frc.robot.subsystems.Scoring;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // Camera
  UsbCamera visionCam = CameraServer.getInstance().startAutomaticCapture();

  // Endgame
  private Endgame endgame = Endgame.getInstance();
  private boolean isDeploying = false;

  // Drive
  private DriveTrain driveTrain = DriveTrain.getInstance();
  public Compressor c = new Compressor();

  // Joystick
  private XboxJoystick1038 driverJoystick = new XboxJoystick1038(0);
  private XboxJoystick1038 operatorJoystick = new XboxJoystick1038(1);
  public boolean previousStartButtonState = driverJoystick.getLineButton();
  public double multiplyer;

  // Dashboard
  Dashboard dashboard = Dashboard.getInstance();
  public static SendableChooser<String> endgameChooser = new SendableChooser<>();
  public static final String kLvl2 = "Lvl2";
  public static final String kLvl3 = "Lvl3";

  // Auton
  Scheduler schedule = Scheduler.getInstance();
  CommandGroup group = new CommandGroup();
  DriverStation driverStation = DriverStation.getInstance();

  // Acquisition
  Acquisition acquisition = Acquisition.getInstance();
  Timer downTimer = new Timer();
  Timer upTimer = new Timer();
  boolean prevXButtonState = false;
  boolean currentXButtonState = false;
  boolean isGoingDown = false;

  // Scoring
  boolean manualSet = false;
  Scoring scoring = Scoring.getInstance();

  // Arduino
  ArduinoReader arduinoReader = ArduinoReader.getInstance();

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    endgameChooser.addOption("Lvl 2", kLvl2);
    endgameChooser.addOption("Lvl 3", kLvl3);
    SmartDashboard.putData("Level", endgameChooser);
    c.setClosedLoopControl(true);
    visionCam.setExposureManual(50);
  }

  @Override
  public void robotPeriodic() {
    dashboard.update();
  }

  public void teleopInit() {
    arduinoReader.initialize();
    c.setClosedLoopControl(true);
    schedule.removeAll();
    endgame.retractFront();
  }

  public void teleopPeriodic() {
    arduinoReader.readArduino();
    driver();
    operator();
  }

  public void autonomousInit() {
    c.setClosedLoopControl(true);
    schedule.removeAll();
    endgame.retractFront();
  }

  public void autonomousPeriodic() {
    arduinoReader.readArduino();
    driver();
    operator();
  }

  public void disabledInit() {
    driveTrain.highGear();
    scoring.disable();
  }

  public void disabledPeriodic() {
  }

  /**
   * Runs driver operations according to button map
   */
  public void driver() {
    if (driverJoystick.getRightTrigger() > 0.5) {
      driveTrain.highGear();
    } else{
      driveTrain.lowGear();
    }
    if(driverJoystick.getRightButton()){
      multiplyer = 1;
    }else{
      multiplyer = .6;
    }

    if (driverJoystick.getYButton()) {
      endgame.overrideRear();
    }
    else if (driverJoystick.getBButton()) {
      endgame.retractRear();
    }
    else if (driverJoystick.getXButton()) {
      isDeploying = true;
      endgame.deployEndgame();
    }
    else{
      endgame.stopRear();
    }
    if (driverJoystick.getAButton()) {
      endgame.retractFront();
    }
    if (driverJoystick.getLeftButton()) {
      endgame.setRearMotor(0.4);
    } else if (driverJoystick.getLeftTrigger() > 0.5) {
      endgame.setRearMotor(-0.4);
    } else {
      endgame.setRearMotor(0.0);
    }

    switch (driveTrain.currentDriveMode) {
    case tankDrive:
      driveTrain.tankDrive(driverJoystick.getLeftJoystickVertical() * multiplyer,
          driverJoystick.getRightJoystickVertical() * multiplyer);
      break;
    case dualArcadeDrive:
      driveTrain.dualArcadeDrive(driverJoystick.getLeftJoystickVertical() * multiplyer,
          driverJoystick.getRightJoystickHorizontal() * multiplyer);
      break;
    case singleArcadeDrive:
      driveTrain.singleAracadeDrive(driverJoystick.getLeftJoystickVertical() * multiplyer,
          driverJoystick.getLeftJoystickHorizontal() * multiplyer);
      break;
    }

    if(driverStation.getMatchTime() == 30){
      driverJoystick.setRightRumble(0.3);
    }
  }

  /**
   * Runs operator controls according to button map
   */
  public void operator() {
    if (operatorJoystick.getLeftButton()) {
      acquisition.acqCargo();
      System.out.println("Acquiring Cargo");
    } else if (operatorJoystick.getLeftTrigger() > 0.5) {
      acquisition.disposeCargo();
      System.out.println("Disposing Cargo");
    } else {
      acquisition.stop();
    }
    if (operatorJoystick.getRightButton()) {
      acquisition.acqHatch();
    } else if (operatorJoystick.getRightTrigger() > 0.5) {
      acquisition.dropHatch();
    } else {
      acquisition.stopHatch();
    }

    if (operatorJoystick.getXButton()) {
      prevXButtonState = currentXButtonState;
      currentXButtonState = true;
    }
    if (operatorJoystick.getAButton()) {
      System.out.println("pushing buttons");
      scoring.setLevel(-41);
    }
    if (operatorJoystick.getBButton()) {
      System.out.println("pushing buttons");
      scoring.setLevel(7);
    }
    if (operatorJoystick.getYButton()) {
      System.out.println("pushing buttons");
      scoring.setLevel(50);
    }

    if (operatorJoystick.getXButton()) {
      System.out.println("pushing buttons");
      scoring.setLevel(-18);
    }

    if (operatorJoystick.getPOV() == 180) {
      System.out.println("pushing buttons");
      scoring.setLevel(-55);
    }

    if (currentXButtonState && !prevXButtonState) {
      isGoingDown = !isGoingDown;
    }

    if (Math.abs(operatorJoystick.getRightJoystickVertical()) > 0.25) {
      scoring.move(operatorJoystick.getRightJoystickVertical());
      manualSet = true;
    }else if(Math.abs(operatorJoystick.getRightJoystickVertical()) < 0.25 && manualSet){
      scoring.move(0);
      manualSet = false;
    }

    if(driverStation.getMatchTime() == 30){
      operatorJoystick.setRightRumble(0.3);
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }
}
