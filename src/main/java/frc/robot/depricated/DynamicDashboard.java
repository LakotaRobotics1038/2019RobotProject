package frc.robot.depricated;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Dashboard;

public class DynamicDashboard {

    Scheduler schedule = Scheduler.getInstance();
    Dashboard dashboard;
    private static double P;
    private static double PPast;
    public static double PCurrent;
    private static double I;
    private static double IPast;
    public static double ICurrent;
    private static double D;
    private static double DPast;
    public static double DCurrent;
    private static double setpoint;
    private static double setpointPast;
    public static double setpointCurrent;

    private static DynamicDashboard dynDash;

    /**
     * Instantiates the dynamic dashboard object
     */
    public DynamicDashboard() {

    }

    /**
     * Returns the instance of the created dashboard object
     * 
     * @return The dynamic dashboard object
     */
    public static DynamicDashboard getInstance() {
        if (dynDash == null) {
            System.out.println("Creating a new Dashboard");
            dynDash = new DynamicDashboard();
        }
        return dynDash;
    }

    /**
     * Initializes the dynamic dashboard by sending the P, I, and D values to the
     * dashbaord
     */
    public void initialize() {
        dashboard = Dashboard.getInstance();
        P = SmartDashboard.getNumber("PVal", -1);
        I = SmartDashboard.getNumber("IVal", -1);
        D = SmartDashboard.getNumber("DVal", -1);
        setpoint = SmartDashboard.getNumber("Setpoint", -1);
        dashboard.update();
        schedule.add(new TurnMotorPID(setpoint, P, I, D));
    }

    /**
     * What should be looped through in order to update the dynamic dashboard
     */
    public void periodic() {
        PCurrent = SmartDashboard.getNumber("PVal", -1);
        ICurrent = SmartDashboard.getNumber("IVal", -1);
        DCurrent = SmartDashboard.getNumber("DVal", -1);
        setpointCurrent = SmartDashboard.getNumber("Setpoint", -1);
        dashboard.update();

        System.out.println(PCurrent + ", " + ICurrent + ", " + DCurrent + ", " + setpointCurrent);

        schedule.run();
        if (PCurrent != PPast || ICurrent != IPast || DCurrent != DPast || setpointCurrent != setpointPast) {
            schedule.removeAll();
            schedule.add(new TurnMotorPID(setpointCurrent, PCurrent, ICurrent, DCurrent));
        }

        PPast = PCurrent;
        IPast = ICurrent;
        DPast = DCurrent;
        setpointPast = setpointCurrent;
    }
}