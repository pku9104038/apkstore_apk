/**
 * 
 */
package com.android.aid;

import com.android.aid.DBSchemaReport.Columns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DBSchemaTraffic extends DBSchema{
	
	protected static final String COL_NAME_MY_RX					= "my_rx";
	protected static final String COL_NAME_MY_TX					= "my_tx";
	protected static final String COL_NAME_MOBILE_RX				= "mobile_rx";
	protected static final String COL_NAME_MOBILE_TX				= "mobile_tx";
	protected static final String COL_NAME_TOTAL_RX					= "total_rx";
	protected static final String COL_NAME_TOTAL_TX					= "total_tx";
	protected static final String COL_NAME_STAMP					= "stamp";
	protected static final String COL_NAME_DATE						= "date";
	
	protected static final String[] COL_MY_RX				= 
		{COL_NAME_MY_RX,COL_TYPE_INTEGER};
	protected static final String[] COL_MY_TX				= 
		{COL_NAME_MY_TX,COL_TYPE_INTEGER};
	protected static final String[] COL_MOBILE_RX				= 
		{COL_NAME_MOBILE_RX,COL_TYPE_INTEGER};
	protected static final String[] COL_MOBILE_TX				= 
		{COL_NAME_MOBILE_TX,COL_TYPE_INTEGER};
	protected static final String[] COL_TOTAL_RX				= 
		{COL_NAME_TOTAL_RX,COL_TYPE_INTEGER};
	protected static final String[] COL_TOTAL_TX				= 
		{COL_NAME_TOTAL_TX,COL_TYPE_INTEGER};
	
	
	protected static final String[] COL_STAMP				=
		{COL_NAME_STAMP,COL_TYPE_TEXT};
	
	protected static final String[] COL_DATE				=
		{COL_NAME_DATE,COL_TYPE_TEXT};

	
	public class Columns extends DBColumns{
		
		private String stamp;
		private String date;
		private long my_rx;
		private long my_tx;
		private long mobile_rx;
		private long mobile_tx;
		private long total_rx;
		private long total_tx;
		
		public Columns(){
			this.setRowId(0);
			this.mobile_rx = 0;
			this.mobile_tx = 0;
			this.my_rx = 0;
			this.my_tx = 0;
			this.total_rx = 0;
			this.total_tx = 0;
			this.stamp = "";
			this.date = "";
		}
		public String getDate(){	return this.date;	}
		public void setDate(String date){	this.date = date;	}
		
		public String getStamp(){	return this.stamp;	}
		public void setStamp(String stamp){	this.stamp = stamp;	}

		
		public long getMyRx(){	return this.my_rx;	}
		public void setMyRx(long rx){	this.my_rx = rx;	}
		
		public long getMyTx(){	return this.my_tx;	}
		public void setMyTx(long tx){	this.my_tx = tx;	}
		
		public long getMobileRx(){	return this.mobile_rx;	}
		public void setMobileRx(long rx){	this.mobile_rx = rx;	}
		
		public long getMobileTx(){	return this.mobile_tx;	}
		public void setMobileTx(long tx){	this.mobile_tx = tx;	}
		
		public long getTotalRx(){	return this.total_rx;	}
		public void setTotalRx(long rx){	this.total_rx = rx;	}
		
		public long getTotalTx(){	return this.total_tx;	}
		public void setTotalTx(long tx){	this.total_tx = tx;	}
	}
	
	
	private Columns getRow(Cursor cursor){
		Columns row = new Columns();
		row.setRowId(cursor.getInt(cursor.getColumnIndex(COL_ROWID[COL_NAME])));
		row.setMyRx(cursor.getLong(cursor.getColumnIndex(COL_MY_RX[COL_NAME])));
		row.setMyTx(cursor.getLong(cursor.getColumnIndex(COL_MY_TX[COL_NAME])));
		row.setMobileRx(cursor.getLong(cursor.getColumnIndex(COL_MOBILE_RX[COL_NAME])));
		row.setMobileTx(cursor.getLong(cursor.getColumnIndex(COL_MOBILE_TX[COL_NAME])));
		row.setTotalRx(cursor.getLong(cursor.getColumnIndex(COL_TOTAL_RX[COL_NAME])));
		row.setTotalTx(cursor.getLong(cursor.getColumnIndex(COL_TOTAL_TX[COL_NAME])));
		row.setStamp(cursor.getString(cursor.getColumnIndex(COL_STAMP[COL_NAME])));
		row.setDate(cursor.getString(cursor.getColumnIndex(COL_DATE[COL_NAME])));
		
		//Log.i(this.getClass().getName(),"my:"+row.getMyRx()+"/"+row.getMyTx() +
		//		", mobile:"+row.getMobileRx()+"/"+row.getMobileTx() +
		//		", total:"+row.getTotalRx()+"/"+row.getTotalTx());
		
		return row;
	}
	
	public DBSchemaTraffic(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getSchemaName() {
		// TODO Auto-generated method stub
		return "traffic_report";
	}

	@Override
	protected void makeSchemaStructs() {
		// TODO Auto-generated method stub
		lstStructs.add(COL_ROWID);
		lstStructs.add(COL_MY_RX);
		lstStructs.add(COL_MY_TX);
		lstStructs.add(COL_MOBILE_RX);
		lstStructs.add(COL_MOBILE_TX);
		lstStructs.add(COL_DATE);
		lstStructs.add(COL_STAMP);
		lstStructs.add(COL_TOTAL_RX);
		lstStructs.add(COL_TOTAL_TX);
		
	}

	@Override
	protected String getUpdateSQL(int updateVersion) {
		// TODO Auto-generated method stub
		String sql = "";
		
		switch(updateVersion){
		case DBStructs.DB_VERSION_14:
			sql = this.getCreateSQL();
			break;
		}
		return sql;
	}
	
	public  void addTrafficRecord(long my_rx,long my_tx, long mobile_rx, long mobile_tx, long total_rx, long total_tx){
		
		ContentValues values = new ContentValues();
		values.put(COL_MY_RX[COL_NAME], my_rx);
		values.put(COL_MY_TX[COL_NAME], my_tx);
		values.put(COL_MOBILE_RX[COL_NAME], mobile_rx);
		values.put(COL_MOBILE_TX[COL_NAME], mobile_tx);
		values.put(COL_TOTAL_RX[COL_NAME], total_rx);
		values.put(COL_TOTAL_TX[COL_NAME], total_tx);
		values.put(COL_STAMP[COL_NAME], StampHelper.StampToDateTime(System.currentTimeMillis()));
		values.put(COL_DATE[COL_NAME], StampHelper.getToday());
			 	
		resolver.insert(this.getUri(), values);
	}
	
	public  void updateTrafficRecord(long my_rx, long my_tx, long mobile_rx, long mobile_tx, long total_rx, long total_tx ){
		
		Columns row = checkTrafficRecord(StampHelper.getToday());
		if(row != null){
			
			String selection = COL_ROWID[COL_NAME]+ "=" + row.getRowId();
			String[] selectionArgs = null;
			
			ContentValues values = new ContentValues();
			values.put(COL_MY_RX[COL_NAME], row.getMyRx()+my_rx);
			values.put(COL_MY_TX[COL_NAME], row.getMyTx()+my_tx);
			values.put(COL_MOBILE_RX[COL_NAME], row.getMobileRx()+mobile_rx);
			values.put(COL_MOBILE_TX[COL_NAME], row.getMobileTx()+mobile_tx);
			values.put(COL_TOTAL_RX[COL_NAME], row.getTotalRx()+total_rx);
			values.put(COL_TOTAL_TX[COL_NAME], row.getTotalTx()+total_rx);
				 	
			resolver.update(this.getUri(), values, selection, selectionArgs);
			
			
		}
		else{
			addTrafficRecord(my_rx, my_tx,mobile_rx, mobile_tx, total_rx, total_tx);
			
		}
	}
	
	public Columns getTrafficRecord(String date){
		Columns row = null;
		
		String[] projection = this.getProjection();
		String selection = COL_DATE[COL_NAME] + "<=" + "?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = date;
		String sortOrder = COL_ROWID[COL_NAME] + " ASC";
		
		Cursor cursor = resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		//Log.i(this.getClass().getName(), "SQL query: "+this.getUri(1));
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				row = getRow(cursor);
			}
			cursor.close();
		}
		
		return row;
		
	}
	
	public Columns checkTrafficRecord(String date){
		Columns row = null;
		
		String[] projection = this.getProjection();
		String selection = COL_DATE[COL_NAME] + "=" + "?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = date;
		String sortOrder = COL_ROWID[COL_NAME] + " ASC";
		
		Cursor cursor = resolver.query(this.getUri(1),projection, selection, selectionArgs, sortOrder);
		if(cursor!=null){
			int rows = cursor.getCount();
			int columns = cursor.getColumnCount();
			//Log.i(this.getClass().getName(), "SQL query: rows="+rows+", columns="+columns);
			if(rows>0){
				cursor.moveToFirst();
				row = getRow(cursor);
			}
			cursor.close();
		}
		
		return row;
		
	}


}
