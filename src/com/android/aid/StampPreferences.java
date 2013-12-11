/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class StampPreferences extends MainSharedPref{
	
	private static final int WEEKLY								= 6;
	private static final int DAILY								= 0;
	private static final int SYNCTIMER							= DAILY;

	private static final String PREF_KEY_CONFIG_SYNC	 		= "config_sync";
	private static final int VAL_DEF_CONFIG_SYNC	 			= VAL_DEF_DEV_INFO_ZERO;

	private static final String PREF_KEY_VERSION_SYNC	 		= "version_sync";
	private static final int VAL_DEF_VERSION_SYNC	 			= VAL_DEF_DEV_INFO_ZERO;

	private static final String PREF_KEY_GROUPS_SYNC	 		= "groups_sync";
	private static final int VAL_DEF_GROUPS_SYNC	 			= VAL_DEF_DEV_INFO_ZERO;

	private static final String PREF_KEY_CATEGORIES_SYNC	 	= "categories_sync";
	private static final int VAL_DEF_CATEGORIES_SYNC	 		= VAL_DEF_DEV_INFO_ZERO;

	private static final String PREF_KEY_APPLICATIONS_SYNC	 	= "applications_sync";
	private static final int VAL_DEF_APPLICATIONS_SYNC	 		= VAL_DEF_DEV_INFO_ZERO;

	private static final String PREF_KEY_APPLICATIONS_UPDATE	 = "applications_update";
	private static final int VAL_DEF_APPLICATIONS_UPDATE	 	= VAL_DEF_DEV_INFO_ZERO;

	private static final String PREF_KEY_APPLICATIONS_STAMP	 	= "applications_stamp";
	public static final String VAL_DEF_APPLICATIONS_STAMP	 	= "2013-01-01 00:00:00";

	private static final String PREF_KEY_APPLICATIONS_PUUP_SYNC	= "applications_puup_sync";
	private static final int VAL_DEF_APPLICATIONS_PUUP_SYNC	 	= VAL_DEF_DEV_INFO_ZERO;
	
	private static final String PREF_KEY_APPLICATIONS_PUUP_STAMP= "applications_puup_stamp";
	public static final String VAL_DEF_APPLICATIONS_PUUP_STAMP	= "2013-07-01 00:00:00";

	
	
	private static final String PREF_KEY_DOWNLOAD_SERVER_TEST	= "download_server_test";
	private static final int VAL_DEF_DOWNLOAD_SERVER_TEST	 	= VAL_DEF_DEV_INFO_ZERO;

	public StampPreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private boolean checkStamp(int stamp, int timer){
		boolean bool = false;
		if(StampHelper.StampToDateInt(System.currentTimeMillis()) > stamp + timer 
//				|| (new PackageHelper(context).isRunningMe()
//				    && new DataConnectionHelper(context).isWLANOnline())
				)
		{
			bool = true;
		}
		return bool;
	}
	
	public int getConfigSyncStamp(){
		return getInt(PREF_KEY_CONFIG_SYNC,VAL_DEF_CONFIG_SYNC);
	}
	
	public void setConfigSyncStamp(){
		putInt(PREF_KEY_CONFIG_SYNC, StampHelper.StampToDateInt(System.currentTimeMillis()));
	}
	
	public boolean isConfigSync(){
		boolean bool = false;
		int stamp = getConfigSyncStamp();
		if(checkStamp(stamp,SYNCTIMER)){
			bool = true;
		}
		return bool;
	}

	public int getVersionSyncStamp(){
		return getInt(PREF_KEY_VERSION_SYNC,VAL_DEF_VERSION_SYNC);
	}
	
	public void setVersionSyncStamp(){
		putInt(PREF_KEY_VERSION_SYNC, StampHelper.StampToDateInt(System.currentTimeMillis()));
	}
	
	public boolean isVersionSync(){
		boolean bool = false;
		int stamp = getVersionSyncStamp();
		if(checkStamp(stamp,SYNCTIMER)){
			bool = true;
		}
		return bool;
	}
	
	public int getGroupsSyncStamp(){
		return getInt(PREF_KEY_GROUPS_SYNC,VAL_DEF_GROUPS_SYNC);
	}
	
	public void setGroupSyncStamp(){
		putInt(PREF_KEY_GROUPS_SYNC, StampHelper.StampToDateInt(System.currentTimeMillis()));
	}
	
	public boolean isGroupsSync(){
		boolean bool = false;
		int stamp = getGroupsSyncStamp();
		if(checkStamp(stamp,SYNCTIMER)){
			bool = true;
		}
		
		return bool;
	}
	
	public int getCategoriesSyncStamp(){
		return getInt(PREF_KEY_CATEGORIES_SYNC,VAL_DEF_CATEGORIES_SYNC);
	}
	
	public void setCategoriesSyncStamp(){
		putInt(PREF_KEY_CATEGORIES_SYNC, StampHelper.StampToDateInt(System.currentTimeMillis()));
	}
	
	public boolean isCategoriesSync(){
		boolean bool = false;
		int stamp = getCategoriesSyncStamp();
		if(checkStamp(stamp,SYNCTIMER)){
			bool = true;
		}
		
		return bool;
	}
	
	public int getApplicationsSyncStamp(){
		return getInt(PREF_KEY_APPLICATIONS_SYNC,VAL_DEF_APPLICATIONS_SYNC);
	}
	
	public void setApplicationsSyncStamp(){
		putInt(PREF_KEY_APPLICATIONS_SYNC, StampHelper.StampToDateInt(System.currentTimeMillis()));
	}
	
	public boolean isApplicationsSync(){
		boolean bool = false;
		int stamp = getApplicationsSyncStamp();
		if(checkStamp(stamp,SYNCTIMER)){
			bool = true;
		}
		
		return bool;
	}

	public int getApplicationsUpdateStamp(){
		int stamp = getInt(PREF_KEY_APPLICATIONS_UPDATE,VAL_DEF_APPLICATIONS_UPDATE);
		return stamp;
	}
	
	public void setApplicationsUpdateStamp(){
		putInt(PREF_KEY_APPLICATIONS_UPDATE, StampHelper.StampToDateMinute(System.currentTimeMillis()));
	}
	
	public boolean isApplicationsUpdate(){
		boolean bool = false;
		int stamplast = getApplicationsUpdateStamp();
		int stampnow = StampHelper.StampToDateMinute(System.currentTimeMillis());
		if(stampnow-stamplast > 10){
			bool = true;
		}
		
		return bool;
	}
	
	public String getApplicationsCloudStamp(){
		String stamp = getString(PREF_KEY_APPLICATIONS_STAMP, VAL_DEF_APPLICATIONS_STAMP);
		return stamp;
	}
	
	public void setApplicationsCloudStamp(String stamp){
		putString(PREF_KEY_APPLICATIONS_STAMP, stamp);
	}

	public int getApplicationsPuupSyncStamp(){
		return getInt(PREF_KEY_APPLICATIONS_PUUP_SYNC,VAL_DEF_APPLICATIONS_PUUP_SYNC);
	}
	
	public void setApplicationsPuupSyncStamp(){
		putInt(PREF_KEY_APPLICATIONS_PUUP_SYNC, StampHelper.StampToDateInt(System.currentTimeMillis()));
	}
	
	public boolean isApplicationsPuupSync(){
		boolean bool = false;
		int stamp = getApplicationsPuupSyncStamp();
		if(checkStamp(stamp,SYNCTIMER)){
			bool = true;
		}
		
		return bool;
	}

	public String getApplicationsPuupCloudStamp(){
		String stamp = getString(PREF_KEY_APPLICATIONS_PUUP_STAMP, VAL_DEF_APPLICATIONS_PUUP_STAMP);
		return stamp;
	}
	
	public void setApplicationsPuupCloudStamp(String stamp){
		putString(PREF_KEY_APPLICATIONS_PUUP_STAMP, stamp);
	}

	public int getDownloadServerStamp(){
		return getInt(PREF_KEY_DOWNLOAD_SERVER_TEST,VAL_DEF_DOWNLOAD_SERVER_TEST);
	}
	
	public void setDownloadServerStamp(){
		putInt(PREF_KEY_DOWNLOAD_SERVER_TEST, StampHelper.StampToDateInt(System.currentTimeMillis()));
	}
	
	public boolean isDownloadServerTest(){
		boolean bool = false;
		int stamp = getDownloadServerStamp();
		if(StampHelper.StampToDateInt(System.currentTimeMillis()) > stamp + SYNCTIMER ){
			bool = true;
		}
		
		return bool;
	}
	
	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "stamp_pref";
	}

}
