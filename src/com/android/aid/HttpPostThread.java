/**
 * 
 */
package com.android.aid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;



/**
 * @author wangpeifeng
 *
 */
public class HttpPostThread extends HttpThread{
	
    
	/**
	 * @param url
	 * @param params
	 * @param listener
	 */
	public HttpPostThread(Context context, String url, ArrayList<NameValuePair> params,
			HttpPostListener listener) {
		super();
		this.context = context;
		this.url = url;
		this.params = params;
		this.listener = listener;
		this.response = new String[2];
		this.response[HttpListener.HTTP_URL] = this.url;
		this.response[HttpListener.HTTP_RESP] = "";
		this.addDeviceAppParams();
	}
	

	/*
	 * METHODS, OVERRIDE
	 */

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
	    try {
			super.run();
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(this.url);
	    	
	    	if(new DataConnectionHelper(context).isDataOnline()){
		    	httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		    	
		    	httpPost.setEntity(new UrlEncodedFormEntity(this.params,HTTP.UTF_8));
		    	
		    	Log.i(getClass().getName(), this.toString());
		    	
		    	
		    	HttpResponse httpResp = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResp.getEntity();
	
			    if(httpEntity != null){
			    	/*
			    	InputStreamReader isReader = new InputStreamReader(httpEntity.getContent());
			    	BufferedReader br;
			    	br = new BufferedReader(br, 0);
					BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
					String line = null;
					while((line = br.readLine()) != null){
						this.response[HttpListener.HTTP_RESP] += line;
					}
			    	Log.i(getClass().getName(), this.url +" PostResp : "+response[HttpListener.HTTP_RESP]);
			    	*/
			    	/*
			    	InputStream is = httpEntity.getContent();
					//FileOutputStream fos = new FileOutputStream( new File(StoragePreferences.getCacheDir()+"/"+ this.getUrlName()+StoragePreferences.JSON_SUFFIX),false);
			    	FileOutputStream fos = new FileOutputStream( new StoragePreferences(context).getHttpRespDir()+"/"+ this.getUrlName()+StoragePreferences.JSON_SUFFIX, false);
					byte[] buffer = new byte[1024];
					int length = 0;
					while((length = is.read(buffer))>0){
						fos.write(buffer, 0, length);
					}
					fos.flush();
					fos.close();
					is.close();
					
					//FileInputStream fis = new FileInputStream( new File(StoragePreferences.getCacheDir()+"/"+this.getUrlName()+".json"));  
					FileInputStream fis = new FileInputStream( new File(new StoragePreferences(context).getHttpRespDir()+"/"+this.getUrlName()+StoragePreferences.JSON_SUFFIX));  
					StringBuffer sb = new StringBuffer();
					int c;
					while((c=fis.read())!=-1){
						sb.append((char)c);
					}
					fis.close();
					this.response[HttpListener.HTTP_RESP] = sb.toString();
					*/
			    	InputStream is = httpEntity.getContent();
			    	StringBuffer sb = new StringBuffer(8192);
					int c;
					while((c=is.read())!=-1){
						sb.append((char)c);
					}
					this.response[HttpListener.HTTP_RESP] = sb.toString();
					
			    }
	    	}
	    	else{
				this.response[HttpListener.HTTP_RESP] += DataConnectionHelper.DATA_CONNECTION_OFFLINE;
				//Log.i(this.getClass().getName(), DataConnectionHelper.DATA_CONNECTION_OFFLINE);
	    	}
	    	Log.i(getClass().getName(), this.url +" PostResp : "+response[HttpListener.HTTP_RESP]);
	    	
	    }catch (Exception e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    	//Log.i(getClass().getName(), "Exception : cause="+ e.getCause()+", msg="+e.getMessage()+", locMsg="+e.getLocalizedMessage());
			
		}
	    finally{
	    	this.listener.OnResponse(this.response);
	    	
	    }
		
	}




	/* (non-Javadoc)
	 * @see java.lang.Thread#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String string = "HttpPost: ";
		string += this.url + "/ ";
		
		Iterator<NameValuePair> i = this.params.iterator();
		while(i.hasNext()){
			string += " & ";
			BasicNameValuePair param = (BasicNameValuePair)i.next();
			//Log.i(this.getClass().getName(), param.toString());
			string += param.getName() + "=" + param.getValue();
		}
		
		return string;
		//return super.toString();
	}
	
	
	

}
