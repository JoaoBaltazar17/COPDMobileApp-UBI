package com.example.stepcounterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float[] accelerationValues = new float[3];
    private ArrayList<Float> normAccelerationValues = new ArrayList<Float>();
    private boolean isRecording = false;
    private Button buttonStart, buttonStop;


    private static final String TAG = "MainActivity";

    private static final int RECORDING_TIME_MS = 30000; // 30 seconds
    private static final int SAMPLING_RATE_US = 20000; // 20ms = 50Hz


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isRecording) {
            accelerationValues[0] = event.values[0];
            accelerationValues[1] = event.values[1];
            accelerationValues[2] = event.values[2];
            float normAcceleration = (float) Math.sqrt(
                    accelerationValues[0]*accelerationValues[0] +
                            accelerationValues[1]*accelerationValues[1] +
                            accelerationValues[2]*accelerationValues[2]
            );
            normAccelerationValues.add(normAcceleration);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonStart:
                startRecording();
                break;
            case R.id.buttonStop:
                stopRecording();
                break;
        }
    }

    private void startRecording() {
        normAccelerationValues.clear();
        isRecording = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                stopRecording();
            }
        }, RECORDING_TIME_MS); // 30 seconds
    }

    private void stopRecording() {
        isRecording = false;
        System.out.println(normAccelerationValues);
        Log.d(TAG, "Acc size: " + normAccelerationValues.size());
        // save the normAccelerationValues array to storage
    }
}
