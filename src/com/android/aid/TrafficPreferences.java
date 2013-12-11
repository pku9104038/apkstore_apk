/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class TrafficPreferences extends MainSharedPref{

	public static final String PREF_KEY_MY_RX 				= "my_rx";
	public static final String PREF_KEY_MY_TX 				= "my_tx";
	
	public static final String PREF_KEY_MOBILE_RX 			= "mobile_rx";
	public static final String PREF_KEY_MOBILE_TX 			= "mobile_tx";
	
	public static final String PREF_KEY_TOTAL_RX 			= "total_rx";
	public static final String PREF_KEY_TOTAL_TX 			= "total_tx";
	
	//private static final String VAL_DEF_LONG_ZERO			= "0";
	public TrafficPreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "traffic_pref";
	}
	
	public void setMyRx(long rx){
		putLong(PREF_KEY_MY_RX, rx);
	}

	public long getMyRx(){
		return getLong(PREF_KEY_MY_RX,VAL_DEF_LONG_ZERO);
	}

	public void setMyTx(long tx){
		putLong(PREF_KEY_MY_TX, tx);
	}

	public long getMyTx(){
		return getLong(PREF_KEY_MY_TX,VAL_DEF_LONG_ZERO);
	}

	public void setMobileRx(long rx){
		putLong(PREF_KEY_MOBILE_RX, rx);
	}

	public long getMobileRx(){
		return getLong(PREF_KEY_MOBILE_RX,VAL_DEF_LONG_ZERO);
	}

	public void setMobileTx(long tx){
		putLong(PREF_KEY_MOBILE_TX, tx);
	}

	public long getMobileTx(){
		return getLong(PREF_KEY_MOBILE_TX,VAL_DEF_LONG_ZERO);
	}

	public void setTotalRx(long rx){
		putLong(PREF_KEY_TOTAL_RX, rx);
	}

	public long getTotalRx(){
		return getLong(PREF_KEY_TOTAL_RX,VAL_DEF_LONG_ZERO);
	}

	public void setTotalTx(long tx){
		putLong(PREF_KEY_TOTAL_TX, tx);
	}

	public long getTotalTx(){
		return getLong(PREF_KEY_TOTAL_TX,VAL_DEF_LONG_ZERO);
	}
	
}
