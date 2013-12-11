/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.content.Intent;

/**
 * @author wangpeifeng
 *
 */
public abstract class ReceiverActionDelegate extends ActionDelegate{

	public ReceiverActionDelegate(String requestAction) {
		super(requestAction);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.android.aid.BaseDelegatee#checkRequestAction(android.content.Intent)
	 */
	@Override
	public boolean checkRequestAction(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean bool = false;
		this.intentRequest = intent;
		
		if(intent != null && this.getRequestAction().equals(intent.getAction())){
			this.setContext(context);
			this.action();
			bool = true;
		}
		
		return bool;
	}



}
