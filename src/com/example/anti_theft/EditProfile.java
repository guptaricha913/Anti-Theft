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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfile extends Activity {
	EditText name,email_id,pin,imei,contact,alter_contact,password,confirm_password;
	Button btn_Register;
	boolean valid=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		name=(EditText)findViewById(R.id.ed_Reg_Name1);
		email_id=(EditText)findViewById(R.id.ed_Reg_Email1);
		pin=(EditText)findViewById(R.id.ed_Reg_Pin1);
		imei=(EditText)findViewById(R.id.ed_Reg_IMEI1);
		contact=(EditText)findViewById(R.id.ed_Reg_MobileNo1);
		alter_contact=(EditText)findViewById(R.id.ed_Reg_AlternativeContact1);
		password=(EditText)findViewById(R.id.ed_Reg_Password1);
		confirm_password=(EditText)findViewById(R.id.ed_Reg_ConfirmPassword1);
		btn_Register=(Button)findViewById(R.id.btnUpdate);	
		Button menu=(Button)findViewById(R.id.btn_Menu);
		Intent i1=getIntent();		
		final String email=i1.getExtras().getString("email");
		IP_Address ip = new IP_Address();
    	final String ip_addr=ip.getIP();
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
		final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";		
		final GetRegistrationDB getdb=new GetRegistrationDB();
		final String data = getdb.getRegistered(email);
		if(data.compareTo("[1]")!=0)
		{
			try
			{
				 JSONArray jArray = new JSONArray(data);
			     for (int i = 0; i < jArray.length(); i++) 
			     {
			        JSONObject json_data = jArray.getJSONObject(i);
			        name.setText(json_data.getString("name"));
			        email_id.setText(json_data.getString("email_id"));
					pin.setText(json_data.getString("pin"));
					imei.setText(json_data.getString("imei"));
					contact.setText(json_data.getString("contact"));
					alter_contact.setText(json_data.getString("alter_contact"));
					password.setText(json_data.getString("password"));
					confirm_password.setText(json_data.getString("password"));
			     }
		     }
			 catch (JSONException e) 
			 {
			 }
		}
		email_id.setClickable(false);
		imei.setClickable(false);
		btn_Register.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View arg0) 
			{
				if(!name.getText().toString().equals(""))
				{
					if(!email_id.getText().toString().equals("") || !email_id.getText().toString().matches(emailPattern))
					{
						if(!pin.getText().toString().equals(""))
						{
							if(!contact.getText().toString().equals("") || contact.getText().toString().length()<10)
							{
								if(!alter_contact.getText().toString().equals("") || alter_contact.getText().toString().length()<10)
								{
									if(!password.getText().toString().equals("") || password.getText().toString().length()<8)
									{
										if(!confirm_password.getText().toString().equals(""))
										{
											if(password.getText().toString().equals(confirm_password.getText().toString()))
											{
												if(valid)
												{		
													List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);
									                namevaluepair.add(new BasicNameValuePair("name",name.getText().toString().trim()));
									                namevaluepair.add(new BasicNameValuePair("email_id",email_id.getText().toString().trim()));
									                namevaluepair.add(new BasicNameValuePair("pin",pin.getText().toString().trim()));
									                namevaluepair.add(new BasicNameValuePair("imei",imei.getText().toString().trim()));
									                namevaluepair.add(new BasicNameValuePair("contact",contact.getText().toString().trim()));
									                namevaluepair.add(new BasicNameValuePair("alter_contact",alter_contact.getText().toString().trim()));
									                namevaluepair.add(new BasicNameValuePair("password",password.getText().toString().trim()));
									                try
									                {
									                    InputStream ir = null;
									                    HttpClient httpclient = new DefaultHttpClient();
									                    HttpPost hp = new HttpPost("http://"+ip_addr+"/antitheft/update.php");
									                    hp.setEntity(new UrlEncodedFormEntity(namevaluepair));
									                    HttpResponse response = httpclient.execute(hp);
									                    HttpEntity entity = response.getEntity();
									                    ir = entity.getContent();
									                    Log.e("MYERROR", entity.toString());
									                    String msg = "Data Updated";
									                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
									                    Intent i1 = new Intent(getApplicationContext(),ViewProfile.class);
									                    i1.putExtra("email",email);
									                    startActivity(i1);

									                }
									                catch(Exception ex)
									                {
									                    ex.printStackTrace();
									                    Log.e("MAIN ERROR", ex.toString());
									                }								            
												}
											}	
											else
											{
												Toast.makeText(getApplicationContext(),"Confirmation password not matched",Toast.LENGTH_SHORT).show();					
												valid=false;
												password.setFocusable(true);
											}
										}	
										else
										{
											Toast.makeText(getApplicationContext(),"Enter confirmation password properly",Toast.LENGTH_SHORT).show();
											valid=false;
											confirm_password.setFocusable(true);
										}
									}	
									else
									{
										Toast.makeText(getApplicationContext(),"Enter password properly",Toast.LENGTH_SHORT).show();
										valid=false;
										password.setFocusable(true);
									}
								}
								else
								{
									Toast.makeText(getApplicationContext(),"Enter alternative contact number properly",Toast.LENGTH_SHORT).show();
									valid=false;
									alter_contact.setFocusable(true);
								}
							}
							else
							{
								Toast.makeText(getApplicationContext(),"Enter Contact number properly",Toast.LENGTH_SHORT).show();
								valid=false;
								contact.setFocusable(true);
							}
						}	
						else
						{
							Toast.makeText(getApplicationContext(),"Enter PIN number properly",Toast.LENGTH_SHORT).show();
							valid=false;
							pin.setFocusable(true);
						}
					}
					else
					{
						Toast.makeText(getApplicationContext(),"Enter Email-ID properly",Toast.LENGTH_SHORT).show();
						valid=false;
						email_id.setFocusable(true);
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(),"Enter Name properly",Toast.LENGTH_SHORT).show();
					valid=false;
					name.setFocusable(true);
				}				
			}
		});
	}
}
