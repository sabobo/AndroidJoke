package com.joke.activity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.joke.BaseActivity;
import com.joke.R;
import com.joke.adapter.JokeurlTextAdapter;
import com.joke.config.ConfigResource;
import com.joke.info.JokeurlTextInfo;
import com.joke.util.NetWorkUtils;

public class JokeurlTextAty extends BaseActivity implements OnScrollListener ,OnClickListener{
	/***/
	private Context context;
	/****/
	private Button bt_JokeurlText_text;
	/** 数据 */
	private List< JokeurlTextInfo> mlist = new ArrayList< JokeurlTextInfo>();
	/** 数据 */
	private List< JokeurlTextInfo> newmlist = new ArrayList< JokeurlTextInfo>();
	/** 列表显示器 */
	private PullToRefreshListView mlistView;
	/***/
	private  JokeurlTextAdapter madapter;
	/** 线程池 */
    private ExecutorService executorService;
    /** 线程池默认大小 */
    private int THREAD_POOL_SIZE = 2;
    /** 请求成功 */
    public static final int LOADING_SUCCESSFUL = 0x0010;
    /** 请求失败 */
    public static final int LOADING_FAILURE = 0x0007;
    /** 加载模式 */
    private int load_type = PAGE_NEXT_LOAD;
    /** 页码 */
    private int pageIndex = 0;
    /** 数量 */
    private int number = 10;
    /** 是否已经启动 */
    public boolean isResume = false;
    /** 首次加载 */
    public static final int FIRST_LOAD = 0x0000;
    /** 翻页加载 */
    public static final int PAGE_NEXT_LOAD = 0x0001;
    /** 下拉刷新 */
    public static final int REFRESH_PULL = 0x0002;
    /** 首次加载请求失败 */
    public static final int FIRST_REQUEST_ERROR = 0x0003;
    /** 翻页加载请求失败 */
    public static final int PAGE_NEXT_REQUEST_ERROR = 0x0004;
    /** 首次加载请求失败 */
    public static final int REFRESH_REQUEST_ERROR = 0x0005;
    /** 自动加载下一页成功 */
    public static final int LOADING_NEXT_PAGE = 0x0021;
    /** 自动加载下一页失败 */
    public static final int LOADING_NEXT_PAGE_ERROR = 0x0020;
    /** 加载数据失败 */
    public static final int LOADING_NO_ERROR = 0x0022;
    /**记录上次滚动之后的第一个可见item**/
    private int mFirstVisibleItem = -1;
    /**记录上次滚动之后的最后一个item**/
    private int mLastVisibleItem = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jokeurltext);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
        .detectDiskReads()  
        .detectDiskWrites()  
        .detectNetwork()  
        .penaltyLog()  
        .build()); 
		context = this;
		executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		initView();
		firstLoadingDatas();
        onSetListener();
		///intData();
	}
	/***
	 * 初始化视图
	 * 
	 * @param view
	 */
	private void initView() {
		bt_JokeurlText_text = (Button)findViewById(R.id.bt_jokeurltext_text);
		mlistView = (PullToRefreshListView) findViewById(R.id.jokeurltext_listview);
		mlistView.setOnScrollListener(this);
		bt_JokeurlText_text.setOnClickListener(this);
	}
	 /** listview */
    private void onSetListener() {
    	mlistView.setMode(Mode.BOTH);
    	mlistView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            	
            	if (!NetWorkUtils.checkNetWork(context)) {
                    Toast.makeText(context, "没有网络!", Toast.LENGTH_LONG).show();
                    mlistView.onRefreshComplete();
                    return;
                } else {
                    load_type = REFRESH_PULL;
                    executorService.submit(new LoadDataThread());
                }	
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                if (!NetWorkUtils.checkNetWork(context)) {
                    Toast.makeText(context, "没有网络!", Toast.LENGTH_LONG).show();
                    mlistView.onRefreshComplete();
                    return;
                } else {
                    load_type = PAGE_NEXT_LOAD;
                    executorService.submit(new LoadDataThread());
                }
            }
        });
    }
	
   /***
	 * 加载数据
	 */
	private void intData() {
		if (NetWorkUtils.checkNetWork(context)) {
		 if (load_type == FIRST_LOAD) {
	            isFirstLoad();
	        }
	        if (load_type == PAGE_NEXT_LOAD) {
	            isPageNext();
	        }
	        if (load_type == REFRESH_PULL) {
	            isPullToRefresh();
	        }
			String jsonResult = request(ConfigResource.JOKEURLTEXT, "page="+pageIndex);
			try {
				JSONObject object1 = new JSONObject(jsonResult);
				String showapi_res_body = object1.getString("showapi_res_body");
				JSONObject object2 = new JSONObject(showapi_res_body);
				String data = object2.getString("contentlist");
				JSONArray array = new JSONArray(data);
				for (int i = 0; i < array.length(); i++) {
					JokeurlTextInfo info = new JokeurlTextInfo();
					String  ct = array.getJSONObject(i).getString("ct");
					String  text = array.getJSONObject(i).getString("text");
					String  title = array.getJSONObject(i).getString("title");
					String  type = array.getJSONObject(i).getString("type");
					info.ct = ct;
					info.text = text;
					info.title = title;
					info.type = info.type;
					newmlist.add(info);
				}
				if (newmlist != null && newmlist.size() > 0) {
	                if (load_type == FIRST_LOAD) {
	                	sendToastMessage(FIRST_LOAD);
	                }
	                if (load_type == PAGE_NEXT_LOAD) {
	                	sendToastMessage(PAGE_NEXT_LOAD);
	                }
	                if (load_type == REFRESH_PULL) {
	                	sendToastMessage(REFRESH_PULL);
	                }
	            } else {
	            	sendToastMessage(LOADING_NEXT_PAGE_ERROR);
	            }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			Toast.makeText(context, "网络异常！", Toast.LENGTH_LONG).show();
		}
	}
	
	 /*** 首次加载数据 */
    public void firstLoadingDatas() {
        load_type = FIRST_LOAD;
        executorService.submit(new LoadDataThread());
    }
    /** 数据加载 */
    private class LoadDataThread implements Runnable {
        @Override
        public void run() {
        	intData();
        }
    }

    /** 发送消息 */
    private void isFirstLoad() {
        pageIndex = 1;
    }

    /** 翻页加载数据 */
    private void isPageNext() {
        pageIndex = pageIndex + 1;
    }

    /** 下拉刷新加载数据 */
    private void isPullToRefresh() {
    	mlist.clear();
    	if(madapter!=null){  
  		  madapter.notifyDataSetChanged();  
        }  
        pageIndex = 1;
    }
    /** 处理UI线程 */
    private Handler handler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case FIRST_LOAD:
            	mlistView.onRefreshComplete();
            	mlist = newmlist;
                madapter = new JokeurlTextAdapter(mlist,context);
                mlistView.setAdapter(madapter);
                break;

            case PAGE_NEXT_LOAD:
            	mlistView.onRefreshComplete();
                madapter.addItems(-1, newmlist);
                break;
            case REFRESH_PULL:
            	mlistView.onRefreshComplete();
            	mlist = newmlist;
                madapter = new JokeurlTextAdapter(mlist,context);
                mlistView.setAdapter(madapter);
                break;
            case FIRST_REQUEST_ERROR:
            	mlistView.onRefreshComplete();
                break;
            case PAGE_NEXT_REQUEST_ERROR:
            	mlistView.onRefreshComplete();
                break;
            case REFRESH_REQUEST_ERROR:
            	mlistView.onRefreshComplete();
                break;
            case LOADING_NEXT_PAGE_ERROR:
                Toast.makeText(context, "所有数据已经加载完成!", Toast.LENGTH_SHORT).show();
                mlistView.onRefreshComplete();
                break;
            }   
        };
    };
    /** 发送消息 */
    private void sendToastMessage(int what) {
        Message msg = handler1.obtainMessage();
        msg.what = what;
        handler1.sendMessage(msg);
    }
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
        //listview第一次载入时，两者都为-1
        boolean shouldAnimate = (mFirstVisibleItem != -1) && (mLastVisibleItem != -1);
        //滚动时最后一个item的位置
        int lastVisibleItem = firstVisibleItem + visibleItemCount -1;
        if(shouldAnimate){//第一次不需要加载动画
            int indexAfterFist =0;
            //如果出现这种情况，说明是在向上scroll，如果scroll比较快的话，一次可能出现多个新的view，我们需要用循环
            //去获取所有这些view，然后执行动画效果
            while(firstVisibleItem + indexAfterFist < mFirstVisibleItem){
                View animateView = view.getChildAt(indexAfterFist);//获取item对应的view
                doAnimateOne(animateView, false);
                indexAfterFist ++;
            }

            int indexBeforeLast = 0;
            //向下scroll, 情况类似，只是计算view的位置时不一样
            while(lastVisibleItem - indexBeforeLast > mLastVisibleItem){
                View animateView = view.getChildAt(lastVisibleItem - indexBeforeLast - firstVisibleItem);
                doAnimateOne(animateView, true);
                indexBeforeLast ++;
            }
        }

        mFirstVisibleItem = firstVisibleItem;
        mLastVisibleItem = lastVisibleItem;
	}
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (!NetWorkUtils.checkNetWork(context)) {
            Toast.makeText(context, "没有网络!", Toast.LENGTH_LONG).show();
            mlistView.onRefreshComplete();
            return;
        } else {
        	load_type = FIRST_LOAD;
            executorService.submit(new LoadDataThread());
        }
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_jokeurltext_text:
			finish();
			((Activity) context).overridePendingTransition(
					R.anim.scale_in, R.anim.slide_bottom_out);
			break;

		default:
			break;
		}
	}
}
