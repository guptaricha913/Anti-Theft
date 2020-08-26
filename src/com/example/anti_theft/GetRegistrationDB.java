package com.example.anti_theft;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class GetRegistrationDB {

    public String getDataFromDB(String email_id,String password)
    {
    	IP_Address ip = new IP_Address();
    	String ip_addr=ip.getIP();
        try 
        {
            HttpPost httppost;
            HttpClient httpclient;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://"+ip_addr+"/antitheft/selectRegistrationDB.php?email_id="+email_id+"&password="+password+"");
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost,
                    responseHandler);
            return response.trim();
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR : " + e.getMessage());
            return e.toString();
        }
    }
    public String getRegistered(String email_id)
    {
    	IP_Address ip = new IP_Address();
    	String ip_addr=ip.getIP();
        try 
        {
            HttpPost httppost;
            HttpClient httpclient;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://"+ip_addr+"/antitheft/selectRegisteredDB.php?email_id="+email_id);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost,
                    responseHandler);
            return response.trim();
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR : " + e.getMessage());
            return e.toString();
        }
    }
  }
