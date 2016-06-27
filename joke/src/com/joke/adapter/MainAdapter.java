package com.joke.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joke.R;
import com.joke.info.MainInfo;
import com.joke.util.Tool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

@SuppressLint("InflateParams") 
public class MainAdapter extends BaseAdapter {
	/***/
	private Context context;
	/***/
	private List<MainInfo> mlist = new ArrayList<MainInfo>();
	/****/
	private LayoutInflater inflater;
	/** Asynchronous loading */
	private DisplayImageOptions options;
	
	@SuppressWarnings({ "unused", "deprecation" })
	public MainAdapter(List<MainInfo> list, Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.mlist = list;
//		options = new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.ico_notfind)
//		.showImageForEmptyUri(R.drawable.ico_notfind)
//		.showImageOnFail(R.drawable.ico_notfind)
//		.resetViewBeforeLoading(true).cacheOnDisk(true)
//		.imageScaleType(ImageScaleType.EXACTLY)
//		.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
//		.displayer(new FadeInBitmapDisplayer(300)).cacheInMemory(false)
//		.cacheOnDisc(true).build();
	}

	 public List<MainInfo> getList(){
	 		return mlist;
	 	}
	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
   
	
	 public void clearItems() {
	        if (mlist.size()>0) {
	            mlist.clear();
	        }
	    }

	    public void addItems(int position,List<MainInfo> list) {
	        if (position>getCount()||position<0) {
	        	this.mlist = list;
	        }else {
	            this.mlist.addAll(position, list);
	        }
	        notifyDataSetChanged();
	    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_main_item, null);
		}
		ImageView img = Tool.ViewHolder.get(convertView, R.id.iv_main_item_img);
		TextView  title = Tool.ViewHolder.get(convertView, R.id.iv_main_item_title);
		TextView  time = Tool.ViewHolder.get(convertView, R.id.iv_main_item_time);
		title.setText(mlist.get(position).title);
		time.setText(mlist.get(position).ct); 
		//Uri uri = Uri.parse(list.get(position).img);SimpleDraweeView
		//img.setImageURI(uri); center_crop
		//ImageLoader.getInstance().displayImage(mlist.get(position).img,img, options);
		
		//Picasso.with(context).load(mlist.get(position).img)
		//.placeholder(R.drawable.ico_notfind)
		//.error(R.drawable.ico_notfind)
		//.into(img);
		 Glide.with(context).load(mlist.get(position).img).centerCrop()
         .placeholder(R.drawable.image_loading).crossFade().into(img);
		
		
		return convertView;
	}


}
