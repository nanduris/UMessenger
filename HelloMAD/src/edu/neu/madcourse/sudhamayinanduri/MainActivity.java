/* The main activity screen where the application is started. It has 4 image buttons to access
 * team members, sudoku and create error and boggle. It also has a button to quit the application.
*/
package edu.neu.madcourse.sudhamayinanduri;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.madcourse.sudhamayinanduri.boggle.BoggleLayout;
import edu.neu.madcourse.sudhamayinanduri.vikranthreddyetikyala.persistentboggle.PersistentMainActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import edu.neu.mobileClass.*;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

	ImageButton profile;
	ImageButton ack;
    ImageButton tricky;
    ImageButton prevAssign;
    
    TextView profiletv;
    TextView acktv;
    TextView prevtv;
    TextView project;
    
    Button exit;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sudhamayi"); // sets the title bar of the screen
        
        // buttons for team members, sudoku, create error and exit
        profile=(ImageButton)findViewById(R.id.profile);
        ack = (ImageButton)findViewById(R.id.imageButton_ack);
        prevAssign = (ImageButton)findViewById(R.id.imageButton_prev);
        tricky = (ImageButton)findViewById(R.id.trickyPart);
        
        exit=(Button)findViewById(R.id.exit);
        
        profiletv = (TextView)findViewById(R.id.textView_team);
        acktv = (TextView)findViewById(R.id.textView_ack);
        prevtv = (TextView)findViewById(R.id.textView_prev);
        project = (TextView)findViewById(R.id.textView_project);
        
        profiletv.setOnClickListener(this);
        acktv.setOnClickListener(this);
        project.setOnClickListener(this);
        prevtv.setOnClickListener(this);
        
        profile.setOnClickListener(this);
        ack.setOnClickListener(this);
        prevAssign.setOnClickListener(this);
        tricky.setOnClickListener(this);
       
        exit.setOnClickListener(this);
      
    }

    // method to handle on-click of buttons
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == exit){ //exits from the application
			 	finish();
				return;
			            }
			
			
			else if(v == profile) {
				Intent intent=new Intent(this,Profile.class);
				 startActivity(intent);
							  }	
				else if(v == project) {
					Intent intent=new Intent(this,ProjectMain.class);
					 startActivity(intent);
								  }
				else if(v == tricky) {
					Intent intent=new Intent(this,ProjectMain.class);
					 startActivity(intent);
								  }
				else if(v == ack) {
					Intent intent=new Intent(this,Acknowledgement.class);
					 startActivity(intent);
								  }
				else if(v == acktv) {
					Intent intent=new Intent(this,Acknowledgement.class);
					 startActivity(intent);
								  }
				else if(v == prevAssign) {
					Intent intent=new Intent(this,PreviousAssignments.class);
					 startActivity(intent);
								  }
				else if(v == prevtv) {
					Intent intent=new Intent(this,PreviousAssignments.class);
					 startActivity(intent);
								  }
			
	}
}
