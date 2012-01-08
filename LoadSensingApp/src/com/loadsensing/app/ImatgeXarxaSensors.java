package com.loadsensing.app;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loadsensing.client.JsonClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ImatgeXarxaSensors extends Activity {

	private static final String DEB_TAG = "Json_Android";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getLlistaSensorsImatges.php";
	private SharedPreferences settings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TouchView(this));


    }

    class TouchView extends View{
        Bitmap bgr;
        Bitmap overlayDefault;
        Bitmap overlay;
        Paint pTouch;
        int X = -100;
        int Y = -100;
        Canvas c2;

        public TouchView(Context context) {
            super(context);

            bgr = BitmapFactory.decodeResource(getResources(),R.drawable.sagradafamilia);
            overlayDefault = BitmapFactory.decodeResource(getResources(),R.drawable.reddot);
            overlay = BitmapFactory.decodeResource(getResources(),R.drawable.reddot).copy(Config.ARGB_8888, true);  
       }
       
        
        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            // Let the ScaleGestureDetector inspect all events.
            final int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
        		Intent intent = new Intent();
        		intent.setClass(getApplicationContext(), SingleSensorActivity.class);
        		intent.putExtra("idsensorselected", "002");
        		startActivity(intent);
                break;
            }
            }

            return true;
        }

        
        
        @Override
        public void onDraw(Canvas canvas){
            super.onDraw(canvas);

            //draw background
            canvas.drawBitmap(bgr, 0, 0, null);
            //copy the default overlay into temporary overlay and punch a hole in it                          
            //c2.drawBitmap(overlayDefault, 0, 0, null); //exclude this line to show all as you draw
              
         // TODO Auto-generated method stub
    		SharedPreferences settings = this.getContext().getSharedPreferences("LoadSensingApp", Context.MODE_PRIVATE);
    		// String address = SERVER_HOST + "?IdImatge=000&session=1326063600";
    		String address = SERVER_HOST + "?IdImatge=000&session="+ settings.getString("session", "");
    		Log.i(DEB_TAG, "Requesting to " + address);

    		try {
    			String jsonString = JsonClient.connectString(address);

    			// Convertim la resposta string a un JSONArray
    			JSONArray llistaSensorsArray = new JSONArray(jsonString);

    			for (int i = 0; i < llistaSensorsArray.length(); i++) {
    				
    				JSONObject sensorJSON = new JSONObject();
    				sensorJSON = llistaSensorsArray.getJSONObject(i);
    				int coordx = Integer.parseInt(sensorJSON.getString("x"));
    				int coordy = Integer.parseInt(sensorJSON.getString("y"));
    				canvas.drawBitmap(overlay, coordx, coordy, null);
    				
    		        
    			} 
    		}
    		catch(Exception ex)
    		{
    			
    		}
        }
    }
}