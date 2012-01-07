package com.loadsensing.app;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loadsensing.client.JsonClient;

public class SingleSensorActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String DEB_TAG = "Json_Android";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getSensorInfo.php";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_sensor);

		// Get Intent parameters
		String SensorSelected = "";
		Bundle extras = null;
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				SensorSelected = null;
			} else {
				SensorSelected = extras.getString("idsensorselected");
				Log.i(DEB_TAG, "Xarxa que hem triat anteriorment: "
						+ SensorSelected);
			}
		} else {
			SensorSelected = (String) savedInstanceState
					.getSerializable("XarxaSelected");
		}

		SharedPreferences settings = getSharedPreferences("LoadSensinsgApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?sensor=" + SensorSelected
				+ "&session=" + settings.getString("session", "");
		Log.i(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaSensorsArray = new JSONArray(jsonString);
			JSONObject sensorJSON = llistaSensorsArray.getJSONObject(0);
			Log.i(DEB_TAG, sensorJSON.toString());
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

			idsensor.setText("Sensor: " + sensorJSON.getString("sensor"));
			nomsensor.setText("Nombre sensor: "
					+ sensorJSON.getString("sensorName"));
			serialnumber.setText("Num. serie: "
					+ sensorJSON.getString("serialNumber"));
			mesura.setText("Measure: " + sensorJSON.getString("measure"));
			unitatmesura.setText("Measure unit: "
					+ sensorJSON.getString("measureUnit"));
			maxload.setText("Max Load: " + sensorJSON.getString("MaxLoad"));
			MaxLoadUnit.setText("MaxLoadUnit: "
					+ sensorJSON.getString("MaxLoadUnit"));
			Sensivity
					.setText("Sensivity: " + sensorJSON.getString("Sensivity"));
			SensivityUnit.setText("SensivityUnit: "
					+ sensorJSON.getString("SensivityUnit"));
			offset.setText("offset: " + sensorJSON.getString("offset"));
			offsetUnit.setText("offsetUnit: "
					+ sensorJSON.getString("offsetUnit"));
			AlarmAt.setText("AlarmAt:" + sensorJSON.getString("AlarmAt"));
			AlarmAtUnit.setText("AlarmAtUnit:"
					+ sensorJSON.getString("AlarmAtUnit"));
			LastTare.setText("LastTare:" + sensorJSON.getString("LastTare"));
			canal.setText("canal" + sensorJSON.getString("canal"));
			tipus.setText("tipus: " + sensorJSON.getString("tipus"));
			Descripcio.setText("Descripcio:"
					+ sensorJSON.getString("Descripcio"));
			Poblacio.setText("Poblacio:" + sensorJSON.getString("Poblacio"));
			Nom.setText("Nom: " + sensorJSON.getString("Nom"));

			Log.i(DEB_TAG, sensorJSON.getString("sensor"));
			Log.i(DEB_TAG, sensorJSON.getString("canal"));
			Log.i(DEB_TAG, sensorJSON.getString("Nom"));
		} catch (Exception ex) {
		}
	}

	public void goBack(View v) {
		finish();
	}

	public void gmap_marker(View v) {
		TextView c = (TextView) v.findViewById(R.id.text4);
		String lat = c.getText().toString();
		Intent intent = new Intent();
		intent.putExtra("lat", lat);
		intent.setClass(this.getApplicationContext(), XarxaGMaps.class);
		startActivity(intent);
	}

}