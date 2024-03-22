package com.example.abdurss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    TextView tv_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);


        ArrayList<String> items = new ArrayList<>();
        // get the intent
        Intent intent = getIntent();

        // get data from the intent
        String name = intent.getStringExtra("Name");

        tv_about = findViewById(R.id.tv_about);

        tv_about.setText("This application was made by " + name);

        intent.putExtra("reply", items); //a random integer to return

        setResult(/*request code*/ 0, intent);

        finish();

    }
}