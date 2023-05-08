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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SettingsEmailPage extends AppCompatActivity {

    private TextInputEditText txtViewNewEmail;

    private String emailShared;
    private String nameShared;

    private String new_email;

    private static String TAG = "SettingEmailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsemail_page);

        txtViewNewEmail = findViewById(R.id.eTxtUserEmailChange);

        // Retrieve user's login credentials
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        emailShared = sharedPreferences.getString("email", "");
        nameShared = sharedPreferences.getString("name", "");
    }


    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void goBackToSettingsMenu (View view) {
        redirectActivity(SettingsEmailPage.this, SettingsPage.class);
    }

    public void saveChangesEmail(View view) {

        String new_email = txtViewNewEmail.getText().toString().trim();

        if (new_email.trim().isEmpty()) {
            // Empty fields
            Toast.makeText(SettingsEmailPage.this, "Hey there! It seems like you left some fields empty!", Toast.LENGTH_LONG).show();
            return;
        }

        Log.e(TAG, "New email: " + new_email + "For the patient with Email: " + emailShared + ".");
        new ChangeUsername().execute(new_email);
    }

    private class ChangeUsername extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SettingsEmailPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            new_email = params[0];

            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";

            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {

                PreparedStatement pstmt = conn.prepareStatement("UPDATE patient SET email = ? WHERE email = ?");
                pstmt.setString(1, new_email);
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
                Toast.makeText(SettingsEmailPage.this, "Successful change!", Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.e(TAG, "Saved:" + new_email + "/" + nameShared + ".");
                editor.putString("email", new_email);
                editor.putString("name", nameShared);
                editor.commit();
            } else {
                Toast.makeText(SettingsEmailPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }
}