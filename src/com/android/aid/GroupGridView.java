/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.widget.GridView;

/**
 * @author wangpeifeng
 *
 */
public class GroupGridView extends GridView{

	public GroupGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsListView#pointToPosition(int, int)
	 */
	@Override
	public int pointToPosition(int x, int y) {
		// TODO Auto-generated method stub
		//return super.pointToPosition(x, y);
		return android.widget.AbsListView.INVALID_POSITION;
	}
	
	

}
