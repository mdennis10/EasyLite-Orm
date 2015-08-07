package com.easyliteorm;

import java.util.Iterator;
import java.util.Set;

import com.easyliteorm.exception.NoPrimaryKeyFoundException;
import com.easyliteorm.exception.NoSuitablePrimaryKeySuppliedException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final class EasyLiteOpenHelper extends SQLiteOpenHelper {
	private Set<Class<?>> entityClasses;
	private final String msg = "No entity class was found in package %s. Please ensure correct package defined in Manifest";
	private final SqliteTypeRegistry sqliteTypeRegistry;
	private final SchemaGenerator schemaGenerator;
	
	protected EasyLiteOpenHelper(Context context, SqliteTypeRegistry sqliteTypeRegistry) {
		super(context,ManifestUtil.getDatabaseName(context),null,ManifestUtil.getDatabaseVersion(context));
		
		this.sqliteTypeRegistry = sqliteTypeRegistry;
		this.entityClasses      = EntityScanner.getDomainClasses(context);
		this.schemaGenerator    = new SchemaGenerator();
		
		if (entityClasses.isEmpty())
			Log.e("EasyLite",String.format(msg, ManifestUtil.getModelPackageName(context)));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (Iterator<Class<?>> iterator = entityClasses.iterator(); iterator.hasNext();)
		{
			Class<?> clazz = iterator.next();
			try {
				Table table = new Table(clazz, sqliteTypeRegistry);
				db.execSQL(schemaGenerator.createTable(table));
			} catch (NoPrimaryKeyFoundException e) {
				iterator.remove();
				Log.e("EasyLite", e.getMessage());
			} catch (NoSuitablePrimaryKeySuppliedException  e) {
				iterator.remove();
				Log.e("EasyLite", e.getMessage());
			}
			
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(Class<?> clazz : entityClasses) {
			Table table = new Table(clazz, sqliteTypeRegistry);
			db.execSQL(schemaGenerator.dropTable(table));
		}

		onCreate(db);//recreates database schema
	}
	
	public Set<Class<?>> getEntityClasses (){
		return entityClasses;
	}

	
}
