package com.loadsensing.app;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.loadsensing.client.JsonClient;

public class LoginActivity extends Activity implements OnClickListener {

	private static final String DEB_TAG = "Json_Android";

	private String SERVER_HOST="http://viuterrassa.com/Android/login.php";

	/*	public static final String PREFS_NAME = "HelloAndroidPREFS"; */
	private SharedPreferences settings;
	
	private ProgressDialog progress;       

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);

		// load up the layout
		setContentView(R.layout.login);

		// Restore preferences
		settings = getPreferences(MODE_PRIVATE);
		String storedUsername = settings.getString("Login", "");
		if (!storedUsername.equals("")){
			CheckBox rememberUserPassword = (CheckBox) findViewById(R.id.remember_user_password);
			rememberUserPassword.setChecked(true);
		}
		EditText editText = (EditText)findViewById(R.id.txt_username);
		editText.setText(storedUsername);
		String storedPassword = settings.getString("Password", "");
		editText = (EditText)findViewById(R.id.txt_password);
		editText.setText(storedPassword);
		// use the values
		
		// get the button resource in the xml file and assign it to a local variable of type Button
		Button login = (Button)findViewById(R.id.login_button);
		login.setOnClickListener(this);
	}

	public void setUserNameText(String $username){
		EditText usernameEditText = (EditText) findViewById(R.id.txt_username);
		usernameEditText.setText($username);
	}

	public void setPasswordText(String $username){
		EditText passwordEditText = (EditText) findViewById(R.id.txt_password);
		passwordEditText.setText($username);
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		// this gets the resources in the xml file
		//and assigns it to a local variable of type EditText
		EditText usernameEditText = (EditText) findViewById(R.id.txt_username);
		EditText passwordEditText = (EditText) findViewById(R.id.txt_password);

		// the getText() gets the current value of the text box
		// the toString() converts the value to String data type
		// then assigns it to a variable of type String
		String sUserName = usernameEditText.getText().toString();
		String sPassword = passwordEditText.getText().toString();

		//Definimos constructor de alerta para los avisos posteriores
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();  
		alertDialog = new AlertDialog.Builder(this).create(); 
		
		//call the backend using Get parameters
		String address = SERVER_HOST+"?user="+sUserName+"&pass="+sPassword+"";

		if(sUserName.equals("") || sPassword.equals("")){
			// error alert			 
			alertDialog.setTitle(getResources().getString(R.string.error));  
			alertDialog.setMessage("Tiene que informar el nombre de usuario y contraseña");  
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int which) {  
					return;  
				} });
			alertDialog.show();
		}else if (!checkConnection(this.getApplicationContext())) {
			alertDialog.setTitle(getResources().getString(R.string.error));  
			alertDialog.setMessage(getResources().getString(R.string.error_no_internet));  
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {  
					return;  
				} });
			alertDialog.show();
		}
		else {
			try {
				showBusyCursor(true);

				progress = ProgressDialog.show(this, getResources().getString(R.string.pantalla_espera_title), getResources().getString(R.string.iniciando_sesion), true);

				Log.i(DEB_TAG, "Username: " + sUserName + " nPassword: " + sPassword);
				Log.i(DEB_TAG, "Requesting to "+address);

				JSONObject json = JsonClient.connectJSONObject(address);

				progress.dismiss();
				
				/*
				 * TODO: SharedPreferences
				 */
				CheckBox rememberUserPassword = (CheckBox) findViewById(R.id.remember_user_password);
				SharedPreferences.Editor editor = settings.edit();
				if (rememberUserPassword.isChecked()){
					editor.putString("Login", sUserName);
					editor.putString("Password", sPassword);					
				}
				else
				{
					editor.putString("Login", "");
					editor.putString("Password", "");
				}
				editor.commit();
				
				HashMap<String, String> map = new HashMap<String, String>();

				//map.put("id",  String.valueOf(i));
				map.put("login", json.getString("login"));
				map.put("session", json.getString("session"));
				
				Log.i(DEB_TAG, json.getString("session"));
				String sessionOk = json.getString("session");
				if(map.get("session") != "0"){	
					
					//Sessión correcta. StartActivity de la home
					Intent intent = new Intent();
				    intent.setClass(this.getApplicationContext(), HomeActivity.class);
				    startActivity(intent);
					
				} else {
					alertDialog.setTitle(getResources().getString(R.string.error));  
					alertDialog.setMessage(getResources().getString(R.string.error_login));  
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {  
							return;  
						} });
					alertDialog.show();
				}

			} catch(JSONException e){
				Log.i(DEB_TAG, "Error parsing data "+e.toString());
			}

			showBusyCursor(false);

		}//end else
	}//end OnClick

	/*
	 *
	 */
	private void showBusyCursor(Boolean $show){
		setProgressBarIndeterminateVisibility($show);
	}
	
	private boolean checkConnection(Context ctx) {
		ConnectivityManager conMgr =  (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
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