/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.aid.DBSchemaDownload.Columns;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ThreadDownloadIcon extends ThreadDownload{

	public ThreadDownloadIcon(Context context, DBSchemaDownload schema,
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
		//Log.i(this.getClass().getName(), "thread list size:"+lstThreads.size());
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

	@Override
	protected void onDownloadFinished() {
		// TODO Auto-generated method stub
//		DBSchemaIconDownload schema = new DBSchemaIconDownload(context);
//		schema.deleteDownloaded(this.filename);
		
//		removeDownloadThread(this.filename);
		//new IntentSender(context).startGroupGuiUpdate();
		if(this.downloadFlag){
			new IntentSender(context).startIconDownload();
		}
		
	}
	public static boolean isThreadListEmpty()
	{
		// TODO Auto-generated method stub
		boolean bool = false;
		if(lstThreads==null){
			bool = true;
		}
		else{
			if(lstThreads.size() == 0){
				bool = true;
			}
		}
		return bool;
	}
	
	
}
