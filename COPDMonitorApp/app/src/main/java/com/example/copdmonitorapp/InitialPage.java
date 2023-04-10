package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitialPage extends AppCompatActivity {

    Button btnSignUpPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_page);

        btnSignUpPage = findViewById(R.id.btnSignUp);
    }

    public void goToSignUpPage(View view) {
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }



}