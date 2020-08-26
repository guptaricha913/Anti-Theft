package com.example.anti_theft;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final EditText edEmail=(EditText)findViewById(R.id.ed_Login_Email);
		final EditText pwd=(EditText)findViewById(R.id.ed_Login_Password);
		Button btn_login=(Button)findViewById(R.id.buttonLogin);		
		Button btn_register=(Button)findViewById(R.id.button_Register);				
		final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		edEmail.setText("");
		pwd.setText("");
		btn_register.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View arg0) 
			{
				Intent register=new Intent(getApplicationContext(),Registration.class);
				startActivity(register);
			}
		});
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
			try
			{
				if(edEmail.getText().toString().equals("") || pwd.getText().toString().equals(""))
				{
					if(edEmail.getText().toString().equals(""))
					{
						Toast.makeText(getApplicationContext(),"Enter Email-ID",Toast.LENGTH_SHORT).show();
						edEmail.setFocusable(true);
					}
					if(pwd.getText().toString().equals(""))
					{
						Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT).show();
						pwd.setFocusable(true);
					}
				}
				else if(!edEmail.getText().toString().matches(emailPattern))
				{
					Toast.makeText(getApplicationContext(),"Enter Valid Email-ID",Toast.LENGTH_SHORT).show();	
					edEmail.setFocusable(true);	
				}
				else
				{
					final GetRegistrationDB getdb = new GetRegistrationDB();
					String data = getdb.getDataFromDB(edEmail.getText().toString().trim(),pwd.getText().toString().trim());
					System.out.println(data);
	                Log.e("log_tag", "Error parsing data " + data);
	                String data1 = data.substring(0,3);
 	                Log.d("url",data1);
					if(data1.compareTo("[1]")==0)
					 {
						 Toast.makeText(getApplicationContext(),"No such user.. Please register",Toast.LENGTH_LONG).show();
						 Intent register=new Intent(getApplicationContext(), Registration.class);
						 startActivity(register);
					 }
					 else
					 {
						 Toast.makeText(getApplicationContext(), "Valid User", Toast.LENGTH_SHORT).show();											 
						 Intent welcome=new Intent(getApplicationContext(),Configuration.class);
						 welcome.putExtra("email",edEmail.getText().toString());
						 startActivity(welcome);
					}
				}
			}	
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "Error: "+e, Toast.LENGTH_LONG).show();
			}
			}
		});
	}	
}