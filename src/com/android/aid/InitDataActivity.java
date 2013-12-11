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
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author wangpeifeng
 *
 */
public class InitDataActivity extends Activity{
	
	public static final String REQUEST_GROUP		= "request_group";
	
	private Context context;
	private ImageView ivWallpapaer, flying_item;
	private Animation in;
	private int x,y;
	private int group_serial;
	private InitPreferences initPref;
	private DBSchemaApplications schemaApp;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        
		this.context = this;
		this.initPref = new InitPreferences(context);
		this.schemaApp = new DBSchemaApplications(context);
		this.group_serial = getIntent().getIntExtra(REQUEST_GROUP, GroupPreferences.GROUP_PRIORITY_MIN);
		new IntentSender(context).startInitData();
        
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);
        ((TextView)findViewById(R.id.textViewMoreService)).setText(R.string.waiting_datainit);
        //((TextView)findViewById(R.id.textViewCallService)).setText(R.string.check_connection_storage);
        ((TextView)findViewById(R.id.textViewCallService)).setVisibility(View.GONE);
		
        in = AnimationUtils.loadAnimation(context, R.anim.flying);
        this.flying_item = (ImageView) findViewById(R.id.imageViewFlying); 
        this.flying_item.startAnimation(in); 

		//this.threadUnzip.start();
        new IntentSender(context).unzipIconFiles();
        this.thread.start();
		
		
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//waiting = false;
		super.onPause();
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		waiting = true;
		super.onResume();
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	private boolean waiting = false;
	
	private Thread threadUnzip = new Thread(){

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			AssetsHelper assetsHelper = new AssetsHelper(context);
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
	
	private Thread thread = new Thread(){

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				super.run();
				int count = 1;
				waiting = true;
				
				while(waiting){
					try {
						//if(initPref.isDataInited()){
					    //if(!(new InitPreferences(context).isDataNull())){
					    if(schemaApp.isGroupAvailable(group_serial)){
					    	setResult(Activity.RESULT_OK);
							finish();
							waiting = false;
						}
					    else{
					    	Log.i("initdata", "waiting..."+count+"S");
					    }
						Thread.sleep(1000);
						count++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			catch(Exception eThread){
				eThread.printStackTrace();
			}
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
	             Log.i("upZipFile", "ze.getName() = "+ze.getName());
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
