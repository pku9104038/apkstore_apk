/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public abstract class HttpListener {
	/*
	 * PROPERTIES, PRIVATE
	 */
	public static final int HTTP_URL				= 0;
	public static final int HTTP_RESP				= 1;
	
	private Context context;
	
	/*
	 * CONSTRUCTOR
	 */
	public HttpListener(Context context){
		this.context = context;
	}
	
	public Context getContext(){
		return this.context;
	}
	
	
	public abstract void OnResponse(String[] response);

}
