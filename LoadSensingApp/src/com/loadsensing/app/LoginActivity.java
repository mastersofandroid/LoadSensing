package com.loadsensing.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.loadsensing.client.JsonClient;

public class LoginActivity extends DashboardActivity implements OnClickListener {

	private String SERVER_HOST = "http://viuterrassa.com/Android/login.php";
	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);

		// load up the layout
		setContentView(R.layout.login);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(LoginActivity.this);

		// Set "Remember username and password" 1/0 if checked/unchecked
		CheckBox rememberUserPassword = (CheckBox) findViewById(R.id.remember_user_password);
		rememberUserPassword.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (getSharedPreference("saveusername").equals("1")) {
					setSharedPreference("saveusername", "0");
				} else {
					setSharedPreference("saveusername", "1");
				}
			}
		});

		// Restore preferences if "Remember username and password" is checked
		if (getSharedPreference("saveusername").equals("1")) {

			rememberUserPassword.setChecked(true);
			EditText editText = (EditText) findViewById(R.id.txt_username);
			editText.setText(getSharedPreference("login"));
			editText = (EditText) findViewById(R.id.txt_password);
			editText.setText(getSharedPreference("password"));
		}

		// get the button resource in the xml file and assign it to a local
		// variable of type Button
		Button login = (Button) findViewById(R.id.login_button);
		login.setOnClickListener(this);

		if (settings.getBoolean("autologin", false)) {
			login.performClick();
		}

		// Delete session
		setSharedPreference("session", "");

	}

	private String getSharedPreference(String fieldName) {
		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		return settings.getString(fieldName, "");
	}

	private void setSharedPreference(String fieldName, String value) {
		SharedPreferences settings = getSharedPreferences("LoadSensingApp",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(fieldName, value);
		editor.commit();
		Log.d(DEB_TAG, "SharedPreferences. fieldName = " + fieldName
				+ " set to " + value);
	}

	public void setUserNameText(String $username) {
		EditText usernameEditText = (EditText) findViewById(R.id.txt_username);
		usernameEditText.setText($username);
	}

	public void setPasswordText(String $username) {
		EditText passwordEditText = (EditText) findViewById(R.id.txt_password);
		passwordEditText.setText($username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		// this gets the resources in the xml file
		// and assigns it to a local variable of type EditText
		EditText usernameEditText = (EditText) findViewById(R.id.txt_username);
		EditText passwordEditText = (EditText) findViewById(R.id.txt_password);

		// the getText() gets the current value of the text box
		// the toString() converts the value to String data type
		// then assigns it to a variable of type String
		String sUserName = usernameEditText.getText().toString();
		String sPassword = passwordEditText.getText().toString();

		// Definimos constructor de alerta para los avisos posteriores
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog = new AlertDialog.Builder(this).create();

		// call the backend using Get parameters
		String address = SERVER_HOST + "?user=" + sUserName + "&pass="
				+ sPassword + "";

		if (sUserName.equals("") || sPassword.equals("")) {
			// error alert
			alertDialog.setTitle(getResources().getString(R.string.error));
			alertDialog
					.setMessage("Tiene que informar el nombre de usuario y contraseña");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			alertDialog.show();
		} else if (!checkConnection(this.getApplicationContext())) {
			alertDialog.setTitle(getResources().getString(R.string.error));
			alertDialog.setMessage(getResources().getString(
					R.string.error_no_internet));
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			alertDialog.show();
		} else {
			try {
				showBusyCursor(true);
				progress = ProgressDialog.show(this,
						getResources()
								.getString(R.string.pantalla_espera_title),
						getResources().getString(R.string.iniciando_sesion),
						true);

				Log.d(DEB_TAG, "Username: " + sUserName + " nPassword: "
						+ sPassword);
				Log.d(DEB_TAG, "Requesting to " + address);

				JSONObject json = JsonClient.connectJSONObject(address);

				CheckBox rememberUserPassword = (CheckBox) findViewById(R.id.remember_user_password);
				if (rememberUserPassword.isChecked()) {
					setSharedPreference("login", sUserName);
					setSharedPreference("password", sPassword);
				} else {
					setSharedPreference("login", "");
					setSharedPreference("password", "");
				}

				Log.d(DEB_TAG, "Session: " + json.getString("session"));

				if (json.getString("session") != "0") {
					progress.dismiss();
					// Guardamos la session en SharedPreferences para utilizarla
					// posteriormente
					setSharedPreference("session", json.getString("session"));
					// Sessión correcta. StartActivity de la home
					Intent intent = new Intent();
					intent.setClass(this.getApplicationContext(),
							HomeActivity.class);
					startActivity(intent);

				} else {
					progress.dismiss();
					alertDialog.setTitle(getResources().getString(
							R.string.error));
					alertDialog.setMessage(getResources().getString(
							R.string.error_login));
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							});
					alertDialog.show();
				}

			} catch (JSONException e) {
				progress.dismiss();
				Log.d(DEB_TAG, "Error parsing data " + e.toString());
			}

			showBusyCursor(false);

		}
	}

	private void showBusyCursor(Boolean $show) {
		setProgressBarIndeterminateVisibility($show);
	}

	private boolean checkConnection(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}
}