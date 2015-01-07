package com.easylite;

import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class EasyLiteOpenHelper extends SQLiteOpenHelper {

	public Set<Class<?>> entityClasses;

	protected EasyLiteOpenHelper(Context context, String dbName,int version, Set<Class<?>> entityClasses) {
		super(context,dbName,null,version);
		this.entityClasses = entityClasses;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (Class<?> clazz : entityClasses)
			new Table(db, clazz).createTable();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(Class<?> clazz : entityClasses)
			new Table(db, clazz).dropTable();
	}

}
