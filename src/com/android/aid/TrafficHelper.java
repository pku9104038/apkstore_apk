/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.net.TrafficStats;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class TrafficHelper {
	
	private long my_rx;
	private long my_tx;
	private long mobile_rx;
	private long mobile_tx;
	private long total_rx;
	private long total_tx;	
	
	private boolean reset;
	
	private Context context;
	
	public TrafficHelper(Context context) {
		super();
		this.my_rx = 0;
		this.my_tx = 0;
		this.mobile_rx = 0;
		this.mobile_tx = 0;
		this.total_rx = 0;
		this.total_tx = 0;
		this.reset = false;
		this.context = context;
	}

	private void checkMyTraffic(){
		
		int uid = new PackageHelper(context).getPackageUid("com.android.aid");
		long rx = TrafficStats.getUidRxBytes(uid);
		long tx = TrafficStats.getUidTxBytes(uid);
		
		
		TrafficPreferences pref = new TrafficPreferences(context);
		long rx0 = pref.getMyRx();
		long tx0 = pref.getMyTx();
		
		long rx_delta = 0, tx_delta = 0;
		if(rx<rx0 || tx<tx0){
			this.reset = true;
			rx_delta = rx;
			tx_delta = tx;
		}
		else{
			rx_delta = rx - rx0;
			tx_delta = tx - tx0;
		}
		
		if(rx_delta>0 ){
			my_rx = rx_delta;
		}
		if(tx_delta>0){
			my_tx = tx_delta;
		}
		pref.setMyRx(rx);
		pref.setMyTx(tx);
		Log.i(this.getClass().getName(), "MyRx:"+rx_delta+", MyTx:"+tx_delta);
		
	}

	private void checkMobileTraffic(){
		
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
			mobile_rx = rx_delta;
		}
		if(tx_delta>0){
			mobile_tx = tx_delta;
		}
		pref.setMobileRx(rx);
		pref.setMobileTx(tx);
		
		
		Log.i(this.getClass().getName(), "MobileRx:"+rx_delta+", MobileTx:"+tx_delta);

		
	}
	
	private void checkTotalTraffic(){
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
			total_rx = rx_delta;
		}
		if(tx_delta>0){
			total_tx = tx_delta;
		}
		pref.setTotalRx(rx);
		pref.setTotalTx(tx);

		Log.i(this.getClass().getName(), "TotalRx:"+rx_delta+", TotalTx:"+tx_delta);
		
	}
	
	public void recordTraffic(){
		this.checkMyTraffic();
		this.checkMobileTraffic();
		this.checkTotalTraffic();
		
		DBSchemaTraffic schema = new DBSchemaTraffic(context);
		schema.updateTrafficRecord(my_rx, my_tx, mobile_rx, mobile_tx, total_rx, total_tx);
		
	}


}
