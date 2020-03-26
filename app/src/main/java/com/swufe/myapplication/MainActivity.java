package com.swufe.myapplication;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView out;
    EditText inp1,inp2;    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        out=findViewById(R.id.showText);
        //inp1=findViewById(R.id.inpText);
        //inp2=findViewById(R.id.inpText2);


        Button  btn=findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("main","onClick called......");
                String str=inp1.getText().toString();
                int a=Integer.valueOf(str).intValue();
                double c=(int) a;
                double f=c*9.00/5.00+32.00;
                inp2.setText("华氏温度为:"+f);
            }
        });

    }
    public void onClick(View v){           //
        Log.i("click","onClick.....");
        out.setText("swufe");

        String str=inp1.getText().toString();
        out.setText("Hello"+str);
    }



}
//int C;
//		int F = in.nextInt();
//		C = (F - 32)*5/9;
//		System.out.println(C);
//		in.close();