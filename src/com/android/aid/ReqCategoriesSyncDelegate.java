/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;



import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqCategoriesSyncDelegate extends ServiceActionDelegate{

	public ReqCategoriesSyncDelegate(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}

	private class CategoriesSyncApi extends WebServiceApi{

		public CategoriesSyncApi(Context context, String web_api_pref_key,
				HttpPostListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			if(new StampPreferences(context).isCategoriesSync()){
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
						new StampPreferences(context).getCategoriesSyncStamp()+""));
				this.postApi(params);	
			}
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			if(new StampPreferences(context).isCategoriesSync()){
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_STAMP, 
						new StampPreferences(context).getCategoriesSyncStamp()+""));
				this.getApi(params);	
			}
			
		}
		
	}
	
	private HttpPostListener lsrCategoriesSync = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			//Log.i(this.getClass().getName(),response[HttpPostListener.POST_URL]+" Resp: "+response[HttpPostListener.POST_RESP]);
			
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				JSONArray array = null;
				if((array = WebServiceApi.getRespArray(response[HttpListener.HTTP_RESP]))!=null){
					new CategoriesPreferences(context).setPref(array.toString());
					new StampPreferences(context).setCategoriesSyncStamp();
				}
			}
			clearBusy();
		}
		
	};
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		if(new StampPreferences(context).isCategoriesSync() ){
			setBusy();
			new CategoriesSyncApi(this.getContext(),
					WebServicePreferences.PREF_KEY_API_CATEGORIES_SYNC,
					this.lsrCategoriesSync);//.postApi();
		}
		
	}
	protected static boolean isBusy = false;
	
	protected void setBusy(){
		isBusy = true;
	}
	
	protected void clearBusy(){
		isBusy = false;
	}
	
	protected boolean isBusy(){
		return isBusy;
	}
}
