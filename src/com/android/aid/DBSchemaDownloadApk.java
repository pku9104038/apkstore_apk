/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import com.android.aid.DBSchemaDownload.Columns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DBSchemaDownloadApk extends DBSchemaDownload{
	
	
	
	public DBSchemaDownloadApk(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	
	}

	@Override
	protected String getSchemaName() {
		// TODO Auto-generated method stub
		return "apk_download";
	}

	@Override
	protected String getUpdateSQL(int updateVersion) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addDownloadFile(int serial){
		
		if(!isDownloading(serial)){
	
			ContentValues values = new ContentValues();
			values.put(COL_SERIAL[COL_NAME], serial);
			values.put(COL_FILE_SIZE[COL_NAME], 0);
			values.put(COL_STATE[COL_NAME], VAL_STATE_DOWNLOADING);
			values.put(COL_STAMP[COL_NAME], 0);
			values.put(COL_COUNT[COL_NAME], 0);
				 	
			resolver.insert(this.getUri(), values);
		}
		updateGroupGui();

	}
	
	public void updateDownloadFile(int serial, String file){

		String selection = COL_SERIAL[COL_NAME]+ "=" + serial;
		String[] selectionArgs = null;
		
		ContentValues values = new ContentValues();
		values.put(COL_FILE_FROM[COL_NAME], file);
		values.put(COL_FILE_TO[COL_NAME], file);
			 	
		resolver.update(this.getUri(), values, selection, selectionArgs);
		
	}
	
	public boolean isDownloading(int serial){
		boolean bool = false;
		
		String[] projection = this.getProjection();
		String selection = COL_STATE[COL_NAME] + "=" + VAL_STATE_DOWNLOADING 
							+ " AND " + COL_SERIAL[COL_NAME] + "="+serial;
		String[] selectionArgs = null;
		
		
		String sortOrder = COL_STAMP[COL_NAME] + " ASC";
		
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
	

}
