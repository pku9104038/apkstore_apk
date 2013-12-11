/**
 * 
 */
package com.android.aid;

import java.util.ArrayList;
import java.util.HashSet;


import com.android.aid.GroupActivity.GroupThread;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


import com.immersion.uhl.Launcher;

/**
 * @author wangpeifeng
 *
 */
public class MainGridActivity extends Activity{
	
	
	/*
	 * CONSTANTS, PUBLIC
	 */
	public static final String GROUP_REQUEST			= "group_request";
	public static final String GROUP_CALLER				= "group_caller";
	
	public static final int GROUP_CALLER_WIDGET			= DBSchemaReport.VAL_ACTION_SELF_WIDGET_GROUP;
	public static final int GROUP_CALLER_APP			= DBSchemaReport.VAL_ACTION_SELF_APP_GROUP;
	
	
	/*
	 * CONSTANTS, PRIVATE
	 */
	private static final int HANDLER_MSG_RREFRESHING	= 1;
	private static final int HANDLER_MSG_RREFRESHED		= 1 + HANDLER_MSG_RREFRESHING;
	private static final int HANDLER_MSG_RREFRESH		= 1 + HANDLER_MSG_RREFRESHED;
	private static final int HANDLER_MSG_TOAST			= 1 + HANDLER_MSG_RREFRESH;
	
	private static final int ACTIVITY_DOWNLOADING		= 1;
	private static final int ACTIVITY_ITEMLONG			= 1 + ACTIVITY_DOWNLOADING;

	/*
	 * PROPERTRIES, PRIVATE 
	 */
	private Context context;
	private int priority;
	private int current_group;
	private TextView tvTitle, tvLeft, tvRight;
	private ImageButton ibtnSearch, ibtnLeft, ibtnRight, ibtnMy;
	private RelativeLayout lytLeft,lytRight;
	//private GridView gridView;
	private GridView gridView;
	private ArrayList<DBSchemaApplications.Columns> lstApps;
	private ArrayList<DBSchemaApplications.Columns> lstAppsRefresh;
	private MainGridAdapter adapter;
	private ProgressBar progressBar;
	private ImageView ivWallpapaer, flying_item;
	private Bitmap wallpaperBitmap;
	private Animation in;
	
	private Launcher mLauncher;
	
	
		/*
	 * PROPERTIES, PUBLIC
	 */
	
	/*
	 * PROPERTIES, PROTECTED
	 */
	
	
	
	private OnClickListener lsrButton = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			
			case R.id.textViewTitle:
				Intent intent = new Intent(context,GroupActivity.class);
//				GroupPreferences groupPref = new GroupPreferences(context);
		        //int i = (int) adapter.getItemId(position);
		        //i = groupPref.getGroupPriorityByImageId(v.getId());
		        
		        intent.putExtra(GroupActivity.GROUP_REQUEST, current_group);
		        intent.putExtra(GroupActivity.GROUP_CALLER, GroupActivity.GROUP_CALLER_APP);
				
				((Activity) context).startActivityForResult(intent,1);
				
				break;
				
			case R.id.imageButtonExit:
				intent = new Intent();
				intent.putExtra(MainActivity.EXTRA_EXIT, true);
				setResult(Activity.RESULT_OK,intent);
				finish();
				break;
			case R.id.imageButtonMy:
				
