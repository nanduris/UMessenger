package edu.neu.madcourse.sudhamayinanduri.vikranthreddyetikyala.persistentboggle;



import java.util.ArrayList;

import java.util.List;

import edu.neu.mobileclass.apis.KeyValueAPI;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ButtonAdapater extends BaseAdapter {

	private char[] boggleAlphabets;
	private Context context;
	private int id = 0;
	private final int TILES_NUM = 25;
	
	public List<Button> getList() {
		return list;
	}

	public void setList(List<Button> list) {
		this.list = list;
	}

	private List<Button> list =  new ArrayList<Button>();
	public ButtonAdapater(Context context)
	{
		this.context = context;
		this.boggleAlphabets = generateChars();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 25;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(list == null)
		  return null;
		else
		{
			return list.get(arg0);
		}
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		Button button;
		if (convertView == null) {  
            button = new Button(context);
            button.setFocusable(false);
    		button.setClickable(false);
    		button.setText((boggleAlphabets[position]=='Q')?(boggleAlphabets[position]+"u"):(boggleAlphabets[position]+""));      
    		button.setId(id++);
    		list.add(button);
    		
        } else {
            return convertView;
        }
		
        return button;
	}
	
	public char[] generateChars()
    {
	
    	char[] boggleAlphabets = new char[TILES_NUM];
		
    	boggleAlphabets = BoggleGame.board.toCharArray();
		
		return boggleAlphabets;
    }
	
}
