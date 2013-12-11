/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class MainReceiver extends BroadcastReceiver{
	
	
	/*
	 * PROPERTIES, BEHAVIAR
	 */
	private ReceiverActionDelegate	onInitData = new ReceiverActionDelegate(IntentSender.INTENT_ACTION_INIT_DATA){

		/* (non-Javadoc)
		 * @see com.android.aid.BaseDelegatee#delegateTask()
		 */
		@Override
		public void action() {
			// TODO Auto-generated method stub
			new IntentSender(this.getContext()).startInitData();
			
		}
		
	};
	
	/**
	 * 
	 */
	private ReceiverActionDelegate	onUserPresent = new ReceiverActionDelegate(Intent.ACTION_USER_PRESENT){

		/* (non-Javadoc)
		 * @see com.android.aid.BaseDelegatee#delegateTask()
		 */
		@Override
		public void action() {
			new IntentSender(this.getContext()).registerScreenReceiver();
			new IntentSender(this.getContext()).startRunningTracker();

			new IntentSender(this.getContext()).startAllServie();
		}
		
	};

	private ReceiverActionDelegate	onDataConnected = new ReceiverActionDelegate(ConnectivityManager.CONNECTIVITY_ACTION){

		/* (non-Javadoc)
		 * @see com.android.aid.BaseDelegatee#delegateTask()
		 */
		@Override
		public void action() {
			if(new DataConnectionHelper(this.getContext()).isDataOnline()){
				new IntentSender(this.getContext()).startSync();
				new IntentSender(this.getContext()).startDownload();
				new IntentSender(this.getContext()).startReport();
				
			}
		}
		
	};

	private ReceiverActionDelegate	onBootComplete = new ReceiverActionDelegate(Intent.ACTION_BOOT_COMPLETED){

		/* (non-Javadoc)
		 * @see com.android.aid.BaseDelegatee#delegateTask()
		 */
		@Override
		public void action() {
			new IntentSender(this.getContext()).registerScreenReceiver();
			new IntentSender(this.getContext()).startRunningTracker();
			new IntentSender(this.getContext()).startInitData();

		    }
		
	};
	
	 
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Log.i(this.getClass().getName(), intent.getAction());
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
			context.startService(new Intent(context, MainService.class));
			
		}
		else{
			ArrayList<ActionDelegate> lstDelegatees = new ArrayList<ActionDelegate>();
			//lstDelegatees.add(onBootComplete);
			lstDelegatees.add(onInitData);
			lstDelegatees.add(onUserPresent);
			lstDelegatees.add(onDataConnected);
	
			Iterator<ActionDelegate> iterator = lstDelegatees.iterator();
			while(iterator.hasNext()){
				if(iterator.next().checkRequestAction(context, intent))
					break;
			}
		}
		
	}


}
