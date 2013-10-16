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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class MessageMenu extends TabActivity 
{
	
	private ProgressDialog progressDialog;
	private String username;
	static ArrayList<Messages> inboxList;
	static ArrayList<Messages> sentList;
	private File sdcard;
	private File file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.u_message_menu);
		
		inboxList = new ArrayList<Messages>();
		sentList = new ArrayList<Messages>();
		
		username = Manager.getUsername();
		progressDialog = new ProgressDialog(MessageMenu.this);
		progressDialog.setMessage("Loading Messages...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	 
		
		Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
		
		Intent intentCompose = new Intent().setClass(this, Friends.class);
		TabSpec tabSpecCompose = tabHost
		  .newTabSpec("Compose New Message")
		  .setIndicator("", ressources.getDrawable(R.drawable.ic_compose))
		  .setContent(intentCompose);
 
		// Apple tab
		
		Intent intentInbox = new Intent().setClass(this, Inbox.class);
		TabSpec tabSpecInbox = tabHost
		  .newTabSpec("Inbox")
		  .setIndicator("", ressources.getDrawable(R.drawable.ic_inbox))
		  .setContent(intentInbox);
 
		// Windows tab
		Intent intentSent = new Intent().setClass(this, Sent.class);
		TabSpec tabSpecSent = tabHost
		  .newTabSpec("Sent Messages")
		  .setIndicator("", ressources.getDrawable(R.drawable.ic_outbox))
		  .setContent(intentSent);
        
		tabHost.addTab(tabSpecCompose);
		tabHost.addTab(tabSpecInbox);
		tabHost.addTab(tabSpecSent);
		
		tabHost.setCurrentTab(0);
		
		Download download = new Download();
		download.execute("");
	}
	
	
	public class Download extends AsyncTask<String, Void, String>
	{

		protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog.show();		
		}
		
		@Override
		protected String doInBackground(String... params) 
		{
		   
			if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
			{
	            //Toast.makeText(MessageMenu.this, "External SD card not mounted", Toast.LENGTH_LONG).show();
	        }
			else 
			{
				
				try {
					handleFiles(username+S.INBOX, inboxList);
					handleFiles(username+S.SENT, sentList);
				} 
				catch (Exception e) 
				{
					inboxList.clear();
					sentList.clear();
				}
				
			}
			return null;
		}
		
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			
		}
		
		public void handleFiles(String key, ArrayList<Messages> list)
		{
			
			try {
	        	   
      	         //Download if there is internet.. if there is no internet use shared preferences
      	   
      	          sdcard = Environment.getExternalStorageDirectory();
	   	          file = new File(sdcard.getAbsolutePath()+"/numad/"+ key+".txt");
	   	        
	   	          
	   	          if(file.exists())
	   	          { 
	   	            file.delete();
	   	          }
	   	          file = new File(sdcard.getAbsolutePath()+"/numad/"+ key+".txt");
	              String fileUrl = UploadAPI.download(S.TEAM, S.PASS, key);
	           	  String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1, fileUrl.length());
	           	  String downloadResult = HttpDownloader.downFile(fileUrl, "/numad/", filename);
	           	
	           	  if(downloadResult.contains("ERROR"))
	           	  {
	           		  
	           		  list.clear();
	           		  return;  
	           	  }
	           	  
	              list.clear();  
	           	  BufferedReader reader = new BufferedReader(new FileReader(file));
   			      String line;
   			
   		    	  while((line = reader.readLine()) != null)
   			      {
   				     Messages msg = parseLine(line);
   				
   					 //msgAdapter.add(msg);
   					 list.add(msg);
   				
   			     }
   			     reader.close();
	     			
	   		} catch (Exception e) 
	   		{
	   			list.clear();
	   			if(file.exists())
	   			{
	   				file.delete();
	   			}
	   			//Toast.makeText(MessageMenu.this, "No internet", Toast.LENGTH_SHORT).show();
	   		}
		}
		
		public Messages parseLine(String line)
	    {
	    	
	        Messages msg = new Messages();
	    	Scanner scan = new Scanner(line);
	    	msg.setFrom(scan.next());
	    	msg.setLocation(parseLocation(scan.next()));
	    	msg.setMessage(scan.next());
	    	msg.setType(scan.next());
	    	msg.setDate(scan.next());
	    	msg.setTime(scan.next());
	    	msg.setUnRead(scan.next());
	    	
	    	return msg;
	    	
	    }
		
		
		public String parseLocation(String latandlong)
	    {
	        
	        	char[] latlong = latandlong.toCharArray();
	        	boolean divide = false;
	        	String latitude = "";
	        	String longitude = "";
	        	
	        	for(int i=0; i<latlong.length;i++)
	        	{
	            	if(latlong[i] == '+')
	            	{
	            	   divide = true;
	            	}
	            	else if(!divide)
	            	{
	            	   latitude += latlong[i];	
	            	}
	            	else if(divide)
	            	{
	            		longitude += latlong[i];
	            	}
	        	}
	        	return getLocationNames(latitude, longitude);
	        
	    }
	    
	    public String getLocationNames(String latitude, String longitude) 
	    {
	    	
	    	if(latitude.equals("United"))
	    	{
	    	     return "United_States";	
	    	}
	    	else
	    	{
		    	Geocoder gcd = new Geocoder(MessageMenu.this, Locale.getDefault());
		    	List<Address> addresses;
		    	try 
		    	{
		    		
			    	addresses = gcd.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1);
			    
			    	if (addresses.size() > 0) 
			    	{
			    		String add = (addresses.get(0).getAddressLine(0) + " " +addresses.get(0).getLocality()+", "+addresses.get(0).getAdminArea()).replace(" ", "_");
			    		
			    		add = add.replace("null", "");
			    		return add;
			    	}
			    		
			    			
		    	} 
		    	catch (Exception e) 
		    	{
			    	//Toast.makeText(MessageMenu.this, "GPS Unavailable", Toast.LENGTH_SHORT).show();
			    }
	    	}
	    	return null;
			
		}
	    
	}

}
