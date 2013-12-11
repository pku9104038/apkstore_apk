/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class CachePreferences extends MainSharedPref{

	public static final String PREF_KEY_CACHE_GROUP 			= "cache_group";
	public static final String VAL_DEF_CACHE 					= "";
	
	public CachePreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "cache_pref";
	}
	
	public void setCacheGroup(int group_serial, String cache){
		putString(PREF_KEY_CACHE_GROUP+group_serial, cache);
	}
	
	public String getCacheGroup(int group_serial){
		return getString(PREF_KEY_CACHE_GROUP+group_serial, VAL_DEF_CACHE);
	}

}
