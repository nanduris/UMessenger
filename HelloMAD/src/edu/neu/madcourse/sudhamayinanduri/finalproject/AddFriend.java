package edu.neu.madcourse.sudhamayinanduri.finalproject;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriend extends Activity {

	private EditText useridtext;
	private Button invite;
	private Button cMenu;
	
	private String username;
	private String userid;
	private TextView search;
	private ProgressDialog progressDialog;
	public int res;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_add_friend);
        
        useridtext = ((EditText) findViewById(R.id.friendid));
        invite = (Button) findViewById(R.id.button_invite);
        cMenu = (Button)findViewById(R.id.button_contact_menu);
        
        username = Manager.getUsername();
        
        search = (TextView)findViewById(R.id.textview_search);
   
        progressDialog = new ProgressDialog(AddFriend.this);
		progressDialog.setMessage("Adding Friend...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
        
        cMenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent i = new Intent(AddFriend.this, MenuScreen.class);
				startActivity(i);
				
			}
		});
        
        invite.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				userid = useridtext.getText().toString();
				try
		        {
					CheckAddFriends check = new CheckAddFriends();
					check.execute("");
		        	
		        }
		        catch(Exception e)
		        {
		        	Toast.makeText(AddFriend.this, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
		        }
			}
		});
        
        
    }

    
    public void alertBuilder(String message)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriend.this);

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
    
    public class CheckAddFriends extends AsyncTask<String, Void, String>
    {

    	protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog.show();		
		}
    	
		@Override
		protected String doInBackground(String... params)
		{
			
			String friends = KeyValueAPI.get(S.TEAM, S.PASS, username+S.FRIENDS);
        	String incomingOther = KeyValueAPI.get(S.TEAM, S.PASS, userid+S.IN_REQUESTS);
        	String incoming = KeyValueAPI.get(S.TEAM, S.PASS, username+S.IN_REQUESTS);
        	String outgoing = KeyValueAPI.get(S.TEAM, S.PASS, username+S.OUT_REQUESTS);
        	String userrecords = KeyValueAPI.get(S.TEAM, S.PASS, S.USER_RECORDS);
			
        	if(friends.contains("ERROR") || incomingOther.contains("ERROR") || incoming.contains("ERROR") || outgoing.contains("ERROR") || userrecords.contains("ERROR"))
        	{
        		res = 1;
        	}
        	else if(userid.equals(username))
        	{
        		res = 2;
        	}
        	else if(userid.contains(" ")  || !(userrecords.contains(" "+userid+"+")))//|| userid.length() < 3)
        	{
        		res = 3;
        	}
        	else if(friends.contains(" "+userid+" "))
        	{
        		res = 4;
        	}
        	else if(friends.contains(" "+userid))
        	{
        		res = 4;
        	}
        	else if(incomingOther.contains(" "+userid+" "))
        	{
        		res = 5;
        	}
        	else if(incoming.contains(" "+userid+" "))
        	{
        		res = 5;
        	}
        	else if(outgoing.contains(" "+userid+" "))
        	{
        		res = 6;
        	}
        	
        	else
        	{
        		
        		KeyValueAPI.put(S.TEAM, S.PASS, userid+S.IN_REQUESTS, incomingOther+" "+username+" ");
        		KeyValueAPI.put(S.TEAM, S.PASS, username+S.OUT_REQUESTS, outgoing+" "+userid+" ");
        		
        		String requests = KeyValueAPI.get(S.TEAM, S.PASS, userid+S.REQUESTS);
        		KeyValueAPI.put(S.TEAM, S.PASS, userid+S.REQUESTS, requests+" "+username);
        		res = 7;
        	}
        	
        	return null;
		}
		
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			switch (res) {
			case 1:
				alertBuilder("No Internet! Check Your Wifi Settings!");
				break;
				
			case 2:
				alertBuilder("Cannot Add Ones Self Id");
				break;
				
			case 3:
				alertBuilder("Oops.. User Not Found");
				break;
				
			case 4:
				alertBuilder("You Are Already Connected");
				break;
				
			case 5:	
				alertBuilder("Request Pending For "+userid);
				break;
				
			case 6:
				alertBuilder("Approval Pending From "+userid);
				break;
			
			case 7:
				Toast.makeText(AddFriend.this, "Friend Request Sent To "+userid, Toast.LENGTH_SHORT).show();
        		Intent intent = new Intent(AddFriend.this, FriendMenu.class);
        		startActivity(intent);
        		break;
				
			default:
				break;
			}
			
		} 
    	
    }
    
  }
