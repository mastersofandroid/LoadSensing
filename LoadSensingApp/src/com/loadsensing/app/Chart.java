package com.loadsensing.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	private String CHART_URL = "http://chart.apis.google.com/chart";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// WebView googleChartView = new WebView(this);
		setContentView(R.layout.chart);
		WebView googleChartView = (WebView) findViewById(R.id.chart);
		// URL example
		// String mUrl ="http://chart.apis.google.com/chart?cht=p3&chd=t:30,60,10&chs=250x100&chl=cars|bikes|trucks";

		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?session="
				+ settings.getString("session", "") + "&id=2&TipusGrafic=0";
		Log.i(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray ValorsGrafica = new JSONArray(jsonString);

			HashMap<String, String> valorHashMap = new HashMap<String, String>();
			List<HashMap<String, String>> valorsURL = new ArrayList<HashMap<String, String>>();

			JSONObject Valors = new JSONObject();
			Valors = ValorsGrafica.getJSONObject(0);

			String grafica = Valors.getString("ValorsGrafica");
			Log.i(DEB_TAG, "String " + grafica);

			JSONArray ValorGrafica = new JSONArray(grafica);
			for (int i = 0; i < ValorGrafica.length(); i++) {
				JSONObject Valor = new JSONObject();
				Valor = ValorGrafica.getJSONObject(i);

				valorHashMap.put("date", Valor.getString("date"));
				valorHashMap.put("value", Valor.getString("value"));
				valorsURL.add(valorHashMap);
				//Log.i(DEB_TAG, "String " + valorsURL.get(i).get("date"));
			}

		} catch (Exception e) {
			Log.i(DEB_TAG, "Error rebent xarxes");
		}

		// googleChartView.loadUrl(mUrl);
	}
}
