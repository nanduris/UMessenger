package edu.neu.madcourse.sudhamayinanduri.vikranthreddyetikyala.persistentboggle;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class CheckReq extends ListActivity {
	
    private List<String> list = new ArrayList<String>();
    private List<String> listDisplay = new ArrayList<String>();
    private List<String> randoms = new ArrayList<String>();
    String otherPlayer;
    String uploadStr;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME,0);
        String str = settings.getString("OutgoingReq"+getIntent().getExtras().getString("username"),"");
		
		
		System.out.println(str+"hey");
		
		Scanner scan = new Scanner(str);
		while(scan.hasNext())
		{
			String rand = scan.next();
			String p = settings.getString(rand,"");
			randoms.add(rand);
			list.add(p);
			listDisplay.add(p.substring(0,4));
			
		}
		setListAdapter(new ArrayAdapter<String>(this, R.layout.simple_row, listDisplay));
    }

    

	public void onListItemClick(ListView l, View v, int position, long id) {
		SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME,0);
		SharedPreferences putsettings = getSharedPreferences(PersistentMainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = putsettings.edit();
        
		String str = settings.getString("OutgoingReq"+getIntent().getExtras().getString("username"),"");
		String pair = list.get(position);
		String rand = randoms.get(position);
		
		Log.e("Please", str);
		Log.e("Check it", rand);
		
		editor.putString("OutgoingReq"+getIntent().getExtras().getString("username"), str.replace(" "+rand+" ", ""));
		editor.commit();
		
		putToServer("OutgoingReq"+getIntent().getExtras().getString("username"),str.replace(" "+rand+" ", ""));
		
		Scanner scan = new Scanner(pair);
		otherPlayer = scan.next();
		String genBoard = scan.next();
		Intent intent = new Intent(CheckReq.this,BoggleGame.class);
		intent.putExtra("username", getIntent().getExtras().getString("username"));
		Log.e("Player 1 is", getIntent().getExtras().getString("username"));
	    intent.putExtra("otherPlayer", otherPlayer);
	    intent.putExtra("player", "2");
		intent.putExtra("board", genBoard);
		intent.putExtra("rand", rand);
		
		/*uploadStr = str.replace(pair,"");
		SharedPreferences.Editor editor = settings.edit();
        editor.putString("OutgoingReq"+getIntent().getExtras().getString("username"),uploadStr);*/
		
	    startActivity(intent);
		
	}
	
	
	public void putToServer(String key, String value)
	{
		try{
		   KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE, key, value);
	   }
		catch(Exception e)
		{
		  Toast.makeText(CheckReq.this,  "No Internet", Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void onStart()
	{
		super.onStart();
		
		//synch with server
		// i have to put the values from server to shared pref
		
		try{
			SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME,0);
			SharedPreferences.Editor editor = settings.edit();
			String get = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, "OutgoingReq"+getIntent().getExtras().getString("username"));
			Log.e("checkHere",get);
			editor.putString("OutgoingReq"+getIntent().getExtras().getString("username"), get);
			editor.commit();
		}
		catch(Exception e){
			System.out.println("No internet available");
		}
	}
}


