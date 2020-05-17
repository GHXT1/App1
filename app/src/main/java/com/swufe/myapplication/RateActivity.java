package com.swufe.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RateActivity<timerTask> extends AppCompatActivity implements Runnable {
    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    private  String updateDate ="";
    EditText rmb;
    TextView show;
    Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showOut);

        //获取sharedpreferences里保存的数据（汇率）
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        //SharedPreferences sharePreferences=PreferenceManager.getDefaultSharedPreferences(this);于上面用法相同
        dollarRate = sharedPreferences.getFloat("dollar_rate", 0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate", 0.0f);
        wonRate = sharedPreferences.getFloat("won_rate", 0.0f);
        updateDate=sharedPreferences.getString("update_date","");


        //获取当前系统时间
        Date today=Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr=sdf.format(today);

        Log.i(TAG, "onCreate:sp dollarRtate=" + dollarRate);
        Log.i(TAG, "onCreate:sp euroRate=" + euroRate);
        Log.i(TAG, "onCreate:sp wonRtate=" + wonRate);
        Log.i(TAG, "onCreate:sp updateDate=" + updateDate);
        Log.i(TAG, "onCreate:todayStr=" + todayStr);

        //判断时间
        if(!todayStr.equals(updateDate)){
            //开启子线程
            Log.i(TAG, "onCreate: 正在更新");
            Thread t = new Thread(this);
            t.start();
        }else{
            Log.i(TAG, "onCreate: 已经更新过，不需要再更新");
        }


        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 5) {
                    Bundle bdl = (Bundle) msg.obj;
                    dollarRate=bdl.getFloat("dollar-rate");
                    wonRate=bdl.getFloat("won-rate");
                    euroRate=bdl.getFloat("euro-rate");

                    Log.i(TAG, "handleMessage: dollarRate:"+dollarRate);
                    Log.i(TAG, "handleMessage: wonRate:"+wonRate);
                    Log.i(TAG, "handleMessage: euroRate:"+euroRate);

                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("dollar_rate", dollarRate);
                    editor.putFloat("euro_rate", euroRate);
                    editor.putFloat("won_rate", wonRate);
                    editor.putString("update_date",todayStr);
                    editor.commit();

                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();               }
                super.handleMessage(msg);
            }
        };//这个分号是对这个语句（handler=new Handler()）结束的标志
    }

    public void onClick(View btn) {
        Log.i(TAG, "onClick:");
        String str = rmb.getText().toString();
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        } else {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, "onClick:r=" + r);
        if (btn.getId() == R.id.btn_dollar) {
            show.setText(String.format("%.2f", r * dollarRate));
        } else if (btn.getId() == R.id.btn_euro) {
            show.setText(String.format("%.2f", r * euroRate));
        } else {
            show.setText(String.format("%.2f", r * wonRate));
        }

    }

    public void openOne(View btn) {
        openConfig();
    }

    private void openConfig() {
        Log.i("open", "openOne:");
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);

        Log.i(TAG, "openOne:dollarRate=" + dollarRate);
        Log.i(TAG, "openOne:euroRate=" + euroRate);
        Log.i(TAG, "openOne:wonRate=" + wonRate);
        //startActivity(config);
        //startActivity(intent);
        startActivityForResult(config, 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_set) {
            openConfig();//避免同一个代码页中多次出现重复的代码（在Refactor中的
            // Extract中的Method）

        }else if(item.getItemId()==R.id.open_list) {
            //打开列表窗口
            Intent list = new Intent(this, MyList2Activity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    //参数传递回旧页面
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            /*
            *bdl.putFloat("dollarkey",newDollar);
        bdl.putFloat("eurokey",newEuro);
        bdl.putFloat("wonkey",newWon);*/
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("dollarkey", 0.1f);
            euroRate = bundle.getFloat("eurokey", 0.2f);
            wonRate = bundle.getFloat("wonkey", 0.3f);
            Log.i(TAG, "onActivityResult:dollarRate=" + dollarRate);
            Log.i(TAG, "onActivityResult:euroRate=" + euroRate);
            Log.i(TAG, "onActivityResult:wonRate=" + wonRate);

            //将新设置的汇率写到sp里;
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate", dollarRate);
            editor.putFloat("euro_rate", euroRate);
            editor.putFloat("won_rate", wonRate);
            editor.commit();
            Log.i(TAG, "onActivityResult:数据已保存到sharedPreferences");
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //用于保存获取的汇率
        Bundle bundle = getFromUsdCny();
        /*for (Element td : tds) {
            Log.i(TAG, "run:td=" + td);
            Log.i(TAG, "run:text="+td.text());
            Log.i(TAG, "run:html="+td.html());
        }*/

            //bundle中保存所获取的汇率

            //获取网络数据
      /*URL url= null;
      try {
          url = new URL("www.usd-cny.com/bankofchina.htm");
          HttpURLConnection http=(HttpURLConnection) url.openConnection();
          InputStream in=http.getInputStream();

          String html=inputStream25tring(in);
          Log.i(TAG,"run:html="+html);
          Document doc = Jsoup.parse(html);
      } catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }*/
        }

    private Bundle getFromUsdCny() {
        Bundle bundle=new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run:  " + doc.title());
            Elements tds = doc.getElementsByTag("td");
    /*for (Element table : tables) {
        Log.i(TAG, "run:table[" + i + "]=" + table);
        i++;
    }*/
            //Log.i(TAG, "run:table6=" + table6);
            //获取TD中的数据
            Element td1, td2;
            for (int i = 0; i < tds.size(); i += 6) {
                td1 = tds.get(i);//货币名字
                td2 = tds.get(i + 5);//对应的汇率
                Log.i(TAG, "run:" + td1.text() + "==>" + td2.text());
                String str = td1.text();
                String val = td2.text();


                try {
                    if ("美元".equals(str)) {
                        bundle.putFloat("dollar-rate", 100f / Float.parseFloat(val));
                    } else if ("韩国元".equals(str)) {
                        bundle.putFloat("won-rate", 100f / Float.parseFloat(val));
                    } else if ("欧元".equals(str)) {
                        bundle.putFloat("euro-rate", 100f / Float.parseFloat(val));
                    }


                } catch (Exception ee) {
                    Log.i(TAG, "getFromUsdCny: run:网页已改变，请修改网页源代码");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //获取Msg对象，用于返回主线程；
        Message msg=handler.obtainMessage(5);
        //msg.obj="hello from run()";
        msg.obj = bundle;
        handler.sendMessage(msg);
        return bundle;
    }

    //timer.schedule(timerTask,0,86400000);
    private String inputStream25tring(InputStream inputStream) throws IOException {
        final int bufferSize=1024;
        final char[] buffer=new char[bufferSize];
        final StringBuilder out=new StringBuilder();
        Reader in=new InputStreamReader(inputStream,"gb2312");
        for(;;){
            int rsz=in.read(buffer,0,buffer.length);
            if (rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return  out.toString();

  }
}


