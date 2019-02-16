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
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.auton.Auton;
import frc.robot.auton.DynamicDashboard;
import frc.robot.auton.EndgameCylinderRetract;
import frc.robot.auton.EndgameCylindersDeploy;
import frc.robot.auton.TurnMotorPID;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Dashboard;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Endgame;
import frc.robot.subsystems.Scoring;
import frc.robot.auton.ArduinoReader;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
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
  UsbCamera VisionCam;
  boolean prevTrigger = false;
  CameraServer VisionCamServer;
  VideoSink server;
  //Joystick1038 Joystick1 = new Joystick1038(1);
  // Encoder1038 testEncoder = new Encoder1038(0, 1, false, 497, 2);

  public static final String JOY_TEST = "joytest";
  public static final String RECORD = "record";

  //private XboxJoystick1038 stick;
  //private DriveTrain driveTrain;
  private String autonSelected;
  private Auton auton;

  public Compressor c = new Compressor();
  public AnalogInput analogInput = new AnalogInput(0);
  CommandGroup group = new CommandGroup();
  public DoubleSolenoid a;
  public DoubleSolenoid b;
  private Endgame endgame = Endgame.getInstance();
  public CANSpark1038 endgameMotor = new CANSpark1038(57);
  public Encoder1038 endgameEncoder = new Encoder1038(1, 0, true, 200, 4); //last two are placeholders

  // Drive
 // public static DriveTrain robotDrive = DriveTrain.getInstance();

  // Joystick
  private XboxJoystick1038 driverJoystick = new XboxJoystick1038(0);
  public boolean previousStartButtonState = driverJoystick.getLineButton();

  // Dashboard
 // DynamicDashboard PIDChanger = DynamicDashboard.getInstance();

  // Auton
  Scheduler schedule = Scheduler.getInstance();
  private boolean scheduleEnabled = false;
  private ArduinoReader arduinoReader = ArduinoReader.getInstance();

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
   

  }
  @Override
  public void robotPeriodic(){
  }

  public void teleopInit() {
    c.setClosedLoopControl(true);
  }

  public void teleopPeriodic() {
    double averageVolts = analogInput.getAverageVoltage();
    double percentVolts = averageVolts / 5;
    double pressure = percentVolts * 200;
    System.out.println("Front Elevation: " + arduinoReader.returnArduinoFrontLaserValue());
    System.out.println("Rear Elevation: " + arduinoReader.returnArduinoRearLaserValue());
    System.out.println(pressure + " PSI");
    if(driverJoystick.getAButton()){
      endgame.retractFront();
    }
    if(driverJoystick.getXButton() && !endgame.getIsRearDeployed()) {
      endgame.deployRear();
      endgameMotor.set(0);
    }
    else if(driverJoystick.getXButton() && endgame.getIsRearDeployed()) {
      System.out.println("Encoder: " + endgameEncoder.get());
      endgameMotor.set(-1);
    }
    else{
      endgameMotor.set(0);
    }
    if(driverJoystick.getBButton()) {
      endgame.deployFront();
    }
    if(driverJoystick.getYButton()) {
      endgame.retractRear();
    }
  }

  public void autonomousInit() {
    c.setClosedLoopControl(true);
    // schedule.removeAll();
    // schedule.add(new EndgameCylindersDeploy(18));
    schedule.removeAll();
    group.addParallel(new EndgameCylinderRetract(5, EndgameCylinderRetract.Value.front));
    group.addParallel(new EndgameCylinderRetract(5, EndgameCylinderRetract.Value.rear));
    schedule.add(group);
  }

  public void autonomousPeriodic() {
    schedule.run();
  }

  public void disabledInit() {

  }

  // Handle driver input
  // public void driver() {
  //   switch (robotDrive.currentDriveMode) {
  //   case tankDrive:
  //     robotDrive.tankDrive(driverJoystick.getLeftJoystickVertical(), driverJoystick.getRightJoystickVertical());
  //     break;
  //   case dualArcadeDrive:
  //     robotDrive.dualArcadeDrive(driverJoystick.getLeftJoystickVertical(), driverJoystick.getRightJoystickHorizontal());
  //     break;
  //   case singleArcadeDrive:
  //     robotDrive.singleAracadeDrive(driverJoystick.getLeftJoystickVertical(),
  //         driverJoystick.getLeftJoystickHorizontal());
  //     break;
  //   }
  // }

  public void operator() {

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
