package edu.neu.madcourse.sudhamayinanduri.finalproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
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

public class FriendRequests extends ListActivity {

	private List<String> friends;
	private String username;
	private int position;
	private TextView tv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        friends = new ArrayList<String>();
        username = Manager.getUsername();
        
        try
        {
        	
	        	String friendreq = KeyValueAPI.get(S.TEAM, S.PASS, username+S.IN_REQUESTS);
	        	
	        	if(!friendreq.contains("ERROR"))
	        	{
		        	Scanner scan = new Scanner(friendreq);
		        	
		        	while(scan.hasNext())
		        	{
		        		friends.add(scan.next());
		        	}
		        	
		        	if(friends.isEmpty())
		        	{
		        		alertBuilder("You Have No Friend Requests!");
		        		friends.add("You Have No Friend Requests!");
		        	}
	        	}
	        	else if(friendreq.contains("ERROR"))
	        	{
	        		alertBuilder("No Internet! Check Your Wifi Settings!");
	        		friends.add("No Internet! Check Your Wifi Settings!");
	        	}
	        	
	        	
        }
        catch(Exception e)
        {
        	Toast.makeText(FriendRequests.this, "No Internet! Check Your Wifi Settings!", Toast.LENGTH_SHORT).show();
        }
        
        //Typeface tf = Typeface.createFromAsset(getAssets(), "agency.ttf");
        //tv = (TextView)findViewById(R.layout.u_friendreq_row);
        //tv.setTypeface(tf);
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.u_friendreq_row, friends));
        ListView lv = getListView();
        //lv.setBackgroundColor(this.getResources().getColor(R.color.black));
        lv.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.graysubtle_bg));
        lv.setScrollingCacheEnabled(false);
    }

    
    public void onListItemClick(ListView l, View v, int position, long id)
    {
    	
    	this.position = position;
    	
    	
        if(friends.get(position).equals("You Have No Friend Requests!") || friends.get(position).equals("No Internet! Check Your Wifi Settings!"))
        {
        	return;
        }
        
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FriendRequests.this);

		alertDialogBuilder
				.setMessage("Connect With "+ friends.get(position)+"?"
						).setPositiveButton(
						"Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								vib.vibrate(300);
								friendAdded();
							}
						}).setNegativeButton("No", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								vib.vibrate(300);
								frienddeleted();
								
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();	

		
		
    }
    
    
    public void friendAdded()
    {
    	
    	String friend = friends.get(position);
    	try
    	{
    		String frienreqs = KeyValueAPI.get(S.TEAM, S.PASS, username+S.IN_REQUESTS);
    		frienreqs = frienreqs.replace(friend, " ");
    		KeyValueAPI.put(S.TEAM, S.PASS, username+S.IN_REQUESTS, frienreqs);
    		
    		String otherfriend = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.OUT_REQUESTS);
    		otherfriend = otherfriend.replace(username, " ");
    		KeyValueAPI.put(S.TEAM, S.PASS, friend+S.OUT_REQUESTS, otherfriend);
    		
    		String addFriendtoList = KeyValueAPI.get(S.TEAM, S.PASS, username+S.FRIENDS);
    		KeyValueAPI.put(S.TEAM, S.PASS, username+S.FRIENDS, addFriendtoList+" "+friend);
    		
    		String toOtherList = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.FRIENDS);
    		KeyValueAPI.put(S.TEAM, S.PASS, friend+S.FRIENDS, toOtherList+" "+username);
    		
    		Toast.makeText(FriendRequests.this, friend+" Added Successfully", Toast.LENGTH_SHORT).show();
        	
    		Intent intent = new Intent(FriendRequests.this, FriendMenu.class);
    		startActivity(intent);
    	}
    	catch(Exception e)
    	{
    		Toast.makeText(FriendRequests.this, "No Internet! Check Your Wifi Settings!", Toast.LENGTH_SHORT).show();
    	}
    	

    }
    
    
    public void alertBuilder(String message)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FriendRequests.this);

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

    
    public void frienddeleted()
    {
    	String friend = friends.get(position);
    	try
    	{
    		String frienreqs = KeyValueAPI.get(S.TEAM, S.PASS, username+S.IN_REQUESTS);
    		frienreqs = frienreqs.replace(friend, " ");
    		KeyValueAPI.put(S.TEAM, S.PASS, username+S.IN_REQUESTS, frienreqs);
    		
    		String otherfriend = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.OUT_REQUESTS);
    		otherfriend = otherfriend.replace(username, " ");
    		KeyValueAPI.put(S.TEAM, S.PASS, friend+S.OUT_REQUESTS, otherfriend);
    		
    		
    		Toast.makeText(FriendRequests.this, "Friend Not Added ", Toast.LENGTH_SHORT).show();
        	
    		Intent intent = new Intent(FriendRequests.this, FriendMenu.class);
    		intent.putExtra("username", username);
    		startActivity(intent);
    	}
    	catch(Exception e)
    	{
    		Toast.makeText(FriendRequests.this, "No Internet! Check Your Wifi Settings!", Toast.LENGTH_SHORT).show();
    	}
    }
}
