package edu.neu.madcourse.sudhamayinanduri.boggle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import edu.neu.madcourse.sudhamayinanduri.R;

@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError" })
public class Boggle extends View {
	   
	// defines the tag
	   private static final String TAG = "Sudoku";

	   // variables to access the tiles
	   private static final String SELX = "selX"; 
	   private static final String SELY = "selY";
	   private static final String VIEW_STATE = "viewState";
	   private static final int ID = 42; 

	   
	   private float width;    // width of one tile
	   private float height;   // height of one tile
	   private int selX;       // X index of selection
	   private int selY;       // Y index of selection
	   private final Rect selRect = new Rect();

	   private final BoggleGame game;
	   
	   // boolean flags 
	   private boolean firstTime = true;
	   private boolean pause = false; 
	   
	   // stores the letters generated
	   private String letters[][] = new String[4][4];
	   
	   // empty array
	   private String emptyletters[][] = new String[4][4];
	   
	   // creates the layout
	   public Boggle(Context context) {
	      
	      super(context);
	      this.game = (BoggleGame) context;
	      setFocusable(true);
	      setFocusableInTouchMode(true);
	      setId(ID); 
	   }

	   @Override
	   protected Parcelable onSaveInstanceState() { 
	      Parcelable p = super.onSaveInstanceState();
	      Log.d(TAG, "onSaveInstanceState");
	      Bundle bundle = new Bundle();
	      bundle.putInt(SELX, selX);
	      bundle.putInt(SELY, selY);
	      bundle.putParcelable(VIEW_STATE, p);
	      return bundle;
	   }
	  // @Override
	   protected void onRestoreInstanceState(Parcelable state) { 
	      Log.d(TAG, "onRestoreInstanceState");
	      Bundle bundle = (Bundle) state;
	      select(bundle.getInt(SELX), bundle.getInt(SELY));
	      super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
	   }
	   
	   // draws the screen of 4 * 4 array for Boggle game
	   @Override
	   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	      width = w / 4f;
	      height = h / 4f;
	      getRect(selX, selY, selRect);
	      Log.d(TAG, "onSizeChanged: width " + width + ", height "
	            + height);
	      super.onSizeChanged(w, h, oldw, oldh);
	   }

	   //draws the Boggle Game
	   @Override
	   protected void onDraw(Canvas canvas) {
		   
	      // Draw the background
	      Paint background = new Paint();
	      background.setColor(getResources().getColor(
	            R.color.puzzle_background));
	      canvas.drawRect(0, 0, getWidth(), getHeight(), background);

	      
	      // Draw the board
	      
	      // Define colors for the grid lines
	      Paint hilite = new Paint();
	      hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

	      
	      //Fill the emptyletters with empty strings
	      for (int i=0; i<4; i++)
	      {
	    	  for(int j=0; j<4; j++)
	    	  {
	    		  emptyletters[i][j] = " ";
	    	  }
	      }

	      // Draw the grid lines
	      for (int i = 0; i < 4; i++) {
	         canvas.drawLine(0, i * height, getWidth(), i * height,
	               hilite);
	         canvas.drawLine(0, i * height + 1, getWidth(), i * height
	               + 1, hilite);
	         canvas.drawLine(i * width, 0, i * width, getHeight(),
	               hilite);
	         canvas.drawLine(i * width + 1, 0, i * width + 1,
	               getHeight(), hilite);
	      }

	      // Draw the numbers...
	      // Define color and style for numbers
	      Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
	      foreground.setColor(getResources().getColor(
	            R.color.puzzle_foreground));
	      foreground.setStyle(Style.FILL);
	      foreground.setTextSize(height * 0.75f);
	      foreground.setTextScaleX(width / height);
	      foreground.setTextAlign(Paint.Align.CENTER);

	      // Draw the number in the center of the tile
	      FontMetrics fm = foreground.getFontMetrics();
	      // Centering in X: use alignment (and X at midpoint)
	      float x = width / 2;
	      // Centering in Y: measure ascent/descent first
	      float y = height / 2 - (fm.ascent + fm.descent) / 2;

	      //if the letters are generated firsttime
	      if(firstTime == true)
	      {
	    	  for (int i = 0; i < 4; i++) {
	 	         for (int j = 0; j < 4; j++) {
	 	            canvas.drawText(generateLetters(i, j), i
	 	                  * width + x, j * height + y, foreground);
	 	      }
	    	  }
	 	         firstTime = false;
	      }
	      // if the letters are redrawn after a pause/resume
	      else
	      {
	    	if  (pause == true) // draws empty tiles on pause
	      {
	    	  for (int i = 0; i < 4; i++) {
		 	         for (int j = 0; j < 4; j++) {
		 	        	canvas.drawText(emptyletters[i][j], i
			 	                  * width + x, j * height + y, foreground);
		 	         }
	      }
	      }
	      
	      	else // re-draws the previous letters on resume
	      {
	    	  for (int i = 0; i < 4; i++) {
		 	         for (int j = 0; j < 4; j++) {
		 	        	canvas.drawText(letters[i][j], i
			 	                  * width + x, j * height + y, foreground);
		 	         }
	      }
	      }
	      }

	      // Draw the selection
	     Log.d(TAG, "selRect=" + selRect);
	      Paint selected = new Paint();
	      selected.setColor(getResources().getColor(
	            R.color.puzzle_selected));
	      canvas.drawRect(selRect, selected);
	   }

	// generates the letters for the boggle game
	private String generateLetters(int i, int j) {
		  letters[i][j] = this.game.getTileString(i, j);
				return letters[i][j];
				
			
	}

		@Override
	   // Gets called in pressing a button
	   public boolean onTouchEvent(MotionEvent event) {
	      if (event.getAction() != MotionEvent.ACTION_DOWN)
	         return super.onTouchEvent(event);
	      System.out.println("Action is :::::::::::>>> "+event.getAction());
	      select((int) (event.getX() / width),
	            (int) (event.getY() / height));
	      
	      Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);
	      this.game.word_typed.setText(this.game.result);
	      return true;
	   }
		
		// on selecting a tile
	   private void select(int x, int y) {
		  System.out.println("test");
	      invalidate(selRect);
	      selX = Math.min(Math.max(x, 0), 8);
	      selY = Math.min(Math.max(y, 0), 8);
	      getRect(selX, selY, selRect);
	      this.game.getTile(selX,selY);
	      
	      System.out.println("Rect X and Y after actiondown : " + selX +" --- " + selY);
	      invalidate(selRect);
	   }
 
	   // sets the rectangle
	   private void getRect(int x, int y, Rect rect) {
	      rect.set((int) (x * width), (int) (y * height), (int) (x
	            * width + width), (int) (y * height + height));
	   }
	   
	   // when pause is called
	   public void pause(boolean b)
	   {
		   System.out.println("Entered Pause method");
		   pause = b;
		   for (int i = 0; i < 4; i++) {
	 	         for (int j = 0; j < 4; j++) {
	 	            drawOnPause(i, j);
	 	      }
		   }   
	   }

	   // the tiles to be drawn on pause
	   private void drawOnPause(int i, int j) {
		// TODO Auto-generated method stub
		   System.out.println("test");
		   invalidate(selRect);
		   selX = Math.min(Math.max(i, 0), 8);
		   selY = Math.min(Math.max(j, 0), 8);
		   getRect(selX, selY, selRect);
		   invalidate(selRect);
	}
	}


