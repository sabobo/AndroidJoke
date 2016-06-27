package com.joke.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
/**
 * the tool for net status
 * @author Eilin.Yang
 *2013-3-26
 */
public class NetWorkUtils {
	/**
	 * 判断网络是否连接
	 * 
	 * @param context 场景
	 * @return true:网络已经连接;false:网络断开
	 */
	public static boolean checkNetWork(Context context) {
		// 获得手机所有连接管理对象（包括对wi-fi等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获得网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {					
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 判断是否是3G或wifi
	 * @param context 场景
	 * @return true:3G或wifi
	 */
	public static boolean isWifiOr3G(Context context) {
		// 获得手机所有连接管理对象（包括对wi-fi等连接的管理）
		boolean flag = false;
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获得网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					int typeP = info.getType();
					int typeS = info.getSubtype();
					flag = isConnectionFast(typeP, typeS);
				}
			}
		} catch (Exception e) {
		}
		return flag;
	}
	
	/**
	 * 判断是否是wifi
	 * @param context 上下文
	 * @return true: is wifi;false:not wifi
	 */
	public static boolean isWifi(Context context) {
		// 获得手机所有连接管理对象（包括对wi-fi等连接的管理）
		boolean flag = false;
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获得网络连接管理的对象
				NetworkInfo wifi = connectivity
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifi != null && wifi.isConnected()) {
					flag = true;
				}
			}
		} catch (Exception e) {
		}
		return flag;
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context 场景
	 * @return 返回网路类型的名称
	 */
	public static String getNetWorkType(Context context) {
		if (!checkNetWork(context)) {
			return "没有可用的网络";
		} else {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetInfo = connectivityManager
					.getActiveNetworkInfo();
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			StringBuffer sb = new StringBuffer();
			if (activeNetInfo != null) {
				sb.append(activeNetInfo.getTypeName());
			}
			if (mobNetInfo != null) {
				sb.append(" and " + mobNetInfo.getTypeName());
			}
			return sb.toString();
		}
	}

	/**
	 * 计算网速
	 * @return
	 */
	public static long getNetworkSpeed() {
		// TODO Auto-generated method stub
		ProcessBuilder cmd;
		long readBytes = 0;
		BufferedReader rd = null;
		try {
			String[] args = { "/system/bin/cat", "/proc/net/dev" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			rd = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			int linecount = 0;
			while ((line = rd.readLine()) != null) {
				linecount++;
				if (line.contains("wlan0") || line.contains("eth0")) {
					// L.E("获取流量整条字符串",line);
					String[] delim = line.split(":");
					if (delim.length >= 2) {
						String[] numbers = delim[1].trim().split(" ");// 提取数据
						readBytes = Long.parseLong(numbers[0].trim());//
						break;
					}
				}
			}
			rd.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return readBytes;
	}

	/**
	 * 判断是否是3G或者wifi
	 * @param type 网路类型
	 * @param subType 子网类型
	 * @return true:is 3G or wifi;false:is not 3G or wifi
	 */
	private static boolean isConnectionFast(int type, int subType) {
		if (type == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return false; // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return true; // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return true; // ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return false; // ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return true; // ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return true; // ~ 700-1700 kbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return true; // ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return true; // ~ 400-7000 kbps
				// NOT AVAILABLE YET IN API LEVEL 7
			case TelephonyManager.NETWORK_TYPE_EHRPD:
				return true; // ~ 1-2 Mbps
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				return true; // ~ 5 Mbps
//			case TelephonyManager.NETWORK_TYPE_HSPAP:
//				return true; // ~ 10-20 Mbps
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return false; // ~25 kbps
			case TelephonyManager.NETWORK_TYPE_LTE:
				return true; // ~ 10+ Mbps
				// Unknown
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;
			default:
				return false;
			}
		} else {
			return false;
		}
	}
}
