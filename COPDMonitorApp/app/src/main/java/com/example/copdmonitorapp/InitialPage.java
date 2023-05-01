package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.mindrot.jbcrypt.BCrypt;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InitialPage extends AppCompatActivity {

    TextView txtViewGoRegister;

    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword;



    private static String TAG = "Initial Page";
    private String pacientNameLogged;
    private String pacientEmailLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_page);

        txtViewGoRegister = findViewById(R.id.txtViewRegisterClick);
        editTextEmail = findViewById(R.id.eTxtEmail);
        editTextPassword = findViewById(R.id.eTxtPassword);

        // Retrieve user's login credentials
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");


        // Check if the username and password are not empty before using them to log the user in
        if (!email.isEmpty() && !password.isEmpty()) {
            new LoginTask().execute(email, password);
        }
    }



    public void onLoginClicked(View view) {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            // Empty fields
            Toast.makeText(InitialPage.this, "Hey there! It seems like you left some fields empty!", Toast.LENGTH_LONG).show();
            return;
        }

        new LoginTask().execute(email, password);
    }


    // DB Connection Pacient

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(InitialPage.this);
            progressDialog.setMessage("Processing, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            String svurl = "jdbc:postgresql://copd-db-instance.cr6kvihylkhm.eu-north-1.rds.amazonaws.com:5432/copd_db";
            String svusername = "postgres";
            String svpassword = "copdproject";

            try (Connection conn = DriverManager.getConnection(svurl, svusername, svpassword)) {

                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Pacient WHERE email = ?");
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String salt = rs.getString("salt");
                    pacientNameLogged = rs.getString("name");
                    pacientEmailLogged = email;
                    String hashedPassword = BCrypt.hashpw(password, salt);

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
                saveUserInSharedPreferences();
                goToHomePage();
            } else {
                Toast.makeText(InitialPage.this, "We're sorry, but we couldn't log you in.", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void goToHomePage(){
        Intent intent = new Intent(this, HomePage.class);
        intent.putExtra("nameLogged", pacientNameLogged);
        intent.putExtra("emailLogged", pacientEmailLogged);
        startActivity(intent);
    }

    public void onRegisterLinkClicked(View view){
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

    private void saveUserInSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("email", editTextEmail.getText().toString());
        editor.putString("password", editTextPassword.getText().toString());
        editor.commit();
    }


}