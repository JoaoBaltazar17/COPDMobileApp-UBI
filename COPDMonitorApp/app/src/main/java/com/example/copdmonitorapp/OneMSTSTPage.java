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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class OneMSTSTPage extends AppCompatActivity {


    // SharedPreferences Variables
    private String emailShared;
    private String nameShared;

    // After Test Variables
    private TextView txtViewCount;
    private TextView txtViewPercentage;

    // Timer Variables
    private TextView txtViewTimerText;
    private Button btnStopStart;

    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    private boolean timerStarted = false;

    private static final int TIMER_DURATION = 5; // 6 mins (6 * 60 segundos)



    // Pulsation Values
    int pulsi = 0;
    int pulsf = 0;

    int count = -1;
    float testpercentage = 0;


    // Navigation Drawer Attributes
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, settings, share, about, logout;
    TextView txtViewNavBarName;
    TextView txtViewNavBarEmail;

    // First 1MSTST Test
    private int pastCountOne = -1;
    private int pastPulsiOne = -1;
    private int pastPulsfOne = -1;
    private int firstTry1 = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onemstst_page);

        // Retrieve user's login credentials
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        emailShared = sharedPreferences.getString("email", "");
        nameShared = sharedPreferences.getString("name", "");


        // Timer Finders
        txtViewTimerText = (TextView) findViewById(R.id.txtViewTimerText1MSTST);
        btnStopStart = (Button) findViewById(R.id.btn1MSTSTStartStop);

        timer = new Timer();


        // Navigation Drawer Finders
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        about = findViewById(R.id.about);
        logout = findViewById(R.id.logout);
        settings = findViewById(R.id.settings);
        share = findViewById(R.id.share);
        txtViewNavBarEmail = findViewById(R.id.eTxtNavBarEmail);
        txtViewNavBarName = findViewById(R.id.eTxtNavBarName);
        txtViewNavBarName.setText(nameShared);
        txtViewNavBarEmail.setText(emailShared);


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
                redirectActivity(OneMSTSTPage.this, HomePage.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(OneMSTSTPage.this, SettingsPage.class);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download this COPD App!");
                startActivity(Intent.createChooser(sendIntent, "Choose one"));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(OneMSTSTPage.this, AboutPage.class);
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

                Intent intent = new Intent(OneMSTSTPage.this, InitialPage.class);
                startActivity(intent);
            }
        });

        // After Test Variables
        txtViewCount = findViewById(R.id.txtViewCounter);
        txtViewPercentage = findViewById(R.id.txtViewTestResult);

        txtViewCount.setVisibility(View.INVISIBLE);
        txtViewPercentage.setVisibility(View.INVISIBLE);

        // Get First 1MSTST Test
        new GetFirstONEMSTSTRecords().execute();


    }

    // Navigation Drawer Methods
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }



    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }


    // Timer Methods

    public void onResetClick1MSTST(View view)
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Reset Timer");
        resetAlert.setMessage("Are you sure you want to reset the timer?");
        resetAlert.setPositiveButton("Reset", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(timerTask != null)
                {
                    timerTask.cancel();
                    setButtonUI("START", R.color.green);
                    btnStopStart.setClickable(true);
                    time = 0.0;
                    timerStarted = false;
                    txtViewTimerText.setText(formatTime(0,0,0));
                    pulsi = 0;
                    pulsf = 0;
                    count = -1;
                    txtViewCount.setText("Count: ");
                    txtViewPercentage.setText("Pontuation [0-100]: ");
                    txtViewCount.setVisibility(View.INVISIBLE);
                    txtViewPercentage.setVisibility(View.INVISIBLE);
                }
            }
        });

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //do nothing
            }
        });

        resetAlert.show();

    }

    public void onStartStopClick1MSTST(View view) {
        if (!timerStarted) {
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


                    timerStarted = true;
                    setButtonUI("START", R.color.verdepastel);

                    Log.e("1MSTS", "1 MSTST HAS BEEN STARTED!");
                    startTimer();
                    btnStopStart.setClickable(false);
                }
            });

            inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            inputAlert.show();
        }
        /*
        else
        {
            timerStarted = false;
            setButtonUI("START", R.color.green);

            timerTask.cancel();
        }
        */
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

                        if (time >= TIMER_DURATION) {

                            // Cancel timer
                            timerTask.cancel();
                            Log.e("AFTER TEST 1MSTST", "Concluded!");

                            AlertDialog.Builder inputAlert = new AlertDialog.Builder(OneMSTSTPage.this);
                            inputAlert.setTitle("Enter your heart rate after completing the test\n");
                            final EditText input = new EditText(OneMSTSTPage.this);
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


                                    // Cancel timer count
                                    timerStarted = false;
                                    time = 0.0;
                                    btnStopStart.setClickable(false);
                                    showCycleCountDialog(); // Show the next dialog for cycle count
                                }
                            });

                            inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Cancel timer count
                                    timerStarted = false;
                                    time = 0.0;
                                    pulsf = 1;
                                    setButtonUI("START", R.color.green);
                                }
                            });

                            inputAlert.show();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void showCycleCountDialog() {
        // Dialog to register number of complete cycles of sitting down and standing up
        AlertDialog.Builder inputAlertSign = new AlertDialog.Builder(OneMSTSTPage.this);
        inputAlertSign.setTitle("Number of complete cycles of sitting down and standing up: \n");
        final EditText inputSign = new EditText(OneMSTSTPage.this);
        inputSign.setInputType(InputType.TYPE_CLASS_NUMBER); // Apenas números inteiros
        inputAlertSign.setView(inputSign);

        inputAlertSign.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String countString = inputSign.getText().toString();

                try {
                    count = Integer.parseInt(countString);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "\n" +
                            "Please enter a valid integer.", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Cancel timer count
                timerStarted = false;
                time = 0.0;
                saveValuesTest();
                setButtonUI("START", R.color.green);
            }
        });

        inputAlertSign.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancel timer count
                timerStarted = false;
                time = 0.0;
                setButtonUI("START", R.color.green);
            }
        });

        inputAlertSign.show();
    }


    // Function to Calculate Test Percentage
    public float calculatePercentage(int pulsI, int  pulsF, int count, int past_pulsI, int past_pulsF, int past_count) {
        int mPuls =  (pulsI +  pulsF) / 2;
        int past_mPuls =  (past_pulsI +  past_pulsF) / 2;



        int avalCicles = (count - past_count) * 3;
        int avalPulsacoes = (past_mPuls - mPuls) * 2;

        float perc = 50 + (avalPulsacoes + avalCicles);
        return perc;

    }

    // Save Test Value's into Database
    private void saveValuesTest() {
        btnStopStart.setClickable(true);
        if (pulsi == 0 || pulsf == 0 || count == -1) {
            // Empty fields
            Toast.makeText(OneMSTSTPage.this, "Hey there! You cannot proceed with null values on test", Toast.LENGTH_LONG).show();
            return;
        }
        if(firstTry1 == 1) {
            testpercentage = 50f;
        }
        else {
            testpercentage = calculatePercentage(pulsi, pulsf, count, pastPulsiOne, pastPulsfOne, pastCountOne);
        }
        new Save1MSTSRecords().execute(String.valueOf(pulsi), String.valueOf(pulsf), String.valueOf(count), String.valueOf(testpercentage));

    }


    private class Save1MSTSRecords extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OneMSTSTPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            int pi = Integer.parseInt(params[0]);
            int pf = Integer.parseInt(params[1]);
            int count = Integer.parseInt(params[2]);
            float perc = Float.parseFloat(params[3]);



            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";



            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {
                int patientId = 0;
                Log.e("1MSTS BD CONNECTION:", "Connection to BD succesfull!");
                // Check if there is another user with the same username
                String selectQuery = "SELECT id FROM Patient WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                    pstmt.setString(1, emailShared);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            patientId = rs.getInt("id");
                            Log.e("1MSTS PatientLogged:", "The ID of the patient is: " + patientId);
                        } else {
                            System.out.println("No patient found with that email address.");
                            return false;
                        }
                        String sql = "INSERT INTO \"1mstst\" (idPatient, initialpulsation, finalpulsation, date1test, countcycles, testpercentage) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt2 = conn.prepareStatement(sql)) {
                            // set the parameter values
                            pstmt2.setInt(1, patientId);
                            pstmt2.setInt(2, pi);
                            pstmt2.setInt(3, pf);
                            pstmt2.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                            pstmt2.setInt(5, count);
                            pstmt2.setFloat(6, perc);
                            pstmt2.executeUpdate();
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                Log.e("1MSTS", "Error executing query", e);
                exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if (result) {
                Toast.makeText(OneMSTSTPage.this, "Test has been sucessfully registered", Toast.LENGTH_LONG).show();
                txtViewCount.setVisibility(View.VISIBLE);
                txtViewPercentage.setVisibility(View.VISIBLE);
                txtViewCount.setText("Count: " + count);
                txtViewPercentage.setText("Test Percentage [0-100]: " + testpercentage);

            } else {
                Toast.makeText(OneMSTSTPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
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
        redirectActivity(OneMSTSTPage.this, ExerciseMenuPage.class);
    }

    // First 6MWT of the Patient logged
    private class GetFirstONEMSTSTRecords extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OneMSTSTPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {

            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";



            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {
                int patientId = 0;
                Log.e("1MSTS BD CONNECTION:", "Connection to BD succesfull!");
                // Check if there is another user with the same username
                String selectQuery = "SELECT id FROM Patient WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                    pstmt.setString(1, emailShared);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            patientId = rs.getInt("id");
                            Log.e("1MSTS PatientLogged:", "The ID of the patient is: " + patientId);
                        } else {
                            System.out.println("No patient found with that email address.");
                            return false;
                        }
                        String sql = "SELECT * FROM \"1mstst\" WHERE idpatient = ? ORDER BY date1test ASC LIMIT 1;";
                        PreparedStatement statement = conn.prepareStatement(sql);

                        statement.setInt(1, patientId);

                        ResultSet resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                            // Retrieve the values from the result set
                            int id = resultSet.getInt("id");
                            int idPatient = resultSet.getInt("idpatient");
                            pastPulsiOne = resultSet.getInt("initialpulsation");
                            pastPulsfOne  = resultSet.getInt("finalpulsation");
                            Timestamp date6test = resultSet.getTimestamp("date1test");
                            pastCountOne = resultSet.getInt("countcycles");

                        } else {
                            return true;
                        }
                        resultSet.close();
                        statement.close();
                        return true;
                    }
                }
            } catch (Exception e) {
                Log.e("1MSTS", "Error executing query", e);
                exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if (result) {
                if (pastCountOne != -1 &&  pastPulsiOne != -1 && pastPulsfOne != -1) {
                    Log.e("1MSTST FIRST TEST VALUES", "Past Count: " + pastCountOne + "\n Past PulsI:" + pastPulsiOne + "\n Past PulsF:" + pastPulsfOne);
                }
                else {
                    firstTry1 = 1;
                    AlertDialog.Builder builder = new AlertDialog.Builder(OneMSTSTPage.this);
                    builder.setTitle("Alert")
                            .setMessage("You are about to take your first 1MSTST Test. This test will be used for CALIBRATION (perform it under your normal conditions).")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            } else {
                Toast.makeText(OneMSTSTPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }
}