/***
 * Copyright (c) 2012 David Garcia / Jose Antonio Gómez
 * mastersofandroid@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.loadsensing.app;

import android.content.Intent;
import android.os.Bundle;

import com.loadsensing.client.IntentIntegrator;
import com.loadsensing.client.IntentResult;

public class QRActivity extends DashboardActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr);

		//Lanzamos la aplicación Barcode Scanner
		IntentIntegrator integrator = new IntentIntegrator(QRActivity.this);
		integrator.initiateScan();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case IntentIntegrator.REQUEST_CODE: {
				if (resultCode != RESULT_CANCELED) {
					IntentResult scanResult = IntentIntegrator.parseActivityResult(
							requestCode, resultCode, data);
					if (scanResult != null) {
						//Con el texto leído, lo guardamos en un parámetro del Intent y lanzamos la actividad
						String idsensor = scanResult.getContents();
	
						Intent intent = new Intent();
						intent.setClass(this.getApplicationContext(),
								SingleSensorActivity.class);
						intent.putExtra("idsensorselected", idsensor);
						startActivity(intent);
						finish();
					}
				}
				break;
			}
		}
	}
}