				break;
			}
			
		}
		
	};
	
	private OnItemClickListener lsrGridItem = new OnItemClickListener(){

		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context,GroupActivity.class);
			GroupPreferences groupPref = new GroupPreferences(context);
	        int i = (int) adapter.getItemId(position);
	        //i = groupPref.getGroupPriorityByImageId(v.getId());
	        
	        intent.putExtra(GroupActivity.GROUP_REQUEST, i);
	        intent.putExtra(GroupActivity.GROUP_CALLER, GroupActivity.GROUP_CALLER_APP);
			
			((Activity) context).startActivityForResult(intent,1);
	        //((Activity) context).startActivity(intent);
	        //finish();
				
			try {
		    	mLauncher.play(Launcher.DOUBLE_STRONG_CLICK_100);		    		    
		    } catch (Exception e) {
		    	Log.e(this.getClass().getName(),"Error: " + e.getMessage());
		    }
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

	class GridGestureListener extends GestureListener{

		public GridGestureListener(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onLeftIn() {
			// TODO Auto-generated method stub
			
		Intent intent = new Intent(context,GroupActivity.class);
		intent.putExtra(GroupActivity.GROUP_REQUEST, GroupPreferences.GROUP_PRIORITY_MAX);
        intent.putExtra(GroupActivity.GROUP_CALLER, GroupActivity.GROUP_CALLER_APP);
		
		((Activity) context).startActivityForResult(intent,1);
		//ingroup = true;
				
		}

		@Override
		public void onRightIn() {
			// TODO Auto-generated method stub
			
		Intent intent = new Intent(context,GroupActivity.class);
		intent.putExtra(GroupActivity.GROUP_REQUEST, GroupPreferences.GROUP_PRIORITY_MIN);
        intent.putExtra(GroupActivity.GROUP_CALLER, GroupActivity.GROUP_CALLER_APP);
		
		((Activity) context).startActivityForResult(intent,1);
		//ingroup = true;
			
		}
		

		
	}

	
	
	/*
	 * CONSTRUCTORS
	 */
	
	
	/*
	 * METHODS, OVERRIDE
	 */
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(Activity.RESULT_OK == resultCode){
			current_group = data.getIntExtra(GROUP_REQUEST, 1);
			
			if(data.getBooleanExtra(MainActivity.EXTRA_EXIT, false)){
				finish();
			}
			
		}
	}

	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

		this.context = this;
		
		
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group);
        
        tvTitle = (TextView)findViewById(R.id.textViewTitle);
        tvTitle.setVisibility(View.VISIBLE);
        current_group = 1;
        //tvTitle.setOnClickListener(lsrButton);
        
        GroupPreferences groupPref = new GroupPreferences(context);
        String left,right;
        int p = GroupPreferences.GROUP_PRIORITY_MAX;
        int serial = groupPref.getGroupSerialByPriority(p);
        if(serial > 0){
        	left = groupPref.getGroupNameBySerial(serial,p);
        }
        else{
        	left = groupPref.getGroupNameByPriority(p);
        }
        p = GroupPreferences.GROUP_PRIORITY_MIN;
        serial = groupPref.getGroupSerialByPriority(p);
        if(serial > 0){
        	right = groupPref.getGroupNameBySerial(serial,p);
        }
        else{
        	right = groupPref.getGroupNameByPriority(p);
        }
        
        tvLeft = (TextView)findViewById(R.id.textViewLeft);
        //tvLeft.setText(left);
        tvLeft.setVisibility(View.INVISIBLE);
        
        tvRight = (TextView)findViewById(R.id.textViewRight);
        //tvLeft.setText(right);
        tvRight.setVisibility(View.INVISIBLE);
        
        ibtnLeft = (ImageButton)findViewById(R.id.imageButtonLeft);
        //ibtnLeft.setOnClickListener(lsrButton);
        ibtnLeft.setVisibility(View.INVISIBLE);
        
        ibtnRight = (ImageButton)findViewById(R.id.imageButtonRight);
        //ibtnRight.setOnClickListener(lsrButton);
        ibtnRight.setVisibility(View.INVISIBLE);
        
        ((ImageView)findViewById(R.id.imageButtonExit)).setOnClickListener(lsrButton);
        
        ((ImageView)findViewById(R.id.imageButtonMy)).setOnClickListener(lsrButton);
        
        
        lytLeft = (RelativeLayout)findViewById(R.id.relativeLayoutLeft);
        //lytLeft.setOnClickListener(lsrButton);
        //lytLeft.setVisibility(View.GONE);
        
        lytRight = (RelativeLayout)findViewById(R.id.relativeLayoutRight);
        //lytRight.setOnClickListener(lsrButton);
        //lytRight.setVisibility(View.GONE);
        
        gridView = (GridView)findViewById(R.id.gridView);
        lstApps = new ArrayList<DBSchemaApplications.Columns>();
        adapter = new MainGridAdapter(context);
        gridView.setAdapter(adapter);
        gestureDetector = new GestureDetector(context, new GridGestureListener(context));
        gridView.setOnTouchListener(lsrTouch);
        
        gridView.setOnItemClickListener(lsrGridItem);
        
        //gridView.setVisibility(View.INVISIBLE);
        
        priority = 1;
        
        ivWallpapaer = (ImageView) findViewById(R.id.imageViewWallpaper); 
        this.wallpaperBitmap = null;
        //this.setWallpaper();
        
        GridView gv;
        gv = (GridView) findViewById(R.id.gridViewBar);
        gv.setAdapter(new GroupGridAdapter(context, 
        		new ArrayList<DBSchemaApplications.Columns>(),
        		null,
        		null,
        		new HashSet<GroupGridAdapter.IconInfo>()));
        gv.setOnTouchListener(lsrTouch);
        //gv.setVisibility(View.INVISIBLE);
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        
        /*
        in = AnimationUtils.loadAnimation(context, R.anim.flying);
        this.flying_item = (ImageView) findViewById(R.id.imageViewFlyingItem); 
        in.getInterpolator();
        this.flying_item.startAnimation(in); 
		*/
        
        //new IntentSender(context).startAllServie();
        
        
        try {
        	mLauncher = new Launcher(this);
        } catch (Exception e) {
        	Log.e(this.getClass().getName(), "Exception!: " + e.getMessage());
        }

	}
	


	private void setWallpaper(){
	    // 获取壁纸管理器  
		try{
			if(this.wallpaperBitmap==null){
		        WallpaperManager wallpaperManager = WallpaperManager  
		                .getInstance(context);  
		        // 获取当前壁纸  
		        Drawable wallpaperDrawable = wallpaperManager.getDrawable();  
		        
		        // 将Drawable,转成Bitmap  
		        wallpaperBitmap = ((BitmapDrawable) wallpaperDrawable).getBitmap();  
			}
			
	        // 需要详细说明一下，mScreenCount、getCurrentWorkspaceScreen()、mScreenWidth、mScreenHeight分别  
	        //对应于Launcher中的桌面屏幕总数、当前屏幕下标、屏幕宽度、屏幕高度.等下拿Demo的哥们稍微要注意一下  
	        float step = 0;  
	        // 计算出屏幕的偏移量  
	        DisplayMetrics dMetrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
	        int screenWidth = dMetrics.widthPixels;
	        int screenHeight = dMetrics.heightPixels;
	        
	        step = (wallpaperBitmap.getWidth() - screenWidth)  
	                / (GroupPreferences.GROUP_PRIORITY_MAX - 1);  
	        // 截取相应屏幕的Bitmap  
	        Bitmap pbm = Bitmap.createBitmap(wallpaperBitmap, (int) ((priority-1) * step), 0,  
	                new DevicePreferences(context).getScreenWidth(),  
	                new DevicePreferences(context).getScreenHeight());  
	        // 设置 背景  
	        ivWallpapaer.setImageBitmap(pbm);
		}
		catch(Exception e){
			e.printStackTrace();
		}
        //layout.setBackgroundDrawable(new BitmapDrawable(pbm));  
	}	

}
