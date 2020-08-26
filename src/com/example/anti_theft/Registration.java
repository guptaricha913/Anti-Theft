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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends Activity 
{
	EditText name,email_id,pin,imei,contact,alter_contact,password,confirm_password;
	Button btn_Register;
	boolean valid=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		name=(EditText)findViewById(R.id.ed_Reg_Name);
		email_id=(EditText)findViewById(R.id.ed_Reg_Email);
		pin=(EditText)findViewById(R.id.ed_Reg_Pin);
		imei=(EditText)findViewById(R.id.ed_Reg_IMEI);
		contact=(EditText)findViewById(R.id.ed_Reg_MobileNo);
		alter_contact=(EditText)findViewById(R.id.ed_Reg_AlternativeContact);
		password=(EditText)findViewById(R.id.ed_Reg_Password);
		confirm_password=(EditText)findViewById(R.id.ed_Reg_ConfirmPassword);
		btn_Register=(Button)findViewById(R.id.btnRegistered);
		final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";		
		IP_Address ip = new IP_Address();
    	final String ip_addr=ip.getIP();
		try
		{
			final TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
	        mngr.getDeviceId();	        
	        imei.setText(mngr.toString());
		}
		catch(Exception e)
		{
			imei.setText("Exception+"+e.toString());
		}
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
													//String msg=database.insert(name.getText().toString(), email_id.getText().toString(), pin.getText().toString(),imei.getText().toString(), contact.getText().toString(), alter_contact.getText().toString(), password.getText().toString());
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
									                    HttpPost hp = new HttpPost("http://"+ip_addr+"/antitheft/register.php");
									                    hp.setEntity(new UrlEncodedFormEntity(namevaluepair));
									                    HttpResponse response = httpclient.execute(hp);
									                    HttpEntity entity = response.getEntity();
									                    ir = entity.getContent();
									                    Log.e("MYERROR", entity.toString());
									                    String msg = "Data Inserted";
									                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
									                    Intent i1 = new Intent(getApplicationContext(),LoginActivity.class);
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
