/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqAppPuupSyncDelegate extends ServiceActionDelegate{

	public ReqAppPuupSyncDelegate(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}
	
	private class AppsPuupSyncApi extends WebServiceApi{

		public AppsPuupSyncApi(Context context, String web_api_pref_key,
				HttpPostListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			if(new StampPreferences(context).isApplicationsPuupSync()){
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
						new StampPreferences(context).getApplicationsSyncStamp()+""));
				
//				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APPS_ONLINE, 
//						new DBSchemaApplications(context).getApplicationSerials()));

				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APP_PUUP_STAMP, 
						new StampPreferences(context).getApplicationsPuupCloudStamp()));
				
				this.postApi(params);	
			}
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			if(new StampPreferences(context).isApplicationsPuupSync()){
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
						new StampPreferences(context).getApplicationsSyncStamp()+""));
				
//				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APPS_ONLINE, 
//						new DBSchemaApplications(context).getApplicationSerials()));

				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APP_PUUP_STAMP, 
						new StampPreferences(context).getApplicationsPuupCloudStamp()));
				
				this.getApi(params);	
			}
			
		}
		
	}

	private HttpPostListener lsrAppSync = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			Log.i(this.getClass().getName(),response[HttpListener.HTTP_URL]+" Resp: "+response[HttpListener.HTTP_RESP]);
			
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				JSONArray array = null;
				if((array = WebServiceApi.getRespArray(response[HttpListener.HTTP_RESP]))!=null){
					DBSchemaApplications schema = new DBSchemaApplications(context);
					schema.updateApplicationPuups(array);
					new StampPreferences(context).setApplicationsPuupSyncStamp();
					new StampPreferences(context).setApplicationsCloudStamp(
							WebServiceApi.getRespAppPuupStamp(response[HttpListener.HTTP_RESP]) );

				}
			}
			clearBusy();
			
		}
		
		
	};
	
	

	@Override
	public void action() {
		// TODO Auto-generated method stub
		if(new StampPreferences(context).isApplicationsPuupSync() && !isBusy()){
			setBusy();
			new AppsPuupSyncApi(this.getContext(),
					WebServicePreferences.PREF_KEY_API_APPS_PUUP_SYNC,
					this.lsrAppSync).postApi();
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
