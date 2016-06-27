package com.joke.net;

import com.joke.widget.CustomProgressDialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class HttpHandler extends Handler {
	private Context context;
	private CustomProgressDialog progressDialog;

	public HttpHandler(Context context) {
		this.context = context;
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

	protected void otherHandleMessage(Message message) {
		
	}
	@Override
	public void handleMessage(Message message){
		switch (message.what) {
		case HttpConnectionUtils.DID_START:
			//Log.d(context.getClass().getSimpleName(), "http connection start...");
			start();
			break;
		case HttpConnectionUtils.DID_SUCCEED:
			progressDialog.dismiss();
			String response = (String)message.obj;
			Log.d(context.getClass().getSimpleName(), "http connection return." + response);
			try {
				String object = new String(response == null ?"" :response.trim());
				if(!object.equals("")||!object.equals(null)){
					//Toast.makeText(context, "operate successd:", Toast.LENGTH_SHORT).show();
					succeed(object);
				}else {
					//Toast.makeText(context, "operate fialed:",Toast.LENGTH_SHORT).show();
					failed(object);
				}
			} catch (Exception  e) {
				 if(progressDialog!=null && progressDialog.isShowing()){  
	                    progressDialog.dismiss();  
	             }
				 e.printStackTrace();
				 //Toast.makeText(context, "Response data is not json data",Toast.LENGTH_LONG).show();  
			}
			break;
		case HttpConnectionUtils.DID_ERROR:
			 if(progressDialog!=null && progressDialog.isShowing()){  
	                progressDialog.dismiss();  
	            }
			 Exception e = (Exception)message.obj;
			 e.printStackTrace();
			 Log.e(context.getClass().getSimpleName(), "connection fail."+e.getMessage());
			 //Toast.makeText(context, "connection fail,please check connection!", Toast.LENGTH_LONG).show();
			 break;
		default:
			break;
		}
		otherHandleMessage(message);
	}
}
