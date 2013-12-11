/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.android.aid.DBSchemaApplications.Columns;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.immersion.uhl.Launcher;

/**
 * @author wangpeifeng
 *
 */
public class GroupActivity extends Activity {
	
	/*
	 * CONSTANTS, PUBLIC
	 */
	public static final String GROUP_REQUEST			= "group_request";
	public static final String GROUP_CALLER				= "group_caller";
	
	public static final int GROUP_CALLER_WIDGET			= DBSchemaReport.VAL_ACTION_SELF_WIDGET_GROUP;
	public static final int GROUP_CALLER_APP			= DBSchemaReport.VAL_ACTION_SELF_APP_GROUP;
	
	
	/*
	 * CONSTANTS, PRIVATE
	 */
	private static final int HANDLER_MSG_RREFRESHING	= 1;
	private static final int HANDLER_MSG_RREFRESHED		= 1 + HANDLER_MSG_RREFRESHING;
	private static final int HANDLER_MSG_RREFRESH		= 1 + HANDLER_MSG_RREFRESHED;
	private static final int HANDLER_MSG_TOAST			= 1 + HANDLER_MSG_RREFRESH;
	private static final int HANDLER_MSG_START_WAITING	= 1 + HANDLER_MSG_TOAST;
	private static final int HANDLER_MSG_STOP_WAITING	= 1 + HANDLER_MSG_START_WAITING;
	private static final int HANDLER_MSG_DATAINIT		= 1 + HANDLER_MSG_STOP_WAITING;
	private static final int HANDLER_MSG_APK_REFRESH	= 1 + HANDLER_MSG_DATAINIT;
	
	private static final int HANDLER_DATA_LEFT			= 1;
	private static final int HANDLER_DATA_RIGHT			= 2;
	
	private static final int ACTIVITY_DOWNLOADING		= 1;
	private static final int ACTIVITY_ITEMLONG			= 1 + ACTIVITY_DOWNLOADING;
	private static final int ACTIVITY_DATAINIT			= 1 + ACTIVITY_ITEMLONG;
	private static final int ACTIVITY_CLOUD				= 1 + ACTIVITY_DATAINIT;
	private static final int ACTIVITY_UPGRADE			= 1 + ACTIVITY_CLOUD;
	private static final int ACTIVITY_STORAGE			= 1 + ACTIVITY_UPGRADE;
	private static final int ACTIVITY_INSTALLED			= 1 + ACTIVITY_STORAGE;

	
	private static final long DEBOUNCE_GAP				= 100;
	private static final long REFRESHING_TIMER			= 3000;
	private static final long SLEEP_TIMER				= 500;
	
	/*
	 * PROPERTRIES, PRIVATE 
	 */
	private Context context;
	private int priority;
	private static int current_group;
	private int id_focus = 0;
	private TextView tvTitle, tvLeft, tvRight;
	private ImageButton ibtnSearch, ibtnLeft, ibtnRight, ibtnMy;
	private RelativeLayout lytLeft,lytRight;
	private GridView gridView, gridViewOut, gridViewIn;
	private ArrayList<DBSchemaApplications.Columns> lstApps;
	private ArrayList<DBSchemaApplications.Columns> lstAppsRefresh;
	private ArrayList<DBSchemaApplications.Columns>[] lstBuffers;
	private GroupGridAdapter adapter, adapterIn, adapterOut;
	
	
	private Set<GroupGridAdapter.IconInfo> setNoIcons, setWaitingIcons, setNoIconsIn,setWaitingIconsIn,
		setNoIconsOut,setWaitingIconsOut;
	
	private int group_serial;
	private ProgressBar progressBar;
	private boolean refreshing, autorefreshing, group_switching;
	private int group_switching_btn;
	private String title, left, right;
	private int screen_width;
	private ImageView ivWallpapaer, flying_item;
	private Bitmap wallpaperBitmap;
	private Animation animationFlying, animationLeftIn,animationRightOut, 
				animationLeftOut, animationRightIn, animationIn, animationOut;
	
	private String file_from_downloading;
	private int app_serial_downloading;
	private int position_downloading;
	private boolean outoff_activity;
	private DBSchemaApplications.Columns columns_downloading;
	
	private DBSchemaApplications schemaApp;
	private DBSchemaIconDownload schemaIcon;
	private GroupPreferences groupPref;
	private RuntimePreferences runtimePref;
	private IntentSender intentSender; 
	
	private RefreshThread refreshThread;
	private GroupThread groupThread;
	
	private boolean new_icon_flag;
	
	private long debounce, refreshing_stamp;
	
	private Launcher mLauncher;
	
	
	private class GuiUpdateDelegate extends ServiceActionDelegate{

		public GuiUpdateDelegate(Context context, String requestAction) {
			super(context, requestAction);
			// TODO Auto-generated constructor stub
		}

		
		/* (non-Javadoc)
		 * @see com.android.aid.ServiceActionDelegate#checkRequestAction(android.content.Context, android.content.Intent)
		 */
		@Override
		public boolean checkRequestAction(Context context, Intent intent) {
			// TODO Auto-generated method stub
//			return super.checkRequestAction(context, intent);
			intentRequest = intent;
			boolean bool = false;
			
			if( intent != null && intent.getBooleanExtra(this.getRequestAction(), false)){
				this.setContext(context);
				this.action();
				bool = true;
			}
			
			return bool;

		}


		@Override
		public void action() {
			// TODO Auto-generated method stub
		       handler.sendEmptyMessage(HANDLER_MSG_APK_REFRESH);
		}
		
	}
	
	private GuiUpdateDelegate guiUpdateDelegate;
	/*
	 * PROPERTIES, PUBLIC
	 */
	
	/*
	 * PROPERTIES, PROTECTED
	 */
	
	
	/*
	 * PROPERTIES, BEHAVIAR
	 */
	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			
			case HANDLER_MSG_APK_REFRESH:
				
				refreshing = true;
				
				refreshContentData(false);
				lstApps = lstAppsRefresh;
		        adapter.setListItems(lstApps);
		        adapter.notifyDataSetChanged();
				gridView.invalidate();
				
				//lytRight.setBackgroundResource(R.drawable.bg_title);
				//lytLeft.setBackgroundResource(R.drawable.bg_title);
				lytLeft.setClickable(true);
				lytRight.setClickable(true);
				tvLeft.setTextColor(getResources().getColor(R.color.tab_label_normal));
				tvRight.setTextColor(getResources().getColor(R.color.tab_label_normal));
				
				group_switching_btn = 0;
				
				refreshing = false;
				
