package com.joke.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joke.util.MD5Utils;


public class Api {

	
	 public static void  HomeData(Context context, final String agent_id,
			 Listener<String>	listener, ErrorListener errorListener) {
		 	RequestQueue queue = Volley.newRequestQueue(context);
			String url = "http://gongnaer.com/Api/Api/getIndexData";
			StringRequest stringRequest = new StringRequest(Method.POST, url,
					listener,errorListener) {
				@Override
				protected Response<String> parseNetworkResponse(
						NetworkResponse response) {
					try {
						String jsonString = new String(response.data, "UTF-8");
						return Response.success(jsonString,
								HttpHeaderParser.parseCacheHeaders(response));
					} catch (UnsupportedEncodingException e) {
						return Response.error(new ParseError(e));
					} catch (Exception je) {
						return Response.error(new ParseError(je));
					}
				}
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					String tk = "";
					String m = String.valueOf(new Date().getTime());// 当前时间戳
					String str = m.substring(m.length() - 5, m.length());// 当前时间戳后五位
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("agent_id", agent_id);
					try {
						tk = MD5Utils.getSignature(map, m, str);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Map<String, String> mapm = new HashMap<String, String>();
					mapm.put("agent_id", agent_id);
					mapm.put("tk", tk);
					mapm.put("t", m);
					return mapm;
				}
			};
			queue.add(stringRequest);
		 
	 }
	 
	 
	
}
