package edu.neu.madcourse.sudhamayinanduri;


import edu.neu.madcourse.sudhamayinanduri.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Bundle;
import android.view.View;

public class Profile extends Activity implements android.view.View.OnClickListener{
	
	Button back;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        
        // button for exiting the screen
        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(this);
        
        // textview to display the device ID
        TextView textDeviceID = (TextView)findViewById(R.id.deviceid);
        
        //retrieve a reference to an instance of TelephonyManager
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
     
        textDeviceID.setText(getDeviceID(telephonyManager));
	}
	
	// method to get the device ID
	String getDeviceID(TelephonyManager phonyManager){
		 
		 String id = phonyManager.getDeviceId();
		 if (id == null){
		  id = "not available";
		 }
		 
		 int phoneType = phonyManager.getPhoneType();
		 switch(phoneType){
		 case TelephonyManager.PHONE_TYPE_NONE:
		  return "NONE: " + id;
		 
		 case TelephonyManager.PHONE_TYPE_GSM:
		  return "GSM: IMEI=" + id;
		 
		 case TelephonyManager.PHONE_TYPE_CDMA:
		  return "CDMA: MEID/ESN=" + id;
		 
		 /*
		  *  for API Level 11 or above
		  *  case TelephonyManager.PHONE_TYPE_SIP:
		  *   return "SIP";
		  */
		 
		 default:
		  return "UNKNOWN: ID=" + id;
		 }
		 
		}
	
	// method to handle on-click of exit button
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == back){ // exits from the activity
			finish();
			return;
			 //Intent intent=new Intent(this,MainActivity.class);
			 //startActivity(intent);
			              	}
	}	
}