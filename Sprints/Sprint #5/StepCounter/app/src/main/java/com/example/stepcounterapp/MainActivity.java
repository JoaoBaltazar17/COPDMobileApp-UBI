package com.example.stepcounterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
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

    private static final int SEQUENCE_SIZE = 10; // Number of sensor events to store for analysis
    private static final int STEP_THRESHOLD = 10;
    private float[] sensorData = new float[SEQUENCE_SIZE * 3]; // Store accelerometer sensor values for X, Y, and Z axes
    private int sensorDataIndex = 0;
    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        stepCountTextView = findViewById(R.id.stepCountTextView);

    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Store the sensor values in the sensorData array
            sensorData[sensorDataIndex++] = x;
            sensorData[sensorDataIndex++] = y;
            sensorData[sensorDataIndex++] = z;

            // Check if enough sensor events have been collected for analysis
            if (sensorDataIndex == SEQUENCE_SIZE * 3) {
                // Analyze the difference between two consecutive sequences to identify a step
                float deltaX = 0;
                float deltaY = 0;
                float deltaZ = 0;
                for (int i = 0; i < SEQUENCE_SIZE - 1; i++) {
                    deltaX += sensorData[i * 3 + 3] - sensorData[i * 3];
                    deltaY += sensorData[i * 3 + 3 + 1] - sensorData[i * 3 + 1];
                    deltaZ += sensorData[i * 3 + 3 + 2] - sensorData[i * 3 + 2];
                }

                if (Math.abs(deltaX) > STEP_THRESHOLD || Math.abs(deltaY) > STEP_THRESHOLD || Math.abs(deltaZ) > STEP_THRESHOLD) {
                    stepCount++;
                    updateStepCount(stepCount);
                }

                // Reset the sensorDataIndex for the next sequence
                sensorDataIndex = 0;
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
