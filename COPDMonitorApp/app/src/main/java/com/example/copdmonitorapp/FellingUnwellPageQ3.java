package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
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

public class FellingUnwellPageQ3 extends AppCompatActivity {

    private static String TAG = "Q3 Page";

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
    int slide2_value;
    int slide1_value;
    int slideValue;
    int slideValueClick = 0;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fellingunwell_pageq3);


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
        Log.e(TAG, "Past slide value: " + slide2_value);

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
                            slideValueClick = 1;

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
                    case 0:
                        txtViewDescription.setText("Very Stable");
                        rectangle.setBackgroundResource(R.drawable.rounded_rectangle_verystable);
                        btnConfirm.setClickable(true);
                        break;
                    case 1:
                        txtViewDescription.setText("Stable");
                        rectangle.setBackgroundResource(R.drawable.rounded_rectangle_stable);
                        btnConfirm.setClickable(true);
                        break;
                    case 2:
                        txtViewDescription.setText("Moderate");
                        rectangle.setBackgroundResource(R.drawable.rounded_rectangle_moderate);
                        btnConfirm.setClickable(true);
                        break;
                    case 3:
                        txtViewDescription.setText("Slightly Increased");
                        rectangle.setBackgroundResource(R.drawable.rounded_rectangle_slightlyincreased);
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
                redirectActivity( FellingUnwellPageQ3.this, HomePage.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( FellingUnwellPageQ3.this, SettingsPage.class);
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
                redirectActivity( FellingUnwellPageQ3.this, AboutPage.class);
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

                Intent intent = new Intent(FellingUnwellPageQ3.this, InitialPage.class);
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
        if(slideValue != 0) {
            Intent intent = new Intent(FellingUnwellPageQ3.this, FellingUnwellPageQ4.class);
            intent.putExtra("slide_value1", slide1_value);
            intent.putExtra("slide_value2", slide2_value);
            intent.putExtra("slide_value3", slideValue);
            startActivity(intent);
        }
    }

    public void onBackPageClick(View view) {
        redirectActivity(FellingUnwellPageQ3.this, HomePage.class);
    }



}