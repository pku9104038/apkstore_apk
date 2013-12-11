/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import com.android.aid.DBSchemaDownload.Columns;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DBSchemaIconDownload extends DBSchemaDownload{
	
	

	public DBSchemaIconDownload(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getSchemaName() {
		// TODO Auto-generated method stub
		return "icon_download";
	}

	@Override
	protected String getUpdateSQL(int updateVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.android.aid.DBSchemaDownload#getDownloading()
	 */
	@Override
	public ArrayList<Columns> getDownloading() {
		// TODO Auto-generated method stub
		
		RuntimePreferences runtimePref = new RuntimePreferences(context);
		int group_serial = runtimePref.getCurrentGroup();
		group_serial = DownloadService.getCurrentGroup();
		//Log.i("currentgroup", "get current_group:"+group_serial);
		
		ArrayList<Columns> list = new ArrayList<Columns>();
		
		String[] projection = this.getProjection();
		String selection = COL_STATE[COL_NAME] + "=" + VAL_STATE_DOWNLOADING
				+ " AND "
				+ COL_SERIAL[COL_NAME] + "=" +  group_serial;
//				+ " AND "
//				+ COL_STAMP[COL_NAME] + "<" +  (System.currentTimeMillis()-VAL_DOWNLOAD_DEBOUNCE)
				;
		String[] selectionArgs = null;
		String sortOrder = COL_STAMP[COL_NAME] + " ASC";
		
		Cursor cursor = resolver.query(this.getUri(),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				do{
					Columns row = new Columns();
					row.setSerial(cursor.getInt(cursor.getColumnIndex(COL_SERIAL[COL_NAME])));
					row.setPackage(cursor.getString(cursor.getColumnIndex(COL_PACKAGE[COL_NAME])));
					row.setVerCode(cursor.getInt(cursor.getColumnIndex(COL_VERCODE[COL_NAME])));
					row.setRowId(cursor.getInt(cursor.getColumnIndex(COL_ROWID[COL_NAME])));
					row.setFileFrom(cursor.getString(cursor.getColumnIndex(COL_FILE_FROM[COL_NAME])));
					row.setFileTo(cursor.getString(cursor.getColumnIndex(COL_FILE_TO[COL_NAME])));
					row.setState(cursor.getInt(cursor.getColumnIndex(COL_STATE[COL_NAME])));
					row.setCount(cursor.getInt(cursor.getColumnIndex(COL_COUNT[COL_NAME])));
					row.setSize(cursor.getInt(cursor.getColumnIndex(COL_FILE_SIZE[COL_NAME])));
					
					list.add(row);
					//Log.i(this.getClass().getName(), "add row:"+row.getRowId());
					cursor.moveToNext();
				}while(!cursor.isAfterLast());
				
			}
			cursor.close();
		}
		
		if(list.size()>0){
			//Log.i("currentgroup", "group_"+group_serial+" new icons ="+list.size());
			return list;
		}
		else{
			//Log.i("currentgroup", "group_"+group_serial+" new icons = 0");
			return super.getDownloading();
		}
	}
	
	

}