				if(autorefreshing){
					autorefreshing = false;
					handler.sendEmptyMessage(HANDLER_MSG_RREFRESHING);
				}
				break;
			case HANDLER_MSG_RREFRESHING:
				if(!refreshing){
				refreshing = true;
				
				//if(msg.getData().getInt("button")==HANDLER_DATA_LEFT){
				if(group_switching_btn ==HANDLER_DATA_LEFT){
						tvLeft.setTextColor(getResources().getColor(R.color.tab_label_pressed));
						
				}
				//else if(msg.getData().getInt("button")==HANDLER_DATA_RIGHT){
				else if(group_switching_btn == HANDLER_DATA_RIGHT){
						tvRight.setTextColor(getResources().getColor(R.color.tab_label_pressed));
						
				}
				lytLeft.setClickable(false);
				lytRight.setClickable(false);
				
				//tvLeft.setVisibility(View.GONE);
				//tvRight.setVisibility(View.GONE);
				
				//progressBar.setVisibility(View.VISIBLE);
				//gridView.setVisibility(View.GONE);
				
				
				//refreshContentData(false);
				
				//handler.sendEmptyMessage(HANDLER_MSG_RREFRESHED);
				
				
				//GroupThread thread = new GroupThread();
				//thread.start();
				//Log.i(this.getClass().getName(),"Thread.State:"+Thread.State.TERMINATED);
				
		    	if(groupThread.getState().equals(Thread.State.NEW)){
		        	
		    		groupThread.start();
		    	
		    	}
		    	else if(groupThread.getState().equals(Thread.State.TIMED_WAITING)){
		    		
		    		groupThread.interrupt();
		    	
		    	}
		    	else{
		    	}
		    	//refreshing = false;
		    	break;
				
				}
				else{
					break;
				}
				//break;
			case HANDLER_MSG_RREFRESHED:
				refreshContentView();
				//lytLeft.setBackgroundResource(R.drawable.bg_title);
				//lytLeft.setClickable(true);
				//lytRight.setBackgroundResource(R.drawable.bg_title);
				//lytRight.setClickable(true);
				lytLeft.setClickable(true);
				lytRight.setClickable(true);
				tvLeft.setTextColor(getResources().getColor(R.color.tab_label_normal));
				tvRight.setTextColor(getResources().getColor(R.color.tab_label_normal));
				group_switching_btn = 0;
				
				//progressBar.setVisibility(View.GONE);
				//gridView.setVisibility(View.VISIBLE);
				refreshing = false;
				if(autorefreshing){
					autorefreshing = false;
					handler.sendEmptyMessage(HANDLER_MSG_RREFRESHING);
				}
				break;
			case HANDLER_MSG_RREFRESH:
				adapter.notifyDataSetChanged();
		    	gridView.invalidate();
		        
				break;
			case HANDLER_MSG_DATAINIT:
				refreshContentData(false);
				//lstAppsRefresh = new DBSchemaApplications(context).getApplications(group_serial,false);
		        lstApps = lstAppsRefresh;
		        adapter.setListItems(lstApps);
		        
				setTextViews();
		        adapter.notifyDataSetChanged();
		    	gridView.invalidate();
		        
				break;
			case HANDLER_MSG_TOAST:
				String toast = msg.getData().getString("toast");
				Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
				break;
				
			case HANDLER_MSG_START_WAITING:
				progressBar.setVisibility(View.VISIBLE);
				break;
				
			case HANDLER_MSG_STOP_WAITING:
				progressBar.setVisibility(View.GONE);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	private OnClickListener lsrGroupIcon = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(System.currentTimeMillis() - debounce > DEBOUNCE_GAP){
				debounce = System.currentTimeMillis();
				try {
			    	mLauncher.play(Launcher.BOUNCE_100);		    		    
			    } catch (Exception e) {
			    	Log.e(this.getClass().getName(),"Error: " + e.getMessage());
			    }
	
				int position = (Integer) v.getTag();
				DBSchemaApplications.Columns columns = (DBSchemaApplications.Columns) adapter.getItem(position);
				
				switch(columns.getAppLocation()){
				case DBSchemaApplications.VAL_LOCATION_CLOUD:
					
					/*
					new DBSchemaDownloadApk(context).addDownloadFile(columns.getAppSerial(),
							columns.getAppPackage(),columns.getAppVerCode(), columns.getAppApkFile());
					//new IntentSender(context).startApkDownload();
					
					new DBSchemaApplications(context).updateAppLocation(columns.getAppSerial(), DBSchemaApplications.VAL_LOCATION_DOWNLOADING);
					
					new DBSchemaReportDownload(context).addActionRecord(
							columns.getAppPackage(),
							columns.getAppVerCode(),
							DBSchemaReport.VAL_ACTION_DOWNLOAD_START);
					
					columns.setAppLocation(DBSchemaApplications.VAL_LOCATION_DOWNLOADING);
					handler.sendEmptyMessage(HANDLER_MSG_RREFRESH);
					*/
					Intent intent = new Intent(context,GroupDownloadActivity.class);
					intent.putExtra(GroupDownloadActivity.EXTRA_LABEL, columns.getAppLabel());
					intent.putExtra(GroupDownloadActivity.EXTRA_FILE_FROM, columns.getAppApkFile());
					intent.putExtra(GroupDownloadActivity.EXTRA_PACKAGE, columns.getAppPackage());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERSION, columns.getAppVerName());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERCODE, columns.getAppVerCode());
					intent.putExtra(GroupDownloadActivity.EXTRA_SERIAL, columns.getAppSerial());
					intent.putExtra(GroupDownloadActivity.EXTRA_REQUEST, GroupDownloadActivity.EXTRA_REQUEST_CLOUD);
					file_from_downloading = columns.getAppApkFile();
					app_serial_downloading = columns.getAppSerial();
					position_downloading = position;
					columns_downloading = columns;
					((Activity) context).startActivityForResult(intent,ACTIVITY_CLOUD);
					
					//new IntentSender(context).startApkDownload();
					
					break;
					
