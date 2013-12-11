/**
 * 
 */
package com.android.aid;

import com.android.aid.DBSchemaReport.Columns;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class ReqReportInstallDelegate extends ReqReportDelegate {

	public ReqReportInstallDelegate(Context context, String requestAction,
			DBSchemaReport schema, String web_api_key) {
		super(context, requestAction, schema, web_api_key);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Columns getNextRecord() {
		// TODO Auto-generated method stub
		return schema.getActionRecord();
	}

}
