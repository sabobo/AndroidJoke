package com.joke;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.joke.config.ConfigResource;
import com.joke.widget.CustomProgressDialog;

public class BaseActivity extends Activity{
	
	private static BaseActivity mInstance = null;
	/****/
	private Context context;
	/***/
	private CustomProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加Activity到堆栈
		// AppManager.getAppManager().addActivity(this);
		mInstance = this;
		context = this;
	}
	protected void start() {
		if (progressDialog == null){
            progressDialog = CustomProgressDialog.createDialog(context);
            //progressDialog.setMessage("正在加载中...");
        }
         
        progressDialog.show();
	}

	protected void succeed(String result) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	protected void failed(String object){
		if(progressDialog!=null && progressDialog.isShowing()){  
            progressDialog.dismiss();  
        } 
	}
	/**
	 * 实力化的方法
	 * 
	 * @return 返回自定义Application对象
	 */
	public static BaseActivity getInstance() {
		return mInstance;
	}
	// 自定义toast控件
	public void showToast(String str) {
		Toast toast = Toast.makeText(getApplicationContext(), str, 500);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg;
	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  ConfigResource.APIKEY);
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	/**
	 * 开始动画列表
	 * @param view 
	 */
	public static void doAnimateOne(View view, boolean scrollDown){
		if(null == view){
			return;
		}
        //我们这里先写一个最简单地动画，GROW
        ViewPropertyAnimator animator = view.animate().setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator());

        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight()/2);
        view.setScaleX(0.01f);
        view.setScaleY(0.01f);

        animator.scaleX(1.0f).scaleY(1.0f);
        animator.start();
    }
	/**
	 * 开始动画列表
	 * @param view
	 * @param scrollDown
	 */
	public static void doAnimateTwo(View view, boolean scrollDown){
        //我们这里先写一个最简单地动画，GROW
		if(null == view){
			return;
		}
		 ViewPropertyAnimator animator = view.animate().setDuration(500)
			        .setInterpolator(new AccelerateDecelerateInterpolator());

			view.setPivotX(view.getWidth()/2);
			view.setPivotY(view.getHeight()/2);

			view.setRotationX(90);
			view.setTranslationY(scrollDown? view.getHeight(): -view.getHeight());
			animator.rotationX(0);
			animator.translationY(0);

			animator.start();
    }
	/**
	 * 开始动画列表
	 * @param view
	 * @param scrollDown
	 */
	public static void doAnimateThree(View view, boolean scrollDown){
        //我们这里先写一个最简单地动画，GROW
		if(null == view){
			return;
		}
        ViewPropertyAnimator animator = view.animate().setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator());
        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight()/2);

        view.setRotationX(90 * (scrollDown? -1:1));
        animator.rotationX(0);
        animator.start();
    }
	/**
	 * 开始动画列表
	 * @param view 309102
	 * @param scrollDown  
	 */ 
	public static void doAnimateFour(View view, boolean scrollDown){
        //我们这里先写一个最简单地动画，GROW
		if(null == view){
			return; 
		}	
        ViewPropertyAnimator animator = view.animate().setDuration(500)
        .setInterpolator(new AccelerateDecelerateInterpolator());
        
        view.setPivotX(0);
        view.setPivotY(view.getHeight());
        view.setRotation(70 * (scrollDown? 1: -1));
        animator.rotation(0);

        animator.scaleX(1.0f).scaleY(1.0f);
        animator.start();
    }
	
}
