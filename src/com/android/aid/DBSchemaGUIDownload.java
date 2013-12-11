/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DBSchemaGUIDownload extends DBSchemaDownload{
	
	
	
	/*
	 * CONSTANTS, PRIVATE
	 */
	/*
	private static final int VAL_STATE_NULL				= 0;
	private static final int VAL_STATE_DOWNLOADING		= 1;
	

	private static final String[] COL_FILE_FROM			= {"file_from",COL_TYPE_TEXT};// src online
	private static final String[] COL_FILE_TO			= {"file_to",COL_TYPE_TEXT};// target bitmap on device eg. group serial icon
	private static final String[] COL_STATE				= {"state",COL_TYPE_INTEGER};
	*/
	/*
	public class Columns extends DBColumns{
		private String file_from;
		private String file_to;
		private int state;
		
		public Columns(){
		
			setRowId(0);
			file_from = null;
			file_to = null;
			state = VAL_STATE_NULL;
		}
		public String getFileFrom(){
			return this.file_from;
		}
		
		public void setFileFrom(String file_from){
			this.file_from = file_from;
		}
		
		public String getFileTo(){
			return this.file_to;
		}
		
		public void setFileTo(String file_to){
			this.file_to = file_to;
		}
		
		public int getState(){
			return this.state;
		}
		
		public void setState(int state){
			this.state = state;
		}
		
		
	}
	*/
	
	/*
	 * CONSTRUCTOR
	 */
	public DBSchemaGUIDownload(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		/*
		this.lstStructs = new ArrayList<String[]>();
		
		lstStructs.add(COL_ROWID);
		lstStructs.add(COL_FILE_FROM);
		lstStructs.add(COL_FILE_TO);
		lstStructs.add(COL_STATE);
		*/
	}

	/*
	 * (non-Javadoc)
	 * @see com.android.aid.DBSchema#getSchemaName()
	 */
	public String getSchemaName(){
		return "gui_download";
	}

	

	/* (non-Javadoc)
	 * @see com.android.aid.DBSchema#getUpdateSQL(int, int)
	 */
	@Override
	public String getUpdateSQL(int updateVersion) {
		// TODO Auto-generated method stub
		String sql = null;
		
		return sql;
	}

	/*
	 * METHODS, SCHEMA API
	 */
	/*
	public void addDownloadList(String[][] fileList){
		for(int i=0; i < fileList.length; i++){
			addDownloadFile(fileList[i][0], fileList[i][1]);
		}
	}
	private void addDownloadFile(String file_from, String file_to){

		ContentValues values = new ContentValues();
		values.put(COL_FILE_FROM[COL_NAME], file_from);
		values.put(COL_FILE_TO[COL_NAME], file_to);
		values.put(COL_STATE[COL_NAME], VAL_STATE_DOWNLOADING);
			 	
		resolver.insert(this.getUri(), values);
		
	}

	public boolean isDownloading(){
		boolean bool = false;
		
		String[] projection = this.getProjection();
		String selection = COL_STATE[COL_NAME]+"=1";
		String[] selectionArgs = null;
		
		String sortOrder = COL_ROWID[COL_NAME];
		
		Cursor cursor =  resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				bool = true;
			}
			cursor.close();
		}
		
		return bool;
	}
	
	public ArrayList<Columns> getDownloading(){
		ArrayList<Columns> list = new ArrayList<Columns>();
		
		String[] projection = this.getProjection();
		String selection = COL_STATE[COL_NAME]+"=1";
		String[] selectionArgs = null;
		String sortOrder = COL_ROWID[COL_NAME];
		
		Cursor cursor = resolver.query(this.getUri(),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				do{
					Columns row = new Columns();
					row.setRowId(cursor.getInt(cursor.getColumnIndex(COL_ROWID[COL_NAME])));
					row.setFileFrom(cursor.getString(cursor.getColumnIndex(COL_FILE_FROM[COL_NAME])));
					row.setFileTo(cursor.getString(cursor.getColumnIndex(COL_FILE_TO[COL_NAME])));
					row.setState(cursor.getInt(cursor.getColumnIndex(COL_STATE[COL_NAME])));
					
					list.add(row);
					Log.i(this.getClass().getName(), "add row:"+row.getRowId());
					cursor.moveToNext();
				}while(!cursor.isAfterLast());
				
			}
			cursor.close();
		}
		
		return list;
	}
	
	public void deleteDownloaded(String filename){

		String where = COL_FILE_FROM[COL_NAME] +" =?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = filename;
		
		resolver.delete(this.getUri(),where, selectionArgs);

	}
	*/
}
