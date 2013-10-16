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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;

public class ComposeQa extends Activity implements OnItemSelectedListener {
	
	private Spinner question;
	
	private RadioGroup answer;
	private RadioButton ansButton;
	String ans;
	String ques;
	
	private Button selLoc;
	private Button mainMenu;
	
	
	String type;
	
	private EditText timerHrs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_compose_qa);
        
        question = (Spinner)findViewById(R.id.spinner_ques);
        answer = (RadioGroup)findViewById(R.id.radioGroup_ans);
        selLoc = (Button)findViewById(R.id.button_sel_loc);
        mainMenu = (Button)findViewById(R.id.button_main_menu);
        timerHrs = (EditText)findViewById(R.id.timer_hrs);
        
        question.setOnItemSelectedListener(ComposeQa.this);
        
        ArrayAdapter<CharSequence> adapterQues = ArrayAdapter.createFromResource(
				this, R.array.question,
				R.layout.u_spinner_feature);
		// Specify the layout to use when the list of choices appears
		adapterQues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		question.setAdapter(adapterQues);
		
		type = Manager.getType();
		
		if (!type.equals("timer"))
		{
			timerHrs.setEnabled(false);
		}
        
		mainMenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent i = new Intent(ComposeQa.this, MenuScreen.class);
				startActivity(i);
				
			}
		});
		
		selLoc.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				
				int selectedId = answer.getCheckedRadioButtonId();
				ansButton = (RadioButton) findViewById(selectedId);
				ans = ansButton.getText().toString();
				
				Manager.setMessage((ques + "+" + ans).replace(" ", "_"));
				
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
								Intent intent = new Intent(ComposeQa.this, SelectLocation.class);
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
						Intent intent = new Intent(ComposeQa.this, SelectLocation.class);
						startActivity(intent);
					}
					else
						alertBuilder("No internet.. Check your wifi, Data connection or try again later");
				}
			}
		});
    }

	public void onItemSelected(AdapterView<?> parent, View v, int pos,
			long id) {
		ques = parent.getItemAtPosition(pos).toString();
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	 public void alertBuilder(String message)
	    {
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ComposeQa.this);

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
