package com.example.anti_theft;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		Button view_config=(Button)findViewById(R.id.btn_viewConfig);
		Button view_profile=(Button)findViewById(R.id.btn_viewProfile);
		Button edit_profile=(Button)findViewById(R.id.btn_editProfile);
		Button logout=(Button)findViewById(R.id.btn_Logout);
		Button send_msg=(Button)findViewById(R.id.btn_SendMsg);
		Intent i1=getIntent();		
		final String email=i1.getExtras().getString("email");
		view_config.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				Intent config=new Intent(getApplicationContext(),Configuration.class);
				config.putExtra("email",email);
				startActivity(config);				
			}
		});
		view_profile.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				Intent view_Profile=new Intent(getApplicationContext(),ViewProfile.class);
				view_Profile.putExtra("email",email);
				startActivity(view_Profile);				
			}
		});
		edit_profile.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				Intent edit_Profile=new Intent(getApplicationContext(),EditProfile.class);
				edit_Profile.putExtra("email",email);
				startActivity(edit_Profile);				
			}
		});
		logout.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				Intent log_out=new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(log_out);				
			}
		});
		send_msg.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				final GetRegistrationDB getdb=new GetRegistrationDB();
				final String data = getdb.getRegistered(email);
				String phoneNo="",message="",id="",pin="";
				if(data.compareTo("[1]")!=0)
				{
					try
					{
						 JSONArray jArray = new JSONArray(data);
					     for (int i = 0; i < jArray.length(); i++) 
					     {
					        JSONObject json_data = jArray.getJSONObject(i);					        
					        id=json_data.getString("email_id");
					        pin=json_data.getString("pin");
							phoneNo=json_data.getString("contact");							
					     }
				         }
					 catch (JSONException e) 
					 {
					 }
				}                
                try {
                message=id+","+pin;
                   SmsManager smsManager = SmsManager.getDefault();
                   smsManager.sendTextMessage(phoneNo, null, message, null, null);
                   Toast.makeText(getApplicationContext(), "SMS sent.\n"+message, Toast.LENGTH_LONG).show();
                } 
                
                catch (Exception e) {
                   Toast.makeText(getApplicationContext(), "SMS failed, please try again."+e, Toast.LENGTH_LONG).show();
                }				
			}
		});
	}
}


