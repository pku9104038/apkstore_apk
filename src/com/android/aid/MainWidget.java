/**
 * 
 */
package com.android.aid;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author wangpeifeng
 *
 */
public class MainWidget extends AppWidgetProvider{
	
	/*
	 * CONSTANTS, PUBLIC
	 */
	public static final String WIDGET_INTENT_ACTION_UPDATE_LABEL	= "com.android.aid.widget.action.update_label";	
	public static final String WIDGET_INTENT_ACTION_UPDATE_ICON		= "com.android.aid.widget.action.update_icon";
	
	/*
	 * PROPERTRIES, PRIVATE 
	 */
    private static HandlerThread sWorkerThread;
    private static Handler sWorkerQueue;
    
    public MainWidget() {
        // Start the worker thread
        sWorkerThread = new HandlerThread("WeatherWidgetProvider-worker");
        sWorkerThread.start();
        sWorkerQueue = new Handler(sWorkerThread.getLooper());
    }

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        final String action = intent.getAction();
        if (action.equals(WIDGET_INTENT_ACTION_UPDATE_LABEL)) {
            // BroadcastReceivers have a limited amount of time to do work, so for this sample, we
            // are triggering an update of the data on another thread.  In practice, this update
            // can be triggered from a background service, or perhaps as a result of user actions
            // inside the main application.
            final Context mContext = context;
            sWorkerQueue.removeMessages(0);
            sWorkerQueue.post(new Runnable() {
                @Override
                public void run() {
              
                	final AppWidgetManager mgr = AppWidgetManager.getInstance(mContext);
                    final ComponentName cn = new ComponentName(mContext, MainWidget.class);
                    final int[] appWidgetIds = mgr.getAppWidgetIds(cn);
                              	
            		for(int id = 0; id < appWidgetIds.length; id++){
            			
            			final RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_main);
            			int size = GroupPreferences.GROUP_PRIORITY_MAX - GroupPreferences.GROUP_PRIORITY_MIN +1;
            			GroupPreferences groupPref = new GroupPreferences(mContext);
            			
            		    for(int i=GroupPreferences.GROUP_PRIORITY_MIN;i<=size;i++){
            		    	final int vid = groupPref.getGroupTextViewId(i);
            		    	final String name = groupPref.getGroupNameByPriority(i);
            		    	remoteViews.setTextViewText(vid, name);
            		    }
            		    mgr.updateAppWidget(appWidgetIds[id], remoteViews);
            	        //Log.i("MainWidget","updateAppWidget Label:"+appWidgetIds[id]+"@"+remoteViews.toString());
            		}
                }
            });
        } else if (action.equals(WIDGET_INTENT_ACTION_UPDATE_ICON)) {
            final Context mContext = context;
            sWorkerQueue.removeMessages(0);
            sWorkerQueue.post(new Runnable() {
                @Override
                public void run() {
                	
                	final AppWidgetManager mgr = AppWidgetManager.getInstance(mContext);
                    final ComponentName cn = new ComponentName(mContext, MainWidget.class);
                    final int[] appWidgetIds = mgr.getAppWidgetIds(cn);
                              	
            		for(int id = 0; id < appWidgetIds.length; id++){
            			
            			final RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_main);
            			int size = GroupPreferences.GROUP_PRIORITY_MAX - GroupPreferences.GROUP_PRIORITY_MIN +1;
            			GroupPreferences groupPref = new GroupPreferences(mContext);
            			
            		    for(int i=GroupPreferences.GROUP_PRIORITY_MIN;i<=size;i++){
            		    	final int vid = groupPref.getGroupImageViewId(i);
            		    	final Bitmap bmp = groupPref.getGroupBitmapByPriority(i);
            		    	//remoteViews.setImageViewBitmap(vid, bmp);
            		    	//mgr.updateAppWidget(appWidgetIds[id], remoteViews);
                 	       // Log.i("MainWidget","updateAppWidget Label:"+appWidgetIds[id]+"@"+remoteViews.toString());

            		    }
            		    
            		}
                 }
            });
         }
		
		super.onReceive(context, intent);
		Log.i(this.getClass().getName(), intent.getAction());
		
	}
	

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		for(int id = 0; id < appWidgetIds.length; id++){
			
			final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);
			int size = GroupPreferences.GROUP_PRIORITY_MAX - GroupPreferences.GROUP_PRIORITY_MIN +1;
			GroupPreferences groupPref = new GroupPreferences(context);
			
		    for(int i=GroupPreferences.GROUP_PRIORITY_MIN;i<=size;i++){
		    	final Intent intent = new Intent(context,GroupActivity.class);
		        intent.putExtra(GroupActivity.GROUP_REQUEST, i);
		        intent.putExtra(GroupActivity.GROUP_CALLER, GroupActivity.GROUP_CALLER_WIDGET);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        final PendingIntent pendingIntent = PendingIntent.getActivity(context, i, 
		        		intent, PendingIntent.FLAG_UPDATE_CURRENT);
	            int vid = groupPref.getGroupLayoutId(i);
	            remoteViews.setOnClickPendingIntent(vid, pendingIntent);
	        }
		    appWidgetManager.updateAppWidget(appWidgetIds[id], remoteViews);
	        Log.i("MainWidget","updateAppWidget:"+appWidgetIds[id]+"@"+remoteViews.toString());
			
		}
		new IntentServiceControl(context).startIntentService(MainIntentService.EXTRA_WIDGET_UPDATE_LABEL);
		new IntentServiceControl(context).startIntentService(MainIntentService.EXTRA_WIDGET_UPDATE_ICON);
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
	}
	

	
}
