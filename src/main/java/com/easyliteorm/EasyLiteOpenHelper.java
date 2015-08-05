package com.easyliteorm;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.easyliteorm.annotation.Entity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import dalvik.system.DexFile;

public final class EasyLiteOpenHelper extends SQLiteOpenHelper {
	private Set<Class<?>> entityClasses;
	private String msg = "No entity class was found in package %s. Please ensure correct package defined in Manifest";
	
	protected EasyLiteOpenHelper(Context context) {
		super(context,ManifestUtil.getDatabaseName(context),null,ManifestUtil.getDatabaseVersion(context));
		this.entityClasses = getDomainClasses(context);
		if (entityClasses.isEmpty())
			Log.e("EasyLite",String.format(msg, ManifestUtil.getModelPackageName(context)));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (Class<?> clazz : entityClasses)
			Table.createTable(db, clazz);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(Class<?> clazz : entityClasses)
			Table.dropTable(db, clazz);
		
		onCreate(db);//recreates database schema
	}
	
	public Set<Class<?>> getEntityClasses (){
		return entityClasses;
	}

	/**
	 * Get Domain Entity Classes
	 * @author Mario
	 * @param context android
	 * @return Set of entity classes
	 */
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

	/**
	 * Get Domain class
	 * @author Mario Dennis
	 * @param className for class
	 * @param context android
	 * @return Class entity class or null if not a entity class
	 */
	private static Class<?> getDomainClass(String className, Context context) {
		Class<?> discoveredClass = null;
		try {
			discoveredClass = Class.forName(className, true, context.getClass().getClassLoader());
			if (discoveredClass != null && discoveredClass.isAnnotationPresent(Entity.class))
				return discoveredClass;    
		} catch (ClassNotFoundException e) {
			Log.e("EasyLite", e.getMessage());
		}
		return null;
	}

	 /**
	  * Get All classes 
	  * @author Mario Dennis
	  * @param context android
	  * @return List of all classes
	  * @throws PackageManager.NameNotFoundException
	  * @throws IOException
	  */
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
					 File[] classfiles = classDirectory.listFiles();
					 if (classfiles == null)
						 break;
					 
					 for (File filePath : classfiles) {
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
