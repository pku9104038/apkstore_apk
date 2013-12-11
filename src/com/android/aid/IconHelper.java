/**
 * 
 */
package com.android.aid;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class IconHelper {
	
	private static final String SLASH 					= "/";
	
	private Context context;

	public IconHelper(Context context) {
		super();
		this.context = context;
	}
	
	public String getIconDir(){
		return StoragePreferences.getIconDir();
	}
	
	public String getIconFile(String packageName){
		return packageName + StoragePreferences.ICON_SUFFIX;
	}
	
	public boolean isIconReady(String packageName){
		String filepath = getIconDir()
				+ SLASH 
				+ getIconFile(packageName);
		return new File(filepath).exists();
		
	}
	
	public boolean isIconFileReady(String file){
		String filepath = getIconDir()
		//		+ SLASH 
				+ file;
		//Log.i(this.getClass().getName(), filepath+":"+new File(filepath).exists());
		return new File(filepath).exists();
		
	}
	
	public boolean removeIcon(String packageName){
		return new File(getIconDir()
				+ SLASH 
				+ getIconFile(packageName)).delete();
		
	}
	public Bitmap getIconBitmap(String packageName){
		Bitmap bitmap = null;
		if(this.isIconReady(packageName)){
			bitmap = BitmapFactory.decodeFile(getIconDir()
					+ SLASH 
					+ getIconFile(packageName));
		
			if(bitmap==null){
				removeIcon(packageName);
			}
		}
		if(bitmap == null){
/*
			DBSchemaIconDownload schema = new DBSchemaIconDownload(this.context);
			schema.addDownloadFile(getIconFile(packageName));

			IntentSender sender = new IntentSender(context);
			sender.startIconDownload();
*/			
			//bitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.icon);
		}
		return bitmap;
	}


}
