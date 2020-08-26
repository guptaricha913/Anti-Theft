package com.example.anti_theft;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class Configuration extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		final CheckBox sim_detection=(CheckBox)findViewById(R.id.cb_sim_detection);
		final CheckBox shout_siren=(CheckBox)findViewById(R.id.cb_shout_siren);
		final CheckBox get_location=(CheckBox)findViewById(R.id.cb_get_location);
		final CheckBox lock_phone=(CheckBox)findViewById(R.id.cb_lock_phone);
		Button save=(Button)findViewById(R.id.btn_SaveConfig);
		Button menu=(Button)findViewById(R.id.btn_Menu);
		Intent i1=getIntent();		
		String simDetection="",siren="",getLocation="",lock_screen="";
		IP_Address ip = new IP_Address();
    	final String ip_addr=ip.getIP();
		final String email=i1.getExtras().getString("email");
		try
		{
			menu.setOnClickListener(new OnClickListener() 
			{			
				@Override
				public void onClick(View v) 
				{
					Intent i=new Intent(getApplicationContext(),MainMenu.class);
					i.putExtra("email",email);
					startActivity(i);
				}
			});
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(),"Error: "+e, Toast.LENGTH_LONG).show();
		}
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);				
		final GetConfigurationDB getdb = new GetConfigurationDB();
		final String data = getdb.geConfigDB(email);
		if(data.compareTo("[1]")!=0)
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
		         if(simDetection.trim().equals("true"))
		         {
		        	 sim_detection.setChecked(true);
		         }
		         else
		         {
		        	 sim_detection.setChecked(false);
		         }
		         if(siren.trim().equals("true"))
		         {
		        	 shout_siren.setChecked(true);
		         }
		         else
		         {
		        	 shout_siren.setChecked(false);
		         }
		         if(getLocation.trim().equals("true"))
		         {
		        	 get_location.setChecked(true);
		         }
		         else
		         {
		        	 get_location.setChecked(false);
		         }
		         if(lock_screen.trim().equals("true"))
		         {
		        	 lock_phone.setChecked(true);
		         }
		         else
		         {
		        	 lock_phone.setChecked(false);
		         }
			 }
			 catch (JSONException e) 
			 {
				 Toast.makeText(getApplicationContext(),"Error: "+e, Toast.LENGTH_LONG).show();
			 }
		}
		try
		{
			save.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) 
				{
					if(sim_detection.isChecked())
			         {
			        	 sim_detection.setChecked(true);
			        	 try{
			        		 final TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				        		final String serial=tm.getSimSerialNumber();
				        		final SaveNo database=new SaveNo(getApplicationContext(), null, null, 0);
				        		if(serial != "")
								{
									String msg=database.insert(serial);
								}
								else
								{
									Toast.makeText(getApplicationContext(),"No sim available",Toast.LENGTH_LONG).show();
								}
			        	 }
			        	 catch(Exception e)
			        	 {
			        	 }
			         }
			         else
			         {
			        	 sim_detection.setChecked(false);
			        	 try{
			        		 final TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				        		final String serial=tm.getSimSerialNumber();
				        		final SaveNo database=new SaveNo(getApplicationContext(), null, null, 0);
				        		if(serial != "")
								{
									String msg=database.delete(serial);
								}
								else
								{
									Toast.makeText(getApplicationContext(),"No sim available",Toast.LENGTH_LONG).show();
								}
			        	 }
			        	 catch(Exception e)
			        	 {
			        		 Toast.makeText(getApplicationContext(),"Msg "+e,Toast.LENGTH_LONG).show();
			        	 }
			         }
					if(data.compareTo("[1]")==0)
					{
						String sim_detect,shout_sir, get_loc, lock_ph;
						Intent i=getIntent();
						String email=i.getExtras().getString("email");
						sim_detect=String.valueOf(sim_detection.isChecked());
						shout_sir=String.valueOf(shout_siren.isChecked());
						get_loc=String.valueOf(get_location.isChecked());
						lock_ph=String.valueOf(lock_phone.isChecked());
						try
						{		
			                List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);
			                namevaluepair.add(new BasicNameValuePair("email_id",email));
			                namevaluepair.add(new BasicNameValuePair("simcard_detection",sim_detect));
			                namevaluepair.add(new BasicNameValuePair("shout_siren",shout_sir));
			                namevaluepair.add(new BasicNameValuePair("get_location",get_loc));
			                namevaluepair.add(new BasicNameValuePair("lockscreen",lock_ph));
			                try
			                {
			                    InputStream ir = null;
			                    HttpClient httpclient = new DefaultHttpClient();
			                    HttpPost hp = new HttpPost("http://"+ip_addr+"/antitheft/insertConfig.php");
			                    hp.setEntity(new UrlEncodedFormEntity(namevaluepair));
			                    HttpResponse response = httpclient.execute(hp);
			                    HttpEntity entity = response.getEntity();
			                    ir = entity.getContent();
			                    Log.e("MYERROR", entity.toString());
			                    String msg1 = "Configurations Saved";
			                    Toast.makeText(getApplicationContext(), msg1, Toast.LENGTH_LONG).show();
			                }
			                catch(Exception ex)
			                {
			                	Toast.makeText(getApplicationContext(),"Error: "+ex, Toast.LENGTH_LONG).show();
			                }

						}
						catch(Exception e)
						{
							Toast.makeText(getApplicationContext(),"Try Again!",Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						String sim_detect,shout_sir, get_loc, lock_ph;
						Intent i=getIntent();
						String email=i.getExtras().getString("email");
						sim_detect=String.valueOf(sim_detection.isChecked());
						shout_sir=String.valueOf(shout_siren.isChecked());
						get_loc=String.valueOf(get_location.isChecked());
						lock_ph=String.valueOf(lock_phone.isChecked());
						try
						{		
			                List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);
			                namevaluepair.add(new BasicNameValuePair("email_id",email));
			                namevaluepair.add(new BasicNameValuePair("simcard_detection",sim_detect));
			                namevaluepair.add(new BasicNameValuePair("shout_siren",shout_sir));
			                namevaluepair.add(new BasicNameValuePair("get_location",get_loc));
			                namevaluepair.add(new BasicNameValuePair("lockscreen",lock_ph));
			                try
			                {
			                    InputStream ir = null;
			                    HttpClient httpclient = new DefaultHttpClient();
			                    HttpPost hp = new HttpPost("http://"+ip_addr+"/antitheft/updateConfig.php");
			                    hp.setEntity(new UrlEncodedFormEntity(namevaluepair));
			                    HttpResponse response = httpclient.execute(hp);
			                    HttpEntity entity = response.getEntity();
			                    ir = entity.getContent();
			                    Log.e("MYERROR", entity.toString());
			                    String msg1 = "Configurations Updated";
			                    Toast.makeText(getApplicationContext(), msg1, Toast.LENGTH_LONG).show();
			                }
			                catch(Exception ex)
			                {
			                	Toast.makeText(getApplicationContext(),"Error: "+ex, Toast.LENGTH_LONG).show();
			                }

						}
						catch(Exception e)
						{
							Toast.makeText(getApplicationContext(),"Try Again!",Toast.LENGTH_LONG).show();
						}
					}
				}
			});
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
		}
	}
}
