package com.example.copdmonitorapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class FellingUnwellPageQ5 extends AppCompatActivity {

        // Navigation Drawer Attributes
        DrawerLayout drawerLayout;
        ImageView menu;
        LinearLayout home, settings, share, about, logout;


        // Components

        TextView txtViewImageVector;
        TextView txtViewTestResult;
        TextView txtViewInstruction1;
        TextView txtViewInstruction2;
        TextView txtViewInstruction3;
        TextView txtViewInstruction4;
        Button btnShareResultsQuestionnaire;


        // Slide Values
        int slide3_value;
        int slide2_value;
        int slide1_value;
        int slide4_value;

        int sumSlides;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fellingunwell_pageq5);


            // Component Finder's
            txtViewImageVector = findViewById(R.id.txtViewImageVector);
            txtViewTestResult = findViewById(R.id.txtViewtTestResult);;
            txtViewInstruction1 = findViewById(R.id.txtViewInstruction1);
            txtViewInstruction2 = findViewById(R.id.txtViewInstruction2);
            txtViewInstruction3 = findViewById(R.id.textViewInst3);
            txtViewInstruction4= findViewById(R.id.textViewInst4);
            btnShareResultsQuestionnaire = findViewById(R.id.btnShareResultsQuestion);

            // Receiving values of question's
            Intent intent = getIntent();
            slide1_value = intent.getIntExtra("slide_value1", 0);
            slide2_value = intent.getIntExtra("slide_value2", 0);
            slide3_value = intent.getIntExtra("slide_value3", 0);
            slide4_value = intent.getIntExtra("slide_value4", 0);
            Log.e("VALUE SLIDES", "SLIDE 1: " + slide1_value + "\nSLIDE 2: " + slide2_value + "\nSLIDE 3: " + slide3_value + "\nSLIDE 4: " + slide4_value);

            // Display percentage, drawable and instructions depending on information:
            sumSlides = slide1_value + slide2_value + slide3_value + slide4_value;





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
                    redirectActivity( FellingUnwellPageQ5.this, HomePage.class);
                }
            });
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ5.this, SettingsPage.class);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ5.this, SharePage.class);
                }
            });
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ5.this, AboutPage.class);
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

                    Intent intent = new Intent(FellingUnwellPageQ5.this, InitialPage.class);
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


    public void onHomeButtonClick(View view) {
        redirectActivity(FellingUnwellPageQ5.this,  HomePage.class);
    }

    public void sendDetailedReportQuestionnaire(View view) {
    }
}