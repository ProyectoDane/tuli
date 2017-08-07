package com.app.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnTouchListener {

	private Game game;
	private List<ImageResource> imageResourceList; //lista de imagenes del juego
	private static int currentImageResourcePosition = -1; //imagen actual del juego (-1 es antes de empezar, cuando empieza suma 1 y arranca en 0
	public static boolean playNext = true; //true es jugar el next, false es restart el actual. Cuando empieza es true, asi puede empezar el 1er juego.
	
	private ImageView answerSpace;
	private List<Feeling> feelingList;
	
	private int firstX;
	private int firstY;
	private boolean canMove=true;
	private boolean gameOver=false;

	private static MediaPlayer mediaPlayer=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//con estas 2 lineas se hace fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_main);
		
		//se cargan los sentimientos posibles
		feelingList = new ArrayList<Feeling>();
		Feeling aux;
		
		aux = new Feeling();
		aux.setImage((ImageView)findViewById(R.id.happy));
		aux.setId(Feeling.happy);
		aux.getImage().setOnTouchListener(this);
		aux.getImage().bringToFront();
		feelingList.add(aux);
	
		aux = new Feeling();
		aux.setImage((ImageView)findViewById(R.id.sad));
		aux.setId(Feeling.sad);
		aux.getImage().setOnTouchListener(this);
		aux.getImage().bringToFront();
		feelingList.add(aux);

		aux = new Feeling();
		aux.setImage((ImageView)findViewById(R.id.indifferent));
		aux.setId(Feeling.indifferent);
		aux.getImage().setOnTouchListener(this);
		aux.getImage().bringToFront();
		feelingList.add(aux);
		
		//espacio para arrastrar la respuesta
		answerSpace = (ImageView)findViewById(R.id.answerSpace);
		
		
		//se cargan las imagenes en la lista
		imageResourceList = new ArrayList<ImageResource>();
		ImageResource aux2;
		
		
		aux2 = new ImageResource();
		aux2.setId(R.drawable.img_4);
		aux2.setCorrectAnswer(Feeling.happy);
		imageResourceList.add(aux2);

		
		aux2 = new ImageResource();
		aux2.setId(R.drawable.img_2);
		aux2.setCorrectAnswer(Feeling.sad);
		imageResourceList.add(aux2);
		
		aux2 = new ImageResource();
		aux2.setId(R.drawable.img_3);
		aux2.setCorrectAnswer(Feeling.happy);
		imageResourceList.add(aux2);
		
		
		aux2 = new ImageResource();
		aux2.setId(R.drawable.img_1);
		aux2.setCorrectAnswer(Feeling.sad);
		imageResourceList.add(aux2);
		
		if(mediaPlayer==null){ //si no existe se crea
			mediaPlayer = MediaPlayerFactory.create(this, R.raw.music, true);	
		}
		mediaPlayer.start();
		
	}
	

	@Override
	protected void onStart() {
		super.onStart();
		
		//randomFeelingList(); //TODO no anda aca, porq todo esta en la posicion 0
		
		if (imageResourceList==null || imageResourceList.size()==0){ //no hay imagenes
			return; //TODO mostrar algun mensaje de que no hay imagenes
		}
		
		if (playNext){
			if (imageResourceList!=null && currentImageResourcePosition + 1 < imageResourceList.size()){
				currentImageResourcePosition++;
			}
			else{
				currentImageResourcePosition=0; //vuelve a empezar la lista de imagenes
			}	
		}
		
		
		
		game = new Game();
		game.setImage((ImageView)findViewById(R.id.image));
		//RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.marco);
		//relativeLayout.setBackgroundResource((imageResourceList.get(currentImageResourcePosition).getId()));
		game.getImage().setImageResource((imageResourceList.get(currentImageResourcePosition).getId()));
		game.setCorrectAnswer(imageResourceList.get(currentImageResourcePosition).getCorrectAnswer());
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mediaPlayer!=null){
			mediaPlayer.start();	
		}
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		if(mediaPlayer!=null){
			mediaPlayer.pause();	
		}
	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		for (Feeling feeling : feelingList){
			if (feeling.isFirstMove()){ 
				feeling.setOriginalPositions();
				feeling.setFirstMove(false);
			}
			
			switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN : {
					if (v.getId()==feeling.getImage().getId() && !gameOver){
						canMove=true;
						firstX = (int)event.getRawX();
						firstY = (int)event.getRawY();
					}
				}
				
				case MotionEvent.ACTION_MOVE : {
					if (v.getId()==feeling.getImage().getId() && !gameOver && canMove){
		                 int secondX = (int)event.getRawX();
		                 int secondY = (int)event.getRawY();
						 
		                 int deltaX = secondX - firstX;
		                 int deltaY = secondY - firstY;
		                 
		                 //el nuevo first pasa a ser el second
		                 firstX = secondX;
		                 firstY = secondY;
		                 
		                 feeling.getImage().layout(feeling.getImage().getLeft() + deltaX , feeling.getImage().getTop() + deltaY, feeling.getImage().getRight() + deltaX, feeling.getImage().getBottom()+ deltaY);
		                 
		         		 //chequea si solto dentro del circulo de answer
		         		 if(checkIfIsInside(answerSpace, feeling.getImage())){
		         			 canMove=false;
		         			 Boolean checkAnswer = checkAnswer(game, feeling);
		         			 if(checkAnswer || checkAnswer==null){ //correcto!
		         				gameOver = true; //termino el juego 
		         				feeling.getImage().layout(answerSpace.getLeft(), answerSpace.getTop(), answerSpace.getRight(), answerSpace.getBottom());
		         				
		         				//se hace una pausa de medio segundo para que quede la imagen puesta y despues se pasa a la actividad de que gano
		         				new Handler().postDelayed(new Runnable() {
		         		            @Override
		         		            public void run() {
		         		            	final Intent intent = new Intent(MainActivity.this, WinActivity.class);
		         		                mediaPlayer.pause();
		         		                startActivity(intent);
		         		                finish();
		         		            }
		         		        }, 500);

		         				/*
		         				
		         				Intent intent = new Intent(this, WinActivity.class);
		         		        startActivity(intent);
		         		        finish();
		         		        */
		         		        
		         				
		         			 }
		         			 else{
		         				 if (!checkAnswer){ //incorrecto
		         					 feeling.setOriginalPositionToImageWithAnimation();	 
		         				 }
		         			 }
		         		 }
					}
				}
				default:
					break;
			}
		}
		
		return true;
	}
	

	


	/**
	 * 
	 * @param image2
	 * @param feeling
	 * @return true si es correcta, false si no es correcta. null si no hay respuesta correcta en la imagen.
	 */
	private Boolean checkAnswer(Game game, Feeling feeling) {
		Boolean result = null;
		
		int correctAnswer = game.getCorrectAnswer();
		if (correctAnswer == -1){
			result = null; //no hay una respuesta correcta en la imagen
		}
		
		int currentAnswer = feeling.getId();
		if (correctAnswer == currentAnswer){
			result = true;
		}
		else{
			result = false;
		}
		
		return result;
	}



	/**
	 * chequea si feeling esta dentro de answer
	 * @param answer
	 * @param feeling
	 */
	private boolean checkIfIsInside(ImageView answer, ImageView feeling) {
		int shift = 20;
		if (answer!=null && feeling!=null){
			if ((feeling.getLeft()>answer.getLeft() && feeling.getLeft()<answer.getLeft()+answer.getWidth()-shift && feeling.getTop()>answer.getTop() && feeling.getTop()<answer.getTop()+answer.getHeight()-shift)
			 || (feeling.getRight()<answer.getRight() && feeling.getRight()>answer.getRight()-answer.getWidth()-shift && feeling.getTop()>answer.getTop() && feeling.getTop()<answer.getTop()+answer.getHeight()-shift)
			 || (feeling.getLeft()>answer.getLeft() && feeling.getLeft()<answer.getLeft()+answer.getWidth()-shift && feeling.getBottom()<answer.getBottom() && feeling.getBottom()>answer.getBottom()-answer.getHeight()-shift)
			 || (feeling.getRight()<answer.getRight() && feeling.getRight()>answer.getRight()+answer.getWidth()-shift && feeling.getBottom()<answer.getBottom() && feeling.getBottom()>answer.getBottom()-answer.getHeight()-shift)	
		    ){
				return true;
			}
			else{
				return false;
			}
			
		}
		
		return false;
		
	}



	
	
	private void randomFeelingList(){
		if (feelingList!=null && feelingList.size()>0){
			ArrayList<Integer> auxList = new ArrayList<Integer>();
			for (int i=0; i<feelingList.size(); i++){
				auxList.add(i);
			}
			
			long seed = System.nanoTime();
			Collections.shuffle(auxList, new Random(seed));
			
			//se cambian de posicion las imagenes de feelings. En la lista continuan en la misma posicion
			for (int i=0; i<auxList.size(); i++){
				Feeling aux = feelingList.get(auxList.get(i));
				feelingList.get(i).getImage().layout(aux.getImage().getLeft(), aux.getImage().getTop(), aux.getImage().getRight(), aux.getImage().getBottom());
			}
		}
	}



	

}
