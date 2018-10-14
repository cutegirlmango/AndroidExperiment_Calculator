package com.example.lyy.androidexperiment_calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onWhiteClick(View view){
        Log.d(TAG, "white click");

    }
    public void onOrangeClick(View view){
        Log.d(TAG, "orange click");

    }
}
