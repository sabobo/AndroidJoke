package com.joke.activity;

import java.text.DateFormat;
import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.joke.R;
import com.joke.util.PullToRefreshView;
import com.joke.util.PullToRefreshView.OnFooterRefreshListener;
import com.joke.util.PullToRefreshView.OnHeaderRefreshListener;

public class MyProducte extends Activity implements OnHeaderRefreshListener, OnFooterRefreshListener {
    //** 下拉刷新 *//*
    private PullToRefreshView pullToRefreshView;

    //** 点击返回到顶部 *//*
    private TextView tv_backtop;

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 定义布局变量
        super.setContentView(R.layout.layout_pulltorefreshview);
        
        pullToRefreshView = (PullToRefreshView) super
        		.findViewById(R.id.pulltorefresh);
        //头部刷新
        pullToRefreshView.setOnHeaderRefreshListener(this);
        //尾部刷新
        pullToRefreshView.setOnFooterRefreshListener(this);
        
        pullToRefreshView.setLastUpdated(new Date().toLocaleString());
    }

    @SuppressWarnings("unused")
	private class ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        // TODO Auto-generated method stub
        pullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Date date = new Date();
                DateFormat ddf = DateFormat.getDateInstance();
                String day = ddf.format(date);
                DateFormat ddy = DateFormat.getTimeInstance();
                String timeString = ddy.format(date);
                DateFormat ddty = DateFormat.getDateTimeInstance();
                String shjian = ddty.format(date);
                System.out.println(day + "----" + timeString + "--------"+ shjian);
                // pullToRefreshView.onHeaderRefreshComplete("更新为"+new
                // Date().toLocaleString());
                pullToRefreshView.onHeaderRefreshComplete("更新为" + timeString);
            }
        }, 1000);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        // TODO Auto-generated method stub
    	pullToRefreshView.onFooterRefreshComplete();
    }

}