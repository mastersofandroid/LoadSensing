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

import com.loadsensing.client.IntentIntegrator;
import com.loadsensing.client.IntentResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * This is the activity for feature 3 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class QRActivity extends DashboardActivity 
{

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
		setContentView (R.layout.qr);
		//setTitleFromActivityLabel (R.id.title_text);

		IntentIntegrator integrator = new IntentIntegrator(QRActivity.this);
		integrator.initiateScan();

	}

	protected void onActivityResult(int requestCode, int resultCode, 
			Intent data) { 
		switch(requestCode) { 
		case IntentIntegrator.REQUEST_CODE: { 
			if (resultCode != RESULT_CANCELED) { 
				IntentResult scanResult = 
						IntentIntegrator.parseActivityResult(requestCode, resultCode, data); 
				if (scanResult != null) { 
					String terminalID = scanResult.getContents();
					
					/*EditText editText = (EditText)findViewById(R.id.QRValue);
					editText.setText(terminalID);*/
					
					// Do whatever you want with the barcode...
					//TODO: Enviar el texto leído a la pantalla de sensor
				} 
			} 
			break; 
		} 
		} 
	} 

} // end class
