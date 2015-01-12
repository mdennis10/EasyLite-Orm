package com.easylite;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final class EasyLiteOpenHelper extends SQLiteOpenHelper {
	private final Context context;
	protected EasyLiteOpenHelper(Context context, String dbName,int version) {
		super(context,dbName,null,version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Set<Class<?>> entityClasses = getDomainClasses(context);
		for (Class<?> clazz : entityClasses)
			Table.createTable(db, clazz);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Set<Class<?>> entityClasses = getDomainClasses(context);
		for(Class<?> clazz : entityClasses)
			Table.dropTable(db, clazz);
	}

	
	public static Set<Class<?>> getDomainClasses(Context context) {
        Set<Class<?>> domainClasses = new HashSet<Class<?>>();
        try {
            for (String className : getAllClasses(context)) {
                if (className.startsWith(ManifestUtil.getModelPackageName(context))) {
                    Class<?> domainClass = getDomainClass(className, context);
                    if (domainClass != null) 
                    	domainClasses.add(domainClass);
                }
            }
        } catch (IOException e) {
            Log.e("EasyLite", e.getMessage());
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("EasyLite", e.getMessage());
        }
        return domainClasses;
    }
	
	 private static Class<?> getDomainClass(String className, Context context) {
		Class<?> discoveredClass = null;
		try {
		    discoveredClass = Class.forName(className, true, context.getClass().getClassLoader());
			if (discoveredClass != null && discoveredClass.isAnnotationPresent(com.easylite.annotation.Table.class))
			    return discoveredClass;    
		} catch (ClassNotFoundException e) {
		    Log.e("EasyLite", e.getMessage());
		}
		return null;
	}
	 
	 private static List<String> getAllClasses(Context context) throws PackageManager.NameNotFoundException, IOException {
		String path = getSourcePath(context);
		List<String> classNames = new ArrayList<String>();
		try {
		    DexFile dexfile = new DexFile(path);
		    Enumeration<String> dexEntries = dexfile.entries();
		    while (dexEntries.hasMoreElements()) 
		        classNames.add(dexEntries.nextElement());
		}catch (NullPointerException e) {
		    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		    Enumeration<URL> urls = classLoader.getResources("");
		    List<String> fileNames = new ArrayList<String>();
		   
		    while (urls.hasMoreElements()) {
		    	String classDirectoryName = urls.nextElement().getFile();
		    	if (classDirectoryName.contains("bin") || classDirectoryName.contains("classes")) {
		    		File classDirectory = new File(classDirectoryName);
		    		for (File filePath : classDirectory.listFiles()) {
		    			populateFiles(filePath, fileNames, "");
		            }
		            classNames.addAll(fileNames);
		        }
		    }
		}
		return classNames;
	 }
	 
	 private static void populateFiles(File path, List<String> fileNames, String parent) {
		if (path.isDirectory()) {
		    for (File newPath : path.listFiles()) {
		        if ("".equals(parent)) 
		        	populateFiles(newPath, fileNames, path.getName());
		        else 
		        	populateFiles(newPath, fileNames, parent + "." + path.getName());
		        
		    }
		} 
		else {
		    String pathName = path.getName();
		    String classSuffix = ".class";
		    pathName = pathName.endsWith(classSuffix) ? pathName.substring(0, pathName.length() - classSuffix.length()) : pathName;
		    if ("".equals(parent)) 
		    	fileNames.add(pathName);
		    else 
		    	fileNames.add(parent + "." + pathName);
		}
	}	
	 
	 private static String getSourcePath(Context context) throws PackageManager.NameNotFoundException {
	        return context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
	 }
}
