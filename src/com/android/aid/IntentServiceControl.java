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
public class IntentServiceControl {
	private Context context;
	
	public IntentServiceControl(Context context) {
		super();
		this.context = context;
	}

	public void startIntentService(int requestExtra){
		Intent intent = new Intent(context,MainIntentService.class);
		intent.putExtra(MainIntentService.REQUEST_EXTRA, requestExtra);
		context.startService(intent);		
	}
	public void startWidgetUpdateIntent(){
		Intent intent = new Intent(context,MainIntentService.class);
		
		context.startService(intent);
	}

}
