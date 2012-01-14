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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.loadsensing.client.JsonClient;

public class SensorsActivity extends ListActivity {
	/** Called when the activity is first created. */

	private static final String DEB_TAG = "LoadSensingApp_LOG";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getLlistaSensors.php";
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> sensors = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_list_view);

		// Get Intent parameters
		String XarxaSelected = "";
		Bundle extras = null;
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				XarxaSelected = null;
			} else {
				XarxaSelected = extras.getString("idxarxaselected");
				Log.d(DEB_TAG, "Xarxa que hem triat anteriorment: "
						+ XarxaSelected);
			}
		} else {
			XarxaSelected = (String) savedInstanceState
					.getSerializable("XarxaSelected");
		}		

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.sensor_row_list_view, new String[] { "id", "sensor",
						"tipus", "descripcio", "poblacio", "canal" },
				new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.text4,
						R.id.text5, R.id.text6 });

		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?IdXarxa=" + XarxaSelected
				+ "&session=" + settings.getString("session", "");
		Log.d(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaSensorsArray = new JSONArray(jsonString);

			for (int i = 0; i < llistaSensorsArray.length(); i++) {
				JSONObject xarxaJSON = llistaSensorsArray.getJSONObject(i);
				sensors = new HashMap<String, String>();
				sensors.put("id", xarxaJSON.getString("id"));
				sensors.put("sensor", xarxaJSON.getString("sensor"));
				sensors.put("canal", xarxaJSON.getString("canal"));
				sensors.put("tipus", xarxaJSON.getString("tipus"));
				sensors.put("descripcio", xarxaJSON.getString("Descripcio"));
				sensors.put("poblacio", xarxaJSON.getString("Poblacio"));
				Log.d(DEB_TAG, xarxaJSON.getString("Poblacio"));

				list.add(sensors);
			}
			
			setListAdapter(adapter);
			
		} catch (Exception ex) {
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {

		TextView c = (TextView) v.findViewById(R.id.text1);
		String idsensorselected = c.getText().toString();
		Log.d(DEB_TAG, "idsensorselected: " + c.getText().toString());

		Intent intent = new Intent();
		intent.setClass(this.getApplicationContext(),
				SingleSensorActivity.class);
		intent.putExtra("idsensorselected", idsensorselected);
		startActivity(intent);

	}

	public void goBack(View v) {
		finish();
	}

	public void onClickHome(View v) {
		goHome(this);
	}

	public void goHome(Context context) {
		final Intent intent = new Intent(context, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.logout:
			SharedPreferences settings = getSharedPreferences("LoadSensingApp",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("session", "");
			editor.commit();
			Log.d(DEB_TAG, "SharedPreferences. Session restarted.");
			startActivity(new Intent(getApplicationContext(),
					LoginActivity.class));
			this.finish();
			return true;
		case R.id.preferences:
			startActivity(new Intent(getApplicationContext(), Preferences.class));
			return true;
		case R.id.exit:
			moveTaskToBack(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
}