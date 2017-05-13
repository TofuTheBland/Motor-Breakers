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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name= "DriverControlled", group="Linear Opmode")  // @Autonomous(...) is the other common choice
@Disabled
public class DriverControlled extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor leftMotor = null;
    DcMotor rightMotor = null;
    DcMotor elevationMotor = null;
    DcMotor pitchMotor = null;
    DcMotor clawMotor = null;
    //DcMotor launcherMotor = null;
    double idle_power;
    double claw_power;
    //double pitch_power_mode = 0;

    Servo buttonServo = null;

    float min_position = 0;
    float max_position = 1;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();


        leftMotor = hardwareMap.dcMotor.get("leftmotor");
        rightMotor = hardwareMap.dcMotor.get("rightmotor");
        //leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        elevationMotor = hardwareMap.dcMotor.get("elevationmotor");
        pitchMotor = hardwareMap.dcMotor.get("pitchmotor");
        clawMotor = hardwareMap.dcMotor.get("clawmotor");
        //launcherMotor = hardwareMap.dcMotor.get("launchermotor");

        buttonServo = hardwareMap.servo.get("buttonservo");
        buttonServo.setPosition(min_position);

        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        idle_power = 0;
        claw_power = 0;
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            //Get analog values from left joystick.
            float xValue = -gamepad1.left_stick_y;
            float yValue = gamepad1.left_stick_x;


            if (gamepad1.right_bumper) {
                elevationMotor.setPower(1);
            } else if (gamepad1.left_bumper) {
                elevationMotor.setPower(-1);
            } else {
                elevationMotor.setPower(0);
            }




            if (gamepad1.right_stick_y > 0.1 || gamepad1.right_stick_y < -0.1) {pitchMotor.setPower((gamepad1.right_stick_y));}
            else{pitchMotor.setPower(0);}

            if (gamepad1.x && (claw_power != -0.2)) {claw_power = -0.2;}
            else if(gamepad1.x && (claw_power == -0.2)){claw_power = 0;}
            //else if(gamepad1.x && (claw_power == -0.4)){claw_power=0;}

            if (gamepad1.b && (claw_power == 0 || claw_power == -0.2)){claw_power = 1;}
            else if(gamepad1.b && (claw_power == 1)){claw_power=0;}

            clawMotor.setPower(claw_power);

            //if(gamepad1.x && (claw_power != 0.0)){claw_power = 0.0;}
            //if(gamepad1.b && (claw_power != 0.0)){claw_power = 0.0;}
            //{pitchMotor.setPower(0);}
            //Mode for claw.
            //if (gamepad1.right_stick_x > 0.1 || gamepad1.right_stick_x < -0.1) {clawMotor.setPower((gamepad1.right_stick_x) * 0.45);}
            //else{clawMotor.setPower(idle_claw_power);}
            //else {clawMotor.setPower(idle_claw_power);}


            /*if(gamepad1.dpad_left){clawMotor.setPower(-0.5);}
            if(gamepad1.dpad_right){clawMotor.setPower(0.5);}
            else{clawMotor.setPower(0);}*/
/*
            if(gamepad1.right_trigger > 0.2 && gamepad1.left_trigger < 0.2){launcherMotor.setPower(gamepad1.right_trigger);}
            if(gamepad1.left_trigger > 0.2 && gamepad1.right_trigger< 0.2){launcherMotor.setPower(-gamepad1.left_trigger);}
            else{launcherMotor.setPower(0);}
cherMotor.setPower(0);}
*/
            if (gamepad1.right_trigger > 0.3 && (buttonServo.getPosition() < max_position)){
                buttonServo.setPosition(min_position += 0.01);
            }
            if (gamepad1.left_trigger > 0.3 && (buttonServo.getPosition() > 0)){
                buttonServo.setPosition(min_position -= 0.01);
            }

            //Calculate left and right drive motor levels.
            float leftPower = yValue + xValue;
            float rightPower = yValue - xValue;

            //Limit maximum values.
            leftPower = Range.clip(leftPower, (float) -0.8, (float) 0.8);
            rightPower = Range.clip(rightPower, (float) -0.8,(float) 0.8);

            //Set motor levels.
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);


            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }
}
