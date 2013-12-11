/**
 * 
 */
package com.android.aid;



import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.immersion.uhl.Launcher;

/**
 * @author wangpeifeng
 *
 */
public class GroupDownloadActivity extends Activity{
	
	public static final String EXTRA_LABEL				= "extra_label";
	public static final String EXTRA_VERSION			= "extra_version";
	public static final String EXTRA_VERCODE			= "extra_vercode";
	public static final String EXTRA_FILE_FROM			= "extra_file_from";
	public static final String EXTRA_SERIAL				= "extra_serial";
	public static final String EXTRA_PACKAGE			= "extra_package";
	public static final String EXTRA_RESULT				= "extra_result";
	public static final String EXTRA_REQUEST			= "extra_request";
	
	public static final int EXTRA_RESULT_NULL			= 0;
	public static final int EXTRA_RESULT_DOWNLOAD		= 1;
	public static final int EXTRA_RESULT_CANCEL			= 2;
	
	public static final int EXTRA_REQUEST_CLOUD			= 1;
	public static final int EXTRA_REQUEST_DOWNLOADING	= 1 + EXTRA_REQUEST_CLOUD;
	public static final int EXTRA_REQUEST_STORAGE		= 1 + EXTRA_REQUEST_DOWNLOADING;
	public static final int EXTRA_REQUEST_INSTALLED		= 1 + EXTRA_REQUEST_STORAGE;
	
	
	private static final int PROGRESS_MAX				= 100;
	private static final int PROGRESS_MIN				= 0;

	private static final int HANDLER_MSG_REFRESH		= 1;
	private static final int HANDLER_MSG_FILE_SIZE		= 1 + HANDLER_MSG_REFRESH;
	private static final int HANDLER_MSG_DOWNLOADING	= 1 + HANDLER_MSG_FILE_SIZE;
	private static final int HANDLER_MSG_CANCEL			= 1 + HANDLER_MSG_DOWNLOADING;
	private static final int HANDLER_MSG_APP_INTRODUCE	= 1 + HANDLER_MSG_CANCEL;
	private static final int HANDLER_MSG_APP_INTRODUCE_ERR = 1 + HANDLER_MSG_APP_INTRODUCE;
	
	private Context context;
	
	private TextView tvApp, tvVersion,tvSize,tvPercent;
	private ProgressBar	progressBar;
	private RadioGroup radioGroup;
	private TextView btnBack;
	private TextView btnCancel,
					//btnBack, 
					btnDownload, btnInstall, btnLaunch, btnUpgrade, btnShare;
	
	private String file_from, file_to;
	private long file_size, download_size;
	private int progress;
	
	private WebView webView;
	private boolean webErr =false;
	private WebViewClient webViewClient = new WebViewClient() {

		
		/* (non-Javadoc)
		 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
		 */
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			handler.sendEmptyMessage(HANDLER_MSG_APP_INTRODUCE);
			Log.i("test", "finish url:"+url);
		}

		/* (non-Javadoc)
		 * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int, java.lang.String, java.lang.String)
		 */
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			Log.i("test","receive error:"+errorCode);
			
