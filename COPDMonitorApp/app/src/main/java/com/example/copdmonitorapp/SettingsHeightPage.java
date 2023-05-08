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

public class SettingsHeightPage extends AppCompatActivity {

    private TextInputEditText txtViewNewHeight;

    private String emailShared;
    private String nameShared;

    private static String TAG = "SettingHeightActivity";

    private double new_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsheight_page);

        txtViewNewHeight = findViewById(R.id.eTxtHeight);

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
        redirectActivity(SettingsHeightPage.this, SettingsPage.class);
    }

    public void saveChangesHeight(View view) {

        new_height = Double.parseDouble(txtViewNewHeight.getText().toString().trim());

        if (new_height == 0.0) {
            // Empty fields
            Toast.makeText(SettingsHeightPage.this, "Hey there! It seems like you left some fields empty!", Toast.LENGTH_LONG).show();
            return;
        }

        Log.e(TAG, "New Height: " + new_height + "For the patient with Email: " + emailShared);
        new ChangeHeight().execute(String.valueOf(new_height));
    }

    private class ChangeHeight extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SettingsHeightPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            new_height = Double.parseDouble(params[0]);

            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";

            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {

                PreparedStatement pstmt = conn.prepareStatement("UPDATE patient SET heightincm = ? WHERE email = ?");
                pstmt.setDouble(1, new_height);
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
                Toast.makeText(SettingsHeightPage.this, "Successful change!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Saved:" + emailShared + "/ New Height:" + new_height + ".");
            } else {
                Toast.makeText(SettingsHeightPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }
}