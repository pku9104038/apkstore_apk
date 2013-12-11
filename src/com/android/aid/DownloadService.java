/**
 * 
 */
package com.android.aid;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DownloadService extends Service{
	
	public static final String REQUEST_GUI_DOWNLOAD			= "request_gui_download";
	
	public static final String REQUEST_ICON_DOWNLOAD		= "request_icon_download";
	
	public static final String REQUEST_APK_DOWNLOAD			= "request_apk_download";
	
	public static final String REQUEST_SWITCH_GROUP			= "request_switch_group";
	
	public static final String REQUEST_UNZIP_ICONS			= "request_unzip_icons";
	
	public static final String EXTRA_CURRENT_GROUP			= "current_group";
	
	
	private static int current_group;
	
	public static int getCurrentGroup(){
		return current_group;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		try{
			if(intent!=null){
			Context context = getApplicationContext();
			
			if(intent.getBooleanExtra(REQUEST_SWITCH_GROUP, false)){
				current_group = intent.getIntExtra(EXTRA_CURRENT_GROUP, 0);
			}
			
			if(intent.getBooleanExtra(REQUEST_UNZIP_ICONS, false)){
				threadUnzip.start();
			}
			
			ArrayList<ServiceActionDelegate> lstDelegates = new ArrayList<ServiceActionDelegate>();
			
			
			if(intent.getBooleanExtra(REQUEST_GUI_DOWNLOAD, false)){
				lstDelegates.add(new ReqGUIDownloadDelegate( context,
					REQUEST_GUI_DOWNLOAD,
					new DBSchemaGUIDownload(context)));
			}
			
			
			if(intent.getBooleanExtra(REQUEST_ICON_DOWNLOAD, false)){
				lstDelegates.add(new ReqIconDownloadDelegate(context,
					REQUEST_ICON_DOWNLOAD,
					new DBSchemaIconDownload(context)));
			}
			
			if(intent.getBooleanExtra(REQUEST_APK_DOWNLOAD, false)){
				lstDelegates.add(new ReqDownloadApkDelegate(context,
					REQUEST_APK_DOWNLOAD,
					new DBSchemaDownloadApk(context)));
			}
			
			
			//runing 
			Iterator<ServiceActionDelegate> iterator = lstDelegates.iterator();
			while(iterator.hasNext()){
				ServiceActionDelegate delegate = iterator.next();
				if(delegate!=null){
					try{
						Log.i(this.getClass().getName(), delegate.getRequestAction());
						delegate.checkRequestAction(getApplicationContext(),intent);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private Thread threadUnzip = new Thread(){

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			AssetsHelper assetsHelper = new AssetsHelper(getApplicationContext());
			assetsHelper.copyAssetsToAppDir("icons.zip");
			File zipfile = new File(StoragePreferences.getAppDir()+"/icons.zip");
			try{
				upZipFile(zipfile, StoragePreferences.getIconDir());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			zipfile.delete();
			
			
		}
		
		
	};
	/**
     * 解压缩功能.
     * 将zipFile文件解压到folderPath目录下.
     * @throws Exception
 */
     public int upZipFile(File zipFile, String folderPath)throws ZipException,IOException {
     //public static void upZipFile() throws Exception{
         ZipFile zfile=new ZipFile(zipFile);
         Enumeration zList=zfile.entries();
        ZipEntry ze=null;
        byte[] buf=new byte[1024];
        while(zList.hasMoreElements()){
            ze=(ZipEntry)zList.nextElement();    
            if(ze.isDirectory()){
                Log.d("upZipFile", "ze.getName() = "+ze.getName());
                String dirstr = folderPath + ze.getName();
                //dirstr.trim();
                dirstr = new String(dirstr.getBytes("8859_1"), "UTF_8");
                Log.i("upZipFile", "str = "+dirstr);
                File f=new File(dirstr);
                f.mkdir();
                continue;
            }
            //Log.i("upZipFile", "ze.getName() = "+ze.getName());
            //OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            OutputStream os=new BufferedOutputStream(new FileOutputStream(folderPath+"/"+ ze.getName()));
            InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
            int readLen=0;
            while ((readLen=is.read(buf, 0, 1024))!=-1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();    
        }
        zfile.close();
        Log.i("upZipFile", "finishssssssssssssssssssss");
        return 0;
    }

     /**
          * 给定根目录，返回一个相对路径所对应的实际文件名.
          * @param baseDir 指定根目录
          * @param absFileName 相对路径名，来自于ZipEntry中的name
          * @return java.io.File 实际的文件
      */
          public static File getRealFileName(String baseDir, String absFileName){
              String[] dirs=absFileName.split("/");
              File ret=new File(baseDir);
              String substr = null;
              if(dirs.length>1){
                  for (int i = 0; i < dirs.length-1;i++) {
                      substr = dirs[i];
                      try {
                          //substr.trim();
                          substr = new String(substr.getBytes("8859_1"), "GB2312");
                          
                      } catch (UnsupportedEncodingException e) {
                          // TODO Auto-generated catch block
                          e.printStackTrace();
                      }
                      ret=new File(ret, substr);
                      
                  }
                  Log.i("upZipFile", "1ret = "+ret);
                  if(!ret.exists())
                      ret.mkdirs();
                  substr = dirs[dirs.length-1];
                  try {
                      //substr.trim();
                      substr = new String(substr.getBytes("8859_1"), "GB2312");
                      Log.i("upZipFile", "substr = "+substr);
                  } catch (UnsupportedEncodingException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                  }
                  
                  ret=new File(ret, substr);
                  Log.i("upZipFile", "2ret = "+ret);
                  return ret;
              }
              return ret;
          }

}
