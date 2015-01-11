package com.easylite;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.annotation.GenerationType;
import com.easylite.annotation.Id;
import com.easylite.exception.EasyLiteSqlException;
import com.easylite.exception.NoPrimaryKeyFoundException;
import com.easylite.exception.NotTableException;

public final class Table {	
	
	protected static final String P_KEY_NAME = "PRIMARY_KEY_NAME";
	protected static final String P_KEY_TYPE = "PRIMARY_KEY_TYPE";

	protected Table() {}
	
	/**
	 * Create a database table 
	 * @author Mario Dennis
	 * @param db
	 * @param clazz
	 * @exception EasyLiteSqlException
	 * @exception NoPrimaryKeyFoundException
	 * @exception NotTableException
	 */
	protected static synchronized final void createTable (SQLiteDatabase db, Class<?> clazz) throws EasyLiteSqlException{
		String sql = prepareCreateStatment(clazz);
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}
	}
	
	
	/**
	 * Drop a database table
	 * @author Mario Dennis
	 * @param db
	 * @param table class to drop
	 * @exception EasyLiteSqlException
	 */
	protected static synchronized final void dropTable (SQLiteDatabase db,Class<?> clazz) throws EasyLiteSqlException{
		try {
			db.execSQL(String.format("DROP TABLE IF EXISTS %s", getTableName(clazz)));
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}
	}
	
	
	private static String prepareCreateStatment (Class<?> clazz){	
		String tableName = getTableName(clazz);
		Map<String, String> keys = getTableKeys(clazz);
		Map<String, String> columns = getTableColumns(clazz);
		
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE ")
		   .append(tableName)
		   .append("(")
		   .append(keys.get(P_KEY_NAME))
		   .append(" ")
		   .append(keys.get(P_KEY_TYPE))
		   .append(" PRIMARY KEY ");
		
		if (getGenerationStrategy(clazz, keys.get(P_KEY_NAME)) == GenerationType.AUTO)
			sql.append("AUTOINCREMENT");

		for (String columnName: columns.keySet())
			sql.append(",")
			   .append(columnName)
			   .append(" ")
			   .append(columns.get(columnName));//Get Column SQLite Column Type
		
		return sql.append(")").toString();
	}

	protected final static GenerationType getGenerationStrategy (Class<?> clazz,String primaryKey){
		try {
			Field field = clazz.getField(primaryKey);
			Id id = field.getAnnotation(Id.class);
			return id.strategy();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return GenerationType.MANUAL;
	}
	

	protected final static String getTableName (Class<?> clazz){
		com.easylite.annotation.Table annotation = clazz.getAnnotation(com.easylite.annotation.Table.class);
		if (annotation == null)
			throw new NotTableException();
		return (!annotation.name().isEmpty()) ? annotation.name() : clazz.getSimpleName();
	}
	

	protected final static Map<String, String> getTableKeys(Class<?> clazz) {
		Map<String, String> keys = new HashMap<String, String>();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields)
			if (field.getAnnotation(Id.class) != null){
				keys.put(P_KEY_NAME, field.getName());
				keys.put(P_KEY_TYPE, SqliteTypeResolver.resolver(field.getType()));
				return keys;
			}
		throw new NoPrimaryKeyFoundException();
	}
	
	
	protected final static Map<String, String> getTableColumns (Class<?> clazz){
		Map<String, String> columns = new HashMap<String, String>();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getAnnotation(Id.class) == null)
				columns.put(field.getName(), SqliteTypeResolver.resolver(field.getType()));
		}
		return columns;
	}
}
