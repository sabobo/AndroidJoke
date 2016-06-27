package com.joke.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class MD5Utils {

	/**
	 * MD5加码32位
	 * 
	 * @param inStr
	 * @return 32位
	 */
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (null == inStr) {
			return inStr;
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	/**
	 * MD5加码16位
	 * 
	 * @param inStr
	 *            字符
	 * @param isfront
	 *            是否前16位,true:前16位，false：后16位
	 * @return
	 */
	public static String MD5_16(String inStr, boolean isfront) {
		String encrypt = MD5(inStr);
		if (null == encrypt || encrypt.length() == 0) {
			return null;
		}
		if (!isfront) {
			return encrypt.substring(16);
		}
		return encrypt.substring(0, 16);
	}

	/**
	 * 可逆的加密算法
	 * 
	 * @param inStr
	 * @param key
	 *            密钥
	 * @return
	 */
	public static String encoding(String inStr, char key) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ key);
		}
		return new String(a);
	}

	/**
	 * 加密后解密
	 * 
	 * @param inStr
	 * @param key
	 *            密钥
	 * @return
	 */
	public static String decoding(String inStr, char key) {
		return encoding(inStr, key);
	}

	/**
	 * 将unicode 字符串
	 * 
	 * @param str
	 *            待转字符串
	 * @return 普通字符串
	 */
	public static String revert(String str) {
		str = (str == null ? "" : str);
		if (str.indexOf("\\u") == -1)// 如果不是unicode码则原样返回
			return str;

		StringBuffer sb = new StringBuffer(1000);

		for (int i = 0; i < str.length() - 6;) {
			String strTemp = str.substring(i, i + 6);
			String value = strTemp.substring(2);
			int c = 0;
			for (int j = 0; j < value.length(); j++) {
				char tempChar = value.charAt(j);
				int t = 0;
				switch (tempChar) {
				case 'a':
					t = 10;
					break;
				case 'b':
					t = 11;
					break;
				case 'c':
					t = 12;
					break;
				case 'd':
					t = 13;
					break;
				case 'e':
					t = 14;
					break;
				case 'f':
					t = 15;
					break;
				default:
					t = tempChar - 48;
					break;
				}

				c += t * ((int) Math.pow(16, (value.length() - j - 1)));
			}
			sb.append((char) c);
			i = i + 6;
		}
		return sb.toString();
	}

	/**
	 * 将字符串转成unicode
	 * 
	 * @param str
	 *            待转字符串
	 * @return unicode字符串
	 */
	public static String convert(String str) {
		str = (str == null ? "" : str);
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8); // 取出高8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			j = (c & 0xFF); // 取出低8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);

		}
		return (new String(sb));
	}

	/**
	 * 签名生成算法
	 * 
	 * @param HashMap
	 * @param <String,String> params 请求参数集，所有参数必须已转换为字符串类型
	 * @param String
	 * @param           secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(HashMap<String, String> params, String t,
			String f) throws IOException {
		// 先将参数以其参数名的字典序升序进行排序
		Map<String, String> sortedParams = new TreeMap<String, String>(params);
		Set<Entry<String, String>> entrys = sortedParams.entrySet();

		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder basestring = new StringBuilder();
		for (Entry<String, String> param : entrys) {
			basestring.append("&").append(param.getKey()).append("=")
					.append(param.getValue());
		}
		basestring.append("&").append("t").append("=").append(t);
		//basestring.append(f);
		
		String sda = basestring.substring(1, basestring.length());
		System.out.println("排序后的参数=="+sda);
		String ds = encryption(encryption(sda) + f);
		System.out.println("排序加密后的参数=="+ds);
		return ds.toString();
	}

	/**
	 * Get XML String of utf-8
	 * 
	 * @return XML-Formed string
	 */
	public static String getUTF8XMLString(String xml) {
		// A StringBuffer Object
		StringBuffer sb = new StringBuffer();
		sb.append(xml);
		String xmString = "";
		String xmlUTF8 = "";
		try {
			xmString = new String(sb.toString().getBytes("UTF-8"));
			xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
			System.out.println("utf-8 编码：" + xmlUTF8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return to String Formed
		return xmlUTF8;
	}
	public final static String MD51(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/** 
     *  
     * @param plain  明文 
     * @return 32位小写密文 
     */ 
    public static String encryption(String plain) { 
        String re_md5 = new String(); 
        try { 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
            md.update(plain.getBytes()); 
            byte b[] = md.digest(); 
 
            int i; 
 
            StringBuffer buf = new StringBuffer(""); 
            for (int offset = 0; offset < b.length; offset++) { 
                i = b[offset]; 
                if (i < 0) 
                    i += 256; 
                if (i < 16) 
                    buf.append("0"); 
                buf.append(Integer.toHexString(i)); 
            } 
 
            re_md5 = buf.toString(); 
 
        } catch (NoSuchAlgorithmException e) { 
            e.printStackTrace(); 
        } 
        return re_md5; 
    } 
 

	
	

}
