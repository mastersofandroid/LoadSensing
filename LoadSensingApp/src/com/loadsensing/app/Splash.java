package com.loadsensing.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class Splash extends Activity {
	
	protected boolean active = true;
	/*
	 * Set the showing time for the Splash screen
	 */	
	protected int splashTime = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Thread timer = new Thread(){
			public void run(){
				try {
	                int waited = 0;
	                while(active && (waited < splashTime)) {
	                    sleep(100);
	                    if(active) {
	                        waited += 100;
	                    }
	                }
	            } catch(InterruptedException e) {
	                // do nothing
	            } finally {
	                finish();
	                startActivity(new Intent("com.loadsensing.app.LOADSENSINGAPPACTIVITY"));
	                stop();
	            }
				
			}
		};
		timer.start();
	}

	/*
	 * Force splashScreen to finish on touching the screen
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        active = false;
	    }
	    return true;
	}
}
