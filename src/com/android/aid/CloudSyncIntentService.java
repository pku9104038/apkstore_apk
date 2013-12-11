/**
 * 
 */
package com.android.aid;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author wangpeifeng
 *
 */
public class CloudSyncIntentService extends IntentService{

	public static final String REQUEST_EXTRA				= "requestExtra";
	
	public static final int EXTRA_NULL						= 0;
	public static final int EXTRA_APP_SYNC_INTENT			= 1 + EXTRA_NULL;

	public CloudSyncIntentService() {
		super("cloudSyncIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		switch(intent.getIntExtra(REQUEST_EXTRA, EXTRA_NULL)){
		case EXTRA_APP_SYNC_INTENT:
			
			break;
			
		}		
	}

}
