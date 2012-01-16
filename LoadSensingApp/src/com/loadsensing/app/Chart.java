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

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.webkit.WebView;
import android.widget.RadioGroup;

import com.loadsensing.client.JsonClient;

/*
 * Esta actividad muestra un gráfico generado online con los datos del sensor consultado. 
 * Recibe por parámetro
 */
public class Chart extends DashboardActivity {

	private String SERVER_HOST = "http://viuterrassa/Android/getValorsGrafic.php";
	private String CHART_URL = "http://chart.apis.google.com/chart?";
	String SensorSelected = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.chart);
		final WebView googleChartView = (WebView) findViewById(R.id.chart);

		Bundle extras = null;

		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				SensorSelected = null;
			} else {
				SensorSelected = extras.getString("idsensorselected");
				Log.d(DEB_TAG, "Xarxa que hem triat anteriorment: "
						+ SensorSelected);
			}
		}

		// Listener para generar el gráfico cada vez que se escoge la opción en
		// el radiobutton
		RadioGroup rg = (RadioGroup) findViewById(R.id.tipochart);
		rg.clearCheck();
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				generaChart(googleChartView, checkedId, SensorSelected);
			}
		});

	}

	private void generaChart(WebView googleChartView, int checkedId,
			String SensorSelected) {
		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?session="
				+ settings.getString("session", "") + "&id=" + SensorSelected
				+ "&TipusGrafic=" + checkedId;

		ArrayList<HashMap<String, String>> valorsURL = new ArrayList<HashMap<String, String>>();

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray ValorsGrafica = new JSONArray(jsonString);

			// En esta llamada sólo hay 1 objeto
			JSONObject Valors = new JSONObject();
			Valors = ValorsGrafica.getJSONObject(0);

			String grafica = Valors.getString("ValorsGrafica");

			// Sobre el string de ValorGrafica, volvemos a parsearlo
			JSONArray ValorGrafica = new JSONArray(grafica);
			for (int i = 0; i < ValorGrafica.length(); i++) {
				JSONObject Valor = new JSONObject();
				Valor = ValorGrafica.getJSONObject(i);

				HashMap<String, String> valorHashMap = new HashMap<String, String>();
				valorHashMap.put("date", Valor.getString("date"));
				valorHashMap.put("value", Valor.getString("value"));
				valorsURL.add(valorHashMap);
			}

		} catch (Exception e) {
			Log.d(DEB_TAG, "Error rebent xarxes");
		}

		// Montamos URL
		String mUrl = CHART_URL;

		// Etiquetas eje X, columna 0 y 1
		mUrl = mUrl + "chxl=0:";
		for (int i = 3; i < valorsURL.size(); i += 4) {
			mUrl = mUrl + "|" + URLEncoder.encode(valorsURL.get(i).get("date"));
		}
		mUrl = mUrl + "|Fecha";
		mUrl = mUrl + "|1:";
		for (int i = 1; i < valorsURL.size(); i += 4) {
			mUrl = mUrl + "|" + URLEncoder.encode(valorsURL.get(i).get("date"));
		}

		// Posición etiquetas eje Y
		mUrl = mUrl + "&chxp=0,30,70,110|1,10,50,90";

		// Rango x,y

		// Coger valor mínimo y máximo
		float max = new Float(valorsURL.get(0).get("value"));
		float min = new Float(valorsURL.get(0).get("value"));
		for (int i = 1; i < valorsURL.size(); i++) {
			Float valueFloat = new Float(valorsURL.get(i).get("value"));
			max = Math.max(max, valueFloat);
			min = Math.min(min, valueFloat);
		}

		BigDecimal maxRounded = new BigDecimal(max);
		maxRounded = maxRounded.setScale(1, BigDecimal.ROUND_CEILING);
		BigDecimal minRounded = new BigDecimal(min);
		minRounded = minRounded.setScale(1, BigDecimal.ROUND_FLOOR);

		mUrl = mUrl + "&chxr=0,-5,110|1,-5,110|2," + minRounded + ","
				+ maxRounded;

		// Ejes visibles
		mUrl = mUrl + "&chxt=x,x,y";

		// Tipo de gráfico
		mUrl = mUrl + "&cht=lxy";

		// Medida del gráfico
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth() - 170;
		int height = display.getHeight() - 390;
		mUrl = mUrl + "&chs=" + width + "x" + height;

		// Colores
		mUrl = mUrl + "&chco=3072F3";

		// Escala
		mUrl = mUrl + "&chds=0,9," + minRounded + "," + maxRounded;

		// Valores
		mUrl = mUrl + "&chd=t:0";
		for (int i = 1; i < valorsURL.size(); i++) {
			mUrl = mUrl + "," + i;
		}
		mUrl = mUrl + "|";
		mUrl = mUrl + valorsURL.get(0).get("value");
		for (int i = 1; i < valorsURL.size(); i++) {
			mUrl = mUrl + "," + valorsURL.get(i).get("value");
		}

		// Título eyenda
		switch (checkedId) {
		case R.id.radio1:
			mUrl = mUrl + "&chdl=Sensor+strain+(V)&chdlp=b";
			break;
		case R.id.radio2:
			mUrl = mUrl + "&chdl=Excitation+power+(V)&chdlp=b";
			break;
		case R.id.radio3:
			mUrl = mUrl + "&chdl=Counter+(cnts)&chdlp=b";
			break;
		}

		// Estilo de lineas
		mUrl = mUrl + "&chls=2";

		// Márgenes
		mUrl = mUrl + "&chma=0,5,5,25|5";

		// Marcador
		mUrl = mUrl + "&chm=r,FF0000,0,0,0";

		Log.d(DEB_TAG, "URL Chart " + mUrl);

		googleChartView.loadUrl(mUrl);
	}
}