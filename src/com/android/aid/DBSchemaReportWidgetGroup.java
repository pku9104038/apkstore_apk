/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class DBSchemaReportWidgetGroup extends DBSchemaReport{

	public DBSchemaReportWidgetGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getSchemaName() {
		// TODO Auto-generated method stub
		return "widget_group_report";
	}

	@Override
	protected String getUpdateSQL(int updateVersion) {
		// TODO Auto-generated method stub
		return null;
	}

}
