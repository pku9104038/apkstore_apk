/**
 * 
 */
package com.android.aid;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author wangpeifeng
 *
 */
public class IntentSender {
	
	/*
	 * CONSTANTS
	 */
	public static final String INTENT_ACTION_INIT_DATA				= 
			"com.android.aid.receiver.action.init_date";
	/*
	 * PROPERTIES,PRIVATE
	 */
	private Context context;

	/*
	 * CONSTRUCTOR
	 */
	public IntentSender(Context context) {
		super();
		this.context = context;
	}
	
	/*
	 * METHODS, PUBLIC 
	 */
	
	public void startAllServie(){

		//startSync();
		//startReport();
		//startDownload();
		
		Intent serviceIntent = new Intent(context, MainService.class);
		
		if(new DataConnectionHelper(context).isDataOnline()){
			
			serviceIntent.putExtra(MainService.REQUEST_CONFIG_SYNC, true);
			
			/*
			if(!new AppubPreferences(context).isAppubOpen()){
				serviceIntent.putExtra(MainService.REQUEST_APPUB_OPEN, true);
			}
			*/
			
			serviceIntent.putExtra(MainService.REQUEST_GROUPS_SYNC, true);
			
			
			serviceIntent.putExtra(MainService.REQUEST_CATEGORIES_SYNC, true);
			serviceIntent.putExtra(MainService.REQUEST_APP_SYNC, true);
			serviceIntent.putExtra(MainService.REQUEST_APP_PUUP_SYNC, true);
			serviceIntent.putExtra(MainService.REQUEST_VERSION_SYNC, true);
			
			
			serviceIntent.putExtra(MainService.REQUEST_REPORT_DEVICE, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_RUNNING, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_ONBOARD, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_ONLINE, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_CELL_ONLINE, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_WLAN_ONLINE, true);
			
			serviceIntent.putExtra(MainService.REQUEST_REPORT_DOWNLOAD, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_INSTALL, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_LAUNCH, true);
			
			serviceIntent.putExtra(MainService.REQUEST_REPORT_APP_ACTIVE, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_APP_GROUP, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_WIDGET_GROUP, true);
			
			serviceIntent.putExtra(MainService.REQUEST_REPORT_TRAFFIC, true);
			
			/*
			if(new ConfigPreferences(context).isDownloadConnection()){
				serviceIntent.putExtra(MainService.REQUEST_GUI_DOWNLOAD, true);
				if(StoragePreferences.isStorageAvailable()){
					serviceIntent.putExtra(MainService.REQUEST_ICON_DOWNLOAD, true);
					serviceIntent.putExtra(MainService.REQUEST_APK_DOWNLOAD, true);
					
				}
			}
			*/
			this.startDownload();
			
		}
		
		serviceIntent.putExtra(MainService.REQUEST_INSTALLED_UPDATE, true);
		
		serviceIntent.putExtra(MainService.REQUEST_SCREEN_RECEIVER, true);
		
		context.startService(serviceIntent);
		
		startRunningTracker();
		//broadcastGUIUpdate();
		
		
	}
	
