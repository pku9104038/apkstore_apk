/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public abstract class HttpGetListener extends HttpListener {
	public HttpGetListener(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	/*
	 * CONSTANTS
	 */
	//public static final int GET_URL				= 0;
	//public static final int GET_RESP			= 1;
	
	/*
	 * PROPERTIES, PRIVATE
	 */
	
	private Context context;
	
	/*
	 * CONSTRUCTOR
	 */
	
	
	
	
	public void OnResponse(String[] response){
		OnHttpGetResponse(response);
	}
	
	
	public abstract void OnHttpGetResponse(String[] response);
}
