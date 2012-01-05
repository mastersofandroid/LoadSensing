package com.loadsensing.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loadsensing.client.JsonClient;

public class SensorsActivity extends ListActivity {
	/** Called when the activity is first created. */

	private static final String DEB_TAG = "Json_Android";
	private String SERVER_HOST = "http://viuterrassa.com/Android/getLlistaSensors.php";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_list_view);

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.custom_row_view, new String[] { "poblacio", "id" },
				new int[] { R.id.text1, R.id.text2 });
		populateList2();
		setListAdapter(adapter);
	}

	static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	private void populateList() {
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("pen", "MONT Blanc");
		temp.put("price", "200.00$");
		temp.put("color", "Silver, Grey, Black");
		list.add(temp);
		HashMap<String, String> temp1 = new HashMap<String, String>();
		temp1.put("pen", "Gucci");
		temp1.put("price", "300.00$");
		temp1.put("color", "Gold, Red");
		list.add(temp1);
		HashMap<String, String> temp2 = new HashMap<String, String>();
		temp2.put("pen", "Parker");
		temp2.put("price", "400.00$");
		temp2.put("color", "Gold, Blue");
		list.add(temp2);
		HashMap<String, String> temp3 = new HashMap<String, String>();
		temp3.put("pen", "Sailor");
		temp3.put("price", "500.00$");
		temp3.put("color", "Silver");
		list.add(temp3);
		HashMap<String, String> temp4 = new HashMap<String, String>();
		temp4.put("pen", "Porsche Design");
		temp4.put("price", "600.00$");
		temp4.put("color", "Silver, Grey, Red");
		list.add(temp4);
	}

	private void populateList2() {
		// TODO: getSession
		SharedPreferences settings = getSharedPreferences("LoadSensinsgApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?IdXarxa=002&?session="
				+ settings.getString("session", "");
		;
		Log.i(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaSensorsArray = new JSONArray(jsonString);

			// Definim HashMap per guardar llista de HashMap xarxa
			ArrayList<HashMap<String, String>> llistaSensorsList = new ArrayList<HashMap<String, String>>();
			// List<HashMap<String, String>> llistaXarxesList2 = new
			// List<HashMap<String, String>>();

			HashMap<String, String> xarxa = null;

			for (int i = 0; i < llistaSensorsArray.length(); i++) {
				JSONObject xarxaJSON = llistaSensorsArray.getJSONObject(i);
				xarxa = new HashMap<String, String>();
				xarxa.put("id", xarxaJSON.getString("id"));
				xarxa.put("sensor", xarxaJSON.getString("sensor"));
				xarxa.put("canal", xarxaJSON.getString("canal"));
				xarxa.put("tipus", xarxaJSON.getString("tipus"));
				xarxa.put("descripcio", xarxaJSON.getString("Descripcio"));
				xarxa.put("poblacio", xarxaJSON.getString("Poblacio"));
				Log.i(DEB_TAG, xarxaJSON.getString("i"));

				list.add(xarxa);
			}
		} catch (Exception ex) {
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		Object o = this.getListAdapter().getItem(position);
		String pen = o.toString();
		Toast.makeText(this, "You have chosen the pen: " + " " + pen,
				Toast.LENGTH_LONG).show();
	}

}