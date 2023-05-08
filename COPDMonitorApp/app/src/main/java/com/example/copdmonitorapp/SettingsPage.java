package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SettingsPage extends AppCompatActivity {


    // Navigation Drawer Attributes
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, settings, share, about, logout;
    TextView txtViewNavBarName;
    TextView txtViewNavBarEmail;
    String emailShared;
    String nameShared;


    // TextViews
    private TextView txtViewNickname;
    private TextView txtViewEmail;

    private TextView nameLogged;
    private TextView emailLogged;
    private TextView date_birthLogged;
    private TextView heightLogged;
    private TextView weightLogged;
    private TextView copd_sevLoggedTitle;
    private TextView copd_sevLoggedValue;


    private static String TAG = "Setting Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        // Retrieve user's login credentials
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        emailShared = sharedPreferences.getString("email", "");
        nameShared = sharedPreferences.getString("name", "");

        // TextViews's
        txtViewNickname = findViewById(R.id.nickname);
        txtViewEmail = findViewById(R.id.email);
        nameLogged = findViewById(R.id.txtViewUsernameLogged);
        emailLogged = findViewById(R.id.txtViewEmailLogged);
        date_birthLogged = findViewById(R.id.txtViewDatelogged);
        heightLogged = findViewById(R.id.txtViewHeightUserlogged);
        weightLogged = findViewById(R.id.txtViewWeightUserlogged);
        copd_sevLoggedValue = findViewById(R.id.txtViewCOPDSeverityValue);
        copd_sevLoggedTitle = findViewById(R.id.txtViewCOPDSeverity);


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
                redirectActivity(SettingsPage.this, HomePage.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SettingsPage.this, SharePage.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SettingsPage.this, AboutPage.class);
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

                Intent intent = new Intent(SettingsPage.this, InitialPage.class);
                startActivity(intent);
            }
        });


        new ProfileInformationTask().execute();

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


    public void goToChangeUsername(View view) {
        redirectActivity(SettingsPage.this, SettingsUsernamePage.class);
    }

    public void goToChangeEmail(View view) {
        redirectActivity(SettingsPage.this, SettingsEmailPage.class);
    }

    public void goToChangeDateBirth(View view) {
        redirectActivity(SettingsPage.this, SettingsDateBirthPage.class);
    }

    public void goToChangeHeight(View view) {
        redirectActivity(SettingsPage.this, SettingsHeightPage.class);
    }

    public void goToChangeWeight(View view) {
        redirectActivity(SettingsPage.this, SettingsWeightPage.class);
    }


    public void changeCOPDSeverity(View view) {
        PopupMenu popupMenu = new PopupMenu(SettingsPage.this, copd_sevLoggedTitle);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        // Set a click listener for the menu items
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_1:
                        copd_sevLoggedValue.setText("Mild");
                        new ProfileInformationTaskUpdateCOPDSeverity().execute("Mild");
                        return true;
                    case R.id.menu_2:
                        copd_sevLoggedValue.setText("Moderate");
                        new ProfileInformationTaskUpdateCOPDSeverity().execute("Moderate");
                        return true;
                    case R.id.menu_3:
                        copd_sevLoggedValue.setText("Severe");
                        new ProfileInformationTaskUpdateCOPDSeverity().execute("Severe");
                        return true;
                    case R.id.menu_4:
                        copd_sevLoggedValue.setText("Very Severe");
                        new ProfileInformationTaskUpdateCOPDSeverity().execute("Very Severe");
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Show the popup menu
        popupMenu.show();
    }




    private class ProfileInformationTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;


        String name;
        String email;
        String date;
        String copd_sev;
        String height = "";
        String weight = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SettingsPage.this);
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

                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Patient WHERE email = ?");
                pstmt.setString(1, emailShared);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    name = rs.getString("name");
                    date = rs.getString("date_birth_mmddaaaa");
                    copd_sev = rs.getString("copd_severity");
                    height = rs.getString("heightincm");
                    weight = rs.getString("weightinkg");
                    email = emailShared;
                }

                return false;
            } catch (Exception e) {
                Log.e("MyApp", "Error executing query", e);
                exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            txtViewNickname.setText(name);
            txtViewEmail.setText(email);
            nameLogged.setText(name);
            emailLogged.setText(email);
            date_birthLogged.setText(date);
            if(height == null) {
                heightLogged.setText("Register your height!");
            }
            else {
                heightLogged.setText(height);
            }
            if(weight == null) {
                weightLogged.setText("Register your weight!");
            }
            else {
                weightLogged.setText(weight);
            }
            copd_sevLoggedValue.setText(copd_sev);
        }

    }

    private class ProfileInformationTaskUpdateCOPDSeverity extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        String new_copdsev;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SettingsPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            new_copdsev = params[0];
            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";


            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {

                PreparedStatement pstmt = conn.prepareStatement("UPDATE patient SET copd_severity = ? WHERE email = ?");
                pstmt.setString(1, new_copdsev);
                pstmt.setString(2, emailShared);
                pstmt.executeUpdate();
                return true;
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
                Toast.makeText(SettingsPage.this, "Successful change!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Saved:" + emailShared + "/ New copd sev:" + new_copdsev + ".");
            } else {
                Toast.makeText(SettingsPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }
}
