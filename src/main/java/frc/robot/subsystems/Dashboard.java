/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import javax.swing.text.StyleContext.SmallAttributeSet;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.robot.Robot;

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
    System.out.println("Put PVal");
    SmartDashboard.putNumber("IVal", 0);
    System.out.println("Put IVal");
    SmartDashboard.putNumber("DVal", 0);
    System.out.println("Put DVal");
    SmartDashboard.putNumber("Setpoint", 100);
    System.out.println("Put Setpoint");
  }

  public void update(){
    SmartDashboard.putNumber("PVal", SmartDashboard.getNumber("PVal", -1));
    System.out.println("Put PVal");
    SmartDashboard.putNumber("IVal", SmartDashboard.getNumber("IVal", -1));
    System.out.println("Put IVal");
    SmartDashboard.putNumber("DVal", SmartDashboard.getNumber("DVal", -1));
    System.out.println("Put DVal");
    SmartDashboard.putNumber("Setpoint", SmartDashboard.getNumber("Setpoint", -1));
    System.out.println("Put Setpoint");
  }
}
