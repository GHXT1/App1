package com.swufe.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RateActivity extends AppCompatActivity {
    private final String TAG="Rate";
    private float dollarRate=0.1f;
    private float euroRate=0.2f;
    private float wonRate=0.3f;
    EditText rmb;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showOut);
    }

    public void onClick(View btn) {
        Log.i(TAG,"onClick:");
        String str = rmb.getText().toString();
        float r = 0;
        float val = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        } else {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }
         Log.i(TAG,"onClick:r="+r);
        if (btn.getId() == R.id.btn_dollar) {
            show.setText(String.format("%.2f",r*dollarRate));
        } else if (btn.getId() == R.id.btn_euro) {
            show.setText(String.format("%.2f",r*euroRate));
        } else {
            show.setText(String.format("%.2f",r*wonRate));
        }

    }
    public void openOne(View btn){
        openConfig();
    }

    private void openConfig() {
        Log.i("open","openOne:");
        Intent config=new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG,"openOne:dollarRate="+dollarRate);
        Log.i(TAG,"openOne:euroRate="+euroRate);
        Log.i(TAG,"openOne:wonRate="+wonRate);
        //startActivity(config);
        //startActivity(intent);
        startActivityForResult(config,1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();//避免同一个代码页中多次出现重复的代码（在Refactor中的
            // Extract中的Method）

        }
        return super.onOptionsItemSelected(item);
    }

    //参数传递回旧页面
  protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1&&resultCode==2){
            /*
            *bdl.putFloat("dollarkey",newDollar);
        bdl.putFloat("eurokey",newEuro);
        bdl.putFloat("wonkey",newWon);*/
            Bundle bundle=data.getExtras();
            dollarRate=bundle.getFloat("dollarkey",0.1f);
            euroRate=bundle.getFloat("eurokey",0.2f);
            wonRate=bundle.getFloat("wonkey",0.3f);
            Log.i(TAG,"onActivityResult:dollarRate="+dollarRate);
            Log.i(TAG,"onActivityResult:euroRate="+euroRate);
            Log.i(TAG,"onActivityResult:wonRate="+wonRate);

        }


  }
}


