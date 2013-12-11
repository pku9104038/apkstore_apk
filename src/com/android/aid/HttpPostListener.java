/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public abstract class HttpPostListener extends HttpListener{
	
	public HttpPostListener(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	/*
	 * CONSTANTS
	 */
	//public static final int POST_URL			= 0;
	//public static final int POST_RESP			= 1;
	
	/*
	 * PROPERTIES, PRIVATE
	 */
	
	private Context context;
	
	/*
	 * CONSTRUCTOR
	 */
	
	
	
	public void OnResponse(String[] response){
		OnHttpPostResponse(response);
	}
	
	
	public abstract void OnHttpPostResponse(String[] response);

}
