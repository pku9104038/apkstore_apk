/**
 * 
 */
package com.android.aid;

import com.immersion.uhl.Launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author wangpeifeng
 *
 */
public class ActivityDownloadType extends Activity {

	
	private Context context;
	private RadioGroup radioGroup;
	private TextView btnBack;

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

	private OnClickListener lsrButton = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){

			case R.id.buttonBack:
				finish();
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
		
		setContentView(R.layout.activity_download_type);
		
		radioGroup = (RadioGroup) findViewById(R.id.radioGroupConnection);
		radioGroup.setOnCheckedChangeListener(lsrRadioGroup);
		int download_pref = new ConfigPreferences(context).getDownloadConnection();
		if(ConfigPreferences.VAL_DOWNLOAD_WLANONLINE==download_pref){
			radioGroup.check(R.id.radioWLANOnline);
		}
		else {
			radioGroup.check(R.id.radioDataOnline);
		}
		
		btnBack = ((TextView)findViewById(R.id.buttonBack));
		btnBack.setOnClickListener(lsrButton);
		
		
	}

}
