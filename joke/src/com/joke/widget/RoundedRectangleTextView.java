package com.joke.widget;

import android.R.color;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.joke.R;

public class RoundedRectangleTextView extends TextView {

    private int strokeColor;
    private int strokeWidth;
    private int solidColor;
    private int radius;
    private int textColor;

    private GradientDrawable drawable;

    public RoundedRectangleTextView(Context context) {
        this(context, null);
    }

    public RoundedRectangleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        strokeWidth = getResources().getDimensionPixelSize(R.dimen.divider) * 2;  
        solidColor = getResources().getColor(color.transparent);  
        radius = getResources().getDimensionPixelSize(R.dimen.dimen_3dp);  
        textColor = getResources().getColor(color.black);  

        drawable = new GradientDrawable();
        update();
    }

    public void changeWholeColorByResId(int strokeColorResId, int strokeWidthResId, int solidColorResId, int radiusResId, int textColorResId) {
        strokeColor(getResources().getColor(strokeColorResId)).
                strokeWidth(getResources().getDimensionPixelSize(strokeWidthResId)).
                solidColor(getResources().getColor(solidColorResId)).
                radius(getResources().getDimensionPixelSize(radiusResId)).
                textColor(getResources().getColor(textColorResId)).invalidate();
    }

    @SuppressWarnings("deprecation")
	private void changeWholeColor(int strokeColor, int strokeWidth, int solidColor, int radius, int textColor) {
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setCornerRadius(radius);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(solidColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
        setTextColor(textColor);
    }

    public RoundedRectangleTextView strokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public RoundedRectangleTextView strokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public RoundedRectangleTextView solidColor(int solidColor) {
        this.solidColor = solidColor;
        return this;
    }

    public RoundedRectangleTextView radius(int radius) {
        this.radius = radius;
        return this;
    }

    public RoundedRectangleTextView textColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public void update(){
        changeWholeColor(strokeColor, strokeWidth, solidColor, radius, textColor);
    }
}