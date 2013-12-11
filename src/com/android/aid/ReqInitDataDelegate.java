/**
 * 
 */
package com.android.aid;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqInitDataDelegate extends ServiceActionDelegate{
	
	/*
	 * CONSTANTS
	 */
	private static final String ASSETS_APPLICATIONS			= "api_applications_sync.json";
	

	public ReqInitDataDelegate(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
    	//Log.i(this.getClass().getName(), "init data!");
		new GroupPreferences(context).getPrefString();
		DBSchemaApplications schema = new DBSchemaApplications(context);
		InitPreferences pref = new InitPreferences(context);
        //if(!(schema.isApplicationsAvailable())){
        	//Log.i(this.getClass().getName(), "init db data!");
		/*
		if(!pref.isDataInited() && !pref.isDataIniting()){
			pref.setDataInit(InitPreferences.VAL_DATA_INITING);
			if(new StampPreferences(context).isApplicationsUpdate()){
				schema.initSchema();
				new StampPreferences(context).setApplicationsUpdateStamp();
			}
			if(schema.isApplicationsAvailable()){
				pref.setDataInit(InitPreferences.VAL_DATA_INITED);
			}
			else{
				pref.setDataInit(InitPreferences.VAL_DATA_NULL);
			}
		}
        */
		
		//schema.initSchema();
		new StampPreferences(context).setApplicationsUpdateStamp();
		if(schema.isApplicationsAvailable()){
			pref.setDataInit(InitPreferences.VAL_DATA_INITED);
		}
		
		
 /*		
		DBSchemaApplications schema = new DBSchemaApplications(context);
		if(!schema.isApplicationsAvailable()){
			String assets = new AssetsHelper(context).getAssetsString(ASSETS_APPLICATIONS);
			if(WebServiceApi.isRespSuccess(assets)){
				JSONArray array = null;
				if((array = WebServiceApi.getRespArray(assets))!=null){
					schema.updateApplicationBasicInfos(array);
				}
			}
			
		}
*/		
	}

}
