package edu.neu.madcourse.sudhamayinanduri.finalproject;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Scanner;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.HttpDownloader;
import edu.neu.mobileclass.apis.KeyValueAPI;
import edu.neu.mobileclass.apis.UploadAPI;

public class MenuScreen extends Activity implements LocationListener{

	private ImageButton friends;
	private ImageButton composeMessages;
	private String username;
	private ProgressDialog progressDialog;
	private Handler handler;
	private static  double latitude;
	private static double longitude;
	private static double prevLatitude;
	private long time;
	private LocationManager locationManager;
	private Date date;
	private Time currTime;
	private boolean teleport;
	private String teleportMsg;
	
	
	private TextView contactstv;
	private TextView composetv;
	
	private Button logout;
	private TextView usertv;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_menu_screen);
        
        date = new Date(System.currentTimeMillis());
        currTime = new Time();
        currTime.setToNow();
        username = Manager.getUsername();
        handler = new Handler();
        friends = (ImageButton) findViewById(R.id.button_friends);
        composeMessages = (ImageButton) findViewById(R.id.button_messages);
        usertv = (TextView)findViewById(R.id.textView_username);
    	usertv.setText("Hello, " + username);
        
        logout = (Button)findViewById(R.id.button_logout);
        
        contactstv = (TextView)findViewById(R.id.textView1);
        composetv = (TextView)findViewById(R.id.textView2);
        
        
        
        
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Manager.setLocationManager(locationManager);
        Manager.setMenuScreen(MenuScreen.this);
        
        
		
		logout.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Manager.getLocationManager().removeUpdates(Manager.getMenuScreen());
				LoginScreen.isStarted = false;
				Intent intent = new Intent(MenuScreen.this, LoginScreen.class);
				startActivity(intent);
				 
			}
		});
		
		friends.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent intent = new Intent(MenuScreen.this,FriendMenu.class);
				startActivity(intent);
				
			}
		});
		
		contactstv.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent intent = new Intent(MenuScreen.this,FriendMenu.class);
				startActivity(intent);
				
			}
		});
		
		composeMessages.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent intent = new Intent(MenuScreen.this, MessageMenu.class);
				startActivity(intent);
				
			}
		});
		
		composetv.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Manager.setCameFrom("menuscreen");
				Intent intent = new Intent(MenuScreen.this, MessageMenu.class);
				startActivity(intent);
				
			}
		});
		
		
    }

    
    public void onStart()
    {
        super.onStart();
        
        if(!LoginScreen.isStarted)
        {
          getLocation();
          LoginScreen.isStarted = true;
        }
        
        /*
        time = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
			
			public void run() {
            
				while((latitude == 0) || (latitude == prevLatitude))
				{
					
					if((System.currentTimeMillis() - time) > 60000)
					{
						
					   handler.post(new Runnable() {
						
						public void run() {
							
							Toast.makeText(MenuScreen.this, "GPS Unavailable", Toast.LENGTH_SHORT).show();
						}
					});	
						
						break;
					}
						
				}
				progressDialog.dismiss();
			}
		});
        
        thread.start();
        */
                
    }
    
    public void getLocation()
    {	
      		locationManager.requestLocationUpdates("gps", 60000, 0, this);
    }
    
    public void onLocationChanged(Location location) 
	{
    	
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		
		
        try
        {
    			checkFriendRequests();
    			addRecentLocation();
    			directNotifications();
    			checkTimers();
    			detectMessages();
    			detecttimerMessages();
    			
	   }
	   catch(Exception e)
       {
		   Toast.makeText(MenuScreen.this, "No Internet! Check Your Wifi Settings!", Toast.LENGTH_SHORT).show();
       }
		
		/*
		handler.post(new Runnable() {
			
			public void run() {
				progressDialog.dismiss();
				 
			}
    	});
			*/
		
	}
    
    public void checkFriendRequests()
    {
    	try
    	{
    			String requests = KeyValueAPI.get(S.TEAM, S.PASS, username+S.REQUESTS);
    			
    			if(!requests.contains("ERROR"))
    			{
	    			Scanner scan = new Scanner(requests);
	    			
	    			while(scan.hasNext())
	    			{
	    				makeFriendRequestNotification(scan.next());
	    			}
	    		
	    		
	    		    KeyValueAPI.put(S.TEAM, S.PASS, username+S.REQUESTS, "");
    			}
    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    
    public void checkTimers()
    {
    	try
    	{
    		String timers = KeyValueAPI.get(S.TEAM, S.PASS, username+S.TIMER);
    		Scanner scan = new Scanner(timers);
    		int count = 0;
    		while(scan.hasNext())
    		{
    			String sentTime= "";
    			String timeLimit = "";
    			boolean divide = false;
    			char[] manageTime = scan.next().toCharArray();
    			
    			for(int i= 0;i < manageTime.length; i++)
    			{
    				if(manageTime[i] == '+')
    					divide = true;
    				else if(!divide)
    				    sentTime += manageTime[i];
    				else if(divide)
    					timeLimit += manageTime[i];
    			}
    			
    			if( (System.currentTimeMillis() - Long.valueOf(sentTime)) > (Double.valueOf(timeLimit) * 60 *60 * 1000) )
    			{
    				//send notification that timer has expired
    				//remove this from all the 4 queues
    				removefromTimerQueues(count);
    			}
    			count++;
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    
    
    public void removefromTimerQueues(int count)
    {
    	try
    	{
    		String timerQueue = KeyValueAPI.get(S.TEAM, S.PASS, username+S.TIMER);
    		String timerMessages = KeyValueAPI.get(S.TEAM, S.PASS, username+S.TIMED_MSGS);
    	    String timerlatitlongi = KeyValueAPI.get(S.TEAM, S.PASS, username+S.TIMER_LATI_LONGI);
    	    
    	    Scanner tq = new Scanner(timerQueue);
    	    Scanner tm = new Scanner(timerMessages);
    	    Scanner tl = new Scanner(timerlatitlongi);
    	    
    	    int i = 0;
    	    
    	    while(tq.hasNext())
    	    {
    	    	if(count == i)
    	    	{
    	    		String message = tm.next();
    	    		KeyValueAPI.put(S.TEAM, S.PASS, username+S.TIMER, timerQueue.replace(tq.next(), ""));
    	    		KeyValueAPI.put(S.TEAM, S.PASS, username+S.TIMED_MSGS, timerMessages.replace(message, ""));
    	    		KeyValueAPI.put(S.TEAM, S.PASS, username+S.TIMER_LATI_LONGI, timerlatitlongi.replace(tl.next(), ""));
    	    		
    	    		//String[] messages = message.split("|");
    	    		
    	    		String mess = message.replace("|", " ");
    	    		Scanner s = new Scanner(mess);
    	    		String n = s.next();
    	    		//editFile(message, username+S.INBOX);
    	    		
    	    		makeNotification("You Lost Timer Message From ",n);
    	    		
    	    		break;
    	    	}
    	    	tq.next();
    	    	tm.next();
    	    	tl.next();
    	    	i++;
    	    }
    	   
    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    
    public void addRecentLocation()
    {
    	
    	if(latitude != 0)
    	{
    		
    		prevLatitude = latitude;
	    	boolean addingLocation = true;
    		
	    	//scan through the list and check if the new location th user is in a duplicate location in his top 10
	    	try
	    	{
	    		String recent = KeyValueAPI.get(S.TEAM, S.PASS, username+S.RECENT);
	            Scanner scan = new Scanner(recent);
	    		
	    		while(scan.hasNext())
	    		{
	    			String currentLat = "";
	    	    	String currentLon = "";
	    			char[] latlong = scan.next().toCharArray();
	    			boolean divide = false;
	    			for(int i = 0; i < latlong.length; i++)
	    			{
	    				if(latlong[i] == '+')
	    				{
	    					divide = true;
	    				}
	    				else if(!divide)
	    				{
	    					currentLat += latlong[i];
	    				}
	    				else if(divide)
	    				{
	    					currentLon += latlong[i];
	    				}
	    			}
	    			
	    			//If the location is already in the list then return as this is a duplicate location
	    			if(radius(latitude, longitude, Double.valueOf(currentLat), Double.valueOf(currentLon)) < 0.3)
	    			{
	    				addingLocation = false;	
	    			}
	    		}
	    		
	    		if(addingLocation)
	    		  KeyValueAPI.put(S.TEAM, S.PASS, username+S.RECENT, latitude+"+"+longitude+" " + recent);
	    		
	    	}
	    	catch(Exception e)
	    	{
	    		
	    	}
    	}
    	
    }
    
    


	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
    
	
	public void detectMessages()
	{
		
		//check for latlong within 100 meters and store index i
		//iterate through msg_queue with same the same value if i
		//read the whole message and send a notification
		// now add this to sent users notification that the message is recieved
		if((latitude != 0))
		{
			try
			{
				
				String latandlong = KeyValueAPI.get(S.TEAM, S.PASS, username+S.LATLONG_QUEUE);
				Scanner scan = new Scanner(latandlong);
				
				int count = 0;
				
				while(scan.hasNext())
	    		{
					String currentLat = "";
					String currentLon = "";
	    			char[] latlong = scan.next().toCharArray();
	    			boolean divide = false;
	    			for(int i = 0; i < latlong.length; i++)
	    			{
	    				if(latlong[i] == '+')
	    				{
	    					divide = true;
	    				}
	    				else if(!divide)
	    				{
	    					currentLat += latlong[i];
	    				}
	    				else if(divide)
	    				{
	    					currentLon += latlong[i];
	    				}
	    			}
	    			
	  
	    		   if(radius(latitude, longitude, Double.valueOf(currentLat), Double.valueOf(currentLon)) < 0.2)
	    			{
	    				messageDetected(count,username+S.MSG_QUEUE);
	    				deleteFromQueue(latandlong,currentLat+"+"+currentLon,username+S.LATLONG_QUEUE);
	    				if(teleport)
	    					manageTeleport(teleportMsg);
	    				//rememember count and send notification and delete from both msg_queue and latlongqueue and download, upload it to users inbox;	
	    			}
	    			count++;
	    		}
	    		
			}
			catch(Exception e)
			{
				
			}
		}
		
	
	}
	
	
	public void detecttimerMessages()
	{
		if(latitude != 0)
		{
			try
			{
				
				String latandlong = KeyValueAPI.get(S.TEAM, S.PASS, username+S.TIMER_LATI_LONGI);
				Scanner scan = new Scanner(latandlong);
				
				int count = 0;
				
				while(scan.hasNext())
	    		{
					String currentLat = "";
					String currentLon = "";
	    			char[] latlong = scan.next().toCharArray();
	    			boolean divide = false;
	    			for(int i = 0; i < latlong.length; i++)
	    			{
	    				if(latlong[i] == '+')
	    				{
	    					divide = true;
	    				}
	    				else if(!divide)
	    				{
	    					currentLat += latlong[i];
	    				}
	    				else if(divide)
	    				{
	    					currentLon += latlong[i];
	    				}
	    			}
	    			
	  
	    			if(radius(latitude, longitude, Double.valueOf(currentLat), Double.valueOf(currentLon)) < 0.3)
	    			{
	    				timerMessageDetected(count);
	    				deleteFromQueue(latandlong,currentLat+"+"+currentLon,username+S.TIMER_LATI_LONGI);
	    				//rememember count and send notification and delete from both msg_queue and latlongqueue and download, upload it to users inbox;	
	    			}
	    			count++;
	    		}
	    		
			}
			catch(Exception e)
			{
				
			}
		}
	}

	
	public void timerMessageDetected(int count)
	{
		//delete it from timer queue
		//read the message from timermsg queue  and put it into inbox then delte it and send a notification abt it
		messageDetected(count, username+S.TIMED_MSGS);
		//delete from s.timer the timer
		removeFromTimer(count);
		
	}
	
	public void removeFromTimer(int count)
	{
		try
		{
			String times = KeyValueAPI.get(S.TEAM, S.PASS, username+S.TIMER);
			Scanner scan = new Scanner(times);
			int i = 0;
			
			while(scan.hasNext())
			{
				if(count == i)
				{
					String remove = scan.next();
				    KeyValueAPI.put(S.TEAM, S.PASS, username+S.TIMER, times.replace(remove, ""));
				    break;
				}
				i++;
				scan.next();
			}	
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void messageDetected(int count, String key)
	{
		try
		{
			String msgQueue = KeyValueAPI.get(S.TEAM, S.PASS, key);
			Scanner scan = new Scanner(msgQueue);
			int newCount = 0;
			
			while(scan.hasNext())
			{
				if(newCount == count)
				{
					String completeMessage = "";
					String from = "";
					String location = "";
					String message = "";
					String type = "";
					String date = "";
					String time = "";
					
					
					for(int i=0;i<=count;i++)
					{
						completeMessage = scan.next();
					}
					
					// the completemessage should be parsed
					Scanner scanMsg = new Scanner(completeMessage.replace("|", " "));
				    from = scanMsg.next();
				    location = scanMsg.next();
				    message = scanMsg.next();
				    type = scanMsg.next();
				    date = scanMsg.next();
				    time = scanMsg.next();
					scanMsg.next();
					 String tele = scanMsg.next();
					
					if(!tele.equals("no"))
					{
						KeyValueAPI.put(S.TEAM, S.PASS, "checker", tele);
						teleportMsg = tele;
						teleport = true;
					}
				    
				    Manager.setMessage(message);
		            editFile(completeMessage, username+S.INBOX);					    
				    //notificationn sudhmayi nanduri
					makeNotification("You Have Message From ",from);
				    
				    deleteFromQueue(msgQueue,completeMessage, key);
				}
				scan.next();
				newCount++;
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	
	public void deleteFromQueue(String queue, String msg, String key)
	{
		try
		{
			queue = queue.replace(msg, "");
			
			KeyValueAPI.put(S.TEAM, S.PASS, key, queue);
		}
		catch(Exception e)
		{
			
		}
	}
	
  
	
	public void directNotifications()
	{
		//have a directnotifications key and always check for it,, where users receive direct messages
		// from users abt timer messages and recived messages
		
		try
		{
				String directMsg = KeyValueAPI.get(S.TEAM, S.PASS, username+S.DIRECT_MSGS);
				
				if(!directMsg.contains("ERROR"))
				{
					Scanner scan = new Scanner(directMsg);
					
					while(scan.hasNext())
					{
						String msg = scan.next().replace(" ", "");
						
						String completeMsg = msg+"|"+"text|"+currTime.hour+":"+currTime.minute+"|"+currTime.month+"/"+currTime.monthDay+"/"+currTime.year+"|"+"no|no";
						
						KeyValueAPI.put(S.TEAM, S.PASS, "checkqueues", " "+msg+ completeMsg);
						editFile(completeMsg, username+S.INBOX);
						String m = msg.replace("|", " ");
						Scanner s = new Scanner(m);
						makeNotification("You Have Timer Message From ",s.next());
						deleteFromQueue(directMsg, msg, username+S.DIRECT_MSGS);
					}
				}
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	public void editFile(String completeMessage, String key)
	{
		try{
			//download and edit the file and upload
			
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
		    writer.newLine();
		    writer.write(completeMessage.replace("|", " "));
		    writer.close();
		    UploadAPI.uploadFile(S.TEAM, S.PASS, username+S.INBOX, sdcard.getAbsolutePath()+"/numad/"+ username+S.INBOX+".txt");
		    
		    file.delete();
		}
		catch(Exception e)
		{
			Toast.makeText(MenuScreen.this, "Error While Upload And Download File", Toast.LENGTH_SHORT).show();
		}
	}
	
   private void makeNotification(String notiMessage, String from) {
		
		
		
		Notification noti = new Notification(R.drawable.ic_notification, "You Have Message!", System.currentTimeMillis());
		Manager.setType("inbox");
		Intent intent = new Intent(MenuScreen.this,MessageMenu.class);
		intent.putExtra("message","You Have Message From "+ from);
		
		PendingIntent contentIntent = PendingIntent.getActivity(MenuScreen.this, 0, intent, 0);
		
		noti.setLatestEventInfo(getApplicationContext(), notiMessage + from , "", contentIntent);
		
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		
		noti.defaults = Notification.DEFAULT_ALL;
	    
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, noti);
	}

    public double radius(double lat1, double lon1, double lat2, double lon2)
    {
    	double R = 6371; 
    	double dLat = Math.toRadians(lat2-lat1);
    	double dLon = Math.toRadians(lon2-lon1);
    	lat1 = Math.toRadians(lat1);
    	lat2 = Math.toRadians(lat2);

    	double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
    	        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
    	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    	
    	return  R * c; 
    }
    
    public void manageTeleport(String message)
    {
    	
    	 String from = "";
		 String location = "";
		 String mes = "";
		 String type = "";
		 String date = "";
		 String time = "";
		
    	
    	try
    	{
    		message = message.replace("~", " ");
    		Scanner scan = new Scanner(message);
    		

    		from = scan.next();
    		location = scan.next();
    		
    		
    		message = message.replace(" ", "|");
    		
    		//KeyValueAPI.put(S.TEAM, S.PASS, "checker", location+" "+message+" "+from+" "+mes);
    		String locationQueue = KeyValueAPI.get(S.TEAM, S.PASS, username+S.LATLONG_QUEUE);
        	KeyValueAPI.put(S.TEAM, S.PASS, username+S.LATLONG_QUEUE , locationQueue+" "+location);
        	
        	String messageQueue = KeyValueAPI.get(S.TEAM, S.PASS, username+S.MSG_QUEUE);
        	KeyValueAPI.put(S.TEAM, S.PASS, username+S.MSG_QUEUE , messageQueue +" " +message);	
        	
        	teleport = false;
    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    
    public void makeFriendRequestNotification(String from)
    {
    	Notification noti = new Notification(R.drawable.ic_notification, "You Have Friend Request!", System.currentTimeMillis());
		Manager.setType("inbox");
		Intent intent = new Intent(MenuScreen.this,FriendMenu.class);
		intent.putExtra("message","You have a friend request from "+ from);
		
		PendingIntent contentIntent = PendingIntent.getActivity(MenuScreen.this, 0, intent, 0);
		
		noti.setLatestEventInfo(getApplicationContext(),"You Have Friend Request From "+ from , "", contentIntent);
		
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		
		noti.defaults = Notification.DEFAULT_ALL;
	    
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, noti);
    }
    
}