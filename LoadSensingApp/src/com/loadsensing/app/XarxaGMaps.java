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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.loadsensing.client.JsonClient;

//Muestra las redes en un mapa de Google Maps
public class XarxaGMaps extends MapActivity {

	private static final String DEB_TAG = "LoadSensingApp_LOG";
	private String SERVER_HOST = "http://viuterrassa/Android/getLlistatXarxes.php";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gmaps);

		MapController mapController;
		MapView mapa;

		// Obtenemos una referencia al control MapView
		mapa = (MapView) findViewById(R.id.mapa);

		// Mostramos los controles de zoom sobre el mapa
		mapa.setBuiltInZoomControls(true);

		// Añadimos la capa de marcadores
		List<Overlay> mapOverlays;
		Drawable drawable;
		OverlayXarxa itemizedOverlay;

		mapOverlays = mapa.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.marker);
		itemizedOverlay = new OverlayXarxa(drawable, mapa);

		int minLatitude = Integer.MAX_VALUE;
		int maxLatitude = Integer.MIN_VALUE;
		int minLongitude = Integer.MAX_VALUE;
		int maxLongitude = Integer.MIN_VALUE;

		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?session="
				+ settings.getString("session", "");
		Log.d(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaXarxesArray = new JSONArray(jsonString);

			// HashMap<String, String> xarxa = null;

			for (int i = 0; i < llistaXarxesArray.length(); i++) {
				// xarxa = new HashMap<String, String>();
				JSONObject xarxaJSON = new JSONObject();
				xarxaJSON = llistaXarxesArray.getJSONObject(i);

				float lat = Float.parseFloat(xarxaJSON.getString("Lat"));
				float lon = Float.parseFloat(xarxaJSON.getString("Lon"));
				GeoPoint point = new GeoPoint((int) (lat * 1E6),
						(int) (lon * 1E6));

				int latInt = point.getLatitudeE6();
				int lonInt = point.getLongitudeE6();

				// Calculamos las coordenadas máximas y mínimas, para
				// posteriormente calcular el zoom y la posición del mapa
				// centrado
				maxLatitude = Math.max(latInt, maxLatitude);
				minLatitude = Math.min(latInt, minLatitude);
				maxLongitude = Math.max(lonInt, maxLongitude);
				minLongitude = Math.min(lonInt, minLongitude);

				OverlayItem overlayitem = new OverlayItem(point,
						xarxaJSON.getString("Nom") + " - "
								+ xarxaJSON.getString("Sensors") + " sensors",
						xarxaJSON.getString("Poblacio"));

				itemizedOverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedOverlay);
			}
			// setListAdapter(adapter);

		} catch (Exception e) {
			Log.d(DEB_TAG, "Error rebent xarxes");
		}

		// Definimos zoom y centramos el mapa
		mapController = mapa.getController();
		mapController.zoomToSpan(Math.abs(maxLatitude - minLatitude),
				Math.abs(maxLongitude - minLongitude));
		mapController.animateTo(new GeoPoint((maxLatitude + minLatitude) / 2,
				(maxLongitude + minLongitude) / 2));
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void goBack(View v) {
		finish();
	}

	public void onClickHome(View v) {
		goHome(this);
	}

	public void goHome(Context context) {
		final Intent intent = new Intent(context, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.logout:
			SharedPreferences settings = getSharedPreferences("LoadSensingApp",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("session", "");
			editor.commit();
			Log.d(DEB_TAG, "SharedPreferences. Session restarted.");
			startActivity(new Intent(getApplicationContext(),
					LoginActivity.class));
			this.finish();
			return true;
		case R.id.preferences:
			startActivity(new Intent(getApplicationContext(), Preferences.class));
			return true;
		case R.id.exit:
			moveTaskToBack(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