				case DBSchemaApplications.VAL_LOCATION_DOWNLOADING:
					intent = new Intent(context,GroupDownloadActivity.class);
					intent.putExtra(GroupDownloadActivity.EXTRA_LABEL, columns.getAppLabel());
					intent.putExtra(GroupDownloadActivity.EXTRA_FILE_FROM, columns.getAppApkFile());
					intent.putExtra(GroupDownloadActivity.EXTRA_PACKAGE, columns.getAppPackage());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERSION, columns.getAppVerName());
					intent.putExtra(GroupDownloadActivity.EXTRA_REQUEST, GroupDownloadActivity.EXTRA_REQUEST_DOWNLOADING);
					file_from_downloading = columns.getAppApkFile();
					app_serial_downloading = columns.getAppSerial();
					position_downloading = position;
					columns_downloading = columns;
					((Activity) context).startActivityForResult(intent,ACTIVITY_DOWNLOADING);
					
					new IntentSender(context).startApkDownload();
					
					break;
					
				case DBSchemaApplications.VAL_LOCATION_STORAGE:
					/*
					intent = new Intent(context,GroupDownloadActivity.class);
					intent.putExtra(GroupDownloadActivity.EXTRA_LABEL, columns.getAppLabel());
					intent.putExtra(GroupDownloadActivity.EXTRA_FILE_FROM, columns.getAppApkFile());
					intent.putExtra(GroupDownloadActivity.EXTRA_PACKAGE, columns.getAppPackage());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERSION, columns.getAppVerName());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERCODE, columns.getAppVerCode());
					intent.putExtra(GroupDownloadActivity.EXTRA_SERIAL, columns.getAppSerial());
					intent.putExtra(GroupDownloadActivity.EXTRA_REQUEST, GroupDownloadActivity.EXTRA_REQUEST_STORAGE);
					file_from_downloading = columns.getAppApkFile();
					app_serial_downloading = columns.getAppSerial();
					position_downloading = position;
					columns_downloading = columns;
					((Activity) context).startActivityForResult(intent,ACTIVITY_STORAGE);
					*/
					
					String path = new ApkFileHelper(context).getApkFile(columns.getAppPackage());
					if(path !=null ){
						new IntentSender(context).startApkInstall(path);
					}
					
					break;
	
				case DBSchemaApplications.VAL_LOCATION_INSTALLED:
					/*
					intent = new Intent(context,GroupDownloadActivity.class);
					intent.putExtra(GroupDownloadActivity.EXTRA_LABEL, columns.getAppLabel());
					intent.putExtra(GroupDownloadActivity.EXTRA_FILE_FROM, columns.getAppApkFile());
					intent.putExtra(GroupDownloadActivity.EXTRA_PACKAGE, columns.getAppPackage());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERSION, columns.getAppVerName());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERCODE, columns.getAppVerCode());
					intent.putExtra(GroupDownloadActivity.EXTRA_SERIAL, columns.getAppSerial());
					intent.putExtra(GroupDownloadActivity.EXTRA_REQUEST, GroupDownloadActivity.EXTRA_REQUEST_INSTALLED);
					file_from_downloading = columns.getAppApkFile();
					app_serial_downloading = columns.getAppSerial();
					position_downloading = position;
					columns_downloading = columns;
					((Activity) context).startActivityForResult(intent,ACTIVITY_INSTALLED);
					break;
					*/
					
					switch(v.getId()){
					case R.id.imageViewIcon:
						String packageName = columns.getAppPackage();
						if(context.getPackageName().equals(packageName)){

							intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
							intent.setType("text/plain"); // 分享发送的数据类型
							//intent.setPackage(packAgeName);
							Resources res = context.getResources();
							
							intent.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.share_title)); // 分享的主题
							intent.putExtra(Intent.EXTRA_TEXT,createShareText(columns.getAppLabel(),columns.getAppApkFile()) 
							/*		
									"我通过"+
									" 蒲公英应用吧 ("+
									"http://www.pu-up.com/Downloads/ApkStore/download/apk/ApkStore.apk"+
									") "+
									"与您分享一个很棒的应用："+
									columns.getAppLabel()+
									" (" +
									new DownloadPreferences(context).getServerUrl()+
									StoragePreferences.getApkPath()+
									"/"+
									columns.getAppApkFile()+
									") "
							*/		
									); // 分享的内容
							context.startActivity(Intent.createChooser(intent, res.getString(R.string.share_intent)));// 目标应用选择对话框的标题 
						}
						else{
							Intent startIntent = new Intent();
							startIntent = getPackageManager().getLaunchIntentForPackage(    
						                packageName);  
							((Activity) context).startActivityForResult(startIntent,0);	
							
							new DBSchemaReportLaunch(context).addActionRecord(packageName, 
									new PackageHelper(context).getPackageVerCode(packageName), 
									DBSchemaReport.VAL_ACTION_PACKAGE_LAUNCH);	
						}
						break;
						
					case R.id.imageViewMarker:
						int newVercode = columns.getAppVerCode();
						int nowVercode = new PackageHelper(context).getPackageVerCode(columns.getAppPackage());
						if(newVercode > nowVercode){
							intent = new Intent(context,GroupDownloadActivity.class);
							intent.putExtra(GroupDownloadActivity.EXTRA_LABEL, columns.getAppLabel());
							intent.putExtra(GroupDownloadActivity.EXTRA_FILE_FROM, columns.getAppApkFile());
							intent.putExtra(GroupDownloadActivity.EXTRA_PACKAGE, columns.getAppPackage());
							intent.putExtra(GroupDownloadActivity.EXTRA_VERSION, columns.getAppVerName());
							intent.putExtra(GroupDownloadActivity.EXTRA_VERCODE, columns.getAppVerCode());
							intent.putExtra(GroupDownloadActivity.EXTRA_SERIAL, columns.getAppSerial());
							intent.putExtra(GroupDownloadActivity.EXTRA_REQUEST, GroupDownloadActivity.EXTRA_REQUEST_CLOUD);
							file_from_downloading = columns.getAppApkFile();
							app_serial_downloading = columns.getAppSerial();
							position_downloading = position;
							columns_downloading = columns;
							((Activity) context).startActivityForResult(intent,ACTIVITY_UPGRADE);
						}
						else{
							
							intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
							intent.setType("text/plain"); // 分享发送的数据类型
							//intent.setPackage(packAgeName);
							Resources res = context.getResources();
							
							intent.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.share_title)); // 分享的主题
							intent.putExtra(Intent.EXTRA_TEXT,createShareText(columns.getAppLabel(),columns.getAppApkFile()) 
							/*		
									"我通过"+
									" 蒲公英应用吧 ("+
									"http://www.pu-up.com/Downloads/ApkStore/download/apk/ApkStore.apk"+
									") "+
									"与您分享一个很棒的应用："+
									columns.getAppLabel()+
									" (" +
									new DownloadPreferences(context).getServerUrl()+
									StoragePreferences.getApkPath()+
									"/"+
									columns.getAppApkFile()+
									") "
							*/		
									); // 分享的内容
							context.startActivity(Intent.createChooser(intent, res.getString(R.string.share_intent)));// 目标应用选择对话框的标题 

						}
						
							
						break;
					}
						
					//new IntentSender(context).startReport();
					
				}
			
			}
						
		}
		
	};
	
	private OnClickListener lsrButton = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.textViewTitle:
				Intent intent = new Intent();
				intent.putExtra(GROUP_REQUEST, priority);
				setResult(Activity.RESULT_OK, intent);
				finish();
				break;
			//case R.id.imageButtonLeft:
			case R.id.relativeLayoutLeft:
				goPrePriority();
				group_switching_btn = HANDLER_DATA_LEFT;
				
				break;
			//case R.id.imageButtonRight:
			case R.id.relativeLayoutRight:
				goNextPriority();
				group_switching_btn = HANDLER_DATA_RIGHT;
				
				break;
			case R.id.imageButtonExit:
				intent = new Intent();
				intent.putExtra(MainActivity.EXTRA_EXIT, true);
				setResult(Activity.RESULT_OK,intent);
				finish();
				break;
			case R.id.imageButtonMy:
				
				break;
			}
			
		}
		
	};
	
	private OnLongClickListener lsrGroupIconLong = new  OnLongClickListener(){

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			int position = (Integer) v.getTag();
			DBSchemaApplications.Columns columns = (DBSchemaApplications.Columns) adapter.getItem(position);
			
			Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
			intent.setType("text/plain"); // 分享发送的数据类型
			//intent.setPackage(packAgeName);
			Resources res = context.getResources();
			
			intent.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.share_title)); // 分享的主题
			intent.putExtra(Intent.EXTRA_TEXT,createShareText(columns.getAppLabel(),columns.getAppApkFile()) 
			/*		
					"我通过"+
					" 蒲公英应用吧 ("+
					"http://www.pu-up.com/Downloads/ApkStore/download/apk/ApkStore.apk"+
					") "+
					"与您分享一个很棒的应用："+
					columns.getAppLabel()+
					" (" +
					new DownloadPreferences(context).getServerUrl()+
					StoragePreferences.getApkPath()+
					"/"+
					columns.getAppApkFile()+
					") "
			*/		
					); // 分享的内容
			context.startActivity(Intent.createChooser(intent, res.getString(R.string.share_intent)));// 目标应用选择对话框的标题 

			return false;
		}
		
	};
	
	private String createShareText(String label, String apkfile){
		String string = "";
		Resources res = context.getResources();
		string += res.getString(R.string.share_via);
		string += "\""+res.getString(R.string.app_name)+"\"";
		string += " ( "+WebServicePreferences.VAL_DEF_APK_DOWNLOAD_FILE+" ) ";
		string += res.getString(R.string.share_app);
		string += ": \""+label+"\"";
		string += " ( " +
				new DownloadPreferences(context).getServerUrl()+
				StoragePreferences.getApkPath()+
				"/"+
				apkfile+
				" ) ";
		string += res.getString(R.string.share_go);
		
		return string;
	}
	
	private OnItemClickListener lsrGridItem = new OnItemClickListener(){

		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(System.currentTimeMillis() - debounce > DEBOUNCE_GAP){
				debounce = System.currentTimeMillis();
			
				DBSchemaApplications.Columns columns = (DBSchemaApplications.Columns) adapter.getItem(position);
				
				switch(columns.getAppLocation()){
				case DBSchemaApplications.VAL_LOCATION_CLOUD:
					/*
					new DBSchemaDownloadApk(context).addDownloadFile(columns.getAppSerial(),
							columns.getAppPackage(),columns.getAppVerCode(), columns.getAppApkFile());
					//new IntentSender(context).startApkDownload();
					
					new DBSchemaApplications(context).updateAppLocation(columns.getAppSerial(), DBSchemaApplications.VAL_LOCATION_DOWNLOADING);
					
					new DBSchemaReportDownload(context).addActionRecord(
							columns.getAppPackage(),
							columns.getAppVerCode(),
							DBSchemaReport.VAL_ACTION_DOWNLOAD_START);
					
					columns.setAppLocation(DBSchemaApplications.VAL_LOCATION_DOWNLOADING);
					handler.sendEmptyMessage(HANDLER_MSG_RREFRESH);
					*/
					Intent intent = new Intent(context,GroupDownloadActivity.class);
					intent.putExtra(GroupDownloadActivity.EXTRA_LABEL, columns.getAppLabel());
					intent.putExtra(GroupDownloadActivity.EXTRA_FILE_FROM, columns.getAppApkFile());
					intent.putExtra(GroupDownloadActivity.EXTRA_PACKAGE, columns.getAppPackage());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERSION, columns.getAppVerName());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERCODE, columns.getAppVerCode());
					intent.putExtra(GroupDownloadActivity.EXTRA_SERIAL, columns.getAppSerial());
					intent.putExtra(GroupDownloadActivity.EXTRA_REQUEST, GroupDownloadActivity.EXTRA_REQUEST_CLOUD);
					file_from_downloading = columns.getAppApkFile();
					app_serial_downloading = columns.getAppSerial();
					position_downloading = position;
					columns_downloading = columns;
					((Activity) context).startActivityForResult(intent,ACTIVITY_CLOUD);
					
					//new IntentSender(context).startApkDownload();
					
					break;
					
				case DBSchemaApplications.VAL_LOCATION_DOWNLOADING:
					
					intent = new Intent(context,GroupDownloadActivity.class);
					intent.putExtra(GroupDownloadActivity.EXTRA_LABEL, columns.getAppLabel());
					intent.putExtra(GroupDownloadActivity.EXTRA_FILE_FROM, columns.getAppApkFile());
					intent.putExtra(GroupDownloadActivity.EXTRA_PACKAGE, columns.getAppPackage());
					intent.putExtra(GroupDownloadActivity.EXTRA_VERSION, columns.getAppVerName());
					intent.putExtra(GroupDownloadActivity.EXTRA_REQUEST, GroupDownloadActivity.EXTRA_REQUEST_DOWNLOADING);
					file_from_downloading = columns.getAppApkFile();
					app_serial_downloading = columns.getAppSerial();
					position_downloading = position;
					columns_downloading = columns;
					((Activity) context).startActivityForResult(intent,ACTIVITY_DOWNLOADING);
					
					new IntentSender(context).startApkDownload();
					
					break;
					
				case DBSchemaApplications.VAL_LOCATION_STORAGE:
					String path = new ApkFileHelper(context).getApkFile(columns.getAppPackage());
					if(path !=null ){
						new IntentSender(context).startApkInstall(path);
					}
					
					break;
	
				case DBSchemaApplications.VAL_LOCATION_INSTALLED:

					String packageName = columns.getAppPackage();
					Intent startIntent = new Intent();
					startIntent = getPackageManager().getLaunchIntentForPackage(    
				                packageName);  
					((Activity) context).startActivityForResult(startIntent,0);	
					
					new DBSchemaReportLaunch(context).addActionRecord(packageName, 
							new PackageHelper(context).getPackageVerCode(packageName), 
							DBSchemaReport.VAL_ACTION_PACKAGE_LAUNCH);
					
				}
			
			}
		}
		
	};
	
	private OnItemLongClickListener lsrGridItemLong = new OnItemLongClickListener(){

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			DBSchemaApplications.Columns columns = (DBSchemaApplications.Columns) adapter.getItem(position);
			switch(columns.getAppLocation()){

			case DBSchemaApplications.VAL_LOCATION_CLOUD:
				
			case DBSchemaApplications.VAL_LOCATION_DOWNLOADING:
				
			case DBSchemaApplications.VAL_LOCATION_STORAGE:

			case DBSchemaApplications.VAL_LOCATION_INSTALLED:
				/*
				Intent intent = new Intent(context,ActivityAppOffline.class);
				intent.putExtra(GroupDownloadActivity.EXTRA_LABEL, columns.getAppLabel());
				intent.putExtra(GroupDownloadActivity.EXTRA_FILE_FROM, columns.getAppApkFile());
				intent.putExtra(GroupDownloadActivity.EXTRA_PACKAGE, columns.getAppPackage());
				intent.putExtra(GroupDownloadActivity.EXTRA_VERSION, columns.getAppVerName());
				intent.putExtra(GroupItemLongActivity.EXTRA_SERIAL, columns.getAppSerial());
				((Activity) context).startActivityForResult(intent,ACTIVITY_ITEMLONG);
			*/
			/*
				Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
				intent.setType("text/plain"); // 分享发送的数据类型
				//intent.setPackage(packAgeName);
				intent.putExtra(Intent.EXTRA_SUBJECT, "应用分享"); // 分享的主题
				intent.putExtra(Intent.EXTRA_TEXT, "分享："+columns.getAppLabel()+"@蒲公英应用吧"); // 分享的内容
				context.startActivity(Intent.createChooser(intent, "选择分享"));// 目标应用选择对话框的标题 
			*/
				break;
			}
			

			return true;
		}
		
	};
	
	private OnTouchListener lsrTouch = new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return gestureDetector.onTouchEvent(event);
		}
		
	};
	
	private AnimationListener lsrAnimation = new AnimationListener(){

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			gridViewOut.scrollTo(0, 0);
			gridViewOut.setVisibility(View.GONE);
			gridView.scrollTo(0, 0);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private GestureDetector gestureDetector;

	class GridGestureListener extends GestureListener{

		public GridGestureListener(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onLeftIn() {
			// TODO Auto-generated method stub
			goPrePriority();
			
		}

		@Override
		public void onRightIn() {
			// TODO Auto-generated method stub
			goNextPriority();
		}
		
	}

	
	
	/*
	 * CONSTRUCTORS
	 */
	
	
	/*
	 * METHODS, OVERRIDE
	 */
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
			outoff_activity = false;
			switch(requestCode){
			case ACTIVITY_CLOUD:
				if(resultCode == Activity.RESULT_OK){
					if(data.getIntExtra(GroupDownloadActivity.EXTRA_RESULT,GroupDownloadActivity.EXTRA_RESULT_NULL) ==
							GroupDownloadActivity.EXTRA_RESULT_DOWNLOAD){
						try{
							/*
							new DBSchemaDownloadApk(context).addDownloadFile(columns_downloading.getAppSerial(),
									columns_downloading.getAppPackage(),columns_downloading.getAppVerCode(), columns_downloading.getAppApkFile());
							new IntentSender(context).startApkDownload();
							
							new DBSchemaApplications(context).updateAppLocation(columns_downloading.getAppSerial(), DBSchemaApplications.VAL_LOCATION_DOWNLOADING);
							
							new DBSchemaReportDownload(context).addActionRecord(
									columns_downloading.getAppPackage(),
									columns_downloading.getAppVerCode(),
									DBSchemaReport.VAL_ACTION_DOWNLOAD_START);
							*/
//							columns_downloading.setAppLocation(DBSchemaApplications.VAL_LOCATION_DOWNLOADING);
			
						}
						catch(Exception e){
							e.printStackTrace();
						}
						
					}
					else if(data.getIntExtra(GroupDownloadActivity.EXTRA_RESULT,GroupDownloadActivity.EXTRA_RESULT_NULL) ==
							GroupDownloadActivity.EXTRA_RESULT_CANCEL){
/*
						if(new DBSchemaDownloadApk(context).isDownloading(file_from_downloading)){
							new DBSchemaDownloadApk(context).deleteDownloaded(file_from_downloading);
							new ThreadDownloadApk(context).stopDownloadThread(file_from_downloading);
							new ApkFileHelper(context).removeDownloadFile(file_from_downloading);
							new DBSchemaApplications(context).updateAppLocation(app_serial_downloading, DBSchemaApplications.VAL_LOCATION_CLOUD);
							new DBSchemaReportDownload(context).addActionRecord(
									columns_downloading.getAppPackage(),
									columns_downloading.getAppVerCode(),
									DBSchemaReport.VAL_ACTION_DOWNLOAD_CANCEL);
		
							((DBSchemaApplications.Columns) adapter.getItem(position_downloading)).setAppLocation(DBSchemaApplications.VAL_LOCATION_CLOUD);
						}
*/						
					}
					handler.sendEmptyMessage(HANDLER_MSG_RREFRESH);

				}
				
				break;
			case ACTIVITY_DOWNLOADING:
				if(resultCode == Activity.RESULT_OK && 
					data.getIntExtra(GroupDownloadActivity.EXTRA_RESULT,GroupDownloadActivity.EXTRA_RESULT_NULL) ==
						GroupDownloadActivity.EXTRA_RESULT_CANCEL){
					try{
						new DBSchemaDownloadApk(context).deleteDownloaded(file_from_downloading);
						new ThreadDownloadApk(context).stopDownloadThread(file_from_downloading);
						new ApkFileHelper(context).removeDownloadFile(file_from_downloading);
						//new DBSchemaApplications(context).updateAppLocation(app_serial_downloading, DBSchemaApplications.VAL_LOCATION_CLOUD);
						new DBSchemaReportDownload(context).addActionRecord(
								columns_downloading.getAppPackage(),
								columns_downloading.getAppVerCode(),
								DBSchemaReport.VAL_ACTION_DOWNLOAD_CANCEL);
	
						if(new PackageHelper(context).isAppsInstalled(columns_downloading.getAppPackage())){
							((DBSchemaApplications.Columns) adapter.getItem(position_downloading)).setAppLocation(DBSchemaApplications.VAL_LOCATION_INSTALLED);
							new DBSchemaApplications(context).updateAppLocation(app_serial_downloading, DBSchemaApplications.VAL_LOCATION_INSTALLED);
						}
						else if(new ApkFileHelper(context).isFileDownloaded(file_from_downloading)){
							((DBSchemaApplications.Columns) adapter.getItem(position_downloading)).setAppLocation(DBSchemaApplications.VAL_LOCATION_STORAGE);
							new DBSchemaApplications(context).updateAppLocation(app_serial_downloading, DBSchemaApplications.VAL_LOCATION_STORAGE);
						}
						else {
							((DBSchemaApplications.Columns) adapter.getItem(position_downloading)).setAppLocation(DBSchemaApplications.VAL_LOCATION_CLOUD);
							new DBSchemaApplications(context).updateAppLocation(app_serial_downloading, DBSchemaApplications.VAL_LOCATION_CLOUD);
						}
//						handler.sendEmptyMessage(HANDLER_MSG_RREFRESH);
					}
					catch(Exception e){
						e.printStackTrace();
					}
					handler.sendEmptyMessage(HANDLER_MSG_RREFRESH);

				}
				break;
			case ACTIVITY_DATAINIT:
				if(resultCode == Activity.RESULT_OK){
					handler.sendEmptyMessage(HANDLER_MSG_DATAINIT);
					refreshThread = new RefreshThread();
			        refreshThread.start();
				}
				else{
					finish();
				}
				break;
			case ACTIVITY_UPGRADE:
				
				if(resultCode == Activity.RESULT_OK){
					if(data.getIntExtra(GroupDownloadActivity.EXTRA_RESULT,GroupDownloadActivity.EXTRA_RESULT_NULL) ==
							GroupDownloadActivity.EXTRA_RESULT_DOWNLOAD){
						try{
							columns_downloading.setAppLocation(DBSchemaApplications.VAL_LOCATION_DOWNLOADING);
							handler.sendEmptyMessage(HANDLER_MSG_RREFRESH);
						}
						catch(Exception e){
							e.printStackTrace();
						}
						
					}
					else if(data.getIntExtra(GroupDownloadActivity.EXTRA_RESULT,GroupDownloadActivity.EXTRA_RESULT_NULL) ==
							GroupDownloadActivity.EXTRA_RESULT_CANCEL){
						if(new DBSchemaDownloadApk(context).isDownloading(file_from_downloading)){
							new DBSchemaDownloadApk(context).deleteDownloaded(file_from_downloading);
							new ThreadDownloadApk(context).stopDownloadThread(file_from_downloading);
							new ApkFileHelper(context).removeDownloadFile(file_from_downloading);
							//new DBSchemaApplications(context).updateAppLocation(app_serial_downloading, DBSchemaApplications.VAL_LOCATION_CLOUD);
							new DBSchemaReportDownload(context).addActionRecord(
									columns_downloading.getAppPackage(),
									columns_downloading.getAppVerCode(),
									DBSchemaReport.VAL_ACTION_DOWNLOAD_CANCEL);
		
							if(new PackageHelper(context).isAppsInstalled(columns_downloading.getAppPackage())){
								((DBSchemaApplications.Columns) adapter.getItem(position_downloading)).setAppLocation(DBSchemaApplications.VAL_LOCATION_INSTALLED);
								new DBSchemaApplications(context).updateAppLocation(app_serial_downloading, DBSchemaApplications.VAL_LOCATION_INSTALLED);
							}
							else if(new ApkFileHelper(context).isFileDownloaded(file_from_downloading)){
								((DBSchemaApplications.Columns) adapter.getItem(position_downloading)).setAppLocation(DBSchemaApplications.VAL_LOCATION_STORAGE);
								new DBSchemaApplications(context).updateAppLocation(app_serial_downloading, DBSchemaApplications.VAL_LOCATION_STORAGE);
							}
							else {
								((DBSchemaApplications.Columns) adapter.getItem(position_downloading)).setAppLocation(DBSchemaApplications.VAL_LOCATION_CLOUD);
								new DBSchemaApplications(context).updateAppLocation(app_serial_downloading, DBSchemaApplications.VAL_LOCATION_CLOUD);
							}
							handler.sendEmptyMessage(HANDLER_MSG_RREFRESH);
						}
						
					}
				}
				
				break;
			}
			
			handler.sendEmptyMessage(HANDLER_MSG_RREFRESH);
			
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group);
		
        this.context = this;
		schemaApp = new DBSchemaApplications(context);
		schemaIcon = new DBSchemaIconDownload(context);
		intentSender = new IntentSender(context);
		
		groupPref = new GroupPreferences(context);
		runtimePref = new RuntimePreferences(context);
		
		Intent intent = getIntent();
		
		int caller =  intent.getIntExtra(GROUP_CALLER, GroupPreferences.GROUP_PRIORITY_MIN);
        switch(caller){
        case GROUP_CALLER_WIDGET:
        	new DBSchemaReportWidgetGroup(context).updateActionRecord(DBSchemaReport.VAL_ACTION_SELF_WIDGET_GROUP);
        	new IntentSender(context).startAllServie();
        	break;
        	
        case GROUP_CALLER_APP:
        	new DBSchemaReportAppGroup(context).updateActionRecord(DBSchemaReport.VAL_ACTION_SELF_APP_GROUP);
        	//new IntentSender(context).startAllServie();
        	
        	break;
        }
		
        this.priority = intent.getIntExtra(GROUP_REQUEST, 1);
        //Log.i(this.getClass().getName(), "priority="+priority);
        
        group_serial = groupPref.getGroupSerialByPriority(priority);
        
       
        tvTitle = (TextView)findViewById(R.id.textViewTitle);
        //tvTitle.setVisibility(View.INVISIBLE);
        
        tvLeft = (TextView)findViewById(R.id.textViewLeft);
        //tvLeft.setVisibility(View.INVISIBLE);
        
        tvRight = (TextView)findViewById(R.id.textViewRight);
        //tvRight.setVisibility(View.INVISIBLE);
        
        ibtnLeft = (ImageButton)findViewById(R.id.imageButtonLeft);
        //ibtnLeft.setOnClickListener(lsrButton);
        
        ibtnRight = (ImageButton)findViewById(R.id.imageButtonRight);
        //ibtnRight.setOnClickListener(lsrButton);
        
        ((ImageView)findViewById(R.id.imageButtonExit)).setOnClickListener(lsrButton);
        
        ((ImageView)findViewById(R.id.imageButtonMy)).setOnClickListener(lsrButton);
        
        //((TextView)findViewById(R.id.textViewTitle)).setOnClickListener(lsrButton);
        
        lytLeft = (RelativeLayout)findViewById(R.id.relativeLayoutLeft);
        lytLeft.setOnClickListener(lsrButton);
        
        lytRight = (RelativeLayout)findViewById(R.id.relativeLayoutRight);
        lytRight.setOnClickListener(lsrButton);
        
        
        gridView = (GridView)findViewById(R.id.gridView);
        lstApps = new ArrayList<DBSchemaApplications.Columns>();
        setNoIcons = setNoIconsIn = new HashSet<GroupGridAdapter.IconInfo>();
        adapter = adapterIn = new GroupGridAdapter(context, lstApps, lsrGroupIcon, lsrGroupIconLong, setNoIcons);
        adapter.setListItems(lstApps);
        adapterIn.setListItems(lstApps);
        
        gridView.setAdapter(adapter);
        //gridView.setOnItemClickListener(lsrGridItem);
        gridView.setOnItemLongClickListener(lsrGridItemLong);
        
        setWaitingIcons = setWaitingIconsIn= new HashSet<GroupGridAdapter.IconInfo>();
        setWaitingIconsOut = new HashSet<GroupGridAdapter.IconInfo>();
        
        gridViewOut = (GridView) findViewById(R.id.gridViewOut);
        setNoIconsOut = new HashSet<GroupGridAdapter.IconInfo>();
        adapterOut = new GroupGridAdapter(context, lstApps, lsrGroupIcon,lsrGroupIconLong,setNoIconsOut);
        adapterIn.setListItems(lstApps);
        gridViewOut.setAdapter(adapterOut);
        
        gestureDetector = new GestureDetector(context, new GridGestureListener(context));
        gridView.setOnTouchListener(lsrTouch);
        
        gridViewOut.setOnTouchListener(lsrTouch);
        gridViewOut.setVisibility(View.GONE);
        
        
        
        screen_width = 1000;
        ivWallpapaer = (ImageView) findViewById(R.id.imageViewWallpaper); 
        this.wallpaperBitmap = null;
        //this.setWallpaper();
        
       
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
		
/*        
        animationFlying = AnimationUtils.loadAnimation(context, R.anim.flying);
        this.flying_item = (ImageView) findViewById(R.id.imageViewFlyingItem); 
        animationFlying.getInterpolator();
        this.flying_item.startAnimation(animationFlying); 
*/		
        switch(new DevicePreferences(context).getScreenWidth()){
        case 320:
            animationLeftIn = AnimationUtils.loadAnimation(context, R.anim.left_in_320);
            animationLeftIn.getInterpolator();
            animationLeftOut = AnimationUtils.loadAnimation(context, R.anim.left_out_320);
            animationLeftOut.getInterpolator();
            animationLeftOut.setAnimationListener(lsrAnimation);
            animationRightIn = AnimationUtils.loadAnimation(context, R.anim.right_in_320);
            animationRightIn.getInterpolator();
            animationRightOut = AnimationUtils.loadAnimation(context, R.anim.right_out_320);
            animationRightOut.getInterpolator();
            animationRightOut.setAnimationListener(lsrAnimation);
        	break;
        	
        case 480:
        	
            animationLeftIn = AnimationUtils.loadAnimation(context, R.anim.left_in_480);
            animationLeftIn.getInterpolator();
            animationLeftOut = AnimationUtils.loadAnimation(context, R.anim.left_out_480);
            animationLeftOut.getInterpolator();
            animationLeftOut.setAnimationListener(lsrAnimation);
            animationRightIn = AnimationUtils.loadAnimation(context, R.anim.right_in_480);
            animationRightIn.getInterpolator();
            animationRightOut = AnimationUtils.loadAnimation(context, R.anim.right_out_480);
            animationRightOut.getInterpolator();
            animationRightOut.setAnimationListener(lsrAnimation);
        	
        	break;
        case 540:
            animationLeftIn = AnimationUtils.loadAnimation(context, R.anim.left_in_540);
            animationLeftIn.getInterpolator();
            animationLeftOut = AnimationUtils.loadAnimation(context, R.anim.left_out_540);
            animationLeftOut.getInterpolator();
            animationLeftOut.setAnimationListener(lsrAnimation);
            animationRightIn = AnimationUtils.loadAnimation(context, R.anim.right_in_540);
            animationRightIn.getInterpolator();
            animationRightOut = AnimationUtils.loadAnimation(context, R.anim.right_out_540);
            animationRightOut.getInterpolator();
            animationRightOut.setAnimationListener(lsrAnimation);
        	
        	break;
        	
        case 720:
            animationLeftIn = AnimationUtils.loadAnimation(context, R.anim.left_in_720);
            animationLeftIn.getInterpolator();
            animationLeftOut = AnimationUtils.loadAnimation(context, R.anim.left_out_720);
            animationLeftOut.getInterpolator();
            animationLeftOut.setAnimationListener(lsrAnimation);
            animationRightIn = AnimationUtils.loadAnimation(context, R.anim.right_in_720);
            animationRightIn.getInterpolator();
            animationRightOut = AnimationUtils.loadAnimation(context, R.anim.right_out_720);
            animationRightOut.getInterpolator();
            animationRightOut.setAnimationListener(lsrAnimation);
        	
        	break;
        	
        case 1080:
            animationLeftIn = AnimationUtils.loadAnimation(context, R.anim.left_in_1080);
            animationLeftIn.getInterpolator();
            animationLeftOut = AnimationUtils.loadAnimation(context, R.anim.left_out_1080);
            animationLeftOut.getInterpolator();
            animationLeftOut.setAnimationListener(lsrAnimation);
            animationRightIn = AnimationUtils.loadAnimation(context, R.anim.right_in_1080);
            animationRightIn.getInterpolator();
            animationRightOut = AnimationUtils.loadAnimation(context, R.anim.right_out_1080);
            animationRightOut.getInterpolator();
            animationRightOut.setAnimationListener(lsrAnimation);
       	
        	break;
        	
        default:
        	
            animationLeftIn = AnimationUtils.loadAnimation(context, R.anim.left_in_480);
            animationLeftIn.getInterpolator();
            animationLeftOut = AnimationUtils.loadAnimation(context, R.anim.left_out_480);
            animationLeftOut.getInterpolator();
            animationLeftOut.setAnimationListener(lsrAnimation);
            animationRightIn = AnimationUtils.loadAnimation(context, R.anim.right_in_480);
            animationRightIn.getInterpolator();
            animationRightOut = AnimationUtils.loadAnimation(context, R.anim.right_out_480);
            animationRightOut.getInterpolator();
            animationRightOut.setAnimationListener(lsrAnimation);
        	
        	break;
        			
        }
        
        
        
        this.guiUpdateDelegate = new GuiUpdateDelegate(context, MainService.REQUEST_GUI_UPDATE);
        MainService.registerGuiUpdateDelegate(guiUpdateDelegate);
        
        if(!new InitPreferences(context).isDataInited()){
			Intent intentStart = new Intent(context,InitDataActivity.class);
			intentStart.putExtra(InitDataActivity.REQUEST_GROUP, this.group_serial);
			outoff_activity = true;
			startActivityForResult(new Intent(context, InitDataActivity.class),ACTIVITY_DATAINIT);
		}
		else{
	        handler.sendEmptyMessage(HANDLER_MSG_DATAINIT);
	        refreshThread = new RefreshThread();
	        refreshThread.start();
	        outoff_activity = false;
		}
        
		if(!StoragePreferences.isStorageAvailable()){
			Toast.makeText(context, this.getResources().getString(R.string.nostorage), Toast.LENGTH_LONG).show();
		}
		
		autorefreshing = false;
		group_switching = false;
		group_switching_btn = 0;
		refreshing_stamp = debounce = System.currentTimeMillis();
		groupThread = new GroupThread();
		
		
	       try {
	        	mLauncher = new Launcher(this);
	        } catch (Exception e) {
	        	Log.e(this.getClass().getName(), "Exception!: " + e.getMessage());
	        }

	}
	



	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try{
			MainService.unregisterGuiUpdateDelegate();
			if(refreshThread!=null){
				refreshThread.running = false;
			}
			try {
		    	mLauncher.play(Launcher.BUMP_100);		    		    
		    } catch (Exception e) {
		    	Log.e(this.getClass().getName(),"Error: " + e.getMessage());
		    }

		}
		catch(Exception e){
			e.printStackTrace();
		}
		super.onDestroy();
		
	}
	


	class GroupThread extends Thread{
		
		boolean running = false;
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			running = true;
			super.run();
			while(running){
				try{
					refreshContentData(false);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				handler.sendEmptyMessage(HANDLER_MSG_RREFRESHED);
				try{
					sleep(1000*60*60*24);
				}
				catch(Exception eSleep){
					eSleep.printStackTrace();
				}
			}
			running = false;
		}
		
	}
	
	
	class RefreshThread extends Thread{
		public boolean running = false;
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				super.run();
				running = true;
				new_icon_flag = false;
				while(running){
					//Log.i(this.getClass().getName(), "runnning:"+group_serial);
					
					try{
						if(!refreshing){
						Iterator<GroupGridAdapter.IconInfo> iterator =  setNoIcons.iterator();
						int i = 0;
						while(iterator.hasNext()){
							GroupGridAdapter.IconInfo iconInfo = iterator.next();
							if(!setWaitingIcons.contains(iconInfo)){
								setWaitingIcons.add(iconInfo);
								//DBSchemaIconDownload schema = new DBSchemaIconDownload(context);
								schemaIcon.addDownloadFile(iconInfo.getGroup(),iconInfo.getFile());
								i++;
							}
						}
						if( i>0 ){
						//if(i>0 && !new_icon_flag ){
							//IntentSender sender = new IntentSender(context);
							intentSender.startIconDownload();
							new_icon_flag = true;
						}
						else{
							new_icon_flag = false;
						}
						
						iterator =  setNoIcons.iterator();
						int j = 0;
						IconHelper helper = new IconHelper(context);
						while(iterator.hasNext()){
							GroupGridAdapter.IconInfo iconInfo = iterator.next();
							if(helper.isIconFileReady(iconInfo.getFile())){
								//setNoIcons.remove(iconfile);								
								iterator.remove();
							}
							else{
								j++;
							}
						}
						//Log.i(this.getClass().getName(), "getApplications:"+group_serial);
						lstAppsRefresh = schemaApp.getApplications(group_serial,false);
						//Log.i(this.getClass().getName(), "getApplications:"+group_serial+"="+lstAppsRefresh.size());
						int m = lstAppsRefresh.size();
				        int n = lstApps.size();
						if(m>n){
				        	lstApps = lstAppsRefresh;
				            adapter.setListItems(lstApps);
				        }
						if(i>0 || m>n || m==0 || j>0){
							handler.sendEmptyMessage(HANDLER_MSG_START_WAITING);
						}
						else{
							handler.sendEmptyMessage(HANDLER_MSG_STOP_WAITING);
						}
						
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
					finally{

						if(group_switching && !refreshing){
							handler.sendEmptyMessage(HANDLER_MSG_RREFRESHING);
							group_switching = false;
							refreshing_stamp = System.currentTimeMillis();
						}
						else if(System.currentTimeMillis()-refreshing_stamp > REFRESHING_TIMER){
							handler.sendEmptyMessage(HANDLER_MSG_RREFRESH);
							refreshing_stamp = System.currentTimeMillis();
						}
						
						sleep(SLEEP_TIMER);
						//Log.i(this.getClass().getName(), "sleeping");
							
					}
				}
			}
			catch(Exception eThread){
				eThread.printStackTrace();
			}
			finally{
				running = false;
			}
		}
		
		
		
	}
	
	private void refreshContentData(boolean cache){
		//Log.i(this.getClass().getName(), "refreshData");
//		DBSchemaApplications schema = new DBSchemaApplications(context);
//		if(!(new InitPreferences(context).isDataNull())){
	        //GroupPreferences groupPref = new GroupPreferences(this.context);
	        group_serial = groupPref.getGroupSerialByPriority(priority);
	        intentSender.notifyCurrentGroup(group_serial);
			//Log.i("currentgroup", "set current_group:"+group_serial);
			if(group_serial > 0){
	        	title = groupPref.getGroupNameBySerial(group_serial,priority);
	        }
	        else{
	        	title = groupPref.getGroupNameByPriority(priority);
	        }
	        
	        int p = getPrePriority();
	        int serial = groupPref.getGroupSerialByPriority(p);
	        if(serial > 0){
	        	left = groupPref.getGroupNameBySerial(serial,p);
	        }
	        else{
	        	left = groupPref.getGroupNameByPriority(p);
	        }
	        
	        p = getNextPriority();
	        serial = groupPref.getGroupSerialByPriority(p);
	        if(serial > 0){
	        	right = groupPref.getGroupNameBySerial(serial,p);
	        }
	        else{
	        	right = groupPref.getGroupNameByPriority(p);
	        }
	        lstAppsRefresh = schemaApp.getApplications(group_serial,false);
	        
	        
	        
//		}
	}
	
	private void refreshContentView(){
		//Log.i(this.getClass().getName(), "refreshView");
		gridViewIn = gridViewOut;
		setNoIconsIn = setNoIconsOut;
		setWaitingIconsIn = setWaitingIconsOut;
		adapterIn = adapterOut;
		
		gridViewOut = gridView;
		adapterOut = adapter;
		setNoIconsOut = setNoIcons;
		setWaitingIconsOut = setWaitingIcons;
		setNoIcons.clear();
		setWaitingIcons.clear();
		
		if(animationOut!=null){
			gridViewOut.startAnimation(animationOut);
		}
		else{
			gridViewOut.setVisibility(View.GONE);
		}
        
        setNoIcons = setNoIconsIn;
        setWaitingIcons = setWaitingIconsIn;
        //setNoIcons.clear();
        //setWaitingIcons.clear();
        gridView = gridViewIn;
        adapter = adapterIn;
        gridView.setVisibility(View.VISIBLE);
        
        
        //gridView.setOnItemClickListener(lsrGridItem);
        gridView.setOnItemLongClickListener(lsrGridItemLong);
        
        lstApps = lstAppsRefresh;
        adapter.setListItems(lstApps);
        adapter.notifyDataSetChanged();
		gridView.invalidate();
		
		if(id_focus>0){
			((ImageView)findViewById(id_focus)).setImageResource(R.drawable.ic_next);
		}
		id_focus = groupPref.getGroupImageViewFocusId(priority);
		((ImageView)findViewById(id_focus)).setImageResource(R.drawable.ic_focused);
        
		//setWallpaper();
		if(animationIn!=null){
			gridView.startAnimation(animationIn);
		}
		
		setTextViews();
		
		try {
	    	mLauncher.play(Launcher.SHORT_BUZZ_100);		    		    
	    } catch (Exception e) {
	    	Log.e(this.getClass().getName(),"Error: " + e.getMessage());
	    }

	}
	
	
	
	private void setTextViews(){
		tvTitle.setText(title);
        tvTitle.setVisibility(View.VISIBLE);
        
        tvLeft.setText(left);
        tvLeft.setVisibility(View.VISIBLE);
        
       	tvRight.setText(right);
        tvRight.setVisibility(View.VISIBLE);
		
	}
	
	private int getPrePriority(){
		
		int p = priority - 1;
		if(p < GroupPreferences.GROUP_PRIORITY_MIN){
			p = GroupPreferences.GROUP_PRIORITY_MAX;
		}
		return p;
		
	}
	
	private int getNextPriority(){
		
		int p = priority + 1;
		if(p > GroupPreferences.GROUP_PRIORITY_MAX){
			p = GroupPreferences.GROUP_PRIORITY_MIN;
		}
		return p;
		
	}

	private void goPrePriority(){
		priority = getPrePriority();
		
		Message msg = new Message();
		msg.what = HANDLER_MSG_RREFRESHING;
		Bundle bundle = new Bundle();
		bundle.putInt("button", HANDLER_DATA_LEFT);
		msg.setData(bundle);
		/*
		if(!refreshing){
			handler.sendMessage(msg);
			//	handler.sendEmptyMessage(HANDLER_MSG_RREFRESHING);
			Log.i(this.getClass().getName(), "HANDLER_MSG_RREFRESHING");
		}
		else{
			autorefreshing = true;
			handler.sendMessage(msg);
			
			//handler.sendEmptyMessage(HANDLER_MSG_RREFRESHING);
			Log.i(this.getClass().getName(), "autorefreshing");
		}
		*/
		group_switching = true;
		//group_switching_btn = HANDLER_DATA_LEFT;
		
		animationIn = animationLeftIn;
		animationOut = animationRightOut;
		
	}
	
	private void goNextPriority(){
		priority = getNextPriority();
		
		Message msg = new Message();
		msg.what = HANDLER_MSG_RREFRESHING;
		Bundle bundle = new Bundle();
		bundle.putInt("button", HANDLER_DATA_RIGHT);
		msg.setData(bundle);
		/*
		if(!refreshing){
			handler.sendMessage(msg);
			//	handler.sendEmptyMessage(HANDLER_MSG_RREFRESHING);
			Log.i(this.getClass().getName(), "HANDLER_MSG_RREFRESHING");
		}
		else{
			autorefreshing = true;
			handler.sendMessage(msg);
			//handler.sendEmptyMessage(HANDLER_MSG_RREFRESHING);
			Log.i(this.getClass().getName(), "autorefreshing");
		}
		*/
		group_switching = true;
		group_switching_btn = HANDLER_DATA_RIGHT;
		animationIn = animationRightIn;
		animationOut = animationLeftOut;
	}
	

	private void setWallpaper(){
		
	    // 获取壁纸管理器  
		try{
			if(this.wallpaperBitmap==null){
		        WallpaperManager wallpaperManager = WallpaperManager  
		                .getInstance(context);  
		        // 获取当前壁纸  
		        Drawable wallpaperDrawable = wallpaperManager.getDrawable();  
		        
		        // 将Drawable,转成Bitmap  
		        wallpaperBitmap = ((BitmapDrawable) wallpaperDrawable).getBitmap();  
			}
			
	        // 需要详细说明一下，mScreenCount、getCurrentWorkspaceScreen()、mScreenWidth、mScreenHeight分别  
	        //对应于Launcher中的桌面屏幕总数、当前屏幕下标、屏幕宽度、屏幕高度.等下拿Demo的哥们稍微要注意一下  
	        float step = 0;  
	        // 计算出屏幕的偏移量  
	        DisplayMetrics dMetrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
	        int screenWidth = dMetrics.widthPixels;
	        int screenHeight = dMetrics.heightPixels;
	        
	        step = (wallpaperBitmap.getWidth() - screenWidth)  
	                / (GroupPreferences.GROUP_PRIORITY_MAX - 1);  
	        // 截取相应屏幕的Bitmap  
	        Bitmap pbm = Bitmap.createBitmap(wallpaperBitmap, (int) ((priority-1) * step), 0,  
	                new DevicePreferences(context).getScreenWidth(),  
	                new DevicePreferences(context).getScreenHeight());  
	        // 设置 背景  
	        ivWallpapaer.setImageBitmap(pbm);
		}
		catch(Exception e){
			e.printStackTrace();
		}
        //layout.setBackgroundDrawable(new BitmapDrawable(pbm));  
	}
	
	private void checkMem(){
        int myProcessID = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        Log.i(this.getClass().getName(), "heap:"+activityManager.getMemoryClass()+"M");
        
        activityManager.getMemoryInfo(memoryInfo);
        Log.i(this.getClass().getName(), "availMem:"+memoryInfo.availMem);
        
        int[] pids = new int[1];
        pids[0] = myProcessID;
        android.os.Debug.MemoryInfo[] memInfos = activityManager.getProcessMemoryInfo(pids);
        android.os.Debug.MemoryInfo memInfo = memInfos[0];
        Log.i(this.getClass().getName(), "dalvikPrivateDirty:"+memInfo.dalvikPrivateDirty);
        Log.i(this.getClass().getName(), "dalvikPss:"+memInfo.dalvikPss);
        Log.i(this.getClass().getName(), "dalvikSharedDirty:"+memInfo.dalvikSharedDirty);
        Log.i(this.getClass().getName(), "nativePrivateDirty:"+memInfo.nativePrivateDirty);
        Log.i(this.getClass().getName(), "nativePss:"+memInfo.nativePss);
        Log.i(this.getClass().getName(), "nativeSharedDirty:"+memInfo.nativeSharedDirty);
        Log.i(this.getClass().getName(), "otherPrivateDirty:"+memInfo.otherPrivateDirty);
        Log.i(this.getClass().getName(), "otherPss:"+memInfo.otherPss);
        Log.i(this.getClass().getName(), "otherSharedDirty:"+memInfo.otherSharedDirty);
        Log.i(this.getClass().getName(), "getTotalPrivateDirty:"+memInfo.getTotalPrivateDirty());
        Log.i(this.getClass().getName(), "getTotalPss:"+memInfo.getTotalPss());
        Log.i(this.getClass().getName(), "getTotalSharedDirty:"+memInfo.getTotalSharedDirty());
		
	}
}
