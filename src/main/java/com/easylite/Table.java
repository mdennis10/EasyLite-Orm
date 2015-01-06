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
	protected StringBuffer createTbSql = new StringBuffer();
	protected String DROP_TABLE_IF_EXIST = "DROP TABLE IF EXISTS %s";
	
	public String name,primaryKeyType,primaryKeyName;
	public Map<String, String> columns = new HashMap<String, String>();
	
	
	protected Table() {}
	
	/**
	 * Create a database table 
	 * @author Mario Dennis
	 * @param db
	 * @param clazz
	 * @exception EasyLiteSqlException
	 * @exception NoPrimaryKeyFoundException
	 * @exception NotEntityException
	 */
	protected final <T> void createTable (SQLiteDatabase db,Class<T> clazz) throws EasyLiteSqlException{
		this.name = Table.getEntityName(clazz);
		
		for (Field field : clazz.getFields()){
			String name = field.getName();
			String type = SqliteTypeResolver.resolver(field.getType().getName());
			if (field.getAnnotation(Id.class) == null)
				columns.put(name, type);
			else{
				this.primaryKeyName = name;
				this.primaryKeyType = type;
			}
		}
		prepareCreateStatment();
		try {
			db.execSQL(createTbSql.toString());
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}finally{
			//Discard previous sql statement
			createTbSql = null;
			createTbSql = new StringBuffer();
		}
	}
	
	private void prepareCreateStatment (){	
		if (primaryKeyName == null || primaryKeyType == null)
			throw new NoPrimaryKeyFoundException();
		
		createTbSql.append(String.format("CREATE TABLE %s (%s %s PRIMARY KEY", name,primaryKeyName,primaryKeyType));
		for (String key : columns.keySet())
			createTbSql.append(String.format(", %s %s", key,columns.get(key)/*Sqlite Data Type*/));
		createTbSql.append(")");
	}

	
	
	/**
	 * Drop a database table
	 * @author Mario Dennis
	 * @param db
	 * @param clazz
	 * @exception EasyLiteSqlException
	 */
	protected final <T> void dropTable (SQLiteDatabase db, Class<T> clazz) throws EasyLiteSqlException{
		this.name = Table.getEntityName(clazz);
		try {
			db.execSQL(String.format(DROP_TABLE_IF_EXIST, this.name));
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}
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
}
