/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class DBSchemaReportCellData extends DBSchemaReport{

	public DBSchemaReportCellData(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getSchemaName() {
		// TODO Auto-generated method stub
		return "cell_data_report";
	}

	@Override
	protected String getUpdateSQL(int updateVersion) {
		// TODO Auto-generated method stub
		return null;
	}

}
