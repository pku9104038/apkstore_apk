/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class WebServicePreferences extends MainSharedPref {

	/*
	 * CONSTANTS, PRIVATE
	 */
	private static final String HTTP_PROTOCOL					= "http://";

	public static final int 	INDEX_KEY						= 0;
	public static final int 	INDEX_VALUE						= 1;
	/*
	 * CONSTANTS, PUBLIC
	 */
	
	//release server
	public static final String VAL_DEF_API_CONFIG_SYNC_MAIN		= "http://config.pu-up.com/Config/androidaid3.conf";
	public static final String VAL_DEF_API_CONFIG_SYNC_SECOND	= "http://config.3gstone.net/Config/androidaid3.conf";
	private static final String VAL_DEF_API_ROOT 				= "/ApkStore/api";
	
	/*
	//dev server
	public static final String VAL_DEF_API_CONFIG_SYNC_MAIN		= "http://config.pu-up.com/Config/androidaid3_dev.conf";
	public static final String VAL_DEF_API_CONFIG_SYNC_SECOND	= "http://config.3gstone.net/Config/androidaid3_dev.conf";
	private static final String VAL_DEF_API_ROOT 				= "/ApkStore_dev/api";
	*/
	
	
	//public static final String PREF_KEY_API_CONFIG_SYNC_MAIN	= "api_config_sync_main";
	//public static final String PREF_KEY_API_CONFIG_SYNC_SECOND	= "api_config_sync_second";
	
	public static final String PREF_KEY_API_ROOT 				= "api_root";
	
	public static final String VAL_DEF_API_DOWNLOAD_TESTFILE	= "download.test";
	
	public static final String VAL_DEF_APK_DOWNLOAD_FILE		= "http://www.pu-up.com/Downloads/ApkStore/download/apk/ApkStore.apk";
	
	public static final String PREF_KEY_API_HOST 				= "api_host";
	private static final String VAL_DEF_API_HOST 				= "www.pu-up.com";
	
	public static final String PREF_KEY_API_PORT 				= "api_port";
	private static final String VAL_DEF_API_PORT 				= ":80";
    
	
	
	/*
	 * APIS 
	 */
	public static final String PREF_KEY_API_APPUB_OPEN			= "api_appub_open";
	private static final String VAL_DEF_API_APPUB_OPEN			= "api_apkstore_load.php";
	
	public static final String PREF_KEY_API_GROUPS_SYNC 		= "api_groups_sync";
	private static final String VAL_DEF_API_GROUPS_SYNC			= "api_groups_sync.php";
    
	public static final String PREF_KEY_API_APPS_SYNC 			= "api_apps_sync";
	private static final String VAL_DEF_API_APPS_SYNC			= "api_applications_sync.php";
	
	public static final String PREF_KEY_API_APPS_PUUP_SYNC 		= "api_apps_puup_sync";
	public static final String VAL_DEF_API_APPS_PUUP_SYNC		= "api_applications_puup_sync.php";
	
	public static final String PREF_KEY_API_VERSION_SYNC 		= "api_version_sync";
	private static final String VAL_DEF_API_VERSION_SYNC		= "api_version_sync.php";
	
	public static final String PREF_KEY_API_CATEGORIES_SYNC 	= "api_categories_sync";
	private static final String VAL_DEF_API_CATEGORIES_SYNC		= "api_categories_sync.php";
	
	public static final String PREF_KEY_API_REPORT_DEVICE		= "api_report_device";
	public static final String VAL_DEF_API_REPORT_DEVICE		= "api_report_device.php";
	
	
	public static final String PREF_KEY_API_REPORT_RUNNING	 	= "api_report_running";
	private static final String VAL_DEF_API_REPORT_RUNNING		= "api_report_running.php";
	
	public static final String PREF_KEY_API_REPORT_ONBOARD	 	= "api_report_onboard";
	private static final String VAL_DEF_API_REPORT_ONBOARD		= "api_report_onboard.php";
	
	public static final String PREF_KEY_API_REPORT_ONLINE	 	= "api_report_online";
	private static final String VAL_DEF_API_REPORT_ONLINE		= "api_report_online.php";
	
	
	public static final String PREF_KEY_API_REPORT_CELL_ONLINE	= "api_report_cell_online";
	private static final String VAL_DEF_API_REPORT_CELL_ONLINE	= "api_report_cell_online.php";
	
	public static final String PREF_KEY_API_REPORT_WLAN_ONLINE	= "api_report_wlan_online";
	private static final String VAL_DEF_API_REPORT_WLAN_ONLINE	= "api_report_wlan_online.php";
	
	public static final String PREF_KEY_API_REPORT_DOWNLOAD	 	= "api_report_download";
	private static final String VAL_DEF_API_REPORT_DOWNLOAD		= "api_report_download.php";
	
	public static final String PREF_KEY_API_REPORT_INSTALL	 	= "api_report_install";
	private static final String VAL_DEF_API_REPORT_INSTALL		= "api_report_install.php";
	
	public static final String PREF_KEY_API_REPORT_LAUNCH	 	= "api_report_launch";
	private static final String VAL_DEF_API_REPORT_LAUNCH		= "api_report_launch.php";
	
	
	public static final String PREF_KEY_API_REPORT_APP_ACTIVE	= "api_report_app_active";
	private static final String VAL_DEF_API_REPORT_APP_ACTIVE	= "api_report_app_active.php";
	
	public static final String PREF_KEY_API_REPORT_APP_GROUP	= "api_report_app_group";
	private static final String VAL_DEF_API_REPORT_APP_GROUP	= "api_report_app_group.php";
	
	public static final String PREF_KEY_API_REPORT_WIDGET_GROUP	= "api_report_widget_group";
	private static final String VAL_DEF_API_REPORT_WIDGET_GROUP	= "api_report_widget_group.php";
	
	public static final String PREF_KEY_API_REPORT_TRAFFIC		= "api_report_traffic";
	private static final String VAL_DEF_API_REPORT_TRAFFIC		= "api_report_traffic.php";
	
	public static final String PREF_KEY_API_QUERY_FILE_SIZE		= "api_query_file_size";
	private static final String VAL_DEF_API_QUERY_FILE_SIZE		= "api_query_file_size.php";
	
	public static final String PREF_KEY_API_QUERY_APP_INTRODUCE	= "api_query_app_introduce";
	private static final String VAL_DEF_API_QUERY_APP_INTRODUCE	= "api_query_app_introduce.php";
	
	public static final String PREF_KEY_API_APK_OFFLINE			= "api_apk_offline";
	private static final String VAL_DEF_API_APK_OFFLINE			= "manager_apk_offline.php";
	
	/*
	 * PROPERTIES, PRIVATE
	 */
	//private HashMap<String, String> mapValuePair;
	
	/*
	 * CONSTRUCTOR
	 */
	/**
	 * @param context
	 */
	public WebServicePreferences(Context context) {
		super(context);
		
		/*
		mapValuePair = new HashMap<String,String>();
		ArrayList<String[]> lstApis = this.getApiList();
		Iterator<String[]> iterator = lstApis.iterator();
		while(iterator.hasNext()){
			String[] api = iterator.next();
			mapValuePair.put(api[INDEX_KEY], this.getString(api[INDEX_KEY], api[INDEX_VALUE]));
		}
		/*
		mapValuePair.put(PREF_KEY_API_VERSION_SYNC, this.getApiVersionSync());
		mapValuePair.put(PREF_KEY_API_GROUPS_SYNC, this.getApiGroupsSync());
		mapValuePair.put(PREF_KEY_API_APPS_SYNC, this.getApiAppsSync());
		mapValuePair.put(PREF_KEY_API_APPS_PUUP_SYNC, this.getApiAppsPuupSync());
		mapValuePair.put(PREF_KEY_API_CATEGORIES_SYNC, this.getApiCategoriesSync());
		
		mapValuePair.put(PREF_KEY_API_REPORT_RUNNING, this.getApiReportRunning());
		mapValuePair.put(PREF_KEY_API_REPORT_ONBOARD, this.getApiReportOnboard());
		mapValuePair.put(PREF_KEY_API_REPORT_ONLINE, this.getApiReportOnline());
		mapValuePair.put(PREF_KEY_API_REPORT_CELL_ONLINE, this.getApiReportCellOnline());
		mapValuePair.put(PREF_KEY_API_REPORT_WLAN_ONLINE, this.getApiReportWlanOnline());
		mapValuePair.put(PREF_KEY_API_REPORT_DOWNLOAD, this.getApiReportDownload());
		mapValuePair.put(PREF_KEY_API_REPORT_INSTALL, this.getApiReportInstall());
		mapValuePair.put(PREF_KEY_API_REPORT_LAUNCH, this.getApiReportLaunch());
		mapValuePair.put(PREF_KEY_API_REPORT_APP_ACTIVE, this.getApiReportAppActive());
		mapValuePair.put(PREF_KEY_API_REPORT_APP_GROUP, this.getApiReportAppGroup());
		mapValuePair.put(PREF_KEY_API_REPORT_WIDGET_GROUP, this.getApiReportWidgetGroup());
		
		*/
		
		
	}
	
	public HashMap<String, String> getApiMaps(){
		HashMap<String,String> mapValuePair = new HashMap<String,String>();
		ArrayList<String[]> lstApis = this.getApiList();
		Iterator<String[]> iterator = lstApis.iterator();
		while(iterator.hasNext()){
			String[] api = iterator.next();
			mapValuePair.put(api[INDEX_KEY], this.getString(api[INDEX_KEY], api[INDEX_VALUE]));
		}
		
		return mapValuePair;
	}
	
	public ArrayList<String[]> getApiList(){
		ArrayList<String[]> lstApis = new ArrayList<String[]>();
		
		lstApis.add(new String[]{PREF_KEY_API_APPUB_OPEN,VAL_DEF_API_APPUB_OPEN});
		
		lstApis.add(new String[]{PREF_KEY_API_VERSION_SYNC,VAL_DEF_API_VERSION_SYNC});
		lstApis.add(new String[]{PREF_KEY_API_GROUPS_SYNC,VAL_DEF_API_GROUPS_SYNC});
		lstApis.add(new String[]{PREF_KEY_API_APPS_SYNC,VAL_DEF_API_APPS_SYNC});
		lstApis.add(new String[]{PREF_KEY_API_APPS_PUUP_SYNC,VAL_DEF_API_APPS_PUUP_SYNC});
		lstApis.add(new String[]{PREF_KEY_API_CATEGORIES_SYNC,VAL_DEF_API_CATEGORIES_SYNC});
		
		lstApis.add(new String[]{PREF_KEY_API_REPORT_RUNNING,VAL_DEF_API_REPORT_RUNNING});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_DEVICE,VAL_DEF_API_REPORT_DEVICE});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_ONBOARD,VAL_DEF_API_REPORT_ONBOARD});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_ONLINE,VAL_DEF_API_REPORT_ONLINE});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_CELL_ONLINE,VAL_DEF_API_REPORT_CELL_ONLINE});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_WLAN_ONLINE,VAL_DEF_API_REPORT_WLAN_ONLINE});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_DOWNLOAD,VAL_DEF_API_REPORT_DOWNLOAD});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_INSTALL,VAL_DEF_API_REPORT_INSTALL});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_LAUNCH,VAL_DEF_API_REPORT_LAUNCH});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_APP_ACTIVE,VAL_DEF_API_REPORT_APP_ACTIVE});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_APP_GROUP,VAL_DEF_API_REPORT_APP_GROUP});
		lstApis.add(new String[]{PREF_KEY_API_REPORT_WIDGET_GROUP,VAL_DEF_API_REPORT_WIDGET_GROUP});
		
		lstApis.add(new String[]{PREF_KEY_API_REPORT_TRAFFIC,VAL_DEF_API_REPORT_TRAFFIC});
		lstApis.add(new String[]{PREF_KEY_API_QUERY_FILE_SIZE,VAL_DEF_API_QUERY_FILE_SIZE});
		lstApis.add(new String[]{PREF_KEY_API_QUERY_APP_INTRODUCE,VAL_DEF_API_QUERY_APP_INTRODUCE});
		
		lstApis.add(new String[]{PREF_KEY_API_APK_OFFLINE,VAL_DEF_API_APK_OFFLINE});
		
		return lstApis;
	}
	
	public String getApiUrl(String api_key){
		return this.getApiRoot()
				+ "/"
				+ getApiMaps().get(api_key);
				//+ mapValuePair.get(api_key);
	}
	
	/*
	 * METHODS, GETTERS & SETTERS
	 */
	
	/**
	 * setApiHost
	 * @param strHost
	 */
	public void setApiHost(String strHost){
		putString(PREF_KEY_API_HOST, strHost);
	}
	
	/**
	 * getApiHost
	 * @return String API_HOST
	 */
	public String getApiHost(){
		return getString(PREF_KEY_API_HOST, VAL_DEF_API_HOST);
	}
	
	/**
	 * setApiPort
	 * @param strPort
	 */
	public void setApiPort(String strPort){
		putString(PREF_KEY_API_PORT, strPort);
	}
	
	/**
	 * getApiPort
	 * @return String API_PORT
	 */
	public String getApiPort(){
		return getString(PREF_KEY_API_PORT, VAL_DEF_API_PORT);
	}
	
	/**
	 * setApiRoot
	 * @param strRoot
	 */
	public void setApiRoot(String strRoot){
		putString(PREF_KEY_API_ROOT, strRoot);
	}
	/**
	 * getApiRoot
	 * @return String API_ROOT
	 */
	public String getApiRoot(){
		String string = HTTP_PROTOCOL + 
				this.getApiHost() + 
				this.getApiPort() +
				getString(PREF_KEY_API_ROOT, VAL_DEF_API_ROOT);
		return string;
	}
	
	/**
	 * setApiGroupSync
	 * @param strApi
	 */
	/*
	public void setApiGroupSync(String strApi){
		putString(PREF_KEY_API_GROUPS_SYNC, strApi);
	}
	
	public String getApiGroupSync(){
		String string = this.getApiRoot() +
						getString(PREF_KEY_API_GROUPS_SYNC, VAL_DEF_API_GROUPS_SYNC);
		return string;
	}
*/
	
	
	public String getApiGroupsSync(){
		return this.getString(VAL_DEF_API_GROUPS_SYNC, VAL_DEF_API_GROUPS_SYNC);
	}
	
	public void setApiGroupsSync(String value){
		this.putString(PREF_KEY_API_GROUPS_SYNC, value);
	}
	
	public String getApiAppsSync(){
		return this.getString(VAL_DEF_API_APPS_SYNC, VAL_DEF_API_APPS_SYNC);
	}
	
	public String getAppSyncJson(){
		String name = "";
		name = getApiAppsSync();
		name = name.substring(0, name.lastIndexOf('.')-1);
		name = name+StoragePreferences.JSON_SUFFIX;
		return name;		
	}
	
	public void setApiAppsSync(String value){
		this.putString(PREF_KEY_API_APPS_SYNC, value);
	}
	
	public String getApiAppsPuupSync(){
		return this.getString(VAL_DEF_API_APPS_PUUP_SYNC, VAL_DEF_API_APPS_PUUP_SYNC);
	}
	
	public void setApiAppsPuupSync(String value){
		this.putString(PREF_KEY_API_APPS_PUUP_SYNC, value);
	}
	
	public String getApiVersionSync(){
		return this.getString(VAL_DEF_API_VERSION_SYNC, VAL_DEF_API_VERSION_SYNC);
	}
	
	public void setApiVersionSync(String value){
		this.putString(PREF_KEY_API_VERSION_SYNC, value);
	}
	
	public String getApiCategoriesSync(){
		return this.getString(VAL_DEF_API_CATEGORIES_SYNC, VAL_DEF_API_CATEGORIES_SYNC);
	}
	
	public void setApiCategoriesSync(String value){
		this.putString(PREF_KEY_API_CATEGORIES_SYNC, value);
	}
	
	public String getApiReportDevice(){
		return this.getString(VAL_DEF_API_REPORT_DEVICE, VAL_DEF_API_REPORT_DEVICE);
	}
	
	public void setApiReportDevice(String value){
		this.putString(PREF_KEY_API_REPORT_DEVICE, value);
	}
	
	public String getApiReportRunning(){
		return this.getString(VAL_DEF_API_REPORT_RUNNING, VAL_DEF_API_REPORT_RUNNING);
	}
	
	public void setApiReportRunning(String value){
		this.putString(PREF_KEY_API_REPORT_RUNNING, value);
	}
	

	public String getApiReportOnboard(){
		return this.getString(VAL_DEF_API_REPORT_ONBOARD, VAL_DEF_API_REPORT_ONBOARD);
	}
	
	public void setApiReportOnboard(String value){
		this.putString(PREF_KEY_API_REPORT_ONBOARD, value);
	}
	

	public String getApiReportOnline(){
		return this.getString(VAL_DEF_API_REPORT_ONLINE, VAL_DEF_API_REPORT_ONLINE);
	}
	
	public void setApiReportOnline(String value){
		this.putString(PREF_KEY_API_REPORT_ONLINE, value);
	}
	

	public String getApiReportCellOnline(){
		return this.getString(VAL_DEF_API_REPORT_CELL_ONLINE, VAL_DEF_API_REPORT_CELL_ONLINE);
	}
	
	public void setApiReportCellOnline(String value){
		this.putString(PREF_KEY_API_REPORT_CELL_ONLINE, value);
	}
	

	public String getApiReportWlanOnline(){
		return this.getString(VAL_DEF_API_REPORT_WLAN_ONLINE, VAL_DEF_API_REPORT_WLAN_ONLINE);
	}
	
	public void setApiReportWlanOnline(String value){
		this.putString(PREF_KEY_API_REPORT_WLAN_ONLINE, value);
	}
	

	public String getApiReportDownload(){
		return this.getString(VAL_DEF_API_REPORT_DOWNLOAD, VAL_DEF_API_REPORT_DOWNLOAD);
	}
	
	public void setApiReportDownload(String value){
		this.putString(PREF_KEY_API_REPORT_DOWNLOAD, value);
	}
	

	public String getApiReportInstall(){
		return this.getString(VAL_DEF_API_REPORT_INSTALL, VAL_DEF_API_REPORT_INSTALL);
	}
	
	public void setApiReportInstall(String value){
		this.putString(PREF_KEY_API_REPORT_INSTALL, value);
	}
	

	public String getApiReportLaunch(){
		return this.getString(VAL_DEF_API_REPORT_LAUNCH, VAL_DEF_API_REPORT_LAUNCH);
	}
	
	public void setApiReportLaunch(String value){
		this.putString(PREF_KEY_API_REPORT_LAUNCH, value);
	}
	

	public String getApiReportAppActive(){
		return this.getString(VAL_DEF_API_REPORT_APP_ACTIVE, VAL_DEF_API_REPORT_APP_ACTIVE);
	}
	
	public void setApiReportAppActive(String value){
		this.putString(PREF_KEY_API_REPORT_APP_ACTIVE, value);
	}
	

	public String getApiReportAppGroup(){
		return this.getString(VAL_DEF_API_REPORT_APP_GROUP, VAL_DEF_API_REPORT_APP_GROUP);
	}
	
	public void setApiReportAppGroup(String value){
		this.putString(PREF_KEY_API_REPORT_APP_GROUP, value);
	}
	

	public String getApiReportWidgetGroup(){
		return this.getString(VAL_DEF_API_REPORT_WIDGET_GROUP, VAL_DEF_API_REPORT_WIDGET_GROUP);
	}
	
	public void setApiReportWidgetGroup(String value){
		this.putString(PREF_KEY_API_REPORT_WIDGET_GROUP, value);
	}
	
	public String getApiReportTradffic(){
		return this.getString(VAL_DEF_API_REPORT_TRAFFIC, VAL_DEF_API_REPORT_TRAFFIC);
	}
	
	public void setApiReportTraffic(String value){
		this.putString(PREF_KEY_API_REPORT_TRAFFIC, value);
	}
	
	
	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "webapi_pref";
	}
}
