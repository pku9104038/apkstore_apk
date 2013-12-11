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
public class ReqAppubOpenSync extends ServiceActionDelegate {

	public ReqAppubOpenSync(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}

	private class WebApi extends WebServiceApi{

		public WebApi(Context context, String web_api_pref_key,
				HttpPostListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
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
		
	}

	private HttpPostListener lsrHttpPost = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				
				new AppubPreferences(context).openAppubSwitch();
			}
			
		}
		
		
	};
	
	

	@Override
	public void action() {
		// TODO Auto-generated method stub
		if(!new AppubPreferences(context).isAppubOpen()){	
			new WebApi(this.getContext(),
					WebServicePreferences.PREF_KEY_API_APPUB_OPEN,
					this.lsrHttpPost).postApi();
		}
	}

}


