package com.example.copdmonitorapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class RegisterPage extends AppCompatActivity {

    TextView TextVClickableGoLogin;

    TextInputEditText editTextEmail;
    TextInputEditText editTextName;
    TextInputEditText editTextPassword;
    TextInputEditText editTextPasswordRepeat;

    MaterialButton btnSeverity;

    private DatePickerDialog datePickerDialog;
    private MaterialButton dateButton;

    private static String TAG = "Register Page";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        TextVClickableGoLogin = findViewById(R.id.txtViewGoLogin);
        editTextEmail = findViewById(R.id.eTxtEmail);
        editTextName = findViewById(R.id.eTxtName);
        editTextPassword = findViewById(R.id.eTxtPassword);
        editTextPasswordRepeat = findViewById(R.id.eTxtPasswordRepeat);
        btnSeverity = findViewById(R.id.btnCOPDSev);

        // Date
        initDatePicker();
        dateButton = findViewById(R.id.btnDatePicker);
    }

    public void onRegisterClicked(View view) {
        String email = editTextEmail.getText().toString().trim();
        String name = editTextName.getText().toString();
        String password = editTextPassword.getText().toString().trim();
        String passwordRepeat = editTextPasswordRepeat.getText().toString().trim();
        String severity = btnSeverity.getText().toString();
        String dateB = dateButton.getText().toString();

        Log.e(TAG, "Date default: " + dateB + " /  Severity default: " + severity);

        if (email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty() || dateB.equals("Date of Birth")) {
            // Empty fields
            Toast.makeText(RegisterPage.this, "Hey there! It seems like you left some fields empty!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(passwordRepeat)) {
            // Passwords don't match
            Toast.makeText(RegisterPage.this, "It seems like the passwords you entered don't match!", Toast.LENGTH_LONG).show();
            return;
        }

        new UserRegistrationTask().execute(email, password, name, dateB, severity);
    }

    private class UserRegistrationTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String name = params[2];
            String age = params[3];
            String copd = params[4];

            String salt = BCrypt.gensalt();
            String hashedPassword = BCrypt.hashpw(password, salt);


            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";


            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {
                Log.e(TAG, "Connection to BD succesfull!");
                // Check if there is another user with the same username
                String selectQuery = "SELECT COUNT(*) FROM Patient WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                    pstmt.setString(1, email);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        rs.next();
                        int count = rs.getInt(1);
                        if (count == 0) {
                            // User does not exist, insert into database
                            String insertQuery = "INSERT INTO Patient (email, password, salt, name, date_birth_mmddaaaa, copd_severity) VALUES (?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement pstmt2 = conn.prepareStatement(insertQuery)) {
                                pstmt2.setString(1, email);
                                pstmt2.setString(2, hashedPassword);
                                pstmt2.setString(3, salt);
                                pstmt2.setString(4, name);
                                pstmt2.setString(5, age);
                                pstmt2.setString(6, copd);
                                pstmt2.executeUpdate();
                            }
                            return true;
                        }
                        return false;
                    }
                }
            } catch (Exception e) {
                Log.e("MyApp", "Error executing query", e);
                exception = e;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if (result) {
                Toast.makeText(getApplicationContext(), "Successful registration!", Toast.LENGTH_SHORT).show();
                saveUserInSharedPreferences();
                goToHomePage();
            } else {
                Toast.makeText(RegisterPage.this, "Sorry, the email is already in use!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Popup to Choose Severity Level
    public void clickSelectSeverity(View view) {
        btnSeverity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(RegisterPage.this, btnSeverity);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                // Set a click listener for the menu items
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_1:
                                btnSeverity.setText("Mild");
                                return true;
                            case R.id.menu_2:
                                btnSeverity.setText("Moderate");
                                return true;
                            case R.id.menu_3:
                                btnSeverity.setText("Severe");
                                return true;
                            case R.id.menu_4:
                                btnSeverity.setText("Very Severe");
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }
        });


    }

    // Date Picker
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    public void goToHomePage(){
        Intent intent = new Intent(this, InitialPage.class);
        startActivity(intent);
    }

    public void onLoginLinkClicked(View view){
        Intent intent = new Intent(this, InitialPage.class);
        startActivity(intent);
    }

    public void saveUserInSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("email", editTextEmail.getText().toString());
        editor.putString("password", editTextPassword.getText().toString());
        editor.commit();
    }

}
