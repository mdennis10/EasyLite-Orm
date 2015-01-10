package com.easylite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.annotation.Id;
import com.easylite.exception.EasyLiteSqlException;

public final class DaoImpl<K,E> implements Dao<K, E>{
	private final SQLiteDatabase db;
	private final Class<E> type;
	private final String tableName;
	Map<String, String> tableKeys; 
	
	public DaoImpl (EasyLiteOpenHelper openHelper,Class<E> type){
		this.db = openHelper.getWritableDatabase();
		this.type = type;
		this.tableKeys = Table.getTableKeys(type);
		this.tableName = Table.getEntityName(type);
	}
	
	
	public long create(E entity) throws EasyLiteSqlException {
		if (entity == null)
			throw new NullPointerException("Null Entity Supplied");
		try {
			ContentValues values = new ContentValues();
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields) 
				putContentValue(values, field, entity);
			
			return this.db.insert(tableName, null, values);
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	public boolean batchCreate(List<E> entities) throws EasyLiteSqlException {
		boolean success = true;
		try {
			if (entities != null && !entities.isEmpty()){
				db.beginTransaction();
				for (E entity : entities){
					if (create(entity) == -1){
						success = false;
						break;
					}
				}
				/*
				 * Commit insert operations
				 * only when all was successful
				 */
				if (success) 
					db.setTransactionSuccessful(); 
			}
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}finally{
			db.endTransaction();
		}
		return success;
	}
	
	
	public int batchCreateWhereNotExist(List<E> entities) throws EasyLiteSqlException{
		int numInserted = 0;
		try {
			if (entities != null && !entities.isEmpty()){
				StringBuilder sql = new StringBuilder();
				db.beginTransaction();
				for (E entity : entities){
					createInsertQuery (sql,entity);
					createInsertWhereClauseForPrimaryKey(sql,entity);
					db.execSQL(sql.toString());
					++numInserted;
				}
				db.setTransactionSuccessful();
			}
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}finally{
			db.endTransaction();
		}
		return numInserted;
	}
	
	private void createInsertWhereClauseForPrimaryKey(StringBuilder sql,E entity) {
		
	}


	private void createInsertQuery(StringBuilder sql, E entity) {
		
	}


	public int delete(E entity) throws EasyLiteSqlException {
		if (entity == null)
			throw new NullPointerException("null Entity Supplied");
		
		try {
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields){
				if(field.getAnnotation(Id.class) != null){		
					@SuppressWarnings("unchecked")
					K key = (K) field.get(entity);
					String[] args = {parsePrimaryKey(tableKeys.get(Table.PRIMARY_KEY_TYPE), key)};
					return db.delete(tableName, tableKeys.get(Table.PRIMARY_KEY_NAME) + "=?", args);
				}
			}
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
	public int update(E entity,String whereClause,String[] whereArgs) throws EasyLiteSqlException{
		if (entity == null)
			throw new NullPointerException("null Entity Supplied");
		
		try{
			ContentValues values = new ContentValues();
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields) 
				putContentValue(values, field, entity);
			
			return db.update(tableName, values, whereClause, whereArgs);
		} catch (SQLException e){
			throw new EasyLiteSqlException(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
	public E findById(K key) throws EasyLiteSqlException {		
		String sql = String.format("SELECT * FROM %s WHERE %s=?", tableName,tableKeys.get(Table.PRIMARY_KEY_NAME));
		String[] args ={parsePrimaryKey(tableKeys.get(Table.PRIMARY_KEY_TYPE), key)};
		
		try {
			Cursor cursor = db.rawQuery(sql, args);
			cursor.moveToFirst();
			E entity = type.newInstance();
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields)
				this.setEntityFields(cursor, field, entity);
			
			return entity;
		} catch (SQLException e){
			throw new EasyLiteSqlException(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public List<E> findAll() throws EasyLiteSqlException {
		List<E> results = new ArrayList<E> ();
		try {
			Cursor cursor = db.query(tableName, null, null, null, null, null, null);
			while (cursor.moveToNext()) {
				E entity = type.newInstance();
				Field[] fields = type.getDeclaredFields();
				for (Field field : fields)
					setEntityFields(cursor, field, entity);
				results.add(entity);
			}
		} catch(SQLException e){
			throw new EasyLiteSqlException(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public List<E> findAll(String whereClause, String[] whereArgs) throws EasyLiteSqlException {
		List<E> results = new ArrayList<E>();
		try {
			Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s", tableName,whereClause), whereArgs);
			while (cursor.moveToNext()) {
				E entity = type.newInstance();
				Field[] fields = type.getDeclaredFields();
				for (Field field : fields)
					setEntityFields(cursor, field, entity);
				results.add(entity);	
			}
		} catch (SQLException e) {
			new EasyLiteSqlException(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return results;
	}

	
	@SuppressWarnings("unchecked")
	public boolean isExist(E entity) throws EasyLiteSqlException {
		Field[] fields = type.getDeclaredFields();
		String key = "";
		try {
			for (Field field : fields)
				if (field.getAnnotation(Id.class) != null)
					key = parsePrimaryKey(tableKeys.get(Table.PRIMARY_KEY_TYPE), (K) field.get(entity));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		String sql = String.format("SELECT * FROM %s WHERE %s=?", tableName,tableKeys.get(Table.PRIMARY_KEY_NAME));
		Cursor cursor = db.rawQuery(sql, new String[]{key});
		return cursor.moveToFirst();
	}

	
	public SQLiteDatabase getSqLiteDatabase() {
		return db;
	}
	

	/**
	 * Parse primary key to String
	 * @author Mario Dennis
	 * @param primaryKeyType
	 * @param key
	 * @return primary key String
	 */
	private  String parsePrimaryKey (String primaryKeyType,K key){
		if (primaryKeyType.equals("int") || primaryKeyType.equals(Integer.class.getName()))
			return Integer.toString((Integer) key);
		else if (primaryKeyType.equals("double") || primaryKeyType.equals(Double.class.getName()))
			return Double.toString((Double) key);
		else if (primaryKeyType.equals("long") || primaryKeyType.equals(Long.class.getName()))
			return Long.toString((Long) key);
		else if (primaryKeyType.equals("float") || primaryKeyType.equals(Float.class.getName()))
			return Float.toString((Float) key);
		else
			return key.toString();
	}
	
	
	/**
	 * Sets entity fields reflectively.
	 * @author Mario Dennis
	 * @param cursor
	 * @param field
	 * @param entity
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void setEntityFields (Cursor cursor,Field field,E entity) throws IllegalArgumentException, IllegalAccessException{
		String type = field.getType().getName();
		if (type.equals("int") || type.equals(Integer.class.getName()))
			field.setInt(entity, cursor.getInt(cursor.getColumnIndex(field.getName())));
		
		else if(type.equals(String.class.getName()))
			field.set(entity, cursor.getString(cursor.getColumnIndex(field.getName())));
		
		else if (type.equals("double") || type.equals(Double.class.getName()))
			field.setDouble(entity, cursor.getDouble(cursor.getColumnIndex(field.getName())));
		
		else if (type.equals("long") || type.equals(Long.class.getName()))
			field.setLong(entity, cursor.getLong(cursor.getColumnIndex(field.getName())));
		
		else if (type.equals("boolean") || type.equals(Boolean.class.getName()))
			field.setBoolean(entity, (cursor.getInt(cursor.getColumnIndex(field.getName())) == 1) ? true: false);
		
		else if (type.equals(Date.class.getName())){
			long d = cursor.getLong(cursor.getColumnIndex(field.getName()));
			Date date = new Date(d);
			field.set(entity, date);
		}
		else if (type.equals("float") || type.equals(Float.class.getName()))
			field.setFloat(entity, cursor.getFloat(cursor.getColumnIndex(field.getName())));
	}
	
	
	/**
	 * Add field value to contentValue
	 * @author Mario Dennis
	 * @param values
	 * @param field
	 * @param entity
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void putContentValue (ContentValues values,Field field,E entity) throws IllegalArgumentException, IllegalAccessException{
		String name = field.getName();
		String type = field.getType().getName();
		
		if (type.equals("int") || type.equals(Integer.class.getName()))
			values.put(name, field.getInt(entity));
		
		else if (type.equals(String.class.getName()) && field.get(entity) != null){
			values.put(name, (String) field.get(entity));
		}
		
		else if (type.equals("double") || type.equals(Double.class.getName()))
			values.put(name, field.getDouble(entity));
		
		else if (type.equals("long") || type.equals(Long.class.getName()))
			values.put(name, field.getLong(entity));
		
		else if (type.equals("boolean") || type.equals(Boolean.class.getName()))
			values.put(name, field.getBoolean(entity));
		
		else if (type.equals("char") || type.equals(Character.class.getName()))
			values.put(name,Character.toString(field.getChar(entity)));
		
		else if (type.equals("float") || type.equals(Float.class.getName()))
			values.put(name,field.getFloat(entity));
		
		else if (type.equals(Date.class.getName()) && field.get(entity) != null){
			Date date = (Date) field.get(entity);
			values.put(name,date.getTime());	
		}
	}

}
