/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class DownloadPreferences extends MainSharedPref{

	private static final String PREF_KEY_DOWNLOAD_SERVER_URL	= "download_server_url";
	private static final String VAL_DEF_DOWNLOAD_SERVER_URL		= "http://www.pu-up.com/Downloads/ApkStore/download/";
	
	
	public DownloadPreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "pref_download";
	}
	
	public String getServerUrl(){
		return getString(PREF_KEY_DOWNLOAD_SERVER_URL,VAL_DEF_DOWNLOAD_SERVER_URL);
	}
	
	public void setServerUrl(String url){
		putString(PREF_KEY_DOWNLOAD_SERVER_URL,url);
	}
	

}
