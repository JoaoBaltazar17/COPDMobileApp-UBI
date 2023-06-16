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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    // SOS TextViewClickable
    TextView txtViewSOSClick;

    // Menu Buttons
    Button btnMenuDailyRecords;
    Button btnMenuMedications;
    Button btnMenuExercise;
    Button btnMenuChat;


    // Navigation Drawer Attributes
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, settings, share, about, logout;

    TextView txtViewNavBarName;
    TextView txtViewNavBarEmail;


    private String pacientLoggedName;
    private String pacientLoggedEmail;

    // Wellness Value BAR
    private MaterialButton btnInfo;
    private ProgressBar progressBar;
    private TextView progressText;
    int i = 0;

    // Wellness Value
    float pont_1MSTST = 0f;
    float pont_6MWT = 0f;
    float pont_CAT = 0f;
    float WellnessValue = 0f;

    // Arrays to save average of each variable
    double pont_Variables = 0f;
    int nosensordata = 0;
    ArrayList<Float> percHours;
    ArrayList<Float> values24PACO2 = new ArrayList<>();
    ArrayList<Float> values24PAO2 = new ArrayList<>();
    ArrayList<Integer> values24RespiratoryRate = new ArrayList<>();
    ArrayList<Float> values24Temperature = new ArrayList<>();

    ArrayList<Float> values18PACO2 = new ArrayList<>();
    ArrayList<Float> values18PAO2 = new ArrayList<>();
    ArrayList<Integer> values18RespiratoryRate = new ArrayList<>();
    ArrayList<Float> values18Temperature = new ArrayList<>();

    ArrayList<Float> values12PACO2 = new ArrayList<>();
    ArrayList<Float> values12PAO2 = new ArrayList<>();
    ArrayList<Integer> values12RespiratoryRate = new ArrayList<>();
    ArrayList<Float> values12Temperature = new ArrayList<>();

    ArrayList<Float> values06PACO2 = new ArrayList<>();
    ArrayList<Float> values06PAO2 = new ArrayList<>();
    ArrayList<Integer> values06RespiratoryRate = new ArrayList<>();
    ArrayList<Float> values06Temperature = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Retrieve user's login credentials
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        pacientLoggedEmail = sharedPreferences.getString("email", "");
        pacientLoggedName = sharedPreferences.getString("name", "");

        // Progress Bar
        // set the id for the progressbar and progress text
        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);

        btnInfo = findViewById(R.id.btnInfoWellness);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setTitle("       COPD Wellness Value");
                TextView textView = new TextView(HomePage.this);
                textView.setText(
                        "\n COPD Wellness Value deducted by:\n" +
                        " - Sensor Shot (Values from Patch Sensor's)\n" +
                        " - How do you feel today?\n" +
                        " - Exercise Tests\n\n");
                textView.setPadding(30, 30, 30, 30);
                textView.setTextSize(16);


                builder.setView(textView);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



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
        txtViewNavBarEmail.setText(pacientLoggedEmail);
        txtViewNavBarName.setText(pacientLoggedName);


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
                recreate();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(HomePage.this, SettingsPage.class);
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
                redirectActivity(HomePage.this, AboutPage.class);
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

                Intent intent = new Intent(HomePage.this, InitialPage.class);
                startActivity(intent);
            }
        });


        // SOS Finder
        txtViewSOSClick = findViewById(R.id.txtViewSOSClickable);


        // Menu Buttons Finder
        btnMenuDailyRecords = findViewById(R.id.btnDailyRecords);
        btnMenuMedications = findViewById(R.id.btnMedication);
        btnMenuExercise = findViewById(R.id.btnExerciseTests);
        btnMenuChat = findViewById(R.id.btnChatLive);


        percHours = new ArrayList<>();
        percHours.add(0.10f);
        percHours.add(0.15f);
        percHours.add(0.20f);
        percHours.add(0.55f);

        // Get Values to Create Wellness Value
        new GetWellnessValue().execute();


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

    public void goToDailyRecordsPage(View view) {
        redirectActivity(HomePage.this, DailyRecordsPage.class);
    }

    public void goToMedicationPage(View view) {
        redirectActivity(HomePage.this, MedicationPage.class);
    }

    public void goSOSPage(View view) {
        redirectActivity(HomePage.this, FellingUnwellPageQ1.class);
    }

    public void goToChatLivePage(View view) {
        redirectActivity(HomePage.this, ChatLivePage.class);
    }

    public void goToExerciseMenuPage(View view) {
        redirectActivity(HomePage.this, ExerciseMenuPage.class);
    }


    private class GetWellnessValue extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(HomePage.this);
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
                Log.e("Wellness BD CONNECTION:", "Connection to BD succesfull!");
                // Check if there is another user with the same username
                String selectQuery = "SELECT id FROM Patient WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                    pstmt.setString(1, pacientLoggedEmail);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            patientId = rs.getInt("id");
                            Log.e("Wellness BD PatientLogged:", "The ID of the patient is: " + patientId);
                        } else {
                            System.out.println("No patient found with that email address.");
                            return false;
                        }

                        // 1MSTST
                        String sql = "SELECT * FROM \"1mstst\" WHERE idpatient = ? ORDER BY date1test DESC LIMIT 1;";
                        PreparedStatement statement1 = conn.prepareStatement(sql);

                        statement1.setInt(1, patientId);

                        ResultSet resultSet1 = statement1.executeQuery();

                        if (resultSet1.next()) {
                            pont_1MSTST = resultSet1.getFloat("testpercentage");
                        } else {
                            return true;
                        }

                        // 6MWT
                        String sql6 = "SELECT * FROM sixmwt WHERE idpatient = ? ORDER BY date6test DESC LIMIT 1;";
                        PreparedStatement statement6 = conn.prepareStatement(sql6);

                        statement6.setInt(1, patientId);

                        ResultSet resultSet6 = statement6.executeQuery();

                        if (resultSet6.next()) {
                            pont_6MWT = resultSet6.getFloat("testpercent");
                        } else {
                            return true;
                        }



                        // CAT
                        String sqlCAT = "SELECT * FROM questionnairesos WHERE idpatient = ? ORDER BY date DESC LIMIT 1;";
                        PreparedStatement statementCAT = conn.prepareStatement(sqlCAT);

                        statementCAT.setInt(1, patientId);

                        ResultSet resultSetCAT = statementCAT.executeQuery();

                        if (resultSetCAT.next()) {
                            int pont_CAT16 = resultSetCAT.getInt("pontuation");
                            int pont_CAT100 = (100 * pont_CAT16) / 16;
                            pont_CAT = 100 - pont_CAT100;
                        } else {
                            return true;
                        }

                        // Variables Sensor Intervals
                        // Select [24h, 18h] Interval Each Sensor
                        for (int i = 1; i <= 4; i++) {
                            String sql24 = "SELECT value FROM sensordetect WHERE idsensor = ? AND idpatient = ? AND timestamp >= NOW() - INTERVAL '24 hours' AND timestamp <= NOW() - INTERVAL '18 hours'";
                            PreparedStatement statement = conn.prepareStatement(sql24);
                            statement.setInt(1, i);
                            statement.setInt(2, patientId);
                            ResultSet resultSet = statement.executeQuery();

                            int rowCount = 0;
                            if (resultSet.next()) {
                                rowCount = 1;
                            }
                            else {
                                rowCount = 0;
                                Log.d("LAST 24-18H", "NO VALUES FROM A VARIABLE: " + i);
                            }

                            if(rowCount > 0) {
                                Log.d("LAST 24-18H", "VALUES DETECTED: " + i);
                                if(i == 3) {

                                    // Respiratory Rate is integer!
                                    ArrayList<Integer> values = new ArrayList<>();
                                    ResultSet nested3ResultSet = statement.executeQuery(); // Create a new result set for each iteration
                                    while (nested3ResultSet.next()) {
                                        int value = nested3ResultSet.getInt("value");
                                        values.add(value);
                                    }
                                    values24RespiratoryRate = values;
                                }
                                else {
                                    ArrayList<Float> values = new ArrayList<>();
                                    ResultSet nestedResultSet = statement.executeQuery(); // Create a new result set for each iteration
                                    while (nestedResultSet.next()) {
                                        float value = nestedResultSet.getFloat("value");
                                        values.add(value);
                                    }
                                    switch (i) {
                                        case 1:
                                            values24PACO2 = values;
                                            break;
                                        case 2:
                                            values24PAO2 = values;
                                            break;
                                        case 4:
                                            values24Temperature = values;
                                            break;
                                    }
                                }
                            }

                            resultSet.close();
                            statement.close();
                        }


                        // Select ]18h, 12h] Interval Each Sensor
                        for (int i = 1; i <= 4; i++) {
                            String sql24 = "SELECT value FROM sensordetect WHERE idsensor = ? AND idpatient = ? AND timestamp >= NOW() - INTERVAL '18 hours' AND timestamp <= NOW() - INTERVAL '12 hours'";
                            PreparedStatement statement = conn.prepareStatement(sql24);
                            statement.setInt(1, i);
                            statement.setInt(2, patientId);
                            ResultSet resultSet = statement.executeQuery();

                            int rowCount = 0;
                            if (resultSet.next()) {
                                rowCount = 1;
                            }
                            else {
                                rowCount = 0;
                                Log.d("LAST 18-12H", "NO VALUES FROM A VARIABLE: " + i);
                            }


                            if(rowCount > 0) {
                                Log.d("LAST 18-12H", "VALUES DETECTED FROM A VARIABLE: " + i);
                                if(i == 3) {
                                    // Respiratory Rate is integer!
                                    // Respiratory Rate is integer!
                                    ArrayList<Integer> values = new ArrayList<>();
                                    ResultSet nested3ResultSet = statement.executeQuery(); // Create a new result set for each iteration
                                    while (nested3ResultSet.next()) {
                                        int value = nested3ResultSet.getInt("value");
                                        values.add(value);
                                    }
                                    values18RespiratoryRate = values;
                                }
                                else {
                                    ArrayList<Float> values = new ArrayList<>();
                                    ResultSet nestedResultSet = statement.executeQuery(); // Create a new result set for each iteration
                                    while (nestedResultSet.next()) {
                                        float value = nestedResultSet.getFloat("value");
                                        values.add(value);
                                    }
                                    switch (i) {
                                        case 1:
                                            values18PACO2 = values;
                                            break;
                                        case 2:
                                            values18PAO2 = values;
                                            break;
                                        case 4:
                                            values18Temperature = values;
                                            break;
                                    }
                                }
                            }


                            resultSet.close();
                            statement.close();
                        }


                        // Select ]12h, 06h] Interval Each Sensor
                        for (int i = 1; i <= 4; i++) {
                            String sql24 = "SELECT value FROM sensordetect WHERE idsensor = ? AND idpatient = ? AND timestamp >= NOW() - INTERVAL '12 hours' AND timestamp <= NOW() - INTERVAL '06 hours'";
                            PreparedStatement statement = conn.prepareStatement(sql24);
                            statement.setInt(1, i);
                            statement.setInt(2, patientId);
                            ResultSet resultSet = statement.executeQuery();

                            int rowCount = 0;
                            if (resultSet.next()) {
                                rowCount = 1;
                            }
                            else {
                                rowCount = 0;
                                Log.d("LAST 12-06H", "NO VALUES FROM A VARIABLE: " + i);
                            }

                            if(rowCount > 0) {
                                Log.d("LAST 12-06H", "VALUES FROM A VARIABLE: " + i);
                                if(i == 3) {
                                    // Respiratory Rate is integer!
                                    ArrayList<Integer> values = new ArrayList<>();
                                    ResultSet nested3ResultSet = statement.executeQuery(); // Create a new result set for each iteration
                                    while (nested3ResultSet.next()) {
                                        int value = nested3ResultSet.getInt("value");
                                        values.add(value);
                                    }
                                    values12RespiratoryRate = values;
                                }
                                else {
                                    ArrayList<Float> values = new ArrayList<>();
                                    ResultSet nestedResultSet = statement.executeQuery(); // Create a new result set for each iteration
                                    while (nestedResultSet.next()) {
                                        float value = nestedResultSet.getFloat("value");
                                        values.add(value);
                                    }
                                    switch (i) {
                                        case 1:
                                            values12PACO2 = values;
                                            break;
                                        case 2:
                                            values12PAO2 = values;
                                            break;
                                        case 4:
                                            values12Temperature = values;
                                            break;
                                    }
                                }
                            }

                            resultSet.close();
                            statement.close();
                        }


                        // Select ]06h, 00h] Interval Each Sensor
                        for (int i = 1; i <= 4; i++) {
                            String sql24 = "SELECT value FROM sensordetect WHERE idsensor = ? AND idpatient = ? AND timestamp >= NOW() - INTERVAL '06 hours' AND timestamp <= NOW() - INTERVAL '00 hours'";
                            PreparedStatement statement = conn.prepareStatement(sql24);
                            statement.setInt(1, i);
                            statement.setInt(2, patientId);
                            ResultSet resultSet = statement.executeQuery();

                            int rowCount = 0;
                            if (resultSet.next()) {
                                rowCount = 1;
                            } else {
                                rowCount = 0;
                                Log.d("LAST 06-00H", "NO VALUES FROM A VARIABLE: " + i);
                            }

                            if (rowCount > 0) {
                                Log.d("LAST 06-00H", "VALUES FROM A VARIABLE: " + i);

                                if (i == 3) {
                                    // Respiratory Rate is integer!
                                    ArrayList<Integer> values = new ArrayList<>();
                                    ResultSet nested3ResultSet = statement.executeQuery(); // Create a new result set for each iteration
                                    while (nested3ResultSet.next()) {
                                        int value = nested3ResultSet.getInt("value");
                                        values.add(value);
                                    }
                                    values06RespiratoryRate = values;
                                } else {
                                    ArrayList<Float> values = new ArrayList<>();
                                    ResultSet nestedResultSet = statement.executeQuery(); // Create a new result set for each iteration
                                    while (nestedResultSet.next()) {
                                        float value = nestedResultSet.getFloat("value");
                                        values.add(value);
                                    }

                                    switch (i) {
                                        case 1:
                                            values06PACO2 = values;
                                            break;
                                        case 2:
                                            values06PAO2 = values;
                                            break;
                                        case 4:
                                            values06Temperature = values;
                                            break;
                                    }
                                    nestedResultSet.close(); // Close the nested result set
                                }
                            }

                            resultSet.close();
                            statement.close();
                        }


                        resultSet1.close();
                        statement1.close();

                        resultSet6.close();
                        statement6.close();

                        resultSetCAT.close();
                        statementCAT.close();
                        return true;
                    }
                }
            } catch (Exception e) {
                Log.e("Wellness Value", "Error executing query", e);
                exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if (result) {
                if (pont_6MWT != 0 &&  pont_CAT != 0 && pont_1MSTST != 0) {
                    if(!values24PACO2.isEmpty() &&  !values18PACO2.isEmpty() && !values12PACO2.isEmpty() && !values06PACO2.isEmpty()
                    && !values24PAO2.isEmpty() && !values18PAO2.isEmpty() && !values12PAO2.isEmpty() && !values06PACO2.isEmpty()
                    && !values24RespiratoryRate.isEmpty() &&  !values18RespiratoryRate.isEmpty() && !values12RespiratoryRate.isEmpty() && !values06RespiratoryRate.isEmpty()
                    && !values24Temperature.isEmpty() && !values18Temperature.isEmpty() && !values12Temperature.isEmpty() && !values06Temperature.isEmpty()) {

                        // PACO2
                        double value24PACO2Avg = calculateAverageFloats(values24PACO2);
                        double perc24PaCO2Avg = evaluatePaCO2(value24PACO2Avg);

                        double value18PACO2Avg = calculateAverageFloats(values18PACO2);
                        double perc18PaCO2Avg = evaluatePaCO2(value18PACO2Avg);

                        double value12PACO2Avg = calculateAverageFloats(values12PACO2);
                        double perc12PaCO2Avg = evaluatePaCO2(value12PACO2Avg);

                        double value06PACO2Avg = calculateAverageFloats(values06PACO2);
                        double perc06PaCO2Avg = evaluatePaCO2(value06PACO2Avg);
                        double percPaCO2 = perc24PaCO2Avg * (0.10) + perc18PaCO2Avg * (0.15)  + perc12PaCO2Avg *  (0.20) + perc06PaCO2Avg * (0.55);


                        // PAO2
                        double value24PaO2Avg = calculateAverageFloats(values24PAO2);
                        double perc24PaO2Avg = evaluatePaO2(value24PaO2Avg);

                        double value18PaO2Avg = calculateAverageFloats(values18PAO2);
                        double perc18PaO2Avg = evaluatePaO2(value18PaO2Avg);

                        double value12PaO2Avg = calculateAverageFloats(values12PAO2);
                        double perc12PaO2Avg = evaluatePaO2(value12PaO2Avg);

                        double value06PaO2Avg = calculateAverageFloats(values06PAO2);
                        double perc06PaO2Avg = evaluatePaO2(value06PaO2Avg);

                        double percPaO2 = perc24PaO2Avg * 0.10 + perc18PaO2Avg * 0.15 + perc12PaO2Avg * 0.20 + perc06PaO2Avg * 0.55;

                        // RR
                        double value24RespiratoryRateAvg = calculateAverageIntegers(values24RespiratoryRate);
                        double perc24RespiratoryRateAvg = evaluateRespiratoryRate(value24RespiratoryRateAvg);

                        double value18RespiratoryRateAvg = calculateAverageIntegers(values18RespiratoryRate);
                        double perc18RespiratoryRateAvg = evaluateRespiratoryRate(value18RespiratoryRateAvg);

                        double value12RespiratoryRateAvg = calculateAverageIntegers(values12RespiratoryRate);
                        double perc12RespiratoryRateAvg = evaluateRespiratoryRate(value12RespiratoryRateAvg);

                        double value06RespiratoryRateAvg = calculateAverageIntegers(values06RespiratoryRate);
                        double perc06RespiratoryRateAvg = evaluateRespiratoryRate(value06RespiratoryRateAvg);

                        double percRespiratoryRate = perc24RespiratoryRateAvg * 0.10 + perc18RespiratoryRateAvg * 0.15 + perc12RespiratoryRateAvg * 0.20 + perc06RespiratoryRateAvg * 0.55;

                        // Temperature
                        double value24TemperatureAvg = calculateAverageFloats(values24Temperature);
                        double perc24TemperatureAvg = evaluateTemperature(value24TemperatureAvg);

                        double value18TemperatureAvg = calculateAverageFloats(values18Temperature);
                        double perc18TemperatureAvg = evaluateTemperature(value18TemperatureAvg);

                        double value12TemperatureAvg = calculateAverageFloats(values12Temperature);
                        double perc12TemperatureAvg = evaluateTemperature(value12TemperatureAvg);

                        double value06TemperatureAvg = calculateAverageFloats(values06Temperature);
                        double perc06TemperatureAvg = evaluateTemperature(value06TemperatureAvg);

                        double percTemperature = perc24TemperatureAvg * 0.10 + perc18TemperatureAvg * 0.15 + perc12TemperatureAvg * 0.20 +
                                perc06TemperatureAvg * 0.55;


                        pont_Variables = (percPaCO2 + percPaO2 + percTemperature + percRespiratoryRate) / 4;

                    }
                    else {
                        Log.e("Home Page Records Sensor", "Values24PACO2: " + values24PACO2 + "Values18PACO2: " + values18PACO2 + "Values12PACO2: " + values12PACO2 + "Values06PACO2: " + values06PACO2);
                        Log.e("Home Page Records Sensor", "Values24PAO2: " + values24PAO2 + " Values18PAO2: " + values18PAO2 + " Values12PAO2: " + values12PAO2 + " Values06PAO2: " + values06PAO2);
                        Log.e("Home Page Records Sensor", "Values24RespiratoryRate: " + values24RespiratoryRate + " Values18RespiratoryRate: " + values18RespiratoryRate + " Values12RespiratoryRate: " + values12RespiratoryRate + " Values06RespiratoryRate: " + values06RespiratoryRate);
                        Log.e("Home Page Records Sensor", "Values24Temperature: " + values24Temperature + " Values18Temperature: " + values18Temperature + " Values12Temperature: " + values12Temperature + " Values06Temperature: " + values06Temperature);
                        nosensordata = 1;
                    }
                    Log.e("Wellness Points", "6MWT: " + pont_6MWT + "\n CAT:" + pont_CAT + "\n 1MSTST:" + pont_1MSTST + "\n VARIABLES:" + pont_Variables);
                    if(nosensordata == 1) {
                        WellnessValue =  ( pont_CAT * (0.5f) + ((pont_1MSTST + pont_6MWT) / 2) * 0.5f);
                        Toast.makeText(HomePage.this, "Not enought data to get a percentage from sensor values!", Toast.LENGTH_LONG).show();

                    }
                    else {
                        WellnessValue =  ((float) (pont_Variables * (0.4f))) + pont_CAT * (0.3f) + (((pont_1MSTST + pont_6MWT) / 2) * 0.3f);
                    }
                    // ProgressBar according to Wellness Value
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // set the limitations for the numeric
                            // text under the progress bar
                            if (i <= WellnessValue) {
                                progressText.setText("" + i + "%");
                                progressBar.setProgress(i);
                                i++;
                                handler.postDelayed(this, 50);
                            } else {
                                handler.removeCallbacks(this);
                            }
                        }
                    }, 10);

                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                    builder.setTitle("Alert")
                            .setMessage("Welcome to COPD App, I assume you're a novice user of the application so the COPD Wellness Value will be shown as soon as there is enough data for the evaluation.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            } else {
                Toast.makeText(HomePage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }

    public double evaluatePaO2(Double PaO2ValueAvg) {
        double perc = 0;
        if (PaO2ValueAvg >= 83 && PaO2ValueAvg <= 108) {
            perc = 100;
        } else if (PaO2ValueAvg >= 65 && PaO2ValueAvg <= 83) {
            perc = 40;
        } else if (PaO2ValueAvg >= 50 && PaO2ValueAvg <= 64) {
            perc = 20;
        } else if (PaO2ValueAvg < 50) {
            perc = 10;
        }
        return perc;
    }

    public double evaluatePaCO2(Double PaCO2ValueAvg) {
        double perc = 0;
        if (PaCO2ValueAvg >= 35 && PaCO2ValueAvg <= 48) {
            perc = 100;
        } else if (PaCO2ValueAvg >= 48 && PaCO2ValueAvg <= 58) {
            perc = 40;
        } else if (PaCO2ValueAvg >= 59 && PaCO2ValueAvg <= 70) {
            perc = 20;
        } else if (PaCO2ValueAvg > 70) {
            perc = 10;
        }
        return perc;
    }

    public double evaluateTemperature(double temperature) {
        double percentage = 0;
        if (temperature < 37) {
            percentage = 100;
        } else if (temperature >= 37 && temperature <= 37.8) {
            percentage = 40;
        } else if (temperature > 37.8 && temperature <= 38.5) {
            percentage = 20;
        } else if (temperature > 38.5) {
            percentage = 10;
        }
        return percentage;
    }

    public double evaluateRespiratoryRate(double respiratoryRate) {
        double percentage = 0;
        if (respiratoryRate < 20) {
            percentage = 100;
        } else if (respiratoryRate >= 20 && respiratoryRate <= 25) {
            percentage = 40;
        } else if (respiratoryRate > 25 && respiratoryRate <= 30) {
            percentage = 20;
        } else if (respiratoryRate > 30) {
            percentage = 10;
        }
        return percentage;
    }




    // Average Calculation
    public Float calculateAverageFloats(List<Float>... listas) {
        List<Float> todosElementos = new ArrayList<>();

        for (List<Float> lista : listas) {
            todosElementos.addAll(lista);
        }

        float soma = 0;
        for (Float elemento : todosElementos) {
            soma += elemento;
        }

        return soma / todosElementos.size();
    }

    // Average Calculation Integer

    public double calculateAverageIntegers(List<Integer>... listas) {
        List<Integer> todosElementos = new ArrayList<>();

        for (List<Integer> lista : listas) {
            todosElementos.addAll(lista);
        }

        int soma = 0;
        for (Integer elemento : todosElementos) {
            soma += elemento;
        }

        return (double) soma / todosElementos.size();
    }




}