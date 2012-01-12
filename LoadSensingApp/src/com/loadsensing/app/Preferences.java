package com.loadsensing.app;

import java.util.Locale;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(Preferences.this);
		Log.d("loc", settings.getString("location", ""));
		Locale locale = new Locale(settings.getString("location", "es"));
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getApplicationContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		// Setting autologin preference
		Preference autologin = (Preference) findPreference("autologin");
		autologin
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object object) {
						Toast.makeText(getBaseContext(),
								R.string.preference_set, Toast.LENGTH_LONG)
								.show();
						return true;
					}
				});

		// Setting locale preference
		ListPreference location = (ListPreference) findPreference("location");
		location.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object object) {
				Toast.makeText(getBaseContext(), R.string.preference_set,
						Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}
}
