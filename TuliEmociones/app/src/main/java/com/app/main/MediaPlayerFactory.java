package com.app.main;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class MediaPlayerFactory {
	
	public static MediaPlayer create(Context context, int resourceId, boolean looping){
		MediaPlayer mediaPlayer = MediaPlayer.create(context, resourceId);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setLooping(looping);
		return mediaPlayer;
	}
	
	public static void play(Context context, int resourceId, boolean looping){
		MediaPlayer mediaPlayer = MediaPlayer.create(context, resourceId);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		/*if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		*/
		try {
			mediaPlayer.setLooping(looping);
			mediaPlayer.start();
			mediaPlayer.release();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
}
