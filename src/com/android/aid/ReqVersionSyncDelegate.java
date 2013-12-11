/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.aid.ApkFileHelper.ApkInfos;


import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqVersionSyncDelegate extends ServiceActionDelegate{
	
	
	public ReqVersionSyncDelegate(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}

	private class VersionSyncApi extends WebServiceApi{

		public VersionSyncApi(Context context, String web_api_pref_key,
				HttpListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
					new StampPreferences(context).getVersionSyncStamp()+""));
			this.postApi(params);	
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
					new StampPreferences(context).getVersionSyncStamp()+""));
			this.getApi(params);	
			
		}
		
	}

	private HttpGetListener lsrVersionSync = new HttpGetListener(context){

		@Override
		public void OnHttpGetResponse(String[] response) {
			// TODO Auto-generated method stub
			//Log.i(this.getClass().getName(),response[HttpPostListener.POST_URL]+" Resp: "+response[HttpPostListener.POST_RESP]);
			
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				
				DevicePreferences devPref = new DevicePreferences(context);
				int newSN = WebServiceApi.getRespSN(response[HttpListener.HTTP_RESP]);
				int nowSN = devPref.getDevSN();
				//Log.i(this.getClass().getName(), "nowSn/newSn="+nowSN+"/"+newSN);
				if(newSN != nowSN){
					devPref.setDevSN(newSN);
					//Log.i(this.getClass().getName(), "set SN:"+newSN);
				}
				
				try{
					JSONArray array = WebServiceApi.getRespArray(response[HttpListener.HTTP_RESP]);
					if(array.length() > 0){
						JSONObject obj = array.getJSONObject(0);
						int newVerCode = obj.getInt(DBSchemaApplications.COL_VERCODE[DBSchema.COL_NAME]);
						int nowVerCode = new PackageHelper(context).getPackageInfo(devPref.getPackage()).versionCode;
						
						//Log.i(this.getClass().getName(),"newVerCode/nowVerCode="+newVerCode+"/"+nowVerCode);
						if(newVerCode>nowVerCode){
							String label = obj.getString(DBSchemaApplications.COL_LABEL[DBSchema.COL_NAME]);
							String vername = obj.getString(DBSchemaApplications.COL_VERNAME[DBSchema.COL_NAME]);
							String apkfile = obj.getString(DBSchemaApplications.COL_APKFILE[DBSchema.COL_NAME]);
							String path = StoragePreferences.getApkDir()+apkfile;
							//Log.i(this.getClass().getName(), "path:"+path);
							/*
							ApkFileHelper apkhelper = new ApkFileHelper(context);
							String downloadedVerFile = apkhelper.getApkFile(context.getPackageName());
							boolean downloadNewVersion = true;
							if( downloadedVerFile != null){
								ApkInfos apkinfo = apkhelper.getApkBasicInfos(downloadedVerFile);
								if(apkinfo != null){
									if(apkinfo.getVerCode()>=newVerCode){
										downloadNewVersion = false;
										if(new StampPreferences(context).isVersionSync()){	
											new NotifyHelper(context).notifyNewVersion(label, vername, path);
											new StampPreferences(context).setVersionSyncStamp();
										}
									}
								}
							}
							*/
							
							if((new File(path)).exists()){
								
								Log.i(this.getClass().getName(), "notify:"+label+vername);
								if(new StampPreferences(context).isVersionSync()){	
									new NotifyHelper(context).notifyNewVersion(label, vername, path);
									new StampPreferences(context).setVersionSyncStamp();
								}
								
							}
							else{
							//if(downloadNewVersion){
								//Log.i(this.getClass().getName(), "download new version"+apkfile);
								
								DBSchemaDownloadApk schema = new DBSchemaDownloadApk(context);
								//schema.addDownloadFile(columns.getSerial());
								
								int serial = obj.getInt(DBSchemaApplications.COL_APP_SERIAL[DBSchema.COL_NAME]);
								String packageName = obj.getString(DBSchemaApplications.COL_PACKAGE[DBSchema.COL_NAME]);
								schema.addDownloadFile(serial,
										packageName,newVerCode, apkfile);
								//schema.addDownloadFile(apkfile);
								
								new IntentSender(context).startApkDownload();
							}
						}
					}
				}
				catch(JSONException e){
					e.printStackTrace();
				}
			}
			//clearBusy();
			
		}
		
		
	};
	
	

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		if(new StampPreferences(context).isVersionSync() /*&& !isBusy()*/){	
			//setBusy();
				//new NotifyHelper(context).notifyNewVersion(label, vername, path);
			//new StampPreferences(context).setVersionSyncStamp(System.currentTimeMillis());
			
			new VersionSyncApi(this.getContext(),
					WebServicePreferences.PREF_KEY_API_VERSION_SYNC,
					this.lsrVersionSync).getApi();
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
