/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public abstract class FileHelper {
	
	protected Context context;
	
	public FileHelper(Context context) {
		super();
		this.context = context;
	}

	public  ArrayList<String> getFiles(ArrayList<String> lstFiles, String dirPath, String fileExt, boolean iterative)  //����Ŀ¼����չ���Ƿ�������ļ���
	{
	
		try{

			File[] files = new File(dirPath).listFiles();
			for (int i = 0; i < files.length; i++)
			{
				File file = files[i];
				if (file.isFile())
				{
					String ext = file.getPath().substring(file.getPath().length() - fileExt.length()).toLowerCase();
					if (ext.equals(fileExt.toLowerCase()))  
						lstFiles.add(file.getPath());
	  
				}
				else if (file.isDirectory() && file.getPath().indexOf("/.") == -1 && iterative)  
					getFiles(lstFiles, file.getPath(), fileExt, iterative);
			}
	    
		}
		catch(Exception e){
			e.printStackTrace();
		}
	    
	    return lstFiles;
	}
	
	public  ArrayList<String> getFiles(ArrayList<String> lstFiles){
		return getFiles(lstFiles, getPath(), getExtension(), false);
	}
	
	public  ArrayList<String> getFiles(){
		ArrayList<String> lstFiles = new ArrayList<String>();
		return getFiles(lstFiles);
	}
	

	
	protected abstract String getPath();
	
	protected abstract String getExtension();
	

}
