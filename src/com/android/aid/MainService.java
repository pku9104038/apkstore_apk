/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.NameValuePair;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class MainService extends Service{

	/*
	 * CONSTANTS, PUBLIC
	 */
	public static final String REQUEST_INIT_DATA			= "request_init_data";
	
	public static final String REQUEST_WIDGET_UPDATE		= "request_widget_update";
	public static final String REQUEST_WIDGET_UPDATE_LABEL	= "request_widget_update_label";
	public static final String REQUEST_WIDGET_UPDATE_ICON	= "request_widget_update_icon";
	
	public static final String REQUEST_ADD_ICON				= "request_add_icon";
	
	public static final String REQUEST_CONFIG_SYNC			= "request_config_sync";
	
	public static final String REQUEST_APPUB_OPEN			= "request_appub_open";
	
	public static final String REQUEST_GROUPS_SYNC			= "request_groups_sync";
	
	public static final String REQUEST_CATEGORIES_SYNC		= "request_categories_sync";
	
	public static final String REQUEST_APP_SYNC				= "request_app_sync";
	
	public static final String REQUEST_APP_PUUP_SYNC		= "request_app_puup_sync";
	
	public static final String REQUEST_GUI_DOWNLOAD			= "request_gui_download";
	
	public static final String REQUEST_ICON_DOWNLOAD		= "request_icon_download";
	
	public static final String REQUEST_APK_DOWNLOAD			= "request_apk_download";
	
	public static final String REQUEST_VERSION_SYNC			= "request_version_sync";
	
	public static final String REQUEST_DOWNLOADED_UPDATE	= "request_downloaded_update";
	
	public static final String REQUEST_INSTALLED_UPDATE		= "request_installed_update";
	
	public static final String REQUEST_DOWNLOADING_UPDATE	= "request_downloading_update";
	
	public static final String REQUEST_GUI_UPDATE			= "request_gui_update";
	
	public static final String REQUEST_UPDATE_WIDGET		= "request_update_widget";
	
	public static final String REQUEST_SCREEN_RECEIVER		= "request_screen_receiver";
	
	public static final String REQUEST_RUNNING_TRACKER		= "request_running_tracker";
	
	public static final String REQUEST_REPORT_DEVICE		= "request_report_device";
	 
	public static final String REQUEST_REPORT_RUNNING		= "request_report_running";
	
	public static final String REQUEST_REPORT_ONBOARD		= "request_report_onboard";
	
	public static final String REQUEST_REPORT_ONLINE		= "request_report_online";
	
	public static final String REQUEST_REPORT_CELL_ONLINE	= "request_report_cell_online";
	
	public static final String REQUEST_REPORT_WLAN_ONLINE	= "request_report_wlan_online";
	
	public static final String REQUEST_REPORT_DOWNLOAD		= "request_report_download";
	
	public static final String REQUEST_REPORT_INSTALL		= "request_report_install";
	
	public static final String REQUEST_REPORT_LAUNCH		= "request_report_launch";
	
	public static final String REQUEST_REPORT_APP_ACTIVE	= "request_report_app_active";
	
	public static final String REQUEST_REPORT_APP_GROUP		= "request_report_app_group";
	
	public static final String REQUEST_REPORT_WIDGET_GROUP	= "request_report_widget_group";
	
	public static final String REQUEST_REPORT_TRAFFIC		= "request_report_traffic";
	
	public static final String EXTRA_ICON_FILE				= "extra_icon_file";
	
	/*
	 * CONSTANTS, PRIVATE
	 */
	
	/*
	 * PROPERTRIES, PRIVATE 
	 */
	
	private static ServiceActionDelegate guiUpdateDelegate;
	/*
	 * PROPERTIES, PUBLIC
	 */
	
	/*
	 * PROPERTIES, PROTECTED
	 */
	
	
	/*
	 * PROPERTIES, BEHAVIAR
	 */
	
	
	/*
	 * METHODS, OVERRIDE
	 */
	

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		try{
			if(intent!=null){
			Context context = getApplicationContext();
			
			ArrayList<ServiceActionDelegate> lstDelegates = new ArrayList<ServiceActionDelegate>();
			
			if(intent.getBooleanExtra(REQUEST_INIT_DATA, false)){
				lstDelegates.add(new ReqInitDataDelegate(context,REQUEST_INIT_DATA));
			}
			
			if(intent.getBooleanExtra(REQUEST_ADD_ICON, false)){
				lstDelegates.add(new ReqAddIconDelegate(context,REQUEST_ADD_ICON));
			}
			
			if(intent.getBooleanExtra(REQUEST_SCREEN_RECEIVER, false)){
				lstDelegates.add(new ReqScreenReceiverDelegate(context,
					REQUEST_SCREEN_RECEIVER));
			}
			
			if(intent.getBooleanExtra(REQUEST_REPORT_DEVICE, false) && 
					!new DevicePreferences(context).isDevSN()){
				lstDelegates.add(new ReqReportDeviceDelegate(context,
						REQUEST_REPORT_DEVICE));
			}
			
			
			if(intent.getBooleanExtra(REQUEST_GUI_UPDATE, false)){
				if(guiUpdateDelegate!=null){
					lstDelegates.add(guiUpdateDelegate);
				}
			}
			
			if(intent.getBooleanExtra(REQUEST_WIDGET_UPDATE_ICON, false)){
				lstDelegates.add(new ReqWidgetUpdateIcon(context,REQUEST_WIDGET_UPDATE_ICON));
			}
			
			if(intent.getBooleanExtra(REQUEST_CONFIG_SYNC, false)){
				lstDelegates.add(new ReqConfigSyncDelegate(context,REQUEST_CONFIG_SYNC));
			}
			
			if(intent.getBooleanExtra(REQUEST_APPUB_OPEN, false)){
				lstDelegates.add(new ReqAppubOpenSync(context,REQUEST_APPUB_OPEN));
			}
			
			if(intent.getBooleanExtra(REQUEST_GROUPS_SYNC, false)){
				lstDelegates.add(new ReqGroupsSyncDelegate(context,REQUEST_GROUPS_SYNC));
			}
			
			if(intent.getBooleanExtra(REQUEST_CATEGORIES_SYNC, false)){
				lstDelegates.add(new ReqCategoriesSyncDelegate(context,REQUEST_CATEGORIES_SYNC));
			}
			
			if(intent.getBooleanExtra(REQUEST_RUNNING_TRACKER, false)){
				lstDelegates.add(new ReqRunningTrackerDelegate(context,
					REQUEST_RUNNING_TRACKER));
			}
			
			if(intent.getBooleanExtra(REQUEST_GUI_DOWNLOAD, false)){
				lstDelegates.add(new ReqGUIDownloadDelegate( context,
					REQUEST_GUI_DOWNLOAD,
					new DBSchemaGUIDownload(context)));
			}
			
			if(intent.getBooleanExtra(REQUEST_APP_SYNC, false)){
				lstDelegates.add(new ReqAppSyncDelegate(context,REQUEST_APP_SYNC));
			}
			
			if(intent.getBooleanExtra(REQUEST_APP_PUUP_SYNC, false)){
				lstDelegates.add(new ReqAppPuupSyncDelegate(context,REQUEST_APP_PUUP_SYNC));
			}
			
			if(intent.getBooleanExtra(REQUEST_ICON_DOWNLOAD, false)){
				lstDelegates.add(new ReqIconDownloadDelegate(context,
					REQUEST_ICON_DOWNLOAD,
					new DBSchemaIconDownload(context)));
			}
			
			if(intent.getBooleanExtra(REQUEST_APK_DOWNLOAD, false)){
				lstDelegates.add(new ReqDownloadApkDelegate(context,
					REQUEST_APK_DOWNLOAD,
					new DBSchemaDownloadApk(context)));
			}
			
			if(intent.getBooleanExtra(REQUEST_VERSION_SYNC, false)){
				lstDelegates.add(new ReqVersionSyncDelegate(context,
					REQUEST_VERSION_SYNC));
			}
			
			
			if(intent.getBooleanExtra(REQUEST_DOWNLOADED_UPDATE, false)){
				lstDelegates.add(new ReqDownloadedUpdateDelegate(context,
					REQUEST_DOWNLOADED_UPDATE,
					new DBSchemaApplications(context)));
			}
			
			if(intent.getBooleanExtra(REQUEST_INSTALLED_UPDATE, false)){
				lstDelegates.add(new ReqInstalledUpdateDelegate(context,
					REQUEST_INSTALLED_UPDATE,
					new DBSchemaApplications(context)));
			}
			
			if(intent.getBooleanExtra(REQUEST_DOWNLOADING_UPDATE, false)){
				lstDelegates.add(new ReqDownloadingUpdateDelegate(context,
					REQUEST_DOWNLOADING_UPDATE,
					new DBSchemaDownloadApk(context)));
			}
			
			if(intent.getBooleanExtra(REQUEST_REPORT_RUNNING, false)){
				lstDelegates.add(new ReqReportRunningDelegate(context,
					REQUEST_REPORT_RUNNING,
					new DBSchemaReportRunning(context),
					WebServicePreferences.PREF_KEY_API_REPORT_RUNNING));
			}
			
			if(intent.getBooleanExtra(REQUEST_REPORT_ONBOARD, false)){
				lstDelegates.add(new ReqReportOnBoardDelegate(context,
					REQUEST_REPORT_ONBOARD,
					new DBSchemaReportOnBoard(context),
					WebServicePreferences.PREF_KEY_API_REPORT_ONBOARD));
			}
			
			if(intent.getBooleanExtra(REQUEST_REPORT_ONLINE, false)){
				lstDelegates.add(new ReqReportOnlineDelegate(context,
					REQUEST_REPORT_ONLINE,
					new DBSchemaReportOnline(context),
					WebServicePreferences.PREF_KEY_API_REPORT_ONLINE));
			}
			if(intent.getBooleanExtra(REQUEST_REPORT_CELL_ONLINE, false)){
				lstDelegates.add(new ReqReportCellOnlineDelegate(context,
					REQUEST_REPORT_CELL_ONLINE,
					new DBSchemaReportCellData(context),
					WebServicePreferences.PREF_KEY_API_REPORT_CELL_ONLINE));
			}
			if(intent.getBooleanExtra(REQUEST_REPORT_WLAN_ONLINE, false)){
				lstDelegates.add(new ReqReportWlanOnlineDelegate(context,
					REQUEST_REPORT_WLAN_ONLINE,
					new DBSchemaReportWLAN(context),
					WebServicePreferences.PREF_KEY_API_REPORT_WLAN_ONLINE));
			}
			if(intent.getBooleanExtra(REQUEST_REPORT_DOWNLOAD, false)){
				lstDelegates.add(new ReqReportDownloadDelegate(context,
					REQUEST_REPORT_DOWNLOAD,
					new DBSchemaReportDownload(context),
					WebServicePreferences.PREF_KEY_API_REPORT_DOWNLOAD));
			}
			if(intent.getBooleanExtra(REQUEST_REPORT_INSTALL, false)){
				lstDelegates.add(new ReqReportInstallDelegate(context,
					REQUEST_REPORT_INSTALL,
					new DBSchemaReportInstall(context),
					WebServicePreferences.PREF_KEY_API_REPORT_INSTALL));
			}
			if(intent.getBooleanExtra(REQUEST_REPORT_LAUNCH, false)){
				lstDelegates.add(new ReqReportLaunchDelegate(context,
					REQUEST_REPORT_LAUNCH,
					new DBSchemaReportLaunch(context),
					WebServicePreferences.PREF_KEY_API_REPORT_LAUNCH));
			}
			if(intent.getBooleanExtra(REQUEST_REPORT_APP_ACTIVE, false)){
				lstDelegates.add(new ReqReportAppActiveDelegate(context,
					REQUEST_REPORT_APP_ACTIVE,
					new DBSchemaReportAppActive(context),
					WebServicePreferences.PREF_KEY_API_REPORT_APP_ACTIVE));
			}
			if(intent.getBooleanExtra(REQUEST_REPORT_APP_GROUP, false)){
				lstDelegates.add(new ReqReportAppGroupDelegate(context,
					REQUEST_REPORT_APP_GROUP,
					new DBSchemaReportAppGroup(context),
					WebServicePreferences.PREF_KEY_API_REPORT_APP_GROUP));
			}
			if(intent.getBooleanExtra(REQUEST_REPORT_WIDGET_GROUP, false)){
				lstDelegates.add(new ReqReportWidgetGroupDelegate(context,
					REQUEST_REPORT_WIDGET_GROUP,
					new DBSchemaReportWidgetGroup(context),
					WebServicePreferences.PREF_KEY_API_REPORT_WIDGET_GROUP));
			}
			
			if(intent.getBooleanExtra(REQUEST_REPORT_TRAFFIC, false)){
				lstDelegates.add(new ReqReportTrafficDelegate(context,
					REQUEST_REPORT_TRAFFIC));
			}
			
			//runing 
			Iterator<ServiceActionDelegate> iterator = lstDelegates.iterator();
			while(iterator.hasNext()){
				ServiceActionDelegate delegate = iterator.next();
				if(delegate!=null){
					try{
						Log.i(this.getClass().getName(), delegate.getRequestAction());
						delegate.checkRequestAction(getApplicationContext(),intent);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	


	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		guiUpdateDelegate = null;
	}


	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void registerGuiUpdateDelegate(ServiceActionDelegate delegate){
		guiUpdateDelegate = delegate;
	}
	
	public static void unregisterGuiUpdateDelegate(){
		guiUpdateDelegate = null;
	}

	
	private static final BroadcastReceiver receiverScreenOnOff = new BroadcastReceiver(){  

		@Override  
		public void onReceive(final Context context, final Intent intent) {  
			// Do your action here  
			if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
				new IntentSender(context).stopRunningTracker();
			}
			else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
				new IntentSender(context).startRunningTracker();
								
			}
		}  
	}; 
	
	private static boolean screenReceived	= false;
	
	protected static void registerScreenReceiver(Context context){
		if(!isScreenReceived()){
			final IntentFilter filter = new IntentFilter();  
		    filter.addAction(Intent.ACTION_SCREEN_OFF);  
		    filter.addAction(Intent.ACTION_SCREEN_ON);  
		    context.registerReceiver(receiverScreenOnOff, filter);  	
		    screenReceived = true;
		}
	}
	private static boolean isScreenReceived(){
		return screenReceived;
	}
	
	private static ThreadRunningTracker runningTracker = null;
	
	protected static void startRunningTracker(Context context){
		if(runningTracker == null){
			runningTracker = new ThreadRunningTracker(context, StampHelper.getTrackerStamp());
			runningTracker.startRunning();
		}
		else{
			runningTracker.setSleepTimer(StampHelper.getTrackerStamp());
			runningTracker.startRunning();
		}

	}
	
	protected static void pauseRunnigTracker(){
		if(runningTracker != null){
			runningTracker.setSleepTimer(StampHelper.getTrackerSleepStamp());
			runningTracker.pauseRunning();
		}
	}

}
