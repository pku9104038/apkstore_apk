/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class InitPreferences extends MainSharedPref{

	private static final String PREF_KEY_DATA_INIT		= "data_init";
	public static final int VAL_DATA_NULL				= 0;
	public static final int VAL_DATA_INITING			= 1;
	public static final int VAL_DATA_INITED				= 2;
	
	private static final int VAL_DEF_DATA_INIT			= VAL_DATA_NULL;
	
	public InitPreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "pref_init";
	}

	public int getDataInit(){
		return getInt(PREF_KEY_DATA_INIT, VAL_DEF_DATA_INIT);
	}
	
	public void setDataInit(int init){
		putInt(PREF_KEY_DATA_INIT, init);
	}
	
	public boolean isDataNull(){
		if (getDataInit()==VAL_DATA_NULL){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isDataInited(){
		if (getDataInit()==VAL_DATA_INITED){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isDataIniting(){
		if (getDataInit()==VAL_DATA_INITING){
			return true;
		}
		else{
			return false;
		}
	}
}
