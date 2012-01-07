package com.loadsensing.app;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.loadsensing.client.BalloonItemizedOverlay;

public class OverlayXarxa extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public OverlayXarxa(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		mContext = mapView.getContext();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}	
	
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		/*
		 * TODO: Poner parametro
		 */
		Intent intent = new Intent();
		intent.setClass(mContext, SingleSensorActivity.class);
		intent.putExtra("idsensorselected", index);
		mContext.startActivity(intent);
	
//		Toast.makeText(mContext, "onBalloonTap for overlay index " + index,
//				Toast.LENGTH_LONG).show();
		
		return true;
	}
}
