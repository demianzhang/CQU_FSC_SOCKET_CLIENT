package com.example.cqu_fsc_socket_client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

public class OilView extends View implements Runnable {

	private float angel = 49;
	private Matrix matrix;
	private Bitmap needleBm;
	private Bitmap speedBm;
	private Bitmap bootBm;

	public OilView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public OilView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public OilView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	public void setRotate_degree(float degree)
	{	
		if(degree>312)
			degree=312;
		else if(degree<49)
			degree=49;
		angel = degree;
	}

	private void init() {
		matrix = new Matrix();

		needleBm = BitmapFactory.decodeResource(getResources(),
				R.drawable.cqu_handle);
		bootBm = BitmapFactory.decodeResource(getResources(),
				R.drawable.info_rt_base_needle_boot_);
		speedBm = BitmapFactory.decodeResource(getResources(),
				R.drawable.cqu_speed);
		new Thread(this).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		matrix.reset();
		canvas.drawBitmap(speedBm, 0, 0, null);
		
		matrix.preTranslate( speedBm.getWidth()/2-bootBm.getWidth()/2+6,
				speedBm.getHeight()/2 - bootBm.getHeight()/ 2);
		matrix.preRotate(angel, needleBm.getWidth()/2, needleBm.getHeight()/18);
		
		canvas.drawBitmap(needleBm, matrix, null);
		canvas.drawBitmap(bootBm, speedBm.getWidth() / 2 - bootBm.getWidth() / 2,
				speedBm.getHeight() / 2 - bootBm.getHeight() / 2, null);
	}

	@Override
	public void run() {
		postInvalidate();
	}
}