			handler.sendEmptyMessage(HANDLER_MSG_APP_INTRODUCE_ERR);
		}
		
		
	};
	
	private WebChromeClient webViewChromeClient = new WebChromeClient(){

		/* (non-Javadoc)
		 * @see android.webkit.WebChromeClient#onProgressChanged(android.webkit.WebView, int)
		 */
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			Log.i("test","progress:"+newProgress);
		}
		
	};
	
	private boolean running;
	
	private String app_downloading;
	private String background_download;
	private int auto_timer;
	
	private Intent intentResult;
	private int requestExtra;
	private int app_serial, app_vercode;
	private String app_package, app_file, app_version, app_label;
	
	
	private Launcher mLauncher;

	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case HANDLER_MSG_REFRESH:
				updateDownloadProgress();
				
				break;
			case HANDLER_MSG_FILE_SIZE:
				long totalSize = msg.getData().getLong("filesize");
				
				String filesize = "";
				int count;
				String bytes = "";
				if(totalSize > 1024*1024 ){
					count = 1024*1024;
					bytes = "M";
					
				}
				else {
					count = 1024;
					bytes = "K";
				}
				filesize = (int)(Math.floor(totalSize/count)) + "." + Math.round((totalSize%(count))*10/(count)) + bytes;
				tvSize.setText(filesize);
				tvSize.setVisibility(View.VISIBLE);
				
				break;
			case HANDLER_MSG_DOWNLOADING:
				
				btnCancel.setVisibility(View.VISIBLE);
				btnDownload.setVisibility(View.GONE);
				btnUpgrade.setVisibility(View.GONE);
				radioGroup.setVisibility(View.VISIBLE);
				//progressBar.setVisibility(View.VISIBLE);
				tvPercent.setVisibility(View.VISIBLE);
				
				break;
			
			case HANDLER_MSG_CANCEL:
				
				btnCancel.setVisibility(View.GONE);
				if(requestExtra==EXTRA_REQUEST_CLOUD){
					btnDownload.setVisibility(View.VISIBLE);
					btnUpgrade.setVisibility(View.GONE);	
				}
				else{
					btnDownload.setVisibility(View.GONE);
					btnUpgrade.setVisibility(View.VISIBLE);
				}
				//radioGroup.setVisibility(View.INVISIBLE);
				//progressBar.setVisibility(View.GONE);
				tvPercent.setVisibility(View.GONE);
				
				break;
				
			case HANDLER_MSG_APP_INTRODUCE_ERR:
				
				webView.setVisibility(View.INVISIBLE);
				webErr = true;
				break;

			case HANDLER_MSG_APP_INTRODUCE:
				
				if(!webErr){
					webView.setVisibility(View.VISIBLE);
				}
				break;
				
			}
		}
		
	};
	
	private OnClickListener lsrButton = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.buttonCancel:
				//setResult(Activity.RESULT_OK);
				if(new DBSchemaDownloadApk(context).isDownloading(app_file)){
					new DBSchemaDownloadApk(context).deleteDownloaded(app_file);
					new ThreadDownloadApk(context).stopDownloadThread(app_file);
					new ApkFileHelper(context).removeDownloadFile(app_file);
					if(new PackageHelper(context).isAppsInstalled(app_package)){
						new DBSchemaApplications(context).updateAppLocation(app_serial, DBSchemaApplications.VAL_LOCATION_INSTALLED);
					}
					else if(new ApkFileHelper(context).isFileDownloaded(app_file)){
						new DBSchemaApplications(context).updateAppLocation(app_serial, DBSchemaApplications.VAL_LOCATION_STORAGE);
					}
					else {
						new DBSchemaApplications(context).updateAppLocation(app_serial, DBSchemaApplications.VAL_LOCATION_CLOUD);
					}
					//new DBSchemaApplications(context).updateAppLocation(app_file, DBSchemaApplications.VAL_LOCATION_CLOUD);
					new DBSchemaReportDownload(context).addActionRecord(
							app_package,
							app_vercode,
							DBSchemaReport.VAL_ACTION_DOWNLOAD_CANCEL);
				}
				intentResult.putExtra(EXTRA_RESULT, EXTRA_RESULT_CANCEL);
				setResult(Activity.RESULT_OK, intentResult);
				handler.sendEmptyMessage(HANDLER_MSG_CANCEL);
				
				//finish();
				break;

			case R.id.buttonBack:
				setResult(Activity.RESULT_OK, intentResult);
				finish();
				break;
				
			case R.id.buttonDownload:
			case R.id.buttonUpgrade:
					
				if(!new DBSchemaDownloadApk(context).isDownloading(app_file)){
					new DBSchemaDownloadApk(context).addDownloadFile(app_serial,
							app_package,app_vercode, app_file);
					new IntentSender(context).startApkDownload();
					
					new DBSchemaApplications(context).updateAppLocation(app_serial, DBSchemaApplications.VAL_LOCATION_DOWNLOADING);
					
					new DBSchemaReportDownload(context).addActionRecord(
							app_package,
							app_vercode,
							DBSchemaReport.VAL_ACTION_DOWNLOAD_START);
					
					//columns_downloading.setAppLocation(DBSchemaApplications.VAL_LOCATION_DOWNLOADING);
					
					//setResult(Activity.RESULT_CANCELED);
				}
				intentResult.putExtra(EXTRA_RESULT, EXTRA_RESULT_DOWNLOAD);
				handler.sendEmptyMessage(HANDLER_MSG_DOWNLOADING);
				//finish();
				
				try {
			    	mLauncher.play(Launcher.DOUBLE_BUMP_100);		    		    
			    } catch (Exception e) {
			    	Log.e(this.getClass().getName(),"Error: " + e.getMessage());
			    }

				break;
				
			case R.id.buttonInstall:
				String path = new ApkFileHelper(context).getApkFile(app_package);
				if(path !=null ){
					new IntentSender(context).startApkInstall(path);
				}
				setResult(Activity.RESULT_OK, intentResult);
				finish();
				
				break;
			case R.id.buttonLaunch:
				Intent startIntent = new Intent();
				startIntent = getPackageManager().getLaunchIntentForPackage(    
			                app_package);  
				((Activity) context).startActivityForResult(startIntent,0);	
				
				new DBSchemaReportLaunch(context).addActionRecord(app_package, 
						new PackageHelper(context).getPackageVerCode(app_package), 
						DBSchemaReport.VAL_ACTION_PACKAGE_LAUNCH);
				
				setResult(Activity.RESULT_OK, intentResult);
				finish();
				
				break;
			
			case R.id.buttonShare:
				
				Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
				intent.setType("text/plain"); // 分享发送的数据类型
				//intent.setPackage(packAgeName);
				Resources res = context.getResources();
				
				intent.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.share_title)); // 分享的主题
				intent.putExtra(Intent.EXTRA_TEXT,createShareText(app_label,app_file) 
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
				
				break;
				
			case R.id.imageButtonSetting:
				intent = new Intent(context,ActivityDownloadType.class);
				((Activity) context).startActivityForResult(intent,1);
				
				break;
			
			}
			
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
	public OnCheckedChangeListener lsrRadioGroup = new OnCheckedChangeListener(){

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch(checkedId){
			case R.id.radioDataOnline:
				new ConfigPreferences(context).setDownloadConnection(ConfigPreferences.VAL_DOWNLOAD_DATAONLINE);
				new IntentSender(context).startApkDownload();
				break;
				
			case R.id.radioWLANOnline:
				new ConfigPreferences(context).setDownloadConnection(ConfigPreferences.VAL_DOWNLOAD_WLANONLINE);
				new IntentSender(context).startApkDownload();
				break;
			}
		}
		
	};
	


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		intentResult = new Intent();
		intentResult.putExtra(EXTRA_RESULT, EXTRA_RESULT_NULL);
		
		setContentView(R.layout.activity_download);
		
		
		Intent intent = getIntent();
		String label = app_label = intent.getStringExtra(EXTRA_LABEL);
		String vername = app_version = intent.getStringExtra(EXTRA_VERSION);
		file_from = app_file = intent.getStringExtra(EXTRA_FILE_FROM);
		
		tvApp = (TextView) findViewById(R.id.textViewApp);
		tvApp.setText(label);
		
		tvVersion = (TextView) findViewById(R.id.textViewVersion);
		tvVersion.setText("V"+vername+"版本");
		
		tvSize = (TextView) findViewById(R.id.textViewSize);
		tvSize.setVisibility(View.GONE);
		
		tvPercent = (TextView) findViewById(R.id.textViewPercent);
		tvPercent.setVisibility(View.GONE);
		
		this.app_downloading = label+
				"\nV"+vername+"版本";// +
				//"\n"+getResources().getString(R.string.app_downloading);//+"\n";
		auto_timer = 8;
		this.background_download = getResources().getString(R.string.background_download);
		//tvApp.setText(this.app_downloading + auto_timer + this.background_download);
		
		String packageName = app_package = intent.getStringExtra(EXTRA_PACKAGE);
		app_serial = intent.getIntExtra(EXTRA_SERIAL,0);
		app_vercode = intent.getIntExtra(EXTRA_VERCODE, 0);
		
		ImageView ivIcon = (ImageView) findViewById(R.id.imageViewApp);
		IconHelper helper = new IconHelper(context);
		Bitmap bmp = helper.getIconBitmap(packageName);
		if(bmp!=null){
			ivIcon.setImageBitmap(helper.getIconBitmap(packageName));
		}
		
		radioGroup = (RadioGroup) findViewById(R.id.radioGroupConnection);
		radioGroup.setOnCheckedChangeListener(lsrRadioGroup);
		int download_pref = new ConfigPreferences(context).getDownloadConnection();
		if(ConfigPreferences.VAL_DOWNLOAD_WLANONLINE==download_pref){
			radioGroup.check(R.id.radioWLANOnline);
		}
		else {
			radioGroup.check(R.id.radioDataOnline);
		}
		
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setMax(PROGRESS_MAX);
		progressBar.setProgress(PROGRESS_MIN);
		progressBar.setBackgroundColor(getResources().getColor(R.color.bg_light_gray));
		
		
		webView = (WebView)findViewById(R.id.WebViewIntroduce);
		String url ="http://www.pu-up.com/ApkStore/html5/app_introduce.php?serial="+app_serial;
		
		webView.loadUrl(url);
		webView.setWebViewClient(webViewClient);
		
		
		btnBack = ((TextView)findViewById(R.id.buttonBack));
		btnBack.setOnClickListener(lsrButton);
		
		btnCancel = ((TextView)findViewById(R.id.buttonCancel));
		btnCancel.setOnClickListener(lsrButton);
		
		btnDownload = ((TextView)findViewById(R.id.buttonDownload));
		btnDownload.setOnClickListener(lsrButton);
		
		btnInstall = ((TextView)findViewById(R.id.buttonInstall));
		btnInstall.setOnClickListener(lsrButton);
		
		btnLaunch = ((TextView)findViewById(R.id.buttonLaunch));
		btnLaunch.setOnClickListener(lsrButton);
		
		btnUpgrade = ((TextView)findViewById(R.id.buttonUpgrade));
		btnUpgrade.setOnClickListener(lsrButton);
		
		btnShare = ((TextView)findViewById(R.id.buttonShare));
		btnShare.setOnClickListener(lsrButton);
		
		findViewById(R.id.imageButtonSetting).setOnClickListener(lsrButton);
		
		requestExtra = intent.getIntExtra(EXTRA_REQUEST, EXTRA_REQUEST_CLOUD);
		switch(requestExtra){
		case EXTRA_REQUEST_CLOUD:
			btnCancel.setVisibility(View.GONE);
			btnInstall.setVisibility(View.GONE);
			btnLaunch.setVisibility(View.GONE);
			btnUpgrade.setVisibility(View.GONE);
			btnShare.setVisibility(View.GONE);
			
			//radioGroup.setVisibility(View.INVISIBLE);
			//progressBar.setVisibility(View.GONE);
			
			break;
			
		case EXTRA_REQUEST_DOWNLOADING:
			btnDownload.setVisibility(View.GONE);
			btnInstall.setVisibility(View.GONE);
			btnShare.setVisibility(View.GONE);
			
			if(new PackageHelper(context).isAppsInstalled(app_package)){
				
			}
			else{
				btnLaunch.setVisibility(View.GONE);
			}
			btnUpgrade.setVisibility(View.GONE);
			break;
			
		case EXTRA_REQUEST_STORAGE:
			btnCancel.setVisibility(View.GONE);
			btnDownload.setVisibility(View.GONE);
			//btnInstall.setVisibility(View.GONE);
			btnLaunch.setVisibility(View.GONE);
			btnUpgrade.setVisibility(View.GONE);
			
			radioGroup.setVisibility(View.INVISIBLE);
			//progressBar.setVisibility(View.GONE);
			
			break;
			
			
		case EXTRA_REQUEST_INSTALLED:
			btnCancel.setVisibility(View.GONE);
			btnDownload.setVisibility(View.GONE);
			btnInstall.setVisibility(View.GONE);
			//btnLaunch.setVisibility(View.GONE);
			int nowVercode = new PackageHelper(context).getPackageVerCode(packageName);
			if(app_vercode > nowVercode){
				;
			}
			else{
				btnUpgrade.setVisibility(View.GONE);
			}
			radioGroup.setVisibility(View.INVISIBLE);
			//progressBar.setVisibility(View.GONE);
			
			break;
	
		}
		
		/*
		if(new DBSchemaDownloadApk(context).isDownloading(file_from)){
			btnDownload.setVisibility(View.GONE);
		}
		else{
			btnCancel.setVisibility(View.GONE);
		}
		*/
		
		
