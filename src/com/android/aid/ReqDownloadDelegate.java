/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public abstract class ReqDownloadDelegate extends ServiceActionDelegate{

	/*
	 * 
	 */
	protected boolean serverChecked;
	protected String serverUrl;
	protected DBSchemaDownload schema ;


	private HttpPostListener lsrDownloadServer = new HttpPostListener(this.context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			//Log.i(this.getClass().getName(),response[HttpPostListener.POST_URL]+" Resp: "+response[HttpPostListener.POST_RESP]);
			
			if(!serverChecked){
				if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
					serverUrl = response[HttpListener.HTTP_URL].substring(0, 
							response[HttpListener.HTTP_URL].length() 
							- WebServicePreferences.VAL_DEF_API_DOWNLOAD_TESTFILE.length());
					serverChecked = true;
					new DownloadPreferences(context).setServerUrl(serverUrl);
					new StampPreferences(context).setDownloadServerStamp();
					Log.i(this.getClass().getName(),"best download server: "+serverUrl);
					try{
						startDownload();
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
		}
		
	};
	
	protected abstract void startDownload();
	

	public ReqDownloadDelegate(Context context, String requestAction, DBSchemaDownload schema) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
		this.schema = schema;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getName(), "ReqDownload");
		if(schema.isDownloading()){
			Log.i(this.getClass().getName(),"isDownloading");
			if(new StampPreferences(context).isDownloadServerTest()){
				ConfigPreferences configPref = new ConfigPreferences(this.context);
				String[] servers = configPref.getDownloadServers();
				
				ArrayList<ConfigUpdateApi> lstServerApi = new ArrayList<ConfigUpdateApi>();
				for(int i = 0; i < servers.length; i++){
					ConfigUpdateApi serverTestApi = new ConfigUpdateApi(context, 
							servers[i]+WebServicePreferences.VAL_DEF_API_DOWNLOAD_TESTFILE,
							lsrDownloadServer);
					lstServerApi.add(serverTestApi);
				}
				for(int i = 0; i < servers.length; i++){
					lstServerApi.get(i).postApi();
				}
			}
			else{
				serverUrl = new DownloadPreferences(context).getServerUrl();
				serverChecked = true;
				startDownload();
			}
		}
		
	}

}
