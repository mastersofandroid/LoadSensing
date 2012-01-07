package com.loadsensing.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.loadsensing.client.JsonClient;

public class SensorsActivity extends ListActivity {
	/** Called when the activity is first created. */

	private static final String DEB_TAG = "Json_Android";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getLlistaSensors.php";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_list_view);
		
		// Get Intent parameters
		String XarxaSelected = "";
		Bundle extras = null;
		if (savedInstanceState == null) {
		    extras = getIntent().getExtras();
		    if(extras == null) {
		    	XarxaSelected= null;
		    } else {
		    	XarxaSelected= extras.getString("idxarxaselected");
		        Log.i(DEB_TAG, "Xarxa que hem triat anteriorment: " + XarxaSelected);
		    }
		} else {
			XarxaSelected= (String) savedInstanceState.getSerializable("XarxaSelected");
		}
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.custom_row_view, new String[] { "id","sensor", "tipus", "descripcio", "poblacio" },
				new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.text4,  R.id.text5 });
		
		
		SharedPreferences settings = getSharedPreferences("LoadSensinsgApp", Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?IdXarxa="+XarxaSelected+"&session="+settings.getString("session", "");
		Log.i(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaSensorsArray = new JSONArray(jsonString);

			HashMap<String, String> sensors = null;

			for (int i = 0; i < llistaSensorsArray.length(); i++) {
				JSONObject xarxaJSON = llistaSensorsArray.getJSONObject(i);
				sensors = new HashMap<String, String>();
				sensors.put("id", xarxaJSON.getString("id"));
				sensors.put("sensor", xarxaJSON.getString("sensor"));
				sensors.put("canal", xarxaJSON.getString("canal"));
				sensors.put("tipus", xarxaJSON.getString("tipus"));
				sensors.put("descripcio", xarxaJSON.getString("Descripcio"));
				sensors.put("poblacio", xarxaJSON.getString("Poblacio"));
				Log.i(DEB_TAG, xarxaJSON.getString("Poblacio"));

				list.add(sensors);
			}
			setListAdapter(adapter);
		} 
		catch (Exception ex) 
		{
		}
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {

		TextView c = (TextView)v.findViewById(R.id.text1);
		String idsensorselected = c.getText().toString();
 		Log.i(DEB_TAG, "idsensorselected: " + c.getText().toString());

		Intent intent = new Intent();
		intent.setClass(this.getApplicationContext(), SingleSensorActivity.class);
		intent.putExtra("idsensorselected", idsensorselected);
		startActivity(intent);
		
/*		super.onListItemClick(l, v, position, id);
		Object o = this.getListAdapter().getItem(position);
		JSONObject xarxaJS = (JSONObject)o;
		String sensor = o.toString();
		setContentView(R.layout.vista_sensor);*/
	}
	
	public void goBack(View v)
	{
		finish();
	}

}