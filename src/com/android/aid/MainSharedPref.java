/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public abstract class MainSharedPref {
	
	/*
	 * CONSTANTS, PUBLIC
	 */
	private static final String PREF_FILE 						= "MainPreferences";
	
	protected static final String VAL_DEF_DEV_INFO_NULL			= "";
	protected static final int VAL_DEF_DEV_INFO_ZERO			= 0;
	protected static final long VAL_DEF_LONG_ZERO				= 0;
	   
	/*
	 * CONSTANTS, PRIVATE
	 */
	
	/*
	 * PROPERTRIES, PRIVATE 
	 */
	protected Context context;
	private SharedPreferences pref;
	private Editor editor;
	/*
	 * PROPERTIES, PUBLIC
	 */
	
	/*
	 * PROPERTIES, PROTECTED
	 */
	
	
	/*
	 * PROPERTIES, BEHAVIAR
	 */
	
	
	/*
	 * CONSTRUCTORS
	 */

	/**
	 * @param context
	 */
	public MainSharedPref(Context context) {
		super();
		this.context = context;
		//this.pref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		this.pref = context.getSharedPreferences(this.getPrefFileName(), Context.MODE_PRIVATE);
		this.editor = pref.edit();
	}
	
	protected abstract String getPrefFileName();
	
	/*
	 * METHODS, PROTECTED
	 */
	
	/**
	 * putString		write string to preferences
	 * @param key
	 * @param value
	 */
	protected void putString(String key, String value)
	{
		try{
			//Log.i("putString",key+":"+value.length()+" @ "+value);
			editor.putString(key, value);
			editor.commit();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * putString		write string to preferences
	 * @param key
	 * @param value
	 */
	protected void clear()
	{
		try{
			editor.clear();
			editor.commit();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * getString 		read string from preferences
	 * @param key
	 * @param def_value
	 * @return String
	 */
	protected String getString(String key, String defValue)
	{
		return pref.getString(key, defValue);
	}

	/**
	 * putInt
	 * @param key
	 * @param value
	 */
	protected void putInt(String key, int value){
		editor.putInt(key, value);
		editor.commit();
	}
	
	/**
	 * getInt
	 * @param key
	 * @param defValue
	 * @return int
	 */
	protected int getInt(String key, int defValue){
		return pref.getInt(key, defValue);
	}
	
	protected void putLong(String key,long value){
		Log.i("putLong",key+":"+value);
		editor.putLong(key, value);
		editor.commit();
	}
	
	protected long getLong(String key, long defValue){
		return pref.getLong(key, defValue);
	}


}
