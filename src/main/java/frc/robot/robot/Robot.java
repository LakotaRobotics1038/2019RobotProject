/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.robot;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.auton.EndgameCylinderRetract;
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

  // Rename cameras to more fun names
  UsbCamera VisionCam;
  boolean prevTrigger = false;
  CameraServer VisionCamServer;
  VideoSink server;

  //Endgame
  private Endgame endgame = Endgame.getInstance();

  // Drive
  private DriveTrain driveTrain = DriveTrain.getInstance();
  public Compressor c = new Compressor();

  // Joystick
  private XboxJoystick1038 driverJoystick = new XboxJoystick1038(0);
  private XboxJoystick1038 operatorJoystick = new XboxJoystick1038(1);
  public boolean previousStartButtonState = driverJoystick.getLineButton();
  public double  multiplyer;

  // Dashboard
  Dashboard dashboard = Dashboard.getInstance();

  // Auton
  Scheduler schedule = Scheduler.getInstance();
  CommandGroup group = new CommandGroup();

  //Acquisition
  Acquisition acquisition = Acquisition.getInstance();

  //Scoring
  Scoring scoring = Scoring.getInstance();

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
  }
  @Override
  public void robotPeriodic(){
    dashboard.update();
  }


  public void teleopInit() {
    c.setClosedLoopControl(true);
    schedule.removeAll();
  }

  public void teleopPeriodic() {
    driver();
  }

  public void autonomousInit() {
    c.setClosedLoopControl(true);
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

  //Handle driver input
  public void driver() {
    multiplyer = .6;
    if(driverJoystick.getRightTrigger() > 0.5){
      driveTrain.highGear();
    }else{
      driveTrain.lowGear();
    }
    if(driverJoystick.getRightButton()){
      multiplyer = 1;
      driveTrain.highGear();
    }
    
    if(driverJoystick.getYButton()){
      endgame.retractFront();
      endgame.retractRear();
    }
    if(driverJoystick.getXButton()) {
      endgame.deployFront();
      endgame.deployRear();
    }
    if(driverJoystick.getAButton()) {
      endgame.retractFront();
      driverJoystick.setLeftRumble(1); //Should be heavy rumble
    }
    if(driverJoystick.getBButton()){
      endgame.retractRear();
      driverJoystick.setLeftRumble(1); //Should be heavy rumble
    }
    if(driverJoystick.getLeftButton()){
      endgame.setRearMotor(0.4);
    }else{
      endgame.setRearMotor(0.0);
    }
  
    switch (driveTrain.currentDriveMode) {
    case tankDrive:
      driveTrain.tankDrive(driverJoystick.getLeftJoystickVertical() * multiplyer, driverJoystick.getRightJoystickVertical() * multiplyer);
      break;
    case dualArcadeDrive:
      driveTrain.dualArcadeDrive(driverJoystick.getLeftJoystickVertical()  * multiplyer, driverJoystick.getRightJoystickHorizontal() * multiplyer);
      break;
    case singleArcadeDrive:
      driveTrain.singleAracadeDrive(driverJoystick.getLeftJoystickVertical() * multiplyer, driverJoystick.getLeftJoystickHorizontal() * multiplyer);
      break;
    }

    if(DriverStation.getInstance().getMatchTime() < 30 && !DriverStation.getInstance().isAutonomous()){
      driverJoystick.setLeftRumble(1); //hard rumble
    }
  }
  public void operator() {
    if(operatorJoystick.getLeftButton()){
      acquisition.acquire();
    }
    if(operatorJoystick.getLeftTrigger() > 0.5){
      acquisition.dispose();
    }
    if(operatorJoystick.getRightButton()){
      acquisition.acqHatch();
    }
    if(operatorJoystick.getRightTrigger() > 0.5){
      acquisition.dropHatch();
      driverJoystick.setLeftRumble(0.5); //Should be light
      operatorJoystick.setLeftRumble(0.5); //Should be light
    }

    if(operatorJoystick.getXButton()){
      scoring.moveToGround();
    }
    if(operatorJoystick.getAButton()){
      scoring.moveToLvl1();
    }
    if(operatorJoystick.getBButton()){
      scoring.moveToLvl2();
    }
    if(operatorJoystick.getYButton()){
      scoring.moveToLvl3();
    }
    if(Math.abs(operatorJoystick.getRightJoystickVertical()) > 0.09){
      scoring.move(operatorJoystick.getRightJoystickVertical());
    }

    if(DriverStation.getInstance().getMatchTime() < 30 && !DriverStation.getInstance().isAutonomous()){
      operatorJoystick.setLeftRumble(1); //hard rumble
    }
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
