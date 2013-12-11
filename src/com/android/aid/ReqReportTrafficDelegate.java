/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class ReqReportTrafficDelegate extends ServiceActionDelegate{
	
	protected DBSchemaTraffic schema;
	protected DBSchemaTraffic.Columns row;
	protected String web_api_key;
	

	public ReqReportTrafficDelegate(Context context, String requestAction) {
		super(context, requestAction);
		schema = new DBSchemaTraffic(context);
		// TODO Auto-generated constructor stub
	}


	private class ReportTrafficApi extends WebServiceApi{

		public ReportTrafficApi(Context context, String web_api_pref_key,
				HttpPostListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair(
					DBSchemaTraffic.COL_DATE[DBSchema.COL_NAME],
					row.getDate()));
			
			params.add(new BasicNameValuePair(
					DBSchemaTraffic.COL_MY_RX[DBSchema.COL_NAME],
					row.getMyRx()+""));
			
			params.add(new BasicNameValuePair(
					DBSchemaTraffic.COL_MY_TX[DBSchema.COL_NAME],
					row.getMyTx()+""));
			
			params.add(new BasicNameValuePair(
					DBSchemaTraffic.COL_MOBILE_RX[DBSchema.COL_NAME],
					row.getMobileRx()+""));
			
			params.add(new BasicNameValuePair(
					DBSchemaTraffic.COL_MOBILE_TX[DBSchema.COL_NAME],
					row.getMobileTx()+""));
			
			params.add(new BasicNameValuePair(
					DBSchemaTraffic.COL_TOTAL_RX[DBSchema.COL_NAME],
					row.getTotalRx()+""));
			
			params.add(new BasicNameValuePair(
					DBSchemaTraffic.COL_TOTAL_TX[DBSchema.COL_NAME],
					row.getTotalTx()+""));
			
			this.postApi(params);	
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			//ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			//this.postApi(params);	
			
		}
		
	}

	private HttpPostListener lsrReportTraffic = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				schema.deleteRow(row.getRowId());
			}
			
		}
		
		
	};
	
	

	@Override
	public void action() {
		// TODO Auto-generated method stub
		row = schema.getTrafficRecord(StampHelper.getYesterday());
		
		if(row != null){	
			new ReportTrafficApi(this.getContext(),
					WebServicePreferences.PREF_KEY_API_REPORT_TRAFFIC,
					this.lsrReportTraffic).postApi();
		}
	}


}
