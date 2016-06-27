package com.joke.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.joke.R;
import com.joke.widget.ClickableHistogram;

public class ClickableHistogramAty  extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_clickablehistogramaty);
        final ClickableHistogram clickableHistogram = (ClickableHistogram) findViewById(R.id.clickableHistogram);
        final ArrayList<ClickableHistogram.ColumnData> dataSource = new ArrayList<ClickableHistogram.ColumnData>();
        for (int i = 0; i <= 32; i++) {
            ClickableHistogram.ColumnData data = clickableHistogram.new ColumnData();
            data.setName("2015-9-1" + i);
            data.setValue(i + 20);
            dataSource.add(data);
        }
        clickableHistogram.setDataSource(dataSource);
       // Toast.makeText(ClickableHistogramAty.this, addZeroForNum("5", 2), Toast.LENGTH_SHORT).show();
        clickableHistogram.setColumnOnClickListener(new ClickableHistogram.OnColumnClickListener() {
            @Override
            public void onColumnClick(View v, int position, ClickableHistogram.ColumnData data) {
                Toast.makeText(ClickableHistogramAty.this, position + "\n" + data.getName()+"\n"+data.getValue(), Toast.LENGTH_SHORT).show();
            }
        });
        Button btnReload = (Button) findViewById(R.id.btnReload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickableHistogram.setDataSource(dataSource);
            }
        });
    }
    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        StringBuffer sb = null;
        if (str.split("\\.")[1].length()==2){
            System.out.println(str.split("\\.")[1].length());
            return str;
        }else{
        	 sb = new StringBuffer();
             // sb.append("0").append(str);// 左(前)补0
              sb.append(str).append("0");//右(后)补0
              str = sb.toString();
              strLen = str.length();
//        	while (strLen < strLength) {
//                sb = new StringBuffer();
//               // sb.append("0").append(str);// 左(前)补0
//                sb.append(str).append("0");//右(后)补0
//                str = sb.toString();
//                strLen = str.length();
//          }
        }
        
        return str;
    }    


}
