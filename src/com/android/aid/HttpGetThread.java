/**
 * 
 */
package com.android.aid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class HttpGetThread extends Thread{
	
	/*
	 * CONSTANTS, PUBLIC
	 */
	/*
	 * CONSTANTS, PRIVATE
	 */
	
	/*
	 * PROPERTRIES, PRIVATE 
	 */
	private Context context;
	private String url;
	private ArrayList<NameValuePair> params;
	private String response[];
    private HttpGetListener listener;
	
    
	/*
	 * PROPERTIES, PUBLIC
	 */
	
	/*
	 * PROPERTIES, PROTECTED
	 */
	
	
	/*
	 * PROPERTIES, BEHAVIAR
	 */
	
	
	/*
	 * CONSTRUCTORS
	 */
    
	/**
	 * @param url
	 * @param params
	 * @param listener
	 */
	public HttpGetThread(Context context, String url, ArrayList<NameValuePair> params,
			HttpGetListener listener) {
		super();
		this.context = context;
		this.url = url;
		this.params = params;
		this.listener = listener;
		this.response = new String[2];
		this.response[HttpListener.HTTP_URL] = this.url;
		this.response[HttpListener.HTTP_RESP] = "";
		this.addDeviceAppParams();
	}
	
	private void addDeviceAppParams(){
		DevicePreferences devPref = new DevicePreferences(context);
		if(this.params == null){
			this.params = new ArrayList<NameValuePair>();
		}
		this.params.add(new BasicNameValuePair(DevicePreferences.PREF_KEY_DEV_SN, 
				devPref.getDevSN()+""));

		this.params.add(new BasicNameValuePair(DevicePreferences.PREF_KEY_DEV_IMEI, 
				devPref.getDevIMEI()));

		this.params.add(new BasicNameValuePair(DevicePreferences.PREF_KEY_DEV_BRAND, 
				devPref.getDevBrand()));
		
		this.params.add(new BasicNameValuePair(DevicePreferences.PREF_KEY_DEV_MODEL, 
				devPref.getDevModel()));

		this.params.add(new BasicNameValuePair(DevicePreferences.PREF_KEY_DEV_SDK, 
				devPref.getDevSDK()+""));

		this.params.add(new BasicNameValuePair(DevicePreferences.PREF_KEY_APP_PACKAGE, 
				devPref.getPackage()));
		
		this.params.add(new BasicNameValuePair(DevicePreferences.PREF_KEY_APP_VERCODE, 
				devPref.getVerCode()+""));
		
		this.params.add(new BasicNameValuePair(DevicePreferences.PREF_KEY_APP_NAME, 
				devPref.getAppName()));
		
	}


	/*
	 * METHODS, OVERRIDE
	 */

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
	    try {
			super.run();
			String respBuff = DataConnectionHelper.DATA_CONNECTION_OFFLINE;
			DefaultHttpClient httpClient = new DefaultHttpClient();
		    //HttpPost httpPost = new HttpPost(this.url);
		    StringBuilder sb = new StringBuilder(this.url);
		    sb.append('?');
		    Iterator<NameValuePair> iterator = this.params.iterator();
		    while(iterator.hasNext()){
		    	NameValuePair pair = iterator.next();
		    	//Log.i(this.getClass().getName(), pair.getName()+"=>"+pair.getValue());
		    	sb.append(pair.getName()).append('=').append(URLEncoder.encode(pair.getValue(),"UTF-8")).append("&");
		    }
		    sb.deleteCharAt(sb.length()-1);
		    HttpGet httpGet = new HttpGet(sb.toString());  
		    
	    	if(new DataConnectionHelper(context).isDataOnline()){
		    	//httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
	    		httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		    	//httpPost.setEntity(new UrlEncodedFormEntity(this.params,HTTP.UTF_8));
		    	
		    	Log.i(getClass().getName(), this.toString());
		    	
		    	
		    	HttpResponse httpResp = httpClient.execute(httpGet);
		    	if(httpResp.getStatusLine().getStatusCode()==200){
					HttpEntity httpEntity = httpResp.getEntity();
		
				    if(httpEntity != null){
						BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
						String line = null;
						respBuff = "";
						while((line = br.readLine()) != null){
							respBuff += line;
						}
					}
		    	}
	    	}
	    	else{
				//this.response[HttpPostListener.POST_RESP] += DataConnectionHelper.DATA_CONNECTION_OFFLINE;
	    	}
	    	this.response[HttpListener.HTTP_RESP] += respBuff;
	    	Log.i(getClass().getName(), this.url +" GetResp : "+response[HttpListener.HTTP_RESP]);
			
		    
	    }catch (Exception e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    	//Log.i(getClass().getName(), "Exception : cause="+ e.getCause()+", msg="+e.getMessage()+", locMsg="+e.getLocalizedMessage());
			
		}
	    finally{
	    	this.listener.OnResponse(this.response);
	    	
	    }
		
	}




	/* (non-Javadoc)
	 * @see java.lang.Thread#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String string = "HttpGet: ";
		StringBuilder sb = new StringBuilder(this.url);
	    sb.append('?');
	    Iterator<NameValuePair> iterator = this.params.iterator();
	    while(iterator.hasNext()){
	    	NameValuePair pair = iterator.next();
	    	try{
	    		sb.append(pair.getName()).append('=').append(URLEncoder.encode(pair.getValue(),"UTF-8")).append("&");
	    	}
	    	catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	    sb.deleteCharAt(sb.length()-1);
		
		return string+sb.toString();
		//return super.toString();
	}
	
	
	
}
