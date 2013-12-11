/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.android.aid.ApkFileHelper.ApkInfos;
import com.android.aid.DBSchemaDownload.Columns;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ThreadDownloadGUI extends ThreadDownload{

	private static ArrayList<ThreadDownload> lstThreads;
	private static HashMap<String, ThreadDownload> mapThreads;// file name mapping to threads


	
	public ThreadDownloadGUI(Context context, DBSchemaDownload schema,
			String download_root, String download_path, String save_path,
			Columns columns) {
		super(context, schema, download_root, download_path, save_path, columns);
		// TODO Auto-generated constructor stub
	}

	/*
	@Override
	protected String getDownloadRoot() {
		// TODO Auto-generated method stub
		return this.download_root+StoragePreferences.getGuiPath()+SLASH;
	}
	@Override
	public String getSaveRoot() {
		// TODO Auto-generated method stub
		return (new StoragePreferences(context)).getGuiDir();
	}
*/
	
	
	@Override
	protected void onDownloadFinished() {
		// TODO Auto-generated method stub
		if(this.downloadFlag){
			new IntentSender(context).startGUIDownload();
		}
//		new IntentSender(context).broadcastGUIUpdate();
		
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
				
				String path = getSaveFile() + StoragePreferences.getDownloadSuffix();
				new AssetsHelper(context).resizePngIconFile(path, new GroupPreferences(context).getWidgetIconSize());
				
				File file = new File(getSaveFile() + StoragePreferences.getDownloadSuffix()); 
				file.renameTo(new File(getSaveFile()));
				schema.deleteDownloaded(this.filename);
				new IntentSender(context).broadcastUpdateWidgetIcon();
				
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

}
