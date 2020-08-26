package com.example.anti_theft;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		int secondsDelayed = 2;        
        try
        {
        	Context context=getApplicationContext();
            boolean network=checkInternetConnection(context);                   
            //NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            
            if(network==true)
            {
            	new Handler().postDelayed(new Runnable() 
            	{
                    public void run() 
                    {
                            startActivity(new Intent(Splash.this,LoginActivity.class));
                            finish();
                    }
            	}, secondsDelayed * 1000);
            	Toast.makeText(getApplicationContext(),"Connection available",Toast.LENGTH_SHORT).show();
            }
            else
            {
            	Toast.makeText(getApplicationContext(),"Please check internet/WiFi connection",Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e)
        {        	
        }
	}
	public boolean checkInternetConnection(Context context) 
    {
    	
    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    	    if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) 
    	    {
    	        return true;
    	    } 
    	    else 
    	    {
    	        return false;
    	    }
    }
}
