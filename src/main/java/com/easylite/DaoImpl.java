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
import android.util.Log;

import com.easylite.annotation.Id;
import com.easylite.exception.EasyLiteSqlException;

public final class DaoImpl<K,E> implements Dao<K, E>{
	private final SQLiteDatabase db;
	private final Class<E> type;
	private final String tableName;
	Map<String, String> tableKeys; 
	
	protected DaoImpl (EasyLiteOpenHelper openHelper,Class<E> type){
		this.db = openHelper.getWritableDatabase();
		this.type = type;
		this.tableKeys = Table.getTableKeys(type);
		this.tableName = Table.getTableName(type);
	}
	
	@Override
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
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		}
		return -1;
	}
	
	@Override
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
	
	@Override
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

	@Override
	public boolean deleteAll() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteAll(String whereClause, String... whereArgs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	@Override
	public int delete(E entity) throws EasyLiteSqlException {
		if (entity == null)
			throw new NullPointerException("null Entity Supplied");
		
		try {
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields)
				if(field.getAnnotation(Id.class) != null){		
					@SuppressWarnings("unchecked")
					K key = (K) field.get(entity);
					String[] args = {ConverterUtil.toString(type, key)};
					return db.delete(tableName, tableKeys.get(Table.P_KEY_NAME) + "=?", args);
				}
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		} catch (IllegalArgumentException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		}
		return 0;
	}

	
	@Override
	public int update(E entity,String whereClause,String... whereArgs) throws EasyLiteSqlException{
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
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		}
		return 0;
	}

	@Override
	public E findById(K key) throws EasyLiteSqlException {	
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ")
		   .append(tableName)
		   .append(" WHERE ")
		   .append(tableKeys.get(Table.P_KEY_NAME))
		   .append("=?");
		
		String pKey = ConverterUtil.toString(type, key);
		try {
			Cursor cursor = db.rawQuery(sql.toString(), new String[]{pKey});
			cursor.moveToFirst();
			E entity = type.newInstance();
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields)
				this.setEntityFields(cursor, field, entity);
			
			return entity;
		} catch (SQLException e){
			throw new EasyLiteSqlException(e);
		} catch (InstantiationException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		}
		return null;
	}

	@Override
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
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.e("EasyLite", e.getMessage());
		}
		return results;
	}
	
	@Override
	public List<E> findAll(String whereClause, String[] whereArgs) throws EasyLiteSqlException {
		List<E> results = new ArrayList<E>();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM ")
			   .append(tableName)
			   .append(" WHERE ")
			   .append(whereClause);
			Cursor cursor = db.rawQuery(sql.toString(), whereArgs);
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
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		}
		return results;
	}

	
	@Override
	public boolean isExist(E entity) throws EasyLiteSqlException {
		Field[] fields = type.getDeclaredFields();
		String key = "";
		try {
			for (Field field : fields)
				if (field.getAnnotation(Id.class) != null)
					key = ConverterUtil.toString(type, field.get(entity));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ")
		   .append(tableName)
		   .append(" WHERE ")
		   .append(tableKeys.get(Table.P_KEY_NAME))
		   .append("=?");
		Cursor cursor = db.rawQuery(sql.toString(), new String[]{key});
		return cursor.moveToFirst();
	}

	
	@Override
	public SQLiteDatabase getSqLiteDatabase() {
		return db;
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
		Class<?> type = field.getType();
		if (type.isAssignableFrom(int.class) || type.equals(Integer.class))
			field.setInt(entity, cursor.getInt(cursor.getColumnIndex(field.getName())));
		
		else if(type.equals(String.class))
			field.set(entity, cursor.getString(cursor.getColumnIndex(field.getName())));
		
		else if (type.isAssignableFrom(double.class)|| type.equals(Double.class))
			field.setDouble(entity, cursor.getDouble(cursor.getColumnIndex(field.getName())));
		
		else if (type.isAssignableFrom(long.class)|| type.equals(Long.class))
			field.setLong(entity, cursor.getLong(cursor.getColumnIndex(field.getName())));
		
		else if (type.isAssignableFrom(boolean.class) || type.equals(Boolean.class))
			field.setBoolean(entity, (cursor.getInt(cursor.getColumnIndex(field.getName())) == 1) ? true: false);
		
		else if (type.equals(Date.class)){
			long d = cursor.getLong(cursor.getColumnIndex(field.getName()));
			Date date = new Date(d);
			field.set(entity, date);
		}
		else if (type.isAssignableFrom(float.class) || type.equals(Float.class.getName()))
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
		Class<?> type = field.getType();
		
		
		if (type.isAssignableFrom(int.class) || 
			type.equals(Integer.class))
			values.put(name, field.getInt(entity));
		
		else if (type.isAssignableFrom(String.class) && field.get(entity) != null)
				 values.put(name, (String) field.get(entity));
		
		else if (type.isAssignableFrom(double.class) || 
				 type.equals(Double.class))
				 values.put(name, field.getDouble(entity));
		
		else if (type.isAssignableFrom(long.class) ||
				 type.equals(Long.class.getName()))
			values.put(name, field.getLong(entity));
		
		else if (type.isAssignableFrom(boolean.class) ||
				 type.equals(Boolean.class))
				 values.put(name, field.getBoolean(entity));
		
		else if (type.isAssignableFrom(char.class) || 
				 type.equals(Character.class))
				 values.put(name,Character.toString(field.getChar(entity)));
		
		else if (type.isAssignableFrom(float.class) || 
				 type.equals(Float.class.getName()))
				 values.put(name,field.getFloat(entity));
		
		else if (type.equals(Date.class) && field.get(entity) != null){
			Date date = (Date) field.get(entity);
			values.put(name,date.getTime());	
		}
	}

}
