package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SettingsPasswordPage extends AppCompatActivity {

    // Retrieve user's login credentials
    private String emailShared;
    private String nameShared;


    // TextInputEditText's

    private TextInputEditText oldPassword;
    private TextInputEditText newPassword;


    String old_pass = "";
    String new_pass = "";







    private static String TAG = "SettingPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingspassword_page);

        // Retrieve user's login credentials
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        emailShared = sharedPreferences.getString("email", "");
        nameShared = sharedPreferences.getString("name", "");

        // TextInputEditText's
        oldPassword = findViewById(R.id.eTxtPasswordOld);
        newPassword = findViewById(R.id.eTxtPasswordNew);



    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void goBackToSettingsMenu (View view) {
        redirectActivity(SettingsPasswordPage.this, SettingsPage.class);
    }


    public void saveChangesPassword(View view) {

        old_pass = oldPassword.getText().toString().trim();
        new_pass = newPassword.getText().toString().trim();


        if (old_pass.isEmpty() || new_pass.isEmpty()) {
            // Empty fields
            Toast.makeText(SettingsPasswordPage.this, "Hey there! It seems like you left some fields empty!", Toast.LENGTH_LONG).show();
            return;
        }

        new VerifyPassword().execute(old_pass, new_pass);
    }

    private class VerifyPassword extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        String old_pass;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SettingsPasswordPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            old_pass = params[0];
            new_pass = params[1];

            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";

            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {

                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Patient WHERE email = ?");
                pstmt.setString(1, emailShared);
                ResultSet rs = pstmt.executeQuery();
                Log.e(TAG, pstmt.toString());
                if (rs.next()) {
                    String salt = rs.getString("salt");
                    String hashedPassword = BCrypt.hashpw(old_pass, salt);
                    if (hashedPassword.equals(rs.getString("password"))) {
                        return true;
                    }
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

            if (result) {
                new UpdatePassword().execute(new_pass);
            } else {
                Toast.makeText(SettingsPasswordPage.this, "Wrong password.", Toast.LENGTH_LONG).show();
            }
        }


        private class UpdatePassword extends AsyncTask<String, Void, Boolean> {
            private Exception exception;

            private ProgressDialog progressDialog;

            String new_pass;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(SettingsPasswordPage.this);
                progressDialog.setMessage("Processing, please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                new_pass = params[0];

                String salt = BCrypt.gensalt();
                String hashedPasswordNew = BCrypt.hashpw(new_pass, salt);

                String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
                String svusername = "postgres";
                String svpassword = "copdproject";

                try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {
                    Log.e(TAG, "Connection to BD succesfull!");
                    // Check if there is another user with the same username
                    String selectQuery = "SELECT COUNT(*) FROM Patient WHERE email = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                        pstmt.setString(1, emailShared);
                        try (ResultSet rs = pstmt.executeQuery()) {
                            rs.next();
                            int count = rs.getInt(1);
                            if (count == 1) {
                                String insertQuery = "UPDATE patient SET password = ?, salt = ? WHERE email = ?";
                                try (PreparedStatement pstmt2 = conn.prepareStatement(insertQuery)) {
                                    pstmt2.setString(1, hashedPasswordNew);
                                    pstmt2.setString(2, salt);
                                    pstmt2.setString(3, emailShared);
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
                    Toast.makeText(SettingsPasswordPage.this, "Successful password reset!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsPasswordPage.this, "Sorry, we can't change your password", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}