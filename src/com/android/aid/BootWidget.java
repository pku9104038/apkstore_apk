/**
 * 
 */
package com.android.aid;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author wangpeifeng
 *
 */
public class BootWidget extends AppWidgetProvider{

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		for(int id = 0; id < appWidgetIds.length; id++){
			final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_boot);
			final Intent intent = new Intent(context,WelcomeActivity.class);
		    final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, 
		        		intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        remoteViews.setOnClickPendingIntent(R.id.textViewWidgetBoot, pendingIntent);
	        appWidgetManager.updateAppWidget(appWidgetIds[id], remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
