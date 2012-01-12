package com.loadsensing.app;

import android.app.Activity;
import android.content.SharedPreferences;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		//Setting autologin preference
		Preference autologin = (Preference) findPreference("autologin");
		autologin
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object object) {
						SharedPreferences settings = getSharedPreferences(
								"LoadSensingApp", Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = settings.edit();
						//Default: no autologin
						int autologin_stored = Integer.parseInt(settings
								.getString("autologin", "0"));
						switch (autologin_stored) {
						case 0:
							editor.putString("autologin", "1");
						case 1:
							editor.putString("autologin", "0");
						}
						editor.commit();
						Toast.makeText(getBaseContext(),
								R.string.preference_set, Toast.LENGTH_LONG)
								.show();
						return true;
					}
				});

		//Setting locale preference
		final ListPreference location = (ListPreference) findPreference("location");
		location.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object object) {
				SharedPreferences settings = getSharedPreferences(
						"LoadSensingApp", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				String locale_stored = settings.getString("locale",
						"es");
				//java.util.Locale.getDefault().toString()
				
				Log.d("aa", location.getValue());
				
				editor.putString("locale", preference.getKey());			
				editor.commit();
				Toast.makeText(getBaseContext(), R.string.preference_set,
						Toast.LENGTH_LONG).show();
				return true;
			}
		});

	}

	/*
	 * @Override protected void onStart() { // TODO Auto-generated method stub
	 * super.onStart(); boolean CheckboxPreference; String ListPreference;
	 * String editTextPreference; String ringtonePreference; String
	 * secondEditTextPreference; String customPref;
	 * 
	 * 
	 * // Get the xml/preferences.xml preferences SharedPreferences settings =
	 * getSharedPreferences( "LoadSensingApp", Activity.MODE_PRIVATE);
	 * CheckboxPreference = settings.getBoolean("autologin", false);
	 * ListPreference = prefs.getString("listPref", "nr1"); editTextPreference =
	 * prefs.getString("editTextPref", "Nothing has been entered");
	 * ringtonePreference = prefs.getString("ringtonePref",
	 * "DEFAULT_RINGTONE_URI"); secondEditTextPreference =
	 * prefs.getString("SecondEditTextPref", "Nothing has been entered"); // Get
	 * the custom preference SharedPreferences mySharedPreferences =
	 * getSharedPreferences( "myCustomSharedPrefs", Activity.MODE_PRIVATE);
	 * customPref = mySharedPreferences.getString("myCusomPref", ""); } }
	 */
}
