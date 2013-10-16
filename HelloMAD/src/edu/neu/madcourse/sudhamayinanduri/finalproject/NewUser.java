package edu.neu.madcourse.sudhamayinanduri.finalproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;
import edu.neu.mobileclass.apis.UploadAPI;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class NewUser extends Activity {

	private EditText desiredUsernametext;
	private EditText passwordtext;
	private EditText name;
	private Button submit;
	private Button login;
	private String desiredUsername;
	private String password;
	private String desiredName;
	
	private TextView dUser;
	private TextView dPass;
	private TextView dName;
	private TextView dGender;
	private RadioButton radio0;
	private RadioButton radio1;
	private int res;
	
	private Time currTime;
	private ProgressDialog progressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_new_user);
        
        
        currTime = new Time();
        currTime.setToNow();

        desiredUsernametext = ((EditText) findViewById(R.id.d_username_edit));
        passwordtext = ((EditText) findViewById(R.id.d_password_edit));
        name = (EditText) findViewById(R.id.d_enter_name);
        submit = (Button) findViewById(R.id.d_button_submit);
        login = (Button)findViewById(R.id.button_login_screen);
        
        dUser = (TextView)findViewById(R.id.d_username_title);
        dPass = (TextView)findViewById(R.id.d_password);
        dName = (TextView)findViewById(R.id.d_name);
        dGender = (TextView)findViewById(R.id.d_gender);
        radio0 = (RadioButton)findViewById(R.id.radio0);
        radio1 = (RadioButton)findViewById(R.id.radio1);
        
  
        progressDialog = new ProgressDialog(NewUser.this);
		progressDialog.setMessage("Creating Your Account...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
        login.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent i = new Intent(NewUser.this, LoginScreen.class);
				startActivity(i);
				
			}
		});
        
        submit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) 
			{
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				desiredUsername = desiredUsernametext.getText().toString();
				password = passwordtext.getText().toString();
				desiredName = name.getText().toString();
				
			   	try
			   	{
			   		String records = KeyValueAPI.get(S.TEAM, S.PASS, S.USER_RECORDS);
			   		
			   		if(desiredUsername.length() < 3 || password.length() < 3)
			   		{
			   			alertBuilder("Username And Password Must Be More Than 3 Characters");
			   	    }
			   		else if(desiredUsername.length() > 8 || password.length() > 8)
			   		{
			   			alertBuilder("Username Must Be Less Than 9 Characters. Try Another?");
			   		}
			   		else if(name.equals(""))
			   		{
			   			alertBuilder("Enter Your Name. Try Another?");
			   		}
			   		else if(desiredUsername.contains(" ") || password.contains(" "))
			   		{
			   			alertBuilder("No Spaces Allowed. Try Another?");
			   		}	
			   		else if(records.contains(" "+desiredUsername+"+"))
			   		{
			   		     alertBuilder("Username Is Already In Use. Try Another?");				   		
			   		}
			   		else 
			   		{
			   		    checkNewUser check = new checkNewUser();
			   		    check.execute("");
			   		}
			   		
			   	}
			   	catch(Exception e)
			   	{
			   		Toast.makeText(NewUser.this, "Service Unavailable!", Toast.LENGTH_SHORT).show();
			   	}
			}
		});
    }

        
    public void alertBuilder(String message)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewUser.this);

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
    
    public void alertBuilder1(String message)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewUser.this);

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
								
								Intent intent = new Intent(NewUser.this, MenuScreen.class);
					   		      Manager.setUsername(desiredUsername);
					   		      startActivity(intent);
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();	
	
    }
    
    public class checkNewUser extends AsyncTask<String, Void, String>
    {

    	protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog.show();		
		}
    	
		//@Override
		protected String doInBackground(String... params) 
		{
			
			 if(createFile(desiredUsername))
	   		 {
				  String records = KeyValueAPI.get(S.TEAM, S.PASS, S.USER_RECORDS);
	   		      KeyValueAPI.put(S.TEAM, S.PASS, S.USER_RECORDS, records + desiredUsername+"+"+password+" ");		
	   		      
	   	          res = 2;	      
	   		 }
	   		 else
	   		 {
	   		     res = 1;
	   		 }   
			return null;
		}
		
		
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			switch (res) {
			case 1:
				Toast.makeText(NewUser.this, "Service Unavailable!", Toast.LENGTH_SHORT).show();
				break;

			case 2:
				alertBuilder1("Succesfully Registered, Welcome to uMessenger");
				break;
				
			default:
				break;
			}
			
		}	
			
		

	    public boolean createFile(String username)
	    {
	    	
	    	try
	    	{

		        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		        {
		           // Toast.makeText(this, "External SD card not mounted", Toast.LENGTH_LONG).show();
		            return false;
		        }
		        
		        
	    	     KeyValueAPI.put(S.TEAM, S.PASS, username+S.FRIENDS, "");
	    	     KeyValueAPI.put(S.TEAM, S.PASS, username+S.IN_REQUESTS, "");
	    	     KeyValueAPI.put(S.TEAM, S.PASS, username+S.OUT_REQUESTS, "");
	    	     KeyValueAPI.put(S.TEAM, S.PASS, username+S.INBOX, "");
	    	     KeyValueAPI.put(S.TEAM, S.PASS, username+S.LATLONG_QUEUE, "");
	    	     KeyValueAPI.put(S.TEAM, S.PASS, username+S.MSG, "");
	    	     KeyValueAPI.put(S.TEAM, S.PASS, username+S.MSG_QUEUE, "");
	    	     KeyValueAPI.put(S.TEAM, S.PASS, username+S.RECENT, "42.338975+-71.091932");
	    	     KeyValueAPI.put(S.TEAM, S.PASS, username+S.REQUESTS, "");
	    	     
	    	     
	    	   //create file usernameinbox with welcome message
	    	     File sdcard = Environment.getExternalStorageDirectory();
	    	        
	    	     File file = new File(sdcard.getAbsolutePath()+"/"+ username+S.INBOX+".txt");
	    	     
	    	     file.createNewFile();
			
	        	BufferedWriter buf = new BufferedWriter(new FileWriter(file, true)); 
	            buf.write("uMessenger United+States Welcome_to_uMessenger text "+ currTime.hour+":"+currTime.minute+" "+currTime.month+"/"+currTime.monthDay+"/"+currTime.year +" no no");
	            buf.close();
	                
	             String result = UploadAPI.uploadFile(S.TEAM, S.PASS,username+S.INBOX, sdcard.getAbsolutePath()+"/"+ username+S.INBOX+".txt");
	             if(!result.contains("uploaded"))
	             {
	            	 return false;
	             }
	             
	             
	             File sdcard1 = Environment.getExternalStorageDirectory();
	    	     File newfile = new File(sdcard1.getAbsolutePath()+"/"+ username+S.SENT+".txt");
	    	     newfile.createNewFile();
	    	     result = UploadAPI.uploadFile(S.TEAM, S.PASS, username+S.SENT, sdcard.getAbsolutePath()+"/"+ username+S.SENT+".txt");
	             
	            
	             if(result.contains("uploaded"))
	             {
	            	 return true;
	             }
	             else
	             {
	            	 return false;
	             }
	    	}
	    	catch(Exception e)
	    	{
	    		//Toast.makeText(NewUser.this, "External Storage Unavailable", Toast.LENGTH_SHORT).show();
	    	}
	    	
	    	return false;
	    	
	    }
	    
	    public void createFiles(String key)
	    {
	    	
	    }

			
    }
}
