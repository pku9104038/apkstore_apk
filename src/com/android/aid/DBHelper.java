/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	
	/*
	 * PROPERTIES,PRIVATE 
	 */
	private Context context;

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//Log.i(this.getClass().getName(), "OnCreate");
		DBStructs dbStructs = new DBStructs(this.context);
		dbStructs.onCreate(db);
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//Log.i(this.getClass().getName(), "OnUpdate");
		if(oldVersion < DBStructs.DB_VERSION_MIN){
			DBStructs dbStructs = new DBStructs(this.context);
			dbStructs.dropSchemas(db);
			onCreate(db);
			
		}
		else{
			DBStructs dbStructs = new DBStructs(this.context);
			dbStructs.onUpgrade(db, oldVersion, newVersion);
		}
	}

}
