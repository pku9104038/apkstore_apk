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
public abstract class DBSchemaReport extends DBSchema{


	protected static final int VAL_ACTION_DOWNLOAD_START			= 1;
	protected static final int VAL_ACTION_DOWNLOAD_CANCEL			= 2;
	protected static final int VAL_ACTION_DOWNLOAD_FINISH			= 3;
	protected static final int VAL_ACTION_DOWNLOAD_DROP				= 4;
	
	protected static final int VAL_ACTION_PACKAGE_ADD				= 11;
	protected static final int VAL_ACTION_PACKAGE_REPLACE			= 12;
	protected static final int VAL_ACTION_PACKAGE_REMOVE			= 13;
	protected static final int VAL_ACTION_PACKAGE_LAUNCH			= 14;
	protected static final int VAL_ACTION_PACKAGE_RUNNING			= 15;
	
	protected static final int VAL_ACTION_SELF_ONBOARD				= 21;
	protected static final int VAL_ACTION_SELF_ONLINE				= 22;
	protected static final int VAL_ACTION_SELF_CELL_ONLINE			= 23;
	protected static final int VAL_ACTION_SELF_WLAN_ONLINE			= 24;
	
	protected static final int VAL_ACTION_SELF_APP_ACTIVE			= 31;
	protected static final int VAL_ACTION_SELF_APP_GROUP			= 32;
	protected static final int VAL_ACTION_SELF_WIDGET_GROUP			= 33;
	
	protected static final String COL_NAME_ACTION					= "action";
	protected static final String COL_NAME_STAMP					= "stamp";
	protected static final String COL_NAME_DATE						= "date";
	protected static final String COL_NAME_COUNT					= "count";
	
	protected static final String[] COL_PACKAGE				= 
		{DBSchemaApplications.COL_PACKAGE[COL_NAME],COL_TYPE_TEXT};
	
	protected static final String[] COL_LABEL				= 
		{DBSchemaApplications.COL_LABEL[COL_NAME],COL_TYPE_TEXT};
	
	protected static final String[] COL_VERCODE				= 
		{DBSchemaApplications.COL_VERCODE[COL_NAME],COL_TYPE_INTEGER};
	
	protected static final String[] COL_ACTION				=
		{COL_NAME_ACTION,COL_TYPE_INTEGER};
	
	protected static final String[] COL_STAMP				=
		{COL_NAME_STAMP,COL_TYPE_TEXT};
	
	protected static final String[] COL_DATE				=
		{COL_NAME_DATE,COL_TYPE_TEXT};

	protected static final String[] COL_COUNT				=
		{COL_NAME_COUNT,COL_TYPE_INTEGER};
	
	public class Columns extends DBColumns{
		
		private String packageName;
		private String label;
		private int verCode;
		private int action;
		private String stamp;
		private String date;
		private int count;
		
		public Columns(){
			this.setRowId(0);
			this.packageName = "";
			this.label = "";
			this.verCode = 0;
			this.action = 0;
			this.stamp = "";
			this.date = "";
			this.count = 0;
		}
		
		public String getPackage(){	return this.packageName;	}
		public void setPackage(String packageName){	this.packageName = packageName;	}
		
		public String getLabel(){ return this.label;	}
		public void setLabel(String label){ this.label = label;	}
		
		public int getVerCode(){	return this.verCode;	}
		public void setVerCode(int verCode){	this.verCode = verCode;	}
		
		public int getAction(){	return this.action;	}
		public void setAction(int action){	this.action = action;	}
		
		public String getStamp(){	return this.stamp;	}
		public void setStamp(String stamp){	this.stamp = stamp;	}
		
		public String getDate(){	return this.date;	}
		public void setDate(String date){	this.date = date;	}
		
		public int getCount(){	return this.count;	}
		public void setCount(int count){	this.count = count;	}
	}
	
	
	public DBSchemaReport(Context context) {
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
		lstStructs.add(COL_PACKAGE);
		lstStructs.add(COL_LABEL);
		lstStructs.add(COL_VERCODE);
		lstStructs.add(COL_ACTION);
		lstStructs.add(COL_DATE);
		lstStructs.add(COL_STAMP);
		lstStructs.add(COL_COUNT);
	
	}



	public  void addActionRecord(String packageName, int verCode, int action){
		
			ContentValues values = new ContentValues();
			values.put(COL_PACKAGE[COL_NAME], packageName);
			values.put(COL_VERCODE[COL_NAME], verCode);
			values.put(COL_ACTION[COL_NAME], action);
			values.put(COL_STAMP[COL_NAME], StampHelper.StampToDateTime(System.currentTimeMillis()));
			values.put(COL_DATE[COL_NAME], StampHelper.getToday());
			values.put(COL_COUNT[COL_NAME], 1);
				 	
			resolver.insert(this.getUri(), values);
	}

	public  void addActionRecord(String packageName, String label, int verCode, int action){
		
		ContentValues values = new ContentValues();
		values.put(COL_PACKAGE[COL_NAME], packageName);
		values.put(COL_LABEL[COL_NAME], label);
		values.put(COL_VERCODE[COL_NAME], verCode);
		values.put(COL_ACTION[COL_NAME], action);
		values.put(COL_STAMP[COL_NAME], StampHelper.StampToDateTime(System.currentTimeMillis()));
		values.put(COL_DATE[COL_NAME], StampHelper.getToday());
		values.put(COL_COUNT[COL_NAME], 1);
			 	
		resolver.insert(this.getUri(), values);
}

