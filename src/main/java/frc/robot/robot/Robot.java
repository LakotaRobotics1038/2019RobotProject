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
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.auton.EndgameCylinderRetract;
import frc.robot.depricated.EndgameCylindersDeploy;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Dashboard;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Endgame;
import frc.robot.subsystems.Scoring;
import edu.wpi.first.wpilibj.AnalogInput;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // Rename cameras to more fun names
  UsbCamera visionCam = CameraServer.getInstance().startAutomaticCapture();
  // boolean prevTrigger = false;

  // Endgame
  private Endgame endgame = Endgame.getInstance();

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
  boolean goingUp = false;
  boolean goingDown = false;

  // Scoring
  Scoring scoring = Scoring.getInstance();

  // Test
  // CANSpark1038 scoringMotor1 = new CANSpark1038(55, MotorType.kBrushed);
  CANSpark1038 scoringMotor2 = new CANSpark1038(56, MotorType.kBrushless);
  CANSpark1038 ballacqMotor = new CANSpark1038(59, MotorType.kBrushed);
  // CANSpark1038 wristMotor = new CANSpark1038(60, MotorType.kBrushed);
  // CANSpark1038 vacuumGen = new CANSpark1038(58, MotorType.kBrushed);
  // Encoder1038 scoringEncoder1 = new Encoder1038(1, 0, false, 1000000, 1);
  // Encoder1038 scoringEncoder2 = new Encoder1038(2, 3, false, 1000000, 1);
  // Gyro1038 gyro = new Gyro1038();
  // AnalogInput pressureSensor = new AnalogInput(0);
  ArduinoReader arduinoReader = ArduinoReader.getInstance();
  // DoubleSolenoid hatchDetatch = new DoubleSolenoid(7, 6);

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // scoringMotor1.restoreFactoryDefaults();
    //scoringMotor2.restoreFactoryDefaults();
    ballacqMotor.restoreFactoryDefaults();
    // wristMotor.restoreFactoryDefaults();
    // vacuumGen.restoreFactoryDefaults();
    // scoringMotor1.setIdleMode(IdleMode.kBrake);
    //scoringMotor2.setIdleMode(IdleMode.kBrake);
    ballacqMotor.setIdleMode(IdleMode.kBrake);
    // wristMotor.setIdleMode(IdleMode.kBrake);
    // vacuumGen.setIdleMode(IdleMode.kBrake);
    // arduinoReader.initialize();
    visionCam.setExposureManual(40);
    arduinoReader.initialize();
  }

  @Override
  public void robotPeriodic() {
    dashboard.update();
    arduinoReader.readArduino();
  }

  public void teleopInit() {
    // c.clearAllPCMStickyFaults();
    // c.setClosedLoopControl(true);
    schedule.removeAll();
    // scoringMotor1.restoreFactoryDefaults();
    scoringMotor2.restoreFactoryDefaults();
    ballacqMotor.restoreFactoryDefaults();
    // wristMotor.restoreFactoryDefaults();
    // scoringMotor1.setIdleMode(IdleMode.kBrake);
    scoringMotor2.setIdleMode(IdleMode.kBrake);
    ballacqMotor.setIdleMode(IdleMode.kBrake);
    // wristMotor.setIdleMode(IdleMode.kBrake);
    endgame.retractFront();
    endgame.retractRear();
    // hatchDetatch.set(DoubleSolenoid.Value.kForward);
  }

  public void teleopPeriodic() {
    // arduinoReader.readArduino();
    driver();
    //driveTrain.dualArcadeDrive(0, 0);
    operator();
    System.out.println(arduinoReader.getScoringAccelerometerVal());
    // System.out.println("scoring encoder: " + scoringMotor2.getEncoder().getPosition());
    // double volts = pressureSensor.getAverageVoltage();
    // double percentage = volts / 5;
    // double pressure = percentage * 200;
    //gyro.readGyro();
    //System.out.println(endgame.getEncoderCounts() + ", " + scoringEncoder1.get() + ", " + scoringEncoder2.get() + ", " + gyro.getAngle());
    // System.out.println("Scoring encoder one: " + scoringEncoder1.get() + " \n Scoring encoder two: "
    //     + scoringEncoder2.get() + " \n Endgame encoder: " + endgame.getEncoderCounts() + " \n Left encoder: "
    //     + driveTrain.getCANSparkLeftEncoder() + " \n Right encoder: " + driveTrain.getCANSparkRightEncoder()
    //     + " \n Gyro: " + gyro.getAngle() + " \n Pressure: " + pressure + " \n Front left laser: "
    //     + arduinoReader.returnArduinoFrontLeftLaserValue() + " \n Front right laser: "
    //     + arduinoReader.returnArduinoFrontRightLaserValue() + " \n Front bottom laser: "
    //     + arduinoReader.returnArduinoFrontLaserValue() + " \n Rear bottom laser: "
    //     + arduinoReader.returnArduinoRearLaserValue() + " \n Accelerometer: "
    //     + arduinoReader.returnScoringAccelerometerValue() + " \n Wrist current: " + wristMotor.getOutputCurrent());
  }

  public void autonomousInit() {
    c.setClosedLoopControl(true);
    schedule.removeAll();
    schedule.add(new EndgameCylindersDeploy(35));
  }

  public void autonomousPeriodic() {
    schedule.run();
  }

  public void disabledInit() {

  }

  // Handle driver input
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
      endgame.retractFront();
      endgame.retractRear();
    }
    if (driverJoystick.getXButton()) {
      endgame.deployFront();
      endgame.deployRear();
    }
    if (driverJoystick.getAButton()) {
      endgame.retractFront();
      // driverJoystick.setLeftRumble(1); //Should be heavy rumble
    }
    if (driverJoystick.getBButton()) {
      endgame.retractRear();
      // driverJoystick.setLeftRumble(1); //Should be heavy rumble
    }
    if (driverJoystick.getLeftButton()) {
      endgame.setRearMotor(0.4);
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

    if (DriverStation.getInstance().getMatchTime() < 30 && !DriverStation.getInstance().isAutonomous()) {
      // driverJoystick.setLeftRumble(1); //hard rumble
    }
  }

  public void operator() {
    if (operatorJoystick.getLeftButton()) {
      acquisition.acqCargo();
    }
    else if (operatorJoystick.getLeftTrigger() > 0.5) {
      acquisition.disposeCargo();
    }
    else{
      acquisition.stop();
    }
    if (operatorJoystick.getRightButton()) {
      acquisition.acqHatch();
    }
    else if (operatorJoystick.getRightTrigger() > 0.5) {
      acquisition.dropHatch();
      // driverJoystick.setLeftRumble(0.5); //Should be light
      // operatorJoystick.setLeftRumble(0.5); //Should be light
    } 
    else {
      acquisition.stopHatch();
    }

    if (operatorJoystick.getXButton()) {
      // downTimer.reset();
      // downTimer.start();
      // goingDown = true;
      acquisition.tiltDown(-1);
    }
    else if (operatorJoystick.getAButton()) {
      // // scoring.moveToLvl1();
      // upTimer.reset();
      // upTimer.start();
      // goingUp = true;
      acquisition.tiltUp(1);
    }
    else {
      acquisition.stopTilt();
    }

    // System.out.println(downTimer.get());
    // if(downTimer.get() < .3 && goingDown == true) {
    //   acquisition.tiltDown(-1);
    // } else if(downTimer.get() < .7 && goingDown == true) {
    //   acquisition.tiltDown(-1);
    // } else if(downTimer.get() < 4 && goingDown == true) {
    //   acquisition.tiltDown(-1);
    // } else{
    //   acquisition.stopTilt();
    //   downTimer.stop();
    //   goingDown = false;
    // }

    // if(upTimer.get() < .5 && goingUp == true) {
    //   acquisition.tiltUp(.2);
    // } else if(upTimer.get() < 1 && goingUp == true) {
    //   acquisition.tiltUp(.4);
    // } else if(upTimer.get() < 1.5 && goingUp == true) {
    //   acquisition.tiltUp(.2);
    // } else{
    //   acquisition.stopTilt();
    //   upTimer.stop();
    //   goingUp = false;
    // }

    // if (operatorJoystick.getBButton()) {
    //   scoring.moveToLvl2();
    // }
    // if (operatorJoystick.getYButton()) {
    //   scoring.moveToLvl3();
    // }
   if (Math.abs(operatorJoystick.getRightJoystickVertical()) > 0.1) {
      // scoring.move(operatorJoystick.getRightJoystickVertical());
      scoringMotor2.set(operatorJoystick.getRightJoystickVertical());
    }

    // if (DriverStation.getInstance().getMatchTime() < 30 && !DriverStation.getInstance().isAutonomous()) {
    //   // operatorJoystick.setLeftRumble(1); //hard rumble
    // }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testInit() {
    schedule.removeAll();
    group.addParallel(new EndgameCylinderRetract(5, EndgameCylinderRetract.Value.front));
    group.addSequential(new EndgameCylinderRetract(5, EndgameCylinderRetract.Value.front));
    schedule.add(group);
  }

  @Override
  public void testPeriodic() {
    schedule.run();
  }
}
