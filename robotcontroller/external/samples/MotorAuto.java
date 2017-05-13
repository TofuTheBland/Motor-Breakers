package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by T420s on 1/21/2017.
 */
public class MotorAuto extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;





    public void runOpMode() {

        leftMotor  = hardwareMap.dcMotor.get("leftmotor");
        rightMotor = hardwareMap.dcMotor.get("rightmotor");
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        setMotors(1);
        sleep(5000);
        setMotors(0);
        sleep(1000);
        setMotors(-1);
        sleep(2000);
        setMotors(0);
        stop();
    }
    private void setMotors(int speed)
    {
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
    }
}