	public  void addActionRecord(int action){
		
		ContentValues values = new ContentValues();
		values.put(COL_PACKAGE[COL_NAME], "");
		values.put(COL_LABEL[COL_NAME], "");
		values.put(COL_VERCODE[COL_NAME], 0);
		values.put(COL_ACTION[COL_NAME], action);
		values.put(COL_STAMP[COL_NAME], StampHelper.StampToDateTime(System.currentTimeMillis()));
		values.put(COL_DATE[COL_NAME], StampHelper.getToday());
		values.put(COL_COUNT[COL_NAME], 1);
			 	
		resolver.insert(this.getUri(), values);
}

	public  void updateActionRecord(String packageName, int verCode, int action){
		
		Columns row = getActionRecord(packageName, action, StampHelper.getToday());
		if(row != null){
			
			String selection = COL_ROWID[COL_NAME]+ "=" + row.getRowId();
			String[] selectionArgs = null;
			int count = row.getCount() + 1;
			
			ContentValues values = new ContentValues();
			values.put(COL_VERCODE[COL_NAME], verCode);
			values.put(COL_COUNT[COL_NAME], count);
				 	
			resolver.update(this.getUri(), values, selection, selectionArgs);
			
			
		}
		else{
			addActionRecord(packageName, verCode,action);
			
		}
	}

	public  void updateActionRecord(String packageName, String label,int verCode, int action){
		
		Columns row = getActionRecord(packageName, action, StampHelper.getToday());
		if(row != null){
			
			String selection = COL_ROWID[COL_NAME]+ "=" + row.getRowId();
			String[] selectionArgs = null;
			int count = row.getCount() + 1;
			
			ContentValues values = new ContentValues();
			values.put(COL_VERCODE[COL_NAME], verCode);
			values.put(COL_COUNT[COL_NAME], count);
				 	
			resolver.update(this.getUri(), values, selection, selectionArgs);
			
			
		}
		else{
			addActionRecord(packageName, label, verCode,action);
			
		}
	}

	public  void updateActionRecord(int action){
		
		Columns row = getActionRecord(action, StampHelper.getToday());
		if(row != null){
			
			String selection = COL_ROWID[COL_NAME]+ "=" + row.getRowId();
			String[] selectionArgs = null;
			int count = row.getCount() + 1;
			ContentValues values = new ContentValues();
			values.put(COL_COUNT[COL_NAME], count);
			resolver.update(this.getUri(), values, selection, selectionArgs);
			
		}
		else{
			addActionRecord(action);
		}
	}

	
	public  Columns getActionRecord(){
		Columns row = null;
		
		String[] projection = this.getProjection();
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = COL_ROWID[COL_NAME] + " ASC";
		
		Cursor cursor = resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		//Log.i(this.getClass().getName(), "SQL query: "+this.getUri(1));
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				row = getRow(cursor);
			}
			cursor.close();
		}
		
		return row;
		
	}

	
	public Columns getActionRecord(String date){
		Columns row = null;
		
		String[] projection = this.getProjection();
		String selection = COL_DATE[COL_NAME] + "<=" + "?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = date;
		String sortOrder = COL_ROWID[COL_NAME] + " ASC";
		
		Cursor cursor = resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		//Log.i(this.getClass().getName(), "SQL query: "+this.getUri(1));
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				row = getRow(cursor);
			}
			cursor.close();
		}
		
		return row;
		
	}
	
	public Columns getActionRecord(String packageName, int action, String date){
		Columns row = null;
		
		String[] projection = this.getProjection();
		String selection = COL_ACTION[COL_NAME] + "=" + action
				+ " AND "
				+ COL_PACKAGE[COL_NAME] + "=" + "?"
				+ " AND "
				+ COL_DATE[COL_NAME] + "=" + "?";
		
		String[] selectionArgs = new String[2];
		selectionArgs[0] = packageName;
		selectionArgs[1] = date;
		String sortOrder = COL_ROWID[COL_NAME] + " ASC";
		
		//Log.i(this.getClass().getName(), "SQL query: "+this.getUri(1));
		
		Cursor cursor = resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				row = getRow(cursor);
			}
			cursor.close();
		}
		
		return row;
		
	}

	public Columns getActionRecord(int action, String date){
		Columns row = null;
		
		String[] projection = this.getProjection();
		String selection = COL_ACTION[COL_NAME] + "=" + action
				+ " AND "
				+ COL_DATE[COL_NAME] + "=" + "?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = date;
		String sortOrder = COL_ROWID[COL_NAME] + " ASC";
		
		//Log.i(this.getClass().getName(), "SQL query: "+this.getUri(1));
		Cursor cursor = resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				row = getRow(cursor);
			}
			cursor.close();
		}
		
		return row;
		
	}

	private Columns getRow(Cursor cursor){
		Columns row = new Columns();
		row.setRowId(cursor.getInt(cursor.getColumnIndex(COL_ROWID[COL_NAME])));
		row.setPackage(cursor.getString(cursor.getColumnIndex(COL_PACKAGE[COL_NAME])));
		row.setLabel(cursor.getString(cursor.getColumnIndex(COL_LABEL[COL_NAME])));
		row.setVerCode(cursor.getInt(cursor.getColumnIndex(COL_VERCODE[COL_NAME])));
		row.setAction(cursor.getInt(cursor.getColumnIndex(COL_ACTION[COL_NAME])));
		row.setCount(cursor.getInt(cursor.getColumnIndex(COL_COUNT[COL_NAME])));
		row.setStamp(cursor.getString(cursor.getColumnIndex(COL_STAMP[COL_NAME])));
		row.setDate(cursor.getString(cursor.getColumnIndex(COL_DATE[COL_NAME])));
		return row;
	}
}
