/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.robot.ArduinoReader;
import frc.robot.robot.Robot;

public class Dashboard {

  //Variables
  private String endgameHeight;

  //Objects
  private static Dashboard dashboard;
  private ArduinoReader arduinoReader = ArduinoReader.getInstance();
  private DriverStation driverStation = DriverStation.getInstance();

  /**
   * Returns the dashboard instance created when the robot starts
   * 
   * @return Dashboard instance
   */
  public static Dashboard getInstance() {
    if (dashboard == null) {
      System.out.println("Creating a new Dashboard");
      dashboard = new Dashboard();
    }
    return dashboard;
  }

  /**
   * Instantiates dashboard object
   */
  private Dashboard() {
    SmartDashboard.putNumber("Left Distance", 0);
    SmartDashboard.putNumber("Right Distance", 0);
    SmartDashboard.putNumber("Match Time", -1);
  }

  /**
   * Updates dashboard with current values
   */
  public void update() {
    SmartDashboard.putNumber("Left Distance", arduinoReader.getFrontLeftLaserVal());
    SmartDashboard.putNumber("Right Distance", arduinoReader.getFrontRightLaserVal());
    SmartDashboard.putNumber("Match Time", driverStation.getMatchTime());    
    endgameHeight = Robot.endgameChooser.getSelected();
  }

  /**
   * 
   * @return The string for the endgame HAB level we want to end on
   */
  public String getEndgameHeight(){
    return endgameHeight;
  }
}
