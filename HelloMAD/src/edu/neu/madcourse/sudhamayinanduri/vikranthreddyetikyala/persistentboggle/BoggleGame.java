package edu.neu.madcourse.sudhamayinanduri.vikranthreddyetikyala.persistentboggle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BoggleGame extends Activity {

	private TextView timer;
	private TextView words;
	private TextView playerOneScore;
	private TextView playerTwoScore;
	private TextView showWord;
	private List<String> allWords = new ArrayList<String>();
	private List<Integer> ids = new ArrayList<Integer>();
	private List<Integer> boggleManager = new ArrayList<Integer>();
	private List<Button> buttonsList = new ArrayList<Button>();
	private String currentWord = "";
	private Button addWord;
	//private ImageView pause;
	private CountDownTimer cdTimer;
	private int score;
	private Context context = this;
	private long timeCounter = 60000;
	private String otherPlayer;
	private String player;
	private String random;

	private final int TILES_NUM = 25;
	private final int MIN_LETTERS = 3;
	private final String RESUME = "Resume";
	private final String TXT = ".txt";
	private final String SCORE = "Score: ";
	private final String NO_SUCH_WORD = "No such Word";
	private final String INVALID_WORD = "Invalid Word";
	private final String WORD_ALREADY_IN_USE = "Word Already in Use";
	private final String MAIN_MENU = "Menu";
	public static String board;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pboggle_game);

		otherPlayer = getIntent().getExtras().getString("otherPlayer");
		player = getIntent().getExtras().getString("username");
		random = getIntent().getExtras().getString("rand");
		
		timer = (TextView) findViewById(R.id.timer);
		words = (TextView) findViewById(R.id.allWords);
		
		showWord = (TextView) findViewById(R.id.word);
		addWord = (Button) findViewById(R.id.addword);
		//pause = (ImageView) findViewById(R.id.pause);

		//onPause();

		addNewWord();

		timer();

		board = getIntent().getExtras().getString("board");
		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new ButtonAdapater(this));

		gridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(300);
				Button button = (Button) parent.getItemAtPosition(position);
				if ((boggleManager.contains(position) && !ids.contains(button.getId())) || boggleManager.isEmpty()) {
					
					ids.add(button.getId());
					currentWord += button.getText();
					showWord.setText(currentWord);

					for (int i = 0; i < TILES_NUM; i++) {
						Button b = (Button) parent.getItemAtPosition(i);
						b.setEnabled(true);
						boggleManager.clear();
						if (buttonsList.size() <= TILES_NUM)
							buttonsList.add(b);
					}
					boggleManager = getAdjacents(button.getId());
					for (int i = 0; i < TILES_NUM; i++) {
						if (!boggleManager.contains(i) || ids.contains(i)) {
							Button b = (Button) parent.getItemAtPosition(i);
							b.setEnabled(false);
						}

					}
				}
			}
		});
	}

	
	public void onStart()
	{
		super.onStart();
		
		playerOneScore = (TextView) findViewById(R.id.playerOneScore);
		playerTwoScore = (TextView) findViewById(R.id.playertwoScore);
		
		Thread thread = new Thread(new Runnable() {
			
			
			public void run() {
				
					while(timeCounter >= 0)
					{	
						
						
						if(getIntent().getExtras().getString("player").equals("1"))
						{
							playerTwoScore.post(new Runnable() {
								
								public void run() {
									SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME, 0);
									String str  = settings.getString(random, "");
									Scanner scan = new Scanner(str);
									scan.next();scan.next();scan.next();
									playerTwoScore.setText(otherPlayer+": "+scan.next());		
								}
							});
							
						}	
						else
						{
							playerOneScore.post(new Runnable() {
								
								public void run() {
									SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME, 0);
									String str  = settings.getString(random, "");
									Scanner scan = new Scanner(str);
									scan.next();scan.next();
									playerOneScore.setText(otherPlayer+": "+scan.next());		
								}
							});
							
						}
						
						
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			}
			}
		});
		thread.start();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.boggle_game, menu);
		return true;
	}


	public void addNewWord() {
		addWord.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean check;
				try {
					if ((currentWord.length() >= MIN_LETTERS) && (currentWord.length() <= TILES_NUM)) {
						if (allWords.contains(currentWord)) {
							callToast(WORD_ALREADY_IN_USE);
							showWord.setText("");
						} else if (check = checkWord()
								&& !allWords.contains(currentWord)) {
							MediaPlayer mp = MediaPlayer.create(context,
									R.raw.boggle_claps);
							mp.start();
							words.setText(words.getText() + " " + currentWord
									+ "(" + (currentWord.length() - 2) + ")");
							if(getIntent().getExtras().getString("player").equals("1"))
							{
							  score += currentWord.length() - 2;
							  playerOneScore.setText(player+": " + score);
							  SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME, 0);
						      SharedPreferences.Editor editor = settings.edit();
						      String rand = settings.getString(getIntent().getExtras().getString("rand"), "");
						      Scanner scan = new Scanner(rand);
						      String name = scan.next();
						      String b = scan.next();
						      String s = score+"";
						      scan.next();
						      String s2 = scan.next();
						      String c1 = scan.next();
						      String c2 = scan.next();
						      editor.putString(getIntent().getExtras().getString("rand"), name+" "+b+" "+s+" "+s2+" "+c1+" "+c2);

						      // Commit the edits!
						      editor.commit();
							}
							else if(getIntent().getExtras().getString("player").equals("2"))
							{
							  score += currentWord.length() - 2;
							  playerTwoScore.setText(player+": " + score);
							  SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME, 0);
						      SharedPreferences.Editor editor = settings.edit();
						     
						      String rand = settings.getString(getIntent().getExtras().getString("rand"), "");
						      Scanner scan = new Scanner(rand);
						      String name = scan.next();
						      String b = scan.next();
						      String s = scan.next();
						      String s2 = score+"";
						      scan.next();
						      String c1 = scan.next();
						      String c2 = scan.next();
						      editor.putString(getIntent().getExtras().getString("rand"), name+" "+b+" "+s+" "+s2+" "+c1+" "+c2);
						      
						      editor.commit();
							}
							showWord.setText("");
							allWords.add(currentWord);
						}

						else if (!check) {
							callToast(NO_SUCH_WORD);
							showWord.setText("");
						}
						currentWord = "";
					} else {
						currentWord = "";
						showWord.setText("");
						callToast(INVALID_WORD);
					}
					boggleManager.clear();
					ids.clear();
					for (Button button : buttonsList)
						button.setEnabled(true);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
	}

	public void timer() {
		cdTimer = (new CountDownTimer(timeCounter, 1000) {

			public void onTick(long millisUntilFinished) {
				timeCounter = millisUntilFinished;
				timer.setText("Time: " + millisUntilFinished / 1000);
			}

			public void onFinish() {				
				
				if(getIntent().getExtras().getString("player").equals("1"))
				{
					
				try{
					
						String s = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, random);
						Scanner scan = new Scanner(s);
						
						String n = scan.next();
						String b = scan.next();
						
						String sco = score + "";
						scan.next();
						String p2 = scan.next();
						
						KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE, random, n+" "+b+" "+sco+" "+p2+" 1 0");
					
					}
					catch(Exception e)
					{
						Toast.makeText(BoggleGame.this, "No Internet", Toast.LENGTH_SHORT).show();
					}	
					
				}
				
				if(getIntent().getExtras().getString("player").equals("2"))
				{
					
					try{
						
						String s = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, random);
						Scanner scan = new Scanner(s);
						
						String n = scan.next();
						String b = scan.next();
						
						String p1 = scan.next();
						String p2 = score+"";
						
						KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE, random, n+" "+b+" "+p1+" "+p2+" 1 1");
						
						if(Integer.valueOf(p1) > Integer.valueOf(p2))
						{
							System.out.println("p1 is "+p1+" p2 is "+p2);
							
							
							try
							{
								Log.e(player+"notique","");
								KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE,otherPlayer+"notique",player+" score is "+ p2 + ", lost the game against you");
								callToast("Player "+otherPlayer+" has won the game");
						
							}
							catch(Exception e)
							{
								callToast("Internet connection lost! Score not saved");
							}
						}
						else
						{
							System.out.println("p1 is "+p1+" p2 is "+p2);
							
							try
							{
								Log.e(player+"notique","");
								KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE,otherPlayer+"notique",player+" score is "+ p2 +", won the game against you");
								callToast("You won the game");
							}
							catch(Exception e)
							{
								callToast("Internet connection lost!Score not saved");
							}
						
						}
						
						//player 1
						String hs = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, (otherPlayer+"hs").replace(" ", ""));
						Scanner sc = new Scanner(hs);
						String phs = sc.next();
						
						
						if(Integer.valueOf(p1) > Integer.valueOf(phs))
						{
							String d = DateFormat.getDateTimeInstance().format(new Date());
							KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE, (otherPlayer+"hs").replace(" ", ""), p1+" on "+d+" by "+otherPlayer);
						}
							
						//player 2	
						String hs2 = KeyValueAPI.get(Server.TEAM_NAME, Server.TEAM_PASSCODE, (player+"hs").replace(" ", ""));
						Scanner sc2 = new Scanner(hs2);
						String phs2 = sc2.next();
						
						if(score > Integer.valueOf(phs2))
						{
							String d = DateFormat.getDateTimeInstance().format(new Date());
							KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE, (player+"hs").replace(" ", ""), score+" on "+d+" by "+player);
						}
					
					}
					catch(Exception e)
					{
						Toast.makeText(BoggleGame.this, "Internet Connection is lost", Toast.LENGTH_SHORT).show();
					}
					
				}
							
				Intent in = new Intent(BoggleGame.this,Scores.class);
				in.putExtra("score", score+"");
				in.putExtra("username", player);
				in.putExtra("rand", random);
				in.putExtra("player", getIntent().getExtras().getString("player"));
				startActivity(in);

		}

			

			private void pushtoServerAgain() {
				
		    	try
		    	{
		    		SharedPreferences settings = getSharedPreferences(PersistentMainActivity.PREFS_NAME, 0);
		    		
		    		Map<String,?> map = settings.getAll();
		    		
		    			
		    			for(String m : map.keySet())
		    			{
		    				KeyValueAPI.put(Server.TEAM_NAME, Server.TEAM_PASSCODE, m, map.get(m).toString());
		    			}
		    	}
		    		catch(Exception e)
		    		{
		    			System.out.println("NoInternet. Cannot push");
		    		}
				
			}
		}.start());
	}
	
	private void addDefaultNotification() {
		NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  

        Notification note = new Notification(R.drawable.notification, "Opponent Scores", System.currentTimeMillis());  

         PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, Scores.class), 0);
         note.setLatestEventInfo(getApplicationContext(), "Score of", "Message Content 2", intent);
         note.flags = Notification.FLAG_AUTO_CANCEL;
         notifManager.notify(1, note);
         
		
	}

	public boolean checkWord() throws Exception {
		AssetManager assetManager = getAssets();
		InputStream ims = null;

		ims = assetManager.open(currentWord.length() + TXT);

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(ims));
		String strLine;
		while ((strLine = bufferedReader.readLine()) != null) {
			if (strLine.equalsIgnoreCase(currentWord)) {
				bufferedReader.close();
				return true;
			}

		}
		bufferedReader.close();
		return false;
	}

	public void callToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	
		public List<Integer> getAdjacents(int id) {
			switch (id) {
			case 0:
				boggleManager.add(1);
				boggleManager.add(5);
				boggleManager.add(6);
				break;
			case 1:
				boggleManager.add(0);
				boggleManager.add(2);
				boggleManager.add(5);
				boggleManager.add(6);
				boggleManager.add(7);
				break;
			case 2:
				boggleManager.add(1);
				boggleManager.add(3);
				boggleManager.add(6);
				boggleManager.add(7);
				boggleManager.add(8);
				break;
			case 3:
				boggleManager.add(2);
				boggleManager.add(4);
				boggleManager.add(7);
				boggleManager.add(8);
				boggleManager.add(9);
				break;
			case 4:
				boggleManager.add(3);
				boggleManager.add(8);
				boggleManager.add(9);
				break;
			case 5:
				boggleManager.add(0);
				boggleManager.add(1);
				boggleManager.add(6);
				boggleManager.add(10);
				boggleManager.add(11);
				break;
			case 6:
				boggleManager.add(0);
				boggleManager.add(1);
				boggleManager.add(2);
				boggleManager.add(5);
				boggleManager.add(7);
				boggleManager.add(10);
				boggleManager.add(11);
				boggleManager.add(12);
				break;
			case 7:
				boggleManager.add(1);
				boggleManager.add(2);
				boggleManager.add(3);
				boggleManager.add(6);
				boggleManager.add(8);
				boggleManager.add(11);
				boggleManager.add(12);
				boggleManager.add(13);
				break;
			case 8:
				boggleManager.add(2);
				boggleManager.add(3);
				boggleManager.add(4);
				boggleManager.add(7);
				boggleManager.add(9);
				boggleManager.add(12);
				boggleManager.add(13);
				boggleManager.add(14);
				break;
			case 9:
				boggleManager.add(3);
				boggleManager.add(4);
				boggleManager.add(8);
				boggleManager.add(13);
				boggleManager.add(14);
				break;
			case 10:
				boggleManager.add(5);
				boggleManager.add(6);
				boggleManager.add(11);
				boggleManager.add(15);
				boggleManager.add(16);
				break;
			case 11:
				boggleManager.add(5);
				boggleManager.add(6);
				boggleManager.add(7);
				boggleManager.add(10);
				boggleManager.add(12);
				boggleManager.add(15);
				boggleManager.add(16);
				boggleManager.add(17);
				break;
			case 12:
				boggleManager.add(6);
				boggleManager.add(7);
				boggleManager.add(8);
				boggleManager.add(11);
				boggleManager.add(13);
				boggleManager.add(16);
				boggleManager.add(17);
				boggleManager.add(18);
				break;
			case 13:
				boggleManager.add(7);
				boggleManager.add(8);
				boggleManager.add(9);
				boggleManager.add(12);
				boggleManager.add(14);
				boggleManager.add(17);
				boggleManager.add(18);
				boggleManager.add(19);
				break;
			case 14:
				boggleManager.add(8);
				boggleManager.add(9);
				boggleManager.add(13);
				boggleManager.add(18);
				boggleManager.add(19);
				break;
			case 15:
				boggleManager.add(10);
				boggleManager.add(11);
				boggleManager.add(16);
				boggleManager.add(20);
				boggleManager.add(21);
				break;
			case 16:
				boggleManager.add(10);
				boggleManager.add(11);
				boggleManager.add(12);
				boggleManager.add(15);
				boggleManager.add(17);
				boggleManager.add(20);
				boggleManager.add(21);
				boggleManager.add(22);
				break;
			case 17:
				boggleManager.add(11);
				boggleManager.add(12);
				boggleManager.add(13);
				boggleManager.add(16);
				boggleManager.add(18);
				boggleManager.add(21);
				boggleManager.add(22);
				boggleManager.add(23);
				break;
			case 18:
				boggleManager.add(12);
				boggleManager.add(13);
				boggleManager.add(14);
				boggleManager.add(17);
				boggleManager.add(19);
				boggleManager.add(22);
				boggleManager.add(23);
				boggleManager.add(24);
				break;
			case 19:
				boggleManager.add(13);
				boggleManager.add(14);
				boggleManager.add(18);
				boggleManager.add(23);
				boggleManager.add(24);
				break;
			case 20:
				boggleManager.add(15);
				boggleManager.add(16);
				boggleManager.add(21);
				break;
			case 21:
				boggleManager.add(15);
				boggleManager.add(16);
				boggleManager.add(17);
				boggleManager.add(20);
				boggleManager.add(22);
				break;
			case 22:
				boggleManager.add(16);
				boggleManager.add(17);
				boggleManager.add(18);
				boggleManager.add(21);
				boggleManager.add(23);
				break;
			case 23:
				boggleManager.add(17);
				boggleManager.add(18);
				boggleManager.add(19);
				boggleManager.add(22);
				boggleManager.add(24);
				break;
			case 24:
				boggleManager.add(18);
				boggleManager.add(19);
				boggleManager.add(23);
				break;

			default:
				break;
			}
		return boggleManager;
	}
}
