package edu.neu.madcourse.sudhamayinanduri.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;

public class ComposeText extends Activity {

	private String username;
	private String friend;
	private TextView to;
	private EditText messageText;
	private String message;
	private Button selectLocation;
	private String latlong;
	private RadioGroup radio;
	private RadioButton rbutton;
	private String type;
	private EditText timerHrs;
	private TextView timer;
	
	private Button mainMenu;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_compose_text);
        
        username = Manager.getUsername();
        friend = Manager.getFriend();
        //latlong = getIntent().getExtras().getString("latlong");
        to = (TextView) findViewById(R.id.to);
        
        to.setText("To: "+ friend);
        messageText = ((EditText) findViewById(R.id.message));
        selectLocation = (Button) findViewById(R.id.button_sel_loc);
        timerHrs = (EditText)findViewById(R.id.enter_timer_hrs);
        timer = (TextView)findViewById(R.id.timer_hrs);
        type = Manager.getType();
        
        mainMenu = (Button)findViewById(R.id.button_main_menu);
        
        
        if(!type.equals("timer"))
        {
        	timerHrs.setEnabled(false);
        	timerHrs.setVisibility(1);
        	//timer.setText(" ");
        }
        
        mainMenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent i = new Intent(ComposeText.this, MenuScreen.class);
				startActivity(i);
				
			}
		});
        
        selectLocation.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
					message = messageText.getText().toString();
					if(message.equals(""))
					{
						alertBuilder("Message is empty");
						return;
					}
					Manager.setMessage(message.replace("|", "").replace("_", ""));
					
					
					
					if(type.equals("timer"))
					{
						if(timerHrs.getText().toString().equals("") || timerHrs.getText().toString().contains(" "))
						{
							alertBuilder("Invalid timer");
						}
						else
						{
							try
							{
								if(KeyValueAPI.isServerAvailable())
								{
									Double.parseDouble(timerHrs.getText().toString());
									Manager.setTimer(timerHrs.getText().toString());
									Intent intent = new Intent(ComposeText.this, SelectLocation.class);
									startActivity(intent);
								}
								else
									alertBuilder("No internet.. Check your wifi, Data connection or try again later");
							}
							catch(NumberFormatException nfe)
							{
								alertBuilder("Invalid time");
							}
						}
						
					}
					else
					{
						if(KeyValueAPI.isServerAvailable())
						{
							Intent intent = new Intent(ComposeText.this, SelectLocation.class);
							startActivity(intent);
						}
						else
							alertBuilder("No internet.. Check your wifi, Data connection or try again later");
					}
					
					
				}
			});
    }
    
    public void alertBuilder(String message)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ComposeText.this);

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
}
