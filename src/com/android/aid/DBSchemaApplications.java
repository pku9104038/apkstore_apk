/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DBSchemaApplications extends DBSchema{
	
	/*
	 * CONSTANTS,PRIVATE
	 */
	public static final String[] COL_APP_SERIAL			= {"application_serial",COL_TYPE_INTEGER + COL_TYPE_UNIQUE};
	public static final String[] COL_LABEL				= {"application",COL_TYPE_TEXT};
	public static final String[] COL_PACKAGE			= {"package",COL_TYPE_TEXT};
	public static final String[] COL_APKFILE			= {"apkfile",COL_TYPE_TEXT};
	public static final String[] COL_VERCODE			= {"version_code",COL_TYPE_INTEGER};
	public static final String[] COL_VERNAME			= {"version_name",COL_TYPE_TEXT};
	private static final String[] COL_GROUP_SERIAL		= {"group_serial",COL_TYPE_INTEGER};
	private static final String[] COL_CATEGORY_SERIAL	= {"category_serial",COL_TYPE_INTEGER};
	private static final String[] COL_LOCATION			= {"location",COL_TYPE_INTEGER};
	private static final String[] COL_ONLINE			= {"online",COL_TYPE_INTEGER};
	private static final String[] COL_GOLD_POINT		= {"gold_point",COL_TYPE_INTEGER};
	private static final String[] COL_SHOW_POINT		= {"show_point",COL_TYPE_INTEGER};
	private static final String[] COL_PUUP_POINT		= {"puup_point",COL_TYPE_INTEGER};
	private static final String[] COL_DOWNLOAD_POINT	= {"download_point",COL_TYPE_INTEGER};
	
	
	public static final int VAL_LOCATION_CLOUD			= 1;
	public static final int VAL_LOCATION_DOWNLOADING	= 2;
	public static final int VAL_LOCATION_STORAGE		= 3;
	public static final int VAL_LOCATION_INSTALLED		= 4;
	
	public static final int VAL_PUUP_POINT_NORMAL		= 1;
	
	
	private static final int VAL_APP_ONLINE				= 1;
	private static final int VAL_APP_OFFLINE			= 0;
	
	private static final int CACHE_MAX					= 20;
	
	private static final String ASSETS_APPLICATIONS			= "api_applications_sync.json";
	
	public class Columns extends DBColumns{
		//properties
		private int app_serial = 0;
		private String app_label = "";
		private String app_package = "";
		private String app_apkfile = "";
		private int app_vercode = 0;
		private String app_vername = "";
		private int app_group = 0;
		private int app_category = 0;
		private int app_location = 0;
		private int app_online = 0;
		private int app_gold_point = 0;
		private int app_show_point = 0;
		private int app_puup_point = 0;
		private int app_download_point = 0;
		
		//getters/setters
		public int getAppSerial(){	return this.app_serial; }
		public void setAppSerial(int serial){this.app_serial = serial;	}
		
		public String getAppLabel(){	return this.app_label;	}
		public void setAppLabel(String label){	this.app_label = label;	}
		
		public String getAppPackage(){	return this.app_package;	}
		public void setAppPackage(String packagename){ this.app_package = packagename;	}
		
		public String getAppApkFile(){	return this.app_apkfile;	}
		public void setAppApkFile(String filename){ this.app_apkfile = filename;	}
		
		public int getAppVerCode(){	return this.app_vercode;	}
		public void setAppVerCode(int vercode){	this.app_vercode = vercode;	}
		
		public String getAppVerName(){	return this.app_vername;	}
		public void setAppVerName(String name){ this.app_vername = name;	}
		
		public int getGroupSerial(){	return this.app_group;	}
		public void setGroupSerial(int serial){ this.app_group = serial;	}
		
		public int getCategorySerial(){	return this.app_category;	}
		public void setCategorySerial(int serial){ this.app_category = serial;	}
		
		public int getAppLocation(){	return this.app_location;	}
		public void setAppLocation(int location){	this.app_location = location;	}
		
		public int getAppOnline(){	return this.app_online;	}
		public void setAppOnline(int online){	this.app_online = online;	}
		
		public int getAppGoldPoint(){	return this.app_gold_point;	}
		public void setAppGoldPoint(int point){	this.app_gold_point = point;	}
		
		public int getAppShowPoint(){	return this.app_show_point;	}
		public void setAppShowPoint(int point){	this.app_show_point = point;	}
		
		public int getAppPuupPoint(){	return this.app_puup_point;	}
		public void setAppPuupPoint(int point){	this.app_puup_point = point;	}
		
		public int getDownloadPoint(){	return this.app_download_point;	}
		public void setAppDownloadPoint(int point){	this.app_download_point = point;	}
	}
	/*
	 * CONSTUCTOR
	 */
	public DBSchemaApplications(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	

	/* (non-Javadoc)
	 * @see com.android.aid.DBSchema#makeSchemaStructs()
	 */
	@Override
	protected void makeSchemaStructs() {
		// TODO Auto-generated method stub
		
		lstStructs.add(COL_ROWID);
		lstStructs.add(COL_APP_SERIAL);
		lstStructs.add(COL_LABEL);
		lstStructs.add(COL_PACKAGE);
		lstStructs.add(COL_APKFILE);
		lstStructs.add(COL_VERCODE);
		lstStructs.add(COL_VERNAME);
		lstStructs.add(COL_GROUP_SERIAL);
		lstStructs.add(COL_CATEGORY_SERIAL);
		lstStructs.add(COL_LOCATION);
		lstStructs.add(COL_ONLINE);
		lstStructs.add(COL_GOLD_POINT);
		lstStructs.add(COL_SHOW_POINT);
		lstStructs.add(COL_PUUP_POINT);
		lstStructs.add(COL_DOWNLOAD_POINT);
		
	}



	/*
	 * METHODS, OVERRIDE
	 */
	/*
	 * (non-Javadoc)
	 * @see com.android.aid.DBSchema#getSchemaName()
	 */
	@Override
	protected String getSchemaName() {
		// TODO Auto-generated method stub
		return "applications";
	}
	/*
	 * (non-Javadoc)
	 * @see com.android.aid.DBSchema#getUpdateSQL(int)
	 */
	@Override
	protected String getUpdateSQL(int updateVersion) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/*
	 * METHODS, API
	 */
	
	public boolean isApplicationsAvailable(){
		boolean bool = false;
		
		String[] projection = this.getProjection();
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		
		Cursor cursor =  resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				bool = true;
			}
			cursor.close();
		}
		
		return bool;
	}
	
	public boolean isGroupAvailable(int group_serial){
		boolean bool = false;
		
		String[] projection = this.getProjection();
		String selection = COL_GROUP_SERIAL[COL_NAME]+ "=" + group_serial
//				+ " AND " + COL_LOCATION[COL_NAME] + "<>" + VAL_LOCATION_INSTALLED 
				+ " AND " + COL_ONLINE[COL_NAME] + "=" + VAL_APP_ONLINE ;
		
		String[] selectionArgs = null;
		String sortOrder = COL_PUUP_POINT[COL_NAME] + " DESC";
		try{
			Cursor cursor =  resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
			if(cursor!=null){
				int rows = cursor.getCount();
				int columns = cursor.getColumnCount();
				Log.i(this.getClass().getName(), "isGroupAvailable: rows="+rows+", columns="+columns);
				if(rows>0){
					bool = true;
				}
				cursor.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return bool;
	}
	
	public void initSchema(){
		//String assets = new AssetsHelper(context).getAssetsString(ASSETS_APPLICATIONS);
		String assets = new JsonFileHelper(context).getJsonFileString(
				new StoragePreferences(context).getHttpRespDir()+"/"+ new WebServicePreferences(context).getAppSyncJson());
		
		if(WebServiceApi.isRespSuccess(assets)){
			JSONArray array = null;
			if((array = WebServiceApi.getRespArray(assets))!=null){
				
				this.updateApplicationBasicInfos(array);
			}
		}
		new File(new StoragePreferences(context).getHttpRespDir()+"/"+ new WebServicePreferences(context).getAppSyncJson()).delete();
		
	}
	
	public void updateApplicationBasicInfos(JSONArray array){
		Log.i(this.getClass().getName(), "updateApplicationBasicInfos start!");
		for(int i =0; i<array.length(); i++){
			try{
				
				JSONObject obj = array.getJSONObject(i);
				Columns columns = new Columns();
				columns.setAppSerial(obj.getInt(COL_APP_SERIAL[COL_NAME]));
				columns.setAppLabel(obj.getString(COL_LABEL[COL_NAME]));
				columns.setAppPackage(obj.getString(COL_PACKAGE[COL_NAME]));
				columns.setAppApkFile(obj.getString(COL_APKFILE[COL_NAME]));
				columns.setAppVerCode(obj.getInt(COL_VERCODE[COL_NAME]));
				columns.setAppVerName(obj.getString(COL_VERNAME[COL_NAME]));
				columns.setGroupSerial(obj.getInt(COL_GROUP_SERIAL[COL_NAME]));
				columns.setCategorySerial(obj.getInt(COL_CATEGORY_SERIAL[COL_NAME]));
				columns.setAppOnline(obj.getInt(COL_ONLINE[COL_NAME]));
				columns.setAppPuupPoint(obj.getInt(COL_PUUP_POINT[COL_NAME]));
				columns.setRowId(getApplicationRowId(columns.getAppSerial()));
				
				/*
				Log.i(this.getClass().getName(), "serial:"+obj.getInt(COL_APP_SERIAL[COL_NAME]) 
						+ ",group:"+obj.getInt(COL_GROUP_SERIAL[COL_NAME])
						+ ",online:"+obj.getInt(COL_ONLINE[COL_NAME]));
				*/
				if(columns.getRowId()>0){
					updateApplicationBasicInfos(columns);
				}
				else{
					addApplicationBasicInfos(columns);
				}
				
			}
			catch(JSONException e){
				e.printStackTrace();
			}
		}
		Log.i(this.getClass().getName(), "updateApplicationBasicInfos end!");
		
		clearOfflineApps();
		
		new IntentSender(context).startDownloadedUpdate();
		new IntentSender(context).startInstalledUpdate();
		
	}
	
	
	public void updateApplicationPuups(JSONArray array){
		
		for(int i =0; i<array.length(); i++){
			try{
				
				JSONObject obj = array.getJSONObject(i);
				Columns columns = new Columns();
				columns.setAppSerial(obj.getInt(COL_APP_SERIAL[COL_NAME]));
				columns.setAppPuupPoint(obj.getInt(COL_PUUP_POINT[COL_NAME]));
				columns.setRowId(getApplicationRowId(columns.getAppSerial()));
				//Log.i(this.getClass().getName(),"serial:"+columns.getAppSerial() + "@" + columns.getAppPuupPoint());
				if(columns.getRowId()>0){
					updateApplicationPuup(columns);
				}
				
			}
			catch(JSONException e){
				e.printStackTrace();
			}
		}
		
		updateGroupGui();
	}

	private void addApplicationBasicInfos(Columns columns){
		
		ContentValues values = new ContentValues();
		values.put(COL_APP_SERIAL[COL_NAME], columns.getAppSerial());
		values.put(COL_LABEL[COL_NAME], columns.getAppLabel());
		values.put(COL_PACKAGE[COL_NAME], columns.getAppPackage());
		values.put(COL_APKFILE[COL_NAME], columns.getAppApkFile());
		values.put(COL_VERCODE[COL_NAME], columns.getAppVerCode());
		values.put(COL_VERNAME[COL_NAME], columns.getAppVerName());
		values.put(COL_GROUP_SERIAL[COL_NAME], columns.getGroupSerial());
		values.put(COL_CATEGORY_SERIAL[COL_NAME], columns.getCategorySerial());
		values.put(COL_ONLINE[COL_NAME], columns.getAppOnline());
		values.put(COL_LOCATION[COL_NAME], VAL_LOCATION_CLOUD);
		//values.put(COL_PUUP_POINT[COL_NAME], VAL_PUUP_POINT_NORMAL);
		values.put(COL_PUUP_POINT[COL_NAME], columns.getAppPuupPoint());
			 	
		resolver.insert(this.getUri(), values);
		
		DBSchemaIconDownload schema = new DBSchemaIconDownload(this.context);
		IconHelper helper = new IconHelper(context);
		String iconfile = helper.getIconFile(columns.getAppPackage());
		if(!helper.isIconFileReady(iconfile) && columns.getAppOnline() == 1){
			//schema.addDownloadFile(columns.getGroupSerial(),iconfile);
		}
		
	}
	
	private void updateApplicationBasicInfos(Columns columns){
		
		String selection = COL_ROWID[COL_NAME]+ "=" + columns.getRowId();
		String[] selectionArgs = null;
		
		ContentValues values = new ContentValues();
		values.put(COL_APP_SERIAL[COL_NAME], columns.getAppSerial());
		values.put(COL_LABEL[COL_NAME], columns.getAppLabel());
		values.put(COL_PACKAGE[COL_NAME], columns.getAppPackage());
		values.put(COL_APKFILE[COL_NAME], columns.getAppApkFile());
		values.put(COL_VERCODE[COL_NAME], columns.getAppVerCode());
		values.put(COL_VERNAME[COL_NAME], columns.getAppVerName());
		values.put(COL_GROUP_SERIAL[COL_NAME], columns.getGroupSerial());
		values.put(COL_CATEGORY_SERIAL[COL_NAME], columns.getCategorySerial());
		values.put(COL_ONLINE[COL_NAME], columns.getAppOnline());
		values.put(COL_PUUP_POINT[COL_NAME], columns.getAppPuupPoint());
		
		/*
		Log.i(this.getClass().getName(), "serial:"+columns.getAppSerial() 
				+ ",group:"+columns.getGroupSerial()
				+ ",online:"+columns.getAppOnline());
		*/	 	
		resolver.update(this.getUri(), values, selection, selectionArgs);
		
	}

	private void updateApplicationPuup(Columns columns){
		
		String selection = COL_ROWID[COL_NAME]+ "=" + columns.getRowId();
		String[] selectionArgs = null;
		//Log.i(this.getClass().getName(), "pu-up:"+columns.getAppSerial()+" to "+columns.getAppPuupPoint());
		ContentValues values = new ContentValues();
		values.put(COL_PUUP_POINT[COL_NAME], columns.getAppPuupPoint());
		resolver.update(this.getUri(), values, selection, selectionArgs);
		
	}
	
	
	private int getApplicationRowId(int app_serial){
		int rowid = 0;
		
		String[] projection = this.getProjection();
		String selection = COL_APP_SERIAL[COL_NAME]+ "=" + app_serial;
		String[] selectionArgs = null;
		String sortOrder = COL_ROWID[COL_NAME];
		
		Cursor cursor =  resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				rowid = cursor.getInt(cursor.getColumnIndex(COL_ROWID[COL_NAME]));
			}
			cursor.close();
		}
		
		
		return rowid;
	}
	
	
	protected String getApplicationSerials(){
		String result = "";
		
		String[] projection = this.getProjection();
		
		String selection =  COL_ONLINE[COL_NAME] + "=" + VAL_APP_ONLINE ;
		
		String[] selectionArgs = null;
		String sortOrder = COL_ROWID[COL_NAME];
		
		Cursor cursor =  resolver.query(this.getUri(),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				try{
					JSONArray array = new JSONArray();
					int i = 0;
					cursor.moveToFirst();
					do{
						int serial = cursor.getInt(cursor.getColumnIndex(COL_APP_SERIAL[COL_NAME]));
						array.put(serial);
						result = array.toString();
						cursor.moveToNext();
					}while(!cursor.isAfterLast());
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			cursor.close();
		}
		
		
		return result;
		
	}
	
	private ArrayList<Columns> getApplications(int group_serial, ArrayList<Columns> list){
		
		String[] projection = this.getProjection();
		String selection = COL_GROUP_SERIAL[COL_NAME]+ "=" + group_serial
//				+ " AND " + COL_LOCATION[COL_NAME] + "<>" + VAL_LOCATION_INSTALLED 
				+ " AND " + COL_ONLINE[COL_NAME] + "=" + VAL_APP_ONLINE ;
		
		String[] selectionArgs = null;
		String sortOrder = COL_PUUP_POINT[COL_NAME] + " DESC";
		
		Cursor cursor =  resolver.query(this.getUri(),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				try{
					JSONArray array = new JSONArray();
					ArrayList<String> addedList = new ArrayList<String>();
					
					int i = 0;
					cursor.moveToFirst();
					do{
						Columns row = new Columns();
						row.setRowId(cursor.getInt(cursor.getColumnIndex(COL_ROWID[COL_NAME])));
						row.setAppSerial(cursor.getInt(cursor.getColumnIndex(COL_APP_SERIAL[COL_NAME])));
						row.setAppLabel(cursor.getString(cursor.getColumnIndex(COL_LABEL[COL_NAME])));
						row.setAppPackage(cursor.getString(cursor.getColumnIndex(COL_PACKAGE[COL_NAME])));
						row.setAppApkFile(cursor.getString(cursor.getColumnIndex(COL_APKFILE[COL_NAME])));
						row.setAppVerCode(cursor.getInt(cursor.getColumnIndex(COL_VERCODE[COL_NAME])));
						row.setAppVerName(cursor.getString(cursor.getColumnIndex(COL_VERNAME[COL_NAME])));
						row.setGroupSerial(cursor.getInt(cursor.getColumnIndex(COL_GROUP_SERIAL[COL_NAME])));
						row.setAppLocation(cursor.getInt(cursor.getColumnIndex(COL_LOCATION[COL_NAME])));
						row.setAppOnline(cursor.getInt(cursor.getColumnIndex(COL_ONLINE[COL_NAME])));
						//Log.i(this.getClass().getName(), row.getAppLabel()+" point=" + cursor.getInt(cursor.getColumnIndex(COL_PUUP_POINT[COL_NAME])));
						if(!addedList.contains(row.getAppPackage())){
						
							list.add(row);
							//Log.i(this.getClass().getName(), "get App serial:"+row.getAppSerial() + ",online:"+row.getAppOnline());
							/*
							if(i<CACHE_MAX){
								JSONObject obj = new JSONObject();
								obj.put(COL_ROWID[COL_NAME], row.getRowId());
								obj.put(COL_APP_SERIAL[COL_NAME], row.getAppSerial());
								obj.putOpt(COL_LABEL[COL_NAME], row.getAppLabel());
								obj.putOpt(COL_LABEL[COL_NAME], row.getAppPackage());
								obj.putOpt(COL_LABEL[COL_NAME], row.getAppApkFile());
								obj.put(COL_VERCODE[COL_NAME], row.getAppVerCode());
								obj.put(COL_VERNAME[COL_NAME], row.getAppVerName());
								obj.put(COL_LOCATION[COL_NAME], row.getAppLocation());
								obj.put(COL_ONLINE[COL_NAME], row.getAppOnline());
								
								array.put(obj);
								
							}
							*/
						}
						cursor.moveToNext();
					}while(!cursor.isAfterLast());
					
					//new CachePreferences(context).setCacheGroup(group_serial, array.toString());
					
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			cursor.close();
		}
		
		
		return list;
	}

	public ArrayList<Columns> getApplications(int group_serial, boolean cache){
		ArrayList<Columns> list = new ArrayList<Columns>();
		if(cache){
			list = getCacheApplications(group_serial,list);
		}
		else{
			list = getApplications(group_serial,list);
		}
		return list;
	}
	
	
	
	private ArrayList<Columns> getCacheApplications(int group_serial, ArrayList<Columns> list){

		String cache = new CachePreferences(context).getCacheGroup(group_serial);
		try{
			
			JSONArray array = new JSONArray(cache);
			for(int i=0; i<array.length(); i++){
				
				JSONObject obj = (JSONObject) array.get(i);
				
				Columns row = new Columns();
				row.setRowId(obj.getInt(COL_ROWID[COL_NAME]));
				row.setAppSerial(obj.getInt(COL_APP_SERIAL[COL_NAME]));
				row.setAppLabel(obj.getString(COL_LABEL[COL_NAME]));
				row.setAppPackage(obj.getString(COL_PACKAGE[COL_NAME]));
				row.setAppApkFile(obj.getString(COL_APKFILE[COL_NAME]));
				row.setAppVerCode(obj.getInt(COL_VERCODE[COL_NAME]));
				row.setAppVerName(obj.getString(COL_VERNAME[COL_NAME]));
				
				row.setAppLocation(obj.getInt(COL_LOCATION[COL_NAME]));
				row.setAppOnline(obj.getInt(COL_ONLINE[COL_NAME]));
				
				list.add(row);
		
				
			}
					
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	public void updateAppLocation(int app_serial, int location){
		
		String selection = COL_APP_SERIAL[COL_NAME]+ "=" + app_serial;
		String[] selectionArgs = null;
		
		ContentValues values = new ContentValues();
		values.put(COL_LOCATION[COL_NAME], location);

		resolver.update(this.getUri(), values, selection, selectionArgs);

		updateGroupGui();

	}

	public void updateAppLocation(String packageName, int location){
		
		String selection = COL_PACKAGE[COL_NAME]+ "=" + "?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = packageName;
		
		ContentValues values = new ContentValues();
		values.put(COL_LOCATION[COL_NAME], location);

		resolver.update(this.getUri(), values, selection, selectionArgs);

		updateGroupGui();

	}
	
	private void updateAppDownloaded(String packageName){
		
		String selection = COL_PACKAGE[COL_NAME]+ "=" + "?"
		//		+ " AND "
		//		+ COL_LOCATION[COL_NAME] + "<>" + VAL_LOCATION_INSTALLED 
		;
		String[] selectionArgs = new String[1];
		selectionArgs[0] = packageName;
		
		ContentValues values = new ContentValues();
		values.put(COL_LOCATION[COL_NAME], VAL_LOCATION_STORAGE);

		resolver.update(this.getUri(), values, selection, selectionArgs);

	}

	private void clearDownloaded(){

		String selection = COL_LOCATION[COL_NAME]+ "=" + VAL_LOCATION_STORAGE;
		String[] selectionArgs = null;
		
		ContentValues values = new ContentValues();
		values.put(COL_LOCATION[COL_NAME], VAL_LOCATION_CLOUD);

		resolver.update(this.getUri(), values, selection, selectionArgs);
		
	}
	
	public void updateDownloaded(ArrayList<ApkFileHelper.ApkInfos> lstApkInfos){
		clearDownloaded();
		for(int i=0; i<lstApkInfos.size(); i++){
			updateAppDownloaded(lstApkInfos.get(i).getPackage());
			//Log.i(this.getClass().getName(), "update downloaded "+lstApkInfos.get(i).getLabel());
		}
		updateGroupGui();

	}
	
	private void clearInstalled(){

		String selection = COL_LOCATION[COL_NAME]+ "=" + VAL_LOCATION_INSTALLED;
		String[] selectionArgs = null;
		
		ContentValues values = new ContentValues();
		values.put(COL_LOCATION[COL_NAME], VAL_LOCATION_CLOUD);

		resolver.update(this.getUri(), values, selection, selectionArgs);
		
	}
	
	private void clearOfflineApps(){

		String where = COL_ONLINE[COL_NAME] + "=" + VAL_APP_OFFLINE ;
		String[] selectionArgs = null;
		
		resolver.delete(this.getUri(),where, selectionArgs);
		
	}
	
	private void updateAppInstalled(String packageName){
		
		String selection = COL_PACKAGE[COL_NAME]+ "=" + "?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = packageName;
		
		ContentValues values = new ContentValues();
		values.put(COL_LOCATION[COL_NAME], VAL_LOCATION_INSTALLED);

		resolver.update(this.getUri(), values, selection, selectionArgs);

	}
	public void updateInstalled(ArrayList<String> lstApps){
		clearInstalled();
		for(int i=0; i<lstApps.size(); i++){
			updateAppInstalled(lstApps.get(i));
		}
		//updateGroupGui();
	}
	
	private void clearDownloading(){
		String selection = COL_LOCATION[COL_NAME]+ "=" + VAL_LOCATION_DOWNLOADING;
		String[] selectionArgs = null;
		
		ContentValues values = new ContentValues();
		values.put(COL_LOCATION[COL_NAME], VAL_LOCATION_CLOUD);

		resolver.update(this.getUri(), values, selection, selectionArgs);
		
	}
	
	private void updateAppDownloading(int serial){

		String selection = COL_APP_SERIAL[COL_NAME]+ "=" + serial
		//+ " AND "
		//+ COL_LOCATION[COL_NAME] + "<>" + VAL_LOCATION_INSTALLED 
		//+ " AND "
		//+ COL_LOCATION[COL_NAME] + "<>" + VAL_LOCATION_STORAGE 
		;
		String[] selectionArgs = null;
		
		ContentValues values = new ContentValues();
		values.put(COL_LOCATION[COL_NAME], VAL_LOCATION_DOWNLOADING);

		resolver.update(this.getUri(), values, selection, selectionArgs);
		
	}
	
	public void updateDownloading(ArrayList<DBSchemaDownload.Columns> lstDownloading){
		clearDownloading();
		for(int i=0; i<lstDownloading.size(); i++){
			updateAppDownloading(lstDownloading.get(i).getSerial());
		}
		updateGroupGui();
		
	}
}
