package com.app.main;

import android.annotation.SuppressLint;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class Feeling {
	
	public final static int happy = 1;
	public final static int sad = 2;
	public final static int indifferent = 3;
	
	private ImageView image;
	private int id;
	private int originalLeft;
	private int originalTop;
	private int originalRight;
	private int originalBottom;
	
	private float originalX;
	private float originalY;
	
	private boolean firstMove=true;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ImageView getImage() {
		return image;
	}
	public void setImage(ImageView image) {
		this.image = image;
	}
	
	public void setOriginalPositions(){
		this.originalLeft = image.getLeft();
		this.originalTop = image.getTop();
		this.originalRight = image.getRight();
		this.originalBottom = image.getBottom();
		
		if (android.os.Build.VERSION.SDK_INT >= 11){
			this.originalX = image.getX();
			this.originalY = image.getY();
		}
		else{
			this.originalX = image.getLeft();
			this.originalY = image.getTop();
		}
	}
	public int getOriginalLeft() {
		return originalLeft;
	}
	public void setOriginalLeft(int originalLeft) {
		this.originalLeft = originalLeft;
	}
	public int getOriginalTop() {
		return originalTop;
	}
	public void setOriginalTop(int originalTop) {
		this.originalTop = originalTop;
	}
	public int getOriginalRight() {
		return originalRight;
	}
	public void setOriginalRight(int originalRight) {
		this.originalRight = originalRight;
	}
	public int getOriginalBottom() {
		return originalBottom;
	}
	public void setOriginalBottom(int originalBottom) {
		this.originalBottom = originalBottom;
	}
	
	public void setOriginalPositionToImage(){
		this.image.layout(this.originalLeft, this.originalTop, this.originalRight, this.originalBottom);
	}
	
	public void setOriginalPositionToImageWithAnimation(){
		/*el TranslateAnimation funciona asi:
		  1er parametro: desde que distancia de X de donde se esta parado se quiere arrancar
		  2do parametro: cuanto de X trasladarse
		  3er parametro: desde que distancia de Y de donde se esta parado se quiere arrancar
		  4to parametro: cuanto de Y trasladarse
		  
		  por eso uso la original (punto destino originalX) - actual (getX())
		*/
		TranslateAnimation animation = new TranslateAnimation(0,this.originalX - this.image.getLeft(), 0, this.originalY - this.image.getTop());
		animation.setDuration(2000);
		animation.setFillAfter(false);
		animation.setAnimationListener(new MyAnimationListener(this));

		this.image.startAnimation(animation);
	}
	
	public boolean isFirstMove() {
		return firstMove;
	}
	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}
	
}
