package com.joke.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.joke.R;

public class MyRatingBar extends ImageView { 
	/**
	 * 未选中时显示的图标资源id
	 */
	private int unselectIconId;
	/**
	 * 选中后显示的图标资源id
	 */
	private int selectIconId;
	/**
	 * 未选中时显示的图标
	 */
	private Bitmap unSelectIcon;
	/**
	 * 选中后显示的图标
	 */
    private Bitmap selectedIcon;
	/**
	 * 星星数量
	 */
	private int starNumber;
	/**
	 * 评星之间的间隔距离
	 */
	private int starMargin;
	/**
	 * 默认星级
	 */
	private float defaultRating;
	/**
	 * 星级变化监听器
	 */
	private OnRatingBarChangeListener onRatingBarChangeListener;
	
	public MyRatingBar(Context context) {
		super(context); 
		//System.out.println("getWidth() = "+getWidth() +" getMeasuredWidth()= "+getMeasuredWidth()+" getHeight()= "+getHeight()+" getMeasuredHeight()= "+getMeasuredHeight());
	}
	public MyRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		//System.out.println("getWidth() = "+getWidth() +" getMeasuredWidth()= "+getMeasuredWidth()+" getHeight()= "+getHeight()+" getMeasuredHeight()= "+getMeasuredHeight());
		
		
		 TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyRatingBar);
		
	    int indexCount = ta.getIndexCount(); 
	   // System.out.println("indexCount = "+indexCount);
		for(int i =0 ;i<indexCount ;i++){
	    	int itemId = ta.getIndex(i);
	    	switch (itemId) { 
			case R.styleable.MyRatingBar_select_icon:
				selectIconId = ta.getResourceId(itemId, R.drawable.ic_launcher);
				//Bitmap img = BitmapFactory.decodeResource(getResources(), resId); 
				//setImageBitmap(img);
				
				//Drawable d = ta.getDrawable(itemId);
				//setImageDrawable(d);
				break;
			case R.styleable.MyRatingBar_unselect_icon:
				unselectIconId = ta.getResourceId(itemId, R.drawable.ic_launcher);
				//Bitmap b = BitmapFactory.decodeResource(getResources(), testId);
				//setImageBitmap(b);
				
				//Drawable d1 = ta.getDrawable(itemId);
				//setImageDrawable(d1);
				break;
			case R.styleable.MyRatingBar_star_number:
				starNumber = ta.getInt(itemId, -1);
				//System.out.println("starNumber==="+starNumber);
				break;
			case R.styleable.MyRatingBar_star_margin:
			    starMargin = ta.getDimensionPixelSize(itemId, 0);
			    //System.out.println("starMargin === "+starMargin); 
				break;
			case R.styleable.MyRatingBar_default_rating:
				defaultRating = ta.getFloat(itemId, 0);
				//System.out.println("defaultRating = "+defaultRating);
				break;
			default:
				break;
			}
	    }
		//System.out.println("----------------------------------------------");
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) { 
		super.onLayout(changed, left, top, right, bottom);
		//System.out.println("---onLayout   getWidth() = "+getWidth() +" getMeasuredWidth()= "+getMeasuredWidth()+" getHeight()= "+getHeight()+" getMeasuredHeight()= "+getMeasuredHeight());
	    prepareIcon(defaultRating);
	}
	/**
	 * 准备相关的状态图标
	 * @param rating 指定的星级指数
	 */
	public void prepareIcon(float rating){
		if(rating > starNumber){
			rating = starNumber;
		}
		if(rating < 0){
			rating = 0;
		}
		rating = rating / starNumber ;
		//System.out.println("prepareIcon rating="+rating);
		unSelectIcon = getBitmap(unselectIconId);//BitmapFactory.decodeResource(getResources(), unselectIconId);//
		selectedIcon = getBitmap(selectIconId); //BitmapFactory.decodeResource(getResources(), selectIconId);//
		int width = getWidth() == 0 ? unSelectIcon.getWidth() : getWidth();
		int height = getHeight() == 0 ? unSelectIcon.getHeight() : getHeight();
		Bitmap result = Bitmap.createBitmap(width , height , Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		Bitmap b1 = Bitmap.createBitmap(selectedIcon, 0, 0, (int) (selectedIcon.getWidth()*rating), selectedIcon.getHeight());
		int left = (int) (unSelectIcon.getWidth()*rating);
		Bitmap b2 = Bitmap.createBitmap(unSelectIcon, left, 0,(int) (unSelectIcon.getWidth()-left), unSelectIcon.getHeight());
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		canvas.drawBitmap(b1, 0, 0, paint );
		canvas.drawBitmap(b2, (int) (unSelectIcon.getWidth()*rating), 0, paint);
		setImageBitmap(result);
	}
	/**
	 * 由图片Id获取单位星星图标，并做相应绽放自适应处理
	 * @param imageId 指定的图标Id
	 * @return
	 */
	protected Bitmap getBitmap(int imageId) {
		Bitmap b = BitmapFactory.decodeResource(getResources(), imageId);
		int width = getWidth() == 0 ? b.getWidth() : (getWidth()-((starNumber-1)*starMargin))/starNumber;
		Bitmap b1 = zoomImg(b, width);
		
		int height = getHeight() == 0 ? b.getHeight() : getHeight();
		Bitmap result = Bitmap.createBitmap(getWidth(), height , b.getConfig());
		Canvas canvas = new Canvas(result);
		Paint paint = new Paint();
		for(int i = 0 ; i < starNumber ; i++ ){
			int left = width * i + starMargin*i; 
			
			canvas.drawBitmap(b1, left, (height - b1.getHeight())/2, paint );
		}
		return result;
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//System.out.println("----ACTION_DOWN----");
			break;
        case MotionEvent.ACTION_MOVE: 
        	float x = event.getX();
			float f = x / (getWidth()/starNumber); 
        	String value = String.format("%.1f", f);
        	f = Float.valueOf(value);
        	if(f < 0){
        		f = 0;
        	}
        	if(f > starNumber){
        		f = starNumber;
        	}
        	if(onRatingBarChangeListener != null){
        		onRatingBarChangeListener.onRatingChanged(this, f);
        	}
        	//System.out.println("----ACTION_MOVE----"+value+"  f==="+f);
        	move((int)x);
			break;
        case MotionEvent.ACTION_UP:
        	//System.out.println("----ACTION_UP----");
			break;
		default:
			break;
		}
		return true;//super.dispatchTouchEvent(event);
	}
	/**
	 * 根据滑动坐标来更新显示状态
	 * @param x X坐标值
	 */
	private void move(int x) {
		if(x <= 0 || x >= getWidth()){
			return;
		}
		Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		Bitmap b1 = Bitmap.createBitmap(selectedIcon, 0, 0, x, result.getHeight());
		Bitmap b2 = Bitmap.createBitmap(unSelectIcon, x, 0, result.getWidth() - x, result.getHeight());
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		canvas.drawBitmap(b1, 0, 0, paint );
		canvas.drawBitmap(b2, x, 0, paint);
		setImageBitmap(result);
	}
	/** 
	 *  处理图片  
	 * @param bm 所要转换的bitmap 
	 * @param newWidth新的宽 
	 * @param newHeight新的高   
	 * @return 指定宽高的bitmap 
	 */ 
	 public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){   
	    // 获得图片的宽高   
	    int width = bm.getWidth();   
	    int height = bm.getHeight();   
	    // 计算缩放比例   
	    float scaleWidth = ((float) newWidth) / width;   
	    float scaleHeight = ((float) newHeight) / height;   
	    // 取得想要缩放的matrix参数   
	    Matrix matrix = new Matrix();   
	    matrix.postScale(scaleWidth, scaleHeight);   
	    // 得到新的图片   www.2cto.com
	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);   
	    return newbm;   
	}  
	 /** 
		 *  处理图片  
		 * @param bm 所要转换的bitmap 
		 * @param newWidth新的宽 
		 * @param newHeight新的高   
		 * @return 指定宽高的bitmap 
		 */ 
		 public static Bitmap zoomImg(Bitmap bm, int newWidth){   
		    // 获得图片的宽高   
		    int width = bm.getWidth();   
		    int height = bm.getHeight();   
		    // 计算缩放比例   
		    float scaleWidth = ((float) newWidth) / width;      
		    // 取得想要缩放的matrix参数   
		    Matrix matrix = new Matrix();   
		    matrix.postScale(scaleWidth, scaleWidth);   
		    // 得到新的图片   www.2cto.com
		    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);   
		    return newbm;   
		}
		 
	
	public void setOnRatingBarChangeListener(
			OnRatingBarChangeListener onRatingBarChangeListener) {
		this.onRatingBarChangeListener = onRatingBarChangeListener;
	}

    /**
     * 星级变动监听器
     * @author 1
     *
     */
	public interface OnRatingBarChangeListener{
    	public void onRatingChanged(MyRatingBar ratingBar, float rating);
    }
}
