package com.easylite;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.easylite.annotation.Entity;
import com.easylite.annotation.GenerationType;
import com.easylite.annotation.Id;
import com.easylite.exception.EasyLiteSqlException;
import com.easylite.exception.NoPrimaryKeyFoundException;
import com.easylite.exception.NotEntityException;

public final class Table {	
	
	protected static final String P_KEY_NAME = "PRIMARY_KEY_NAME";
	protected static final String P_KEY_TYPE = "PRIMARY_KEY_TYPE";

	protected Table() {}
	
	/**
	 * Create a database table 
	 * @author Mario Dennis
	 * @param db instance
	 * @param clazz type of entity
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @exception NoPrimaryKeyFoundException when no primary key annotation found on entity class
	 * @exception NotEntityException when no table annotation found on entity class
	 */
	protected static synchronized final void createTable (SQLiteDatabase db, Class<?> clazz) throws EasyLiteSqlException{
		try {
			String sql = prepareCreateStatment(clazz);
			db.execSQL(sql);
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		} catch (NoPrimaryKeyFoundException e){
			Log.e("EasyLite", e.getMessage());
		} catch (NotEntityException e) {
			Log.e("EasyLite", e.getMessage());
		} 
	}
	
	
	/**
	 * Drop a database table
	 * @author Mario Dennis
	 * @param db instance
	 * @param clazz type of entity
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 */
	protected static synchronized final void dropTable (SQLiteDatabase db,Class<?> clazz) throws EasyLiteSqlException{
		try {
			db.execSQL(String.format("DROP TABLE IF EXISTS %s", getTableName(clazz)));
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}
	}
	
	/**
	 * Prepare SQL statement to create table
	 * @author Mario Dennis
	 * @param clazz type of entity
	 * @return sql statement
	 */
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
		
		GenerationType generationType = getGenerationStrategy(clazz, keys.get(P_KEY_NAME));
		if ( generationType == GenerationType.AUTO)
			sql.append("AUTOINCREMENT");

		for (String columnName: columns.keySet())
			sql.append(",")
			   .append(columnName)
			   .append(" ")
			   .append(columns.get(columnName));//Get Column SQLite Column Type
		
		return sql.append(")").toString();
	}

	
	/**
	 * Get GenerationStrategy utilized by entity class
	 * @author Mario Dennis
	 * @param clazz type of entity
	 * @param primaryKey field name
	 * @return GenerationType strategy
	 */
	protected final static GenerationType getGenerationStrategy (Class<?> clazz,String primaryKey){
		try {
			Field field = clazz.getDeclaredField(primaryKey);
			Id id = field.getAnnotation(Id.class);
			return id.strategy();
		} catch (NoSuchFieldException e) {
			Log.e("EasyLite", e.getMessage() + " " +clazz.getName());
		} catch (SecurityException e) {
			Log.e("EasyLite", e.getMessage());
		}
		return GenerationType.MANUAL;
	}
	

	/**
	 * Get table name of domain entity
	 * @author Mario Dennis
	 * @param clazz type of entity
	 * @throws NotEntityException when no table annotation found on entity class
	 * @return tableName of entity class
	 */
	protected final static String getTableName (Class<?> clazz){
		Entity annotation = clazz.getAnnotation(Entity.class);
		if (annotation == null)
			throw new NotEntityException();
		return (!annotation.name().isEmpty()) ? annotation.name() : clazz.getSimpleName();
	}
	

	/**
	 * Get information about table keys of entity class
	 * @author Mario Dennis
	 * @exception NoPrimaryKeyFoundException when no primary key annotation found on entity class
	 * @param clazz type of entity
	 * @return table keys
	 */
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
	
	
	/**
	 * Get table columns
	 * @author Mario Dennis
	 * @param clazz type of entity
	 * @return Map of all table columns except primary key column
	 */
	protected final static Map<String, String> getTableColumns (Class<?> clazz){
		Map<String, String> columns = new HashMap<String, String>();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getAnnotation(Id.class) == null)
				columns.put(field.getName(), SqliteTypeResolver.resolver(field.getType()));
		}
		return columns;
	}
}
