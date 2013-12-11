/**
 * 
 */
package com.android.aid;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class AssetsHelper  {
	
	/*
	 * CONSTANTS,PRIVATE
	 */
	private static final String SLASH					= "/";
	public static final String ASSETS_STRING_NULL		= "";
	/*
	 * PROPERTIES, PRIVATE 
	 */
	private Context context;
	
	
	/*
	 * CONSTRUCTOR
	 */
	public AssetsHelper(Context context){
		this.context = context;
	}
	
	public String getAssetsDir(){
		return this.context.getFilesDir().getAbsolutePath()+SLASH;
	}
	
	/**
	 * 
	 * @param assetsName
	 * @return
	 */
	private boolean copyAssetsToFileDir(String assetsName){
		boolean bool = false;
		
		try{

			InputStream is = context.getAssets().open(assetsName);
			//OutputStream os  = context.openFileOutput(assetsName, Context.MODE_PRIVATE);
			FileOutputStream fos = new FileOutputStream( new File(getAssetsDir()+SLASH+assetsName),
					true);
			byte[] buffer = new byte[1024];
			int length = 0;
			while((length = is.read(buffer))>0){
				fos.write(buffer, 0, length);
			}
			fos.flush();
			fos.close();
			is.close();
			bool = true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return bool;
	}

	public boolean copyAssetsToAppDir(String assetsName){
		boolean bool = false;
		
		try{

			InputStream is = context.getAssets().open(assetsName);
			//OutputStream os  = context.openFileOutput(assetsName, Context.MODE_PRIVATE);
			FileOutputStream fos = new FileOutputStream( new File(StoragePreferences.getAppDir()+SLASH+assetsName),
					true);
			byte[] buffer = new byte[1024];
			int length = 0;
			while((length = is.read(buffer))>0){
				fos.write(buffer, 0, length);
			}
			fos.flush();
			fos.close();
			is.close();
			bool = true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return bool;
	}
	
	/**
	 * 
	 * @param assetsName
	 * @return
	 */
	public boolean isAssetsReady(String assetsName){
		
		return new File(getAssetsDir()
				+ SLASH 
				+ assetsName).exists();
		
	}
	
	/**
	 * 
	 * @param assetsName
	 * @return
	 */
	public Bitmap getAssetsBitmap(String assetsName){
		Bitmap bitmap = null;
		if(this.isAssetsReady(assetsName) ){
			bitmap = BitmapFactory.decodeFile(getAssetsDir()
					+ SLASH 
					+ assetsName);
		}
		return bitmap;
	}
	
	/**
	 * 
	 * @param assetsName
	 * @return
	 */
	public String getAssetsString(String assetsName){
		String string = ASSETS_STRING_NULL;
		
		try{
			
			InputStream is = context.getAssets().open(assetsName);
			FileOutputStream fos = new FileOutputStream( new File(getAssetsDir()+SLASH+assetsName),
					false);
			
			byte[] buffer = new byte[1024];
			int length = 0;
			while((length = is.read(buffer))>0){
				fos.write(buffer, 0, length);
			}
			
			fos.flush();
			fos.close();
			is.close();
			
			FileInputStream fis = new FileInputStream( new File(getAssetsDir()+SLASH+assetsName));  
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
	
	public void saveStringFile(String string, String filename){
		
		byte[] b=string.getBytes(); 
	    BufferedOutputStream stream = null; 
	    String path = StoragePreferences.getIconDir()+filename;
	    File targetFile = new File(path);
	    
	    Log.i(this.getClass().getName(), "write:"+path+":"+string.length());
	    
		try{
			
			/*
			FileOutputStream fos = new FileOutputStream( new File(path),
					false);
			
			stream = new BufferedOutputStream(fos); 
	        stream.write(b); 
	        
	        fos.close();
	        */
	        
	        if (targetFile.exists()) {   
	            targetFile.delete();   
	        }   
	    
	        OutputStreamWriter osw;   
	        try{   
	            osw = new OutputStreamWriter(   
	                        new FileOutputStream(targetFile),"utf-8");   
	            try {   
	                osw.write(string);   
	                osw.flush();   
	                osw.close();   
	            } catch (IOException e) {   
	                // TODO Auto-generated catch block   
	                e.printStackTrace();   
	            }   
	        }
	        catch(Exception e1){
	        	e1.printStackTrace();
	        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
	    /*
		finally { 
	        if (stream != null) { 
	            try { 
	                 stream.close(); 
	            } 
	            catch (IOException e1) { 
	                 e1.printStackTrace(); 
	            } 
	         } 
	    } 
	    */
	}
	
	public void removeFile(String filename){
		File file = new File(getAssetsDir()+filename);
		if(file.exists()){
			file.delete();
		}
	}
	
	public void renameFile(String file_from, String file_to){
		File file = new File(getAssetsDir()+file_from);
		if(file.exists()){
			File old_file = new File(getAssetsDir()+file_to);
			if(old_file.exists()){
				old_file.delete();
			}
			file.renameTo(old_file);
		}
	}
	
	public String readStringFile(String filename){
		
		String path = StoragePreferences.getIconDir()+filename;
		
	    String filecontent = ASSETS_STRING_NULL;   
	    File f = new File(path);   
	    if (f != null && f.exists())   
	    {
	        FileInputStream fis = null;   
	        try {   
	            fis = new FileInputStream(f);   
	        } catch (FileNotFoundException e1) {   
	            // TODO Auto-generated catch block   
	            e1.printStackTrace();   
	            return null;   
	        }   
	  
	        CharBuffer cb;   
	        try {   
	            cb = CharBuffer.allocate(fis.available());   
	        } catch (IOException e1) {   
	            // TODO Auto-generated catch block   
	            e1.printStackTrace();   
	            return null;   
	        }   
	    
	        InputStreamReader isr;   
	        try {   
	            isr = new InputStreamReader(fis, "utf-8");   
	            try {   
	                if (cb != null) {   
	                   isr.read(cb);   
	                }   
	                filecontent = new String(cb.array());   
	                isr.close();   
	            } catch (IOException e) {   
	                e.printStackTrace();   
	            }   
	        } catch (UnsupportedEncodingException e) {   
	            // TODO Auto-generated catch block   
	            e.printStackTrace();           
	        }   
	    }   
	    return filecontent;   
		
	}
	
	
	/*
	public String readStringFile(String filename){
		String string = ASSETS_STRING_NULL;
		String path = StoragePreferences.getIconDir()+filename;
	    
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
		Log.i(this.getClass().getName(), "read:"+path+":"+string.length());
	    
		return string;		
		
	}
	*/
	public void resizePngIconFile(String path, int size_max){
		
		Bitmap bmp = BitmapFactory.decodeFile(path);
		if(bmp!=null){
			if(bmp.getWidth() > size_max 
					|| bmp.getHeight() > size_max){
			 	
				Bitmap bitmap = Bitmap.createScaledBitmap(bmp, size_max, size_max, true);
	
			 	File bitmapFile = new File(path);
			 	FileOutputStream bitmapWtriter = null;
			 	try {
			 		bitmapWtriter = new FileOutputStream(bitmapFile);
			 		bitmap.compress(Bitmap.CompressFormat.PNG, 90, bitmapWtriter); 
			 	} 
			 	catch (FileNotFoundException e) {
			 		// TODO Auto-generated catch block
			 		e.printStackTrace();
			 
			 	}
			 	
			}
		}
		
		bmp = BitmapFactory.decodeFile(path);
		if(bmp == null){
			try{
				new File(path).delete();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void savePngIconFile(Bitmap bmp, String path, int size_max){
		
		if(bmp!=null){
			 	
				Bitmap bitmap = Bitmap.createScaledBitmap(bmp, size_max, size_max, true);
	
			 	File bitmapFile = new File(path);
			 	FileOutputStream bitmapWtriter = null;
			 	try {
			 		bitmapWtriter = new FileOutputStream(bitmapFile);
			 		bitmap.compress(Bitmap.CompressFormat.PNG, 90, bitmapWtriter); 
			 	} 
			 	catch (FileNotFoundException e) {
			 		// TODO Auto-generated catch block
			 		e.printStackTrace();
			 
			 	}
			 	
		}
		
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		if(bmp == null){
			try{
				new File(path).delete();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
}
