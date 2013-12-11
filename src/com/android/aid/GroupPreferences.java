/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;

/**
 * @author wangpeifeng
 *
 */
public class GroupPreferences extends MainSharedPref {
	
	/*
	 * CONSTANTS, PUBLIC
	 */
	
	public static final int GROUP_PRIORITY_MIN					= 1;
	public static final int GROUP_PRIORITY_MAX					= 16;
	public static final int GROUP_SERIAL_DEFAULT				= 1;
	
	//public static final int GROUP_ICON_MAX_SIZE					= 72;
	
	
	public static final String PREF_KEY_GROUPS_SYSNC 			= "group_sync";
	public static final String VAL_DEF_GROUPS_SYSNC 			= "GROUP_NULL";
	public static final String ASSETS_GROUPS_SYSNC	 			= "api_group_sync.json";
	
	public static final String KEY_GROUP_SERIAL		 			= "group_serial";
	public static final String KEY_GROUP_NAME		 			= "group_name";
	public static final String KEY_GROUP_PRIORITY	 			= "priority";
	public static final String KEY_GROUP_ICON		 			= "icon";
	
	public static final String ID_STRING_GROUP_LAYOUT			= "RelativeLayoutGroup";
	public static final String ID_STRING_GROUP_TEXTVIEW			= "textViewGroup";
	public static final String ID_STRING_GROUP_IMAGEVIEW		= "imageViewGroup";
	public static final String ID_STRING_GROUP_IMAGEVIEWMARKER	= "imageViewGroupMarker";
	public static final String ID_STRING_GROUP_IMAGEVIEWFOCUS	= "imageViewFocus";
	public static final String ID_STRING_GROUP_STRING			= "group_";

	public static final String NAME_GROUP_ICONS					= "ic_group";
	public static final String DOT_PNG							= ".png";
	
	
	
	/*
	 * PROPERTIES, PRIVATE
	 */
	private AssetsHelper helper; 
	/*
	 * CONSTRUCTOR
	 */
	/**
	 * @param context
	 */
	public GroupPreferences(Context context) {
		super(context);
		this.helper = new AssetsHelper(context);
	}
	
	/*
	 * METHODS, PUBLIC
	 */
	
	public int getWidgetIconSize(){
		int size = 64;
		switch(new DevicePreferences(context).getDensityDpi()){
		case 120://DisplayMetrics.DENSITY_LOW:
			size = 32;
			break;
		case 160://DisplayMetrics.DENSITY_MEDIUM:
			size = 42;
			break;
		case 240://DisplayMetrics.DENSITY_HIGH:
			size = 64;
			break;
		case 360://DisplayMetrics.DENSITY_XHIGH:
			size = 84;
			break;
		case 480://DisplayMetrics.DENSITY_XXHIGH:
			size = 128;
			break;
			
		}
		//Log.i(this.getClass().getName(), "icon size:"+size);
		return size;
	}
	public void setPref(String strPref){
		
		int oldStamp = this.getPrefStamp(this.getPrefString());
		int newStamp = this.getPrefStamp(strPref);
		IntentSender sender = new IntentSender(context);
		
		if(newStamp > oldStamp ){
			Log.i(this.getClass().getName(), "setGroups!"+newStamp + "/" + oldStamp+":"+strPref.length());
			
			putString(PREF_KEY_GROUPS_SYSNC, strPref);
			//putString(PREF_KEY_GROUPS_SYSNC, this.getPrefString());
			
			//writePrefFile(strPref);
			//helper.saveStringFile(strPref, ASSETS_GROUPS_SYSNC);
			//helper.renameFile(ASSETS_GROUPS_SYSNC+".download", ASSETS_GROUPS_SYSNC);
			sender.broadcastUpdateWidgetLabel();
		}	
		/*
		if(newStamp > oldStamp || isBitmapFileNotAvailable()){
			
			DBSchemaGUIDownload schema = new DBSchemaGUIDownload(this.context);
			schema.addDownloadList(this.getIconFileList());

			Log.i(this.getClass().getName(), "start gui download");
			sender.startGUIDownload();
			
		
		}
		*/
	}
	
	private void writePrefFile(String strPref){
		//AssetsHelper helper = new AssetsHelper(context);
		helper.saveStringFile(strPref, ASSETS_GROUPS_SYSNC);
		//new StampPreferences(context).setGroupSyncStamp();
	}
	
	public String getPrefString(){
		
		String string =  getString(PREF_KEY_GROUPS_SYSNC, VAL_DEF_GROUPS_SYSNC);
		
		//AssetsHelper helper = new AssetsHelper(context);
		//String string =  helper.readStringFile(ASSETS_GROUPS_SYSNC);

		if(VAL_DEF_GROUPS_SYSNC.equals(string)){
		//if(AssetsHelper.ASSETS_STRING_NULL.equals(string)){
			string = new AssetsHelper(context).getAssetsString(ASSETS_GROUPS_SYSNC);
			putString(PREF_KEY_GROUPS_SYSNC, string);
			//helper.saveStringFile(string, ASSETS_GROUPS_SYSNC);
			
		}
		//helper.saveStringFile(string, ASSETS_GROUPS_SYSNC);
		
		
		return string;
	}
	
