package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        txtViewDateAtual.setText(dateString);

        // Variables TextView's
        etxtViewPaCO2 = findViewById(R.id.eTxtPressaoCO2);
        etxtViewPaO2 = findViewById(R.id.eTxtPaO2);
        etxtViewRespiratoryFreq = findViewById(R.id.eTxtFreqResp);
        etxtViewTemperature= findViewById(R.id.eTxtTemp);



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
                redirectActivity( DailyRecordsPage.this, AboutPage.class);
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
            float PaCO2 = Float.parseFloat(params[0]);
            float PaO2 = Float.parseFloat(params[1]);
            int RespiratoryFreq= Integer.parseInt(params[2]);
            float Temperature = Float.parseFloat(params[3]);


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
                        Log.e(TAG, "Cheguei");
                        String sql = "INSERT INTO dailyrecords (paco2, pao2, respiratory_freq, temperature, timestamp, idpatient) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt2 = conn.prepareStatement(sql)) {
                                // set the parameter values
                                pstmt2.setFloat(1, PaCO2);
                                pstmt2.setFloat(2, PaO2);
                                pstmt2.setInt(3, RespiratoryFreq);
                                pstmt2.setFloat(4, Temperature);
                                pstmt2.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                                pstmt2.setInt(6, patientId);
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
                Log.e(TAG, "" + etxtViewPaCO2.getText().toString().trim() + "etxtViewPaO2.getText().toString().trim()" + "etxtViewRespiratoryFreq.getText().toString().trim()" + "etxtViewTemperature.getText().toString().trim()");
            } else {
                Toast.makeText(DailyRecordsPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }


}