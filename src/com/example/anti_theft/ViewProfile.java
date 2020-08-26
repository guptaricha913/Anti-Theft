package com.example.anti_theft;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewProfile extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_profile);
		TextView fullname=(TextView)findViewById(R.id.tv_fullName);
		TextView email=(TextView)findViewById(R.id.tv_email);
		TextView pin_no=(TextView)findViewById(R.id.tv_pin);
		TextView imei=(TextView)findViewById(R.id.tv_imei);
		TextView contact=(TextView)findViewById(R.id.tv_contact);
		TextView alter_contact=(TextView)findViewById(R.id.tv_alterContact);
		Button menu=(Button)findViewById(R.id.btn_Menu);
		Intent i1=getIntent();		
		final String email_id=i1.getExtras().getString("email");
		menu.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				Intent i=new Intent(getApplicationContext(),MainMenu.class);
				i.putExtra("email",email_id);
				startActivity(i);
			}
		});
		final GetRegistrationDB getdb=new GetRegistrationDB();
		final String data = getdb.getRegistered(email_id);
		if(data.compareTo("[1]")!=0)
		{
			try
			{
				 JSONArray jArray = new JSONArray(data);
			     for (int i = 0; i < jArray.length(); i++) 
			     {
			        JSONObject json_data = jArray.getJSONObject(i);
			        fullname.setText(json_data.getString("name"));
			        email.setText(json_data.getString("email_id"));
					pin_no.setText(json_data.getString("pin"));
					imei.setText(json_data.getString("imei"));
					contact.setText(json_data.getString("contact"));
					alter_contact.setText(json_data.getString("alter_contact"));
			     }
		         }
			 catch (JSONException e) 
			 {
			 }
		}
	}
}
