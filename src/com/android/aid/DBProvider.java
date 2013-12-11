/**
 * 
 */
package com.android.aid;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DBProvider extends ContentProvider {
	
	/*
	 * PROPERTIES,PRIVATE
	 */
	private DBHelper dbHelper = null;
	private final  static byte[] _dblock = new byte[0]; 
	
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub

		int rows = 0;
		

		synchronized (_dblock){
		
			try{
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				String args = "";
				if(selectionArgs!=null){
					int l = selectionArgs.length;
					for (int i = 0; i<l; i++){
						args += " "+selectionArgs[i];
					}
				}
				//Log.i(this.getClass().getName(), "sql delete: "+uri.toString() + " where "+selection+" Args "+args);
				rows = db.delete(uri.getLastPathSegment(), selection, selectionArgs);
			}
			catch(SQLiteException e){
				//Log.i(this.getClass().getName(), "SQLiteException cause:"+e.getCause()+",msg"+e.getMessage());
				e.printStackTrace();
			}
			
		}
			
		return rows;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Uri uriResult = null;
		
		synchronized (_dblock){
				
				try{
		
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					//Log.i(this.getClass().getName(), "SQL insert: "+uri.getPath());
					long rowId = db.insert(uri.getLastPathSegment(), null, values);
					if(rowId>0){
						//Log.i(this.getClass().getName(), "SQL insert: "+uri.toString() + "/"+rowId );
						uriResult = uri.buildUpon().appendEncodedPath("/"+rowId).build();
					}
				}
				catch(SQLiteException e){
					//Log.i(this.getClass().getName(), "SQLException cause:"+e.getCause()+",msg"+e.getMessage());
					e.printStackTrace();
				}
			
		}
		
		return uriResult;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new DBHelper(getContext(),
				DBStructs.getDBName(),
				null,
				DBStructs.getDBVersion());
		
		return true;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		Cursor cursor = null;

		try{
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			
			String limit = uri.getQuery();
			String groupBy = null;
			String having = null;

			//Log.i(this.getClass().getName(), "SQL query: "+uri.toString());
			cursor = db.query(uri.getLastPathSegment(), projection, selection, selectionArgs, groupBy, having, sortOrder,limit);
			
			if(cursor!=null){
				int rows = 0, columns = 0;
				rows = cursor.getCount();
				columns = cursor.getColumnCount();
			//	Log.i(this.getClass().getName(), "SQL query result: "+rows+" rows,"+columns+" columns");
			}

		}
		catch(SQLiteException e){
			//Log.i(this.getClass().getName(), "SQLException cause:"+e.getCause()+",msg"+e.getMessage());
			e.printStackTrace();
		}
		
		return cursor;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int rows = 0;
		synchronized (_dblock){
			
			try{
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				//Log.i(this.getClass().getName(), "SQL update: "+uri.toString());
				rows = db.update(uri.getLastPathSegment(), values, selection, selectionArgs);
				//Log.i(this.getClass().getName(), "SQL update: "+ uri.getLastPathSegment() +" rows "+ rows);
			}
			catch(SQLiteException e){
				//Log.i(this.getClass().getName(), "SQLException cause:"+e.getCause()+",msg"+e.getMessage());
				e.printStackTrace();
			}
			
		}
		
		return rows;
	}
	
}
