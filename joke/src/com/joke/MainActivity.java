package com.joke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.joke.activity.JokeurlTextAty;
import com.joke.adapter.MainAdapter;
import com.joke.config.Api;
import com.joke.config.ConfigResource;
import com.joke.info.MainInfo;
import com.joke.util.NetWorkUtils;
import com.joke.util.ViewFinder;
import com.joke.widget.DottedProgressBar;

public class MainActivity extends BaseActivity implements OnScrollListener,
		OnClickListener {
	/***/
	private Context context;
	/****/
	private Button bt_main_text;
	/** 数据 */
	private List<MainInfo> mlist = new ArrayList<MainInfo>();
	/** 数据 */
	private List<MainInfo> newmlist = new ArrayList<MainInfo>();
	/** 列表显示器 */
	private PullToRefreshListView mlistView;
	/***/
	private MainAdapter madapter;
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
	@SuppressWarnings("unused")
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
	/** 记录上次滚动之后的第一个可见item **/
	private int mFirstVisibleItem = -1;
	/** 记录上次滚动之后的最后一个item **/
	private int mLastVisibleItem = -1;
	/****/
	private DottedProgressBar bar;
    private ViewFinder finder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Fresco.initialize(this);//开始初始化
		setContentView(R.layout.activity_main);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		context = this;
		executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		finder = new ViewFinder(this);
		initView();
		firstLoadingDatas();
		onSetListener();
		// intData();
		// volley_json_post();
		// volley_post();
		//volley_post1();
	}

	/***
	 * 初始化视图
	 * 
	 * @param view
	 */
	private void initView() {
		bt_main_text = finder.find(R.id.bt_main_text);
		mlistView = finder.find(R.id.main_listview);
		bar = finder.find(R.id.main_progress);
		Runnable run = new Runnable() {

			@Override
			public void run() {
				bar.startProgress();
			}
		};
		Handler han = new Handler();
		han.postAtTime(run, 100);
		bar.stopProgress();
		mlistView.setOnScrollListener(this);
		bt_main_text.setOnClickListener(this);
	}

	/** listview */
	private void onSetListener() {
		mlistView.setMode(Mode.BOTH);
		mlistView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				load_type = REFRESH_PULL;
				executorService.submit(new LoadDataThread());
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				load_type = PAGE_NEXT_LOAD;
				executorService.submit(new LoadDataThread());
			}
		});
	}
	public void okHttpClient() {
		
		
	}
	@SuppressWarnings("unused")
	private void volley_json_post() {
		RequestQueue queue = Volley.newRequestQueue(this);
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("phone", "13429667914");
		hashMap.put("key", "335adcc4e891ba4e4be6d7534fd54c5d");
		JSONObject jsonParams = new JSONObject(hashMap);
		String url = "http://apis.juhe.cn/mobile/get?";
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
				url, jsonParams, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Toast.makeText(MainActivity.this, response.toString(),
								Toast.LENGTH_SHORT).show();
						System.out.println("json_post==" + response.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(MainActivity.this, error.toString(),
								Toast.LENGTH_SHORT).show();
					}

				});
		queue.add(request);
	}

	/***
	 * post 请求
	 */
	@SuppressWarnings("unused")
	private void volley_post() {
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = "http://ip.taobao.com/service/getIpInfo.php";
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String result) {
						Toast.makeText(MainActivity.this, result,
								Toast.LENGTH_LONG).show();
						System.out.println("postString==" + result);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {

					}
				}) {
			// post方式请求数据的参数传递
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("ip", "202.96.128.166");
				return hashMap;
			}
		};
		queue.add(request);
	}

	@SuppressWarnings("unused")
	private void volley_post1() {
		Api.HomeData(context, "1", listener, null);
	}

	Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, response,
					Toast.LENGTH_LONG).show();
			System.out.println("Volley==" + response);
		}
	};
	ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			System.out.println("VolleyError==" + error);
		}
	};
	void OkHttpPost(){}
	
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

			String jsonResult = request(ConfigResource.JOKEURLPIC, "page="
					+ pageIndex);
			System.out.println("MainActivityresult=====" + jsonResult);
			try {
				JSONObject object1 = new JSONObject(jsonResult);
				String showapi_res_body = object1.getString("showapi_res_body");
				JSONObject object2 = new JSONObject(showapi_res_body);
				String data = object2.getString("contentlist");
				JSONArray array = new JSONArray(data);
				for (int i = 0; i < array.length(); i++) {
					MainInfo info = new MainInfo();
					String ct = array.getJSONObject(i).getString("ct");
					String img = array.getJSONObject(i).getString("img");
					String title = array.getJSONObject(i).getString("title");
					String type = array.getJSONObject(i).getString("type");
					info.ct = ct;
					info.img = img;
					String mak = img.substring(img.length() - 3, img.length());
					if ("430".equals(mak)) {

					}
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
					// adapter.clearItems();
					sendToastMessage(LOADING_NEXT_PAGE_ERROR);
				}
				// madapter = new MainAdapter(context, mlist);
				// mlistView.setAdapter(madapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
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
		pageIndex = 1;
	}

	/** 处理UI线程 */
	private Handler handler1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FIRST_LOAD:
				mlistView.onRefreshComplete();
				mlist = newmlist;
				madapter = new MainAdapter(mlist, context);
				mlistView.setAdapter(madapter);
				break;
			case PAGE_NEXT_LOAD:
				mlistView.onRefreshComplete();
				madapter.addItems(-1, newmlist);
				break;
			case REFRESH_PULL:
				mlistView.onRefreshComplete();
				mlistView.onRefreshComplete();
				mlist = newmlist;
				madapter = new MainAdapter(mlist, context);
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
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		// listview第一次载入时，两者都为-1
		boolean shouldAnimate = (mFirstVisibleItem != -1)
				&& (mLastVisibleItem != -1);
		// 滚动时最后一个item的位置
		int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
		if (shouldAnimate) {// 第一次不需要加载动画
			int indexAfterFist = 0;
			// 如果出现这种情况，说明是在向上scroll，如果scroll比较快的话，一次可能出现多个新的view，我们需要用循环
			// 去获取所有这些view，然后执行动画效果
			while (firstVisibleItem + indexAfterFist < mFirstVisibleItem) {
				View animateView = view.getChildAt(indexAfterFist);// 获取item对应的view
				doAnimateOne(animateView, false);
				indexAfterFist++;
			}

			int indexBeforeLast = 0;
			// 向下scroll, 情况类似，只是计算view的位置时不一样
			while (lastVisibleItem - indexBeforeLast > mLastVisibleItem) {
				View animateView = view.getChildAt(lastVisibleItem
						- indexBeforeLast - firstVisibleItem);
				doAnimateOne(animateView, true);
				indexBeforeLast++;
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_main_text:
			// AppManager.getActivity(JokeurlTextAty.class);
			startActivity(new Intent(context, JokeurlTextAty.class));
			((Activity) context).overridePendingTransition(
					R.anim.slide_bottom_in, R.anim.scale_out);
			break;

		default:
			break;
		}
	}
	
}
