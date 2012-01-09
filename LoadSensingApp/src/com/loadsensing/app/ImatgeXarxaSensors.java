package com.loadsensing.app;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loadsensing.client.JsonClient;

public class ImatgeXarxaSensors extends Activity {

	private static final String DEB_TAG = "Json_Android";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getLlistaSensorsImatges.php";
	private String SERVER_HOST_IMAGE = "http://viuterrassa.com/Android/getLlistaImatges.php";
	private String PATH_IMAGE = "http://viuterrassa.com/Android/Imatges/";
	private SharedPreferences settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new TouchView(this));
	}

	class TouchView extends View {
		Bitmap bgr;
		Bitmap overlay;

		public TouchView(Context context) {
			super(context);

			// COGER DE URL

			overlay = BitmapFactory.decodeResource(getResources(),
					R.drawable.reddot).copy(Config.ARGB_8888, true);
		}

		/*
		 * @Override public boolean onTouchEvent(MotionEvent ev) { // Let the
		 * ScaleGestureDetector inspect all events. final int action =
		 * ev.getAction(); float x = ev.getX(); // or getRawX(); float y =
		 * ev.getY();
		 * 
		 * switch (action & MotionEvent.ACTION_MASK) { case
		 * MotionEvent.ACTION_DOWN: { Intent intent = new Intent();
		 * intent.setClass(getApplicationContext(), SingleSensorActivity.class);
		 * intent.putExtra("idsensorselected", "002"); startActivity(intent);
		 * break;
		 * 
		 * /* if (x >= overlay.getwi && x < (xOfYourBitmap +
		 * yourBitmap.getWidth()) && y >= yOfYourBitmap && y < (yOfYourBitmap +
		 * yourBitmap.getHeight())) { //tada, if this is true, you've started
		 * your click inside your bitmap } break; }
		 */

		/*
		 * } } return true; }
		 */
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			// TODO Auto-generated method stub
			SharedPreferences settings = this.getContext()
					.getSharedPreferences("LoadSensingApp",
							Context.MODE_PRIVATE);

			// Obtenim la imatge a partir del webservice
			/*
			 * Bundle extras = null; if (savedInstanceState == null) { extras =
			 * getIntent().getExtras(); if (extras == null) { XarxaSelected =
			 * null; } else { XarxaSelected =
			 * extras.getString("idxarxaselected"); Log.i(DEB_TAG,
			 * "Xarxa que hem triat anteriorment: " + XarxaSelected); } }
			 */
			String idImg = "";
			String pathImg = "";

			try {
				String IdXarxaParam = "002";
				String addressImg = SERVER_HOST_IMAGE + "?IdXarxa="
						+ IdXarxaParam + "&session="
						+ settings.getString("session", "");
				String jsonStringImg = JsonClient.connectString(addressImg);
				// Convertim la resposta string a un JSONArray
				JSONArray imatgeArray = new JSONArray(jsonStringImg);
				JSONObject imatgeJson = new JSONObject();
				imatgeJson = imatgeArray.getJSONObject(0);

				// Capturem l'IdImatge per cridar al següent WebService que ens
				// tornarà les posicions dels sensors en la imatge
				// i capturem la key imatge per obtenir el nom que concatenat a
				// la ruta de la imatge en el servidor, ens permetrà
				// descarregar-la

				pathImg = PATH_IMAGE + imatgeJson.getString("imatge");
				idImg = imatgeJson.getString("IdImatge");

				// draw background
				URL imatgeURL = null;

				imatgeURL = new URL(pathImg);

				HttpURLConnection conn = (HttpURLConnection) imatgeURL
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				int length = conn.getContentLength();

				InputStream is = conn.getInputStream();
				bgr = BitmapFactory.decodeStream(is);
				// bgr = Bitmap.createScaledBitmap(bgr,480,800,true);
				// bgr = BitmapFactory.decodeResource(getResources(),
				// R.drawable.sagradafamilia);
				// canvas.scale(480, 800);
				canvas.drawBitmap(bgr, 0, 0, null);

			} catch (Exception ex) {

			}

			try {
				String address = SERVER_HOST + "?IdImatge=" + idImg
						+ "&session=" + settings.getString("session", "");
				Log.i(DEB_TAG, "Requesting to " + address);
				String jsonString = JsonClient.connectString(address);

				// Convertim la resposta string a un JSONArray
				JSONArray llistaSensorsArray = new JSONArray(jsonString);

				for (int i = 0; i < llistaSensorsArray.length(); i++) {

					JSONObject sensorJSON = new JSONObject();
					sensorJSON = llistaSensorsArray.getJSONObject(i);
					int coordx = Integer.parseInt(sensorJSON.getString("x"));
					int coordy = Integer.parseInt(sensorJSON.getString("y"));
					// overlay =
					// Bitmap.createScaledBitmap(overlay,480,800,true);
					// canvas.drawBitmap(overlay, coordx, coordy, null);

					Button b = new Button(this.getContext());
					AbsoluteLayout ll = new AbsoluteLayout(this.getContext());
					AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT, coordx,
							coordy);
					// layoutParams.setMargins(coordx, coordy, 0, 0);

					ll.addView(b, layoutParams);

					// Measure and layout the linear layout before drawing it
					ll.measure(MeasureSpec.getSize(ll.getMeasuredWidth()),
							MeasureSpec.getSize(ll.getMeasuredHeight()));
					ll.layout(0, 0, MeasureSpec.getSize(b.getMeasuredWidth()),
							MeasureSpec.getSize(b.getMeasuredHeight()));
					// Finally draw the linear layout on the canvas
					ll.draw(canvas);
					b.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							Toast.makeText(getContext(), "button clicked \n",
									Toast.LENGTH_LONG);
							Log.i(DEB_TAG, "toquem boto");
							return false;
						}
					});
					// create an onClick event for the button
					/*
					 * b.setOnClickListener(new OnClickListener() {
					 * 
					 * @Override public void onClick(View v) {
					 * Toast.makeText(getContext(), "button clicked \n",
					 * Toast.LENGTH_LONG); } //end of public void
					 * 
					 * });
					 */

				}
			} catch (Exception ex) {

			}
		}
	}
}