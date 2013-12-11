/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.aid.DBSchemaReport.Columns;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqReportDeviceDelegate extends ServiceActionDelegate{



	
	public ReqReportDeviceDelegate(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}

	

	private class ReportDeviceApi extends WebServiceApi{

		public ReportDeviceApi(Context context, String web_api_pref_key,
				HttpPostListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			this.postApi(params);	
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			this.postApi(params);	
			
		}
		
	}

	private HttpPostListener lsrReportDevice = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				
				DevicePreferences devPref = new DevicePreferences(context);
				int newSN = WebServiceApi.getRespSN(response[HttpListener.HTTP_RESP]);
				int nowSN = devPref.getDevSN();
				//Log.i(this.getClass().getName(), "nowSn/newSn="+nowSN+"/"+newSN);
				if(newSN != nowSN){
					devPref.setDevSN(newSN);
				}
				
			}
			
		}
		
		
	};
	
	

	@Override
	public void action() {
		// TODO Auto-generated method stub
		if(!new DevicePreferences(context).isDevSN()){	
			new ReportDeviceApi(this.getContext(),
					WebServicePreferences.PREF_KEY_API_REPORT_DEVICE,
					this.lsrReportDevice).postApi();
		}
	}


}
