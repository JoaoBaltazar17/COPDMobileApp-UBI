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

public class SettingsWeightPage extends AppCompatActivity {

    private TextInputEditText txtViewNewWeight;

    private String emailShared;
    private String nameShared;

    private static String TAG = "SettingWeightActivity";

    private double new_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsweight_page);

        txtViewNewWeight = findViewById(R.id.eTxtWeight);

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
        redirectActivity(SettingsWeightPage.this, SettingsPage.class);
    }

    public void saveChangesWeight(View view) {

        new_weight = Double.parseDouble(txtViewNewWeight.getText().toString().trim());

        if (new_weight == 0.0) {
            // Empty fields
            Toast.makeText(SettingsWeightPage.this, "Hey there! It seems like you left some fields empty!", Toast.LENGTH_LONG).show();
            return;
        }

        Log.e(TAG, "New Height: " + new_weight + "For the patient with Email: " + emailShared);
        new ChangeWeight().execute(String.valueOf(new_weight));
    }

    private class ChangeWeight extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SettingsWeightPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            new_weight = Double.parseDouble(params[0]);

            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";

            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {

                PreparedStatement pstmt = conn.prepareStatement("UPDATE patient SET weightinkg = ? WHERE email = ?");
                pstmt.setDouble(1, new_weight);
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
                Toast.makeText(SettingsWeightPage.this, "Successful change!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Saved:" + emailShared + "/ New Height:" + new_weight + ".");
            } else {
                Toast.makeText(SettingsWeightPage.this, "We're sorry, but operation failed.", Toast.LENGTH_LONG).show();
            }
        }

    }
}