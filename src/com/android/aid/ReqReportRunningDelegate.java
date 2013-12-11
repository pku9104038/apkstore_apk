/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.android.aid.DBSchemaReport.Columns;

import android.content.Context;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ReqReportRunningDelegate extends ReqReportDelegate{

	public ReqReportRunningDelegate(Context context, String requestAction,
			DBSchemaReport schema, String web_api_key) {
		super(context, requestAction, schema, web_api_key);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Columns getNextRecord() {
		// TODO Auto-generated method stub
		return schema.getActionRecord(StampHelper.getYesterday());
		//return schema.getActionRecord();
	}
/*
	@Override
	protected ArrayList<NameValuePair> getApiParams() {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_PACKAGE[DBSchema.COL_NAME],
				row.getPackage()));
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_VERCODE[DBSchema.COL_NAME],
				row.getVerCode()+""));
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_DATE[DBSchema.COL_NAME],
				row.getDate()));
		
		params.add(new BasicNameValuePair(
				DBSchemaReport.COL_COUNT[DBSchema.COL_NAME],
				row.getCount()+""));
		
		
		return params;
	}

	@Override
	protected void apiResponseAction(String response) {
		// TODO Auto-generated method stub
		
		if(WebServiceApi.isRespSuccess(response)){
			schema.deleteRow(row.getRowId());
			this.requestAgain();
		}
		
	}

*/
}
