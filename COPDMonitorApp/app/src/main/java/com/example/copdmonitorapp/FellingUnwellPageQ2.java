package com.example.copdmonitorapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class FellingUnwellPageQ2 extends AppCompatActivity {

        // Navigation Drawer Attributes
        DrawerLayout drawerLayout;
        ImageView menu;
        LinearLayout home, settings, share, about, logout;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fellingunwell_pageq2);


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
                    redirectActivity( FellingUnwellPageQ2.this, HomePage.class);
                }
            });
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ2.this, SettingsPage.class);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ2.this, SharePage.class);
                }
            });
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ2.this, AboutPage.class);
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

                    Intent intent = new Intent(FellingUnwellPageQ2.this, InitialPage.class);
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

    public void onBackPageClick(View view) {
            redirectActivity(FellingUnwellPageQ2.this, FellingUnwellPageQ1.class);
    }

    public void onConfirmPag2ButtonClick(View view) {
        redirectActivity(FellingUnwellPageQ2.this, FellingUnwellPageQ3.class);
    }
}