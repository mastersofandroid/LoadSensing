package com.loadsensing.app;

import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;

public class SplashActivity extends DashboardActivity {

	protected boolean active = true;

	// Set the showing time for the Splash screen
	protected int splashTime = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(SplashActivity.this);

		Log.d("location", settings.getString("location", "es"));
		Locale locale = new Locale(settings.getString("location", "es"));
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

		Thread timer = new Thread() {
			public void run() {
				try {
					int waited = 0;
					while (active && (waited < splashTime)) {
						sleep(100);
						if (active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					finish();
					// Start activity when finishing the splash screen
					startActivity(new Intent(
							"com.loadsensing.app.LOGINACTIVITY"));
				}

			}
		};
		timer.start();
	}

	/*
	 * Force splashScreen to finish on touching the screen
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			active = false;
		}
		return true;
	}
}
