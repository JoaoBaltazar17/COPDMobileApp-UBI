package com.example.stepcounterapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager oSM;
    private Sensor oL;

    private boolean bMessage = false;

    private float previousXX = 0;
    private float previousYY = 0;
    private float previousZZ = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

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
    public void onSensorChanged(SensorEvent sensorEvent) {


        float aXX = sensorEvent.values[0];
        float aYY = sensorEvent.values[1];
        float aZZ = sensorEvent.values[2];

        float deltaXX = aXX - previousXX;
        previousXX = aXX;

        float deltaYY = aYY - previousYY;
        previousYY = aYY;

        float deltaZZ = aZZ - previousZZ;
        previousZZ = aZZ;

        if(deltaXX != 0 | deltaYY != 0 | deltaZZ != 0) {
            if(!bMessage) {
                System.out.println("X: " + aXX + "\n" + "Y: " + aYY + "\n" + "Z: " + aZZ);
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
