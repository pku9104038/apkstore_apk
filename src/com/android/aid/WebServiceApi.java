/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public abstract class WebServiceApi {

	/*
	 * CONSTANTS, PUBLIC
	 */
	/*
	 * CONSTANTS, PRIVATE
	 */
	
	private static final String HTTP_PROTOCOL						= "http://";
	
	//webservice api response json keys 
	private static final String API_RESP 							= "api_resp";
	private static final String API_RESP_ERR 						= "api_resp_err";
	private static final String API_RESP_MSG						= "api_resp_msg";
	public static final String API_RESP_ARRAY						= "api_resp_array";
	public static final String API_RESP_STAMP						= "api_resp_stamp";//yyyymmddhh
	private static final String API_RESP_SN 						= "dev_sn";
	
	public static final String API_PARAM_STAMP						= "api_param_stamp";//yyyymmddhh
	public static final String API_PARAM_APPS_ONLINE				= "api_param_apps_online";
	public static final String API_PARAM_APP_UPDATE_STAMP			= "api_param_app_update_stamp";
	public static final String API_PARAM_APP_PUUP_STAMP				= "api_param_app_puup_stamp";
	
	public static final String API_PARAM_FILENAME					= "api_param_filename";
	public static final String API_PARAM_APP_SERIAL					= "api_param_app_serial";
	public static final String API_PARAM_APP_PACKAGE				= "api_param_app_package";
	public static final String API_PARAM_APP_LABEL					= "api_param_app_label";
	public static final String API_PARAM_ACCOUNT 					= "account";
	public static final String API_PARAM_PASSWORD					= "password";
	
	//webservice api response json default value 
	public static final String API_RESP_MSG_NULL					= "Web service api response message = NULL!";
	
	/*
	 * PROPERTRIES, PRIVATE 
	 */
	private Context context;
	private HttpListener listener;
	private WebServicePreferences webPref;
	private String web_api_pref_key; 
	
    
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
	 * @param context
	 * @param listener
	 */
	public WebServiceApi(Context context, String web_api_pref_key, HttpListener listener) {
		super();
		this.context = context;
		this.listener = listener;
		this.web_api_pref_key = web_api_pref_key;
		this.webPref = new WebServicePreferences(context);
	}
	
	/*
	 * METHODS, PROTECTED
	 */
	
	protected String getApiUrl(){
		return this.webPref.getApiUrl(this.web_api_pref_key);
	}
	
	/**
	 * postApi  create a new thread to post the params to script located a strUrl
	 * 			then callback the onHttpResponse while get the response from server
	 * @param strUrl
	 * @param params
	 */
	protected void postApi(String strUrl, ArrayList<NameValuePair> params)
	{
		HttpPostThread thread = new HttpPostThread(context ,strUrl, params, (HttpPostListener) this.listener);
		thread.start();
	}
	
	protected void postApi(ArrayList<NameValuePair> params)
	{
		this.postApi(this.getApiUrl(), params);
	}
	

	protected void getApi(String strUrl, ArrayList<NameValuePair> params)
	{
		HttpGetThread thread = new HttpGetThread(context ,strUrl, params, (HttpGetListener)this.listener);
		thread.start();
	}
	
	protected void getApi(ArrayList<NameValuePair> params)
	{
		this.getApi(this.getApiUrl(), params);
	}
	

	/*
	 * METHODS, RESPONSE JSON DECODE
	 */

	/**
	 * isRespSuccess
	 * 
	 * @param response
	 * @return
	 */
	protected static boolean isRespSuccess(String response)
	{
		boolean result = false;
		try {
			result =  new JSONObject(response).getBoolean(API_RESP);
		}
		catch (Exception e){
			e.printStackTrace();
			result = false;
		}
		return result;
		
	}
	/**
	 * 
	 * @param response
	 * @return
	 */
	protected static String getRespMsg(String response)
	{
		try {
			return new JSONObject(response).getString(API_RESP_MSG);
		}
		catch (Exception e){
			e.printStackTrace();
			return API_RESP_MSG_NULL;
		}
	}
	
	/**
	 * 
	 * @param response
	 * @return -1 while no error message 
	 */
	protected static int getRespErr(String response)
	{
		try {
			return new JSONObject(response).getInt(API_RESP_ERR);
		}
		catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 
	 * @param response
	 * @return -1 while no error message 
	 */
	protected static int getRespSN(String response)
	{
		try {
			return new JSONObject(response).getInt(API_RESP_SN);
		}
		catch (Exception e){
			e.printStackTrace();
			return DevicePreferences.VAL_DEF_DEV_SN;
		}
	}

	protected static String getRespAppUpdateStamp(String response)
	{
		try {
			return new JSONObject(response).getString(API_PARAM_APP_UPDATE_STAMP);
		}
		catch (Exception e){
			e.printStackTrace();
			return StampPreferences.VAL_DEF_APPLICATIONS_STAMP;
		}
	}

	protected static String getRespAppPuupStamp(String response)
	{
		try {
			return new JSONObject(response).getString(API_PARAM_APP_PUUP_STAMP);
		}
		catch (Exception e){
			e.printStackTrace();
			return StampPreferences.VAL_DEF_APPLICATIONS_PUUP_STAMP;
		}
	}

	/**
	 * 
	 * @param response
	 * @return null while array not exist
	 */
	protected static JSONArray getRespArray(String response)
	{
		try {
			return new JSONObject(response).getJSONArray(API_RESP_ARRAY);
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * METHODS,WEB SERVICE API
	 */
	/*
	public void apiGroupSync(){
		
		String strUrl = this.webPref.getApiGroupSync();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		postApi(strUrl, params);
	}
	*/
	public abstract void postApi();
	public abstract void getApi();

}
