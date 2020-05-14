 package com.swufe.myapplication;

 import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    List<String> data=new ArrayList<String>();
    private  String TAG="mylist";
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);


        ListView listView=findViewById(R.id.mylist);

        /*for(int i=0;i<10;i++){
            data.add("item"+1);
        }*/

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.nodata));
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> listv, View view, int position, long id){
        Log.i(TAG,"onItemclick:positions"+position);
        Log.i(TAG, "onItemClick: parent"+listv);//listv(parent)是点击的那个控件的listview对象
        adapter.remove(listv.getItemAtPosition(position));//删除点击的那个控件
        //er.notifyDataSetChanged();

    }
}
