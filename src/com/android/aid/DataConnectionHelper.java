/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author wangpeifeng
 *
 */
public class DataConnectionHelper {
	
	
	public static final String DATA_CONNECTION_OFFLINE		= "{\"api_resp\":\"offline\"}";
	private Context context;
	
	
	
	/**
	 * @param context
	 */
	public DataConnectionHelper(Context context) {
		super();
		this.context = context;
	}



	public boolean isDataOnline(){
		boolean bool = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null){
			bool = info.isConnected();
		}
		return bool;
	}
	
	public boolean isMobileOnline(){

		boolean bool = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null){
			if(info.isConnected()){
				NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if(mobile != null){
					bool = mobile.isConnected();
				}
			}
		}
    	return bool;

		
	}
	
	public boolean isWLANOnline(){
		
		boolean bool = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null){
			if(info.isConnected()){
				NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if( wifi != null){
					bool = wifi.isConnected();
				}
			}
		}
		return bool;
		
	}
	
	public boolean isConfigOnline(){
		return new ConfigPreferences(context).isDownloadConnection();
	}

}
