/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.android.aid.ApkFileHelper.ApkInfos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public abstract class ThreadDownload extends Thread{
	
	/*
	 * CONSTANTS, PROTECTED
	 */
	protected static final int MAX_THREADS			= 3;
	protected static final int MIN_THREADS			= 1;
	protected static final int MAX_DUMMY_TIMER		= 30000;
	protected static final int BUFFER_SIZE			= 8192;
	protected static final long SLEEP_TIMER			= 100;
	protected static final String SLASH				= "/";
	protected static final String APP_DIR			= "/ApkStore";
	protected static final String DOWNLOAD_SUFFIX	= ".download";
	/*
	 * PROPERTIES,PROTECTED
	 */
	protected Context context;
	protected boolean running;
	protected long stamp;
	protected boolean pause;
	protected boolean finished;
	protected boolean downloadFlag;
	protected String filename;
	protected String savename;
	protected String download_root;
	protected String download_path;
	protected String save_path; 
	protected int download_count;
	protected long size;
	protected int serial;
	
	protected DBSchemaDownload schema;
	protected DBSchemaDownload.Columns column;
	
	/*
	 * CONSTRUCTOR
	 */
	
	public ThreadDownload(Context context){
		super();
		this.context = context;
	}
	
	public ThreadDownload(Context context, DBSchemaDownload schema, String download_root, 
			String download_path, String save_path, DBSchemaDownload.Columns columns) {
		super();
		this.context = context;
		this.schema = schema;
		this.running = false;
		this.pause = false;
		this.finished = false;
		this.downloadFlag = false;
		this.filename = columns.getFileFrom();
		this.savename = columns.getFileTo();
		this.download_root = download_root;
		this.download_path = download_path;
		this.save_path = save_path;
		this.download_count = columns.getCount();
		this.size = columns.getSize();
		this.serial = columns.getSerial();
		this.column = columns;
		checkSaveDir();
		getThreadList();
		getThreadMap();
	}

	/*
	 * METHODS, PROTECTED
	 */
	protected boolean isRunning(){
		return this.running;
	}
	
	
	protected boolean isPause(){
		return this.pause;
	}
	
	protected void pauseThread(){
		this.pause = true;
	}
	
	protected void resumeThread(){
		this.pause = false;
	}
	
	protected void stopThread(){
		
		this.running = false;
		this.pause = false;
		//Log.i(this.getClass().getName(),"stop thread:"+this.filename);
	}
	
	protected String getFilename(){
		return this.filename;
	}
	
	protected void setFileName(String filename){
		this.filename = filename;
	}
	
	
	protected boolean isDownloading(String filename){
		boolean bool = getThreadMap().containsKey(filename);
		//Log.i(this.getClass().getName(), "isDownloading:"+filename+"="+bool);
		return bool;
	}
	
	protected void addDownloadThread(ThreadDownload thread){
		getThreadList().add(thread);
		getThreadMap().put(thread.getFilename(), thread);
		//Log.i(this.getClass().getName(), "add thread " + this.filename +" to:"+getThreadList().size());
	}
	
	protected void removeDownloadThread(Thread thread,String filename){
		//ThreadDownload thread = getThreadMap().get(filename);
		
		getThreadList().remove(thread);
		getThreadMap().remove(filename);
		//Log.i(this.getClass().getName(), "remove thread" + this.filename + " to:"+getThreadList().size());
	}
	
	protected void stopDownloadThread(String filename){
		//ThreadDownload thread = getThreadMap().get(filename);
		//Log.i(this.getClass().getName(), "stop download thread:"+filename);
		ThreadDownload thread = getThread(filename);
		if(thread!=null){
			thread.stopThread();
		}
	}
	
	public ThreadDownload getThread(int index){
		if(getThreadList().size()>index){
			return getThreadList().get(index);
		}
		else{
			return null;
		}
	}

	public ThreadDownload getThread(String filename){
		ThreadDownload thread = null;
		ArrayList<ThreadDownload> list = getThreadList();
		int i =0;
		if(list!=null){
			int size = getThreadList().size();
			for(i=0; i< size; i++){
				if(filename.equals(list.get(i).filename)){
					thread = list.get(i);
					//Log.i(this.getClass().getName(), "getThread:"+i);
					break;
				}
			}
		}
		
		
		return thread;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		downloadFlag = false;
		FileOutputStream fos = null;
		InputStream is = null;
		try{
			super.run();
			running = true;
			stamp = System.currentTimeMillis();
			
			long downloadLenght = 0;
			long savedLenght =0;
			long fileLenght = 0;
			long downloadLoops = 0;
			HttpURLConnection httpConnection = null;
			if(StoragePreferences.isStorageAvailable()){
				
				try{
					//Log.i(this.getClass().getName(), "run:"+this.filename);
					URL url = new URL(getDownloadRoot()+java.net.URLEncoder.encode(filename, "UTF-8"));
					httpConnection = (HttpURLConnection) url.openConnection();
					fileLenght = httpConnection.getContentLength();
					httpConnection.disconnect();
					
					if(this.size != fileLenght){
						this.schema.updateDownloadFileSize(filename, fileLenght);
					}
					
					File downloadFile = new File(getSaveFile()
							+ StoragePreferences.getDownloadSuffix());
					if(downloadFile.exists()){
						savedLenght = (new RandomAccessFile(downloadFile,"rw")).length();
					}
					if(savedLenght == fileLenght && fileLenght >0 ){
						this.finished = true;
						downloadFlag = true;
						
					}
					else{
						
					
						httpConnection = (HttpURLConnection) url.openConnection();
						
						//httpConnection.setAllowUserInteraction(true);
						Log.i(this.getClass().getName(),"Range => bytes=" + savedLenght + "-" + fileLenght);
						httpConnection.setConnectTimeout(12*1000);
						httpConnection.setRequestProperty("accept", "*/*"); 
						httpConnection.setRequestProperty("Range", "bytes=" + savedLenght + "-" + fileLenght);
						httpConnection.connect();
						Log.i(this.getClass().getName(), "resp code:"+httpConnection.getResponseCode());
						is = httpConnection.getInputStream();
						
						if(fileLenght>1 && savedLenght > 0 && savedLenght < fileLenght )
						{
						//	httpConnection = (HttpURLConnection) url.openConnection();
						//	httpConnection.setAllowUserInteraction(true);
						//	httpConnection.setRequestProperty("Range", "bytes=" + savedLenght + "-" + fileLenght);
							fos = new FileOutputStream(downloadFile,true);
							
						}
						else{
							savedLenght = 0;
							fos = new FileOutputStream(downloadFile,false);
						}
						
						int readLenght = 0;
						
						
						//byte[] buffer = null;
						byte[] buffer = new byte[BUFFER_SIZE];
						//Log.i(this.getClass().getName(), filename+" start saveLenght/fileLenght="+savedLenght+"/"+fileLenght);
						//downloadLoops = (fileLenght - savedLenght)/BUFFER_SIZE + 1;
						while(running && savedLenght<fileLenght && downloadLoops >= 0){
							//running = false;
							stamp = System.currentTimeMillis();
						
							/*
							if(fileLenght-savedLenght > BUFFER_SIZE){
								buffer = new byte[BUFFER_SIZE];
							}	
							else{
								buffer = new byte[(int) (fileLenght-savedLenght)];
							}
							*/
							
							//downloadLoops--;
							/*
							if(readLenght > 0){
								Thread.sleep(SLEEP_TIMER);
								Log.i(this.getClass().getName(), this.filename +" sleep:"+downloadLoops);
							}
							*/
							try{
								
								readLenght=is.read(buffer);
								Log.i(this.getClass().getName(), "serial:"+ column.getSerial() +","+ filename +":" + savedLenght +"/" + fileLenght+"@"+readLenght+"/"+buffer.length);
								
								if(readLenght>0){
									fos.write(buffer, 0, readLenght);
									downloadFlag=true;
									downloadLenght += readLenght;
									savedLenght += readLenght;
								}
	
							}
							catch(Exception writeE){
								writeE.printStackTrace();
								readLenght = 0;
								running = false;
								downloadFlag = false;
							}
							
							if(isApkDownloading()){
								if(!(new ConfigPreferences(context).isDownloadConnection())){
									running = false;
								}
							}
							else{
								if(!new DataConnectionHelper(context).isDataOnline()){
									running = false;
								}
							}
							Thread.sleep(SLEEP_TIMER);
							
							
						}
						
						if(savedLenght >= fileLenght && fileLenght > 0){
							this.finished = true;
							downloadFlag = true;
							//Log.i(this.getClass().getName(), "download finished:"+filename);
						}
						
						
					
					}
				
				}
				catch(Exception e){
					e.printStackTrace();
					Log.i(this.getClass().getName(), "download exception:"+e.getCause()+","+e.getMessage());
				}
				finally{
					try{
						if(fos!=null){
							fos.close();
						}
						if(is!=null){
							is.close();
						}
						httpConnection.disconnect();
					}
					catch(Exception eIO){
						eIO.printStackTrace();
					}
				}
			
			}
		}
		catch(Exception eThread){
			eThread.printStackTrace();
		}
		finally{
			if(!downloadFlag){
				schema.updateDownloadFile(this.filename, this.download_count+1);
			}
			exitDownload();
			
		}
		
		
	}
	protected void checkSaveDir(){
		
		File file = new File(getAppDir());
		if(!file.exists()){
			file.mkdirs();
		}
		
		file = new File(getSaveRoot());
		if(!file.exists()){
			file.mkdir();
		}
		
	}
	
	protected void exitDownload(){
		try{
			saveDownloaded();
			
			removeDownloadThread(this,this.filename);
			onDownloadFinished();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void saveDownloaded(){
		try{
			if(this.finished){
				File file = new File(getSaveFile() + StoragePreferences.getDownloadSuffix()); 
				file.renameTo(new File(getSaveFile()));
				schema.deleteDownloaded(this.filename);
			}
			else{
				if(this.download_count == DBSchemaDownload.VAL_DOWNLOAD_COUNT_MAX){
					//Log.i("ThreadDownload", "delete: " + this.filename);
					schema.deleteDownloaded(this.filename);
					if(this.filename.substring(this.filename.lastIndexOf('.')).toLowerCase().equals(".apk")){
						new DBSchemaReportDownload(context).addActionRecord(column.getPackage(), 
								column.getVerCode(), DBSchemaReport.VAL_ACTION_DOWNLOAD_DROP);
					}
				}
				else{
					schema.updateDownloadFile(this.filename, this.download_count+1);
				}
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected String getAppDir(){
		return StoragePreferences.getAppDir();
	}
	
	protected String getSaveFile(){
		return getSaveRoot() +  this.savename;
	}
	
	protected String getDownloadRoot() {
		// TODO Auto-generated method stub
		return this.download_root+this.download_path+SLASH;
	}
	
	public String getSaveRoot() {
		// TODO Auto-generated method stub
		return this.save_path;
	}

	protected boolean isThreadListAvailable()
	{
		// TODO Auto-generated method stub
		boolean bool = false;
		if(new PackageHelper(context).isRunningMe()){
			if(getThreadList().size() < MAX_THREADS){
				bool = true;
				//Log.i(this.getClass().getName(), "threads available = "+(MAX_THREADS-getThreadList().size()));
			}
		}
		else{
			if(getThreadList().size() < MIN_THREADS){
				bool = true;
				//Log.i(this.getClass().getName(), "threads available = "+(MAX_THREADS-getThreadList().size()));
			}
		}
		
		if(!bool){
			ArrayList<ThreadDownload> list = getThreadList();
			//Iterator<ThreadDownload> iterator = list.iterator();
			//while(iterator.hasNext()){
				//Log.i(this.getClass().getName(),iterator.next().getFilename());
			//}
			String firstfile = list.get(0).getFilename();
			list.get(0).stopDownloadThread(firstfile);
			//Log.i(this.getClass().getName(), "firstfile:"+firstfile+"@"+list.get(0).stamp+"/"+System.currentTimeMillis());
			long timer = System.currentTimeMillis() - list.get(0).stamp - MAX_DUMMY_TIMER;
			Log.i(this.getClass().getName(), "timer:"+(int)(timer));
			if(((int)(timer)) > 0){
				//Log.i(this.getClass().getName(), "remove:"+firstfile);
				removeDownloadThread(list.get(0),list.get(0).getFilename());
				//getThreadList().remove(0);
				//getThreadMap().remove(firstfile);
			}
		}
		
		return bool;
	}

	

	
	protected boolean isApkDownloading(){
		boolean bool = false;
		if(this.column.getFileFrom().toLowerCase().endsWith(".apk")){
			bool = true;
		}
		return bool;
	}
	
	protected boolean startDownloadThread(){
		boolean bool = false;
		//Log.i(this.getClass().getName(), "start thread:"+this.filename);
		if(this.isThreadListAvailable()){
			bool = true;
			if(!this.isDownloading(this.getFilename())){
				this.addDownloadThread(this);
				this.start();
			}
		}
		
		return bool;
	}
	
	/*
	 * METHODS, ABSTRACT
	 */
	
	protected abstract ArrayList<ThreadDownload> getThreadList();
	
	protected abstract HashMap<String, ThreadDownload> getThreadMap();
	/**
	 * onDownloadFinished()
	 * called while running exit
	 */
	protected abstract void onDownloadFinished();
	
	

}
