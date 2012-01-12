/*
 * Copyright (C) 2011 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.loadsensing.app;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DashboardActivity extends Activity {

	public static final String DEB_TAG = "LoadSensingApp_LOG";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_default);
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onRestart() {
		super.onRestart();
		// TODO Auto-generated method stub
		super.onDestroy();
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(DashboardActivity.this);
		Log.d("loc", settings.getString("location", ""));
		Locale locale = new Locale(settings.getString("location", "es"));
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getApplicationContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
		startActivity(getIntent());
		finish();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
	}

	/**
	 */
	// Click Methods

	/**
	 * Handle the click on the home button.
	 * 
	 * @param v
	 *            View
	 * @return void
	 */

	public void onClickHome(View v) {
		goHome(this);
	}

	/**
	 * Handle the click on the search button.
	 * 
	 * @param v
	 *            View
	 * @return void
	 */

	public void onClickSearch(View v) {
		startActivity(new Intent(getApplicationContext(), SearchActivity.class));
	}

	/**
	 * Handle the click on the About button.
	 * 
	 * @param v
	 *            View
	 * @return void
	 */

	public void onClickAbout(View v) {
		startActivity(new Intent(getApplicationContext(), AboutActivity.class));
	}

	/**
	 * Handle the click of a Feature button.
	 * 
	 * @param v
	 *            View
	 * @return void
	 */

	public void onClickFeature(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.llistatxarxes:
			startActivity(new Intent(getApplicationContext(),
					LlistaXarxesActivity.class));
			break;
		case R.id.localitzacioxarxes:
			startActivity(new Intent(getApplicationContext(), XarxaGMaps.class));
			break;

		case R.id.qrcode:
			startActivity(new Intent(getApplicationContext(), QRActivity.class));
			break;
		default:
			break;
		}
	}

	/**
	 */
	// More Methods

	/**
	 * Go back to the home activity.
	 * 
	 * @param context
	 *            Context
	 * @return void
	 */

	public void goHome(Context context) {
		final Intent intent = new Intent(context, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	public void goBack(View v) {
		finish();
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