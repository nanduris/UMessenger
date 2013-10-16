package edu.neu.madcourse.sudhamayinanduri.finalproject;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class ComposeSelect extends Activity implements OnItemSelectedListener{

	private String username;
	private TextView userid;
	private Button next;
	private Button delete;
	private Button mainMenu;
	
	String friend;
	private RadioGroup msgFeatureType;
	private RadioButton msgf;

	
	String msgFeature;
	String type;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.u_compose_select);

		username = Manager.getUsername();
		friend = Manager.getFriend();
		// userid = (TextView) findViewById(R.id.userid);
		next = (Button) findViewById(R.id.button_next);
		delete = (Button) findViewById(R.id.button_delete);
		mainMenu = (Button)findViewById(R.id.button_main_menu);
		
		
		
		msgFeatureType = (RadioGroup)findViewById(R.id.radio_msg_type);
		
		//msgType.setBackgroundColor(this.getResources().getColor(R.color.white));
		
		
		// userid.setText(friend);
		
		mainMenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent i = new Intent(ComposeSelect.this, MenuScreen.class);
				startActivity(i);
				
			}
		});
		
		next.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				int selectedId = msgFeatureType.getCheckedRadioButtonId();
				msgf = (RadioButton) findViewById(selectedId);
				msgFeature = msgf.getText().toString();
				
				if (msgFeature.equals("Type your Text"))
				{
					Manager.setFeature("msgText");
					Intent intent = new Intent(ComposeSelect.this, ComposeFeature.class);
					startActivity(intent);
				}
				
				
				else if(msgFeature.equals("Answer a Question"))
				{
					Manager.setFeature("qa");
					Intent intent = new Intent(ComposeSelect.this, ComposeFeature.class);
					startActivity(intent);
				}

				
			}
		});

		delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vib.vibrate(300);
					String friends = KeyValueAPI.get(S.TEAM, S.PASS, username+ S.FRIENDS);
					friends = friends.replace(friend, " ");
					KeyValueAPI.put(S.TEAM, S.PASS, username + S.FRIENDS, friends);

					friends = KeyValueAPI.get(S.TEAM, S.PASS, friend+ S.FRIENDS);
					
					friends = friends.replace(username, " ");
					KeyValueAPI.put(S.TEAM, S.PASS, friend + S.FRIENDS, friends);

					alertBuilder("User deleted successfully");
				} 
				catch (Exception e)
				{
					alertBuilder("Sorry couldn't delete user");
				}
			}
		});
	}

	public void alertBuilder(String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ComposeSelect.this);

		alertDialogBuilder.setMessage(message).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						vib.vibrate(300);
						Intent intent = new Intent(ComposeSelect.this, Friends.class);
						
						startActivity(intent);

					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	public void onItemSelected(AdapterView<?> parent, View v, int pos,
			long id) {
		type = parent.getItemAtPosition(pos).toString();
		
		Log.e("onitemselcted", type);
		
		if (type.equals("Surprise"))
			Manager.setType("text");
		
		else if(type.equals("Timer"))
			Manager.setType("timer");
		
		else if(type.equals("Teleport"))
			Manager.setType("teleport");
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
