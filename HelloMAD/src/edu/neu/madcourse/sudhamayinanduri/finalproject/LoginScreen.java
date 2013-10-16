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

public class LoginScreen extends Activity {

	
	private EditText username;
	private EditText password;
	private Button signin;
	private Button newuser;
	
	private TextView untv;
	private TextView pwtv;
	private TextView cpright;
	private ProgressDialog progressDialog;
	private int res;
	public static boolean isStarted = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_login_screen);
        
        username = (EditText) findViewById(R.id.username_edit);
        password = (EditText) findViewById(R.id.password_edit);
        signin = (Button) findViewById(R.id.button_submit);
        newuser = (Button) findViewById(R.id.button_newuser);
        
        untv = (TextView)findViewById(R.id.username_title);
        pwtv = (TextView)findViewById(R.id.password_title);
        cpright = (TextView)findViewById(R.id.textView2);
        
        progressDialog = new ProgressDialog(LoginScreen.this);
		progressDialog.setMessage("Signing In...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
        
        
        signin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) 
			{
				//signin.setBackgroundColor(getResources().getColor(R.color.yellow));
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				
				CheckUser check = new CheckUser();
				check.execute("");

			}
		});
        
        newuser.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) 
			{
				//newuser.setBackgroundColor(getResources().getColor(R.color.yellow));
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
			    Intent intent = new Intent(LoginScreen.this, NewUser.class);
			    startActivity(intent);
			}
		});
                
    }

    
    public void alertelse()
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginScreen.this);

		alertDialogBuilder
				.setMessage("Service Unavailable, Come Back Later"
						).setPositiveButton(
						"Yes",
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
    
    public boolean isServerAvailable()
    {
       try
       {
    	   if(KeyValueAPI.isServerAvailable())
    		   return true;
           
       }
       catch(Exception e)
       {
    	   Toast.makeText(LoginScreen.this, "No Internet! Check Your Wifi Settings!", Toast.LENGTH_SHORT).show();
       }
       
       return false;
          
    }
    
    
    public void alertBuilder(String message)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginScreen.this);

		alertDialogBuilder
				.setMessage(message
						).setPositiveButton(
						"Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								vib.vibrate(300);
								Intent intent = new Intent(LoginScreen.this, NewUser.class);
								startActivity(intent);
							}
						}).setNegativeButton("No", 
								new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface dialog, int which) {
										Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
										vib.vibrate(300);
										
									}
								});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();	
	
    }
    
    public boolean authenticate()
    {
    	
    	 String user = username.getText().toString();
    	 String pass = password.getText().toString();
       
    	 if(user.equals("") || pass.equals(""))
    	 {
    		 return false;
    	 }
    	 
    	 try
    	 {
    		 String records = KeyValueAPI.get(S.TEAM, S.PASS, S.USER_RECORDS);
    		 
    		 if(records.contains(" "+user+"+"+pass+" "))
    			 return true;
    		 else
    			 return false;
    		  
    	 }
    	 catch(Exception e)
    	 {
    		 return false;
    	 }
    }
    
    
    public class CheckUser extends AsyncTask<String, Void, String>
    {

    	protected void onPreExecute()
    	{
    	   super.onPreExecute();
    	   progressDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			if(isServerAvailable())
	        {
	        	if(authenticate())
	        	{
	        		res = 1;
	        	}
	        	else
	        	{
	        		res = 2;		
	        	}
	        }
	        else
	        {
	        	res = 3;
	        }
			return null;
		}
    	
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			switch (res) {
			case 1:
				Intent intent = new Intent(LoginScreen.this,MenuScreen.class);
        		Manager.setUsername(username.getText().toString());
        		startActivity(intent);
				break;

			case 2:
				alertBuilder("User Not Found, New User?");
				break;
			
			case 3:
				alertelse();
			default:
				break;
			}
			
			
		}
    }
    
  }
