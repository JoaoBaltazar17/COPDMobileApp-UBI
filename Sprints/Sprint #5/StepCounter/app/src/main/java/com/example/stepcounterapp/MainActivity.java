package com.example.stepcounterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private TextView stepCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        stepCountTextView = findViewById(R.id.stepCountTextView);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        private static final int STEP_THRESHOLD = 10;
        private float previousY = 0;
        private int stepCount = 0;
        @Override
        public void onSensorChanged(SensorEvent event) {
            float y = event.values[1];
            float deltaY = y - previousY;
            previousY = y;
            if (Math.abs(deltaY) > STEP_THRESHOLD) {
                stepCount++;
                updateStepCount(stepCount);
            }
        }

        private void updateStepCount(int stepCount) {
            stepCountTextView.setText("Step count: " + stepCount);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Code to handle accuracy changes
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

}