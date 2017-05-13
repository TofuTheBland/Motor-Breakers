/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.robotcontroller.external.samples;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name= "Hit Ball.", group="Linear Opmode")  // @Autonomous(...) is the other common choice

public class HitBall extends LinearOpMode {

    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 3.9 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED         = 0.7;
    static final double     right_motor_correction = 1.45; //The right motor requires this many times more power
    //to correct for weight.

    //Encoder desired position distances.
    double turn_inches = 10.25;
    //Initialize one wall.
    double distance_to_turn_one = 10;
    //Turn 90 degrees.
    double distance_to_beacon_one = 10;
    double distance_to_turn_two = 10;
    //Turn -90 degrees.
    double distance_to_turn_three = 10;
    //Turn 90 degrees.
    double distance_to_beacon_two = 10;
    //Reverse and back away.
    double distance_to_ball_from_beacon = 10;
    int distance_to_check_color = 5;

    float hsvValues[] = {0F, 0F, 0F};
    final float values[] = hsvValues;
    float hue = 0;


    int num_checks = 0;
    int necessary_checks = 50;


    DcMotor leftMotor, rightMotor;
    UltrasonicSensor ultrasonicSensor;
    ColorSensor colorSensor;
    Servo buttonServo;


    @Override
    public void runOpMode() {

        /*
         * The init() method of the hardware class does all the work here
         */

        leftMotor = hardwareMap.dcMotor.get("leftmotor");
        rightMotor = hardwareMap.dcMotor.get("rightmotor");
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("sonic");
        colorSensor = hardwareMap.colorSensor.get("color sensor");
        buttonServo = hardwareMap.servo.get("buttonservo");

        colorSensor.enableLed(true);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        /*// Send telemetry message to indicate successful Encoder reset

                leftMotor.getCurrentPosition(),
                rightMotor.getCurrentPosition());
        */

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        sleep(8500);
        encoderDrive(0.3, 60, 60, 4.0);
        //encoderDrive(0.3, 5,5, 4.0);
        //encoderDrive(TURN_SPEED,   -turn_inches, turn_inches, 2.0);  // S2: Turn Left 12 Inches with _ Sec timeout
        //encoderDrive(TURN_SPEED,   turn_inches, -turn_inches, 2.0);  // S5: Turn Right 12 Inches with _ Sec timeou
        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        //encoderDrive(DRIVE_SPEED,  48,  48, 4.5);  // S1: Forward 48 Inches with 5 Sec timeout
        //encoderDrive(TURN_SPEED,   -turn_inches, turn_inches, 3.5);  // S2: Turn Left 12 Inches with 4 Sec timeout
        //encoderDrive(DRIVE_SPEED, 24, 24, 2.25);  // S3: Forward 24 Inches with 4 Sec timeout
        //check color here and press button
        //approach_beacon_and_check_colors();
        //encoderDrive(DRIVE_SPEED, -24,-24, 2.0); // S4: Reverse 24 Inches with 4 Sec timeout
        //encoderDrive(TURN_SPEED,   turn_inches, -turn_inches, 3.5);  // S5: Turn Right 12 Inches with 4 Sec timeout
        //encoderDrive(DRIVE_SPEED,  36,  36, 4.5);  // S6: Forward 48 Inches with 5 Sec timeout
        //encoderDrive(TURN_SPEED,   -turn_inches, turn_inches, 4.0);  // S7: Turn Left 12 Inches with 4 Sec timeout
        //encoderDrive(DRIVE_SPEED, 24, 24, 4.0);  // S8: Forward 24 Inches with 4 Sec timeout
        //check color here and press button
        //approach_beacon_and_check_colors();
        //encoderDrive(DRIVE_SPEED, -24,-24, 4.0); // S9: Reverse 24 Inches with 4 Sec timeout
        //encoderDrive(TURN_SPEED,   turn_inches, -turn_inches, 4.0);  // S10: Turn Right 12 Inches with 4 Sec timeout
        //encoderDrive(DRIVE_SPEED, 24,24, 4);
        //push that ball

        sleep(500);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            leftMotor.setTargetPosition(newLeftTarget);
            rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftMotor.setPower(Math.abs(speed));
            rightMotor.setPower(Math.abs(speed * right_motor_correction));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftMotor.isBusy() && rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        leftMotor.getCurrentPosition(),
                        rightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftMotor.setPower(0);
            rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            sleep(5);   // optional pause after each move
        }
    }

    private void approach_beacon_and_check_colors()
    {
        //Get close enough to beacon to check color.
        while(ultrasonicSensor.getUltrasonicLevel() > distance_to_check_color)
        {
            leftMotor.setPower(0.3);
            rightMotor.setPower(0.3);
        }

        //Stop all movement.
        leftMotor.setPower(0);rightMotor.setPower(0);
        sleep(50);
        //Check color of beacon.
        //while(num_checks <= necessary_checks)
        //{

        color_check(hsvValues, values);
        hue = hsvValues[0];
        if(hue > 0 && hue <60){buttonServo.setPosition(1);press_button();}
        else if(hue > 300 && hue < 360){buttonServo.setPosition(1);press_button();}
        else{approach_beacon_and_check_colors();}

        //else{//DO _________ beacon press}
            // Continuously loop over and check for a certain duration (represented by positive verifies)
            //and progress program routine once color is verified to be correct.
        //}



        //if/else for pressing color-corresponding button.

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
            sleep(10);
        }
        return 0;
    }

    //Does encoderDriver work in reverse??? Will -speed = -distance or a
    // non-registered increment of distance traveled
    private void press_button()
    {
        //Could check color after press. If not color match, press again.
        encoderDrive(0.4, 2, 2,3);
        sleep(100);
        encoderDrive(0.4, -2, -2,3);
    }


}

