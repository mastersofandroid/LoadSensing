package com.loadsensing.app;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.loadsensing.client.JsonClient;

public class XarxaGMaps extends MapActivity {

	private static final String DEB_TAG = "Json_Android";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getLlistatXarxes.php";
	private SharedPreferences settings;
	
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
		drawable = this.getResources().getDrawable(R.drawable.gmaps_marker);
		itemizedOverlay = new OverlayXarxa(drawable, mapa);
		
		int minLatitude = Integer.MAX_VALUE;
		int maxLatitude = Integer.MIN_VALUE;
		int minLongitude = Integer.MAX_VALUE;
		int maxLongitude = Integer.MIN_VALUE;
		
		SharedPreferences settings = getSharedPreferences("LoadSensinsgApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?session="
				+ settings.getString("session", "");
		Log.i(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaXarxesArray = new JSONArray(jsonString);

			//HashMap<String, String> xarxa = null;

			for (int i = 0; i < llistaXarxesArray.length(); i++) {
				//xarxa = new HashMap<String, String>();
				JSONObject xarxaJSON = new JSONObject();
				xarxaJSON = llistaXarxesArray.getJSONObject(i);

				GeoPoint point = new GeoPoint(
						Integer.parseInt(xarxaJSON.getString("Lat").replace(".", "")),
						Integer.parseInt(xarxaJSON.getString("Lon").replace(".", "")));
				
				int lat = point.getLatitudeE6();
				int lon = point.getLongitudeE6();

				maxLatitude = Math.max(lat, maxLatitude);
				minLatitude = Math.min(lat, minLatitude);
				maxLongitude = Math.max(lon, maxLongitude);
				minLongitude = Math.min(lon, minLongitude);
				
				OverlayItem overlayitem = new OverlayItem(point, xarxaJSON.getString("Nom") + " - " + xarxaJSON.getString("Sensors") + " sensors", xarxaJSON.getString("Poblacio"));

				itemizedOverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedOverlay);
			}
			//setListAdapter(adapter);

		} catch (Exception e) {
			Log.i(DEB_TAG, "Error rebent xarxes");
		}
		
		// Definimos zoom y centramos el mapa
		mapController = mapa.getController();
		mapController.zoomToSpan(Math.abs(maxLatitude - minLatitude), Math.abs(maxLongitude - minLongitude));
		mapController.animateTo(new GeoPoint( 
				(maxLatitude + minLatitude)/2, 
				(maxLongitude + minLongitude)/2 )); 
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
