package edu.neu.madcourse.sudhamayinanduri.finalproject;

import edu.neu.madcourse.sudhamayinanduri.R;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ComposeFeature extends Activity implements OnItemSelectedListener{
	
	private String username;
	
	private Button mainMenu;
	private Button next;
	
	private Spinner msgType;
	String msgFeature;
	String type;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_compose_feature);
        
        msgFeature = Manager.getFeature();
        
        msgType = (Spinner) findViewById(R.id.spinner_feature);
        
        next = (Button) findViewById(R.id.button_next);
        mainMenu = (Button)findViewById(R.id.button_main_menu);
        
        msgType.setOnItemSelectedListener(ComposeFeature.this);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.feature_type,
				R.layout.u_spinner_feature);
		
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		msgType.setAdapter(adapter);
		
		mainMenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent i = new Intent(ComposeFeature.this, MenuScreen.class);
				startActivity(i);
				
			}
		});
		
		next.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				
				if (msgFeature.equals("msgText"))
				{
					
					Intent intent = new Intent(ComposeFeature.this, ComposeText.class);
					startActivity(intent);
				}
				
				
				else if(msgFeature.equals("qa"))
				{
					
					Intent intent = new Intent(ComposeFeature.this, ComposeQa.class);
					startActivity(intent);
				}

			}
		});


    }
			
			public void onItemSelected(AdapterView<?> parent, View v, int pos,
					long id) {
				type = parent.getItemAtPosition(pos).toString();
				
				Log.e("onitemselcted", type);
				
				if (type.equals("Surprise Your Friend!"))
					Manager.setType("text");
				
				else if(type.equals("Time Your Message!"))
					Manager.setType("timer");
				
				else if(type.equals("Message Hunt with Teleport!"))
					Manager.setType("teleport");
				
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

    
}
