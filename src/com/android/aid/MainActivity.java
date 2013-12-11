
package com.android.aid;

import java.util.ArrayList;
import java.util.HashSet;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
	
	/*
	 * CONSTANTS, PRIVATE
	 */
	//public static final String PACKAGE									= "com.android.aid";
	public static final String ID_STRING_GROUP_LAYOUT					= "RelativeLayoutGroup";
	public static final String ID_STRING_GROUP_TEXTVIEW					= "textViewGroup";

	private static final int HANDLER_MSG_WELCOME						= 1;
	private static final int HANDLER_MSG_GROUP							= 1 + HANDLER_MSG_WELCOME;
	
	public static final String EXTRA_EXIT								= "extra_exit";
	/*
	 * PROPERTIES,PRIVATE
	 */
	private LinearLayout lytGroups;
	//private ImageView ivWelcome;
	private ImageView ivWallpapaer, flying_item;
	private Animation in;
	
	/*
	 * PRIORITIES, BEHAVIAR
	 */
	
	private OnClickListener lsrGroupLayout = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context,GroupActivity.class);
			GroupPreferences groupPref = new GroupPreferences(context);
	        int i = 0;
	        i = groupPref.getGroupPriorityById(v.getId());
	        //i = groupPref.getGroupPriorityByImageId(v.getId());
	        
	        intent.putExtra(GroupActivity.GROUP_REQUEST, i);
	        intent.putExtra(GroupActivity.GROUP_CALLER, GroupActivity.GROUP_CALLER_APP);
			
			((Activity) context).startActivityForResult(intent,1);
			
		}
		
	};

	private OnTouchListener lsrTouch = new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return gestureDetector.onTouchEvent(event);
		}
		
	};
	
	
	private GestureDetector gestureDetector;
	
	private boolean ingroup = false;

	class GestureListener extends SimpleOnGestureListener{

		/* (non-Javadoc)
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
		 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			int scrollx = (int) (e1.getRawX()-e2.getRawX());
			int scrolly = (int) (e1.getRawY()-e2.getRawY());
			int dx = Math.abs(scrollx);
			int dy = Math.abs(scrolly);
			if(dx > dy*3 && dx > 100){
				if(!ingroup){
				int i = GroupPreferences.GROUP_PRIORITY_MIN;
				if(scrollx>0){
					
				}
				else{
					i = GroupPreferences.GROUP_PRIORITY_MAX;
						
				}
		        
				Intent intent = new Intent(context,GroupActivity.class);
				intent.putExtra(GroupActivity.GROUP_REQUEST, i);
		        intent.putExtra(GroupActivity.GROUP_CALLER, GroupActivity.GROUP_CALLER_APP);
				
				((Activity) context).startActivityForResult(intent,1);
				ingroup = true;
				}
				return true;
				
			}
			else{
				return super.onScroll(e1, e2, distanceX, distanceY);
			}
		}

		
	}
	
	
	private static final String TAG			 = "com.android.aid.WelcomeActivity";
	private GestureOverlayView gestureView;
	
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
	
	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case HANDLER_MSG_WELCOME:
				initGroup();
				break;
			case HANDLER_MSG_GROUP:
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
		ingroup = false;
		if(Activity.RESULT_OK == resultCode){
			if(data.getBooleanExtra(EXTRA_EXIT, false)){
				finish();
			}
		}
	}
	
	private OnClickListener lsrButton = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.imageButtonExit:
				finish();
				break;
			case R.id.imageButtonMy:
				
				break;
			}
			
		}
		
	};

	/*
	 * PRIORITIES, PRIVATE
	 */
	private Context context;
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

        this.context = this;
        //new IntentSender(context).startInitData();
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ivWallpapaer = (ImageView) findViewById(R.id.imageViewWallpaper); 

        this.lytGroups = (LinearLayout) findViewById(R.id.LinearLayoutMain);
        lytGroups.setVisibility(View.GONE);
 
        ((ImageView)findViewById(R.id.imageButtonExit)).setOnClickListener(lsrButton);
        
        ((ImageView)findViewById(R.id.imageButtonMy)).setOnClickListener(lsrButton);

        gestureDetector = new GestureDetector(context, new GestureListener());
       
        
        GridView gv;
        gv = (GridView) findViewById(R.id.gridView);
        GroupGridAdapter adapter = new GroupGridAdapter(context, 
        		new ArrayList<DBSchemaApplications.Columns>(),
        		null,
        		null,
        		new HashSet<GroupGridAdapter.IconInfo>());
        gv.setAdapter(adapter);
        gv.setOnTouchListener(lsrTouch);
        
        
        //((ImageView)findViewById(R.id.imageView)).setOnTouchListener(lsrTouch);
        //gestureView = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
        //gestureView.addOnGestureListener(lsrGesture);
        
        this.setWallpaper();
        
        in = AnimationUtils.loadAnimation(context, R.anim.flying);
        this.flying_item = (ImageView) findViewById(R.id.imageViewFlyingItem); 
        in.getInterpolator();
        this.flying_item.startAnimation(in); 
		
        handler.sendEmptyMessage(HANDLER_MSG_WELCOME);
        
        //new DBSchemaReportAppActive(context).addActionRecord(DBSchemaReport.VAL_ACTION_SELF_APP_ACTIVE);
        new DBSchemaReportAppActive(context).updateActionRecord(DBSchemaReport.VAL_ACTION_SELF_APP_ACTIVE);
        
        //new IntentSender(context).startSync();
        //new IntentSender(context).startReport();
        //new IntentSender(context).startDownload();
        new IntentSender(context).startAllServie();
        
    }

	private void initGroup(){
    	
        GroupPreferences groupPref = new GroupPreferences(context);
        for(int i=GroupPreferences.GROUP_PRIORITY_MIN;i<=GroupPreferences.GROUP_PRIORITY_MAX;i++){
            int id = groupPref.getGroupTextViewId(i);
            ((TextView)findViewById(id)).setText(groupPref.getGroupNameByPriority(i));
            ImageView iv;
            
            id = groupPref.getGroupImageViewId(i);
            ((ImageView)findViewById(id)).setImageBitmap(groupPref.getGroupBitmapByPriority(i));  
           
            id = groupPref.getGroupImageViewMarkerId(i);
            ((ImageView)findViewById(id)).setVisibility(View.INVISIBLE);
            
            id = groupPref.getGroupLayoutId(i);
            //id = groupPref.getGroupImageViewId(i);
            findViewById(id).setOnClickListener(this.lsrGroupLayout);

		}
        this.lytGroups.setVisibility(View.VISIBLE);
        //this.ivWelcome.setVisibility(View.GONE);
    	
    }
/*    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
*/
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
        Bitmap pbm = Bitmap.createBitmap(bm, (int)((GroupPreferences.GROUP_PRIORITY_MAX-1) * step), 0,  
                (int) (screenWidth),  
                (int) (screenHeight));  
        // 设置 背景  
        ivWallpapaer.setImageBitmap(pbm);
        
        //layout.setBackgroundDrawable(new BitmapDrawable(pbm));  
	}
    
}
