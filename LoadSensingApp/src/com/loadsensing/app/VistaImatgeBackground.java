package com.loadsensing.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class VistaImatgeBackground extends View {

	public VistaImatgeBackground(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public VistaImatgeBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public VistaImatgeBackground(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo);
		canvas.drawBitmap(myBitmap, 0, 0, null);

	}

}
