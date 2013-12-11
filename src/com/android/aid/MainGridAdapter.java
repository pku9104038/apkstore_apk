/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wangpeifeng
 *
 */
public class MainGridAdapter extends BaseAdapter{
	
	private Context context;
	
	
	private static final int GRID_COLUMNS	= 4;
	

	/**
	 * @param context
	 */
	public MainGridAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return GroupPreferences.GROUP_PRIORITY_MAX;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return (new GroupPreferences(context)).getGroupNameByPriority((int)getItemId(position));
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position+1;//priority
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try{
			
			convertView = LayoutInflater.from(context).inflate(R.layout.griditem_group, null);
			
			ImageView ivIcon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
			ImageView ivMarker = (ImageView) convertView.findViewById(R.id.imageViewMarker);
			TextView tvLabel = (TextView) convertView.findViewById(R.id.textViewLabel);
			TextView tvPadding = (TextView) convertView.findViewById(R.id.textViewPadding);
			
			tvLabel.setText((String)getItem(position));
			
			//ivIcon.setImageBitmap(new GroupPreferences(context).getGroupBitmapByPriority((int)getItemId(position)));
			ivIcon.setImageResource(new GroupPreferences(context).getGroupResIDByPriority((int)getItemId(position)));
			ivMarker.setVisibility(View.INVISIBLE);	

			((TextView) convertView.findViewById(R.id.textViewPadding)).setVisibility(View.INVISIBLE);
			
			int padding = (int) (parent.getMeasuredHeight()/GRID_COLUMNS 
					- parent.getMeasuredWidth()/GRID_COLUMNS
					- tvLabel.getTextSize()
					- tvPadding.getTextSize()
					);
			Log.i(this.getClass().getName(), parent.getMeasuredHeight()+"/"+parent.getMeasuredWidth()+","
					+tvLabel.getTextSize()+"/"+tvPadding.getTextSize()+","+padding);
			if(padding < 0){
				padding = 0;
			}
			( convertView.findViewById(R.id.textViewPadding)).setPadding(0, padding/2, 0, 0);
			tvLabel.setPadding(2, 0, 2, padding/2);
		}
		catch(InflateException e){
			e.printStackTrace();
		}
		
		return convertView;
	}

}
