package com.easylite;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class ManifestUtil {

	public final static String METADATA_DATABASE = "DATABASE";
	public final static String METADATA_VERSION = "VERSION";
	public final static String METADATA_MODEL_PACKAGE_NAME = "MODEL_PACKAGE_NAME";
	private static final String DEFAULT_DATABASE = "easylite.db";
	
	/**
	 * Get database version 
	 * @author Mario Dennis
	 * @param context android
	 * @return  1 when none is defined within manifest
	 */
	public static int getDatabaseVersion (Context context){
		Integer version = getMetaDataInteger(context, METADATA_VERSION);
		if (version == null || version == 0)
			return 1;
		return version;
	}
	
	
	/**
	 * Get database name
	 * @author Mario Dennis
	 * @param context context
	 * @return databaseName or default when none is defined
	 */
	public static String getDatabaseName (Context context){
		String databaseName = getMetaDataString(context, METADATA_DATABASE);
		if (databaseName == null)
			return DEFAULT_DATABASE;
		else 
			return databaseName;
	}
	
	/**
	 * Get package name of domain entity classes
	 * @author Mario Dennis
	 * @param context android
	 * @return empty when not defined in manifest
	 */
	public static String getModelPackageName (Context context){
		String packageName = getMetaDataString(context, METADATA_MODEL_PACKAGE_NAME);
		if (packageName == null)
			return "";
		return packageName;
	}
	
	// Get MetaData from Manifest file
	private static String getMetaDataString(Context context, String name) {
		String value = null;
		PackageManager pm = context.getPackageManager();
		try {
		    ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
		    value = ai.metaData.getString(name);
		} catch (Exception e) {
		    Log.d("EasyLite", "Couldn't find config value: " + name);
		}
		return value;
    }
	
	
	// Get MetaData from Manifest File
	private static Integer getMetaDataInteger(Context context, String name) {
		Integer value = null;
		PackageManager pm = context.getPackageManager();
		try {
		    ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
		    value = ai.metaData.getInt(name);
		} catch (Exception e) {
		    Log.d("EasyLite", "Couldn't find config value: " + name);
		}
		return value;
	}
}
