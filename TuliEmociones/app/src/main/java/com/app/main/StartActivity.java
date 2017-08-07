package com.app.main;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class StartActivity extends Activity implements OnTouchListener {

	private ImageView start;
	private boolean touched = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//con estas 2 lineas se hace fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
		setContentView(R.layout.activity_start);
		
		start = (ImageView) findViewById(R.id.start);
		
		start.setOnTouchListener(this);
		
		//Intent svc=new Intent(this, BackgroundSoundService.class);
		//startService(svc);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		touched = false;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_start, menu);
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!touched){
			if (v.getId()==start.getId()){
				touched = true;
				Intent intent = new Intent(this, MainActivity.class);
		        startActivity(intent);
		        finish();
			}
		}
		
		return true;
	}

}
