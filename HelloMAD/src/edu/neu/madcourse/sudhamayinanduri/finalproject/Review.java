package edu.neu.madcourse.sudhamayinanduri.finalproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.HttpDownloader;
import edu.neu.mobileclass.apis.KeyValueAPI;
import edu.neu.mobileclass.apis.UploadAPI;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Review extends Activity {

	private String username;
	private String message;
	private String friend;
	private String latitude;
	private String longitude;
	private String type;
	private Button send;
	private String timer;
	private Date date;
	private Time time;
	private String msg1;
	private String latitude2;
	private String longitude2;
	
	private TextView to;
	private TextView msgType;
	private TextView msg;
	private TextView loc;
	private Button mainMenu;
	private int res;
	
	private ProgressDialog progressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_review_screen);
        
        
        username = Manager.getUsername();
        friend = Manager.getFriend();
        message = Manager.getMessage();
        latitude = Manager.getLatitude();
        longitude = Manager.getLongitude();
        latitude2 = Manager.getLatitude2();
        longitude2 = Manager.getLongitude2();
        
        type = Manager.getType();
        timer = Manager.getTimer();
        date = new Date(System.currentTimeMillis());
        time = new Time();
        time.setToNow();
        send = (Button) findViewById(R.id.button_send);
        mainMenu = (Button)findViewById(R.id.button_main_menu);
         
        progressDialog = new ProgressDialog(Review.this);
      	progressDialog.setMessage("Sending Message...");
      	progressDialog.setCancelable(false);
      	progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
        to = (TextView)findViewById(R.id.textView_to);
        msgType = (TextView)findViewById(R.id.textView_type);
        msg = (TextView)findViewById(R.id.textView_msg);
        loc = (TextView)findViewById(R.id.textView_loc);
        
        to.setText("To: "+ friend);
        msgType.setText("Type: " + type);
        msg.setText("Message: "+ message.replace("_", " ").replace("+", " "));
        loc.setText("Location: "+ getLocationNames(latitude, longitude).replace("_", " "));
        
        msg1 = "You_have_a_message_at_"+getLocationNames(latitude2, longitude2);
        
        
        	mainMenu.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					
					Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vib.vibrate(300);
					Intent i = new Intent(Review.this, MenuScreen.class);
					startActivity(i);
					
				}
			});
        	   	
        	send.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vib.vibrate(300);
			    try
			     {		
				    CheckReview check = new CheckReview();
				    check.execute("");
				 }
		         catch(Exception e)
		          {
		        	 Toast.makeText(Review.this, "Service Unavailable!", Toast.LENGTH_SHORT).show();
		          }
				}
			});
        	
        
        
    }
    
    
   
    
    public String getLocationNames(String latitude, String longitude) 
    {
    	Geocoder gcd = new Geocoder(Review.this, Locale.getDefault());
    	List<Address> addresses;
    	try 
    	{
    		
	    	addresses = gcd.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1);
	    
	    	if (addresses.size() > 0) 
	    	{
	    		return (addresses.get(0).getAddressLine(0) + " " +addresses.get(0).getLocality()+", "+addresses.get(0).getAdminArea()).replace(" ", "_");
	    	}
	    		
	    			
    	} 
    	catch (Exception e) 
    	{
	    	//Toast.makeText(this, "Could not get location name", Toast.LENGTH_SHORT).show();
	    }
    	return null;
		
	}

    public class CheckReview extends AsyncTask<String, Void, String>
    {
    	
    	protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog.show();		
		}

		@Override
		protected String doInBackground(String... params) {
			

			 try
		     {	
		    	
			        	if(type.equals("text"))
			        	{
				        	String locationQueue = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.LATLONG_QUEUE);
				        	if(locationQueue.contains("ERROR"))
				        	{
				        	     res = 1;
				        	     return "";
				        	}
				        	locationQueue += " "+latitude+"+"+longitude;
				        	KeyValueAPI.put(S.TEAM, S.PASS, friend+S.LATLONG_QUEUE , locationQueue);
				        	
				        	String messageQueue = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.MSG_QUEUE);
				        	String mymsg = " "+username+"|"+latitude+"+"+longitude+"|"+message.replace(" ", "_")+"|"+type+"|"+time.hour+":"+time.minute+"|"+time.month+"/"+time.monthDay+"/"+time.year+"|"+"no|no";
				        	KeyValueAPI.put(S.TEAM, S.PASS, friend+S.MSG_QUEUE , messageQueue+mymsg);
				        	
				        	manageSent(mymsg, username+S.SENT);
				        	res = 2;
			        	}
			        	else if(type.equals("timer"))
			        	{
			        		String directMessage = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.DIRECT_MSGS);
			        		if(directMessage.contains("ERROR"))
				        	{
			        			 res = 1;
				        	     return "";
				        	}
			        		directMessage +=  username+"|"+latitude+"+"+longitude+"|"+"you_have_a_timer_message_at_"+getLocationNames(latitude, longitude)+"_in_"+timer+"_hours_from_"+username+" ";
			        		KeyValueAPI.put(S.TEAM, S.PASS, friend+S.DIRECT_MSGS, directMessage);
			        		
			        		String timerQueue = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.TIMER);
			        		String currTime = " "+System.currentTimeMillis()+"+"+timer;
			        		KeyValueAPI.put(S.TEAM, S.PASS, friend+S.TIMER, timerQueue+currTime);
			        		
			        		String timerMessages = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.TIMED_MSGS);
			        		String mymsg = " "+username+"|"+latitude+"+"+longitude+"|"+message.replace(" ", "_")+"|"+type+"|"+time.hour+":"+time.minute+"|"+time.month+"/"+time.monthDay+"/"+time.year+"|"+"no|no";
			        		KeyValueAPI.put(S.TEAM, S.PASS, friend+S.TIMED_MSGS, timerMessages+mymsg);
			        		
			        	    String timerlatitlongi = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.TIMER_LATI_LONGI);
			        	    timerlatitlongi += " "+latitude+"+"+longitude;
			        	    KeyValueAPI.put(S.TEAM, S.PASS, friend+S.TIMER_LATI_LONGI, timerlatitlongi);
			        	    
			        	    manageSent(mymsg, username+S.SENT);
			        	    res = 2;
			        	    
			        	}
			        	else if(type.equals("teleport"))
			        	{
			        	    
			        		String locationQueue = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.LATLONG_QUEUE);
			        		if(locationQueue.contains("ERROR"))
				        	{
			        			res = 1;
				        	    return "";
				        	}
				        	locationQueue += " "+latitude+"+"+longitude;
				        	KeyValueAPI.put(S.TEAM, S.PASS, friend+S.LATLONG_QUEUE , locationQueue);
				        	
				        	String messageQueue = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.MSG_QUEUE);
				        	String mymsg = username+"~"+latitude2+"+"+longitude2+"~"+message.replace(" ", "_")+"~"+"text"+"~"+time.hour+":"+time.minute+"~"+time.month+"/"+time.monthDay+"/"+time.year+"~"+"no~no";
				        	messageQueue += " "+username+"|"+latitude+"+"+longitude+"|"+msg1+"|"+type+"|"+time.hour+":"+time.minute+"|"+time.month+"/"+time.monthDay+"/"+time.year+"|"+"no|"+mymsg;
				        	KeyValueAPI.put(S.TEAM, S.PASS, friend+S.MSG_QUEUE , messageQueue);
				        	
				        	manageSent(mymsg, username+S.SENT);
				        	res = 2;
				        	
			        	}
			     
						
			        	
	        	
			  }
	         catch(Exception e)
	          {
	        	 //Toast.makeText(Review.this, "Service unavailable", Toast.LENGTH_SHORT).show();
	          }
			
			return null;
		}
		
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
		    progressDialog.dismiss();
		    
		    switch (res) {
			case 1:
				Toast.makeText(Review.this, "Service Unavailable!", Toast.LENGTH_SHORT).show();
				break;

			case 2:
				Toast.makeText(Review.this, "Message Sent", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Review.this, MenuScreen.class);
	        	startActivity(intent);
				break;
			    
			default:
				break;
			}
		}
		
		
		 public void manageSent(String completeMessage, String key)
		    {
		    	//download the file edit it and upload it
		    	try{
					//download and edit the file and upload
					
		    		
		    		if(!type.equals("teleport"))
		    			completeMessage = completeMessage.replace(username+"|", friend+"|");
		    		else
		    			completeMessage = completeMessage.replace(username+"~", friend+"~");
		    		
		            File sdcard = Environment.getExternalStorageDirectory();
			        File file = new File(sdcard.getAbsolutePath()+"/numad/"+ key+".txt");
			        
			        if(file.exists())
			        {
			            file.delete();	
			        }
			        
				    String fileUrl = UploadAPI.download(S.TEAM, S.PASS, key);
				    String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1, fileUrl.length());
				    String downloadResult = HttpDownloader.downFile(fileUrl, "/numad/", filename);
			
				    BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
				    BufferedReader reader = new BufferedReader(new FileReader(file));
				    
				    if(!(reader.readLine() == null))
				    	writer.newLine();
				    
				    if(!type.equals("teleport"))
				    	writer.write(completeMessage.replace("|", " "));
		    		else
		    			writer.write(completeMessage.replace("~", " "));
				    
				    
				    writer.close();
				    reader.close();
				    UploadAPI.uploadFile(S.TEAM, S.PASS, key, sdcard.getAbsolutePath()+"/numad/"+ key+".txt");
				    
				    file.delete();
				}
				catch(Exception e)
				{
					//Toast.makeText(Review.this, "External storage unavailable", Toast.LENGTH_SHORT).show();
				}
		    	
		    }
    	
    }
}