	public void startReport(){

		if(new DataConnectionHelper(context).isDataOnline()){
			Intent serviceIntent = new Intent(context, MainService.class);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_DEVICE, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_RUNNING, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_ONBOARD, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_ONLINE, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_CELL_ONLINE, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_WLAN_ONLINE, true);
			
			serviceIntent.putExtra(MainService.REQUEST_REPORT_DOWNLOAD, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_INSTALL, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_LAUNCH, true);
			
			serviceIntent.putExtra(MainService.REQUEST_REPORT_APP_ACTIVE, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_APP_GROUP, true);
			serviceIntent.putExtra(MainService.REQUEST_REPORT_WIDGET_GROUP, true);
			
			serviceIntent.putExtra(MainService.REQUEST_REPORT_TRAFFIC, true);
			
			this.context.startService(serviceIntent);
		}
		
	}
	
	public void startDownload(){
/*
		if(new ConfigPreferences(context).isDownloadConnection()){
			Intent serviceIntent = new Intent(context, MainService.class);
			serviceIntent.putExtra(MainService.REQUEST_GUI_DOWNLOAD, true);
			if(StoragePreferences.isStorageAvailable()){
				serviceIntent.putExtra(MainService.REQUEST_ICON_DOWNLOAD, true);
		
				serviceIntent.putExtra(MainService.REQUEST_APK_DOWNLOAD, true);
			}
			this.context.startService(serviceIntent);
		}
*/	

		if(new ConfigPreferences(context).isDownloadConnection()){
			Intent serviceIntent = new Intent(context, DownloadService.class);
			serviceIntent.putExtra(MainService.REQUEST_GUI_DOWNLOAD, true);
			if(StoragePreferences.isStorageAvailable()){
				serviceIntent.putExtra(MainService.REQUEST_ICON_DOWNLOAD, true);
		
				serviceIntent.putExtra(MainService.REQUEST_APK_DOWNLOAD, true);
			}
			this.context.startService(serviceIntent);
		}
		
	}
	
	public void startGUIDownload(){
		if(new ConfigPreferences(context).isDownloadConnection()){
			Intent serviceIntent = new Intent(this.context, DownloadService.class);
			serviceIntent.putExtra(MainService.REQUEST_GUI_DOWNLOAD, true);
			this.context.startService(serviceIntent);
		}
	}
	
	public void startIconDownload(){
		if(new ConfigPreferences(context).isDownloadConnection()
			&& StoragePreferences.isStorageAvailable()
			&& ThreadDownloadIcon.isThreadListEmpty()
			){
			Intent serviceIntent = new Intent(this.context, DownloadService.class);
			serviceIntent.putExtra(MainService.REQUEST_ICON_DOWNLOAD, true);
			this.context.startService(serviceIntent);
		}
	}

	public void startApkDownload(){
		if(new ConfigPreferences(context).isDownloadConnection()
				&& StoragePreferences.isStorageAvailable()){
			Intent serviceIntent = new Intent(this.context, DownloadService.class);
			serviceIntent.putExtra(MainService.REQUEST_APK_DOWNLOAD, true);
			this.context.startService(serviceIntent);
		}
	}
	
	public void startSync(){
		if(new DataConnectionHelper(context).isDataOnline()){
			Intent serviceIntent = new Intent(context, MainService.class);
			serviceIntent.putExtra(MainService.REQUEST_CONFIG_SYNC, true);
			/*
			if(!new AppubPreferences(context).isAppubOpen()){
				serviceIntent.putExtra(MainService.REQUEST_APPUB_OPEN, true);
			}
			*/
			serviceIntent.putExtra(MainService.REQUEST_GROUPS_SYNC, true);
			
			serviceIntent.putExtra(MainService.REQUEST_CATEGORIES_SYNC, true);
			
			serviceIntent.putExtra(MainService.REQUEST_APP_SYNC, true);
			
			serviceIntent.putExtra(MainService.REQUEST_APP_PUUP_SYNC, true);
			
			serviceIntent.putExtra(MainService.REQUEST_VERSION_SYNC, true);
		
			this.context.startService(serviceIntent);
		}
		
	}
	public void registerScreenReceiver(){
		Intent serviceIntent = new Intent(context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_SCREEN_RECEIVER, true);
		this.context.startService(serviceIntent);
	}
	
	public void startRunningTracker(){
		Intent serviceIntent = new Intent(context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_RUNNING_TRACKER, true);
		serviceIntent.putExtra(ReqRunningTrackerDelegate.EXTRA_RUNNING_TRACKER, true);
		this.context.startService(serviceIntent);
		
	}

	public void stopRunningTracker(){
		Intent serviceIntent = new Intent(context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_RUNNING_TRACKER, true);
		serviceIntent.putExtra(ReqRunningTrackerDelegate.EXTRA_RUNNING_TRACKER, false);
		this.context.startService(serviceIntent);
		
	}
	
	
	public void startApkInstall(String filepath){
		
		Intent startIntent = new Intent();
		startIntent.setAction(android.content.Intent.ACTION_VIEW);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startIntent.setDataAndType(Uri.fromFile(new File(filepath)),    
	                "application/vnd.android.package-archive");    
	    context.startActivity(startIntent); 
		
	}

	public void startInitData(){
		Intent serviceIntent = new Intent(this.context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_INIT_DATA, true);
		this.context.startService(serviceIntent);
	}
	
	public void addIconDownload(String iconfile){

		Intent serviceIntent = new Intent(this.context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_ADD_ICON, true);
		serviceIntent.putExtra(MainService.EXTRA_ICON_FILE, iconfile);
		this.context.startService(serviceIntent);
		
	}

	public void startGroupGuiUpdate(){
		Intent serviceIntent = new Intent(this.context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_GUI_UPDATE, true);
		this.context.startService(serviceIntent);
	}
	/*
	public void startWidgetUpdate(){
		Intent serviceIntent = new Intent(this.context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_WIDGET_UPDATE, true);
		this.context.startService(serviceIntent);
	}
	*/
	public void startUpdateWidgetLabel(){
		Intent serviceIntent = new Intent(this.context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_WIDGET_UPDATE_LABEL, true);
		this.context.startService(serviceIntent);
	}
	
	public void startUpdateWidgetIcon(){
		Intent serviceIntent = new Intent(this.context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_WIDGET_UPDATE_ICON, true);
		this.context.startService(serviceIntent);
		
	}

	public void startInstalledUpdate(){
		Intent serviceIntent = new Intent(this.context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_INSTALLED_UPDATE, true);
		this.context.startService(serviceIntent);
	}

	public void startDownloadedUpdate(){
		Intent serviceIntent = new Intent(this.context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_DOWNLOADED_UPDATE, true);
		this.context.startService(serviceIntent);
	}

	public void startDownloadingUpdate(){
		Intent serviceIntent = new Intent(this.context, MainService.class);
		serviceIntent.putExtra(MainService.REQUEST_DOWNLOADING_UPDATE, true);
		this.context.startService(serviceIntent);
	}

	public void notifyCurrentGroup(int group_serial){
		Intent serviceIntent = new Intent(this.context, DownloadService.class);
		serviceIntent.putExtra(DownloadService.REQUEST_SWITCH_GROUP, true);
		serviceIntent.putExtra(DownloadService.EXTRA_CURRENT_GROUP, group_serial);
		this.context.startService(serviceIntent);
		
	}
	public void unzipIconFiles(){
		Intent serviceIntent = new Intent(this.context, DownloadService.class);
		serviceIntent.putExtra(DownloadService.REQUEST_UNZIP_ICONS, true);
		this.context.startService(serviceIntent);
		
	}

	/*
	public void broadcastGUIUpdate(){

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(INTENT_ACTION_UPDATE_WEDGIT);
		this.context.sendBroadcast(broadcastIntent);
		
	}
	*/
	public void broadcastUpdateWidgetLabel(){

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(MainWidget.WIDGET_INTENT_ACTION_UPDATE_LABEL);
		this.context.sendBroadcast(broadcastIntent);
		
	}
	public void broadcastUpdateWidgetIcon(){

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(MainWidget.WIDGET_INTENT_ACTION_UPDATE_ICON);
		this.context.sendBroadcast(broadcastIntent);
		
	}

	/*
	public void broadcastInitData(){

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(INTENT_ACTION_INIT_DATA);
		this.context.sendBroadcast(broadcastIntent);
		
	}
*/
}
