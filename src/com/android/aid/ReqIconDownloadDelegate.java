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
public class ReqIconDownloadDelegate extends ReqDownloadDelegate{



	public ReqIconDownloadDelegate(Context context, String requestAction,
			DBSchemaDownload schema) {
		super(context, requestAction, schema);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void startDownload() {
		// TODO Auto-generated method stub
		//Log.i(this.getClass().getName(), "start Download icons");
		
		ArrayList<DBSchemaIconDownload.Columns> lstGUIDownloading = schema.getDownloading();
		if(lstGUIDownloading.size()>0){
			//Log.i(this.getClass().getName(), "icon list > 0");
			Iterator<DBSchemaGUIDownload.Columns> iterator = lstGUIDownloading.iterator();
			int i = 0;
			while(iterator.hasNext()){
				//Log.i(this.getClass().getName(), "icon list:"+i);
				DBSchemaGUIDownload.Columns row = iterator.next();
				
				ThreadDownloadIcon thread = new ThreadDownloadIcon(context, 
						schema,
						this.serverUrl, 
						StoragePreferences.getIconPath(),
						//row.getFileFrom(), 
						StoragePreferences.getIconDir(),
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


