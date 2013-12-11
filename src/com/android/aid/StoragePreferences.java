/**
 * 
 */
package com.android.aid;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class StoragePreferences extends MainSharedPref{

	private static final String APP_DIR				= "/ApkStore";
	private static final String GUI_PATH			= "gui";
	private static final String APK_PATH			= "apk";
	private static final String ICON_PATH			= "icons";
	private static final String CACHE_PATH			= "cache";
	
	
	public static final String SLASH				= "/";
	public static final String DOWNLOAD_SUFFIX		= ".download";
	public static final String ICON_SUFFIX			= ".png";
	public static final String JSON_SUFFIX			= ".json";
	
	
	public StoragePreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "storage_pref";
	}
	
	
	public static String getDownloadSuffix(){
		return DOWNLOAD_SUFFIX;
	}
	public static String getAppDir(){
		//Log.i("test", Environment.getExternalStorageDirectory().getAbsolutePath());
		//Log.i("test", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
		String dir =  Environment.getExternalStorageDirectory().getAbsolutePath()
				+ APP_DIR;
		if(! new File(dir).exists()){
			new File(dir).mkdir();
		}
		return dir;
		
	}
	
	public static String getGuiPath(){
		return GUI_PATH;
	}
	
	public static String getIconPath(){
		return ICON_PATH;
	}
	
	public static String getApkPath(){
		return APK_PATH;
	}
	
	public String getGuiDir(){

		String dir = (new AssetsHelper(context)).getAssetsDir();
/*		
		String dir = getAppDir()
				+ SLASH
				+ GUI_PATH
				+ SLASH;
				*/
		if(!new File(dir).exists()){
			new File(dir).mkdirs();
		}

		return dir;
	}
	
	public static String getIconDir(){
		
		String dir = getAppDir()
				+ SLASH
				+ ICON_PATH
				+ SLASH;

		if(!new File(dir).exists()){
			new File(dir).mkdirs();
		}
		return dir;
		
		
		
	}


	public static String getApkDir(){
		
		String dir = getAppDir()
				+ SLASH
				+ APK_PATH
				+ SLASH;
		if(!new File(dir).exists()){
			new File(dir).mkdirs();
		}
		return dir;
		
		
		
	}
	
	public static String getCacheDir(){
		
		String dir = getAppDir()
				+ SLASH
				+ CACHE_PATH
				+ SLASH;
		if(!new File(dir).exists()){
			new File(dir).mkdirs();
		}
		return dir;
		
	}
	
	public String getHttpRespDir(){
		
		return context.getFilesDir().getAbsolutePath()+SLASH;

	}
	
	public static boolean isStorageAvailable(){
		boolean bool = false;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			bool = true;
		}
		return bool;
	}
}
