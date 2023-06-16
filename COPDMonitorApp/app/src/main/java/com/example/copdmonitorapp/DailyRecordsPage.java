package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyRecordsPage extends AppCompatActivity {

    TextView txtViewDateAtual;

    // Navigation Drawer Attributes
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, settings, share, about, logout;
    TextView txtViewNavBarName;
    TextView txtViewNavBarEmail;
    String emailShared;
    String nameShared;


    // Record TextView's
    TextInputEditText etxtViewPaCO2;
    TextInputEditText etxtViewPaO2;
    TextInputEditText etxtViewRespiratoryFreq;
    TextInputEditText etxtViewTemperature;


    // Average Record TextView's
    TextView etxtViewAveragePaCO2;
    TextView etxtViewAveragePaO2;
    TextView etxtViewAverageRespiratoryFreq;
    TextView etxtViewAverageTemperature;


    // Arrays to save average of each variable

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


    private static String TAG = "Daily Records Activity";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailyrecords_page);

        // Retrieve user's login credentials
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        emailShared = sharedPreferences.getString("email", "");
        nameShared = sharedPreferences.getString("name", "");


        // Actual Date
        txtViewDateAtual = findViewById(R.id.txtViewCurrentDate);
        // Create a Date object representing the current date and time
        Date date = new Date();
        // Create a SimpleDateFormat object to format the date as desired
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy HH:mm:ss");
        // Format the date as a String
        String dateString = dateFormat.format(date);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        txtViewDateAtual.setText(ts.toString());

        // Variables TextView's
        etxtViewPaCO2 = findViewById(R.id.eTxtPressaoCO2);
        etxtViewPaO2 = findViewById(R.id.eTxtPaO2);
        etxtViewRespiratoryFreq = findViewById(R.id.eTxtFreqResp);
        etxtViewTemperature= findViewById(R.id.eTxtTemp);

        // AverageVariables TextView's
        etxtViewAveragePaCO2 = findViewById(R.id.etxtViewAveragePaCO2);
        etxtViewAveragePaO2 = findViewById(R.id.etxtViewAveragePaO2);
        etxtViewAverageRespiratoryFreq = findViewById(R.id.etxtViewAverageRespiratoryFreq);
        etxtViewAverageTemperature= findViewById(R.id.etxtViewAverageTemperature);



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
                redirectActivity( DailyRecordsPage.this, HomePage.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( DailyRecordsPage.this, SettingsPage.class);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( DailyRecordsPage.this, SharePage.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download this COPD App!");
                startActivity(Intent.createChooser(sendIntent, "Choose one"));
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

                Intent intent = new Intent(DailyRecordsPage.this, InitialPage.class);
                startActivity(intent);
            }
        });



        new GetLast24ValuesParameter().execute();
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


    // Save Variable Value's into Database
    public void onSaveClicked(View view) {
        if (etxtViewPaCO2.getText().toString().isEmpty() || etxtViewPaO2.getText().toString().isEmpty() || etxtViewRespiratoryFreq.getText().toString().isEmpty() || etxtViewTemperature.getText().toString().isEmpty()) {
            // Empty fields
            Toast.makeText(DailyRecordsPage.this, "Hey there! It seems like you left some fields empty!", Toast.LENGTH_LONG).show();
            return;
        }
        new SaveDailyRecords().execute(etxtViewPaCO2.getText().toString().trim(), etxtViewPaO2.getText().toString().trim(), etxtViewRespiratoryFreq.getText().toString().trim(), etxtViewTemperature.getText().toString().trim());
    }


    private class SaveDailyRecords extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        float paco2;
        float pao2;
        int rr;
        float temp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DailyRecordsPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            float v1 = Float.parseFloat(params[0]);
            float v2 = Float.parseFloat(params[1]);
            int v3 = Integer.parseInt(params[2]);
            float v4 = Float.parseFloat(params[3]);

            paco2 = v1;
            pao2 = v2;
            rr = v3;
            temp = v4;


            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";

            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {
                int patientId = 0;
                Log.e(TAG, "Connection to BD succesfull!");
                // Check if there is another user with the same username
                String selectQuery = "SELECT id FROM Patient WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                    pstmt.setString(1, emailShared);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            patientId = rs.getInt("id");
                            Log.e(TAG, "The ID of the patient is: " + patientId);
                        } else {
                            System.out.println("No patient found with that email address.");
                            return false;
                        }
                        String sql = "INSERT INTO sensordetect (timestamp, value, idsensor, idpatient) VALUES (?, ?, ?, ?), (?, ?, ?, ?), (?, ?, ?, ?), (?, ?, ?, ?)";

                        try (PreparedStatement pstmt2 = conn.prepareStatement(sql)) {
                            Timestamp ts = new Timestamp(System.currentTimeMillis());

                            // Set values for the first row
                            pstmt2.setTimestamp(1, ts);
                            pstmt2.setDouble(2, v1);
                            pstmt2.setInt(3, 1);
                            pstmt2.setInt(4, patientId);

                            // Set values for the second row
                            pstmt2.setTimestamp(5, ts);
                            pstmt2.setDouble(6, v2);
                            pstmt2.setInt(7, 2);
                            pstmt2.setInt(8, patientId);

                            // Set values for the thirst row
                            pstmt2.setTimestamp(9, ts);
                            pstmt2.setInt(10, v3);
                            pstmt2.setInt(11, 3);
                            pstmt2.setInt(12, patientId);

                            // Set values for the first row
                            pstmt2.setTimestamp(13, ts);
                            pstmt2.setDouble(14, v4);
                            pstmt2.setInt(15, 4);
                            pstmt2.setInt(16, patientId);

                            // Execute the query
                            pstmt2.executeUpdate();
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                Log.e("MyApp", "Error executing query", e);
                exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if (result) {
                Toast.makeText(DailyRecordsPage.this, "Recorded Sucessfully", Toast.LENGTH_LONG).show();
                Log.e(TAG,  paco2 + "/" + pao2 + "/" + rr + "/" + temp);
                etxtViewPaCO2.setText("");
                etxtViewPaO2.setText("");
                etxtViewRespiratoryFreq.setText("");
                etxtViewTemperature.setText("");
                new GetLast24ValuesParameter().execute();
            } else {
                Toast.makeText(DailyRecordsPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }



    // Get Last 24 Values of each Parameter
    private class GetLast24ValuesParameter extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DailyRecordsPage.this);
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
                Log.e("DailyRecords BD CONNECTION:", "Connection to BD succesfull!");
                // Check if there is another user with the same username
                String selectQuery = "SELECT id FROM Patient WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                    pstmt.setString(1, emailShared);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            patientId = rs.getInt("id");
                            Log.e("DailyRecords BD PatientLogged:", "The ID of the patient is: " + patientId);
                        } else {
                            System.out.println("No patient found with that email address.");
                            return false;
                        }

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
                                Log.e("LAST 24-18H", "NO VALUES FROM A VARIABLE: " + i);
                            }

                            if(rowCount > 0) {
                                Log.e("LAST 24-18H", "VALUES DETECTED: " + i);
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
                                Log.e("LAST 18-12H", "NO VALUES FROM A VARIABLE: " + i);
                            }


                            if(rowCount > 0) {
                                Log.e("LAST 18-12H", "VALUES DETECTED FROM A VARIABLE: " + i);
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
                                Log.e("LAST 12-06H", "NO VALUES FROM A VARIABLE: " + i);
                            }

                            if(rowCount > 0) {
                                Log.e("LAST 12-06H", "VALUES FROM A VARIABLE: " + i);
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
                                Log.e("LAST 06-00H", "NO VALUES FROM A VARIABLE: " + i);
                            }

                            if (rowCount > 0) {
                                Log.e("LAST 06-00H", "VALUES FROM A VARIABLE: " + i);

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


                        return true;
                    }
                }
            } catch (Exception e) {
                Log.e("Daily Records Values", "Error executing query", e);
                exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if (result) {
                if (!values24PACO2.isEmpty() && !values18PACO2.isEmpty() && !values12PACO2.isEmpty() && !values06PACO2.isEmpty()){
                    double valuePACO2 = calculateAverageFloats(values24PACO2, values18PACO2, values12PACO2, values06PACO2);
                    Log.e("Daily Records Sensor", "Values24PACO2: " + values24PACO2 + "Values18PACO2: " + values18PACO2 + "Values12PACO2: " + values12PACO2 + "Values06PACO2: " + values06PACO2 + "\nAverage: " + valuePACO2);
                    etxtViewAveragePaCO2.setText(String.valueOf(valuePACO2));
                }
                else {
                    etxtViewAveragePaCO2.setText("Insufficient recent PaCO2 sensor data!");
                    Log.e("Daily Records Sensor", "NO Values24PACO2: " + values24PACO2 + "Values18PACO2: " + values18PACO2 + "Values12PACO2: " + values12PACO2 + "Values06PACO2: " + values06PACO2);
                }

                if (!values24PAO2.isEmpty() && !values18PAO2.isEmpty() && !values12PAO2.isEmpty() && !values06PAO2.isEmpty()){
                    double valuePAO2 = calculateAverageFloats(values24PAO2, values18PAO2, values12PAO2, values06PAO2);
                    Log.e("Daily Records Sensor", "Values24PAO2: " + values24PAO2 + " Values18PAO2: " + values18PAO2 + " Values12PAO2: " + values12PAO2 + " Values06PAO2: " + values06PAO2 + "\nAverage: " + valuePAO2);
                    etxtViewAveragePaO2.setText(String.valueOf(valuePAO2));
                }
                else {
                    Log.e("Daily Records Sensor", "NO Values24PAO2: " + values24PAO2 + "Values18PAO2: " + values18PAO2 + "Values12PAO2: " + values12PAO2 + "Values06PAO2: " + values06PAO2);
                    etxtViewAveragePaO2.setText("Insufficient recent PaO2 sensor data");
                }

                if (!values24RespiratoryRate.isEmpty() && !values18RespiratoryRate.isEmpty() && !values12RespiratoryRate.isEmpty() && !values06RespiratoryRate.isEmpty()) {
                    double valueRespiratoryRate = calculateAverageIntegers(values24RespiratoryRate, values18RespiratoryRate, values12RespiratoryRate, values06RespiratoryRate);
                    Log.e("Daily Records Sensor", "Values24RespiratoryRate: " + values24RespiratoryRate + " Values18RespiratoryRate: " + values18RespiratoryRate + " Values12RespiratoryRate: " + values12RespiratoryRate + " Values06RespiratoryRate: " + values06RespiratoryRate + "\nAverage: " + valueRespiratoryRate);
                    etxtViewAverageRespiratoryFreq.setText(String.valueOf(valueRespiratoryRate));
                } else {
                    Log.e("Daily Records Sensor", "NO Values24RespiratoryRate: " + values24RespiratoryRate + "Values18RespiratoryRate: " + values18RespiratoryRate + "Values12RespiratoryRate: " + values12RespiratoryRate + "Values06RespiratoryRate: " + values06RespiratoryRate);
                    etxtViewAverageRespiratoryFreq.setText("Insufficient recent Respiratory Frequency sensor data");

                }

                if (!values24Temperature.isEmpty() && !values18Temperature.isEmpty() && !values12Temperature.isEmpty() && !values06Temperature.isEmpty()) {
                    double valueTemperature = calculateAverageFloats(values24Temperature, values18Temperature, values12Temperature, values06Temperature);
                    Log.e("Daily Records Sensor", "Values24Temperature: " + values24Temperature + " Values18Temperature: " + values18Temperature + " Values12Temperature: " + values12Temperature + " Values06Temperature: " + values06Temperature + "\nAverage: " + valueTemperature);
                    etxtViewAverageTemperature.setText(String.valueOf(valueTemperature));
                } else {
                    Log.e("Daily Records Sensor", "NO Values24Temperature: " + values24Temperature + "Values18Temperature: " + values18Temperature + "Values12Temperature: " + values12Temperature + "Values06Temperature: " + values06Temperature);
                    etxtViewAverageTemperature.setText("Insufficient recent Temperature sensor data");

                }


            } else {
                Toast.makeText(DailyRecordsPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

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