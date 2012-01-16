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

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loadsensing.client.JsonClient;

public class SingleSensorActivity extends DashboardActivity {

	private String SERVER_HOST = "http://77.228.158.13/Android/getSensorInfo.php";
	public static final String DEB_TAG = "LoadSensingApp_LOG";
	String SensorSelected = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_sensor);

		// Get Intent parameters
		
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

		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?sensor=" + SensorSelected
				+ "&session=" + settings.getString("session", "");
		Log.d(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaSensorsArray = new JSONArray(jsonString);
			JSONObject sensorJSON = llistaSensorsArray.getJSONObject(0);
			Log.d(DEB_TAG, sensorJSON.toString());
			TextView idsensor = (TextView) findViewById(R.id.sensor);
			TextView nomsensor = (TextView) findViewById(R.id.sensorname);
			TextView serialnumber = (TextView) findViewById(R.id.serialnumber);
			TextView mesura = (TextView) findViewById(R.id.measure);
			TextView unitatmesura = (TextView) findViewById(R.id.measureunit);
			TextView maxload = (TextView) findViewById(R.id.maxload);
			TextView MaxLoadUnit = (TextView) findViewById(R.id.maxloadunit);
			TextView Sensivity = (TextView) findViewById(R.id.sensitivity);
			TextView SensivityUnit = (TextView) findViewById(R.id.sensitivityunit);
			TextView offset = (TextView) findViewById(R.id.offset);
			TextView offsetUnit = (TextView) findViewById(R.id.offsetunit);
			TextView AlarmAt = (TextView) findViewById(R.id.alarmat);
			TextView AlarmAtUnit = (TextView) findViewById(R.id.alarmAtunit);
			TextView LastTare = (TextView) findViewById(R.id.lasttare);
			TextView canal = (TextView) findViewById(R.id.canal);
			TextView tipus = (TextView) findViewById(R.id.tipus);
			TextView Descripcio = (TextView) findViewById(R.id.descripcio);
			TextView Poblacio = (TextView) findViewById(R.id.poblacio);
			TextView Nom = (TextView) findViewById(R.id.nom);

			idsensor.setText(sensorJSON.getString("sensor"));
			nomsensor.setText(sensorJSON.getString("sensorName"));
			serialnumber.setText(sensorJSON.getString("serialNumber"));
			mesura.setText(sensorJSON.getString("measure"));
			unitatmesura.setText(sensorJSON.getString("measureUnit"));
			maxload.setText(sensorJSON.getString("MaxLoad"));
			MaxLoadUnit.setText(sensorJSON.getString("MaxLoadUnit"));
			Sensivity.setText(sensorJSON.getString("Sensivity"));
			SensivityUnit.setText(sensorJSON.getString("SensivityUnit"));
			offset.setText(sensorJSON.getString("offset"));
			offsetUnit.setText(sensorJSON.getString("offsetUnit"));
			AlarmAt.setText(sensorJSON.getString("AlarmAt"));
			AlarmAtUnit.setText(sensorJSON.getString("AlarmAtUnit"));
			LastTare.setText(sensorJSON.getString("LastTare"));
			canal.setText(sensorJSON.getString("canal"));
			tipus.setText(sensorJSON.getString("tipus"));
			Descripcio.setText(sensorJSON.getString("Descripcio"));
			Poblacio.setText(sensorJSON.getString("Poblacio"));
			Nom.setText(sensorJSON.getString("Nom"));

			Log.d(DEB_TAG, sensorJSON.getString("sensorName"));
		} catch (Exception ex) {
			Log.d(DEB_TAG, "Exception: " + ex.getMessage());
		}
	}

	public void graph(View v) {
		Intent intent = new Intent();
		intent.putExtra("idsensor", SensorSelected);
		intent.setClass(this.getApplicationContext(), Chart.class);
		startActivity(intent);
	}
}