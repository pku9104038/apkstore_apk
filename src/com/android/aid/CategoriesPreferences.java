/**
 * 
 */
package com.android.aid;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * @author wangpeifeng
 *
 */
public class CategoriesPreferences extends MainSharedPref{

	public static final String PREF_KEY_CATEGORIES_SYSNC 		= "group_sync";
	public static final String VAL_DEF_CATEGORIES_SYSNC 		= "CATEGORIES_NULL";

	public static final String KEY_CATEGORY_SERIAL				= "category_serial";
	private static final String KEY_CATEGORY_ARRAY				= "category_array";

	public CategoriesPreferences(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPrefFileName() {
		// TODO Auto-generated method stub
		return "categories.pref";
	}
	
	public String getPref(){
		return  getString(PREF_KEY_CATEGORIES_SYSNC, VAL_DEF_CATEGORIES_SYSNC);
		
	}
	
	public void setPref(String strPref){
		putString(PREF_KEY_CATEGORIES_SYSNC, strPref);
	}

	
	private JSONArray getGroupsCategories(){
		JSONArray array = null;
		
		try{
			array = new JSONArray(this.getPref());
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		
		return array;
	}
	
	private JSONArray getCategories(int group_serial,JSONArray groups_categories){
		JSONArray array = null;
		try{
			for(int i = 0; i<array.length(); i++){
				JSONObject obj = groups_categories.getJSONObject(i);
				if(group_serial == obj.getInt(GroupPreferences.KEY_GROUP_SERIAL)){
					array = obj.getJSONArray(KEY_CATEGORY_ARRAY);
				}
			}
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		
		return array;
	}
}
