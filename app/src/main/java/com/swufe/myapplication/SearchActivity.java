package com.swufe.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SearchActivity extends ListActivity implements Runnable, AdapterView.OnItemClickListener{
    Handler handler;
    EditText search_Detail;
    String str,str1;
    private  String updateDate ="";
    private static final String TAG = "Search";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final List<String> searchlist=new ArrayList<String>();
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        //SharedPreferences sharePreferences=PreferenceManager.getDefaultSharedPreferences(this);于上面用法相同
        updateDate=sharedPreferences.getString("update_date","");

        //获取当前系统时间
        Date today=Calendar.getInstance().getTime();
        Calendar today1= new GregorianCalendar();
        today1.setTime(today);
        today1.add(Calendar.DAY_OF_YEAR,-7);
        Date today2=today1.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr=sdf.format(today2);
        int timeFlag=todayStr.compareTo(updateDate);

        if(timeFlag<0){
            Log.i(TAG, "onCreate: 已经更新过，不需要再更新");
        }else{
            Log.i(TAG, "onCreate: 正在更新");
            Thread t = new Thread(this);
            t.start();
        }

        search_Detail=findViewById(R.id.search_Detail);

        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        setListAdapter(adapter);

        handler =new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==7){
                    List<String> list2=(List<String>)msg.obj;
                    ListAdapter adapter=new ArrayAdapter<String>(SearchActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);

                    SharedPreferences sharedPreferences = getSharedPreferences("search", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("update_date",todayStr);
                    editor.commit();

                    Toast.makeText(SearchActivity.this,"已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
    }
    public void onClick(View btn) {
        Log.i(TAG, "onClick:");
        str = search_Detail.getText().toString();
        if (str.length() >0) {
            run();
        } else {
            Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void run() {
        //获取网络数据，放入list带回到主线程
        List<String> retList = new ArrayList<String>();

        Document doc = null;
        try {
            Thread.sleep(1000);
            doc = Jsoup.connect("http://it.swufe.edu.cn/index/tzgg.htm").get();
            Log.i(TAG, "run:  " + doc.title());//http://it.swufe.edu.cn
            //http://www.usd-cny.com/bankofchina.htm
            Elements tables = doc.getElementsByTag("!--#endeditable--");
    /*for (Element table : tables) {
        Log.i(TAG, "run:table[" + i + "]=" + table);
        i++;
    }*/
            Element endeditable4=tables.get(3);
            //Log.i(TAG, "run:table6=" + table6);
            //获取TD中的数据
            Elements spans = endeditable4.getElementsByTag("span");
            for (int i = 0; i < spans.size(); i += 1) {
                Element td1 = spans.get(i);
                Element td2 = spans.get(i + 1);

                str1 = td1.html();
                String val = td2.text();
                Log.i(TAG, "run:" + str1 + "==>" + val);
                retList.add(str1 + "=======" + val);


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(str1.indexOf(str)!=-1) {
            Message msg = handler.obtainMessage(7);
            msg.obj = retList;
            handler.sendMessage(msg);
        }else{
            Toast.makeText(this, "并未查找到相关信息", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView detail=view.findViewById(R.id.search_list);
        String a=String.valueOf(detail.getText());
        Intent intent=new Intent();
        intent.setData(Uri.parse(a));
        intent.setAction(Intent.ACTION_VIEW);
        this.startActivity(intent);

    }
}
