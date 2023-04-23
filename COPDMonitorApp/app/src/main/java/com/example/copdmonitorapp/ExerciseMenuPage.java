package com.example.copdmonitorapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ExerciseMenuPage extends AppCompatActivity {


    // Navigation Drawer Attributes
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, settings, share, about, logout;


    // Slider's
    LinearProgressIndicator progressBar6MWT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercisemenu_page);


        // Navigation Drawer Finders
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        about = findViewById(R.id.about);
        logout = findViewById(R.id.logout);
        settings = findViewById(R.id.settings);
        share = findViewById(R.id.share);


        // Slider's Finder
        progressBar6MWT = findViewById(R.id.progressBar6MWT);



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
                redirectActivity(ExerciseMenuPage.this, HomePage.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(ExerciseMenuPage.this, SettingsPage.class);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(ExerciseMenuPage.this, SharePage.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(ExerciseMenuPage.this, AboutPage.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ExerciseMenuPage.this, "LogOut", Toast.LENGTH_SHORT).show();
            }
        });

        // Slider's Initialize Values
        progressBar6MWT.show();


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

    // URL Method to access COPD Web App
    public void openUrl(View view) {
        TextView urlTextView = (TextView) view;
        String url = urlTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    public void on6MWTClick(View view) {
        redirectActivity(ExerciseMenuPage.this, SixMWTPage.class);
    }

    public void on1MSTSTClick(View view) {
        redirectActivity(ExerciseMenuPage.this, OneMSTSTPage.class);
    }
}