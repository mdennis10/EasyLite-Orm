package com.easylite;

import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class EasyLiteOpenHelper extends SQLiteOpenHelper {

	public Set<Class<?>> entityClasses;

	protected EasyLiteOpenHelper(Context context, String dbName,int version, Set<Class<?>> entityClasses) {
		super(context,ManifestUtil.getDatabaseName(context),null,
			  ManifestUtil.getDatabaseVersion(context));
		this.entityClasses = entityClasses;
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
	}

}
