/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public abstract class DBSchemaDownload extends DBSchema{

	protected static final int VAL_STATE_NULL				= 0;
	protected static final int VAL_STATE_DOWNLOADING		= 1;

	protected static final long VAL_DOWNLOAD_DEBOUNCE		= 600000;//do not retry a file in 10 min
	public static final long VAL_DOWNLOAD_COUNT_MAX			= 10;//max retry 10 times
	
	protected static final String[] COL_SERIAL				= {"serial",COL_TYPE_INTEGER};// app_serial to download, group_serial for icon
	protected static final String[] COL_PACKAGE				= {"package",COL_TYPE_TEXT};// 
	protected static final String[] COL_VERCODE				= {"vercode",COL_TYPE_INTEGER};// 
	protected static final String[] COL_FILE_FROM			= {"file_from",COL_TYPE_TEXT};// src online
	protected static final String[] COL_FILE_TO				= {"file_to",COL_TYPE_TEXT};// target bitmap on device eg. group serial icon
	protected static final String[] COL_FILE_SIZE			= {"size",COL_TYPE_INTEGER};
	protected static final String[] COL_STATE				= {"state",COL_TYPE_INTEGER};
	protected static final String[] COL_STAMP				= {"stamp",COL_TYPE_INTEGER};
	protected static final String[] COL_COUNT				= {"count",COL_TYPE_INTEGER};

	public class Columns extends DBColumns{
		private int serial ;
		private String packageName;
		private int verCode;
		private String file_from;
		private String file_to;
		private long size;
		private int state;
		private long stamp;
		private int count;
		
		public Columns(){

			setRowId(0);
			serial = 0;
			packageName = "";
			file_from = null;
			file_to = null;
			size = 0;
			state = VAL_STATE_NULL;
			stamp = 0;
			count = 0;
		}
		
		public int getSerial(){	return this.serial;	}
		public void setSerial(int serial){ this.serial = serial;	};
		
		public String getPackage(){	return this.packageName;	}
		public void setPackage(String packageName){	this.packageName = packageName;	}
		
		public int getVerCode(){	return this.verCode;	}
		public void setVerCode(int verCode){	this.verCode = verCode;	}
		
		public String getFileFrom(){	return this.file_from;	}
		public void setFileFrom(String file_from){	this.file_from = file_from;	}
		
		public String getFileTo(){	return this.file_to;	}
		public void setFileTo(String file_to){	this.file_to = file_to;	}
		
		public long getSize(){	return this.size;	}
		public void setSize(int size){	this.size = size;	}

		public int getState(){	return this.state;	}
		public void setState(int state){	this.state = state;	}

		public long getStamp(){	return this.stamp;	}
		public void setStamp(int stamp){	this.stamp = stamp;	}

		public int getCount(){	return this.count;	}
		public void setCount(int count){	this.count = count;	}
	}

	public DBSchemaDownload(Context context) {
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
		lstStructs.add(COL_SERIAL);
		lstStructs.add(COL_PACKAGE);
		lstStructs.add(COL_VERCODE);
		lstStructs.add(COL_FILE_FROM);
		lstStructs.add(COL_FILE_TO);
		lstStructs.add(COL_FILE_SIZE);
		lstStructs.add(COL_STATE);
		lstStructs.add(COL_STAMP);
		lstStructs.add(COL_COUNT);
		
	}



	/*
	 * METHODS, SCHEMA API
	 */
	/**
	 * 
	 * @param fileList
	 */
	public void addDownloadList(String[][] fileList){
		for(int i=0; i < fileList.length; i++){
			//Log.i(this.getClass().getName(), "addDownliadFile:"+fileList[i][0]+"/"+fileList[i][1]);
			addDownloadFile(fileList[i][0], fileList[i][1]);
		}
	}

	/**
	 * 
	 * @param file_from
	 * @param file_to
	 */
	public void addDownloadFile(String file_from, String file_to){
		
		if(!isDownloading(file_from)){
	
			ContentValues values = new ContentValues();
			values.put(COL_SERIAL[COL_NAME], 0);
			values.put(COL_PACKAGE[COL_NAME], "");
			values.put(COL_VERCODE[COL_NAME],0);
			values.put(COL_FILE_FROM[COL_NAME], file_from);
			values.put(COL_FILE_TO[COL_NAME], file_to);
			values.put(COL_FILE_SIZE[COL_NAME], 0);
			values.put(COL_STATE[COL_NAME], VAL_STATE_DOWNLOADING);
			values.put(COL_STAMP[COL_NAME], 0);
			values.put(COL_COUNT[COL_NAME], 0);
				 	
			resolver.insert(this.getUri(), values);
		}
	}


	/**
	 * 
	 * @param file_from
	 * @param file_to
	 */
	public void addDownloadFile(String file){
		addDownloadFile(file,file);
	}

	public void addDownloadFile(int serial, String packageName, int verCode, String file){
		if(!isDownloading(file)){
			
			ContentValues values = new ContentValues();
			values.put(COL_SERIAL[COL_NAME], serial);
			values.put(COL_PACKAGE[COL_NAME], packageName);
			values.put(COL_VERCODE[COL_NAME], verCode);
			values.put(COL_FILE_FROM[COL_NAME], file);
			values.put(COL_FILE_TO[COL_NAME], file);
			values.put(COL_FILE_SIZE[COL_NAME], 0);
			values.put(COL_STATE[COL_NAME], VAL_STATE_DOWNLOADING);
			values.put(COL_STAMP[COL_NAME], 0);
			values.put(COL_COUNT[COL_NAME], 0);
				 	
			resolver.insert(this.getUri(), values);
		}
	}

	public void addDownloadFile(int group_serial,  String icon_file){
		if(!isDownloading(icon_file)){
			
			ContentValues values = new ContentValues();
			values.put(COL_SERIAL[COL_NAME], group_serial);
			values.put(COL_PACKAGE[COL_NAME], "");
			values.put(COL_VERCODE[COL_NAME], 0);
			values.put(COL_FILE_FROM[COL_NAME], icon_file);
			values.put(COL_FILE_TO[COL_NAME], icon_file);
			values.put(COL_FILE_SIZE[COL_NAME], 0);
			values.put(COL_STATE[COL_NAME], VAL_STATE_DOWNLOADING);
			values.put(COL_STAMP[COL_NAME], 0);
			values.put(COL_COUNT[COL_NAME], 0);
				 	
			resolver.insert(this.getUri(), values);
		}
	}

	public void updateDownloadFile(String file, int count){
		
		String selection = COL_FILE_FROM[COL_NAME]+ "=?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = file;
		
		ContentValues values = new ContentValues();
		values.put(COL_STAMP[COL_NAME], System.currentTimeMillis());
		values.put(COL_COUNT[COL_NAME], count);
			 	
		resolver.update(this.getUri(), values, selection, selectionArgs);

	}

	public void updateDownloadFileSize(String file, long size){
		
		String selection = COL_FILE_FROM[COL_NAME]+ "=?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = file;
		
		ContentValues values = new ContentValues();
		values.put(COL_FILE_SIZE[COL_NAME], size);
			 	
		resolver.update(this.getUri(), values, selection, selectionArgs);

	}

	public long getDownloadFileSize(String file){
		long size = 0;
		String[] projection = this.getProjection();
		String selection = COL_STATE[COL_NAME] + "=" + VAL_STATE_DOWNLOADING
				+ " AND "
				+ COL_FILE_FROM[COL_NAME] + "=" + "?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = file;
		
		String sortOrder = COL_ROWID[COL_NAME];
		
		Cursor cursor =  resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				size = cursor.getInt(cursor.getColumnIndex(COL_FILE_SIZE[COL_NAME]));
			}
			cursor.close();
		}

		return size;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDownloading(){
		boolean bool = false;
		
		String[] projection = this.getProjection();
		String selection = COL_STATE[COL_NAME] + "=" + VAL_STATE_DOWNLOADING
//				+ " AND "
//				+ COL_STAMP[COL_NAME] + "<" +  (System.currentTimeMillis()-VAL_DOWNLOAD_DEBOUNCE)
				;
		String[] selectionArgs = null;
		
		String sortOrder = COL_ROWID[COL_NAME];
		
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
	
	public boolean isDownloading(String file){
		boolean bool = false;
		
		String[] projection = this.getProjection();
		String selection = COL_STATE[COL_NAME] + "=" + VAL_STATE_DOWNLOADING 
							+ " AND " + COL_FILE_FROM[COL_NAME] + "=?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = file;
		
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
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Columns> getDownloading(){
		
		ArrayList<Columns> list = new ArrayList<Columns>();
		
		String[] projection = this.getProjection();
		String selection = COL_STATE[COL_NAME] + "=" + VAL_STATE_DOWNLOADING
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
		
		return list;
	}
	
	
	public ArrayList<Columns> getDownloadingAll(){
		ArrayList<Columns> list = new ArrayList<Columns>();
		
		String[] projection = this.getProjection();
		String selection = COL_STATE[COL_NAME] + "=" + VAL_STATE_DOWNLOADING;
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
		
		return list;
	}
	
	
	public void deleteDownloaded(String filename){

		String where = COL_FILE_FROM[COL_NAME] +" =?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = filename;
		
		resolver.delete(this.getUri(),where, selectionArgs);
	}

}
