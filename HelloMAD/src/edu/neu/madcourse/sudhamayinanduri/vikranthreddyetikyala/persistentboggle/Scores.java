package edu.neu.madcourse.sudhamayinanduri.vikranthreddyetikyala.persistentboggle;

import java.util.Map;

import java.util.Scanner;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HeterogeneousExpandableList;
import android.widget.TextView;

public class Scores extends Activity {

	private TextView myScore; 
	//private TextView otherScore;
	private Button okay;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        
        myScore = (TextView) findViewById(R.id.score);
        //otherScore = (TextView) findViewById(R.id.otherScore);
        myScore.setText(getIntent().getExtras().getString("username")+", your Score is: "+getIntent().getExtras().getString("score"));
        okay = (Button)findViewById(R.id.okay);
        
        okay.setOnClickListener(new OnClickListener() {
        	
			public void onClick(View v) {
				Intent i = new Intent(Scores.this, MenuScreen.class);
				i.putExtra("username", getIntent().getExtras().getString("username"));
				startActivity(i);
			}
		});
    
        //pullfromServerAgain();
    }

    /*private void pullfromServerAgain() {
    	try
    	{
    		String otherPlayerScore = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, getIntent().getExtras().getString("rand"));
            
            Scanner scan = new Scanner(otherPlayerScore);
            scan.next();
            scan.next();
            if(getIntent().getExtras().getString("player").equals("1"))
            {
            	scan.next();
            	String tmp = scan.next();
            	Log.e("Heyyyyyyyyyyy", tmp);
            	if(tmp == "0")
            	{
            		otherScore.setText("Game not ended by the other player");
            	}
            	else
            	{
            		otherScore.setText(tmp);
            	}
            }
            else if(getIntent().getExtras().getString("player").equals("2"))
            {
            	String tmp = scan.next();
            	Log.e("Heyyyyyyyyyyy", tmp);
            	if(tmp == "0")
            	{
            		otherScore.setText("Game not ended by other player");
            	}
            	else
            	{
            		otherScore.setText(tmp);
            	}
            }

            // Commit the edits!
            
    	}
    	catch(Exception e)
    	{
    		
    	} 	        
		
	}*/

	
    protected void onStart()
    {
    	super.onStart();
    	
    	//push to server
    	
    	try
    	{
    		SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME, 0);
    		
    		Map<String,?> map = settings.getAll();
    		
    			
    			for(String m : map.keySet())
    			{
    				KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE, m, map.get(m).toString());
    			}
    	}
    		catch(Exception e)
    		{
    			
    		}
    	}
    }

