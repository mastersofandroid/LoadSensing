package com.loadsensing.app;

import android.content.Intent;
import android.os.Bundle;

import com.loadsensing.client.IntentIntegrator;
import com.loadsensing.client.IntentResult;

public class QRActivity extends DashboardActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr);

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