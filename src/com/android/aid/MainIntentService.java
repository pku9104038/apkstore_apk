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
public class MainIntentService extends IntentService{
	
	public static final String REQUEST_EXTRA				= "requestExtra";
	
	public static final int EXTRA_NULL						= 0;
	public static final int EXTRA_WIDGET_UPDATE_INTENT		= 1 + EXTRA_NULL;
	public static final int EXTRA_WIDGET_UPDATE_LABEL		= 1 + EXTRA_WIDGET_UPDATE_INTENT;
	public static final int EXTRA_WIDGET_UPDATE_ICON		= 1 + EXTRA_WIDGET_UPDATE_LABEL;

	public MainIntentService() {
		super("myIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		switch(intent.getIntExtra(REQUEST_EXTRA, EXTRA_NULL)){
		
		case EXTRA_WIDGET_UPDATE_INTENT:
			break;
		
		case EXTRA_WIDGET_UPDATE_LABEL:
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(MainWidget.WIDGET_INTENT_ACTION_UPDATE_LABEL);
			getApplicationContext().sendBroadcast(broadcastIntent);
			break;
		
		case EXTRA_WIDGET_UPDATE_ICON:
			broadcastIntent = new Intent();
			broadcastIntent.setAction(MainWidget.WIDGET_INTENT_ACTION_UPDATE_ICON);
			getApplicationContext().sendBroadcast(broadcastIntent);
			break;
		
		case EXTRA_NULL:
		
			break;
			
		}
		
	}

}
