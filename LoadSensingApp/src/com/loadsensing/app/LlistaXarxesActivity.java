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

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.loadsensing.client.JsonClient;

public class LlistaXarxesActivity extends ListActivity {
	private static final String DEB_TAG = "LoadSensingApp_LOG";
	private String SERVER_HOST = "http://77.228.158.13/Android/getLlistatXarxes.php";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xarxa_list_view);
		// call the backend using Get parameters
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.xarxa_row_list_view, new String[] { "nom", "poblacio",
						"sensors", "lat", "lon", "idXarxa", "nomThumbnail" },
				new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.text4,
						R.id.text5, R.id.text6, R.id.thumbnailxarxa });

		adapter.setViewBinder(new setImageListView());

		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		String address = SERVER_HOST + "?session="
				+ settings.getString("session", "");
		Log.d(DEB_TAG, "Requesting to " + address);

		try {
			String jsonString = JsonClient.connectString(address);

			// Convertim la resposta string a un JSONArray
			JSONArray llistaXarxesArray = new JSONArray(jsonString);

			HashMap<String, String> xarxa = null;

			for (int i = 0; i < llistaXarxesArray.length(); i++) {
				xarxa = new HashMap<String, String>();
				JSONObject xarxaJSON = new JSONObject();
				xarxaJSON = llistaXarxesArray.getJSONObject(i);
				int nomImatge = obtenirNomImatge(xarxaJSON
						.getString("IdXarxa"));
				Log.i(DEB_TAG, "nom: " + nomImatge);
				xarxa.put("id", String.valueOf(i));
				xarxa.put("poblacio", xarxaJSON.getString("Poblacio"));
				xarxa.put("nom", xarxaJSON.getString("Nom"));
				xarxa.put("idXarxa", xarxaJSON.getString("IdXarxa"));
				xarxa.put("sensors", xarxaJSON.getString("Sensors"));
				xarxa.put("lat", xarxaJSON.getString("Lat"));
				xarxa.put("lon", xarxaJSON.getString("Lon"));
				xarxa.put("nomThumbnail", Integer.toString(nomImatge));

				Log.d(DEB_TAG, xarxaJSON.getString("Poblacio"));
				list.add(xarxa);

			}
			setListAdapter(adapter);

		} catch (Exception e) {
			Log.d(DEB_TAG, "Error rebent xarxes");
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		/*
		 * Open Sensor
		 */
		TextView c = (TextView) v.findViewById(R.id.text6);
		String idxarxaselected = c.getText().toString();
		Log.d(DEB_TAG, "idxarxaseleccionada: " + c.getText().toString());

		Intent intent = new Intent();
		intent.setClass(this.getApplicationContext(), SensorsActivity.class);
		intent.putExtra("idxarxaselected", idxarxaselected);
		startActivity(intent);

	}

	public void goBack(View v) {
		finish();
	}

	public int obtenirNomImatge(String idXarxa) {

		int nomImatge = 0;

		switch (Integer.parseInt(idXarxa)) {
		case 0:
			nomImatge = R.drawable.sagradafamilia;
			break;
		case 1:
			nomImatge = R.drawable.torreagbar;
			break;
		case 2:
			nomImatge = R.drawable.pontvalencia;
			break;
		default:
			break;
		}
		return nomImatge;
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

	public class setImageListView implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			if ((view instanceof ImageView) & (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}

			return false;
		}
	}
}
