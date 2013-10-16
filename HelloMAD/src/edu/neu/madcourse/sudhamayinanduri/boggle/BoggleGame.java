package edu.neu.madcourse.sudhamayinanduri.boggle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;


import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.madcourse.sudhamayinanduri.CreateError;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

// The logic of the main boggle game
public class BoggleGame extends Activity implements android.view.View.OnClickListener{

	// variables
	private static final String TAG = "Boggle";

	// an array to store the generated letters
	private String[] puzzle = new String[16];
	
	// a hashset to store the final list of words made (hashset doesnot allow duplicate words)
	private HashSet<String> finalList = new HashSet<String>();
	
	// a String Buffer to store a word made
	StringBuffer result = new StringBuffer();
	
	// an emtpy String Buffer
	StringBuffer resultTemp = new StringBuffer();
	
	//private String[] finalDisplay = new String[16];

	// the array which stores the same letters for each cube as in a real boggle game
	private final String[] cubes = { "aaeegn", "abbjoo", "achops", "affkps",
			"aoottw", "cimotu", "deilrx", "delrvy", "distty", "eeghnw",
			"eeinsu", "ehrtvw", "eiosst", "elrtty", "himnqu", "hlnnrz" };

	// an array to store the position of letter selected
	private int[] positionOfWordsSelected = new int[16];
	
	private Boggle boggle;
	
	// Buttons to navigate and perform various functions
	Button addword;
	Button clearWord;
	Button pause;
	Button resume;
	Button newGame;
	Button endGame;
	
	// TextViews to display score, timer, list of words and the current word being typed
	TextView word_typed;
	TextView timerCount;
	TextView score;
	TextView listOfWords;
	
	// variables to store the time
	long timer1;
	long timer2;
	
	// an increment variable
	int k=0;
	
	//int count = -1;
	// stores the current score 
	int scoreCount = 0;
	
	// creates the Integer Object of the score which is later converted to String
	public Integer scoreString = new Integer(scoreCount);

	
	
	int size = 0;	// Size of the word formed so far

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		boggle = new Boggle(this);
		setContentView(R.layout.boggle_layout);
	
		LinearLayout l = (LinearLayout) findViewById(R.id.score_layout);
		addword = (Button) findViewById(R.id.add_word);
		clearWord = (Button)findViewById(R.id.clear_word);
		pause = (Button) findViewById(R.id.pause);
		resume = (Button)findViewById(R.id.resume);
		newGame = (Button) findViewById(R.id.new_game);
		endGame = (Button)findViewById(R.id.end_game);
		word_typed = (TextView)findViewById(R.id.word_typed);
		timerCount = (TextView)findViewById(R.id.timer);
		score = (TextView)findViewById(R.id.score);
		listOfWords = (TextView)findViewById(R.id.list_of_words);
		
		//for(int i=0; i<16; i++)
		//{
			//finalDisplay[i] =" ";
		//}
		

		l.addView(boggle);
		boggle.requestFocus();