/*		
		DBSchemaDownloadApk schema = new DBSchemaDownloadApk(context);
		file_size = schema.getDownloadFileSize(file_from);
		download_size = new ApkFileHelper(context).getApkDownloadSize(file_from);
		Log.i(this.getClass().getName(), "downloading:"+download_size+"/"+file_size);
		
		if(download_size == -1){
			progress = PROGRESS_MAX;
		}
		else if(download_size == 0){
			progress = PROGRESS_MIN;
		}
		else{
			if(file_size>0 && download_size < file_size){
				progress = Math.max((int)((download_size*PROGRESS_MAX)/file_size),PROGRESS_MIN);
			}
		}
		progressBar.setProgress(progress);
*/		
		
		this.queryFileSize();
		thread.start();
		
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
		super.onDestroy();
		running = false;
	}


	private void updateDownloadProgress(){
		if(auto_timer==0){
			setResult(Activity.RESULT_CANCELED);
			finish();
		}
		else{
			DBSchemaDownloadApk schema = new DBSchemaDownloadApk(context);
			file_size = schema.getDownloadFileSize(file_from);
			download_size = new ApkFileHelper(context).getApkDownloadSize(file_from);
			Log.i(this.getClass().getName(), "downloading:"+download_size+"/"+file_size);
			
			if(download_size == -1){
				progress = PROGRESS_MAX;
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
			else if(download_size == 0){
				progress = PROGRESS_MIN;
			}
			else{
				if(file_size>0 && download_size < file_size){
					progress = Math.max((int)((download_size*PROGRESS_MAX)/file_size),PROGRESS_MIN);
				}
			}
			progressBar.setProgress(progress);
			
			//tvPercent.setVisibility(View.VISIBLE);
			tvPercent.setText(Math.max(1, progress)+"%");
			
			//tvApp.setText(this.app_downloading/* + auto_timer + this.background_download*/);
			//auto_timer--;
		}
		
		
	}
	
	private Thread thread = new Thread(){

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
			super.run();
				running = true;
				while(running){
					handler.sendEmptyMessage(HANDLER_MSG_REFRESH);
					try{
						sleep(1000);
					}
					catch(Exception e){
						e.printStackTrace();
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
		
	};
	
	private class QueryFileSizeApi extends WebServiceApi{

		public QueryFileSizeApi(Context context, String web_api_pref_key,
				HttpListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_FILENAME, 
					app_file));
			this.postApi(params);	
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_FILENAME, 
					app_file));
			this.getApi(params);	
			
		}
		
	}

	private HttpPostListener lsrQueryFileSize = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			//Log.i(this.getClass().getName(),response[HttpPostListener.POST_URL]+" Resp: "+response[HttpPostListener.POST_RESP]);
			long size = 0;
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				try{
					String filebytes = WebServiceApi.getRespMsg(response[HttpListener.HTTP_RESP]);
					size = Long.parseLong(filebytes);
				}
				catch(Exception e){
					e.printStackTrace();
					size = 0;
				}
				Log.i(this.getClass().getName(),"filesize:"+size);
				if(size > 0){
					Message msg = new Message();
					msg.what = HANDLER_MSG_FILE_SIZE;
					Bundle bundle = new Bundle();
					bundle.putLong("filesize", size);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}
			
		}
		
		
	};
	
	

	private void queryFileSize() {
		// TODO Auto-generated method stub
			new QueryFileSizeApi(context,
					WebServicePreferences.PREF_KEY_API_QUERY_FILE_SIZE,
					this.lsrQueryFileSize).postApi();
			Log.i(this.getClass().getName(),"QueryFileSizeApi");
	}

	private class QueryAppIntroduceApi extends WebServiceApi{

		public QueryAppIntroduceApi(Context context, String web_api_pref_key,
				HttpListener listener) {
			super(context, web_api_pref_key, listener);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void postApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APP_SERIAL, 
					app_serial+""));
			this.postApi(params);	
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APP_SERIAL, 
					app_serial+""));
			this.getApi(params);	
			
		}
		
	}

	private HttpPostListener lsrQueryAppIntroduce = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			//Log.i(this.getClass().getName(),response[HttpPostListener.POST_URL]+" Resp: "+response[HttpPostListener.POST_RESP]);
			String introduce = "";
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				try{
					introduce = WebServiceApi.getRespMsg(response[HttpListener.HTTP_RESP]);
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
					Message msg = new Message();
					//msg.what = HANDLER_MSG_APP_INTRODUCE;
					Bundle bundle = new Bundle();
					bundle.putString("introduce", introduce);
					msg.setData(bundle);
					//handler.sendMessage(msg);
				
			}
			
		}
		
		
	};
	
	

	private void queryAppIntroduce() {
		// TODO Auto-generated method stub
			new QueryAppIntroduceApi(context,
					WebServicePreferences.PREF_KEY_API_QUERY_APP_INTRODUCE,
					this.lsrQueryAppIntroduce).postApi();
			
	}
	
}
