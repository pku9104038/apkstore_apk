/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class HttpThread extends Thread {
	/*
	 * CONSTANTS, PUBLIC
	 */
	/*
	 * CONSTANTS, PRIVATE
	 */
	
	/*
	 * PROPERTRIES, PRIVATE 
	 */
	protected Context context;
	protected String url;
	protected ArrayList<NameValuePair> params;
	protected String response[];
    protected HttpPostListener listener;
	
    
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

	
	protected  void addDeviceAppParams(){
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

	protected String getUrlName(){
		String name = "";
		name = this.url.substring(this.url.lastIndexOf('/'));
		name = name.substring(0, name.lastIndexOf('.')-1);
		return name;
	}

}
