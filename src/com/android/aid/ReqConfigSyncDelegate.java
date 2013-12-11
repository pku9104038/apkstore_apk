/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;


/**
 * @author wangpeifeng
 *
 */
public class ReqConfigSyncDelegate extends ServiceActionDelegate {

	public ReqConfigSyncDelegate(Context context,String requestAction) {
		super(context,requestAction);
		// TODO Auto-generated constructor stub
	}

	class ConfigSyncApi extends ConfigUpdateApi{

		public ConfigSyncApi(Context context, String web_api_pref_key,
				HttpPostListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			if(new StampPreferences(context).isConfigSync()){
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				
				this.postApi(params);	
			}
		}
		
	}
	/* (non-Javadoc)
	 * @see com.android.aid.BaseDelegatee#delegateTask()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		Context context = this.getContext();
		/*
		ConfigUpdateApi mainConfigSyncApi = new ConfigUpdateApi(this.getContext(),
				
				WebServicePreferences.VAL_DEF_API_CONFIG_SYNC_MAIN,
				
				new HttpPostListener(context){

					@Override
					public void OnHttpPostResponse(String[] response) {
						// TODO Auto-generated method stub
						
						ConfigPreferences configPref = new ConfigPreferences(this.getContext());
						configPref.setConfigPreferences(response[HttpPostListener.POST_RESP]);
						
					}
			
				}
				
			);
		
		mainConfigSyncApi.postApi();
		
		*/
		/*
		 * 
		 */
		if(new StampPreferences(this.getContext()).isConfigSync() && !isBusy()){
			setBusy();
			ConfigUpdateApi SecondConfigSyncApi = new ConfigUpdateApi(this.getContext(),
					
					WebServicePreferences.VAL_DEF_API_CONFIG_SYNC_SECOND,
					
					new HttpPostListener(context){
	
						@Override
						public void OnHttpPostResponse(String[] response) {
							// TODO Auto-generated method stub
							ConfigPreferences configPref = new ConfigPreferences(this.getContext());
							configPref.setConfigPreferences(response[HttpListener.HTTP_RESP]);
							new StampPreferences(this.getContext()).setConfigSyncStamp();
							clearBusy();
						}
				
					}
					
				);
			
			SecondConfigSyncApi.postApi();
		}
		
		
	}
	
	protected static boolean isBusy = false;
	
	protected void setBusy(){
		isBusy = true;
	}
	
	protected void clearBusy(){
		isBusy = false;
	}
	
	protected boolean isBusy(){
		return isBusy;
	}

}
