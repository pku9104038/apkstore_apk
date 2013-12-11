/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public abstract class ThreadBasic extends Thread {
	
	private static final long TIMER_ZERO			= 0;
	
	protected Context context;
	protected boolean running;
	protected boolean pause;
	protected boolean run_once;
	protected long sleep_timer;
	
	
	/**
	 * @param context
	 */
	
	public ThreadBasic(Context context, long timer) {
		super();
		this.context = context;
		this.sleep_timer = timer;
		this.running = false;
		this.run_once = false;
		this.pause = false;
	}

	protected void startRunning(){
		this.running = true;
		resumeRunning();
		try{
	    	if(this.getState().equals(Thread.State.NEW)){
	    		this.start();
	    	}
	    	else {
	    		this.interrupt();
	    	}
		}
		catch(Exception e){
			e.printStackTrace();
			//Log.i(this.getClass().getName(), "Exception:"+e.getCause() + ","+ e.getMessage());
		}
	}
	
	protected void startRunningOnce(){
		this.running = true;
		this.run_once = true;
		resumeRunning();
		try{
	    	if(this.getState().equals(Thread.State.NEW)){
	    		this.start();
	    	}
	    	else {
	    		this.interrupt();
	    	}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void stopRunning(){
		this.running = false;
		pauseRunning();
		this.sleep_timer = TIMER_ZERO;
		try{
			this.interrupt();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void pauseRunning(){
		this.pause = true;
	}
	
	protected void resumeRunning(){
		this.pause = false;
		try{
			this.interrupt();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected boolean isPaused(){
		return this.pause;
	}
	
	protected void setSleepTimer(long timer){
		this.sleep_timer = timer;
	}
	
	protected void goSleep(){
		if(!run_once){
			if(this.sleep_timer>TIMER_ZERO){
				try{
					//Log.i(this.getClass().getName(),this.toString()+"go sleep");
					sleep(this.sleep_timer);
					//Log.i(this.getClass().getName(),this.toString()+"wake up");
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		else{
			this.running = false;
		}
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			super.run();
			while(running){
				try{	
					if(!isPaused()){
						actionWithPause();
					}
					actionWithoutPaused();
					
					this.goSleep();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		catch(Exception eThread){
			eThread.printStackTrace();
		}
		finally{
			running = false;
		}
	}
	
	
	protected abstract void actionWithPause();

	protected abstract void actionWithoutPaused();

}
