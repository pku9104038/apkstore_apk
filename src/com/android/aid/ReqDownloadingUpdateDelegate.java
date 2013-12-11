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
public class ReqDownloadingUpdateDelegate extends ServiceActionDelegate{
	
	private DBSchemaDownloadApk schema;

	public ReqDownloadingUpdateDelegate(Context context, String requestAction,DBSchemaDownloadApk schema) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
		this.schema = schema;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ArrayList<DBSchemaDownload.Columns> lstDownloading = schema.getDownloadingAll();
		new DBSchemaApplications(context).updateDownloading(lstDownloading);
		//Log.i(this.getClass().getName(), lstDownloading.size() + " app installed!");
		new IntentSender(context).startGroupGuiUpdate();
		
	}
	

}
