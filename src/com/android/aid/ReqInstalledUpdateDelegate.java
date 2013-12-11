/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqInstalledUpdateDelegate extends ServiceActionDelegate{
	
	private DBSchemaApplications schema;

	public ReqInstalledUpdateDelegate(Context context, String requestAction, DBSchemaApplications schema) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
		this.schema = schema;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ArrayList<String> lstApps = new PackageHelper(context).getAppsInstalled();
		schema.updateInstalled(lstApps);
		//Log.i(this.getClass().getName(), lstApps.size() + " app installed!");
		new IntentSender(context).startDownloadedUpdate();
	}

}