		addword.setOnClickListener(this);
		clearWord.setOnClickListener(this);
		pause.setOnClickListener(this);
		resume.setOnClickListener(this);
		newGame.setOnClickListener(this);
		endGame.setOnClickListener(this);
		word_typed.setText(result);	
		score.setText(scoreString.toString());
		resume.setEnabled(false);
		listOfWords.setText(finalList.toString());
	}

	/** Return the tile at the given coordinates */
	protected StringBuffer getTile(int x, int y) {
		System.out.println("Entering getTile()");
		MusicBoggle.play(this, R.raw.touch);
		 int letterPositionOnBoggleBoard;
		switch (y) {
		case 0:
			 letterPositionOnBoggleBoard = x;
			 System.out.println(letterPositionOnBoggleBoard);
			 break;
			
		case 1:
			letterPositionOnBoggleBoard = x + 4;
			System.out.println(letterPositionOnBoggleBoard);
			break;
		case 2:
			letterPositionOnBoggleBoard = x + 8;
			System.out.println(letterPositionOnBoggleBoard);
			break;
		case 3:
			letterPositionOnBoggleBoard = x + 12;
			System.out.println(letterPositionOnBoggleBoard);
			break;
		default: // never enters here
			letterPositionOnBoggleBoard = -1;
			System.out.println(letterPositionOnBoggleBoard);
			break;
			
		}
			// if the letter is not selected
			if (!(already_selected(letterPositionOnBoggleBoard)))
			{
				if (size == 0) // for the first letter
				{
					System.out.println("If size=0");
					positionOfWordsSelected[size] = letterPositionOnBoggleBoard;
					size = size + 1;
					return result.append(puzzle[letterPositionOnBoggleBoard]); // get an alphabet out from boggle puzzle board and assign to result
				}
		
				else // for the next letters
				{
					System.out.println("If size > 0, then size = " + size);
			
					int tempSize = size; // variable to store and point size to last accessed boggle board position
					int j = positionOfWordsSelected[--tempSize]; // Get the last selected alphabet on the boggle board :: Changed by sarang
					System.out.println("J " + j);
			
					// Check if user selects adjacent tile
					if( 
							(letterPositionOnBoggleBoard == (j + 1)) 
							|| (letterPositionOnBoggleBoard == (j - 1))
							||(letterPositionOnBoggleBoard == (j + 3)) 
							|| (letterPositionOnBoggleBoard == (j - 3)) 
							||(letterPositionOnBoggleBoard == (j + 4)) 
							|| (letterPositionOnBoggleBoard == (j - 4)) 
							||(letterPositionOnBoggleBoard == (j + 5)) 
							|| (letterPositionOnBoggleBoard == (j - 5))
							)
					{
						System.out.println(result);
						positionOfWordsSelected[size] = letterPositionOnBoggleBoard;
						size = size + 1;
						word_typed = (TextView)findViewById(R.id.word_typed);
						return result.append(puzzle[letterPositionOnBoggleBoard]);
					}
			
					// If user selects any of the non adjacent tile
					else
					{
						System.out.println("Result is:" + result);
						word_typed = (TextView)findViewById(R.id.word_typed);
						return result;
					}
				}
			}
			else // in non of the cases above, there is no change in the word
			{
				System.out.println("Result is:" + result);
				word_typed = (TextView)findViewById(R.id.word_typed);
				return result;
			}
			
			}

	// method to check of the letter has already been selected
	private boolean already_selected(int letterPositionOnBoggleBoard) {
		for(int i=0; i<=size; i++)
		{
			if(letterPositionOnBoggleBoard == positionOfWordsSelected[i])
			{
				return true;
			}
			else
				return false;
		}
		return false;
	}

	// method fills the puzzle with empty strings
	private void empty_fill()
	{
		for(int i=0; i<16; i++)
		{
			puzzle[i] = " ";
		}
	}
	
	// gets the generates letter for each tile when generated first time 
	protected String getTileString(int x, int y) {
		String s;
		switch (y) {
		case 0:
			s = getchar(x);
			puzzle[x] = s;
			System.out.println(s);
			System.out.println(puzzle[x]);
			break;
		case 1:
			s = getchar(x + 4);
			puzzle[x + 4] = s;
			break;
		case 2:
			s = getchar(x + 8);
			puzzle[x + 8] = s;
			break;
		case 3:
			s = getchar(x + 12);
			puzzle[x + 12] = s;
			break;
		default:
			s = "a";
			break;
		}
		return s;
	}

	// generates the letter for each tile
	protected String getchar(int i) {
		String cube = cubes[i];
		char cubeArray[] = cube.toCharArray();
		int j = 0 + (int) (Math.random() * ((5 - 0) + 1)); // to generate a
															// random side of a
															// cube
		char x = cubeArray[j];
		String y = Character.toString(x);
		return y;
	}
	
	// timer for 60 secs
	private long ts =60000;
	
	// the timer class
	CountDownTimer c =  new CountDownTimer(ts, 1000) {

	     public void onTick(long millisUntilFinished) {
	         timerCount.setText("TIMER: " + millisUntilFinished / 1000);
	         timer1 = millisUntilFinished / 1000;
	         
	     }

	     public void onFinish() {
	    	 
	    	 // clears all the buffers and sets textviews to null and deactivates the buttons
	         timerCount.setText("Time Over !!!");
	         //showScore();
	         result = new StringBuffer();
	         word_typed.setText(result);
	         puzzle = new String[16];
	         empty_fill();
	         pause.setEnabled(false);
	         resume.setEnabled(false);
	         addword.setEnabled(false);
	         clearWord.setEnabled(false);
	         boggle.pause(true);
	         
	     }
	     
	  }.start();
	  
	  

	// methods for clicking the buttons 
	public void onClick(View v) {

		// checks if the word is of minimum length of 3 letters
		if ((v == addword) && (result.length() >= 3) ) {

			AssetManager assetManager = getAssets();
			try {
				
				char firstLetter = result.charAt(0);

				System.out.println("########################### dictionary/"
						+ Character.toString(firstLetter).toLowerCase()
						+ ".txt");
				InputStream in;

				in = assetManager.open("dictionary/"
						+ Character.toString(firstLetter).toLowerCase()
						+ ".txt");
				System.out.println("Search for : " + result.toString());
				
				// finds for a search in dictionary
				if (find(in, result.toString())) {
					
					// If a valid word is formed then add the word to the final list of words made so far
					finalList.add(result.toString());
					scoreCount = scoreCount + 1;
					MusicBoggle.play(this, R.raw.win);
				}
				
				// the buffers and arrays are cleared for the next word to form
				result = new StringBuffer();
				positionOfWordsSelected = new int[16];
				size = 0;
				
				// the score is set and displayed
				scoreString = scoreCount;
				word_typed.setText(result);
				score.setText("Score:" + scoreString.toString());
				
				
				System.out.println("3");
				
				// the words formed are entered to the final list and are displayed 
				for (String s : finalList) {
					System.out.println("Entered 1");
					System.out.println("Words added are : " + s);
					
						System.out.println("Entered 2");
				}
				System.out.println(finalList.toString());
				listOfWords.setText("List of Words:" + finalList.toString());
				System.out.println("4");
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out
						.println("ERROR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
				e.printStackTrace();
			}

		}
		
		// clears the word typed
		else if(v == clearWord)
		{
			result = new StringBuffer();
			positionOfWordsSelected = new int[16];
			size = 0;
			word_typed.setText(result);
		}
		
		// on pausing the game
		else if(v == pause)
		{
			this.boggle.pause(true);
			resultTemp = result;
			word_typed.setText(" ");
			resume.setEnabled(true);
			pause.setEnabled(false);
			addword.setEnabled(false);
			clearWord.setEnabled(false);
			timer2 = timer1;
			System.out.println("Timer1 " + timer1);
			System.out.println("Timer2 " + timer2);
			c.cancel();
		}
		
		// on resuming the game
		else if(v == resume)
		{
			this.boggle.pause(false);	
			result = resultTemp;
			word_typed.setText(resultTemp);
			resultTemp = new StringBuffer();
			resume.setEnabled(false);
			pause.setEnabled(true);
			addword.setEnabled(true);
			clearWord.setEnabled(true);
			System.out.println("Timer1 " + timer1);
			System.out.println("Timer2 " + timer2);
			
			// creates a new timer on resume
			CountDownTimer c =  new CountDownTimer((timer2 * 1000), 1000) {

			     public void onTick(long millisUntilFinished) {
			         timerCount.setText("TIMER: " + millisUntilFinished / 1000);
			         timer1 = millisUntilFinished / 1000;
			         
			     }

			     public void onFinish() {
			    	 
			    	 // clears all the buffers and sets textviews to null and deactivates the buttons
			         timerCount.setText("Time Over !!!");
			         //showScore();
			         result = new StringBuffer();
			         word_typed.setText(result);
			         puzzle = new String[16];
			         empty_fill();
			         pause.setEnabled(false);
			         resume.setEnabled(false);
			         addword.setEnabled(false);
			         clearWord.setEnabled(false);
			         boggle.pause(true);
			         
			         
			     }
			     
			  }.start();	
		}
		
		// creates a new game
		else if(v == newGame)
		{
			Intent intent=new Intent(this,BoggleGame.class);
			startActivity(intent);
		}
		
		// quits the game
		else if(v == endGame)
		{
			finish();
			return;
		}
			
	}
	
	/*private void showScore() {
		Intent intent=new Intent(this,GameOver.class);
		 startActivity(intent);
		
	}*/

	// finds if the word is present in the dictionary
	public static boolean find(InputStream inputStream, String searchString) {
		System.out.println("Came in the find");
		System.out.println("Is it null : " + inputStream == null);
		boolean result = false;
		Scanner in = null;
		try {
			in = new Scanner(inputStream);
			System.out.println("1");
			while (in.hasNextLine() && !result) {
				String temp = in.nextLine();
				result = temp.equalsIgnoreCase(searchString);
			}
			System.out.println("2");
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}


