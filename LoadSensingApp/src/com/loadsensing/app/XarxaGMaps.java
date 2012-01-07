package com.loadsensing.app;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class XarxaGMaps extends MapActivity {

	private static final String DEB_TAG = "Json_Android";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gmaps);
		
		MapController mapController;
		MapView mapa;
		Bundle extras = null;
	    String longitud = "";
	    String latitud = "";
	    
		if (savedInstanceState == null) {
		    extras = getIntent().getExtras();
		    longitud= extras.getString("lon");
			Log.i(DEB_TAG, "Longitud: " + longitud);
		    latitud= extras.getString("lat");
			Log.i(DEB_TAG, "Latitud: " + latitud);		    
		} else {
			longitud= (String) savedInstanceState.getSerializable("lon");
			latitud= (String) savedInstanceState.getSerializable("lat");
		}
		
		// Obtenemos una referencia al control MapView
		mapa = (MapView) findViewById(R.id.mapa);

		// Mostramos los controles de zoom sobre el mapa
		mapa.setBuiltInZoomControls(true);
		
		//Añadimos la capa de marcadores
		List<Overlay> mapOverlays;
		Drawable drawable;
		OverlayXarxa itemizedOverlay;
		
		mapOverlays = mapa.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.gmap_marker);
		itemizedOverlay = new OverlayXarxa(drawable);
		
		GeoPoint point = new GeoPoint(Integer.parseInt(latitud.replace(".", "")), Integer.parseInt(longitud.replace(".", "")));
		OverlayItem overlayitem = new OverlayItem(point, "", "");
		
		itemizedOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlay);
		
		//Definimos zoom y centramos el mapa
		mapController = mapa.getController();
		mapController.setZoom(14); // Zoon 1 is world view
		mapController.setCenter(point);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
