package com.loadsensing.app;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.loadsensing.client.JsonClient;

public class ImatgeXarxaSensors extends Activity {

	private static final String DEB_TAG = "LoadSensingApp_LOG";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getLlistaSensorsImatges.php";
	private String SERVER_HOST_IMAGE = "http://viuterrassa.com/Android/getLlistaImatges.php";
	private String PATH_IMAGE = "http://viuterrassa.com/Android/Imatges/";
	ArrayList<HashMap<String, String>> listaSensors = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(new TouchView(this));
		setContentView(R.layout.imatge_xarxa_sensors);
		// getWindow().setBackgroundDrawableResource(R.drawable.menulocalitzacioxarxes);
	}

	class TouchView extends View {

		Bitmap bgr;
		Bitmap overlay;

		public TouchView(Context context) {
			super(context);

			// COGER DE URL
			overlay = BitmapFactory.decodeResource(getResources(),
					R.drawable.sensor).copy(Config.ARGB_8888, true);
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			SharedPreferences settings = this.getContext()
					.getSharedPreferences("LoadSensingApp",
							Context.MODE_PRIVATE);
			String XarxaSelected = "";
			// Obtenim la imatge a partir del webservice
			Bundle extras = null;
			extras = getIntent().getExtras();
			if (extras == null) {
				XarxaSelected = null;
			} else {
				XarxaSelected = extras.getString("idxarxaselected");
				Log.d(DEB_TAG, "Xarxa que hem triat anteriorment: "
						+ XarxaSelected);
			}

			String idImg = "";
			String pathImg = "";
			float escala = 0;
			
			try {
				// String IdXarxaParam = "002";
				String IdXarxaParam = "00" + XarxaSelected;
				Log.d(DEB_TAG, "Xarxa que hem triat anteriorment: "
						+ IdXarxaParam);
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
				InputStream is = conn.getInputStream();
				bgr = BitmapFactory.decodeStream(is);
				// bgr = Bitmap.createScaledBitmap(bgr,480,800,true);
				// bgr = BitmapFactory.decodeResource(getResources(),
				// R.drawable.sagradafamilia);
				
				escala = (float)getWindowManager().getDefaultDisplay().getHeight()/(float)bgr.getHeight();

				//canvas.scale((float)bgr.getWidth()*escala, (float)getWindowManager().getDefaultDisplay().getHeight());
				canvas.scale(escala,escala);
				canvas.drawBitmap(bgr, 0, 0, null);

			} catch (Exception ex) {

			}

			try {
				String address = SERVER_HOST + "?IdImatge=" + idImg
						+ "&session=" + settings.getString("session", "");
				Log.d(DEB_TAG, "Requesting to " + address);
				String jsonString = JsonClient.connectString(address);

				// Convertim la resposta string a un JSONArray
				JSONArray llistaSensorsArray = new JSONArray(jsonString);
				HashMap<String, String> sensor = null;

				for (int i = 0; i < llistaSensorsArray.length(); i++) {
					sensor = new HashMap<String, String>();
					JSONObject sensorJSON = new JSONObject();
					sensorJSON = llistaSensorsArray.getJSONObject(i);

					String coordx = sensorJSON.getString("x");
					String coordy = sensorJSON.getString("y");

					// Integer.parseInt(sensorJSON.getString("x"));
					sensor.put("idsensor", sensorJSON.getString("id"));
					sensor.put("x", Float.toString(Float.valueOf(coordx)*escala));
					sensor.put("y", Float.toString(Float.valueOf(coordy)*escala));
					// overlay =
					// Bitmap.createScaledBitmap(overlay,480,800,true);
					canvas.drawBitmap(overlay, Integer.parseInt(coordx)-(overlay.getWidth()/2),
							Integer.parseInt(coordy)-(overlay.getHeight()/2), null);
					listaSensors.add(sensor);
				}
			} catch (Exception ex) {

			}
		}

		public boolean onTouchEvent(MotionEvent ev) {
			// Let the ScaleGestureDetector inspect all events.
			final int action = ev.getAction();
			float x = ev.getX(); // or getRawX();
			float y = ev.getY();

			switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				for (int j = 0; j < listaSensors.size(); j++) {
					HashMap<String, String> sensor = new HashMap<String, String>();
					sensor = listaSensors.get(j);
					int coordenadaxsensor = Math.round(Float.valueOf(sensor.get("x")));
					int coordenadaysensor = Math.round(Float.valueOf(sensor.get("y")));
					Log.d(DEB_TAG, "x donde se hace click : " + x);
					Log.d(DEB_TAG, "y donde se hace click: " + y);
					Log.d(DEB_TAG, "coordenada sensor x: " + coordenadaxsensor);
					Log.d(DEB_TAG, "coordenada sensor y: " + coordenadaysensor);
					Log.d(DEB_TAG,
							"overlay.getHeight() : " + overlay.getHeight());
					Log.d(DEB_TAG, "overlay.getWidth() : " + overlay.getWidth());

					if (x >= (coordenadaxsensor - overlay.getWidth())
							&& x < (coordenadaxsensor + overlay.getWidth())
							&& y >= (coordenadaysensor - overlay.getHeight())
							&& y < (coordenadaysensor + overlay.getHeight())) {
						Log.d(DEB_TAG, "Click dentro");
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								SingleSensorActivity.class);
						intent.putExtra("idsensorselected",
								sensor.get("idsensor"));
						startActivity(intent);
						break;
					}
				}

				break;

				/*
				 * if (x >= overlay.getwi && x < (xOfYourBitmap +
				 * yourBitmap.getWidth()) && y >= yOfYourBitmap && y <
				 * (yOfYourBitmap + yourBitmap.getHeight())) { //tada, if this
				 * is true, you've started your click inside your bitmap }
				 * break; }
				 */

			}
			}
			return true;
		}

	}

	public void goHome(Context context) {
		final Intent intent = new Intent(context, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
}