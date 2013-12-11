 /**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ConfigPreferences extends MainSharedPref{

	/*
	 *CONSTANTS, PRIVATE  
	 */
	private static final String PREF_KEY_SERVER_CONFIG	 		= "server_config";
	
	private static final String VAL_DEF_CONFIG_ASSETS	 		= "androidaid3.conf";
	
	private static final String PREF_KEY_CONF_DATE	 			= "conf_date";
	private static final String PREF_KEY_API_HOST	 			= "api_host";
	private static final String PREF_KEY_API_PORT	 			= "api_port";
	private static final String PREF_KEY_API_ROOT	 			= "api_root";
	private static final String PREF_KEY_DOWNLOAD_SERVER 		= "download_server";
	private static final String PREF_KEY_DOWNLOAD_ROOT	 		= "download_root";
	
	private static final String PREF_KEY_DOWNLOAD_CONNECTION 	= "download_connection";
	public static final int VAL_DOWNLOAD_DATAONLINE	 			= 1;
	public static final int VAL_DOWNLOAD_WLANONLINE	 			= 2;
	private static final int VAL_DEF_DOWNLOAD_CONNECTION	 	= VAL_DOWNLOAD_DATAONLINE;
	
	
	private static final String[] VAL_DEF_DOWNLOAD_ROOT	 		= 
		{"http://www.pu-up.com/ApkStore/download/"};
	
	/*
	 * PROPERTIES,PRIVATE
	 */
	
	public ConfigPreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	private String getDefConfig(){
		String string = "";
		
		return string; 
	}
	private String getConfigPreferences(){
		String string = "";
		string = this.getString(PREF_KEY_SERVER_CONFIG, "");
		/*
		if("".equals(string)){
			AssetsHelper assetsHelper = new AssetsHelper(context);
			//String defString = assetsHelper.getAssetsString(VAL_DEF_CONFIG_ASSETS);
			String defString = "";
			string = this.getString(PREF_KEY_SERVER_CONFIG, defString);
			this.setConfigPreferences(string);
		}
		*/
		return string;
	}
	
	private JSONObject getConfigJson(String config) throws JSONException{
		JSONObject json = null;
		json = new JSONObject(config);
		return json;
	}
	
	
	/**
	 * 
	 * @param config
	 * @return int yyyymmdd
	 */
	private int getConfigDate(String config){
		int date = 0;
		try {
			JSONObject obj = new JSONObject(config);
			date = obj.getInt(PREF_KEY_CONF_DATE);
			
		}
		catch(JSONException e){
			e.printStackTrace();
			date = 0;
		}
		finally{
		}
		return date;
	}
	
	/**
	 * isNewConfig
	 * @param config
	 * @return
	 */
	private boolean isNewConfig(String config){
		boolean bool = true;
		int curDate = this.getConfigDate(this.getConfigPreferences());
		int newDate = this.getConfigDate(config);
		if( newDate <= curDate){
			bool = false;
		}
		return bool;
	}

	/**
	 * getApiHost
	 * @param config
	 * @return
	 */
	private String getApiHost(String config){
		String string = "";
		try {
			JSONObject obj = new JSONObject(config);
			string = obj.getString(PREF_KEY_API_HOST);
			
		}
		catch(JSONException e){
			e.printStackTrace();
			WebServicePreferences webPref = new WebServicePreferences(this.context);
			string = webPref.getApiHost();
		}
		finally{
			return string;
		}
	}
	
	/**
	 * getApiHost
	 * @param config
	 * @return
	 */
	private String getApiPort(String config){
		String string = "";
		try {
			JSONObject obj = new JSONObject(config);
			string = obj.getString(PREF_KEY_API_PORT);
			
		}
		catch(JSONException e){
			e.printStackTrace();
			WebServicePreferences webPref = new WebServicePreferences(this.context);
			string = webPref.getApiPort();
		}
		finally{
			return string;
		}
	}
	/**
	 * getApiHost
	 * @param config
	 * @return
	 */
	private String getApiRoot(String config){
		String string = "";
		try {
			JSONObject obj = new JSONObject(config);
			string = obj.getString(PREF_KEY_API_ROOT);
			
		}
		catch(JSONException e){
			e.printStackTrace();
			WebServicePreferences webPref = new WebServicePreferences(this.context);
			string = webPref.getApiRoot();
		}
		finally{
			return string;
		}
	}

	private void updateApis(){
		ArrayList<String[]> lstApis = new WebServicePreferences(context).getApiList();
		Iterator<String[]> iterator = lstApis.iterator();
		if(iterator.hasNext()){
			String[] api = iterator.next();
			String key = api[WebServicePreferences.INDEX_KEY];
			setApi(key);
		}
	}
	
	private void setApi(String key){
	
		String api = getApi(key);
		if( ! VAL_DEF_DEV_INFO_NULL.equals(api)){
			new WebServicePreferences(context).putString(key, api);
		}
		
	}
	
	private String getApi(String key){
		
		String string = VAL_DEF_DEV_INFO_NULL;
		try {
			JSONObject obj = new JSONObject(this.getString(PREF_KEY_SERVER_CONFIG,VAL_DEF_DEV_INFO_NULL));
			string = obj.getString(key);
			
		}
		catch(JSONException e){
			e.printStackTrace();
			string = VAL_DEF_DEV_INFO_NULL;
		}
		return string;
		
	}
	
	/**
	 * setConfigPreferences
	 * @param config
	 */
	public void setConfigPreferences(String config){
		
		if(this.isNewConfig(config)){
			Log.i(this.getClass().getName(), "set new config!");
			
			this.putString(PREF_KEY_SERVER_CONFIG, config);
			
			WebServicePreferences webPref = new WebServicePreferences(this.context);
			webPref.setApiHost(this.getApiHost(config));
			webPref.setApiPort(this.getApiPort(config));
			webPref.setApiRoot(this.getApiRoot(config));
			
			//this.updateApis();
		}
	}

	public String[] getDownloadServers(){
		String[] servers = null;
		try {
			JSONObject obj = this.getConfigJson(getConfigPreferences());
			JSONArray array = obj.getJSONArray(PREF_KEY_DOWNLOAD_SERVER);
			servers = new String[array.length()];
			for(int i = 0; i < array.length(); i++){
				servers[i] = "";
				JSONObject server = array.getJSONObject(i);
				servers[i] = server.getString(PREF_KEY_DOWNLOAD_ROOT);
			}
			
		}
		catch(JSONException e){
			e.printStackTrace();
			servers = VAL_DEF_DOWNLOAD_ROOT;
		}
		
		
		return servers;
	}

	public int getDownloadConnection(){
		return getInt(PREF_KEY_DOWNLOAD_CONNECTION, VAL_DEF_DOWNLOAD_CONNECTION);
		
	}
	
	public void setDownloadConnection(int conn){
		putInt(PREF_KEY_DOWNLOAD_CONNECTION,conn);
	}
	
	public boolean isDownloadConnection(){
		boolean bool = false;
		DataConnectionHelper helper = new DataConnectionHelper(context);
		if(getDownloadConnection()==VAL_DOWNLOAD_DATAONLINE &&
				helper.isDataOnline()){
			bool = true;
		}
		else if( getDownloadConnection()==VAL_DOWNLOAD_WLANONLINE &&
				helper.isWLANOnline()){
			bool = true;
		}
		return bool;
		//return true;
	}
	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "config_pref";
	}


}
