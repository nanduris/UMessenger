package edu.neu.madcourse.sudhamayinanduri;

import edu.neu.madcourse.sudhamayinanduri.finalproject.LoginScreen;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ProjectMain extends Activity {

	private Button start;
	private Button sample;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_project_main);
        
        start = (Button)findViewById(R.id.button_start);
        sample = (Button)findViewById(R.id.button_sample);
        
        sample.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(ProjectMain.this,Sample.class);
				startActivity(i);
				
			}
		});
        
        start.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(ProjectMain.this, LoginScreen.class);
				startActivity(i);
				
			}
		});
    }
}
