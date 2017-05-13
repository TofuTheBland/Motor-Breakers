package org.firstinspires.ftc.robotcontroller.external.samples;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by T420s on 11/3/2016.
 */

@TeleOp(name= "AutonomousExample", group="Linear Opmode")  // @Autonomous(...) is the other common choice
@Disabled

public class AutonomousExample extends OpMode {

    DcMotor leftMotor = null;
    DcMotor rightMotor = null;
    ColorSensor colorSensor;
    Servo buttonServo = null;

    int mode = 0;
    //Main initialization here. Code that runs once.

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // get a reference to the RelativeLayout so we can change the background
    // color of the Robot Controller app to match the hue detected by the RGB sensor.
    View relativeLayout;

    // bPrevState and bCurrState represent the previous and current state of the button.
    boolean bPrevState = false;
    boolean bCurrState = false;

    // bLedOn represents the state of the LED.
    boolean bLedOn = true;


    static final double INCREMENT = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int CYCLE_MS = 50;     // period of each cycle
    static final double MAX_POS = 1.0;     // Maximum rotational position
    static final double MIN_POS = 0.0;     // Minimum rotational position

    // Define class members
    Servo servo;
    double position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
    boolean rampUp = true;

    @Override
    public void init() {

        leftMotor = hardwareMap.dcMotor.get("leftmotor");
        rightMotor = hardwareMap.dcMotor.get("rightmotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        //relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);
        //colorSensor = hardwareMap.colorSensor.get("colorsensor");
        //colorSensor.enableLed(bLedOn);
       // buttonServo = hardwareMap.servo.get("buttonservo");


    }


    //Main Code to repeat here.
    @Override
    public void loop() {
        //leftMotor.setPower(1);
        //rightMotor.setPower(1);

        //Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

        // send the info back to driver station using telemetry function.
        //telemetry.addData("LED", bLedOn ? "On" : "Off");
        //telemetry.addData("Clear", colorSensor.alpha());
        //telemetry.addData("Red  ", colorSensor.red());
        //telemetry.addData("Green", colorSensor.green());
        //telemetry.addData("Blue ", colorSensor.blue());
        //telemetry.addData("Hue", hsvValues[0]);

        // change the background color to match the color detected by the RGB sensor.
        // pass a reference to the hue, saturation, and value array as an argument
        // to the HSVToColor method.
        /*relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
        });*/
        telemetry.update();

        /*if (rampUp) {
            // Keep stepping up until we hit the max value.
            position += INCREMENT;
            if (position >= MAX_POS) {
                position = MAX_POS;
                rampUp = !rampUp;   // Switch ramp direction
            }
        } else {
            // Keep stepping down until we hit the min value.
            position -= INCREMENT;
            if (position <= MIN_POS) {
                position = MIN_POS;
                rampUp = !rampUp;  // Switch ramp direction
            }

            servo.setPosition(position);
        }*/

    }
}
