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
public class ReqDownloadedUpdateDelegate extends ServiceActionDelegate{
	
	private DBSchemaApplications schema;
	public ReqDownloadedUpdateDelegate(Context context, String requestAction, DBSchemaApplications schema) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
		this.schema = schema;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ArrayList<ApkFileHelper.ApkInfos> lstApkInfos = new ApkFileHelper(context).getApkBasicInfos();
		//Log.i(this.getClass().getName(), lstApkInfos.size()+" apk downloaded!");
		schema.updateDownloaded(lstApkInfos);
		new IntentSender(context).startDownloadingUpdate();
		new IntentSender(context).startGroupGuiUpdate();

	}

}
