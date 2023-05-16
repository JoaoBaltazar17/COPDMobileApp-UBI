package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class FellingUnwellPageQ4 extends AppCompatActivity {

    private static String TAG = "Q4 Page";

    // Navigation Drawer Attributes
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, settings, share, about, logout;


    // Components
    Slider slider;
    View rectangle;
    TextView txtViewDescription;
    TextView txtViewInfo1;
    Button btnConfirm;


    // Variables
    int slide3_value;
    int slide2_value;
    int slide1_value;
    int slide_sumValues;

    int slideValue;
    String emailShared;
    String nameShared;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fellingunwell_pageq4);

        // Retrieve user's login credentials
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        emailShared = sharedPreferences.getString("email", "");
        nameShared = sharedPreferences.getString("name", "");


        // Component's Finders
        slider = findViewById(R.id.slideQ);
        rectangle = findViewById(R.id.rectangle);
        txtViewDescription = findViewById(R.id.txtViewDescriptionSlider);
        txtViewInfo1 = findViewById(R.id.txtViewInformation1);
        btnConfirm = findViewById(R.id.btnConfirm);


        // Receiving value of past slider
        Intent intent = getIntent();
        slide1_value = intent.getIntExtra("slide_value1", 0);
        slide2_value = intent.getIntExtra("slide_value2", 0);
        slide3_value = intent.getIntExtra("slide_value3", 0);
        Log.e("VALUE SLIDES", "SLIDE 1: " + slide1_value + "\nSLIDE 2: " + slide2_value + "\nSLIDE 3: " + slide3_value);

        // Slider
        final boolean[] isSliderClicked = {false};
        slider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isSliderClicked[0] = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isSliderClicked[0]) {
                            // Handle the "click" event on the Slider
                            Log.d("Slider Example", "Slider clicked");
                            txtViewInfo1.setVisibility(View.INVISIBLE);
                            rectangle.setVisibility(View.VISIBLE);
                            txtViewDescription.setVisibility(View.VISIBLE);
                            btnConfirm.setClickable(true);
                            btnConfirm.setBackgroundResource(R.color.lavender);

                            isSliderClicked[0] = false;
                        }
                        break;
                }
                return false;
            }
        });

        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                // Perform actions with the new Slider value
                Log.d("Slider Example", "New value: " + value);
                slideValue = (int) value; // Convert float value to int

                int color = getResources().getColor(R.color.lavender);
                btnConfirm.setBackgroundTintList(ColorStateList.valueOf(color));

                switch (slideValue) {
                    case 1:
                        txtViewDescription.setText("Stable");
                        rectangle.setBackgroundResource(R.drawable.rounded_rectangle_stable);
                        break;
                    case 2:
                        txtViewDescription.setText("Slightly Increased");
                        rectangle.setBackgroundResource(R.drawable.rounded_rectangle_slightlyincreased);
                        break;
                    case 3:
                        txtViewDescription.setText("Moderately Increased");
                        rectangle.setBackgroundResource(R.drawable.rounded_rectangle_modeincreased);
                        break;
                    case 4:
                        txtViewDescription.setText("Significantly Increased");
                        rectangle.setBackgroundResource(R.drawable.rounded_rectangle_signincreased);
                        break;
                    default:
                        // Action for other values outside the range 1 to 4
                        break;
                }
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
                redirectActivity( FellingUnwellPageQ4.this, HomePage.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( FellingUnwellPageQ4.this, SettingsPage.class);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( FellingUnwellPageQ4.this, SharePage.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( FellingUnwellPageQ4.this, AboutPage.class);
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

                Intent intent = new Intent(FellingUnwellPageQ4.this, InitialPage.class);
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


    public void onConfirmPagButtonClick(View view) {
        if (slideValue != 0) {
            slide_sumValues = slide1_value + slide2_value + slide3_value + slideValue;
            new SaveQuestionnaireAnswers().execute(String.valueOf(slide_sumValues));
        }
    }

    private class SaveQuestionnaireAnswers extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FellingUnwellPageQ4.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            int sumAnswers = Integer.parseInt(params[0]);
            Log.e(TAG, "Valor recebido da soma: " + slide_sumValues);


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
                        String sql = "INSERT INTO questionnairesos (date, sumquestions, idpatient) VALUES (?, ?, ?)";
                        try (PreparedStatement pstmt2 = conn.prepareStatement(sql)) {
                            // set the parameter values
                            pstmt2.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                            pstmt2.setInt(2, sumAnswers);
                            pstmt2.setInt(3, patientId);
                            pstmt2.executeUpdate();
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error executing query", e);
                exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if (result) {
                Intent intent = new Intent(FellingUnwellPageQ4.this, FellingUnwellPageQ5.class);
                intent.putExtra("sumAnwsers", slide_sumValues);
                startActivity(intent);
            } else {
                Toast.makeText(FellingUnwellPageQ4.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onBackPageClick(View view) {
        redirectActivity(FellingUnwellPageQ4.this, HomePage.class);
    }


}