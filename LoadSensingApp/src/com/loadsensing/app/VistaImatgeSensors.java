package com.loadsensing.app;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.loadsensing.client.JsonClient;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class VistaImatgeSensors extends View {

	public VistaImatgeSensors(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public VistaImatgeSensors(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public VistaImatgeSensors(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				   MeasureSpec.getSize(heightMeasureSpec));
	}
	
	private static final String DEB_TAG = "Json_Android";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getLlistaSensorsImatges.php";
	private SharedPreferences settings;
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		// TODO Auto-generated method stub
		//String address = SERVER_HOST + "?IdImatge=000&session="+ settings.getString("session", "");
		//SharedPreferences settings = context.getSharedPreferences("LoadSensinsgApp", Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?IdImatge=000&session=1326063600";
		Log.i(DEB_TAG, "Requesting to " + address);
		
		Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.reddot);
		
		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaSensorsArray = new JSONArray(jsonString);

			// HashMap<String, String> xarxa = null;

			for (int i = 0; i < llistaSensorsArray.length(); i++) {
				// xarxa = new HashMap<String, String>();
				JSONObject sensorJSON = new JSONObject();
				sensorJSON = llistaSensorsArray.getJSONObject(i);
				int coordx = Integer.parseInt(sensorJSON.getString("x"));
				int coordy = Integer.parseInt(sensorJSON.getString("y"));
				canvas.drawBitmap(myBitmap, coordx, coordy, null);
			}
		}catch(Exception e)
		{
			
		}
       
	}
	
/*	@Override
	protected boolean onTouch(MotionEvent event) {
	    final int x=(int)event.getX();
	    Log.i("***********xPos","="+x);
	    final int y=(int)event.getY();
	    Log.i("***********yPos","="+y);

	    if(event.getAction()==MotionEvent.ACTION_UP)
	    {

	    }
	    if(event.getAction()==MotionEvent.ACTION_DOWN)
	    {
	        canvas.drawBitmap(mBitmap1,50+x,60,null );
	        this.postInvalidate();

	    }
	    if(event.getAction()==MotionEvent.ACTION_MOVE)
	    {

	    }
	    return false;
	}
*/


}
