/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.aid.DBSchemaApplications.Columns;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author wangpeifeng
 *
 */
public class GroupGridAdapter extends BaseAdapter{
	public class IconInfo{
		private String iconFile;
		private int iconGroup;
		public IconInfo(String iconFile, int iconGroup) {
			super();
			this.iconFile = iconFile;
			this.iconGroup = iconGroup;
		}
		public String getFile(){
			return this.iconFile;
		}
		
		public int getGroup(){
			return this.iconGroup;
		}
		
	};
	
	private Context context;
	private ArrayList<DBSchemaApplications.Columns> lstItems;
	private OnClickListener lsrIcon;
	private OnLongClickListener lsrIconLong;
	private Set<IconInfo> setNoIcons;
	private IconHelper helper = null;
	private PackageHelper packageHelper = null;
	private DBSchemaIconDownload iconShema;
	private int group_serial;
	
	
	/**
	 * CONSTRUCTOR
	 * @param context
	 * @param lstItems
	 */
	public GroupGridAdapter(Context context, ArrayList<DBSchemaApplications.Columns> lstItems, 
			OnClickListener lsr, OnLongClickListener lsrLong, Set<IconInfo> set) {
		super();
		this.context = context;
		this.lstItems = lstItems;
		this.lsrIcon = lsr;
		this.lsrIconLong = lsrLong;
		this.setNoIcons = set;
		this.helper = new IconHelper(context);
		this.packageHelper = new PackageHelper(context);
		this.iconShema = new DBSchemaIconDownload(context);
		this.group_serial = 0;

	}

	public void setListItems(ArrayList<DBSchemaApplications.Columns> lstItems){
		this.lstItems = lstItems;
	}
	
	public void setIconSet(Set<IconInfo> set){
		this.setNoIcons = set;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lstItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Object item = null;
		try{
			 item = lstItems.get(position);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		long id = 0;
		try{
			DBSchemaApplications.Columns item = (DBSchemaApplications.Columns) getItem(position);
			id = item.getAppSerial();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		BitmapHolder bitmapHolder ;
		View view = null;
		try{
			
			if(convertView == null){
				view = LayoutInflater.from(context).inflate(R.layout.griditem_group, null);
				bitmapHolder = new BitmapHolder();
				view.setTag(bitmapHolder);
				
			}
			else{
				view = convertView;	
				bitmapHolder = (BitmapHolder) view.getTag();
				if(bitmapHolder!=null){
					bitmapHolder.recycle();
				}
				else{
					bitmapHolder = new BitmapHolder();	
					view.setTag(bitmapHolder);
				}
			}
			
			ImageView ivIcon = (ImageView) view.findViewById(R.id.imageViewIcon);
			ImageView ivMarker = (ImageView) view.findViewById(R.id.imageViewMarker);
			TextView tvLabel = (TextView) view.findViewById(R.id.textViewLabel);
			DBSchemaApplications.Columns item = (DBSchemaApplications.Columns) getItem(position);
			
			tvLabel.setText(item.getAppLabel());
			tvLabel.setTag(position);
			tvLabel.setOnClickListener(lsrIcon);
			tvLabel.setOnLongClickListener(lsrIconLong);
			
			String packageName= item.getAppPackage();
			//IconHelper helper = new IconHelper(context);
			boolean iconReady = false;
			Bitmap bitmap = null;
			if(helper.isIconReady(packageName)){
				bitmap = helper.getIconBitmap(packageName);
				if(bitmap!=null){
					ivIcon.setImageBitmap(bitmap);
					iconReady = true;
					bitmapHolder.bmp = bitmap;
				}
			}
			
			if(!iconReady){
				
				ivIcon.setImageResource(R.drawable.ic_dummyicon);
				setNoIcons.add(new IconInfo(helper.getIconFile(packageName),item.getGroupSerial()));
				
//				DBSchemaIconDownload schema = new DBSchemaIconDownload(this.context);

				//Log.i(this.getClass().getName(), "add group_"+item.getGroupSerial()+" icon "+helper.getIconFile(packageName));
				//iconShema.addDownloadFile(item.getGroupSerial(),helper.getIconFile(packageName));

//				IntentSender sender = new IntentSender(context);
//				sender.addIconDownload(helper.getIconFile(packageName));

			}
			/*
			else{
				ivIcon.setVisibility(View.INVISIBLE);					
			}
			*/
			ivIcon.setTag(position);
			ivIcon.setOnClickListener(lsrIcon);
			ivIcon.setOnLongClickListener(lsrIconLong);
			ivMarker.setVisibility(View.VISIBLE);	
			ivMarker.setTag(position);
			ivMarker.setOnClickListener(lsrIcon);
			switch(item.getAppLocation()){
			case DBSchemaApplications.VAL_LOCATION_CLOUD:
				ivMarker.setImageResource(R.drawable.ic_cloud);
				break;
			case DBSchemaApplications.VAL_LOCATION_DOWNLOADING:
				ivMarker.setImageResource(R.drawable.ic_download);
				break;
				
			case DBSchemaApplications.VAL_LOCATION_STORAGE:
				ivMarker.setImageResource(R.drawable.ic_install);
				break;
				
			case DBSchemaApplications.VAL_LOCATION_INSTALLED:
				int newVercode = item.getAppVerCode();
				int nowVercode = packageHelper.getPackageVerCode(packageName);
				if(newVercode > nowVercode){
					ivMarker.setImageResource(R.drawable.ic_upgrade);
				}
				else{
					ivMarker.setImageResource(R.drawable.ic_recommend);
					//ivMarker.setImageResource(R.drawable.item);
					
				}
				break;
			}
			
			int padding = (int) (parent.getMeasuredHeight()/4 
					- parent.getMeasuredWidth()/4
					- tvLabel.getTextSize()*2);
			//((TextView) convertView.findViewById(R.id.textViewPadding)).setPadding(0, padding/2, 0, 0);
			//tvLabel.setPadding(2, 0, 2, padding);
			
		}
		catch(InflateException e){
			e.printStackTrace();
		}
		
		return view;
	}
	
	static class BitmapHolder{
		Bitmap bmp;
		
		public void recycle(){
			if(bmp!=null && !bmp.isRecycled()){
				bmp.recycle();
				//Log.i(this.getClass().getName(),"bmp recycle:"+bmp.getByteCount());
			}
		}
	}
}
