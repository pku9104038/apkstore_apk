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
public abstract class ReqReportDelegate extends ServiceActionDelegate{
	
	protected DBSchemaReport schema;
	protected DBSchemaReport.Columns row;
	protected String web_api_key;
	
	public ReqReportDelegate(Context context, String requestAction, DBSchemaReport schema, String web_api_key) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
		this.schema = schema;
		this.web_api_key = web_api_key;
		this.row = null;
	}

	/* (non-Javadoc)
	 * @see com.android.aid.ActionDelegate#action()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		row = getNextRecord();
		
		if(row!=null){
			//Log.i("test", "row="+row.getPackage()+"@"+row.getAction());
			new ReportApi(context,web_api_key,lsrReport).postApi();
		}
		else{
			//Log.i("test", "row == null");
		}
		
	}
	
	
	private class ReportApi extends WebServiceApi{

		public ReportApi(Context context, String web_api_pref_key,
				HttpPostListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			this.postApi(getApiParams());	
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			this.getApi(getApiParams());	
			
		}
		
	}

	private HttpPostListener lsrReport = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			//Log.i(this.getClass().getName(),response[HttpPostListener.POST_URL]+" Resp: "+response[HttpPostListener.POST_RESP]);
			apiResponseAction(response[HttpListener.HTTP_RESP]);
			
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				schema.deleteRow(row.getRowId());
				requestAgain();
			}

			/*
			if(WebServiceApi.isRespSuccess(response[HttpPostListener.POST_RESP])){
				JSONArray array = null;
				if((array = WebServiceApi.getRespArray(response[HttpPostListener.POST_RESP]))!=null){
					DBSchemaApplications schema = new DBSchemaApplications(context);
					schema.updateApplicationBasicInfos(array);

				}
			}
			*/
			
		}
		
		
	};
	
	protected abstract DBSchemaReport.Columns getNextRecord();
	
	protected ArrayList<NameValuePair> getApiParams(){
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_PACKAGE[DBSchema.COL_NAME],
				row.getPackage()));
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_LABEL[DBSchema.COL_NAME],
				row.getLabel()));
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_VERCODE[DBSchema.COL_NAME],
				row.getVerCode()+""));
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_ACTION[DBSchema.COL_NAME],
				row.getAction()+""));
		//Log.i(this.getClass().getName(), "action:"+row.getAction());
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_STAMP[DBSchema.COL_NAME],
				row.getStamp()));
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_DATE[DBSchema.COL_NAME],
				row.getDate()));
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_COUNT[DBSchema.COL_NAME],
				row.getCount()+""));
		
		
		return params;
	
	}
	
	protected  void apiResponseAction(String response){
		
	}

}
