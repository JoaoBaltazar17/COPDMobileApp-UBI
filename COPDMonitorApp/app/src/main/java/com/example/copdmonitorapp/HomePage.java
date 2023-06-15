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



    // First 6MWT of the Patient logged
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
                        PreparedStatement statement = conn.prepareStatement(sql);

                        statement.setInt(1, patientId);

                        ResultSet resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                            pont_1MSTST = resultSet.getFloat("testpercentage");
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


                        resultSet.close();
                        statement.close();

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
                    Log.e("Wellness Points", "6MWT: " + pont_6MWT + "\n CAT:" + pont_CAT + "\n 1MSTST:" + pont_1MSTST);
                    WellnessValue = pont_CAT * (0.3f) + ( ((pont_1MSTST + pont_6MWT) / 2) * 0.7f);

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



}