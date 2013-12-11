/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public abstract class ServiceActionDelegate extends ActionDelegate {
	
	
	/*
	 * PROPERTIES,PRIVATE
	 */
	
	public ServiceActionDelegate(Context context, String requestAction) {
		super(requestAction);
		this.context = context;
		
		// TODO Auto-generated constructor stub
	}

	
	/* (non-Javadoc)
	 * @see com.android.aid.BaseDelegatee#checkRequestAction(android.content.Intent)
	 */
	@Override
	public boolean checkRequestAction(Context context, Intent intent) {
		// TODO Auto-generated method stub
		intentRequest = intent;
		boolean bool = false;
		
		if( intent != null && intent.getBooleanExtra(this.getRequestAction(), false)){
			this.setContext(context);
			//this.action();
			Log.i("ServiceRequest",this.getRequestAction());
			try{
				thread.start();
				//action();
				bool = true;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return bool;
	}
	
	protected Thread thread = new Thread(){

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				super.run();
				action();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	};
	
	protected void requestAgain(){
		Intent intent = new Intent(context,MainService.class);
		intent.putExtra(this.requestAction, true);
		
		context.startService(intent);
	}


}
