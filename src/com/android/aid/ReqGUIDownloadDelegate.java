/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqGUIDownloadDelegate extends ReqDownloadDelegate{

	/*
	 * PROPERTIES, PRIVATE
	 */
	//private boolean serverChecked;
	//private String serverUrl;
	
	/*
	 * PROPERTIES, BEHAVIAR
	 */
	/*
	private HttpPostListener lsrHttpPost = new HttpPostListener(this.context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			
			if(!serverChecked){
				if(WebServiceApi.isRespSuccess(response[HttpPostListener.POST_RESP])){
					serverUrl = response[HttpPostListener.POST_URL].substring(0, 
							response[HttpPostListener.POST_URL].length() 
							- WebServicePreferences.VAL_DEF_API_DOWNLOAD_TESTFILE.length());
					serverChecked = true;
					Log.i(this.getClass().getName(),"best download server: "+serverUrl);
					
					StartDownloadGUI(serverUrl);
					
				}
			}
			
		}
		
	};
*/
	/*
	 * CONSTRUCTOR
	 */
	
	public ReqGUIDownloadDelegate(Context context, String requestAction,
			DBSchemaDownload schema) {
		super(context, requestAction, schema);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param context
	 * @param requestAction
	 */
/*
	public ReqGUIDownloadDelegate(Context context,String requestAction) {
		super(context, requestAction, schema);
		// TODO Auto-generated constructor stub
		this.serverChecked = false;
		this.serverUrl = null;
	}
*/
	/*
	 * (non-Javadoc)
	 * @see com.android.aid.ActionDelegate#action()
	 */
	/*
	@Override
	public void action() {
		// TODO Auto-generated method stub
		DBSchemaGUIDownload schema = new DBSchemaGUIDownload(context);
		if(schema.isDownloading()){
			
			ConfigPreferences configPref = new ConfigPreferences(this.context);
			String[] servers = configPref.getDownloadServers();
			
			ArrayList<ConfigUpdateApi> lstServerApi = new ArrayList<ConfigUpdateApi>();
			for(int i = 0; i < servers.length; i++){
				ConfigUpdateApi serverTestApi = new ConfigUpdateApi(context, 
						servers[i]+WebServicePreferences.VAL_DEF_API_DOWNLOAD_TESTFILE,
						this.lsrHttpPost);
				lstServerApi.add(serverTestApi);
			}
			for(int i = 0; i < servers.length; i++){
				lstServerApi.get(i).postApi();
			}
		}
	}
*/

	/**
	 * 
	 * @param download_root
	 */
	/*
	private void StartDownloadGUI(String download_root){

		DBSchemaGUIDownload schema = new DBSchemaGUIDownload(context);
		ArrayList<DBSchemaGUIDownload.Columns> lstGUIDownloading = schema.getDownloading();
		if(lstGUIDownloading.size()>0){
			Iterator<DBSchemaGUIDownload.Columns> iterator = lstGUIDownloading.iterator();
			int i = 0;
			while(iterator.hasNext()){
				DBSchemaGUIDownload.Columns row = iterator.next();
				ThreadDownloadGUI thread = new ThreadDownloadGUI(context, download_root, row.getFileFrom(), row.getFileTo());
				if(thread.isAvailable()){
					if(!thread.isDownloading(thread.getFilename())){
						thread.addDownloadThread(thread);
						thread.start();
					}
				}
				else{
					break;
				}
				i++;
				
			}
			
		}
		
	}
	*/
	protected void startDownload(){

		ArrayList<DBSchemaDownload.Columns> lstDownloading = schema.getDownloading();
		if(lstDownloading.size()>0){
			Iterator<DBSchemaDownload.Columns> iterator = lstDownloading.iterator();
			int i = 0;
			while(iterator.hasNext()){
				DBSchemaDownload.Columns row = iterator.next();
				ThreadDownloadGUI thread = new ThreadDownloadGUI(context,
						schema,
						this.serverUrl, 
						StoragePreferences.getGuiPath(),
						//row.getFileFrom(), 
						(new StoragePreferences(context)).getGuiDir(),
						row);
				/*
				if(thread.isThreadListAvailable()){
					if(!thread.isDownloading(thread.getFilename())){
						thread.addDownloadThread(thread);
						thread.start();
					}
				}
				
				else{
				*/
				if(!thread.startDownloadThread()){
					break;
				}
				else if(thread.getThreadList().size()==ThreadDownload.MAX_THREADS){
					break;
				}
				i++;
				
			}
			
		}
		
	}


	
	
}
