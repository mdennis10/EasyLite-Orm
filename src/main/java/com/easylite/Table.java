package com.easylite;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.annotation.Entity;
import com.easylite.annotation.Id;
import com.easylite.exception.EasyLiteSqlException;
import com.easylite.exception.NoPrimaryKeyFoundException;
import com.easylite.exception.NotEntityException;

public final class Table {
	
	protected final String name;
	protected static final String KEY_NAME = "primary_key";
	protected static final String KEY_TYPE = "primary_key_type";
	private final Map<String, String> keys;
	public Map<String, String> columns = new HashMap<String, String>();
	private final SQLiteDatabase db;
	private final Class<?> clazz;
	
	
	protected Table(SQLiteDatabase db, Class<?> clazz) {
		this.db = db;
		this.clazz = clazz;
		this.name = getEntityName(clazz);
		this.keys = getTableKeys (clazz);
		this.columns = getColumns(clazz);
	}
	


	/**
	 * Create a database table 
	 * @author Mario Dennis
	 * @param db
	 * @param clazz
	 * @exception EasyLiteSqlException
	 * @exception NoPrimaryKeyFoundException
	 * @exception NotEntityException
	 */
	protected synchronized final <T> void createTable () throws EasyLiteSqlException{
		String sql = prepareCreateStatment(keys.get(KEY_NAME),keys.get(KEY_TYPE));
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
	 * @param clazz
	 * @exception EasyLiteSqlException
	 */
	protected synchronized final <T> void dropTable () throws EasyLiteSqlException{
		try {
			db.execSQL(String.format("DROP TABLE IF EXISTS %s", this.name));
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}
	}
	
	
	private String prepareCreateStatment (String primaryKeyName,String primaryKeyType){	
		if (primaryKeyName == null || primaryKeyType == null)
			throw new NoPrimaryKeyFoundException();
		
		StringBuffer sql = new StringBuffer();
		sql.append(String.format("CREATE TABLE %s (%s %s PRIMARY KEY", name,primaryKeyName,primaryKeyType));
		for (String key : columns.keySet())
			sql.append(String.format(", %s %s", key,columns.get(key)));
		sql.append(")");
		return sql.toString();
	}

	
	/**
	 * Get name of Entity which is used has table name.
	 * @author Mario Dennis
	 * @param clazz
	 * @return entityName
	 * @exception NotEntityException
	 */
	protected static String getEntityName (Class<?> clazz){
		Entity entity = clazz.getAnnotation(Entity.class);
		if (entity == null)
			throw new NotEntityException();
		
		return (!entity.name().isEmpty()) ? entity.name() : clazz.getSimpleName();
	}
	
	/**
	 * Get keys of entity class
	 * @author Mario Dennis
	 * @param clazz
	 * @return map of keys, which is empty when none is found 
	 */
	public Map<String, String> getTableKeys(Class<?> clazz) {
		Map<String, String> keys = new HashMap<String, String>();
		Field[] fields = clazz.getFields();
		for(Field field : fields)
			if (field.getAnnotation(Id.class) != null){
				keys.put(KEY_NAME, field.getName());
				keys.put(KEY_TYPE, SqliteTypeResolver.resolver(field.getType().getName()));
			}
		return keys;
	}
	
	
	/**
	 * Get keys for current table context
	 * @author Mario Dennis
	 * @return keys
	 */
	public Map<String, String> getTableKeys() {
		return keys;
	}
	
	private Map<String, String> getColumns (Class<?> clazz){
		Map<String, String> columns = new HashMap<String, String>();
		for (Field field : clazz.getFields()){
			String name = field.getName();
			String type = SqliteTypeResolver.resolver(field.getType().getName());
			if (field.getAnnotation(Id.class) == null)
				columns.put(name, type);
		}
		return columns;
	}
	
	
	/**
	 * Get table columns
	 * @author Mario Dennis
	 * @return columns
	 */
	public Map<String, String> getColumns() {
		return columns;
	}
	
	
	/**
	 * Get name of table's primary key
	 * @author Mario Dennis
	 * @param clazz
	 * @exception NoPrimaryKeyFoundException
	 * @return primary key name
	 */
	protected static String getPrimaryKeyName (Class<?> clazz){
		Field[] fields = clazz.getFields();
		for(Field field : fields)
			if (field.getAnnotation(Id.class) != null)
				return field.getName();
		
		throw new NoPrimaryKeyFoundException();
	}
	
	
	/**
	 * Get name of primary key data-type
	 * @author Mario Dennis
	 * @param clazz
	 * @exception NoPrimaryKeyFoundException
	 * @return data-type name
	 */
	protected static String getPrimaryKeyTypeName (Class<?> clazz){
		Field[] fields = clazz.getFields();
		for(Field field : fields)
			if (field.getAnnotation(Id.class) != null)
				return field.getType().getName();
		
		throw new NoPrimaryKeyFoundException();
	}

	/**
	 * Get name table
	 * @return tableName
	 */
	public String getTableName() {
		return name;
	}

}
