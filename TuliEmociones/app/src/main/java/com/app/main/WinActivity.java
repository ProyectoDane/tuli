package com.app.main;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class WinActivity extends Activity implements OnTouchListener{
	
	private ImageView restart;
	private ImageView next;
	private boolean touched = false;
	
	private static MediaPlayer mediaPlayer=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//con estas 2 lineas se hace fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_win);
		
		restart = (ImageView) findViewById(R.id.restart);
		next = (ImageView) findViewById(R.id.next);
		
		//win.setOnTouchListener(this);
		restart.setOnTouchListener(this);
		next.setOnTouchListener(this);
		
		mediaPlayer = MediaPlayerFactory.create(this, R.raw.yes, false);
		mediaPlayer.start();
	}
	
	

	@Override
	protected void onStart() {
		super.onStart();
		touched = false;
	}
	
	

	@Override
	protected void onStop() {
		super.onStop();
		mediaPlayer.stop();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mediaPlayer.stop();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_win, menu);
		return true;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!touched){
			/*
			if (v.getId()==win.getId()){
				touched = true;
				Intent intent = new Intent(this, MainActivity.class);
		        startActivity(intent);
		        finish();
			}
			*/
			
			if (v.getId()==restart.getId()){
				if (!touched){
					touched = true;
					MainActivity.playNext = false; //restart
					Intent intent = new Intent(this, MainActivity.class);
			        startActivity(intent);
			        finish();
				}
			}
			else{
				if (v.getId()==next.getId()){
					if (!touched){
						touched = true;
						MainActivity.playNext = true;
						Intent intent = new Intent(this, MainActivity.class);
				        startActivity(intent);
				        finish();
					}
				}
			}
		}
		
		
		return true;
	}
	
	

	

}
