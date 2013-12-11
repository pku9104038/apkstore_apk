/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DBStructs {
	
	/*
	 * CONSTANTS, PUBLIC
	 */
	
	public static final String DB_NAME			= "apk_store";
	public static final String DB_AUTHORITY		= "com.android.aid";
	public static final String URI_HEADER 		= "content://"+DB_AUTHORITY;
	
	public static final int DB_VERSION_MIN		= 13;// start from 10 for com.android.aid online update
	
	public static final int DB_VERSION_14		= 14;// add report traffic tables
	public static final int DB_VERSION			= DB_VERSION_14;

	/*
	 * PROPERTIES,PRIVATE
	 */
	private ArrayList<DBSchema> lstSchemas;
	/*
	 * CONSTRUCTOR
	 */
	public DBStructs(Context context) {
		super();
		// TODO Auto-generated constructor stub
		lstSchemas = new ArrayList<DBSchema>();
		
		lstSchemas.add(new DBSchemaGUIDownload(context));
		lstSchemas.add(new DBSchemaApplications(context));
		lstSchemas.add(new DBSchemaIconDownload(context));
		lstSchemas.add(new DBSchemaDownloadApk(context));

		lstSchemas.add(new DBSchemaReportRunning(context));
		lstSchemas.add(new DBSchemaReportOnBoard(context));
		lstSchemas.add(new DBSchemaReportOnline(context));
		lstSchemas.add(new DBSchemaReportCellData(context));
		lstSchemas.add(new DBSchemaReportWLAN(context));
		
		lstSchemas.add(new DBSchemaReportDownload(context));
		lstSchemas.add(new DBSchemaReportInstall(context));
		lstSchemas.add(new DBSchemaReportLaunch(context));
		
		lstSchemas.add(new DBSchemaReportAppActive(context));
		lstSchemas.add(new DBSchemaReportAppGroup(context));
		lstSchemas.add(new DBSchemaReportWidgetGroup(context));
		
		lstSchemas.add(new DBSchemaTraffic(context));

	}
	
	
	
	/*
	 * METHODS,PUBLIC
	 */
	public static String getDBName(){
		return DB_NAME;
	}
	
	public static String getDBAuthority(){
		return DB_AUTHORITY;
	}
	
	public static int getDBVersion(){
		return DB_VERSION;
	}
	
	public static String getDBUriHeader(){
		return URI_HEADER;
	}
	public static int getDBVersionMin(){
		return DB_VERSION_MIN;
	}
	
	/**
	 * onCreate
	 * @param db
	 */
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Iterator<DBSchema> iterator = lstSchemas.iterator();
		while(iterator.hasNext()){
			DBSchema schema = iterator.next();
			schema.createSchema(db);
		}
		
	}
	
	/**
	 * onUpgrade
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		for(int i = oldVersion+1; i <= newVersion; i++){
			Iterator<DBSchema> iterator = lstSchemas.iterator();
			while(iterator.hasNext()){
				DBSchema schema = iterator.next();
				schema.updateSchema(db, oldVersion, newVersion);
			}
			
		}
	}

	public void dropSchemas(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Iterator<DBSchema> iterator = lstSchemas.iterator();
		while(iterator.hasNext()){
			DBSchema schema = iterator.next();
			String sql = "DROP TABLE IF EXISTS " + schema.getSchemaName();
			//Log.i(this.getClass().getName(), sql);
			try{
				db.execSQL(sql);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	
}
