package edu.neu.madcourse.sudhamayinanduri.finalproject;

import edu.neu.madcourse.sudhamayinanduri.R;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewMessage extends Activity {

	private TextView msg;
	private TextView from;
	private TextView date;
	private TextView loc;

	
	private Button mainMenu;
	private Button reply;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_view_message);
        
        mainMenu = (Button)findViewById(R.id.button_main_menu);
         
        reply = (Button)findViewById(R.id.button_reply);
        
        msg = (TextView) findViewById(R.id.tv_msg);
        from = (TextView)findViewById(R.id.tv_from);
        date = (TextView)findViewById(R.id.tv_date);
        loc = (TextView)findViewById(R.id.tv_loc);
        
        
        from.setText("From: "+ Manager.getMessages().getFrom());
        msg.setText("Message Content: " + Manager.getMessages().getMessage().replace("_", " ").replace("+", " "));
        date.setText("On: "+ Manager.getMessages().getTime() + ", " + Manager.getMessages().getDate());
        loc.setText("At: " + Manager.getMessages().getLocation().replace("_", " "));
        Manager.getMessages().setUnRead("no");
        
        
      
        reply.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent intent = new Intent(ViewMessage.this, ComposeSelect.class);
				Manager.setFriend(Manager.getMessages().getFrom());
				startActivity(intent);
				
			}
		});
        
        
        mainMenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Intent i = new Intent(ViewMessage.this, MenuScreen.class);
				startActivity(i);
				
			}
		});
        
    }
}
