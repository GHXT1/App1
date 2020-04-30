package com.swufe.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RateCalcActivity extends AppCompatActivity {
    float rate=0f;
    String TAG="RateCalc";
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        title=getIntent().getStringExtra("title");
        rate=getIntent().getFloatExtra("rate",0f);

        Log.i(TAG, "onCreate:title ="+title);
        Log.i(TAG, "onCreate:rate ="+rate);
        ((TextView)findViewById(R.id.title2)).setText(title);
    }
}
