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

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loadsensing.client.JsonClient;

import android.os.Bundle;
import android.util.Log;

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
	    
	    //TODO: getSession
	    String address = SERVER_HOST+"?session=1325718000";
	    Log.i(DEB_TAG, "Requesting to "+address);
	  	
	  	try {
		  	String jsonString = JsonClient.connectString(address);
		  	
		  	//Convertim la resposta string a un JSONArray
		  	JSONArray llistaXarxesArray = new JSONArray(jsonString);
		  	
		  	//Definim HashMap per guardar llista de HashMap xarxa
		  	ArrayList<HashMap<String, String>> llistaXarxesList = new ArrayList<HashMap<String, String>>();
	  		HashMap<String, String> xarxa = new HashMap<String, String>();	  		
	  		
	  		for(int i=0;i < llistaXarxesArray.length();i++){
	  			JSONObject xarxaJSON = llistaXarxesArray.getJSONObject(i);
	  			
		  		xarxa.put("id", String.valueOf(i));
		  		xarxa.put("poblacio", xarxaJSON.getString("Poblacio"));
		  		xarxa.put("nom", xarxaJSON.getString("Nom"));
		  		xarxa.put("idXarxa", xarxaJSON.getString("IdXarxa"));
		  		xarxa.put("sensors", xarxaJSON.getString("Sensors"));
		  		xarxa.put("lat", xarxaJSON.getString("Lat"));
		  		xarxa.put("lon", xarxaJSON.getString("Lon"));
		  		
		  		Log.i(DEB_TAG, xarxa.get("poblacio"));
		  		
		  		llistaXarxesList.add(xarxa);
	  		}
	  	}
	  	catch(Exception e)
	  	{
	  		Log.i(DEB_TAG, "Error rebent xarxes");
	  	}	 
	  		  
	}    
	    
	    
}
