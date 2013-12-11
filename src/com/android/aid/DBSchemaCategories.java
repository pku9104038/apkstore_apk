/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class DBSchemaCategories extends DBSchema{

	private static final String[] COL_GROUP_SERIAL		= {"group_serial",COL_TYPE_INTEGER};
	private static final String[] COL_CATEGORY_SERIAL	= {"category_serial",COL_TYPE_INTEGER};

	private class Columns{
		public int group_serial = 0;
		public int category_serial = 0;
	}
	
	public DBSchemaCategories(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	
	

	/* (non-Javadoc)
	 * @see com.android.aid.DBSchema#makeSchemaStructs()
	 */
	@Override
	protected void makeSchemaStructs() {
		// TODO Auto-generated method stub
		
		lstStructs.add(COL_ROWID);
		lstStructs.add(COL_GROUP_SERIAL);
		lstStructs.add(COL_CATEGORY_SERIAL);
		
	}



	@Override
	protected String getSchemaName() {
		// TODO Auto-generated method stub
		return "categories";
	}

	@Override
	protected String getUpdateSQL(int updateVersion) {
		// TODO Auto-generated method stub
		return null;
	}

}
