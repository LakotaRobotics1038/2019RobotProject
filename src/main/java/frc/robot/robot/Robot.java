/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.robot;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.auton.EndgameCylindersDeploy;
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

  // Auton
  Scheduler schedule = Scheduler.getInstance();
  CommandGroup group = new CommandGroup();

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

  // Test
  CANSpark1038 ballacqMotor = new CANSpark1038(59, MotorType.kBrushed);
  // CANSpark1038 wristMotor = new CANSpark1038(60, MotorType.kBrushed);

  // Arduino
  ArduinoReader arduinoReader = ArduinoReader.getInstance();

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {

    c.setClosedLoopControl(true);
    ballacqMotor.restoreFactoryDefaults();
    // wristMotor.restoreFactoryDefaults();
    ballacqMotor.setIdleMode(IdleMode.kBrake);
    // wristMotor.setIdleMode(IdleMode.kBrake);
    visionCam.setExposureManual(60);
    arduinoReader.initialize();
  }

  @Override
  public void robotPeriodic() {
    dashboard.update();
    arduinoReader.readArduino();
    // System.out.println(scoring.returnArmPot());
  }

  public void teleopInit() {
    arduinoReader.initialize();
    c.setClosedLoopControl(true);
    schedule.removeAll();
    ballacqMotor.restoreFactoryDefaults();
    ballacqMotor.setIdleMode(IdleMode.kBrake);
    endgame.retractFront();
    // endgame.retractRear();
    arduinoReader.initialize();
    group.addSequential(new EndgameCylindersDeploy(40));
    schedule.add(group);
  }

  public void teleopPeriodic() {
    arduinoReader.readArduino();
    // arduinoReader.getScoringAccelerometerVal();
    // System.out.println(endgame.getScrewCounts());
    driver();
    operator();
  }

  public void autonomousInit() {
    arduinoReader.initialize();
    c.setClosedLoopControl(true);
    schedule.removeAll();
    ballacqMotor.restoreFactoryDefaults();
    ballacqMotor.setIdleMode(IdleMode.kBrake);
    endgame.retractFront();
    endgame.retractRear();
    arduinoReader.initialize();
    group.addSequential(new EndgameCylindersDeploy(40));
    schedule.add(group);
  }

  public void autonomousPeriodic() {
    arduinoReader.readArduino();
    driver();
    operator();
  }

  public void disabledInit() {
    arduinoReader.stopSerialPort();
    scoring.disable();
  }

  public void disabledPeriodic() {
  }

  /**
   * Runs driver operations according to button map
   */
  public void driver() {
    multiplyer = .6;
    if (driverJoystick.getRightTrigger() > 0.5) {
      driveTrain.highGear();
    } else {
      driveTrain.lowGear();
    }
    if (driverJoystick.getRightButton()) {
      multiplyer = 1;
      driveTrain.highGear();
    }

    if (driverJoystick.getYButton()) {
      // endgame.retractFront();
      // endgame.retractRear();
      // isDeploying = false;
      //endgame.deployRear(-1);
    }
    else if (driverJoystick.getBButton()) {
      endgame.retractRear();
      // driverJoystick.setLeftRumble(1); //Should be heavy rumble
    }
    else if (driverJoystick.getXButton()) {
      isDeploying = true;
      //endgame.deployEndgame();
      endgame.deployRear(-1);
    }
    else{
      endgame.stopRear();
    }
    if (driverJoystick.getAButton()) {
      endgame.retractFront();
      // driverJoystick.setLeftRumble(1); //Should be heavy rumble
    }
    if (driverJoystick.getLeftButton()) {
      endgame.setRearMotor(0.4);
    } else if (driverJoystick.getLeftTrigger() > 0.5) {
      endgame.setRearMotor(-0.4);
    } else {
      endgame.setRearMotor(0.0);
    }

    // if (isDeploying) {
    //   schedule.run();
    //   if (arduinoReader.getFrontBottomLaserVal() >= 15 && arduinoReader.getRearBottomLaserVal() >= 15) {
    //     isDeploying = false;
    //   }
    // } else if (!isDeploying) {
    //   schedule.removeAll();
    //   schedule.add(group);
    // }

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
  }

  /**
   * Runs operator controls according to button map
   */
  public void operator() {
    if (operatorJoystick.getLeftButton()) {
      acquisition.acqCargo();
    } else if (operatorJoystick.getLeftTrigger() > 0.5) {
      acquisition.disposeCargo();
    } else {
      acquisition.stop();
    }
    if (operatorJoystick.getRightButton()) {
      acquisition.acqHatch();
    } else if (operatorJoystick.getRightTrigger() > 0.5) {
      acquisition.dropHatch();
      // driverJoystick.setLeftRumble(0.5); //Should be light
      // operatorJoystick.setLeftRumble(0.5); //Should be light
    } else {
      acquisition.stopHatch();
    }

    if (operatorJoystick.getXButton()) {
      // scoring.setLevel(-50);
      prevXButtonState = currentXButtonState;
      currentXButtonState = true;
    }
    if (operatorJoystick.getAButton()) {
      scoring.setLevel(-37);
    }
    if (operatorJoystick.getBButton()) {
      scoring.setLevel(10);
    }
    if (operatorJoystick.getYButton()) {
      scoring.setLevel(55);
    }
    

    if (Math.abs(operatorJoystick.getLeftJoystickVertical()) > 0.25) {
      // wristMotor.set(operatorJoystick.getLeftJoystickVertical());
    } else {
      // wristMotor.set(0);
    }

    if (operatorJoystick.getXButton()) {
      scoring.setLevel(-15);
    }

    if (operatorJoystick.getPOV() == 180) {
      scoring.setLevel(-48);
    }

    if (currentXButtonState && !prevXButtonState) {
      isGoingDown = !isGoingDown;
      // acquisition.setHeight(isGoingDown);
    }

    if (Math.abs(operatorJoystick.getRightJoystickVertical()) > 0.25) {
      scoring.move(operatorJoystick.getRightJoystickVertical());
      manualSet = true;
    }else if(Math.abs(operatorJoystick.getRightJoystickVertical()) < 0.25 && manualSet){
      scoring.move(0);
      manualSet = false;
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
