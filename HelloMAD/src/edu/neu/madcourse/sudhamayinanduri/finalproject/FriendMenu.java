package edu.neu.madcourse.sudhamayinanduri.finalproject;

import edu.neu.madcourse.sudhamayinanduri.R;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class FriendMenu extends TabActivity implements OnTabChangeListener{

	private String username;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_friend_menu);
        username = Manager.getUsername();
        
        Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
		
		Intent intentView = new Intent().setClass(this, Friends.class);
		TabSpec tabSpecView = tabHost
		  .newTabSpec("View Contacts")
		  .setIndicator("", ressources.getDrawable(R.drawable.ic_contacts))
		  .setContent(intentView);
 
		// Apple tab
		Intent intentAdd = new Intent().setClass(this, AddFriend.class);
		TabSpec tabSpecAdd = tabHost
		  .newTabSpec("Add New Contact")
		  .setIndicator("", ressources.getDrawable(R.drawable.ic_new))
		  .setContent(intentAdd);
 
		// Windows tab
		Intent intentReq = new Intent().setClass(this, FriendRequests.class);
		TabSpec tabSpecReq = tabHost
		  .newTabSpec("New Add Requests")
		  .setIndicator("", ressources.getDrawable(R.drawable.ic_new_req))
		  .setContent(intentReq);
        
		tabHost.addTab(tabSpecView);
		tabHost.addTab(tabSpecAdd);
		tabHost.addTab(tabSpecReq);
		
		tabHost.setCurrentTab(0);
}
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		
	}
}
