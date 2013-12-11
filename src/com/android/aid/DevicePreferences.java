/**
 * 
 */
package com.android.aid;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * @author wangpeifeng
 *
 */
public class DevicePreferences extends MainSharedPref {
	/*
	 * CONSTANTS, PRIVATE
	 */
	 
	public static final String PREF_KEY_DEV_SN	 					= "dev_sn";
	public static final int VAL_DEF_DEV_SN	 						= VAL_DEF_DEV_INFO_ZERO;
	
	public static final String PREF_KEY_DEV_IMEI 					= "dev_imei";
	private static final String VAL_DEF_DEV_IMEI 					= VAL_DEF_DEV_INFO_NULL;
	
	public static final String PREF_KEY_DEV_BRAND 					= "dev_brand";
	public static final String PREF_KEY_DEV_MODEL 					= "dev_model";
	public static final String PREF_KEY_DEV_SDK 					= "dev_sdk";
	
	public  static final String PREF_KEY_APP_VERCODE				= "app_vercode";
	
	public static final String PREF_KEY_APP_PACKAGE					= "app_package";
	
	public static final String PREF_KEY_APP_NAME					= "app_name";
	
	/*
	 * CONSTANTS, PRIVATE
	 */
	
	/*
	 * PROPERTIES, PRIVATE
	 */
	
	/*
	 * CONSTRUCTOR
	 */
	/**
	 * @param context
	 */
	public DevicePreferences(Context context) {
		super(context);
	}
	
	/*
	 * METHODS, GETTERS & SETTERS
	 */
	
	
	public String getAppName(){
		return context.getResources().getString(R.string.app_name);
	}

	
	/**
	 * setDevSN
	 * @param devSN
	 */
	public void setDevSN(int devSN){
		putInt(PREF_KEY_DEV_SN,devSN);
	}
	
	public int getDevSN(){
		return getInt(PREF_KEY_DEV_SN, VAL_DEF_DEV_SN);
	}
	
	public boolean isDevSN(){
		boolean bool = false;
		if(getDevSN()!=VAL_DEF_DEV_SN){
			bool = true;
		}
		return bool;
	}

	public void setDevIMEI(String imei){
		putString(PREF_KEY_DEV_IMEI,imei);
	}
	
	public String getDevIMEI(){
		String imei = getString(PREF_KEY_DEV_IMEI, VAL_DEF_DEV_IMEI);
		if(VAL_DEF_DEV_IMEI.equals(imei)){
			TelephonyManager mgrTelephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			imei =  mgrTelephony.getDeviceId();
			if(imei!=null){
				setDevIMEI(imei);
			}
		}
		
		return getString(PREF_KEY_DEV_IMEI, VAL_DEF_DEV_IMEI);
	}
	
	public String getDevBrand(){
		return Build.BRAND;	
	}
	
	
	public String getDevModel(){
		return Build.MODEL;
	}
	
	
	public int getDevSDK(){
		return Build.VERSION.SDK_INT;
	}
	
	public String getPackage(){
		return this.context.getPackageName();
	}
	
	public int getVerCode(){
		return new PackageHelper(context).getPackageVerCode(getPackage());
	}
	
	
	public int getScreenWidth(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return  dm.widthPixels;
        
        
	}
	public int getScreenHeight(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return  dm.heightPixels;
        
        
	}
	public int getXDpi(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return (int) dm.xdpi;
        		
	}
	
	public int getYDpi(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return (int) dm.ydpi;
    }
	
	public int getDensityDpi(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return (int) dm.densityDpi;
        		
	}
	
	public float getDensity(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.density;
    }
	
	public String getScreenInfo(){
		String str = "";
		str += "H:"+getScreenHeight();
		str += "W:"+getScreenWidth();
		str += "Xdpi:"+getXDpi();
		str += "Ydpi:"+getYDpi();
		str += "DDpi:"+getDensityDpi();
		str += "D:"+getDensity();
		return str;
				
	}
	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "device_pref";
	}
	
	
}
