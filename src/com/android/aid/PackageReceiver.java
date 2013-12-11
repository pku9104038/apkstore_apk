/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author wangpeifeng
 *
 */
public class PackageReceiver extends BroadcastReceiver{

	private ReceiverActionDelegate	onPackageAdd = new ReceiverActionDelegate(Intent.ACTION_PACKAGE_ADDED){

		/* (non-Javadoc)
		 * @see com.android.aid.BaseDelegatee#delegateTask()
		 */
		@Override
		public void action() {
			// TODO Auto-generated method stub
			
			if(!intentRequest.getBooleanExtra(Intent.EXTRA_REPLACING, false)){

				Intent serviceIntent = new Intent(this.getContext(), MainService.class);
				serviceIntent.putExtra(MainService.REQUEST_INSTALLED_UPDATE, true);
				
				this.getContext().startService(serviceIntent);
				String packageName = intentRequest.getDataString().substring("package:".length()); 
				new ApkFileHelper(context).removeApkFile(packageName); 
				int verCode = new PackageHelper(context).getPackageVerCode(packageName);
				new DBSchemaReportInstall(context).addActionRecord(packageName, verCode, 
						DBSchemaReport.VAL_ACTION_PACKAGE_ADD);

				new IntentSender(context).startInstalledUpdate();
				
			}
            
		}
		
	};

	private ReceiverActionDelegate	onPackageReplace = new ReceiverActionDelegate(Intent.ACTION_PACKAGE_REPLACED){

		/* (non-Javadoc)
		 * @see com.android.aid.BaseDelegatee#delegateTask()
		 */
		@Override
		public void action() {
			// TODO Auto-generated method stub
			Intent serviceIntent = new Intent(this.getContext(), MainService.class);
			serviceIntent.putExtra(MainService.REQUEST_INSTALLED_UPDATE, true);
			
			this.getContext().startService(serviceIntent);
			String packageName = intentRequest.getDataString().substring("package:".length()); 
			new ApkFileHelper(context).removeApkFile(packageName); 
			
			int verCode = new PackageHelper(context).getPackageVerCode(packageName);
			new DBSchemaReportInstall(context).addActionRecord(packageName, verCode, 
					DBSchemaReport.VAL_ACTION_PACKAGE_REPLACE);

			new IntentSender(context).startInstalledUpdate();

		}
		
	};

	private ReceiverActionDelegate	onPackageRemove = new ReceiverActionDelegate(Intent.ACTION_PACKAGE_REMOVED){

		/* (non-Javadoc)
		 * @see com.android.aid.BaseDelegatee#delegateTask()
		 */
		@Override
		public void action() {
			// TODO Auto-generated method stub

			if(!intentRequest.getBooleanExtra(Intent.EXTRA_REPLACING, false)){
				Intent serviceIntent = new Intent(this.getContext(), MainService.class);
				serviceIntent.putExtra(MainService.REQUEST_INSTALLED_UPDATE, true);
				String packageName = intentRequest.getDataString().substring("package:".length()); 
				int verCode = new PackageHelper(context).getPackageVerCode(packageName);
				new DBSchemaReportInstall(context).addActionRecord(packageName, verCode, 
						DBSchemaReport.VAL_ACTION_PACKAGE_REMOVE);

				new IntentSender(context).startInstalledUpdate();

			}
            
		}
		
	};

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ArrayList<ActionDelegate> lstDelegatees = new ArrayList<ActionDelegate>();
		lstDelegatees.add(onPackageAdd);
		lstDelegatees.add(onPackageReplace);
		lstDelegatees.add(onPackageRemove);

		Iterator<ActionDelegate> iterator = lstDelegatees.iterator();
		while(iterator.hasNext()){
			if(iterator.next().checkRequestAction(context, intent))
				break;
		}
		
	}

}
