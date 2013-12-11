/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class ReqScreenReceiverDelegate extends ServiceActionDelegate{

	public ReqScreenReceiverDelegate(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		MainService.registerScreenReceiver(getContext());
		
	}

}
