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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the activity for feature 1 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class LlistaXarxesActivity extends ListActivity 
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
        setContentView(R.layout.custom_list_view);
	    setTitleFromActivityLabel (R.id.title_text);
	    //call the backend using Get parameters
	    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	    SimpleAdapter adapter = new SimpleAdapter(
        		this,
        		list,
        		R.layout.custom_row_view,
        		new String[] {"nom","poblacio","sensors","lat","lon"},
        		new int[] {R.id.text1,R.id.text2,R.id.text3,R.id.text4,R.id.text5}
        		);
        //carregarLlistaXarxes();
	    
	    String address = SERVER_HOST+"?session=1325718000";
	    Log.i(DEB_TAG, "Requesting to "+address);     
	  	
	    try {
		  	String jsonString = JsonClient.connectString(address);
		  	
		  	//Convertim la resposta string a un JSONArray
		  	JSONArray llistaXarxesArray = new JSONArray(jsonString);
		  	
		  	//Definim HashMap per guardar llista de HashMap xarxa
		  	ArrayList<HashMap<String, String>> llistaXarxesList = new ArrayList<HashMap<String, String>>();
		  	//List<HashMap<String, String>> llistaXarxesList2 = new List<HashMap<String, String>>();
	  		
		  	HashMap<String, String> xarxa = null;	  		
	  		
	  		for(int i=0;i < llistaXarxesArray.length();i++){
	  			xarxa = new HashMap<String, String>();
	  			JSONObject xarxaJSON = new JSONObject();
	  			xarxaJSON = llistaXarxesArray.getJSONObject(i);
	  			
		  		xarxa.put("id", String.valueOf(i));
		  		xarxa.put("poblacio", xarxaJSON.getString("Poblacio"));
		  		xarxa.put("nom", xarxaJSON.getString("Nom"));
		  		xarxa.put("idXarxa", xarxaJSON.getString("IdXarxa"));
		  		xarxa.put("sensors", "Sensors: " + xarxaJSON.getString("Sensors"));
		  		xarxa.put("lat", "Latitud: " + xarxaJSON.getString("Lat"));
		  		xarxa.put("lon", "Longitud: " + xarxaJSON.getString("Lon"));
		  		Log.i("log--> ", xarxaJSON.getString("Poblacio"));
		  		list.add(xarxa);
	  		}
	  		setListAdapter(adapter);
	        
	  	}
	  	catch(Exception e)
	  	{
	  		Log.i(DEB_TAG, "Error rebent xarxes");
	  	}       
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{

	    super.onListItemClick(l, v, position, id);
	    Object o = this.getListAdapter().getItem(position);
	    String pen = o.toString();
	    Toast.makeText(this, "Has seleccionat la xarxa " + " " + pen, Toast.LENGTH_LONG).show(); 
	}
	

/*	private void carregarLlistaXarxes() {
    	//TODO: getSession
	    String address = SERVER_HOST+"?session=1325718000";
	    Log.i(DEB_TAG, "Requesting to "+address);
	    
	  	try {
		  	String jsonString = JsonClient.connectString(address);
		  	
		  	//Convertim la resposta string a un JSONArray
		  	JSONArray llistaXarxesArray = new JSONArray(jsonString);
		  	
		  	//Definim HashMap per guardar llista de HashMap xarxa
		  	ArrayList<HashMap<String, String>> llistaXarxesList = new ArrayList<HashMap<String, String>>();
		  	//List<HashMap<String, String>> llistaXarxesList2 = new List<HashMap<String, String>>();
	  		
		  	HashMap<String, String> xarxa = new HashMap<String, String>();	  		
	  		
	  		for(int i=0;i < llistaXarxesArray.length();i++){
	  			JSONObject xarxaJSON = new JSONObject();
	  			xarxaJSON = llistaXarxesArray.getJSONObject(i);
	  			
		  		xarxa.put("id", String.valueOf(i));
		  		xarxa.put("poblacio", xarxaJSON.getString("Poblacio"));
		  		xarxa.put("nom", xarxaJSON.getString("Nom"));
		  		xarxa.put("idXarxa", xarxaJSON.getString("IdXarxa"));
		  		xarxa.put("sensors", xarxaJSON.getString("Sensors"));
		  		xarxa.put("lat", xarxaJSON.getString("Lat"));
		  		xarxa.put("lon", xarxaJSON.getString("Lon"));
		  		Log.i("log--> ", xarxaJSON.getString("Poblacio"));
		  		list.add(xarxa);
	  		}
	  		
	  	}
	  	catch(Exception e)
	  	{
	  		Log.i(DEB_TAG, "Error rebent xarxes");
	  	}	 
	  		  
	}    
	*/
	
	public void setTitleFromActivityLabel (int textViewId)
	{
	    TextView tv = (TextView) findViewById (textViewId);
	    if (tv != null) tv.setText (getTitle ());
	} // end setTitleText
	    
}
