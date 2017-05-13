package org.firstinspires.ftc.robotcontroller.external.samples;


import android.graphics.Color;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;



/**
 * Created by T420s on 1/14/2017.
 */
public class BeaconAutonomous extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    UltrasonicSensor ultrasonicSensor;
    int leftPos;
    int rightPos;
    static int start_to_initial_turn_position = 5000;
    static int to_beacon_position = 500;
    static int turn_position = 250;
    static int beacon_to_beacon_position = 450;
    static int to_move_ball_position = 750;

    final int RED = 0, BLU = 1;
    final int TEAM = RED;

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle //Sleep Time
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position

    float hsvValues[] = {0F,0F,0F};
    final float values[] = hsvValues;


    Servo servo;
    ColorSensor colorSensor;
    double  position = (MAX_POS - MIN_POS) / 2; // Start at halfway position


    public void runOpMode() {
        leftMotor = hardwareMap.dcMotor.get("leftmotor");
        rightMotor = hardwareMap.dcMotor.get("rightmotor");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("sonic");
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        colorSensor = hardwareMap.colorSensor.get("color sensor");


        leftPos = 0;
        rightPos = 0;


        colorSensor.enableLed(true);

        //servo = hardwareMap.servo.get("button servo");


        // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo." );
        telemetry.update();
        waitForStart();

        reset_motor_encoders();

        //Drive to position that will allow 90 degree turn to align with beacon.
        while(rightPos < start_to_initial_turn_position /*&& rightPos < start_to_initial_turn_position*/)
        {
            leftMotor.setTargetPosition(start_to_initial_turn_position);
            rightMotor.setTargetPosition(start_to_initial_turn_position);
            leftMotor.setPower(0.5);
            rightMotor.setPower(0.5);
            leftPos = leftMotor.getCurrentPosition();
            rightPos = rightMotor.getCurrentPosition();
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            telemetry.addData("Left Position: ", leftPos);
            telemetry.addData("Right Position: ", rightPos);
            telemetry.update();

        }

        reset_motor_encoders();

        //Turn 90 degrees left.
        turn_left();

        reset_motor_encoders();

        go_to_beacon_and_check_colors();

        reset_motor_encoders();

        //Turn 90 degrees right.
        turn_right();

        reset_motor_encoders();

        //From first beacon turn to second beacon turn.
        while(rightPos < beacon_to_beacon_position)
        {
            leftMotor.setTargetPosition(start_to_initial_turn_position);
            rightMotor.setTargetPosition(start_to_initial_turn_position);
            leftMotor.setPower(0.5);
            rightMotor.setPower(0.5);
            leftPos = -leftMotor.getCurrentPosition();
            rightPos = -rightMotor.getCurrentPosition();
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        //Turn 90 degrees left.
        turn_left();

        reset_motor_encoders();

        go_to_beacon_and_check_colors();

        reset_motor_encoders();

        turn_right();

        reset_motor_encoders();

        turn_right();

        reset_motor_encoders();

        turn_45_degrees_right();

        while(rightPos < to_move_ball_position)
        {
            leftMotor.setTargetPosition(start_to_initial_turn_position);
            rightMotor.setTargetPosition(start_to_initial_turn_position);
            leftMotor.setPower(0.5);
            rightMotor.setPower(0.5);
            leftPos = -leftMotor.getCurrentPosition();
            rightPos = -rightMotor.getCurrentPosition();
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
    }
    private void turn_left()
    {
        while(rightPos < turn_position)
        {
            leftMotor.setTargetPosition(start_to_initial_turn_position);
            rightMotor.setTargetPosition(start_to_initial_turn_position);
            leftMotor.setPower(-0.5);
            rightMotor.setPower(0.5);
            leftPos = leftMotor.getCurrentPosition();
            rightPos = rightMotor.getCurrentPosition();
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    private void turn_right()
    {
        while(rightPos < turn_position)
        {
            leftMotor.setTargetPosition(start_to_initial_turn_position);
            rightMotor.setTargetPosition(start_to_initial_turn_position);
            leftMotor.setPower(0.5);
            rightMotor.setPower(-0.5);
            leftPos = leftMotor.getCurrentPosition();
            rightPos = rightMotor.getCurrentPosition();
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    private void turn_45_degrees_right()
    {
        while(leftPos < turn_position/2 && rightPos < turn_position)
        {
            leftMotor.setTargetPosition(start_to_initial_turn_position);
            rightMotor.setTargetPosition(start_to_initial_turn_position);
            leftMotor.setPower(0.5);
            rightMotor.setPower(-0.5);
            leftPos = leftMotor.getCurrentPosition();
            rightPos = rightMotor.getCurrentPosition();
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    private void go_to_beacon_and_check_colors()
    {
        //From turn to beacon.
        while(leftPos < to_beacon_position && rightPos < to_beacon_position)
        {
            leftMotor.setTargetPosition(start_to_initial_turn_position);
            rightMotor.setTargetPosition(start_to_initial_turn_position);
            leftMotor.setPower(0.5);
            rightMotor.setPower(0.5);
            leftPos = leftMotor.getCurrentPosition();
            rightPos = rightMotor.getCurrentPosition();
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        reset_motor_encoders();
        check_and_press_beacon(hsvValues, values );

        //From beacon to turn.
        while(leftPos < to_beacon_position && rightPos < to_beacon_position)
        {
            leftMotor.setTargetPosition(start_to_initial_turn_position);
            rightMotor.setTargetPosition(start_to_initial_turn_position);
            leftMotor.setPower(-0.5);
            rightMotor.setPower(-0.5);
            leftPos = leftMotor.getCurrentPosition();
            rightPos = rightMotor.getCurrentPosition();
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

    }

    private int color_check(float mValues[], final float values[])
    {
        //Check color, return 0 for red, 1 for blue.
        //HSV array; check color hue, saturation, value.
        for(int i = 0; i < 20; i++)
        {
            Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, mValues);
            telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue ", colorSensor.blue());
            telemetry.addData("Hue", mValues[0]);
            telemetry.update();
            sleep(25);
        }

        return 0;
    }

    private boolean press_button(int color)
    {

       /* switch(color) {
            case(BLU):
                if(TEAM == RED) {
                    return true;
                } else {
                    return false;
                }
                break;
            case(RED):
                if(TEAM == BLU) {
                    return true;
                } else {
                    return false;
                }
                break;
            default:
                return false;
        }*/
        return false;
    }


    private void check_and_press_beacon(float hsvValues[], float values[])
    {
        //Check color beacon and press button using servo;
        //for(;;){}
        press_button(color_check(hsvValues, values));
        //while(!true){sleep(10);}

    }


    private void reset_motor_encoders()
    {
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftPos = 0;
        rightPos = 0;

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

}
