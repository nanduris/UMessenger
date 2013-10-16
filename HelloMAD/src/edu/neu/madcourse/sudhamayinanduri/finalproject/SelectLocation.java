package edu.neu.madcourse.sudhamayinanduri.finalproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import edu.neu.madcourse.sudhamayinanduri.R;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class SelectLocation extends MapActivity {
	
	 MapView mapView;
	 MapController mc;
	 int lat;
	 int longi;
	 GeoPoint p;
	 private String username;
	 private String friend;
	 private String message;
	 private String latitude1;
	 private String longitude1;
	 private String type;
	 private String timer;
	 private GestureDetector gestureDetector;
	 private String latitude;
	 private String longitude;
	 private int count = 0;
     private String lat2;
     private String lon2;
     //private Button clear;
     private String focuslat;
     private String focuslong;
     
     private MapController objMapController;
	 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_select_location);
        
        username = Manager.getUsername();
        friend = Manager.getFriend();
        message = Manager.getMessage();
        type = Manager.getType();
        
        openingAlert();
        
        
        if(type.equals("timer"))
           timer = Manager.getTimer();
         
        
        	
		         mapView = (MapView) findViewById(R.id.mapview);
		         mapView.setBuiltInZoomControls(true);
		         
		         objMapController = mapView.getController();
		         
		         //clear = (Button)findViewById(R.id.button_clear);
		         
		         
		        /*if(!type.equals("teleport"))
		        {
		        	clear.setEnabled(false);
		        	clear.setVisibility(1);
		        }
		        clear.setOnClickListener(new android.view.View.OnClickListener() {
					
					public void onClick(View v) {
						
						Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						vib.vibrate(300);
						
						count = 0;
						Toast.makeText(SelectLocation.this, "Select your first location to teleport with long press", Toast.LENGTH_SHORT).show();
						
					}
				});        */
		        
		        List<Overlay> mapOverlays = mapView.getOverlays();
		        Drawable drawable = this.getResources().getDrawable(R.drawable.map_pointer);
		        Overlays itemizedoverlay = new Overlays(drawable, this);
		        
		        
		        
		        if(type.equals("teleport"))
		        	Toast.makeText(SelectLocation.this, "Select Your First Location To Teleport With Long Press", Toast.LENGTH_SHORT).show();
       
		  try
		  {        
		    
	        	String latandlong = KeyValueAPI.get(S.TEAM, S.PASS, friend+S.RECENT);
	        	
	        	 if(!latandlong.contains("ERROR"))
	        	{
	        	
		        	Scanner scan = new Scanner(latandlong);
		        	
		        	while(scan.hasNext())
		        	{
		        		
		        		String latitude1 = "";
		        		String longitude1 = "";
		        		boolean divide = false;
		        		char[] latlong = (scan.next()).toCharArray();
		        		
		        		for(int i=0; i<latlong.length; i++)
		        		{
		                    if(latlong[i] == '+')
		                    	divide = true;
		                    else if(!divide)
		                    	latitude1 += latlong[i];
		                    else if(divide)
		                    	longitude1 += latlong[i];
		        		}
		        		
		        	   
		        		double l1 = (Double.valueOf(latitude1)) * 1e6;
		        		double l2 = (Double.valueOf(longitude1)) * 1e6;
		        		
		        		int lati = (int) l1;
		        		int longi = (int) l2;
		        		
		        		String locationName = getLocationNames(latitude1, longitude1);
		        		GeoPoint point = new GeoPoint(lati ,longi);
		        		OverlayItem overlayitem = new OverlayItem(point, latitude1, longitude1);
		        		
		        		itemizedoverlay.addOverlay(overlayitem);
		        		mapOverlays.add(itemizedoverlay);
		        		
		        		
		        		
		        		focuslat = latitude1;
		        		focuslong = longitude1;
		        	}
		        	
		        	gestureDetector = new GestureDetector(this, new RetrieveOnGesture(), null, true);
		        	

	        		double l1 = (Double.valueOf(focuslat)) * 1e6;
	        		double l2 = (Double.valueOf(focuslong)) * 1e6;
	        		
	        		int lati = (int) l1;
	        		int longi = (int) l2;
	        		
	        		GeoPoint point = new GeoPoint(lati ,longi);
		        	
		        	int minLatitude = lati - 1;
		        	int maxLatitude = lati + 1;
		        	int minLongitude = longi - 1;
		        	int maxLongitude = longi + 1;

		        	maxLatitude = Math.max(lati, maxLatitude);
		        	minLatitude = Math.min(lati, minLatitude);
		        	maxLongitude = Math.max(longi, maxLongitude);
		        	minLongitude = Math.min(longi, minLongitude);
		        	 
		        	objMapController.animateTo(new GeoPoint( 
		        			 (maxLatitude + minLatitude)/2, 
		        			 (maxLongitude + minLongitude)/2 )); 
		        	objMapController.setZoom(18);
		        	
        	}
	        	
	        	 
        }
        catch(Exception e)
        {
        	Toast.makeText(SelectLocation.this, "Service Unavailable", Toast.LENGTH_SHORT).show();
        }
        
        
    }
        
    
    public String getLocationNames(String latitude, String longitude) 
    {
    	Geocoder gcd = new Geocoder(SelectLocation.this, Locale.getDefault());
    	List<Address> addresses;
    	try 
    	{
    		
	    	addresses = gcd.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1);
	    
	    	if (addresses.size() > 0) 
	    	{
	    		return (addresses.get(0).getAddressLine(0) + " " +addresses.get(0).getLocality()+", "+addresses.get(0).getAdminArea());
	    	}
	    		
	    			
    	} 
    	catch (Exception e) 
    	{
	    	//Toast.makeText(this, "Could not get location name", Toast.LENGTH_SHORT).show();
	    }
    	return null;
		
	}

        	
      @Override
    public boolean dispatchTouchEvent(MotionEvent event) 
      {
    	  
	         gestureDetector.onTouchEvent(event);
	         return super.dispatchTouchEvent(event);
    	  
     }
        	        	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	class RetrieveOnGesture implements GestureDetector.OnGestureListener {

		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		public void onLongPress(MotionEvent e) 
		{
			
			try
			{
				
					GeoPoint p = mapView.getProjection().fromPixels((int)e.getX(), (int)e.getY());
					 
					latitude = ((Double.valueOf(p.getLatitudeE6()))/1000000)+"";
					longitude = ((Double.valueOf(p.getLongitudeE6()))/1000000)+"";
					String locationNames = getLocationNames(((Double.valueOf(p.getLatitudeE6()))/1000000)+"", ((Double.valueOf(p.getLongitudeE6()))/1000000)+"");
					
					if(!locationNames.contains("null"))
					{
						if(!type.equals("teleport"))
						{
						    AlertDialog.Builder dialog = new AlertDialog.Builder(SelectLocation.this);
						    dialog.setTitle("Select Location");
						    dialog.setMessage("Location: " + getLocationNames(((Double.valueOf(p.getLatitudeE6()))/1000000)+"", ((Double.valueOf(p.getLongitudeE6()))/1000000)+""));
						    dialog.setPositiveButton("Yes", new OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									 
									 Intent intent = new Intent(SelectLocation.this,Review.class);
									 Manager.setLatitude(latitude);
									 Manager.setLongitude(longitude);
									 startActivity(intent);
								}
							});
						    
						    dialog.setNegativeButton("No", new OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									
									
								}
							});
						    dialog.show();
						}
						else
						{
							setLatLongTeleport(latitude, longitude);		
						}
	
					}
						
			}
			catch(Exception ex)
			{
	        	Toast.makeText(SelectLocation.this, "Service Unavailable!", Toast.LENGTH_SHORT).show();
			}
			
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

	
	
	class Overlays extends ItemizedOverlay {
		
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		Context mContext;

		public Overlays(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mContext = context;
			
		}
		
		public void addOverlay(OverlayItem overlay) {
			
			
				mOverlays.add(overlay);
				populate();
			
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}
		
		@Override
		protected boolean onTap(int index)
		{
			/*
			try
			{
				
					Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vib.vibrate(300);
					OverlayItem item = mOverlays.get(index);
					
					latitude = item.getTitle();
					longitude = item.getSnippet();
					
					if(!type.equals("teleport"))
					{
						  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
						  dialog.setTitle("Select Location?"); 
						  dialog.setMessage(getLocationNames(latitude, longitude));
						  dialog.setPositiveButton("Yes", new OnClickListener() {
						  
							  
							  
							public void onClick(DialogInterface dialog, int which) {
								  Intent intent = new Intent(SelectLocation.this,Review.class);
								  Manager.setLatitude(latitude);
								  Manager.setLongitude(longitude);
								  startActivity(intent);		
								 
							}
						});
						  dialog.setNegativeButton("No", new OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						});
						  dialog.show();
					}
					else
					{
						setLatLongTeleport(latitude, longitude);		
					}
	
					 return true;
				

			}
			catch(Exception e)
			{
	        	Toast.makeText(SelectLocation.this, "Service unavailable", Toast.LENGTH_SHORT).show();
			}
			
*/
			 return false;
		}
	}

	
	public void setLatLongTeleport(String lati, String longi)
	{
		
		latitude = lati;
		longitude = longi;
	
		if(count == 0)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(SelectLocation.this);
		    dialog.setTitle("Select Location");
		    dialog.setMessage("Location: " + getLocationNames(lati, longi));
		    dialog.setPositiveButton("Yes", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					 
					 Manager.setLatitude(latitude);
					 Manager.setLongitude(longitude);
				     Toast.makeText(SelectLocation.this, "Select Your Second Location To Teleport", Toast.LENGTH_SHORT).show();
					 count++;
				}
			});
		    
		    dialog.setNegativeButton("No", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					
				}
			});
		    dialog.show();
		}
		else if(count == 1)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(SelectLocation.this);
		    dialog.setTitle("Select Location");
		    dialog.setMessage("Location: " + getLocationNames(lati, longi));
		    dialog.setPositiveButton("Yes", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					 
					 Intent intent = new Intent(SelectLocation.this,Review.class);
					 Manager.setLatitude2(latitude);
					 Manager.setLongitude2(longitude);
					 startActivity(intent);
				}
			});
		    
		    dialog.setNegativeButton("No", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					
				}
			});
		    dialog.show();
		}

	}
	
	
	public void openingAlert()
	{
		String message = "1. Pointers show " + friend + " recent locations!" + "\n" + "2. Long press on map for selecting any location!"+"\n"+"3. Are you on a Message Hunt with Teleport? Select two locations!";
		new AlertDialog.Builder(this).setTitle("Hints").setMessage(message).setIcon(R.drawable.map_pointer).setNeutralButton("OK", null).show();
	}
}
