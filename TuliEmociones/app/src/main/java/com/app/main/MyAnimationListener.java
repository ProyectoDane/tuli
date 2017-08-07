package com.app.main;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class MyAnimationListener implements AnimationListener{
	private Feeling feeling;

	
	public MyAnimationListener(Feeling feeling) {
		this.feeling = feeling;
	}

	@Override
    public void onAnimationEnd(Animation animation) {
        this.feeling.getImage().clearAnimation();
        this.feeling.setOriginalPositionToImage();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

}