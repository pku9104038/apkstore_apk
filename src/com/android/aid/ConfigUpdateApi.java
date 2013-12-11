/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class ConfigUpdateApi extends WebServiceApi{

	private String config_url;
	public ConfigUpdateApi(Context context, String web_api_pref_key,
			HttpPostListener listener) {
		super(context, web_api_pref_key, listener);
		this.config_url = web_api_pref_key;//input url directlly
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.android.aid.WebServiceApi#postApi(java.util.ArrayList)
	 */
	@Override
	protected void postApi(ArrayList<NameValuePair> params) {
		// TODO Auto-generated method stub
		this.postApi(this.config_url,params);
	}

	@Override
	public void postApi() {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		this.postApi(params);	
	}

	@Override
	public void getApi() {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		this.getApi(params);	
		
	}
	protected void getApi(ArrayList<NameValuePair> params) {
		// TODO Auto-generated method stub
		this.getApi(this.config_url,params);
	}

}
