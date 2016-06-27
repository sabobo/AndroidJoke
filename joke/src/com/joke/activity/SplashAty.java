package com.joke.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.joke.MainActivity;
import com.joke.R;
import com.joke.widget.PathTextView;
import com.joke.widget.RoundedRectangleTextView;
/**
 * 欢迎界面
 * @author liubo
 *
 */
public class SplashAty  extends Activity {
	/***/
	private Context context;
	/***/
	private PathTextView mPathTextView;
	/***/
	private RoundedRectangleTextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stubv   
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_splash);
		mPathTextView = (PathTextView)findViewById(R.id.path);
		String sa = "JOKE CANT NO";
		mPathTextView.init(sa);
		textView = (RoundedRectangleTextView)findViewById(R.id.tv_sp_text);
		context = getApplicationContext();
		textView.setText("笑话");
		textView.strokeColor(Color.RED).textColor(Color.WHITE).update();
		//AdManager.getInstance(SplashAty.this).init("548333a785687b1d", "d12024bf4599f4bc",true);
		startView();
	}
	/**
	 * 线程启动视图
	 */
	private void startView(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try { 
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block LoginAty
					e.printStackTrace();
				}
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		}).start();
	}
//	/**
//	 * 退出时回收资源
//	 */
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//
//		// （可选）注销积分监听
//		// 如果在onCreate调用了PointsManager.getInstance(this).registerNotify(this)进行积分余额监听器注册，那这里必须得注销
//		PointsManager.getInstance(this).unRegisterNotify((PointsChangeNotify) this);
//
//		// （可选）注销积分订单赚取监听
//		// 如果在onCreate调用了PointsManager.getInstance(this).registerPointsEarnNotify(this)
//		// 进行积分订单赚取监听器注册，那这里必须得注销
//		PointsManager.getInstance(this).unRegisterPointsEarnNotify((PointsEarnNotify) this);
//
//		// 回收积分广告占用的资源
//		OffersManager.getInstance(this).onAppExit();
//	}
//	@Override
//	public void onPointEarn(Context arg0, EarnPointsOrderList list) {
//		// 遍历订单
//		for (int i = 0; i < list.size(); ++i) {
//			EarnPointsOrderInfo info = list.get(i);
//			Toast.makeText(this, info.getMessage(), Toast.LENGTH_LONG).show();
//		}
//	}
//	@Override
//	public void onPointBalanceChange(float arg0) {
//		// TODO Auto-generated method stub
//		
//	}
}