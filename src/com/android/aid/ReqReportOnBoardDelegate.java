/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.android.aid.DBSchemaReport.Columns;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class ReqReportOnBoardDelegate extends ReqReportDelegate {

	public ReqReportOnBoardDelegate(Context context, String requestAction,
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
		return null;
	}

	@Override
	protected void apiResponseAction(String response) {
		// TODO Auto-generated method stub
		
	}
*/
}