	public int getPrefStamp(String strPref){
		int intResult = 0;
		
		try{
			JSONObject objGroups = new JSONObject(strPref);
			intResult = objGroups.getInt(WebServiceApi.API_RESP_STAMP);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return intResult;
	}
	
	public int getGroupLayoutId(int priority){
		return this.context.getResources().getIdentifier(ID_STRING_GROUP_LAYOUT+priority, "id", "com.android.aid");
	}
	
	public int getGroupTextViewId(int priority){
		return this.context.getResources().getIdentifier(ID_STRING_GROUP_TEXTVIEW+priority, "id", "com.android.aid");
	}
	
	public int getGroupDefaultString(int priority){
		return this.context.getResources().getIdentifier(ID_STRING_GROUP_STRING+priority, "string", "com.android.aid");
	}
	
	
	public int getGroupImageViewId(int priority){
		return this.context.getResources().getIdentifier(ID_STRING_GROUP_IMAGEVIEW+priority, "id", "com.android.aid");
	}
	
	public int getGroupImageViewMarkerId(int priority){
		return this.context.getResources().getIdentifier(ID_STRING_GROUP_IMAGEVIEWMARKER+priority, "id", "com.android.aid");
	}
	
	public int getGroupImageViewFocusId(int priority){
		return this.context.getResources().getIdentifier(ID_STRING_GROUP_IMAGEVIEWFOCUS+priority, "id", "com.android.aid");
	}
	
	public String getGroupNameByPriority(int priority){
		
		SparseArray<String> array = this.mapGroupKeyValue(KEY_GROUP_PRIORITY, KEY_GROUP_NAME);
	    
		String string =  array.get(priority);
		if(string == null){
			string = this.context.getResources().getString(this.getGroupDefaultString(priority));
		}
		
		return string;
	}
	
	public boolean isBitmapFileNotAvailable(){
		boolean bool = false;
		String path = new StoragePreferences(context).getGuiDir();
		for(int i = GROUP_PRIORITY_MIN; i<=GROUP_PRIORITY_MAX; i++){
			String fileName = NAME_GROUP_ICONS+i+DOT_PNG;
			String pathName = path + fileName;
			if(!(new File(pathName).exists())){
				bool = true;
			}
			
		}
		
		return bool;
	}
	
	public Bitmap getGroupBitmapByPriority(int priority){
		
		String assetsName = NAME_GROUP_ICONS+priority+DOT_PNG;
		Bitmap bmp = null;
		String pathName = new StoragePreferences(context).getGuiDir() + assetsName;
		if(new File(pathName).exists() ){
			bmp = BitmapFactory.decodeFile(pathName);
			if(bmp != null){
//				if(bmp.getHeight() > getWidgetIconSize() || 
//						bmp.getWidth() > getWidgetIconSize()){
					
					new AssetsHelper(context).resizePngIconFile(pathName, getWidgetIconSize());
					bmp = BitmapFactory.decodeFile(pathName);
//				}
			}
		}
		if(bmp == null){
			int id =this.context.getResources().getIdentifier(NAME_GROUP_ICONS+priority, "drawable", "com.android.aid");
			bmp = BitmapFactory.decodeResource(this.context.getResources(), id);
			new AssetsHelper(context).savePngIconFile(bmp, pathName, getWidgetIconSize());
			bmp = BitmapFactory.decodeFile(pathName);

		}
		if(bmp == null){
			bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.icon);
			new AssetsHelper(context).savePngIconFile(bmp, pathName, getWidgetIconSize());
			bmp = BitmapFactory.decodeFile(pathName);
		}
		
		return bmp;
	}
	
