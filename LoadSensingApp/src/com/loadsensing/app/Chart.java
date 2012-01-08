package com.loadsensing.app;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

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
	private String CHART_URL = "http://chart.apis.google.com/chart?";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// WebView googleChartView = new WebView(this);
		setContentView(R.layout.chart);
		WebView googleChartView = (WebView) findViewById(R.id.chart);
		// URL example
		// String mUrl
		// ="http://chart.apis.google.com/chart?cht=p3&chd=t:30,60,10&chs=250x100&chl=cars|bikes|trucks";

		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?session="
				+ settings.getString("session", "") + "&id=2&TipusGrafic=0";
		Log.i(DEB_TAG, "Requesting to " + address);

		ArrayList<HashMap<String, String>> valorsURL = new ArrayList<HashMap<String, String>>();

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray ValorsGrafica = new JSONArray(jsonString);

			HashMap<String, String> valorHashMap = new HashMap<String, String>();

			// En esta llamada sólo hay 1 objeto
			JSONObject Valors = new JSONObject();
			Valors = ValorsGrafica.getJSONObject(0);

			String grafica = Valors.getString("ValorsGrafica");
			Log.i(DEB_TAG, "String " + grafica);

			// Sobre el string de ValorGrafica, volvemos a parsearlo
			JSONArray ValorGrafica = new JSONArray(grafica);
			for (int i = 0; i < ValorGrafica.length(); i++) {
				JSONObject Valor = new JSONObject();
				Valor = ValorGrafica.getJSONObject(i);

				valorHashMap.put("date", Valor.getString("date"));
				valorHashMap.put("value", Valor.getString("value"));
				valorsURL.add(valorHashMap);
				Log.i(DEB_TAG, valorsURL.get(i).get("date"));
			}

		} catch (Exception e) {
			Log.i(DEB_TAG, "Error rebent xarxes");
		}

		// Montamos URL
		String mUrl = CHART_URL;

		// Etiquetas eje X
		mUrl = mUrl + "chxl=0:";

		for (int i = 1; i < valorsURL.size(); i+=2) {
			mUrl = mUrl + "|" + URLEncoder.encode(valorsURL.get(i).get("date"));
			HashMap<String, String> a = valorsURL.get(i);
			Log.i(DEB_TAG, "URL Chart " + a.get("date"));
			Log.i(DEB_TAG, "i: " + i);
		}
		mUrl = mUrl + "|Fecha";

		// Posición etiquetas eje Y
		mUrl = mUrl + "&chxp=0,10,30,50,70,90,110";

		// Rango x,y
		mUrl = mUrl + "&chxr=0,-5,110|1,1,2";

		// Ejes visibles
		mUrl = mUrl + "&chxt=x,y";

		// Tipo de gráfico
		mUrl = mUrl + "&cht=lxy";

		// Medida del gráfico
		mUrl = mUrl + "&chs=440x200";

		// Colores
		mUrl = mUrl + "&chco=3072F3";

		// Escala
		mUrl = mUrl + "&chds=0,9,1.62,1.65";

		// Valores
		mUrl = mUrl
				+ "&chd=t:0,1,2,3,4,5,6,7,8,9|1.631,1.63,1.636,1.631,1.64,1.64,1.636,1.63,1.632,1.633";

		// Leyenda
		mUrl = mUrl + "&chdl=Sensor+strain+(V)&chdlp=b";

		// Estilo de lineas
		mUrl = mUrl + "&chls=2";

		// Márgenes
		mUrl = mUrl + "&chma=0,5,5,25|5";

		// Marcador
		mUrl = mUrl + "&chm=r,FF0000,0,0,0";

		Log.i(DEB_TAG, "URL Chart " + mUrl);

		googleChartView.loadUrl(mUrl);
	}
}
