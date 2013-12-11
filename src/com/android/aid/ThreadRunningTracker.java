/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.TrafficStats;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ThreadRunningTracker extends ThreadBasic{

	public ThreadRunningTracker(Context context, long timer) {
		super(context, timer);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void actionWithPause() {
		// TODO Auto-generated method stub
		this.recordRunningPackage();
		this.recordOnBoard();
		this.recordOnLine();
		this.recordWLANOnLine();
		this.recordCellOnLine();
		//new IntentSender(context).startReport();
	
		
	}

	@Override
	protected void actionWithoutPaused() {
		// TODO Auto-generated method stub
		//boolean reset = this.recordMyTraffic();
		//this.recordMobileTraffic(reset);
		//this.recordTotalTraffic(reset);
		new TrafficHelper(context).recordTraffic();
	}
	
	private void recordRunningPackage(){
		PackageInfo info =new PackageHelper(context).getRunningPackage();
		String label = (String) info.applicationInfo.loadLabel(context.getPackageManager());
		//Log.i(this.getClass().getName(), "package:"+info.packageName+"@"+label);
		DBSchemaReportRunning schema = new DBSchemaReportRunning(context);
		schema.updateActionRecord(info.packageName, label, info.versionCode,
				DBSchemaReport.VAL_ACTION_PACKAGE_RUNNING);
		
		//Log.i(this.getClass().getName(), "running:"+info.packageName + ",v:"+info.versionCode);
	}
	
	private void recordOnBoard(){
		DBSchemaReportOnBoard schema = new DBSchemaReportOnBoard(context);
		schema.updateActionRecord(DBSchemaReport.VAL_ACTION_SELF_ONBOARD);
	}

	private void recordOnLine(){
		if(new DataConnectionHelper(context).isDataOnline()){
			DBSchemaReportOnline schema = new DBSchemaReportOnline(context);
			schema.updateActionRecord(DBSchemaReport.VAL_ACTION_SELF_ONLINE);
		}
	}
	
	private void recordWLANOnLine(){
		if(new DataConnectionHelper(context).isWLANOnline()){
			DBSchemaReportWLAN schema = new DBSchemaReportWLAN(context);
			schema.updateActionRecord(DBSchemaReport.VAL_ACTION_SELF_WLAN_ONLINE);
		}
	}

	private void recordCellOnLine(){
		if(new DataConnectionHelper(context).isMobileOnline()){
			DBSchemaReportCellData schema = new DBSchemaReportCellData(context);
			schema.updateActionRecord(DBSchemaReport.VAL_ACTION_SELF_CELL_ONLINE);
		}
	}
	
	private boolean recordMyTraffic(){
		boolean reset = false;
		
		int uid = new PackageHelper(context).getPackageUid("com.android.aid");
		long rx = TrafficStats.getUidRxBytes(uid);
		long tx = TrafficStats.getUidTxBytes(uid);
		
		TrafficPreferences pref = new TrafficPreferences(context);
		long rx0 = pref.getMyRx();
		long tx0 = pref.getMyTx();
		
		long rx_delta = 0, tx_delta = 0;
		if(rx<rx0 || tx<tx0){
			reset = true;
			rx_delta = rx;
			tx_delta = tx;
		}
		else{
			rx_delta = rx - rx0;
			tx_delta = tx - tx0;
		}
		
		if(rx_delta>0 ){
			
		}
		if(tx_delta>0){
			
		}
		pref.setMyRx(rx);
		pref.setMyTx(tx);
		Log.i(this.getClass().getName(), "MyRx:"+rx_delta+", MyTx:"+tx_delta);
		return reset;
	}

	private void recordMobileTraffic(boolean reset){
		
		long rx = TrafficStats.getMobileRxBytes();
		long tx = TrafficStats.getMobileTxBytes();
		
		TrafficPreferences pref = new TrafficPreferences(context);
		long rx0 = pref.getMobileRx();
		long tx0 = pref.getMobileTx();
		
		long rx_delta = 0, tx_delta = 0;
		
		if(reset){
			rx_delta = rx;
			tx_delta = tx;
		}
		else{
			if(rx<rx0 || tx<tx0){
				rx_delta = rx;
				tx_delta = tx;
			}
			else{
				rx_delta = rx - rx0;
				tx_delta = tx - tx0;
			}
		}
		
		if(rx_delta>0 ){
			
		}
		if(tx_delta>0){
			
		}
		pref.setMobileRx(rx);
		pref.setMobileTx(tx);
		
		
		Log.i(this.getClass().getName(), "MobileRx:"+rx_delta+", MobileTx:"+tx_delta);

		
	}
	
	private void recordTotalTraffic(boolean reset){
		long rx = TrafficStats.getTotalRxBytes();
		long tx = TrafficStats.getTotalTxBytes();
		TrafficPreferences pref = new TrafficPreferences(context);
		long rx0 = pref.getTotalRx();
		long tx0 = pref.getTotalTx();
		
		long rx_delta = 0, tx_delta = 0;
		
		if(reset){
			rx_delta = rx;
			tx_delta = tx;
		}
		else{
			if(rx<rx0 || tx<tx0){
				rx_delta = rx;
				tx_delta = tx;
			}
			else{
				rx_delta = rx - rx0;
				tx_delta = tx - tx0;
			}
		}
		
		if(rx_delta>0 ){
			
		}
		if(tx_delta>0){
			
		}
		pref.setTotalRx(rx);
		pref.setTotalTx(tx);

		Log.i(this.getClass().getName(), "TotalRx:"+rx_delta+", TotalTx:"+tx_delta);
		
	}

}
