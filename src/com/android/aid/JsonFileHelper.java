/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class JsonFileHelper extends FileHelper {

	public JsonFileHelper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPath() {
		// TODO Auto-generated method stub
		return StoragePreferences.getCacheDir();
	}

	@Override
	protected String getExtension() {
		// TODO Auto-generated method stub
		return ".json";
	}
	public String getJsonFileString(String path){
		String string = "";
		
		try{
			
			FileInputStream fis = new FileInputStream( new File(path));  
			StringBuffer sb = new StringBuffer();
			int c;
			while((c=fis.read())!=-1){
				sb.append((char)c);
			}
			fis.close();
			string = sb.toString();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return string;		
	}
	
		
}
