/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqDownloadApkDelegate extends ReqDownloadDelegate {

	public ReqDownloadApkDelegate(Context context, String requestAction,
			DBSchemaDownload schema) {
		super(context, requestAction, schema);
		// TODO Auto-generated constructor stub
	}
	
	class ApkVersionSyncApi extends WebServiceApi{

		public ApkVersionSyncApi(Context context, String web_api_pref_key,
				HttpPostListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private HttpPostListener lsrHttpPost = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			
		}
		
	};

	@Override
	protected void startDownload() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getName(), "start download apk");
		if(new ConfigPreferences(context).isDownloadConnection()){
			Log.i(this.getClass().getName(), "download connected");
			ArrayList<DBSchemaDownloadApk.Columns> lstDownloading = schema.getDownloading();
			if(lstDownloading.size()>0){
				Log.i(this.getClass().getName(), "download list > 0");
				Iterator<DBSchemaDownloadApk.Columns> iterator = lstDownloading.iterator();
				int i = 0;
				while(iterator.hasNext()){
					Log.i(this.getClass().getName(), "download list :" + i);
					DBSchemaDownloadApk.Columns row = iterator.next();
					
					ThreadDownloadApk thread = new ThreadDownloadApk(context, 
							schema,
							this.serverUrl, 
							StoragePreferences.getApkPath(),
							//row.getFileFrom(), 
							StoragePreferences.getApkDir(),
							row);
					if(!thread.startDownloadThread()){
						break;
					}
					else if(thread.getThreadList().size()==ThreadDownload.MAX_THREADS){
						break;
					}
					i++;
					
				}
			}
		}
		
	}


}
