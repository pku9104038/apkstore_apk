/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
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
public class PackageHelper {
	private Context context;
	
	public PackageHelper(Context context) {
		super();
		this.context = context;
	}

	public ArrayList<String> getAppsInstalled(){

		PackageManager packageManager =context.getPackageManager();  
		ArrayList<ApplicationInfo> listApp = (ArrayList<ApplicationInfo>) packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES);
		Iterator<ApplicationInfo> iterator = listApp.iterator();
		ArrayList<String> lstApps = new ArrayList<String>();
		while(iterator.hasNext()){
			
			ApplicationInfo appInfo = (ApplicationInfo) iterator.next();
			Intent intent = packageManager.getLaunchIntentForPackage(    
					appInfo.packageName);  
			if(intent!=null){
				lstApps.add(appInfo.packageName);
			}
		}
		return lstApps;
	}

	public boolean isAppsInstalled(String packageName){

		boolean result = false;
		PackageManager packageManager =context.getPackageManager();  
		ArrayList<ApplicationInfo> listApp = (ArrayList<ApplicationInfo>) packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES);
		Iterator<ApplicationInfo> iterator = listApp.iterator();
		while(iterator.hasNext()){
			
			ApplicationInfo appInfo = (ApplicationInfo) iterator.next();
			Log.i(this.getClass().getName(), appInfo.packageName + " vs "+ packageName);
			if(appInfo.packageName.equals(packageName)){  
				result = true;
				break;
			}
		}
		return result;
	}
	
	public PackageInfo getPackageInfo(String packageName){
		try {
			return context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	public int getPackageUid(String packageName){
		
		int uid = 0;
		try {
			uid = context.getPackageManager().getPackageInfo(packageName, 0).applicationInfo.uid;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return uid;
	}
	
	public int getPackageVerCode(String packageName){
		int verCode = 0;
		PackageInfo info = getPackageInfo(packageName);
		if(info != null){
			verCode = info.versionCode;
		}
		return verCode;
	}
	
    public PackageInfo getRunningPackage(){
    	PackageInfo info = null;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); 
		List<RunningTaskInfo> lstActivities = activityManager.getRunningTasks(1); 
		if(lstActivities.size()>0){
			String packageName = lstActivities.get(0).baseActivity.getPackageName();
			info = getPackageInfo(packageName);
		}
		
		return info;
	}
    
    public boolean isRunningMe(){
    	boolean bool = false;
    	if(context.getPackageName().equals(
    			getRunningPackage().packageName)){
    		bool = true;
    	}
    	
    	return bool;
    }
}
