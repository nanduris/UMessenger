package edu.neu.madcourse.sudhamayinanduri;

import edu.neu.madcourse.sudhamayinanduri.boggle.BoggleLayout;
import edu.neu.madcourse.sudhamayinanduri.vikranthreddyetikyala.persistentboggle.PersistentMainActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PreviousAssignments extends Activity implements OnClickListener{
	
	private ImageButton imgSudoku;
	private ImageButton imgBoggle;
	private ImageButton imgPBoggle;
	
	private TextView tvSudoku;
	private TextView tvBoggle;
	private TextView tvPBoggle;
	
	private Button mainMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_assignments);
        
        
        imgBoggle = (ImageButton)findViewById(R.id.imageButton_boggle);
        imgPBoggle = (ImageButton)findViewById(R.id.imageButton_pboggle);
        
        
        tvBoggle = (TextView)findViewById(R.id.textView_boggle);
        tvPBoggle = (TextView)findViewById(R.id.textView_pboggle);
        
        mainMenu = (Button)findViewById(R.id.button_main);
        
        
        imgBoggle.setOnClickListener(this);
        imgPBoggle.setOnClickListener(this);
        
        
        tvBoggle.setOnClickListener(this);
        tvPBoggle.setOnClickListener(this);
        
        mainMenu.setOnClickListener(this);
        
    }

	public void onClick(View v) {
		
		if (v == imgBoggle)
		{
			Intent intent=new Intent(PreviousAssignments.this, BoggleLayout.class);
			 startActivity(intent);
		}
		else if (v == tvBoggle)
		{
			Intent intent=new Intent(PreviousAssignments.this, BoggleLayout.class);
			 startActivity(intent);
		}
		else if (v == imgPBoggle)
		{
			Intent intent=new Intent(PreviousAssignments.this, PersistentMainActivity.class);
			 startActivity(intent);
		}
		else if (v == tvPBoggle)
		{
			Intent intent=new Intent(PreviousAssignments.this, PersistentMainActivity.class);
			 startActivity(intent);
		}
		else if (v == mainMenu)
		{
			Intent intent=new Intent(PreviousAssignments.this, MainActivity.class);
			 startActivity(intent);
		}
	}
	
}
