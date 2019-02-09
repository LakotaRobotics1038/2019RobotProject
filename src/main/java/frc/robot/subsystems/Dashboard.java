/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class Dashboard{

  private static Dashboard dashboard;

  public static Dashboard getInstance() {
    if (dashboard == null) {
      System.out.println("Creating a new Dashboard");
      dashboard = new Dashboard();
    }
    return dashboard;
  }

  private Dashboard() {
    //SmartDashboard.Method(something);
    SmartDashboard.putNumber("PVal", 0);
    SmartDashboard.putNumber("IVal", 0);
    SmartDashboard.putNumber("DVal", 0);
    SmartDashboard.putNumber("Setpoint", 100);
  }

  public void update(){
    SmartDashboard.putNumber("PVal", SmartDashboard.getNumber("PVal", -1));
    SmartDashboard.putNumber("IVal", SmartDashboard.getNumber("IVal", -1));
    SmartDashboard.putNumber("DVal", SmartDashboard.getNumber("DVal", -1));
    SmartDashboard.putNumber("Setpoint", SmartDashboard.getNumber("Setpoint", -1));
  }
}
