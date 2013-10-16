package edu.neu.madcourse.sudhamayinanduri.boggle;


import android.content.Context;
import android.media.MediaPlayer;

// provides the music for the boggle game
public class MusicBoggle {
	private static MediaPlayer mp = null;

	   // play the music
	   public static void play(Context context, int resource) {
	      stop(context);

	      // Start music only if not disabled in preferences
	      if (PrefsBoggle.getMusic(context)) {
	         mp = MediaPlayer.create(context, resource);
	         mp.start();
	      }
	   }
	   

	   // Stop the music
	   public static void stop(Context context) { 
	      if (mp != null) {
	         mp.stop();
	         mp.release();
	         mp = null;
	      }
	   }

}
