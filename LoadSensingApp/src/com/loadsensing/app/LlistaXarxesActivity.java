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

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.loadsensing.client.JsonClient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * This is the activity for feature 1 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class LlistaXarxesActivity extends DashboardActivity 
{
	private static final String DEB_TAG = "Json_Android";
	private String SERVER_HOST="http://viuterrassa.com/Android/getLlistatXarxes.php";

	/**
	 * onCreate
	 *
	 * Called when the activity is first created. 
	 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
	 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().
	 *
	 * @param savedInstanceState Bundle
	 */
	
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.llista_xarxes_activity);
	    setTitleFromActivityLabel (R.id.title_text);
	    //call the backend using Get parameters
	  	/*	String address = SERVER_HOST;
	  		JSONObject json = JsonClient.connect(address);
	  		HashMap<String, String> map = new HashMap<String, String>();
	  		try {
	  			map.put("session", json.getString("session"));
	  			Log.i(DEB_TAG, json.getString("session"));
	  		}
	  		catch(Exception e)
	  		{
	  			Log.i(DEB_TAG, "ERROR EN L'ENVIAMENT");
	  		}	 
	  		*/   
	}    
	    
	    
}
