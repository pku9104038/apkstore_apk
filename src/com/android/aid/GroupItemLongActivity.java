/**
 * 
 */
package com.android.aid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wangpeifeng
 *
 */
public class GroupItemLongActivity extends Activity {
	
	public static final String EXTRA_LABEL			= "extra_label";
	public static final String EXTRA_SERIAL			= "extra_serial";
	public static final String EXTRA_PACKAGE		= "extra_package";
	
	
	private Context context;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String label = intent.getStringExtra(EXTRA_LABEL);
		String packageName = intent.getStringExtra(EXTRA_PACKAGE);
		
		context = this;
		
		setContentView(R.layout.activity_itemlong);
		IconHelper helper = new IconHelper(context);
		ImageView ivIcon = (ImageView) findViewById(R.id.imageViewApp);
		TextView tvLabel = (TextView) findViewById(R.id.textViewApp);
		
		ivIcon.setImageBitmap(helper.getIconBitmap(packageName));
		tvLabel.setText(label);
		

	}
	
	

}
