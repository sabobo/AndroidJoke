package com.joke.util;


/**
 * 解析工具类
 * 
 * @author kymjs
 * 
 */
public class Parser {
	/**
     * 首页博客列表
     * 
     * @param json
     * @return
     *//*
    public static List<Blog> getBlogList(String json) {
        List<Blog> datas = new ArrayList<Blog>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                Blog data = new Blog();
                JSONObject obj = jsonArray.getJSONObject(i);
                data.setId(obj.optString("id", "-1"));
                data.setTitle(obj.optString("title", "无题"));
                data.setUrl(obj.optString("url", "http://blog.kymjs.com/"));
                data.setImageUrl(obj.optString("imageUrl", ""));
                data.setDate(obj.optString("date", "未知时间"));
                data.setIsRecommend(obj.optInt("isRecommend", 0));
                data.setAuthor(obj.optString("author", "佚名"));
                data.setIsAuthor(obj.optInt("isAuthor", 0));
                data.setDescription(obj.optString("description", "暂无简介"));
                datas.add(data);
            }
        } catch (JSONException e) {
            Log.e("kymjs", "getBlogList()解析异常");
        }
        return datas;
    }*/
}
