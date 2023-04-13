package com.example.copdmonitorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InitialPage extends AppCompatActivity {

    TextView txtViewGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_page);

        txtViewGoRegister = findViewById(R.id.txtViewRegisterClick);
    }

    public void onRegisterLinkClicked(View view) {
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }



}