/**
 * 
 */
package com.android.aid;

/**
 * @author wangpeifeng
 *
 */
public class DBColumns {
	
	protected int rowid;
	
	
	public DBColumns(){
		rowid = 0;
	}
	
	public void setRowId(int id){
		rowid = id;
	}
	
	public int getRowId(){
		return rowid;
	}

}
