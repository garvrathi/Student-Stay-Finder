package com.example.welcomepage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class loginsuccessful extends AppCompatActivity {

    ImageView backbutton = findViewById(R.id.backbtn);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsuccessful);

        backbutton.setOnClickListener(v -> onBackPressed());
    }
}