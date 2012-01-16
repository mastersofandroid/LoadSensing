/***
 * Copyright (c) 2012 David Garcia / Jose Antonio Gómez
 * mastersofandroid@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.loadsensing.app;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.loadsensing.client.JsonClient;

public class ImatgeXarxaSensors extends DashboardActivity {

	private String SERVER_HOST = "http://viuterrassa/Android/getLlistaSensorsImatges.php";
	private String SERVER_HOST_IMAGE = "http://viuterrassa/Android/getLlistaImatges.php";
	private String PATH_IMAGE = "http://viuterrassa/Android/Imatges/";
	ArrayList<HashMap<String, String>> listaSensors = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Definimos idioma
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(ImatgeXarxaSensors.this);
		Locale locale = new Locale(settings.getString("location", "es"));
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getApplicationContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

		setContentView(new TouchView(this));
	}

	class TouchView extends View {

		Bitmap bgr;
		Bitmap overlay;

		public TouchView(Context context) {
			super(context);
			
			// definim color de fons negre
			int color = Color.parseColor("#000000");
			setBackgroundColor(color);

			// bitmap de la imatge del sensor
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

			try 
			{
				String IdXarxaParam = "00" + XarxaSelected;
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

				
				URL imatgeURL = null;
				// imatgeURL conté la URL de la imatge a mostrar, farem un Bitmap (anomenat bgr) a partir del 
				// inputStream de la connexió
				imatgeURL = new URL(pathImg);
				HttpURLConnection conn = (HttpURLConnection) imatgeURL
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				bgr = BitmapFactory.decodeStream(is);

				float ampladapantalla = getWindowManager().getDefaultDisplay()
						.getWidth();
				int orientacio = getRequestedOrientation();
				if (ampladapantalla < bgr.getWidth()) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					canvas.drawBitmap(bgr, 0, 0, null);
				} else {
					// si la orientació es vertical, escalem la imatge amb un coeficient calculat a partir
					// de la divisió entre l'alçada de la pantalla i l'alçada de la imatge.
					if (orientacio != 0) {
						escala = (float) getWindowManager().getDefaultDisplay()
								.getHeight() / (float) bgr.getHeight();
					} else {
						// si la imatge està en horitzontal, no escalem (coeficient = 1)
						escala = 1;
					}
					
					canvas.scale(escala, escala);
					// dibuixem el bitmap per pantalla
					canvas.drawBitmap(bgr, 0, 0, null);
				}
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
					
					// fem recorregut per la llista de sensors i guardem els valors idsensor, i les coordenades X i Y.
					sensor.put("idsensor", sensorJSON.getString("id"));
					sensor.put("x",
							Float.toString(Float.valueOf(coordx) * escala)); // calculem la coordenada x pel coeficient de l'escala
					sensor.put("y",
							Float.toString(Float.valueOf(coordy) * escala)); // calculem la coordenada y pel coeficient de l'escala
					
					// per ajustar més el punt de coordenada del sensor, dibuixarem la imatge amb un offset a cada coordenada
					// a la coordenada X li restem la meitat de l'amplada de la imatge del sensor
					// a la coordenada y li restem la meitat de l'alçada de la imatge del sensor
					canvas.drawBitmap(overlay, Integer.parseInt(coordx)
							- (overlay.getWidth() / 2),
							Integer.parseInt(coordy)
									- (overlay.getHeight() / 2), null);
					listaSensors.add(sensor);
				}
			} catch (Exception ex) {

			}
		}

		public boolean onTouchEvent(MotionEvent ev) {
			
			final int action = ev.getAction();
			float x = ev.getX(); 
			float y = ev.getY();

			// a l'event ACTION_DOWN comprovarem si les coordenades X i Y de l'event (on es fa el click a la pantalla)
			// coincideix amb una imatge de les dibuixades
			switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				// fem un recorregut de tots els sensors de la xarxa comprovant si coincideixen les coordenades
				// per ampliar una mica el rang, a les coordenades li sumem l'amplada i l'alçada de la imatge respectivament
				for (int j = 0; j < listaSensors.size(); j++) {
					
					HashMap<String, String> sensor = new HashMap<String, String>();
					sensor = listaSensors.get(j);
					int coordenadaxsensor = Math.round(Float.valueOf(sensor
							.get("x")));
					int coordenadaysensor = Math.round(Float.valueOf(sensor
							.get("y")));
					
					if (x >= (coordenadaxsensor - overlay.getWidth())
							&& x < (coordenadaxsensor + overlay.getWidth())
							&& y >= (coordenadaysensor - overlay.getHeight())
							&& y < (coordenadaysensor + overlay.getHeight())) {
						
						// si es fa click dins una imatge, obrim la nova pantalla amb la informació del sensor (pasant l'id per paràmetre)
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