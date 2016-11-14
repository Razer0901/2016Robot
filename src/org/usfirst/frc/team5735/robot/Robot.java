
package org.usfirst.frc.team5735.robot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class Robot extends IterativeRobot {
	
	public static final int PORT_USB_DRIVE_CONTROLLER = 0, PORT_USB_SHOOTER_CONTROLLER = 1;
	public static final int PORT_PWM_DRIVETRAIN_LEFT = 0, PORT_PWM_DRIVETRAIN_RIGHT = 1;
	public static final int PORT_CAN_MOTOR_SHOOTER_MASTER = 1, PORT_CAN_MOTOR_SHOOTER_SLAVE = 2, PORT_CAN_MOTOR_SHOOTER_PUSHER = 3, PORT_CAN_MOTOR_SHOOTER_ARM = 4, PORT_CAN_MOTOR_SHOOTER_PIVOT = 5;
	public static final int PORT_ANALOG_SHOOTER_ARM_POT = 0, PORT_ANALOG_SHOOTER_PIVOT_POT = 1;
	public static final int PORT_DIO_ENCODER_RIGHT = 0, PORT_DIO_ENCODER_LEFT = 2;
	
	XboxController driveController = new XboxController(PORT_USB_DRIVE_CONTROLLER);
	XboxController shooterController = new XboxController(PORT_USB_SHOOTER_CONTROLLER);
	
	CANTalon shooterFlywheelMasterMotor = new CANTalon(PORT_CAN_MOTOR_SHOOTER_MASTER);
	CANTalon shooterFlywheelSlaveMotor = new CANTalon(PORT_CAN_MOTOR_SHOOTER_SLAVE);
	CANTalon shooterPusherMotor = new CANTalon(PORT_CAN_MOTOR_SHOOTER_PUSHER);
	CANTalon shooterArmMotor = new CANTalon(PORT_CAN_MOTOR_SHOOTER_ARM);
	CANTalon shooterPivotMotor = new CANTalon(PORT_CAN_MOTOR_SHOOTER_PIVOT);
	
//	Potentiometer shooterArmPot = new AnalogPotentiometer(PORT_ANALOG_SHOOTER_ARM_POT, 360, 0);
//	Potentiometer shooterPivotPot = new AnalogPotentiometer(PORT_ANALOG_SHOOTER_PIVOT_POT, 360, 0);
//	
//	Encoder leftEncoder = new Encoder(PORT_DIO_ENCODER_LEFT, PORT_DIO_ENCODER_LEFT+1, false, Encoder.EncodingType.k4X);
//	Encoder rightEncoder = new Encoder(PORT_DIO_ENCODER_RIGHT, PORT_DIO_ENCODER_RIGHT+1, false, Encoder.EncodingType.k4X);
//	
//	double counter = 0;
	
	RobotDrive driveTrain = new RobotDrive(PORT_PWM_DRIVETRAIN_LEFT,PORT_PWM_DRIVETRAIN_RIGHT);
	
//	CameraServer camServer;
	
    public void robotInit(){
		shooterFlywheelMasterMotor.set(0);
		shooterFlywheelSlaveMotor.changeControlMode(CANTalon.ControlMode.Follower);
		shooterFlywheelSlaveMotor.set(shooterFlywheelMasterMotor.getDeviceID());
		shooterFlywheelSlaveMotor.reverseOutput(true);
		shooterPusherMotor.set(0);
		shooterArmMotor.set(0);
		shooterPivotMotor.set(0);
		
		//Init Webcam
//    	camServer = CameraServer.getInstance();
//        camServer.setQuality(50);
//        camServer.startAutomaticCapture("cam0");
    }

    public void autonomousPeriodic() {

    }
    
    public void teleopPeriodic() {
    	while (isOperatorControl() && isEnabled()){
    		if(shooterController.getLinearAxis(XboxController.RIGHT_TRIGGER_AXIS) > 0.5){
        		shooterFlywheelMasterMotor.set(1);
    		}else{
    			shooterFlywheelMasterMotor.set(0);
    		}
    		
    		if(shooterController.isButtonPressed(XboxController.A_BUTTON)){
        		shooterPusherMotor.set(0.4);
    		}else if (shooterController.isButtonReleased(XboxController.A_BUTTON)){
    			shooterPusherMotor.set(0);
    		}
    		
    		if(shooterController.isButtonPressed(XboxController.X_BUTTON)){
    			shooterFlywheelMasterMotor.set(1);
    			shooterPusherMotor.set(-0.4);
    		}else if (shooterController.isButtonReleased(XboxController.X_BUTTON)){
    			shooterPusherMotor.set(0);
    			shooterFlywheelMasterMotor.set(0);
    		}
        		
			double shooterArmAngle = shooterController.getCubicAxis(XboxController.RIGHT_Y_AXIS);
			double shooterArmFactor = 0.45;
			shooterArmMotor.set(shooterArmAngle*shooterArmFactor);
			
			double shooterPivotAngle = shooterController.getCubicAxis(XboxController.LEFT_Y_AXIS);
			double shooterPivotFactor = 0.5;
			shooterPivotMotor.set(shooterPivotAngle*shooterPivotFactor);
    		
    		
    		double driveScale = 0.6+0.2*driveController.getCubicAxis(XboxController.RIGHT_TRIGGER_AXIS);
    		double rotateScale = 0.7;
    		
    		//DriveTrain Control
    		if(driveController.isButtonHeld(XboxController.R_BUTTON)){
        		driveTrain.arcadeDrive(-driveController.getCubicAxis(XboxController.RIGHT_Y_AXIS)*driveScale, -driveController.getCubicAxis(XboxController.LEFT_X_AXIS)*rotateScale);
    		}else{
        		driveTrain.arcadeDrive(driveController.getCubicAxis(XboxController.RIGHT_Y_AXIS)*driveScale, -driveController.getCubicAxis(XboxController.LEFT_X_AXIS)*rotateScale);
    		}
    		
//    		if(counter%40 == 0){
//				System.out.println("Left Encoder:" + leftEncoder.get());
//				System.out.println("Right Encoder:" + rightEncoder.get());
//				System.out.println("Shooter Arm Pot:" + shooterArmPot.get());
//				System.out.println("Shooter Pivot Pot:" + shooterPivotPot.get());
//    		}
//    		
//    		counter++;
    		Timer.delay(0.05);
    	}
    }
    
    public void testPeriodic() {
    	
    }
}
