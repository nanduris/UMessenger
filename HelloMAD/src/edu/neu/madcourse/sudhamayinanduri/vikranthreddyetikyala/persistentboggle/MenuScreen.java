/* Menu Screen which provides menu for the game*/
package edu.neu.madcourse.sudhamayinanduri.vikranthreddyetikyala.persistentboggle;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MenuScreen extends Activity {

	private Button initiate;
	private Button checkReq;
	private Button highScores;
	private Button noti;
	private EditText playerName;
	private Button logout;
	private boolean playerOne = false;
	private String playersQueue;
	private String userName;
	private final String[] boggleStrings = {"AAAFRS",
									"AAEEEE",
									"AAFIRS",
									"ADENNN",
									"AEEEEM",
									"AEEGMU",
									"AEGMNN",
									"AFIRSY",
									"BJKQXZ",
									"CCNSTW",
									"CEIILT",
									"CEILPT",
									"CEIPST",
									"DDLNOR",
									"DHHLOR",
									"DHHNOT",
									"DHLNOR",
									"EIIITT",
									"EMOTTT",
									"ENSSSU",
									"FIPRSY",
									"GORRVW",
									"HIPRRY",
									"NOOTUW",
									"OOOTTU"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);
        
        initiate = (Button) findViewById(R.id.initiate);
        checkReq = (Button) findViewById(R.id.checkReq);
        highScores = (Button) findViewById(R.id.high);
        logout = (Button) findViewById(R.id.logout);
        noti = (Button)findViewById(R.id.noti);
        playerName = (EditText) findViewById(R.id.playerName);
        userName = getIntent().getExtras().getString("username");
     
        noti.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				String s;
				
				try
				{
					System.out.println("Entered try");
					s = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, userName+"notique");
					System.out.println("notique:"+ s);
				}
				
				catch(Exception e)
				{
					s = "Cannot get notifications due to loss of internet connection";
					Toast.makeText(MenuScreen.this, "Internet Connection is lost!Game cannot be saved", Toast.LENGTH_SHORT).show();
				}
				
				Notification noti = new Notification(R.drawable.notification, "Notifications on Scores", System.currentTimeMillis());
				Intent intent = new Intent(MenuScreen.this, MenuScreen.class);
				intent.putExtra("username",userName);
				PendingIntent contentIntent = PendingIntent.getActivity(MenuScreen.this, 0, intent, 0);
			
				noti.setLatestEventInfo(getApplicationContext(), s , "", contentIntent);
				
				noti.flags = Notification.FLAG_AUTO_CANCEL;
				
				noti.defaults = Notification.DEFAULT_ALL;
			    
		        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		        nm.notify(1, noti);
				
				
			}
        	
        });
        
        initiate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				String otherPlayer = playerName.getText().toString();
				
				SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME,0);
				String usernames = settings.getString("usernames","");
				if(usernames.contains(otherPlayer))
				{
						Log.e(otherPlayer, getIntent().getExtras().getString("username"));
						String board = generateChars();
						SharedPreferences setting = getSharedPreferences(PersistentMainActivity.PREFS_NAME, 0);
				        SharedPreferences.Editor editor = setting.edit();
				        int random = (int) (Math.random() * 1000);
				        
				        Log.e("Random ", random+"");
				        String reqs = settings.getString("OutgoingReq"+otherPlayer,"");
				        Log.e("outGoingReq", reqs);
				        editor.putString("OutgoingReq"+otherPlayer,reqs+" " + random );
				        System.out.println("OutgoingReq"+otherPlayer+reqs+" " + random );
				        editor.putString(random+"", userName+" "+board+" 0 0 0 0");
				        //editor.putString(userName+"Score"+otherPlayer, "0 0");

				        //put in the server
				        putToServer("OutgoingReq",random+"",userName+" "+board,otherPlayer,reqs);
				        // Commit the edits!
				        editor.commit();
				   
				        
				        Intent intent = new Intent(MenuScreen.this,BoggleGame.class);
				        intent.putExtra("username", getIntent().getExtras().getString("username"));
						Log.e("Player 1 is", getIntent().getExtras().getString("username"));
					    intent.putExtra("otherPlayer", playerName.getText().toString());
					    intent.putExtra("player", "1");
					    intent.putExtra("rand", random+"");
					    
					    intent.putExtra("board", board);
					    startActivity(intent);
				}
		
				else
					Toast.makeText(MenuScreen.this, "No such user exists", Toast.LENGTH_SHORT).show();			
		
			}
		});
        
        checkReq.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(MenuScreen.this,CheckReq.class);
				i.putExtra("username", userName);
				startActivity(i);
				
			}

		});
        
        highScores.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				try
				{
					String alex = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, "alexhs");
					String bob = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, "bobhs");
					String john = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, "johnhs");
					String mary = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, "maryhs");
				    
				

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MenuScreen.this);
				alertDialogBuilder
						.setMessage("Score of "+alex+", Score of "+bob+", Score of "+john+", Score of "+mary)
						.setPositiveButton("ok",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

									}

								});
				
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				alertDialog.setCanceledOnTouchOutside(isRestricted());
			}
			
			catch(Exception e)
			{
				Toast.makeText(MenuScreen.this, "No internet to load high scores", Toast.LENGTH_SHORT).show();
			}
			}
		});
        
        logout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
				return;	
			}
		});
    }
    
    
    public void putToServer(String outGoingReq, String random, String board,String otherPlayerName,String reqs)
    {
    	try{
    		Log.e("puttoserver", board);
	    	KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE, outGoingReq+otherPlayerName, reqs+" "+random);
	    	KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE, random, board+" 0 0 0 0");
    	}
    	catch(Exception e)
    	{
    		Toast.makeText(MenuScreen.this, "No internet", Toast.LENGTH_SHORT).show();
    	}
    	
    }
    
    protected void onStart()
    {
    	super.onStart();
    	
    	try
    	{
    		// pull from server
    		String req = KeyValueAPI.get(Server.TEAM_NAME,Server.TEAM_PASSCODE,Server.OUTGOING_REQ + userName);
    		
    		SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            
    		Scanner scan = new Scanner(req);
    		while(scan.hasNext())
    		{
    			String getnext = scan.next();
    		    String board = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, getnext);	
    		    editor.putString(getnext, board);
    		}
    		
            editor.putString("OutgoingReq"+userName,req);
            // Commit the edits!
            editor.commit();
    	}
    	catch(Exception e)
    	{
    		Toast.makeText(MenuScreen.this, "No internet", Toast.LENGTH_SHORT).show();
    	}
		
    	
    }
    
    public String generateChars()
    {
    	Collections.shuffle(Arrays.asList(boggleStrings));
	
    	char[] boggleAlphabets = new char[25];
    	String board="";
		for(int i=0;i<25;i++)
		{
			int num = (int) (Math.random() * 5);
			boggleAlphabets[i] = boggleStrings[i].charAt(num);
			
		}
		for(int i=0;i<25;i++)
		{
			board += boggleAlphabets[i]; 
		}
		return board;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_screen, menu);
        return true;
    }
}
