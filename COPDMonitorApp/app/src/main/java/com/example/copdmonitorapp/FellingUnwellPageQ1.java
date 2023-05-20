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

public class FellingUnwellPageQ1 extends AppCompatActivity {

        private static String TAG = "Q1 Page";

        // Navigation Drawer Attributes
        DrawerLayout drawerLayout;
        ImageView menu;
        LinearLayout home, settings, share, about, logout;


        // Components
        Slider slider;
        View rectangle;
        TextView txtViewDescription;
        TextView txtViewInfo1;
        TextView txtViewInfo2;
        Button btnConfirm;


        // Variables
        int slideValueClick = 0;
        int slideValue;



        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fellingunwell_pageq1);


            // Component's Finders
            slider = findViewById(R.id.slideQ);
            rectangle = findViewById(R.id.rectangle);
            txtViewDescription = findViewById(R.id.txtViewDescriptionSlider);
            txtViewInfo1 = findViewById(R.id.txtViewInformation1);
            txtViewInfo2 = findViewById(R.id.txtViewInformation2);
            btnConfirm = findViewById(R.id.btnConfirm);


            // Link in text information
            SpannableString spannableString = new SpannableString(txtViewInfo1.getText());

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Uri uri = Uri.parse("https://www.thoracic.org/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            };

            int startIndex = txtViewInfo1.getText().toString().indexOf("American Thoracic Society");
            int endIndex = startIndex + "American Thoracic Society".length();

            spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            txtViewInfo1.setText(spannableString);
            txtViewInfo1.setMovementMethod(LinkMovementMethod.getInstance());

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
                                txtViewInfo2.setVisibility(View.INVISIBLE);

                                slideValueClick = 1;
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
                            Log.e("BUG", "BOAS");
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
                    redirectActivity( FellingUnwellPageQ1.this, HomePage.class);
                }
            });
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ1.this, SettingsPage.class);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ1.this, SharePage.class);
                }
            });
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ1.this, AboutPage.class);
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

                    Intent intent = new Intent(FellingUnwellPageQ1.this, InitialPage.class);
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
            Log.e(TAG, "Slide value while confirm: " + slideValue);
        if(slideValueClick != 0) {
            Intent intent = new Intent(FellingUnwellPageQ1.this, FellingUnwellPageQ2.class);
            intent.putExtra("slide_value1", slideValue);
            startActivity(intent);
        }
    }

    public void onBackPageClick(View view) {
            redirectActivity(FellingUnwellPageQ1.this, HomePage.class);
    }



}