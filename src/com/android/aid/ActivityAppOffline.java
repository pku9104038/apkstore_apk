/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * @author wangpeifeng
 *
 */
public class ActivityAppOffline extends Activity{

	public static final String EXTRA_LABEL			= "extra_label";
	public static final String EXTRA_VERSION		= "extra_version";
	public static final String EXTRA_VERCODE		= "extra_vercode";
	public static final String EXTRA_FILE_FROM		= "extra_file_from";
	public static final String EXTRA_SERIAL			= "extra_serial";
	public static final String EXTRA_PACKAGE		= "extra_package";
	public static final String EXTRA_RESULT			= "extra_result";
	
	private static final int HANDLER_MSG_TOAST		= 1;
	
	private Context context;
	
	private TextView tvApp, tvVersion,tvSize;
	private EditText edAccount, edPassword;
	
	private int app_serial, app_vercode;
	private String app_package, app_file, app_version, app_label;
	
	
	
	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case HANDLER_MSG_TOAST:
				Toast.makeText(context, msg.getData().getCharSequence("toast"), Toast.LENGTH_LONG).show();
				finish();
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
				finish();
				break;
				
			case R.id.buttonSubmit:
				apkOffline();

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
		
		setContentView(R.layout.activity_apkoffline);
		
		Intent intent = getIntent();
		String label = app_label = intent.getStringExtra(EXTRA_LABEL);
		String vername = app_version = intent.getStringExtra(EXTRA_VERSION);
		app_file = intent.getStringExtra(EXTRA_FILE_FROM);
		
		tvApp = (TextView) findViewById(R.id.textViewApp);
		tvApp.setText(label);
		
		tvVersion = (TextView) findViewById(R.id.textViewVersion);
		tvVersion.setText("V"+vername+"版本");
		
		tvSize = (TextView) findViewById(R.id.textViewSize);
		tvSize.setVisibility(View.GONE);
		
		edAccount = (EditText) findViewById(R.id.editTextAccount);
		edPassword = (EditText) findViewById(R.id.editTextPassword);
		
		String packageName = app_package = intent.getStringExtra(EXTRA_PACKAGE);
		app_serial = intent.getIntExtra(EXTRA_SERIAL,0);
		app_vercode = intent.getIntExtra(EXTRA_VERCODE, 0);
		
		ImageView ivIcon = (ImageView) findViewById(R.id.imageViewApp);
		IconHelper helper = new IconHelper(context);
		ivIcon.setImageBitmap(helper.getIconBitmap(packageName));
		
		((Button)findViewById(R.id.buttonCancel)).setOnClickListener(lsrButton);
		((Button)findViewById(R.id.buttonSubmit)).setOnClickListener(lsrButton);
		
		
	}
	
	

	
	private class ApkOfflineApi extends WebServiceApi{

		public ApkOfflineApi(Context context, String web_api_pref_key,
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
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APP_PACKAGE, 
					app_package));
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_APP_LABEL, 
					app_label));
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_ACCOUNT, 
					edAccount.getText().toString()));
			params.add(new BasicNameValuePair(WebServiceApi.API_PARAM_PASSWORD, 
					edPassword.getText().toString()));
			
			this.postApi(params);	
			
		}

		@Override
		public void getApi() {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			this.getApi(params);	
			
		}
		
	}

	private HttpPostListener lsrApkOffline = new HttpPostListener(context){

		@Override
		public void OnHttpPostResponse(String[] response) {
			// TODO Auto-generated method stub
			String strMsg = "通讯失败，请检查网络链接！";
			if(WebServiceApi.isRespSuccess(response[HttpListener.HTTP_RESP])){
				try{
					strMsg = WebServiceApi.getRespMsg(response[HttpListener.HTTP_RESP]);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = HANDLER_MSG_TOAST;
				Bundle bundle = new Bundle();
				bundle.putString("toast", strMsg);
				msg.setData(bundle);
				handler.sendMessage(msg);
				
			}
			
		}
		
		
	};
	
	

	private void apkOffline() {
		// TODO Auto-generated method stub
			new ApkOfflineApi(context,
					WebServicePreferences.PREF_KEY_API_APK_OFFLINE,
					this.lsrApkOffline).postApi();
	}
		
	
	
}
