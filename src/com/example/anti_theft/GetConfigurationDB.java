package com.example.anti_theft;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class GetConfigurationDB {

    public String geConfigDB(String email_id)
    {
        try 
        {
        	IP_Address ip = new IP_Address();
        	String ip_addr=ip.getIP();
            HttpPost httppost;
            HttpClient httpclient;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://"+ip_addr+"/antitheft/selectConfigurationDB.php?email_id="+email_id+"");
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
