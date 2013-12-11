package com.android.aid;

import android.content.Context;
import android.util.Log;

public class AppubPreferences extends MainSharedPref{
	
	private static String PREF_KEY_APPUB_SWITCH		= "appub_switch";
	private static int VAL_APPUB_OPEN				= 1;
	private static int VAL_APPUB_CLOSE				= 0;
	private static int VAL_DEF_APPUB_SWITCH			= VAL_APPUB_OPEN;

	public AppubPreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "pref_appub";
	}
	
	public int getAppubSwitch(){
		return getInt(PREF_KEY_APPUB_SWITCH,VAL_DEF_APPUB_SWITCH);
	}
	
	public void openAppubSwitch(){
		putInt(PREF_KEY_APPUB_SWITCH, VAL_APPUB_OPEN);
	}
	
	public boolean isAppubOpen(){
		if(getAppubSwitch()==VAL_APPUB_OPEN){
			Log.i(this.getClass().getName(), "appub open");
			return true;
		}
		else{
			Log.i(this.getClass().getName(), "appub close");
			return false;
		}
	}

}
