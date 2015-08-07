package com.easyliteorm;

import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final class EasyLiteOpenHelper extends SQLiteOpenHelper {
	private Set<Class<?>> entityClasses;
	private final String msg = "No entity class was found in package %s. Please ensure correct package defined in Manifest";
	private final SqliteTypeRegistry sqliteTypeRegistry;
	
	protected EasyLiteOpenHelper(Context context, SqliteTypeRegistry sqliteTypeRegistry) {
		super(context,ManifestUtil.getDatabaseName(context),null,ManifestUtil.getDatabaseVersion(context));
		
		this.sqliteTypeRegistry = sqliteTypeRegistry;
		this.entityClasses = EntityScanner.getDomainClasses(context);
		
		if (entityClasses.isEmpty())
			Log.e("EasyLite",String.format(msg, ManifestUtil.getModelPackageName(context)));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableUtil table = new TableUtil();
		for (Class<?> clazz : entityClasses)
			table.createTable(db, clazz);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		TableUtil table = new TableUtil();
		for(Class<?> clazz : entityClasses)
			table.dropTable(db, clazz);
		
		onCreate(db);//recreates database schema
	}
	
	public Set<Class<?>> getEntityClasses (){
		return entityClasses;
	}

	
}
