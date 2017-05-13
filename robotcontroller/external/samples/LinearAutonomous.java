package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by T420s on 12/7/2016.
 */

public class LinearAutonomous extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;

    int leftPos;
    int rightPos;

    int desired_distance = 2000;
    int ninety_degree_turn_position = 250;

    public void runOpMode() {

        leftMotor  = hardwareMap.dcMotor.get("leftmotor");
        rightMotor = hardwareMap.dcMotor.get("rightmotor");
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        rightMotor.setPower(0.75);
        leftMotor.setPower(0.75);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);



        move_linearly_by_encoder(8000);
        move_linearly_by_encoder(-5000);
        turn_right_by_encoder();
        move_linearly_by_encoder(5000);

        /*
        sleep(200);
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        waitForStart();

        sleep(500);
        leftMotor.setPower(1);
        rightMotor.setPower(1);
        sleep(1500);
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        */

        stop();
    }

    private void reset_motor_encoders()
    {
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftPos = 0;
        rightPos = 0;

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //leftMotor.setTargetPosition(desired_distance);
        //rightMotor.setTargetPosition(desired_distance);

        //leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ///rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    private void move_linearly_by_encoder(int position)
    {
        reset_motor_encoders();
        rightMotor.setPower(0.75);
        leftMotor.setPower(0.75);
        while(rightPos < position )
        {
            //leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            rightMotor.setPower(0.75);
            leftMotor.setPower(0.75);


            leftMotor.setTargetPosition(position);
            rightMotor.setTargetPosition(position);

            leftPos = -leftMotor.getCurrentPosition();
            rightPos = rightMotor.getCurrentPosition();

            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            //if(position >= position){leftMotor.setPower(0);rightMotor.setPower(0);}



            telemetry.addData("Left Position: ", leftPos);
            telemetry.addData("Right Position: ", rightPos);
            telemetry.update();
        }
        leftMotor.setPower(0);rightMotor.setPower(0);
        reset_motor_encoders();
    }

    private void turn_right_by_encoder()
    {
        reset_motor_encoders();
        rightMotor.setPower(0.75);
        leftMotor.setPower(0.75);
        while(rightPos < ninety_degree_turn_position)
        {
            //leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            rightMotor.setPower(0.75);
            leftMotor.setPower(0.75);


            leftMotor.setTargetPosition(ninety_degree_turn_position);
            rightMotor.setTargetPosition(ninety_degree_turn_position);

            leftPos = -leftMotor.getCurrentPosition();
            rightPos = rightMotor.getCurrentPosition();

            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //if (rightPos >= ninety_degree_turn_position - 5)
            //{
              //  leftMotor.setPower(0);
                //rightMotor.setPower(0);
            //}

            leftMotor.setPower(0.5);
            rightMotor.setPower(-0.5);
            telemetry.addData("Left Position: ", leftPos);
            telemetry.addData("Right Position: ", rightPos);
            telemetry.update();
        }
        reset_motor_encoders();
    }

}
