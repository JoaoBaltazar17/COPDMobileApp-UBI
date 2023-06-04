package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SixMWTPage extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "6MWT (Step Counter)";

    // Share Variables
    Button btnShareWhatsApp;

    // Timer Variables
    private TextView txtViewTimerText;
    private Button btnStopStart;

    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    private boolean timerStarted = false;

    private static final int TIMER_DURATION = 30; // 6 mins (6 * 60 segundos)

    // SharedPreferences Variables
    private String emailShared;
    private String nameShared;


    // Navigation Drawer Attributes
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, settings, share, about, logout;

    // Pulsation Values
    int pulsi = 0;
    int pulsf = 0;

    float testpercentage = 0;
    float distance = 0;

    // Test Concluded Variables
    private TextView txtViewSteps;
    private TextView txtViewDistance;
    private TextView txtViewPercentage;

    // Step Counter Variables

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float[] accelerationValues = new float[3];
    private ArrayList<Float> normAccelerationValues = new ArrayList<Float>();
    private ArrayList<Float> accX = new ArrayList<Float>();
    private ArrayList<Float> accY = new ArrayList<Float>();
    private ArrayList<Float> accZ = new ArrayList<Float>();
    private ArrayList<Long> tempo = new ArrayList<Long>();

    private long previousTimeStamp = 0;
    int stepCount = -1;
    Vibrator v;


    private static final int RECORDING_TIME_MS = 30000; // 30 seconds
    private static final int SAMPLING_RATE_US = 20000; // 20ms = 50Hz


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sixmwt_page);

        // Retrieve user's login credentials
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        emailShared = sharedPreferences.getString("email", "");
        nameShared = sharedPreferences.getString("name", "");


        // Timer Finders
        txtViewTimerText = (TextView) findViewById(R.id.txtViewTimerText);
        btnStopStart = (Button) findViewById(R.id.btn6MWTStartStop);

        timer = new Timer();


        // Navigation Drawer Finders
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        about = findViewById(R.id.about);
        logout = findViewById(R.id.logout);
        settings = findViewById(R.id.settings);
        share = findViewById(R.id.share);


        // Menu Navigation and Components Listener's
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SixMWTPage.this, HomePage.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SixMWTPage.this, SettingsPage.class);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SixMWTPage.this, SharePage.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SixMWTPage.this, AboutPage.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();

                Intent intent = new Intent(SixMWTPage.this, InitialPage.class);
                startActivity(intent);
            }
        });

        // After Test Variables
        txtViewSteps = findViewById(R.id.txtViewSteps);
        txtViewDistance = findViewById(R.id.txtViewDistance);
        txtViewPercentage = findViewById(R.id.txtViewTestResult);

        txtViewSteps.setVisibility(View.INVISIBLE);
        txtViewDistance.setVisibility(View.INVISIBLE);
        txtViewPercentage.setVisibility(View.INVISIBLE);


        // Step Counter Variables
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);



    }

    // Navigation Drawer Methods
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
        sensorManager.unregisterListener(this);
    }

    // Step Counter Methods
    public void onSensorChanged(SensorEvent event) {
        if (timerStarted) {
            accelerationValues[0] = event.values[0];
            accX.add(accelerationValues[0]);
            accelerationValues[1] = event.values[1];
            accY.add(accelerationValues[0]);
            accelerationValues[2] = event.values[2];
            accZ.add(accelerationValues[0]);
            float normAcceleration = (float) Math.sqrt(
                    accelerationValues[0] * accelerationValues[0] +
                            accelerationValues[1] * accelerationValues[1] +
                            accelerationValues[2] * accelerationValues[2]
            );
            normAccelerationValues.add(normAcceleration);
            tempo.add(System.currentTimeMillis());


            // Checking the actual sampling rate of the accelerometer sensor
            long currentTimestamp = System.currentTimeMillis();
            if (previousTimeStamp != 0) {
                long timeDifference = currentTimestamp - previousTimeStamp;
            }
            previousTimeStamp = currentTimestamp;
        }
    }


    // Timer Methods

    public void onResetClick(View view) {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Reset Timer");
        resetAlert.setMessage("Are you sure you want to reset the timer?");
        resetAlert.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (timerTask != null) {
                    timerTask.cancel();
                    setButtonUI("START", R.color.green);
                    time = 0.0;
                    timerStarted = false;
                    txtViewTimerText.setText(formatTime(0, 0, 0));
                    accX.clear();
                    accY.clear();
                    accZ.clear();
                    tempo.clear();
                    normAccelerationValues.clear();
                    txtViewSteps.setVisibility(View.INVISIBLE);
                    txtViewDistance.setVisibility(View.INVISIBLE);
                    txtViewPercentage.setVisibility(View.INVISIBLE);
                    txtViewSteps.setText("Steps: ");
                    txtViewDistance.setText("Distance: ");
                    txtViewPercentage.setText("Pontuation [0-100]: ");
                    btnStopStart.setClickable(true);
                }
            }
        });

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });

        resetAlert.show();

    }

    public void onStartStopClick(View view) {
        if (!timerStarted) {
            // Clear possible past test results
            accX.clear();
            accY.clear();
            accZ.clear();
            tempo.clear();
            normAccelerationValues.clear();

            AlertDialog.Builder inputAlert = new AlertDialog.Builder(this);
            inputAlert.setTitle("Enter your heart rate before the test starts\n");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER); // Apenas números inteiros
            inputAlert.setView(input);

            inputAlert.setPositiveButton("Start Test", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String pulseString = input.getText().toString();
                    int pulse = 0;

                    try {
                        pulse = Integer.parseInt(pulseString);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "\n" +
                                "Please enter a valid integer.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (pulse < 40 || pulse > 140) {
                        Toast.makeText(getApplicationContext(), "Please enter an integer between 40 and 140.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    pulsi = pulse;


                    setButtonUI("START", R.color.verdepastel);

                    Log.e("6MSTS", "1 MSTST HAS BEEN STARTED!");
                    startTimer();
                    btnStopStart.setClickable(false);
                }
            });

            inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            inputAlert.setCancelable(false);
            inputAlert.show();
        }
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        txtViewTimerText.setText(getTimerText());
                        if (time == 5 && timerStarted == false) {
                            // Vibrate the device
                            time = 0.0;
                            timerStarted = true;
                            txtViewTimerText.setText(getTimerText());
                            Log.e("AFTER TEST 6MSTST", "Starts COUTING!");
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            if (vibrator != null && vibrator.hasVibrator()) {
                                long[] pattern = {0, 1000, 500, 1000}; // Vibration pattern: wait for 0ms, vibrate for 1000ms, wait for 500ms, vibrate for 1000ms
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    VibrationEffect effect = VibrationEffect.createWaveform(pattern, -1); // -1 means to repeat indefinitely
                                    vibrator.vibrate(effect);
                                } else {
                                    vibrator.vibrate(pattern, -1);
                                }
                            }

                        }
                        if (time >= TIMER_DURATION) {

                            // Cancel timer
                            timerTask.cancel();
                            timerStarted = false;
                            Log.e("AFTER TEST 6MSTST", "Concluded!");

                            // Vibrate the device
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            if (vibrator != null && vibrator.hasVibrator()) {
                                long[] pattern = {0, 1000, 500, 1000}; // Vibration pattern: wait for 0ms, vibrate for 1000ms, wait for 500ms, vibrate for 1000ms
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    VibrationEffect effect = VibrationEffect.createWaveform(pattern, -1); // -1 means to repeat indefinitely
                                    vibrator.vibrate(effect);
                                } else {
                                    vibrator.vibrate(pattern, -1);
                                }
                            }

                            // Show the "Test Finished" dialog
                            AlertDialog.Builder testFinishedDialogBuilder = new AlertDialog.Builder(SixMWTPage.this);
                            testFinishedDialogBuilder.setTitle("Your test has finished");
                            testFinishedDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Stop the vibration
                                    if (vibrator != null) {
                                        vibrator.cancel();
                                        AlertDialog.Builder inputAlert = new AlertDialog.Builder(SixMWTPage.this);
                                        inputAlert.setTitle("Enter your heart rate after completing the test\n");
                                        final EditText input = new EditText(SixMWTPage.this);
                                        input.setInputType(InputType.TYPE_CLASS_NUMBER); // Apenas números inteiros
                                        inputAlert.setView(input);

                                        inputAlert.setPositiveButton("Conclude Test", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String pulseString = input.getText().toString();
                                                int pulse = 0;

                                                try {
                                                    pulse = Integer.parseInt(pulseString);
                                                } catch (NumberFormatException e) {
                                                    Toast.makeText(getApplicationContext(), "\n" +
                                                            "Please enter a valid integer.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                if (pulse < 40 || pulse > 140) {
                                                    Toast.makeText(getApplicationContext(), "Please enter an integer between 40 and 140.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                pulsf = pulse;


                                                time = 0.0;
                                                setButtonUI("START", R.color.green);
                                                writeStepVariablesCSV(); // Show the next dialog for cycle count
                                            }
                                        });

                                        inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // Cancel timer count
                                                timerStarted = false;
                                                time = 0.0;
                                                setButtonUI("START", R.color.green);
                                                writeStepVariablesCSV(); // Show the next dialog for cycle count
                                                pulsf = 1;
                                            }
                                        });
                                        inputAlert.setCancelable(false); // Prevent dismissing the dialog by clicking outside or pressing the back button
                                        inputAlert.show();
                                    }
                                }
                            });

                            testFinishedDialogBuilder.setCancelable(false); // Prevent dismissing the dialog by clicking outside or pressing the back button
                            testFinishedDialogBuilder.show();

                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }


    private void writeStepVariablesCSV() {
        Toast.makeText(this, "Test has been finished!", Toast.LENGTH_LONG).show();
        try {
            // Get the internal storage directory
            File directory = getFilesDir();
            // Create a new file in the directory
            File file = new File(directory, "dados_aceleracao.csv");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            // Write the data to the file
            for (int i = 0; i < normAccelerationValues.size(); i++) {
                bw.write(tempo.get(i) + "," + accX.get(i) + "," + accY.get(i) + "," + accZ.get(i) + "," + normAccelerationValues.get(i) + "\n");
            }
            bw.close();
            Log.e(TAG, "File saved to: " + file.getAbsolutePath());
            saveValuesTest();
        } catch (IOException e) {
            Log.e(TAG, "Error writing to file: " + e.getMessage());
        }
    }

    private int countSteps() {

        double threshold = 1.0;
        Log.e(TAG, "Start looking at CSV File...");
        // Get the internal storage directory
        File directory = getFilesDir();
        // Create a file object for the CSV file
        File file = new File(directory, "dados_aceleracao.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            Log.e(TAG, "Starts Counting!!!!");
            String line;
            double previousNormAcceleration = 0;
            int stepCount = 0;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                double normAcceleration = Double.parseDouble(data[4]);

                if (previousNormAcceleration == 0) {
                    previousNormAcceleration = normAcceleration;
                    continue;
                }

                double variation = normAcceleration - previousNormAcceleration;

                if (variation >= threshold) {
                    // Verifica se ocorreu uma variação maior ou igual ao limiar
                    double nextNormAcceleration = 0;

                    // Procura pelo próximo valor de norma de aceleração
                    while ((line = br.readLine()) != null) {
                        data = line.split(",");
                        nextNormAcceleration = Double.parseDouble(data[4]);

                        if (nextNormAcceleration < normAcceleration) {
                            break;
                        }
                    }

                    if (nextNormAcceleration < normAcceleration) {
                        stepCount++;
                    }
                }

                previousNormAcceleration = normAcceleration;
            }

            return stepCount;
        } catch (IOException e) {
            e.toString();
        }
        return stepCount;
    }




        private void saveValuesTest() {
        stepCount = countSteps();
        Log.e(TAG, "Steps Counted: " + stepCount);
        btnStopStart.setClickable(true);
        if (pulsi == 0 || pulsf == 0) {
            // Empty fields
            Toast.makeText(SixMWTPage.this, "Hey there! You cannot proceed with null values on test", Toast.LENGTH_LONG).show();
            return;
        }
        distance = stepCount * 0.762f;
        testpercentage = 20.2f;
        new Save6MWTRecords().execute(String.valueOf(pulsi), String.valueOf(pulsf), String.valueOf(stepCount), String.valueOf(distance), String.valueOf(testpercentage));

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class Save6MWTRecords extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SixMWTPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            int pi = Integer.parseInt(params[0]);
            int pf = Integer.parseInt(params[1]);
            int countSteps = Integer.parseInt(params[2]);
            float dist = Float.parseFloat(params[3]);
            float perc = Float.parseFloat(params[4]);



            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";



            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {
                int patientId = 0;
                Log.e("6MSTS BD CONNECTION:", "Connection to BD succesfull!");
                // Check if there is another user with the same username
                String selectQuery = "SELECT id FROM Patient WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                    pstmt.setString(1, emailShared);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            patientId = rs.getInt("id");
                            Log.e("6MSTS PatientLogged:", "The ID of the patient is: " + patientId);
                        } else {
                            System.out.println("No patient found with that email address.");
                            return false;
                        }
                        String sql = "INSERT INTO sixmwt (idPatient, initialpulsation, finalpulsation, date6test, numbersteps, distance, testpercent) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt2 = conn.prepareStatement(sql)) {
                            // set the parameter values
                            pstmt2.setInt(1, patientId);
                            pstmt2.setInt(2, pi);
                            pstmt2.setInt(3, pf);
                            pstmt2.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                            pstmt2.setInt(5, countSteps);
                            pstmt2.setFloat(6, dist);
                            pstmt2.setFloat(7, perc);
                            pstmt2.executeUpdate();
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                Log.e("6MSTS", "Error executing query", e);
                exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if (result) {
                Toast.makeText(SixMWTPage.this, "Test has been sucessfully registered", Toast.LENGTH_LONG).show();
                txtViewSteps.setVisibility(View.VISIBLE);
                txtViewDistance.setVisibility(View.VISIBLE);
                txtViewPercentage.setVisibility(View.VISIBLE);
                txtViewSteps.setText("Steps: " + stepCount);

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String roundedDistance = decimalFormat.format(distance);
                txtViewDistance.setText("Distance: " + roundedDistance);
                txtViewPercentage.setText("Test Percentage [0-100]: " + testpercentage);

            } else {
                Toast.makeText(SixMWTPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }

    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }

    private void setButtonUI(String start, int color) {
        btnStopStart.setText(start);
        btnStopStart.setTextColor(ContextCompat.getColor(this, color));
    }

    public void goBackToExerciseMenu(View view) {
        redirectActivity(SixMWTPage.this, ExerciseMenuPage.class);
    }
}