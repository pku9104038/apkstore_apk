/**
 * 
 */
package com.android.aid;

import java.io.File;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

/**
 * @author wangpeifeng
 *
 */
public class NotifyHelper {

	private static final int ID_NEW_VERSION_SELF	= 1;
	private static final int ID_BROADCAST_MSG		= 2;
	
	private Context context;
	
	public NotifyHelper(Context context) {
		super();
		this.context = context;
	}



	public  void showNotification(String tickerText, String contentTitle,
			String contentText, int id, int resId){
       
		NotificationManager notificationManager= 
				(NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		
 		Notification notification = new Notification(resId,
				tickerText, System.currentTimeMillis());
                                  //PendingIntent ��ʾ������֪ͨ��intentִ�е�����
 		/*
 		Intent intent = new Intent(context,AppManagerMy.class);
        intent.putExtra(Data.APP_REQUEST, Data.ACTIVITY_MY);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				((Activity)context).getIntent(), 0);
                                  //֪ͨ��ʾ����
		notification.setLatestEventInfo(context, contentTitle, contentText,
				pendingIntent);
*/
                                  //notification.defaults = defaults;����Ϊ�𶯣������⻹������ģ����Ǳ����ڻ���ǰ���á�

                                  //����
		notificationManager.notify(id, notification);

	}
	
	
	public void notifyNewVersion(String label, String vername, String path){
		
		NotificationManager notificationManager= (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		Notification note=new Notification();
		note.flags=Notification.FLAG_AUTO_CANCEL;
		RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notify_newversion);  

		String notes = context.getResources().getString(R.string.notify_newversion);
		//contentView.setTextViewText(R.id.textViewNotificationTime, StampToStringMMddHHmm(System.currentTimeMillis()));  
		contentView.setViewVisibility(R.id.textViewNotificationTime, View.GONE);  
		contentView.setTextViewText(R.id.textViewNotificationNotes, notes+vername);  
		
		contentView.setTextViewText(R.id.textViewNotificationTitle, label);  
		
		note.contentView = contentView;

		note.tickerText=  label + ":" + vername; 

		// note.sound=Uri.parse("file:///sdcard/good.mp3");

		note.icon= R.drawable.ic_notification;
		
		Intent startIntent = new Intent();
		startIntent.setAction(android.content.Intent.ACTION_VIEW);
       
		startIntent.setDataAndType(Uri.fromFile(new File(path)),    
            "application/vnd.android.package-archive");    

		PendingIntent p=PendingIntent.getActivity(context, 0, startIntent , 0);

		note.contentIntent=p;


		notificationManager.notify(ID_NEW_VERSION_SELF, note);		
	}

	
}
