package com.loadsensing.app;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.loadsensing.client.JsonClient;

public class Chart extends DashboardActivity {

	private static final String DEB_TAG = "Json_Android";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getValorsGrafic.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// WebView googleChartView = new WebView(this);
		setContentView(R.layout.chart);
		WebView googleChartView = (WebView) findViewById(R.id.chart);
		// URL example
		// String mUrl =
		// "http://chart.apis.google.com/chart?cht=p3&chd=t:30,60,10&chs=250x100&chl=cars|bikes|trucks";

		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?session="
				+ settings.getString("session", "") + "&id=2&TipusGrafic=0";
		Log.i(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaXarxesArray = new JSONArray(jsonString);

			// HashMap<String, String> xarxa = null;

			for (int i = 0; i < llistaXarxesArray.length(); i++) {
				// xarxa = new HashMap<String, String>();
				JSONObject xarxaJSON = new JSONObject();
				xarxaJSON = llistaXarxesArray.getJSONObject(i);

				// xarxaJSON.getString("Lat");

			}

		} catch (Exception e) {
			Log.i(DEB_TAG, "Error rebent xarxes");
		}

		// googleChartView.loadUrl(mUrl);
	}
}
