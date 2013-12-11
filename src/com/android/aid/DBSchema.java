/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public abstract class DBSchema {

	/*
	 * CONSTANTS,PROTECTED
	 */
	public static final int COL_NAME				= 0;
	public static final int COL_TYPE				= 1;
	
	protected static final String COL_TYPE_NULL		= " NULL ";
	protected static final String COL_TYPE_INTEGER	= " INTEGER ";
	protected static final String COL_TYPE_REAL		= " REAL ";
	protected static final String COL_TYPE_TEXT		= " TEXT ";
	protected static final String COL_TYPE_BLOB		= " BLOB ";
	
	protected static final String COL_TYPE_PMKEY	= " PRIMARY KEY ";
	protected static final String COL_TYPE_NN		= " NOT NULL ";
	protected static final String COL_TYPE_AI		= " AUTOINCREMENT ";
	protected static final String COL_TYPE_UNIQUE	= " UNIQUE ";
	
	protected static final String[] COL_ROWID		= {"row_id",
															COL_TYPE_INTEGER 
															+ COL_TYPE_PMKEY
															+ COL_TYPE_AI
															+ COL_TYPE_NN };
	
	protected static final String SLASH				= "/";
	
	
	/*
	 * PROPERTIES, PROTECTED
	 */
	protected Context context;
	protected ContentResolver resolver;
	protected ArrayList<String[]> lstStructs;
	
	/*
	 * CONSTRUCTOR
	 */
	/**
	 * 
	 * @param context
	 */
	public DBSchema(Context context){
		this.context = context;
		this.resolver = context.getContentResolver();
		this.lstStructs = new ArrayList<String[]>();
		makeSchemaStructs();
		
	}
	
	/**
	 * 
	 * @return
	 */
	protected Uri getUri(){
		
		Uri uri = new Uri.Builder().build().parse(DBStructs.getDBUriHeader()
				+ SLASH 
				+ getSchemaName());
		
		return uri;

	}
	
	/**
	 * 
	 * @param limit
	 * @return
	 */
	protected Uri getUri(int limit){
		
		Uri uri = new Uri.Builder().build().parse(DBStructs.getDBUriHeader()
				+ SLASH 
				+ getSchemaName()
				+ "?"+limit);
		
		return uri;

	}
	
	/**
	 * 
	 * @return
	 */
	protected ArrayList<String[]> getSchemaStruct(){
		return this.lstStructs;
	}
	
	/**
	 * 
	 * @return
	 */
	protected String[] getProjection(){
		String[] projection = new String[this.lstStructs.size()];
		
		for(int i=0; i<this.lstStructs.size(); i++){
			projection[i] = this.lstStructs.get(i)[COL_NAME];
		}
		
		return projection;
	}
	
	
	/**
	 * 
	 * @return
	 */
	protected String getCreateSQL(){
		
		String sql = "CREATE TABLE ";
		sql += this.getSchemaName();
		sql += " ( ";
		//sql += COL_SERIAL[COL_NAME] + " " + COL_TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT NOT NULL, ";

		ArrayList<String[]> list = getSchemaStruct();
		Iterator<String[]> iterator = list.iterator();
		//iterator.next();//skip serial
		while(iterator.hasNext()){
			String[] column = iterator.next();
			sql += column[COL_NAME];
			sql += " ";
			sql += column[COL_TYPE];
			if(iterator.hasNext()){
				sql += ", ";
			}
					
		}
		
		sql += " );";
		
		return sql;
	}
	
	/**
	 * 
	 * @param db
	 */
	public void createSchema(SQLiteDatabase db){
		try{
			String sql = getCreateSQL();
			//Log.i(this.getClass().getName(), sql);
			db.execSQL(sql);
		}
		catch(SQLiteException e){
			//Log.e("SQLException", "Cause:"+e.getCause()+",Msg:"+e.getMessage());
			e.printStackTrace();
		}		
	}
	
	/**
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public void updateSchema(SQLiteDatabase db, int oldVersion, int newVersion){
		for(int i = oldVersion + 1; i <= newVersion; i++){
			try{
				String sql = getUpdateSQL(i);
				if(sql != null){
					//Log.i(this.getClass().getName(), sql);
					db.execSQL(sql);
				}
			}
			catch(SQLiteException e){
				Log.e("SQLException", "Cause:"+e.getCause()+",Msg:"+e.getMessage());
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void deleteRow(int id){
		
		String where = COL_ROWID[COL_NAME] +" =" +id;
		String[] selectionArgs = null;
		
		resolver.delete(this.getUri(),where, selectionArgs);

		
	}
	
	protected void updateGroupGui(){
		new IntentSender(context).startGroupGuiUpdate();
	}

	
	/*
	 * METHODS,ABSTRACT
	 */
	protected abstract String getSchemaName();
	
	protected abstract void makeSchemaStructs();
	
	protected abstract String getUpdateSQL(int updateVersion);

}
