package com.easyliteorm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.easyliteorm.exception.NoPrimaryKeyFoundException;
import com.easyliteorm.exception.NoSuitablePrimaryKeySuppliedException;

import java.util.Iterator;
import java.util.Set;

public final class EasyLiteOpenHelper extends SQLiteOpenHelper {

	private Set<Class<?>> entityClasses;
	private final String msg = "No entity class was found in package %s. Please ensure correct package defined in Manifest";
	private final SQLiteTypeRegistry sqliteTypeRegistry;
	private final SchemaGenerator schemaGenerator;
	
	protected EasyLiteOpenHelper(Context context, SQLiteTypeRegistry sqliteTypeRegistry) {
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

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");// Enable foreign key constraints
		}
	}

	public Set<Class<?>> getEntityClasses (){
		return entityClasses;
	}

	
}
