package com.example.stepcounterapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager oSM;
    private Sensor oL;

    private static final String TAG = "StepDetector";
    private static final int STEP_THRESHOLD = 10;

    private int stepCount = 0;
    private boolean isStepDetected = false;


    TextView txtVPassos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        txtVPassos = findViewById(R.id.stepCountTextView);
        oSM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        oL = oSM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(oSM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            String sName = oL . getName() ;
            String sVendor = oL . getVendor() ;
            float fP = oL . getPower() ;
            int iVersion = oL.getVersion() ;
            System.out.println( "SENSORACTIVITY" +
                    " Sensor found ! Specs − Name=" + sName +
                            " Vendor=" + sVendor +
                            " Power=" + fP +
                            " Version=" + iVersion);
        }
        else {
            // Failure! No accelerometer sensor.
            System.out.println("Sensor not found");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float acceleration = (float) Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
            if (acceleration > STEP_THRESHOLD && !isStepDetected) {
                isStepDetected = true;
                stepCount++;
            }
            if (acceleration < STEP_THRESHOLD && isStepDetected) {
                isStepDetected = false;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Toast.makeText(this, "Accuracy has changed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        oSM.registerListener(this, oL, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        oSM.unregisterListener(this);
    }
}
