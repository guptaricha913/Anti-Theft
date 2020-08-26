package com.example.anti_theft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class SimDetect extends BroadcastReceiver {
	   @Override
	   public void onReceive(Context context, Intent intent) 
	   {	
		   int count=0;
		   try
		   {
			   	final TelephonyManager tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				final SaveNo database=new SaveNo(context.getApplicationContext(), null, null, 0);
				Cursor sim_available=database.getConfigurations();
				sim_available.moveToFirst();		
				String serial=tm.getSimSerialNumber();
					if(sim_available.getCount()>0)
					{
						
						for(int i=0;i<sim_available.getCount();i++)
						{
							if(sim_available.getString(i).toString().equals(serial))
							{
								count++;
								Toast.makeText(context.getApplicationContext(),"Sim available",2000).show();
							}
							if(!sim_available.getString(i).toString().equals(serial) && serial != null)
							{							
								Toast.makeText(context.getApplicationContext(),"Another sim inserted",2000).show();
								/*try {
					                String message="Your original sim card is removed and has been changed with another sim card";
					                   SmsManager smsManager = SmsManager.getDefault();
					                   smsManager.sendTextMessage(phoneNo, null, message, null, null);
					                   Toast.makeText(context.getApplicationContext(), "SMS sent.\n"+message, Toast.LENGTH_LONG).show();
					                } 
					                
					                catch (Exception e) {}	*/
							}
							if(serial == null)
							{
								Toast.makeText(context.getApplicationContext(),"No sim available",2000).show();
							}
						}
					}
					else
					{	
						
					}
			}		  
		   catch(Exception e)
		   {
		   }
	   }
}
