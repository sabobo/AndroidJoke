package com.joke.adapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joke.R;
import com.joke.info.JokeurlTextInfo;
import com.joke.util.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

@SuppressLint("InflateParams") 
public class JokeurlTextAdapter extends BaseAdapter {
	/***/
	private Context context;
	/***/
	private List<JokeurlTextInfo> list = new ArrayList<JokeurlTextInfo>();
	/****/
	private LayoutInflater inflater;
	/** Asynchronous loading */
	private DisplayImageOptions options;
	
	@SuppressWarnings({ "unused", "deprecation" })
	public JokeurlTextAdapter(List<JokeurlTextInfo> list, Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ico_notfind)
		.showImageForEmptyUri(R.drawable.ico_notfind)
		.showImageOnFail(R.drawable.ico_notfind)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300)).cacheInMemory(false)
		.cacheOnDisc(true).build();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	 public void clearItems() {
	        if (list.size()>0) {
	            list.clear();
	        }
	    }

	    public void addItems(int position,List<JokeurlTextInfo> list) {
	        if (position>getCount()||position<0) {
	        	this.list = list;
	        }else {
	            this.list.addAll(position, list);
	        }
	        notifyDataSetChanged();
	    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_jokeurltext_item, null);
		}
		TextView text = Tool.ViewHolder.get(convertView, R.id.iv_jokeurltext_item_text);
		TextView  title = Tool.ViewHolder.get(convertView, R.id.iv_jokeurltext_item_title);
		TextView  time = Tool.ViewHolder.get(convertView, R.id.iv_jokeurltext_item_time);

		ImageGetter imgGetter = new Html.ImageGetter() {
	        public Drawable getDrawable(String source) {
	              Drawable drawable = null;
	              URL url;
	              try {
	                  url = new URL(source);
	                  drawable = Drawable.createFromStream(url.openStream(), "");
	              } catch (Exception e) {
	                  e.printStackTrace();
	                  return null;
	              }
	             // drawable = Drawable.createFromPath(source); //显示本地图片
	              drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
	              return drawable; 
	        }
	};
		
		Spanned textstr = Html.fromHtml(list.get(position).text, imgGetter, null);
		text.setText(textstr);
		title.setText(list.get(position).title);
		time.setText(list.get(position).ct);
		return convertView;
	}


}
