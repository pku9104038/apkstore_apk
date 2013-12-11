/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.android.aid.ApkFileHelper.ApkInfos;
import com.android.aid.DBSchemaDownload.Columns;

/**
 * @author wangpeifeng
 *
 */
public class ThreadDownloadApk extends ThreadDownload{

	public ThreadDownloadApk(Context context){
		super(context);
	}
	public ThreadDownloadApk(Context context, DBSchemaDownload schema,
			String download_root, String download_path, String save_path,
			Columns columns) {
		super(context, schema, download_root, download_path, save_path, columns);
		// TODO Auto-generated constructor stub
	}
	
	private static ArrayList<ThreadDownload> lstThreads;
	private static HashMap<String, ThreadDownload> mapThreads;// file name mapping to threads

	@Override
	protected ArrayList<ThreadDownload> getThreadList() {
		// TODO Auto-generated method stub
		if(lstThreads == null){
			lstThreads = new ArrayList<ThreadDownload>();
		}
		return lstThreads;
	}

	@Override
	protected HashMap<String, ThreadDownload> getThreadMap() {
		// TODO Auto-generated method stub
		if(mapThreads == null){
			mapThreads = new HashMap<String, ThreadDownload>();
		}
		
		return mapThreads;
	}
	
	
	

	/* (non-Javadoc)
	 * @see com.android.aid.ThreadDownload#exitDownload()
	 */
	@Override
	protected void exitDownload() {
		// TODO Auto-generated method stub
		saveDownloaded();
		removeDownloadThread(this,this.filename);
		onDownloadFinished();
	}
	
	private void saveDownloaded(){
		try{
			if(this.finished){
				
				new ApkFileHelper(context).removeApkFile(column.getPackage());
				File file = new File(getSaveFile() + StoragePreferences.getDownloadSuffix()); 
				file.renameTo(new File(getSaveFile()));
				schema.deleteDownloaded(this.filename);
				

				String path = getSaveFile();
				String packageName = new ApkFileHelper(context).getApkBasicInfos(path).getPackage();
				new DBSchemaReportDownload(context).addActionRecord(column.getPackage(), 
							column.getVerCode(), DBSchemaReport.VAL_ACTION_DOWNLOAD_FINISH);
					
				if(new DevicePreferences(context).getPackage().equals(packageName)){
					ApkInfos info = new ApkFileHelper(context).getApkBasicInfos(path);
					String label = info.getLabel();
					String vername = info.getVerName();
					new NotifyHelper(context).notifyNewVersion(label, vername,path );
					new VersionPreferences(context).setVerDownloadedFile(this.filename);
				}
				else{
					new IntentSender(context).startDownloadedUpdate();
					new IntentSender(context).startApkInstall(this.getSaveFile());
					
				}
				
			}
			else{
				if(this.download_count == DBSchemaDownload.VAL_DOWNLOAD_COUNT_MAX){
					schema.deleteDownloaded(this.filename);
					if(this.filename.substring(this.filename.lastIndexOf('.')).toLowerCase().equals(".apk")){
						new DBSchemaReportDownload(context).addActionRecord(column.getPackage(), 
								column.getVerCode(), DBSchemaReport.VAL_ACTION_DOWNLOAD_DROP);
					}
				}
				else{
					schema.updateDownloadFile(this.filename, this.download_count+1);
				}
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDownloadFinished() {
		// TODO Auto-generated method stub
		if(this.downloadFlag){
			new IntentSender(context).startApkDownload();
		}
	}

}
