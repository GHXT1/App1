package com.swufe.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private final String TAG = "Second";
    TextView score;
    TextView score2;   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = findViewById(R.id.showText2);
        score2 = findViewById(R.id.showText2b);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea= ((TextView)findViewById(R.id.showText2)).getText().toString();
        String scoreb=((TextView)findViewById(R.id.showText2b)).getText().toString();

        Log.i(TAG, "onSaveInstanceState: ");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);
        
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea=savedInstanceState.getString("teama_score");
        String scoreb=savedInstanceState.getString("teamb_score");
        Log.i(TAG, "onRestoreInstanceState: ");

        ((TextView)findViewById(R.id.showText2)).setText(scorea);
        ((TextView)findViewById(R.id.showText2b)).setText(scoreb);
        
    }

    public void btnAdd1(View btn){           //
        if(btn.getId()==R.id.btn1){
            showScore(1);}
        else{
            showScore2(1);
        }
    }
    public void btnAdd2(View btn){ if(btn.getId()==R.id.btn2){
        showScore(2);}
    else{
        showScore2(2);
    }}
    public void btnAdd3(View btn){if(btn.getId()==R.id.btn3){
        showScore(3);}
    else{
        showScore2(3);
    }}
    public void btnReset(View btn){score.setText("0");score2.setText("0"); }
    private void showScore(int inc){
        Log.i("show","inc="+inc);
        String oldScore=(String)score.getText();
        int newScore=Integer.parseInt(oldScore)+inc;
        score.setText(""+newScore);

    }
   private void showScore2(int inc){
        Log.i("show","inc="+inc);
        String oldScore=(String)score2.getText();
        int newScore=Integer.parseInt(oldScore)+inc;
        score2.setText(""+newScore);

    }


}
//int C;
//		int F = in.nextInt();
//		C = (F - 32)*5/9;
//		System.out.println(C);
//		in.close();