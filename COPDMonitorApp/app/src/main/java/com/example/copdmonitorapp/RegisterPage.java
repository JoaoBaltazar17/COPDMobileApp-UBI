package com.example.copdmonitorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterPage extends AppCompatActivity {

    TextView TextVClickableGoLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        TextVClickableGoLogin = findViewById(R.id.txtViewGoLogin);
    }

    public void onLoginLinkClicked(View view){
        Intent intent = new Intent(this, InitialPage.class);
        startActivity(intent);
    }
}
