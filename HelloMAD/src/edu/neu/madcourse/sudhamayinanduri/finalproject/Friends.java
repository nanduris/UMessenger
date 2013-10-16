package edu.neu.madcourse.sudhamayinanduri.finalproject;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Friends extends ListActivity {

	private List<String> friends;
	private String username;
	private TextView txt;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        
        friends = new ArrayList<String>();
        username = Manager.getUsername();
        txt = (TextView) findViewById(R.id.text);
        
        
        try
        {
        	
	        	String friend = KeyValueAPI.get(S.TEAM, S.PASS, username+S.FRIENDS);
	    
	        	/*
	        	if(Manager.getCameFrom().equals("menuscreen"))
	            {
	            	anotheralertBuilder();
	            }*/
	        	
	        	if(!friend.contains("ERROR"))
	        	{
		        	Scanner scan = new Scanner(friend);
		        	
		        	while(scan.hasNext())
		        	{
		        		friends.add(scan.next());
		        	}
		        	if(friends.isEmpty())
		        	{
		        		alertBuilder("You Have No Friends!");
		        		friends.add("You Have No Friends!");
		        	}
	        	}
	        	else if(friend.contains("ERROR"))
	        	{
	        		alertBuilder("No Internet! Check Your Wifi Settings!");
	        		friends.add("No Internet! Check Your Wifi Settings!");
	        	}
        	
        }
        catch(Exception e)
        {
        	Toast.makeText(Friends.this, "Service Unavailable", Toast.LENGTH_SHORT).show();
        }
        
        //Typeface tf = Typeface.createFromAsset(getAssets(), "agency.ttf");
        
        //txt = (TextView)findViewById(R.layout.u_friendreq_row);
        
        //txt.setTypeface(tf);
        
        
        setListAdapter(new ArrayAdapter<String>(Friends.this, R.layout.u_friendreq_row, friends));
        ListView lv = getListView();
        //lv.setBackgroundColor(this.getResources().getColor(R.color.black));
        lv.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.graysubtle_bg)); 
        lv.setScrollingCacheEnabled(false);
    }

    
    public void alertBuilder(String message)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);

		alertDialogBuilder
				.setMessage(message
						).setPositiveButton(
						"OK",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								vib.vibrate(300);
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();	
	
    }
    
    public void anotheralertBuilder()
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);

		alertDialogBuilder
				.setMessage("Select Friend to Send Message"
						).setPositiveButton(
						"OK",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								vib.vibrate(300);

							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();	
	
    }
    
    public void onListItemClick(ListView l, View v, int position, long id)
    {
    	
    	if(friends.get(position).equals("You Have No Friends!") || friends.get(position).equals("No Internet! Check Your Wifi Settings!"))
    	{
    		return;
    	}
    	
    	Intent intent = new Intent(Friends.this, ComposeSelect.class);
    	Manager.setFriend(friends.get(position));
    	startActivity(intent);
    }
}
