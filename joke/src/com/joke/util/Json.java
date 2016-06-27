package com.joke.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * json 解析
 * @author liubo
 *
 */
public class Json {
	
	/**
	 * 获取返回的判别值
	 * @param result
	 * @return code
	 * @throws JSONException
	 */
	public String resultJsonCode (String result) throws JSONException{
		String code="";
		JSONObject jsonObject = new JSONObject(result);
		code = jsonObject.getString("code");
		return code;
		
	}
	/**
	 * 获取返回的判别值
	 * @param result
	 * @return msg
	 * @throws JSONException
	 */
	public String resultJsonMsg (String result) throws JSONException{
		String msg="";
		JSONObject jsonObject = new JSONObject(result);
		msg = jsonObject.getString("msg");
		return msg;
	}
	/**
	 * 获取返回的判别值
	 * @param result
	 * @return data
	 * @throws JSONException
	 */
	public String resultJsonData (String result) throws JSONException{
		String data="";
		JSONObject jsonObject = new JSONObject(result);
		data = jsonObject.getString("data");
		return data;
	}
	/**
	 * 获取返回的总记录数
	 * @param data
	 * @return num
	 * @throws JSONException
	 */
	public String resultJsonNum (String data) throws JSONException{
		String num="";
		JSONObject jsonObject = new JSONObject(data);
		num = jsonObject.getString("num");
		return num;
	}
	/**
	 * 获取返回的结果集
	 * @param data
	 * @return orderList
	 * @throws JSONException
	 */
	public String resultJsonOrderList (String data) throws JSONException{
		String orderList="";
		JSONObject jsonObject = new JSONObject(data);
		orderList = jsonObject.getString("orderList");
		return orderList;
	}
	
	/**
	 * 登录数据解析
	 * @param string
	 * @return
	 * @throws JSONException
	 *//*
	public List<LoginBean> loginJson(String string) throws JSONException{
		List<LoginBean> bean=new ArrayList<LoginBean>();
		LoginBean bean2 = new LoginBean();
		String data = resultJsonData(string);
			if(!StringUtils.isEmpty(data)){
				JSONObject json = new JSONObject(data);
				JSONArray jsonarray = new JSONArray();
				jsonarray.put(0, json);
				  int nn = jsonarray.length();
                  if (nn > 0) {
                	  for (int i = 0; i < nn; i++) {
                		  bean2 = getLogin(jsonarray.getJSONObject(i));
                		  if (bean != null) {
                			  bean.add(bean2);
                          }
                	  }
                  }
			}
		return bean;
		
	}
	 *//**
	  * 获取登录数据
	  * 
	  * @throws JSONException
	  *//*
	 private LoginBean getLogin(JSONObject jsonObject) throws JSONException {
		 LoginBean bean=new LoginBean();
	     if (null !=jsonObject) {
	    	 bean.setMobile(jsonObject.getString("name")) ;
	    	 bean.setShopno(jsonObject.getString("shopno"));
	     }
	     return bean;
	 }*/
	
}
