/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class ReqAddIconDelegate extends ServiceActionDelegate{

	public ReqAddIconDelegate(Context context, String requestAction) {
		super(context, requestAction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		DBSchemaIconDownload schema = new DBSchemaIconDownload(this.context);
		schema.addDownloadFile(intentRequest.getStringExtra(MainService.EXTRA_ICON_FILE));

		IntentSender sender = new IntentSender(context);
		sender.startIconDownload();
		
	}

}
