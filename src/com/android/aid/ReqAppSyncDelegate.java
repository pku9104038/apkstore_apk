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
public class ReqAppSyncDelegate extends ServiceActionDelegate {
	
	private static boolean isDataIniting = false;;
	
	public ReqAppSyncDelegate(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}
	
	private class AppsSyncApi extends WebServiceApi{

		public AppsSyncApi(Context context, String web_api_pref_key,
				HttpPostListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			if(new StampPreferences(context).isApplicationsSync()){
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
						new StampPreferences(context).getApplicationsSyncStamp()+""));
				
				//params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APPS_ONLINE, 
				//		new DBSchemaApplications(context).getApplicationSerials()));

				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APP_UPDATE_STAMP, 
						new StampPreferences(context).getApplicationsCloudStamp()));

				this.postApi(params);	
			}
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			if(new StampPreferences(context).isApplicationsSync()){
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
						new StampPreferences(context).getApplicationsSyncStamp()+""));
				
				//params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APPS_ONLINE, 
				//		new DBSchemaApplications(context).getApplicationSerials()));
				
				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APP_UPDATE_STAMP, 
						new StampPreferences(context).getApplicationsCloudStamp()));

				this.getApi(params);	
			}
			
		}
		
	}

	private HttpPostListener lsrAppSync = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			
			//new IntentSender(context).startInitData();
			
			//Log.i(this.getClass().getName(),response[HttpPostListener.POST_URL]+" Resp: "+response[HttpPostListener.POST_RESP]);
			
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP]) ){
				Log.i(this.getClass().getName(), "Appsync response OK:"+response[HttpListener.HTTP_RESP].length());
				JSONArray array = null;
				if(!isDataIniting){
					
					Log.i(this.getClass().getName(),"!isDataIniting");
					
					if((array = WebServiceApi.getRespArray(response[HttpListener.HTTP_RESP]))!=null){
						DBSchemaApplications schema = new DBSchemaApplications(context);
						InitPreferences pref = new InitPreferences(context);
						Log.i(this.getClass().getName(),"getRespArray");
							
						//if(new StampPreferences(context).isApplicationsUpdate()){
							Log.i(this.getClass().getName(),"isApplicationsUpdate");
							new StampPreferences(context).setApplicationsUpdateStamp();
							schema.updateApplicationBasicInfos(array);
							if(schema.isApplicationsAvailable()){
								Log.i(this.getClass().getName(),"isApplicationsAvailable");
								pref.setDataInit(InitPreferences.VAL_DATA_INITED);
								new StampPreferences(context).setApplicationsSyncStamp();
								new StampPreferences(context).setApplicationsCloudStamp(
										WebServiceApi.getRespAppUpdateStamp(response[HttpListener.HTTP_RESP]) );
							}
							else{
								pref.setDataInit(InitPreferences.VAL_DATA_NULL);
							}
						//}
						
					}
				}
			}
			
		}
		
		
	};
	
	

	@Override
	public void action() {
		// TODO Auto-generated method stub
//		if(new StampPreferences(context).isApplicationsSync() ){
			
			new AppsSyncApi(this.getContext(),
					WebServicePreferences.PREF_KEY_API_APPS_SYNC,
					this.lsrAppSync).postApi();
//		}
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
