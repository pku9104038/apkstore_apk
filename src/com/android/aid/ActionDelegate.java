/**
 * 
 */
package com.android.aid;

import android.content.Context;
import android.content.Intent;

/**
 * @author wangpeifeng
 *
 */
public abstract class ActionDelegate {

	/*
	 * PROPERTIES, PRIVATE
	 */
	protected String requestAction;
	
	protected Context context;
	protected Intent intentRequest;
	
	
	/*
	 * CONSTRUCTOR
	 */
	
	/**
	 * BaseDelegatee
	 * @param requestAction
	 */
	
	public ActionDelegate(String requestAction){
		this.requestAction = requestAction;
	}
	
	/*
	 * METHODS, PROTECTED
	 */
	/**
	 * getRequestAction
	 * @return
	 */
	protected String getRequestAction(){
		return this.requestAction;
	}
	
	/**
	 * 
	 * @param context
	 */
	protected void setContext(Context context){
		this.context = context;
	}
	
	
	protected Context getContext(){
		return this.context;
	}
	/**
	 * 
	 * @param context
	 * @param intent
	 * @return
	 */
	public abstract boolean checkRequestAction(Context context, Intent intent);
	
	/**
	 * action()
	 */
	public abstract void action();

}
