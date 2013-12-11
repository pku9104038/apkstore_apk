/**
 * 
 */
package com.android.aid;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author wangpeifeng
 *
 */
public class WelcomeActivity extends Activity {
	
	/*
	 * 
	 */
	private static final int HANDLER_MSG_ANIMATION			= 1;
	private static final int HANDLER_MSG_STOP				= 1 + HANDLER_MSG_ANIMATION;
	
	
	private Context context;
	private ImageView ivWallpapaer, flying_item;
	private Animation in;
	private int x,y;
	
	private GestureOverlayView gestureView;
	private GestureDetector gestureDetector;
	private OnGestureListener lsrGesture = new OnGestureListener(){

		@Override
		public void onGesture(GestureOverlayView overlay, MotionEvent event) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "onGesture");
			
		}

		@Override
		public void onGestureCancelled(GestureOverlayView overlay,
				MotionEvent event) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "onGestureCancelled");
			
		}

		@Override
		public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "onGestureEnded");
			
		}

		@Override
		public void onGestureStarted(GestureOverlayView overlay,
				MotionEvent event) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "onGestureStarted");
			
		}

	};
	
	private OnClickListener lsrButton = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "button");
			switch(v.getId()){
			case R.id.textViewCallService:
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + context.getResources().getString(R.string.servicenumber)));
				startActivityForResult(intent, 0);
				
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
        
		this.context = this;
		
        
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);
        //ivWallpapaer = (ImageView) findViewById(R.id.imageViewWallpaper); 
        //this.setWallpaper();
        
        in = AnimationUtils.loadAnimation(context, R.anim.flying);
        this.flying_item = (ImageView) findViewById(R.id.imageViewFlying); 
        this.flying_item.startAnimation(in); 
        
		findViewById(R.id.textViewCallService).setOnClickListener(lsrButton);
		
		if(isAppubOpen()){
			findViewById(R.id.textViewMoreService).setVisibility(View.GONE);
			findViewById(R.id.textViewCallService).setVisibility(View.GONE);
		}

		this.thread.start();
		
		new IntentSender(context).startAllServie();
		
		
		//Log.i(this.getClass().getName(), new DevicePreferences(context).getScreenInfo());

		
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		waiting = false;
		super.onPause();
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		waiting = true;
		super.onResume();
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	private boolean waiting = false;
	private Thread thread = new Thread(){

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				super.run();
				int count = 1;
				waiting = true;
				while(waiting){
					try {
						Thread.sleep(1000);
//						if(isAppubOpen()){
							handler.sendEmptyMessage(HANDLER_MSG_STOP);
							waiting = false;
//						}
	//					count--;
	//					handler.sendEmptyMessage(HANDLER_MSG_ANIMATION);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	//			handler.sendEmptyMessage(HANDLER_MSG_STOP);
			}
			catch(Exception eThread){
				eThread.printStackTrace();
			}
		}
		
	};
	
	
	private boolean isAppubOpen(){
//		return new AppubPreferences(context).isAppubOpen();
		return true;
	}
	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case HANDLER_MSG_ANIMATION:
				x += 10;
				y += 10;
		        ivWallpapaer.scrollTo(x,y);
		        ivWallpapaer.startAnimation(in); 
				
				break;
				
			case HANDLER_MSG_STOP:
				//Intent intent = new Intent(context,MainActivity.class);
				Intent intent = new Intent(context,MainGridActivity.class);
				//((WelcomeActivity)context).startActivityForResult(intent,0);
				((Activity)context).startActivity(intent);
				finish();
				
				break;
			}
			
		}
		
		
	};

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}
	
	private void setWallpaper(){
	    // 获取壁纸管理器  
        WallpaperManager wallpaperManager = WallpaperManager  
                .getInstance(context);  
        // 获取当前壁纸  
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();  
        
        // 将Drawable,转成Bitmap  
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();  

        // 需要详细说明一下，mScreenCount、getCurrentWorkspaceScreen()、mScreenWidth、mScreenHeight分别  
        //对应于Launcher中的桌面屏幕总数、当前屏幕下标、屏幕宽度、屏幕高度.等下拿Demo的哥们稍微要注意一下  
        float step = 0;  
        // 计算出屏幕的偏移量  
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        int screenWidth = dMetrics.widthPixels;
        int screenHeight = dMetrics.heightPixels;
        
        step = (bm.getWidth() - screenWidth)  
                / (GroupPreferences.GROUP_PRIORITY_MAX - 1);  
        // 截取相应屏幕的Bitmap  
        Bitmap pbm = Bitmap.createBitmap(bm, (int) ((GroupPreferences.GROUP_PRIORITY_MAX-1) * step), 0,  
                (int) (screenWidth),  
                (int) (screenHeight));  
        // 设置 背景  
        ivWallpapaer.setImageBitmap(pbm);
        
        //layout.setBackgroundDrawable(new BitmapDrawable(pbm));  
	}
}
