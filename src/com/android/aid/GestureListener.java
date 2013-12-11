/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * @author wangpeifeng
 *
 */
public abstract class GestureListener extends SimpleOnGestureListener {

	private Context context;
	
	private long stampDown, stampChecked;
	
	
	/**
	 * @param context
	 */
	public GestureListener(Context context) {
		super();
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onDoubleTap(android.view.MotionEvent)
	 */
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return super.onDoubleTap(e);
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onDoubleTapEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return super.onDoubleTapEvent(e);
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		stampDown = System.currentTimeMillis();
		stampChecked = System.currentTimeMillis() - 10;
		return super.onDown(e);
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if(stampChecked < stampDown){
			checkFling(e1, e2, velocityX,velocityY);
		}
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		super.onLongPress(e);
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		if(stampChecked<stampDown){
			checkScroll(e1, e2, distanceX,distanceY);
		}
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onShowPress(android.view.MotionEvent)
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		super.onShowPress(e);
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onSingleTapConfirmed(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return super.onSingleTapConfirmed(e);
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return super.onSingleTapUp(e);
	}
	
	
	private boolean checkScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY){
		boolean bool = false;
		
		int scrollx = (int) (e1.getRawX()-e2.getRawX());
		int scrolly = (int) (e1.getRawY()-e2.getRawY());
		int dx = Math.abs(scrollx);
		int dy = Math.abs(scrolly);
		int screen_width = new DevicePreferences(context).getScreenWidth();
		
		if(dx > dy && dx > screen_width/4){
			stampChecked = System.currentTimeMillis();
			if(scrollx > 0){
				onRightIn();
			}
			else{
				onLeftIn();
			}
			bool =  true;
		}
		
		return bool;
	}
	
	private boolean checkFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY){
		
		boolean bool = false;
		
		int scrollx = (int) (e1.getRawX()-e2.getRawX());
		int scrolly = (int) (e1.getRawY()-e2.getRawY());
		int dx = Math.abs(scrollx);
		int dy = Math.abs(scrolly);
		int screen_width = new DevicePreferences(context).getScreenWidth();
		if(dx > dy && dx > screen_width/4){
			stampChecked = System.currentTimeMillis();
			if(scrollx > 0){
				onRightIn();
			}
			else{
				onLeftIn();
			}
			bool = true;
		}
		
		return bool;
		
	}
	
	
	public abstract void onLeftIn();
	
	public abstract void onRightIn();
	
	

}
