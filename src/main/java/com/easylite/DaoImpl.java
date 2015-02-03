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
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.easylite.annotation.Id;
import com.easylite.annotation.OrderByType;
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
				if (success) //commits when all transactions successful
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
	public synchronized void batchCreateOverridable(List<E> entities) throws EasyLiteSqlException{
		try {
			if (entities != null && !entities.isEmpty()){
				StringBuilder sql = new StringBuilder();
				String pKeyName = tableKeys.get(Table.P_KEY_NAME);
				sql.append("INSERT OR REPLACE INTO ")
				   .append(tableName)
				   .append(" (")
				   .append(pKeyName);
				
				Map<String, String> columns = Table.getTableColumns(type);
				for (String column : columns.keySet())
					sql.append(",")
					   .append(column);
				
				sql.append(") VALUES (?");
				int size = columns.size();
				for (int x = 0; x < size;x++)
					sql.append(",?");
				
				sql.append(")");
				db.beginTransaction();
				for (E entity : entities){
					SQLiteStatement statement = db.compileStatement(sql.toString());
					String[] args = bindArgs (statement,entity,columns);
					statement.bindAllArgsAsStrings(args);
					statement.executeInsert();
				}
				db.setTransactionSuccessful();
			}
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		} catch (IllegalArgumentException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}finally{
			db.endTransaction();
		}
	}
	

	private String[] bindArgs(SQLiteStatement statement, E entity,Map<String, String> columns) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Object pKeyRaw = getFieldValue(type.getDeclaredField(tableKeys.get(Table.P_KEY_NAME)), entity);
		String pKey = ConverterUtil.toString(pKeyRaw.getClass(), pKeyRaw);
		String[] args = new String[columns.size() + 1];
		int position = 0;
		args[position] = pKey;
		for (String column : columns.keySet()){
			Field field = type.getDeclaredField(column);
			String val = ConverterUtil.toString(field.getType(), field.get(entity));
			args[++position] = val;
		}
		return args;
	}

	public Object getFieldValue (Field field, E entity) throws IllegalArgumentException, IllegalAccessException{
		Class<?> type = field.getType();
		if (type.equals(Date.class)){
			Date date = (Date) field.get(entity);
			return (date != null) ?  Long.toString(date.getTime()) : null;
		}
		if (type.isAssignableFrom(boolean.class) || type.equals(Boolean.class)){
			return (field.getBoolean(entity)) ? 1 : 0;
		}
		return field.get(entity);
	}
	
	@Override
	public int deleteAll() throws EasyLiteSqlException{
		try {
			return db.delete(tableName, null, new String[]{});
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}
	}

	@Override
	public int deleteAll(String whereClause, String... whereArgs) throws EasyLiteSqlException{
		try {
			return db.delete(tableName, whereClause, whereArgs);
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}
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
	public List<E> findAll(String whereClause, String[] whereArgs,String orderBy,OrderByType orderByType) throws EasyLiteSqlException {
		List<E> results = new ArrayList<E>();
		try {
			String orderType = (orderByType != null) ? orderByType.toString() : "ASC";
			Cursor cursor = db.query(tableName, null, whereClause, whereArgs, null, null, String.format("%s %s", orderBy,orderType));
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
		String pKeyName = tableKeys.get(Table.P_KEY_NAME);
		String pkey = "";
		try {
			Field field = type.getDeclaredField(pKeyName);
			pkey = ConverterUtil.toString(type, field.get(entity));
		} catch (IllegalArgumentException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (NoSuchFieldException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (SecurityException e) {
			Log.e("EasyLite", e.getMessage());
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ")
		   .append(tableName)
		   .append(" WHERE ")
		   .append(pKeyName)
		   .append("=?");
		Cursor cursor = db.rawQuery(sql.toString(), new String[]{pkey});
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
