package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProgressBar progressBar;
    private TextView progressText;
    int i = 0;



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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (i <= 100) {
                    progressText.setText("" + i);
                    progressBar.setProgress(i);
                    i++;
                    handler.postDelayed(this, 200);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        }, 200);


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
                redirectActivity(HomePage.this, SharePage.class);
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
        btnMenuExercise = findViewById(R.id.btnExerciseTests);;
        btnMenuChat = findViewById(R.id.btnChatLive);;

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





}