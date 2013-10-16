/* Main Login Screen which starts the game*/
package edu.neu.madcourse.sudhamayinanduri.vikranthreddyetikyala.persistentboggle;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PersistentMainActivity extends Activity {

	private Button submitButton;
	private Button about;
	private Button ack;
	private Button rules;
	private EditText username;
	private EditText password;
	private final String TEAM_NAME = "wizards";
	private final String TEAM_PASS = "123456";
	public static final String PREFS_NAME = "persistentBoggle";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pb);
        
        submitButton = (Button) findViewById(R.id.submit);
        about = (Button)findViewById(R.id.about);
        ack = (Button)findViewById(R.id.ack);
        rules = (Button)findViewById(R.id.rules);
        username = (EditText) findViewById(R.id.userfield);
        password = (EditText) findViewById(R.id.passfield);
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.putString("usernames","bob+adgj alex+adgj mary+adgj john+adgj");

        // Commit the edits!
        editor.commit();
        
        
        
        submitButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Boolean flag = true;
				
				if(sharedPrefcontainsId())
				{
					System.out.println("Entered Here");
					Intent intent = new Intent(PersistentMainActivity.this,MenuScreen.class);
					intent.putExtra("username", username.getText().toString());
					startActivity(intent);
				}
				else
				{
					callToast("Invalid Username/Password");
				}
				
				
			}

			private boolean sharedPrefcontainsId() {
				System.out.println("entered 2");
				SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
				String usernames = settings.getString("usernames","");
				String u = username.getText().toString();
		        String p = password.getText().toString();
				System.out.println(u+"+"+p);
				System.out.println(usernames.contains(u+"+"+p));
				if((usernames.contains(u+"+"+p)) & (u != "") & (p != ""))
				{
					System.out.println("Entered 3");
					return true;
				}
				else
					return false;
			}
        });
        
        about.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(PersistentMainActivity.this, About.class);
				startActivity(i);
			}
		});

		ack.setOnClickListener(new OnClickListener() {
	
			public void onClick(View v) {
		
				Intent i = new Intent(PersistentMainActivity.this, Ack.class);
				startActivity(i);
			}
		});

		rules.setOnClickListener(new OnClickListener() {
	
			public void onClick(View v) {
				Intent i = new Intent(PersistentMainActivity.this, Rules.class);
				startActivity(i);
			}
		});

    }
			

    public void callToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
    
}