	public int getGroupResIDByPriority(int priority){
		
		String resourceName = NAME_GROUP_ICONS+priority;		
		return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
	}
	
	
	public String getGroupUriByPriority(int priority){
		String result = null;
		String assetsName = NAME_GROUP_ICONS+priority+DOT_PNG;
		String pathName = new StoragePreferences(context).getGuiDir() + assetsName;
		Bitmap bmp = null;
		if(new File(pathName).exists() ){
			bmp = BitmapFactory.decodeFile(pathName);
			if(bmp != null){
				result = pathName;
			}
			else{
				new File(pathName).delete();
			}
		}
		if(result==null){
			int id =this.context.getResources().getIdentifier(NAME_GROUP_ICONS+priority, "drawable", "com.android.aid");
			bmp = BitmapFactory.decodeResource(this.context.getResources(), id);
			new AssetsHelper(context).savePngIconFile(bmp, pathName, getWidgetIconSize());
			bmp = BitmapFactory.decodeFile(pathName);
			if(bmp == null){
				bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.icon);
				new AssetsHelper(context).savePngIconFile(bmp, pathName, getWidgetIconSize());
				bmp = BitmapFactory.decodeFile(pathName);
			}
			if(bmp != null){
				result = pathName;
			}
		}
		return result;
	}

	public int getGroupSerialByPriority(int priority){
		int serial = 1;
		try{
		    SparseArray<String> array = this.mapGroupKeyValue(KEY_GROUP_PRIORITY, KEY_GROUP_SERIAL);
		    serial =  Integer.parseInt(array.get(priority));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return serial;
	}

	public String getGroupNameBySerial(int serial,int priority){
		String string = "";
		try{
		    SparseArray<String> array = this.mapGroupKeyValue(KEY_GROUP_SERIAL, KEY_GROUP_NAME);
		    string =  array.get(serial);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return string;
	}
	
	public int getGroupPriorityById(int id){
		int priority = 0;
		String id_entry = context.getResources().getResourceEntryName(id);
		priority = Integer.parseInt(id_entry.substring(ID_STRING_GROUP_LAYOUT.length()));
        return priority;
	}
	
	public int getGroupPriorityByImageId(int id){
		int priority = 0;
		String id_entry = context.getResources().getResourceEntryName(id);
		priority = Integer.parseInt(id_entry.substring(ID_STRING_GROUP_IMAGEVIEW.length()));
        return priority;
	}

	/*
	 * METHODS, PRIVATE
	 */
	
	private SparseArray<String> mapGroupPriorityName(){
		SparseArray<String> array = new SparseArray<String>();
		try{
			JSONArray jsonarray = this.getGroupsJasonArray();
			if(jsonarray != null){
				for(int i = 0; i < jsonarray.length(); i++){
					JSONObject obj = (JSONObject) jsonarray.get(i);
					int key = Integer.parseInt(obj.getString(KEY_GROUP_PRIORITY));
					String value = obj.getString(KEY_GROUP_NAME);
					array.put(key, value);
					
				}
			}
			
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return array;
	}
	
	private SparseArray<String> mapGroupPrioritySerial(){
		SparseArray<String> array = new SparseArray<String>();
		try{
			JSONArray jsonarray = this.getGroupsJasonArray();
			if(jsonarray != null){
				for(int i = 0; i < jsonarray.length(); i++){
					JSONObject obj = (JSONObject) jsonarray.get(i);
					int key = Integer.parseInt(obj.getString(KEY_GROUP_PRIORITY));
					String value = obj.getString(KEY_GROUP_SERIAL);
					array.put(key, value);
					
				}
			}
			
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return array;
	}

	public String[][] getIconFileList(){
		
		SparseArray<String> array = this.mapGroupPriorityIcon();
		
		String[][] fileList = new String[GROUP_PRIORITY_MAX-GROUP_PRIORITY_MIN+1][2];
		int i = 0;
		for(int priority=GROUP_PRIORITY_MIN; priority<=GROUP_PRIORITY_MAX; priority++){
			String file_from =  array.get(priority);
			String file_to = NAME_GROUP_ICONS+priority+DOT_PNG;
			fileList[i][0] = file_from;
			fileList[i][1] = file_to;
			i++;
	
		}
	    return fileList;
	}

	private SparseArray<String> mapGroupPriorityIcon(){
		SparseArray<String> array = new SparseArray<String>();
		try{
			JSONArray jsonarray = this.getGroupsJasonArray();
			if(jsonarray != null){
				for(int i = 0; i < jsonarray.length(); i++){
					JSONObject obj = (JSONObject) jsonarray.get(i);
					int key = Integer.parseInt(obj.getString(KEY_GROUP_PRIORITY));
					String value = obj.getString(KEY_GROUP_ICON);
					array.put(key, value);
					
				}
			}
			
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return array;
	}
	
	/*
	 * METHODS, PRIVATE
	 */
	
	private JSONArray getGroupsJasonArray(){
		JSONArray array = null;
		
		try{
			JSONObject objGroups = new JSONObject(this.getPrefString());
			array = objGroups.getJSONArray(WebServiceApi.API_RESP_ARRAY);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return array;
	}

	private SparseArray<String> mapGroupSerialName(){
		SparseArray<String> array = new SparseArray<String>();
		try{
			JSONArray jsonarray = this.getGroupsJasonArray();
			if(jsonarray != null){
				for(int i = 0; i < jsonarray.length(); i++){
					JSONObject obj = (JSONObject) jsonarray.get(i);
					int key = Integer.parseInt(obj.getString(KEY_GROUP_SERIAL));
					String value = obj.getString(KEY_GROUP_NAME);
					array.put(key, value);
					
				}
			}
			
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return array;
	}
	
	private SparseArray<String> mapGroupKeyValue(String strKey, String strValue){
		SparseArray<String> array = new SparseArray<String>();
		try{
			JSONArray jsonarray = this.getGroupsJasonArray();
			if(jsonarray != null){
				for(int i = 0; i < jsonarray.length(); i++){
					JSONObject obj = (JSONObject) jsonarray.get(i);
					
					int key = Integer.parseInt(obj.getString(strKey));
					String value = obj.getString(strValue);
					array.put(key, value);
				}
			}
			
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return array;
	}

	

	/**
	 * getPrefFileName()
	 */
	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "groups_pref";
	}

}
