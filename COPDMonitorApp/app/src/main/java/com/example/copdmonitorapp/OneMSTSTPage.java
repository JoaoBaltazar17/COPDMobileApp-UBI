package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class OneMSTSTPage extends AppCompatActivity {

    // Share Variables
    Button btnShareWhatsApp;

    // Timer Variables
    private TextView txtViewTimerText;
    private Button btnStopStart;

    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    private boolean timerStarted = false;

    private static final int TIMER_DURATION = 5; // 6 mins (6 * 60 segundos)



    // Pulsation Values
    int pulsi = 0;
    int pulsf = 0;


    // Navigation Drawer Attributes
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, settings, share, about, logout;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onemstst_page);

        // Share Button Finder and Listener
        btnShareWhatsApp = findViewById(R.id.btnShareResults);
        btnShareWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent with the WhatsApp URL scheme
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Your text here");
                shareIntent.setPackage("com.whatsapp");
                startActivity(shareIntent);
            }
        });

        // Timer Finders
        txtViewTimerText = (TextView) findViewById(R.id.txtViewTimerText1MSTST);
        btnStopStart = (Button) findViewById(R.id.btn1MSTSTStartStop);

        timer = new Timer();


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
                redirectActivity(OneMSTSTPage.this, HomePage.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(OneMSTSTPage.this, SettingsPage.class);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(OneMSTSTPage.this, SharePage.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(OneMSTSTPage.this, AboutPage.class);
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

                Intent intent = new Intent(OneMSTSTPage.this, InitialPage.class);
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


    // Timer Methods

    public void onResetClick1MSTST(View view)
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Reset Timer");
        resetAlert.setMessage("Are you sure you want to reset the timer?");
        resetAlert.setPositiveButton("Reset", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(timerTask != null)
                {
                    timerTask.cancel();
                    setButtonUI("START", R.color.green);
                    time = 0.0;
                    timerStarted = false;
                    txtViewTimerText.setText(formatTime(0,0,0));

                }
            }
        });

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //do nothing
            }
        });

        resetAlert.show();

    }

    public void onStartStopClick1MSTST(View view) {
        if (!timerStarted) {
            AlertDialog.Builder inputAlert = new AlertDialog.Builder(this);
            inputAlert.setTitle("Enter your heart rate before the test starts\n");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER); // Apenas números inteiros
            inputAlert.setView(input);

            inputAlert.setPositiveButton("Start Test", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String pulseString = input.getText().toString();
                    int pulse = 0;

                    try {
                        pulse = Integer.parseInt(pulseString);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "\n" +
                                "Please enter a valid integer.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (pulse < 40 || pulse > 140) {
                        Toast.makeText(getApplicationContext(), "Please enter an integer between 40 and 140.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    pulsi = pulse;


                    timerStarted = true;
                    setButtonUI("START", R.color.verdepastel);

                    Log.e("1MSTS", "1 MSTST HAS BEEN STARTED!");
                    startTimer();
                }
            });

            inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Faça algo caso o usuário cancele a inserção da pulsação
                }
            });

            inputAlert.show();
        }
        /*
        else
        {
            timerStarted = false;
            setButtonUI("START", R.color.green);

            timerTask.cancel();
        }
        */
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        txtViewTimerText.setText(getTimerText());

                        if (time >= TIMER_DURATION) {

                            // Cancel timer
                            timerTask.cancel();
                            Log.e("AFTER TEST 1MSTST", "Concluded!");

                            AlertDialog.Builder inputAlert = new AlertDialog.Builder(OneMSTSTPage.this);
                            inputAlert.setTitle("Enter your heart rate after completing the test\n");
                            final EditText input = new EditText(OneMSTSTPage.this);
                            input.setInputType(InputType.TYPE_CLASS_NUMBER); // Apenas números inteiros
                            inputAlert.setView(input);

                            inputAlert.setPositiveButton("Conclude Test", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String pulseString = input.getText().toString();
                                    int pulse = 0;

                                    try {
                                        pulse = Integer.parseInt(pulseString);
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(getApplicationContext(), "\n" +
                                                "Please enter a valid integer.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (pulse < 40 || pulse > 140) {
                                        Toast.makeText(getApplicationContext(), "Please enter an integer between 40 and 140.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    pulsf = pulse;


                                    // Cancel timer count
                                    timerStarted = false;
                                    time = 0.0;
                                    setButtonUI("START", R.color.green);
                                }
                            });

                            inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Cancel timer count
                                    timerStarted = false;
                                    time = 0.0;
                                    setButtonUI("START", R.color.green);
                                }
                            });

                            inputAlert.show();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }


    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }

    private void setButtonUI(String start, int color) {
        btnStopStart.setText(start);
        btnStopStart.setTextColor(ContextCompat.getColor(this, color));
    }

    public void goBackToExerciseMenu(View view) {
        redirectActivity(OneMSTSTPage.this, ExerciseMenuPage.class);
    }
}