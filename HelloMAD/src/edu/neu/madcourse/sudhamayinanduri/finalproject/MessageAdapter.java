package edu.neu.madcourse.sudhamayinanduri.finalproject;


import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.sudhamayinanduri.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Messages>
{

	 private ArrayList<Messages> items;
	 Activity activity;
	 private static boolean check = false;
	public MessageAdapter(Activity activity, int textViewResourceId, ArrayList<Messages> items) 
	{
		
		super(activity, textViewResourceId);
		this.activity = activity;
		this.items = items;
		check = false;
		
	}
	

	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		 View vi = convertView;
		 
		 Typeface tf = Typeface.createFromAsset(activity.getAssets(), "agency.ttf");
	        
		   //if(!check)
			 //  reverse();
	        
	        if(convertView == null)
	        {
	            LayoutInflater inflater = activity.getLayoutInflater();
	            vi = inflater.inflate(R.layout.u_simple_row, null);
	        }
	        
	        Messages msg = items.get(position);
	        
	        TextView tt = (TextView) vi.findViewById(R.id.toptext);
	        TextView mt = (TextView) vi.findViewById(R.id.middletext);
	        TextView bt = (TextView) vi.findViewById(R.id.bottomtext);     
	        
	        tt.setTypeface(Typeface.DEFAULT_BOLD);
	        
	        //tt.setTypeface(tf);
	        mt.setTypeface(tf);
	        //bt.setTypeface(tf);
	       
	        
	        
	 	   
	        tt.setText(msg.getFrom());
	        mt.setText(msg.getLocation().replace("_", " "));
	        bt.setText(msg.getDate()+" on "+msg.getTime());
	 
	        return vi;
    }
 
}
