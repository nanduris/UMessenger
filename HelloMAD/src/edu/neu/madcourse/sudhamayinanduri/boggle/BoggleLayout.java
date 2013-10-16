package edu.neu.madcourse.sudhamayinanduri.boggle;


import edu.neu.madcourse.sudhamayinanduri.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


// creates the layout to start or quit the boggle game 
public class BoggleLayout extends Activity implements OnClickListener {
	
	// button used
	Button newButton; // creates a new game
	Button ackButton; // opens acknowledgements
	Button about; // opens the rules of boggle game
	Button quitButton; // exits the game

	
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.boggle_main);
	
	      // Set up click listeners for all the buttons
	      ackButton = (Button) findViewById(R.id.ack);
	      ackButton.setOnClickListener(this);
	      newButton = (Button) findViewById(R.id.newButton);
	      newButton.setOnClickListener(this);
	      about = (Button) findViewById(R.id.aboutButton);
	      about.setOnClickListener(this);
	      quitButton = (Button) findViewById(R.id.quitButton);
	      quitButton.setOnClickListener(this);
	}

	// provides the method for clicking the buttons
	public void onClick(View v) {
		if(v == newButton){
			Intent i = new Intent(this, BoggleGame.class);
	         startActivity(i);
		            }
		else if(v == ackButton) {
			Intent i = new Intent(this, AcknowledgementBoggle.class);
	         startActivity(i);
					    	 }
		else if(v == about) {
			Intent i = new Intent(this, AboutBoggle.class);
	         startActivity(i);
					    	}
		else if(v == quitButton) {
			finish();
			return;
		}
						  }
}