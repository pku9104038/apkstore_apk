/**
 * 
 */
package com.android.aid;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class VersionPreferences extends MainSharedPref{

	private static final String PREF_KEY_VER_DOWNLOADING_FILE	= "downloading_file";
	private static final String PREF_KEY_VER_DOWNLOADED_FILE	= "downloaded_file";
	private static final String VAL_DEF_VERSION_FILE	= VAL_DEF_DEV_INFO_NULL;
	
	public VersionPreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "pref_version";
	}
	
	public String getVerDownloadedFile(){
		return getString(PREF_KEY_VER_DOWNLOADED_FILE, VAL_DEF_VERSION_FILE);
	}
	
	public void setVerDownloadedFile(String file){
		putString(PREF_KEY_VER_DOWNLOADED_FILE,file);
	}
	public String getVerDownloadingFile(){
		return getString(PREF_KEY_VER_DOWNLOADING_FILE, VAL_DEF_VERSION_FILE);
	}
	
	public void setVerDownloadingFile(String file){
		putString(PREF_KEY_VER_DOWNLOADING_FILE,file);
	}

}
