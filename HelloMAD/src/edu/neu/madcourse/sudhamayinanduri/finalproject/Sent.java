package edu.neu.madcourse.sudhamayinanduri.finalproject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.HttpDownloader;
import edu.neu.mobileclass.apis.UploadAPI;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Sent extends ListActivity {

	private ArrayList<Messages> msgList = null;
    private MessageAdapter msgAdapter;
    private ListView lv;
    private String username;
    private boolean check = false;
    private String key;
    
    private TextView top;
    private TextView middle;
    private TextView bottom;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_inbox);
       
       username = Manager.getUsername(); 
       lv = (ListView) findViewById(android.R.id.list);
       msgList = MessageMenu.sentList;      
       key = username+S.SENT;
       
       
       
       msgAdapter = new MessageAdapter(this, R.layout.u_simple_row, msgList);
       
         			
	     			
	   Collections.reverse(msgList);
	   for(Messages messages : msgList)
	   {
	     	msgAdapter.add(messages);
	   }
	   
        
        lv.setAdapter(msgAdapter);
        //ListView l = getListView();
        //l.setBackgroundColor(this.getResources().getColor(R.color.black));
        
    }

    
    
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	super.onListItemClick(l, v, position, id);
       
    	
    	Messages msg = (Messages)l.getItemAtPosition(position);
    	//Messages msg = msgList.get(position);
    	Intent intent = new Intent(Sent.this,ViewMessage.class);
    	
    	Manager.setMessage(msg.getMessage());
    	Manager.setDate(msg.getDate());
    	Manager.setTime(msg.getTime());
    	Manager.setType(msg.getType());
    	Manager.setLocation(msg.getLocation());
    	Manager.setFrom(msg.getFrom());
    	Manager.setMessages(msg);
    	startActivity(intent);
    	
    }
}
