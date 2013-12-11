/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class RuntimePreferences extends MainSharedPref {
	
	public static final String PREF_KEY_GROUP_CURRENT 			= "group_current";
	public static final int VAL_DEF_GROUPP_CURRENT 				= 0;
	
	public RuntimePreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "runtime_pref";
	}

	public int getCurrentGroup(){
		return getInt(PREF_KEY_GROUP_CURRENT,VAL_DEF_GROUPP_CURRENT);
	}
	
	public void setCurrentGroup(int group_serial){
		putInt(PREF_KEY_GROUP_CURRENT, group_serial);
	}

}
