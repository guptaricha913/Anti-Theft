package com.example.anti_theft;
import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
public class IncomingMessage extends BroadcastReceiver implements LocationListener
{
	final SmsManager sms = SmsManager.getDefault();	
	LocationManager locationManager ;
	Context cntxt=null;
    String provider;
    String simDetection,siren,getLocation,lock_screen,email,pin,msgdPin;
    private static final String TAG = "tempActivity";
	private DevicePolicyManager policyManager;
	private ComponentName componentName;
	private Activity activity;
		public void onReceive(Context context, Intent intent) 
		{
		cntxt=context;
		try
		{
			enableInternet(true);
			turnGPSOn();
		}
		catch(Exception e)
		{}
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		final Bundle bundle = intent.getExtras();		
		final GetRegistrationDB getdb1=new GetRegistrationDB();		
		final GetConfigurationDB getdb = new GetConfigurationDB();
		String message = null;
		try {
			if (bundle != null) {
				
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				
				for (int i = 0; i < pdusObj.length; i++) {
					
					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();
			        message = currentMessage.getDisplayMessageBody();
			        String[] msgArray = message.split(",");
			        email=msgArray[0];
			        msgdPin=msgArray[1];
				} 
              } 

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" +e);			
		}
		final String data = getdb.geConfigDB(email);		
		final String data1 = getdb1.getRegistered(email);
		if(data.compareTo("[1]")!=0 && data1.compareTo("[1]")!=0)
		{
			try 
			 { 
				 JSONArray jArray = new JSONArray(data);
			     for (int i = 0; i < jArray.length(); i++) 
			     {
			        JSONObject json_data = jArray.getJSONObject(i);
			        simDetection = json_data.getString("simcard_detection");
			        siren = json_data.getString("shout_siren");
			        getLocation = json_data.getString("get_location");
			        lock_screen = json_data.getString("lockscreen");
			     }
			     JSONArray jArray1 = new JSONArray(data1);
			     for (int i = 0; i < jArray.length(); i++) 
			     {
			        JSONObject json_data = jArray1.getJSONObject(i);
			        pin=json_data.getString("pin");
			     }
			     if(msgdPin.trim().equals(pin.trim()) && siren.trim().equals("true"))
			        {
						playClick(context);
			        }
			     if(msgdPin.trim().equals(pin.trim()) && lock_screen.trim().equals("true"))
			     {
			    	 try
			 		{	 
			 			if(Build.VERSION.SDK_INT > 8){
			 	            StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 	            StrictMode.setThreadPolicy(policy1);
			 	        }
			 			policyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);		
			 			componentName = new ComponentName(context, Admin.class);		
			 			mylock();
			 		}
			 		catch(Exception e)
			 		{
			 			Toast.makeText(cntxt.getApplicationContext(),"Error " +e,Toast.LENGTH_LONG).show();
			 		}
			     }
			 }
			 catch (JSONException e) 
			 {}			
		}		
		try{
			locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        provider = locationManager.getBestProvider(criteria, false);
	        if(provider!=null && !provider.equals("")){
	            Location location = locationManager.getLastKnownLocation(provider);
	            locationManager.requestLocationUpdates(provider,20000, 1, this);
	            if(location!=null)
	                onLocationChanged(location);
	            else
	            {}
	        }
	        else{}
		}
		catch(Exception e)
		{}	
		try
		{
			enableInternet(false);
			turnGPSOff();
		}
		catch(Exception e)
		{}
		}
		public void turnGPSOn()
		{
		     Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		     intent.putExtra("enabled", true);
		     this.cntxt.sendBroadcast(intent);

		    String provider = Settings.Secure.getString(cntxt.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		    if(!provider.contains("gps")){ //if gps is disabled
		        final Intent poke = new Intent();
		        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
		        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		        poke.setData(Uri.parse("3")); 
		        this.cntxt.sendBroadcast(poke);
		    }
		}
		public void turnGPSOff()
		{
		    String provider = Settings.Secure.getString(cntxt.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		    if(provider.contains("gps")){ //if gps is enabled
		        final Intent poke = new Intent();
		        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		        poke.setData(Uri.parse("3")); 
		        this.cntxt.sendBroadcast(poke);
		    }
		}
		void enableInternet(boolean yes)
		{
		    ConnectivityManager iMgr = (ConnectivityManager)cntxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		    Method iMthd = null;
		    try {
		        iMthd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled",boolean.class);
		        } 
		    catch (Exception e) {} 
		    iMthd.setAccessible(false);
		    if(yes)
		     {

		                try {
		                    iMthd.invoke(iMgr, true);
		                    Toast.makeText(cntxt, "Data Connection Enabled", Toast.LENGTH_SHORT).show();
		                } 
		                catch (Exception e) 
		                {
		                	Toast.makeText(cntxt.getApplicationContext(),"Error"+e, Toast.LENGTH_SHORT).show();
		                }
		     }
		    else
		     {
		        try 
		        {
		            iMthd.invoke(iMgr, true);
		            Toast.makeText(cntxt, "Data connection Disabled", Toast.LENGTH_SHORT).show();
		        }
		        catch (Exception e) 
		        { }
		     }
		}	
	public void playClick(Context context)
	{
	    try
	    {
	    	MediaPlayer mp = MediaPlayer.create(context, R.raw.siren);  
		    mp.start();
	    }
	    catch(Exception e)
	    { }
	}
	
	@Override
	public void onLocationChanged(Location location) 
	{
		if(msgdPin.equals(pin) && getLocation.equals("true"))
		{
			Toast.makeText(cntxt.getApplicationContext(),"Longitude:" + location.getLongitude()+"\nLatitude:" + location.getLatitude(), Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	private void mylock() 
	{
		try
		{
			try
			{
				policyManager.setPasswordQuality(componentName,DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
				policyManager.setPasswordMinimumLength(componentName,5);
				boolean result = policyManager.resetPassword(msgdPin,DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
				Toast.makeText(cntxt.getApplicationContext(),"Result : "+result,Toast.LENGTH_LONG).show();
			}
			catch(Exception e)
			{
				Toast.makeText(cntxt.getApplicationContext(),"Error 1:" +e,Toast.LENGTH_LONG).show();
			}
			boolean active = policyManager.isAdminActive(componentName);			
			if (!active) 
			{ 
				activeManage(); 
				policyManager.lockNow(); 
				Toast.makeText(cntxt.getApplicationContext(),"trying",Toast.LENGTH_LONG).show();
			} else 
			{
				Toast.makeText(cntxt.getApplicationContext(),"Already have access",Toast.LENGTH_LONG).show();
				policyManager.lockNow();
			}
			activity.finish();
		}
		catch(Exception e)
		{
			Toast.makeText(cntxt.getApplicationContext(),"Error : "+e,Toast.LENGTH_LONG).show();
		}		
	}

	private void activeManage() {
		try
		{	
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "developers");
			this.activity = (Activity)cntxt;
			activity.startActivityForResult(intent, 1);
			Toast.makeText(cntxt.getApplicationContext(),"enable",Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
			Toast.makeText(cntxt.getApplicationContext(),"Error 1: "+e,Toast.LENGTH_LONG).show();
		}
	}
}

/*package com.example.anti_theft;
import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
public class IncomingMessage extends BroadcastReceiver implements LocationListener
{
	final SmsManager sms = SmsManager.getDefault();	
	LocationManager locationManager ;
	Context cntxt=null;
    String provider;
    String simDetection,siren,getLocation,lock_screen,email,pin,msgdPin;
		public void onReceive(Context context, Intent intent) 
		{
		cntxt=context;
		try
		{
			enableInternet(true);
			turnGPSOn();
		}
		catch(Exception e)
		{}
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		final Bundle bundle = intent.getExtras();		
		final GetRegistrationDB getdb1=new GetRegistrationDB();		
		final GetConfigurationDB getdb = new GetConfigurationDB();
		String message = null;
		try {
			if (bundle != null) {
				
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				
				for (int i = 0; i < pdusObj.length; i++) {
					
					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();
			        message = currentMessage.getDisplayMessageBody();
			        String[] msgArray = message.split(",");
			        email=msgArray[0];
			        msgdPin=msgArray[1];
				} 
              } 

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" +e);			
		}
		final String data = getdb.geConfigDB(email);		
		final String data1 = getdb1.getRegistered(email);
		if(data.compareTo("[1]")!=0 && data1.compareTo("[1]")!=0)
		{
			try 
			 { 
				 JSONArray jArray = new JSONArray(data);
			     for (int i = 0; i < jArray.length(); i++) 
			     {
			        JSONObject json_data = jArray.getJSONObject(i);
			        simDetection = json_data.getString("simcard_detection");
			        siren = json_data.getString("shout_siren");
			        getLocation = json_data.getString("get_location");
			        lock_screen = json_data.getString("lockscreen");
			     }
			     JSONArray jArray1 = new JSONArray(data1);
			     for (int i = 0; i < jArray.length(); i++) 
			     {
			        JSONObject json_data = jArray1.getJSONObject(i);
			        pin=json_data.getString("pin");
			     }
			     if(msgdPin.trim().equals(pin.trim()) && siren.trim().equals("true"))
			        {
						playClick(context);
			        }
			 }
			 catch (JSONException e) 
			 {}			
		}		
		try{
			locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        provider = locationManager.getBestProvider(criteria, false);
	        if(provider!=null && !provider.equals("")){
	            Location location = locationManager.getLastKnownLocation(provider);
	            locationManager.requestLocationUpdates(provider,20000, 1, this);
	            if(location!=null)
	                onLocationChanged(location);
	            else
	            {}
	        }
	        else{}
		}
		catch(Exception e)
		{}	
		try
		{
			enableInternet(false);
			turnGPSOff();
		}
		catch(Exception e)
		{}
		}
		public void turnGPSOn()
		{
		     Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		     intent.putExtra("enabled", true);
		     this.cntxt.sendBroadcast(intent);

		    String provider = Settings.Secure.getString(cntxt.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		    if(!provider.contains("gps")){ //if gps is disabled
		        final Intent poke = new Intent();
		        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
		        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		        poke.setData(Uri.parse("3")); 
		        this.cntxt.sendBroadcast(poke);
		    }
		}
		public void turnGPSOff()
		{
		    String provider = Settings.Secure.getString(cntxt.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		    if(provider.contains("gps")){ //if gps is enabled
		        final Intent poke = new Intent();
		        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		        poke.setData(Uri.parse("3")); 
		        this.cntxt.sendBroadcast(poke);
		    }
		}
		void enableInternet(boolean yes)
		{
		    ConnectivityManager iMgr = (ConnectivityManager)cntxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		    Method iMthd = null;
		    try {
		        iMthd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled",boolean.class);
		        } 
		    catch (Exception e) {} 
		    iMthd.setAccessible(false);
		    if(yes)
		     {

		                try {
		                    iMthd.invoke(iMgr, true);
		                    Toast.makeText(cntxt, "Data Connection Enabled", Toast.LENGTH_SHORT).show();
		                } 
		                catch (Exception e) 
		                {
		                	Toast.makeText(cntxt.getApplicationContext(),"Error"+e, Toast.LENGTH_SHORT).show();
		                }
		     }
		    else
		     {
		        try 
		        {
		            iMthd.invoke(iMgr, true);
		            Toast.makeText(cntxt, "Data connection Disabled", Toast.LENGTH_SHORT).show();
		        }
		        catch (Exception e) 
		        { }
		     }
		}	
	public void playClick(Context context)
	{
	    try
	    {
	    	MediaPlayer mp = MediaPlayer.create(context, R.raw.siren);  
		    mp.start();
	    }
	    catch(Exception e)
	    { }
	}
	
	@Override
	public void onLocationChanged(Location location) 
	{
		if(msgdPin.equals(pin) && getLocation.equals("true"))
		{
			Toast.makeText(cntxt.getApplicationContext(),"Longitude:" + location.getLongitude()+"\nLatitude:" + location.getLatitude(), Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
*/