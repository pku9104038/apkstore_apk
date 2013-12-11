/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqGroupsSyncDelegate extends ServiceActionDelegate{

	public ReqGroupsSyncDelegate(Context context,String requestAction) {
		super(context,requestAction);
		// TODO Auto-generated constructor stub
	}
	
	class GroupsSyncApi extends WebServiceApi{

		public GroupsSyncApi(Context context, String web_api_pref_key,
				HttpListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			if(new StampPreferences(context).isGroupsSync()){
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
						new StampPreferences(context).getGroupsSyncStamp()+""));
				
				this.postApi(params);	
			}
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			if(new StampPreferences(context).isGroupsSync()){
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
						new StampPreferences(context).getGroupsSyncStamp()+""));
				
				this.getApi(params);	
			}
			
		}
		
	}
	
	private HttpGetListener lsrApi; 
	/*
	= new HttpGetListener(context){

		@Override
		public void OnHttpGetResponse(String[] response) {
			// TODO Auto-generated method stub
			
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				if(WebServiceApi.getRespArray(response[HttpListener.HTTP_RESP])!=null){
					GroupPreferences groupPref = new GroupPreferences(this.getContext());
					groupPref.setPref(response[HttpListener.HTTP_RESP]);
					new StampPreferences(this.getContext()).setGroupSyncStamp();

				}
			}
		}

	};
*/
	/* (non-Javadoc)
	 * @see com.android.aid.BaseDelegatee#delegateTask()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		Context context = this.getContext();
		lsrApi = new HttpGetListener(context){

			@Override
			public void OnHttpGetResponse(String[] response) {
				// TODO Auto-generated method stub
				
				if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
					if(WebServiceApi.getRespArray(response[HttpListener.HTTP_RESP])!=null){
						GroupPreferences groupPref = new GroupPreferences(this.getContext());
						
						groupPref.setPref(response[HttpListener.HTTP_RESP]);
						
						/*
						String old_pref = groupPref.getPrefString();
						int old_stamp = groupPref.getPrefStamp(old_pref);
						int new_stamp = groupPref.getPrefStamp(response[HttpListener.HTTP_RESP]);
						IntentSender sender = new IntentSender(this.getContext());
						if(new_stamp > old_stamp){
							//groupPref.putString(GroupPreferences.PREF_KEY_GROUPS_SYSNC, response[HttpListener.HTTP_RESP]);
						}
						else if(groupPref.isBitmapFileNotAvailable()){
							DBSchemaGUIDownload schema = new DBSchemaGUIDownload(this.getContext());
							schema.addDownloadList(groupPref.getIconFileList());

							Log.i(this.getClass().getName(), "start gui download");
							sender.startGUIDownload();
		
						}
						*/

					}
				}
			}

		};

		if(new StampPreferences(context).isGroupsSync()){
			new GroupsSyncApi(this.getContext(),
					WebServicePreferences.PREF_KEY_API_GROUPS_SYNC,				
					(HttpListener)lsrApi).getApi();
		}
	}



}
