/**
 * 
 */
package com.android.aid;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class GUIContentObserver extends ContentObserver{

	public GUIContentObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.database.ContentObserver#onChange(boolean)
	 */
	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		//Log.i("test", "onChange!");
		super.onChange(selfChange);
	}

	
}
