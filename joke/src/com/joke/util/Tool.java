package com.joke.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joke.R;

public class Tool {
	/**
	 * ViewHolder 公共的方法
	 * @author liubo
	 *
	 */
	public  static class ViewHolder {
	    @SuppressWarnings("unchecked")
		public static <T extends View> T get(View view, int id) {
	        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
	        if (viewHolder == null) {
	            viewHolder = new SparseArray<View>();
	            view.setTag(viewHolder);
	        }
	        View childView = viewHolder.get(id);
	        if (childView == null) {
	            childView = view.findViewById(id);
	            viewHolder.put(id, childView);
	        }
	        return (T) childView;
	    }
	}
	/**
	 * 将textview中的字符全角化
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		   char[] c = input.toCharArray();
		   for (int i = 0; i< c.length; i++) {
		       if (c[i] == 12288) {
		         c[i] = (char) 32;
		         continue;
		       }if (c[i]> 65280&& c[i]< 65375)
		          c[i] = (char) (c[i] - 65248);
		       }
		   return new String(c);
		}

	 public static void show(Context context, String text, boolean isLong) {
	        LayoutInflater inflater = LayoutInflater.from(context);
	        View layout = inflater.inflate(R.layout.toast_layout, null);

	        ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
	        image.setImageResource(R.drawable.xiaohualogin);

	        TextView textV = (TextView) layout.findViewById(R.id.toast_text);
	        textV.setText(text);

	        Toast toast = new Toast(context);
	        toast.setGravity(Gravity.CENTER, 0, 0);
	        toast.setDuration((isLong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
	        toast.setView(layout);
	        toast.show();
	    }
}
