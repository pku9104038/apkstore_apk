/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class ReqRunningTrackerDelegate extends ServiceActionDelegate {
	
	public static final String EXTRA_RUNNING_TRACKER		= "running_tracker";

	public ReqRunningTrackerDelegate(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		if(intentRequest.getBooleanExtra(EXTRA_RUNNING_TRACKER, false)){
			MainService.startRunningTracker(context);
		}
		else{
			MainService.pauseRunnigTracker();
		}
		
	}

}
