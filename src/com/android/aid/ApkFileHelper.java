/**
 * 
 */
package com.android.aid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ApkFileHelper extends FileHelper{
	
	public class ApkInfos{
		private String path;
		private String packgeName;
		private String label;
		private int verCode;
		private String verName;
		private Drawable icon;
		
		public ApkInfos(){
			this.path = "";
			this.packgeName = "";
			this.label = "";
			this.verCode = 0;
			this.verName = "";
			this.icon = null;
		}
		
		public String getPath(){	return this.path;	}
		public void setPath(String path){	this.path = path;	}
		
		public String getPackage(){	return this.packgeName;	}
		public void setPackage(String packageName){	this.packgeName = packageName;	}
		
		public String getLabel(){	return this.label;	}
		public void setLabel(String label){	this.label = label;	}
		
		public String getVerName(){	return this.verName;	}
		public void setVerName(String verName){	this.verName = verName;	}
		
		public int getVerCode(){	return this.verCode;	}
		public void setVerCode(int verCode){	this.verCode = verCode;	}
		
		public Drawable getIcon(){	return this.icon;	}
		public void setIcon(Drawable icon){	this.icon = icon;	}
		
	}

	public ApkFileHelper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPath() {
		// TODO Auto-generated method stub
		return StoragePreferences.getApkDir();
	}

	@Override
	protected String getExtension() {
		// TODO Auto-generated method stub
		return ".apk";//lowercase
	}
	
	public ApkInfos getApkInfos(String apkPath){
		ApkInfos apkInfos = new ApkInfos();
		
		File apkFile = new File(apkPath);  
	    if (apkFile.exists() && apkPath.toLowerCase().endsWith(getExtension())) {  
	        
	    	String PATH_PackageParser = "android.content.pm.PackageParser";  
	        String PATH_AssetManager = "android.content.res.AssetManager";  

	        try {  
	            Class<?> pkgParserCls = Class.forName(PATH_PackageParser);  
	            Class<?>[] typeArgs = {String.class};  
	            Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);  
	            Object[] valueArgs = {apkPath};  
	            Object pkgParser = pkgParserCt.newInstance(valueArgs);  
	              
	            DisplayMetrics metrics = new DisplayMetrics();  
	            metrics.setToDefaults();  
	            typeArgs = new Class<?>[]{File.class,String.class,  
	                                    DisplayMetrics.class,int.class};  
	            Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(  
	                    "parsePackage", typeArgs);  
	              
	            valueArgs=new Object[]{new File(apkPath),apkPath,metrics,0};  
	              
	            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,  
	                    valueArgs);  
	              
	            if (pkgParserPkg!=null) {  
	            	Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(  
	                    "applicationInfo");  
	              
	            	if (appInfoFld.get(pkgParserPkg)!=null) {  
	                
	            		ApplicationInfo info = (ApplicationInfo) appInfoFld  
	            				.get(pkgParserPkg);           
	              
	            		Class<?> assetMagCls = Class.forName(PATH_AssetManager);            
	            		Object assetMag = assetMagCls.newInstance();  
	            		
	            		typeArgs = new Class[1];  
	            		typeArgs[0] = String.class;  
	            		Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(  
	            				"addAssetPath", typeArgs);  
	            		valueArgs = new Object[1];  
	            		valueArgs[0] = apkPath;  
	            		assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);  
	              
	              
	            		Resources res = context.getResources();  
	            		typeArgs = new Class[3];  
	            		typeArgs[0] = assetMag.getClass();  
	            		typeArgs[1] = res.getDisplayMetrics().getClass();  
	            		typeArgs[2] = res.getConfiguration().getClass();  
	            		Constructor<Resources> resCt = Resources.class  
	            				.getConstructor(typeArgs);  
	            		valueArgs = new Object[3];  
	            		valueArgs[0] = assetMag;  
	            		valueArgs[1] = res.getDisplayMetrics();  
	            		valueArgs[2] = res.getConfiguration();  
	            		res = (Resources) resCt.newInstance(valueArgs);  
	              
	              
	            		if (info!=null) {  
	            			if (info.icon != 0) {
	            				apkInfos.setIcon( res.getDrawable(info.icon));  
	            				  
	            			}  
	            			if (info.labelRes != 0) {  
	            				apkInfos.setLabel((String) res.getText(info.labelRes)); 
	            			}
	            			else {  
	            				String apkName=apkFile.getName();  
	            				apkInfos.setLabel(apkName.substring(0,apkName.lastIndexOf(".")));  
	            			}  
	            			apkInfos.setPackage(info.packageName); 
	            		}

	            		PackageManager pm = context.getPackageManager();  
	            		PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);  
	            		if (packageInfo != null) {  
	            			apkInfos.setVerName(packageInfo.versionName);
	            			apkInfos.setVerCode(packageInfo.versionCode);
	            		}
	            		apkInfos.setPath(apkPath);
	            	}
	            }
	        } catch (Exception e) {   
	            e.printStackTrace();  
	        }  
	    }		
		return apkInfos;
	}

	public ApkInfos getApkBasicInfos(String apkPath){
		ApkInfos apkInfos = new ApkInfos();
		
		File apkFile = new File(apkPath);  
	    if (apkFile.exists() && apkPath.toLowerCase().endsWith(getExtension())) {  
	        
	    	String PATH_PackageParser = "android.content.pm.PackageParser";  
	        String PATH_AssetManager = "android.content.res.AssetManager";  

	        try {  
	            Class<?> pkgParserCls = Class.forName(PATH_PackageParser);  
	            Class<?>[] typeArgs = {String.class};  
	            Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);  
	            Object[] valueArgs = {apkPath};  
	            Object pkgParser = pkgParserCt.newInstance(valueArgs);  
	              
	            DisplayMetrics metrics = new DisplayMetrics();  
	            metrics.setToDefaults();  
	            typeArgs = new Class<?>[]{File.class,String.class,  
	                                    DisplayMetrics.class,int.class};  
	            Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(  
	                    "parsePackage", typeArgs);  
	              
	            valueArgs=new Object[]{new File(apkPath),apkPath,metrics,0};  
	              
	            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,  
	                    valueArgs);  
	              
	            if (pkgParserPkg!=null) {  
	            	Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(  
	                    "applicationInfo");  
	              
	            	if (appInfoFld.get(pkgParserPkg)!=null) {  
	                
	            		ApplicationInfo info = (ApplicationInfo) appInfoFld  
	            				.get(pkgParserPkg);           
	              
	            		Class<?> assetMagCls = Class.forName(PATH_AssetManager);            
	            		Object assetMag = assetMagCls.newInstance();  
	            		
	            		typeArgs = new Class[1];  
	            		typeArgs[0] = String.class;  
	            		Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(  
	            				"addAssetPath", typeArgs);  
	            		valueArgs = new Object[1];  
	            		valueArgs[0] = apkPath;  
	            		assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);  
	              
	              
	            		Resources res = context.getResources();  
	            		typeArgs = new Class[3];  
	            		typeArgs[0] = assetMag.getClass();  
	            		typeArgs[1] = res.getDisplayMetrics().getClass();  
	            		typeArgs[2] = res.getConfiguration().getClass();  
	            		Constructor<Resources> resCt = Resources.class  
	            				.getConstructor(typeArgs);  
	            		valueArgs = new Object[3];  
	            		valueArgs[0] = assetMag;  
	            		valueArgs[1] = res.getDisplayMetrics();  
	            		valueArgs[2] = res.getConfiguration();  
	            		res = (Resources) resCt.newInstance(valueArgs);  
	              
	              
	            		if (info!=null) {  
	            			if (info.icon != 0) {
//	            				apkInfos.setIcon( res.getDrawable(info.icon));  
	            			}  
	            			if (info.labelRes != 0) {  
	            				apkInfos.setLabel((String) res.getText(info.labelRes)); 
	            			}
	            			else {  
	            				String apkName=apkFile.getName();  
	            				apkInfos.setLabel(apkName.substring(0,apkName.lastIndexOf(".")));  
	            			}  
	            			apkInfos.setPackage(info.packageName); 
	            		}

	            		PackageManager pm = context.getPackageManager();  
	            		PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);  
	            		if (packageInfo != null) {  
	            			apkInfos.setVerName(packageInfo.versionName);
	            			apkInfos.setVerCode(packageInfo.versionCode);
	            		}
	            		apkInfos.setPath(apkPath);
	            	}
	            }
	        } catch (Exception e) {   
	            e.printStackTrace();  
	        }  
	    }		
		return apkInfos;
	}
	
	public ArrayList<ApkInfos> getApkInfos(){
		ArrayList<ApkInfos> lstInfos = new ArrayList<ApkInfos>();
		ArrayList<String> lstFiles = getFiles();
		for(int i=0; i<lstFiles.size(); i++){
			ApkInfos info = getApkInfos(lstFiles.get(i));
			lstInfos.add(info);
		}
		
		return lstInfos;
	}

	public ArrayList<ApkInfos> getApkBasicInfos(){
		ArrayList<ApkInfos> lstInfos = new ArrayList<ApkInfos>();
		ArrayList<String> lstFiles = getFiles();
		for(int i=0; i<lstFiles.size(); i++){
			ApkInfos info = getApkBasicInfos(lstFiles.get(i));
			lstInfos.add(info);
		}
		
		return lstInfos;
	}
	
	public String getApkFile(String packageName){
		String string = null;
		ArrayList<ApkInfos> lstApkInfos = getApkBasicInfos();
		for(int i=0; i< lstApkInfos.size(); i++){
			if(lstApkInfos.get(i).getPackage().equals(packageName)){
				string = lstApkInfos.get(i).getPath();
				break;
			}
		}
		return string;
	}
	
	public void removeApkFile(String packageName){
		ArrayList<ApkInfos> lstApkInfos = getApkBasicInfos();
		for(int i=0; i< lstApkInfos.size(); i++){
			if(lstApkInfos.get(i).getPackage().equals(packageName)){
				File file = new File(lstApkInfos.get(i).getPath());
				if(file.exists()){
					file.delete();
				}
			}
		}
	}
	
	public boolean  isFileDownloaded(String filename){
		boolean result = false;
		String path = StoragePreferences.getApkDir()+filename+StoragePreferences.DOWNLOAD_SUFFIX;
		File file = new File(path);
		if(file.exists()){
			result = true;
		}
		return result;
	}

	public void removeDownloadFile(String filename){
		String path = StoragePreferences.getApkDir()+filename+StoragePreferences.DOWNLOAD_SUFFIX;
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
	}

	public long getApkDownloadSize(String file){
		long size = 0;
		String path = StoragePreferences.getApkDir()+file;
		//Log.i(this.getClass().getName(), path);
		if(new File(path).exists()){
			size = -1;
		}
		else {
			File downloadFile = new File(path
					+ StoragePreferences.getDownloadSuffix());
			if(downloadFile.exists()){
				size = downloadFile.length();
			}
			
		}
		return size;
	}
}
