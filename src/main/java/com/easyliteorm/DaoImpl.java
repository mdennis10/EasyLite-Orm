package com.easyliteorm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import com.easyliteorm.annotation.Id;
import com.easyliteorm.annotation.OrderByType;
import com.easyliteorm.exception.EasyLiteSqlException;
import com.easyliteorm.exception.IllegalWhereArgumentException;
import com.easyliteorm.exception.NoSuitablePrimaryKeySuppliedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public final class DaoImpl<K,E> implements Dao<K, E>{
	private final SQLiteDatabase db;
	private final Class<E> type;
	private Table table;

	protected DaoImpl (EasyLiteOpenHelper openHelper,Class<E> type,SQLiteTypeRegistry typeRegistry){
		this.db = openHelper.getWritableDatabase();
		this.type = type;
		this.table = new Table(type, typeRegistry);
	}
	
	@Override
	public long create(E entity) throws EasyLiteSqlException {
		if (entity == null)
			throw new NullPointerException("Null Entity Supplied");
		long result = -1;
		try {
			ContentValues values = new ContentValues();
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields) 
				putContentValue(values, field, entity);
			
			result =  this.db.insert(table.getName(), null, values);
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		} catch (IllegalArgumentException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		}
		return result;
	}

	@Override
	public void createAsync(final ResponseListener<Long> listener, final E entity) {
		EasyLiteAsyncTask<Long> task = new EasyLiteAsyncTask<Long>(new Action<Long>() {
			@Override
			public Long execute() {
				return create(entity);
			}
		}, listener);
		task.execute();
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
				String pKeyName = table.getPrimaryKeyColumn().getName();
				sql.append("INSERT OR REPLACE INTO ")
				   .append(table.getName())
				   .append(" (")
				   .append(pKeyName);
				
				Set<Column> columns = table.getColumns();
				for (Column column : columns)
					sql.append(",")
					   .append(column.getName());
				
				sql.append(") VALUES (?");
				int size = columns.size();
				for (int x = 0; x < size;x++)
					sql.append(",?");
				
				sql.append(")");
				db.beginTransaction();
				for (E entity : entities){
					SQLiteStatement statement = db.compileStatement(sql.toString());
					String[] args = bindArgs (entity,columns);
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
			if (db.inTransaction())
				db.endTransaction();
		}
	}


	@Override
	public void batchCreateOverridableAsync(ResponseListener<Boolean>listener, final List<E> entities) throws EasyLiteSqlException {
		EasyLiteAsyncTask<Boolean> task = new EasyLiteAsyncTask<Boolean>(new Action<Boolean>() {
			@Override
			public Boolean execute() {
				batchCreateOverridable(entities);
				return true;
			}
		},listener);
		task.execute();
	}


	private String[] bindArgs(E entity,Set<Column> columns) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Object pKeyRaw = getFieldValue(type.getDeclaredField(table.getPrimaryKeyColumn().getName()), entity);
		String pKey = ConverterUtil.toString(pKeyRaw.getClass(), pKeyRaw);
		String[] args = new String[columns.size() + 1];
		int position = 0;
		args[position] = pKey;
		for (Column column : columns){
			Field field = type.getDeclaredField(column.getName());
			field.setAccessible(true);
			String val = ConverterUtil.toString(field.getType(), field.get(entity));
			args[++position] = val;
		}
		return args;
	}

	public Object getFieldValue (Field field, E entity) throws IllegalArgumentException, IllegalAccessException{
		field.setAccessible(true);
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
			return db.delete(table.getName(), null, new String[]{});
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}
	}

	@Override
	public void deleteAllAsync(ResponseListener<Integer> listener) {
		EasyLiteAsyncTask<Integer> task = new EasyLiteAsyncTask<Integer>(new Action<Integer>() {
			@Override
			public Integer execute() {
				return deleteAll();
			}
		},listener);
		task.execute();
	}


	@Override
	public int deleteAll(String whereClause, Object... whereArgs) throws EasyLiteSqlException{
		try {
			return db.delete(table.getName(), whereClause, formatWhereParams(whereArgs));
		} catch (SQLException e) {
			throw new EasyLiteSqlException(e);
		}
	}


	@Override
	public void deleteAllAsync(ResponseListener<Integer> listener, final String whereClause, final Object... whereArgs) throws EasyLiteSqlException {
		EasyLiteAsyncTask<Integer> task = new EasyLiteAsyncTask<Integer>(new Action<Integer>() {
			@Override
			public Integer execute() {
				return deleteAll(whereClause,whereArgs);
			}
		},listener);
		task.execute();
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
					return db.delete(table.getName(), table.getPrimaryKeyColumn().getName()+ "=?", args);
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
	public void deleteAsync(ResponseListener<Integer> listener,final E entity) {
		EasyLiteAsyncTask<Integer> task = new EasyLiteAsyncTask<Integer>(new Action<Integer>() {
			@Override
			public Integer execute() {
				return delete(entity);
			}
		},listener);
		task.execute();
	}

	
	@Override
	public int update(E entity,String whereClause,Object... whereArgs) throws EasyLiteSqlException{
		if (entity == null)
			throw new NullPointerException("null Entity Supplied");
		
		try{
			ContentValues values = new ContentValues();
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields) 
				putContentValue(values, field, entity);
			
			return db.update(table.getName(), values, whereClause,formatWhereParams(whereArgs));
		} catch (SQLException e){
			throw new EasyLiteSqlException(e);
		} catch (IllegalArgumentException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("EasyLite", e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			return 0;
		}
		return 0;
	}

	@Override
	public void updateAsync(ResponseListener<Integer> listener, final E entity, final String whereClause, final Object... whereArgs) throws EasyLiteSqlException {
		EasyLiteAsyncTask<Integer> task = new EasyLiteAsyncTask<Integer>(new Action<Integer>() {
			@Override
			public Integer execute() {
				return update(entity,whereClause,whereArgs);
			}
		}, listener);
		task.execute();
	}

	@Override
	public E findById(K key) throws EasyLiteSqlException {	
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ")
		   .append(table.getName())
		   .append(" WHERE ")
		   .append(table.getPrimaryKeyColumn().getName())
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
		}catch (IndexOutOfBoundsException e) {
			return null;
		}
		return null;
	}


	@Override
	public void findByIdAsync(ResponseListener<E> listener, final K key) throws EasyLiteSqlException {
		EasyLiteAsyncTask<E> task = new EasyLiteAsyncTask<E>(new Action<E>() {
			@Override
			public E execute() {
				return findById(key);
			}
		}, listener);
		task.execute();
	}

	@Override
	public List<E> findAll() throws EasyLiteSqlException {
		List<E> results = new ArrayList<E> ();
		try {
			Cursor cursor = db.query(table.getName(), null, null, null, null, null, null);
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
		} catch (IndexOutOfBoundsException e) {
			return results;
		}
		return results;
	}


	@Override
	public void findAllAsync(ResponseListener<List<E>> listener) {
		EasyLiteAsyncTask<List<E>> task = new EasyLiteAsyncTask<List<E>>(new Action<List<E>>() {
			@Override
			public List<E> execute() {
				return findAll();
			}
		},listener);
		task.execute();
	}

	@Override
	public List<E> findAll(String orderBy,OrderByType orderByType,String whereClause,Object... whereArgs) throws EasyLiteSqlException {
		List<E> results = new ArrayList<E>();
		try {
			String orderType = (orderByType != null) ? orderByType.toString() : OrderByType.ASC.toString();
			String[] newWhereArgs = formatWhereParams(whereArgs);
			Cursor cursor = db.query(table.getName(), null, whereClause, newWhereArgs, null, null, String.format("%s %s", orderBy,orderType));
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
		} catch (IllegalArgumentException e) {
			Log.e("EasyLite", new IllegalWhereArgumentException(e).getMessage());
		}catch (IndexOutOfBoundsException e) {
			return results;
		}
		return results;
	}


	@Override
	public void findAllAsync(ResponseListener<List<E>> listener, final String orderBy, final OrderByType orderByType, final String whereClause, final Object... whereArgs) throws EasyLiteSqlException {
		EasyLiteAsyncTask<List<E>> task = new EasyLiteAsyncTask<List<E>>(new Action<List<E>>() {
			@Override
			public List<E> execute() {
				return findAll(orderBy,orderByType,whereClause,whereArgs);
			}
		}, listener);
		task.execute();
	}

	public String[] formatWhereParams (Object... whereArgs){
		if (whereArgs == null)
			return null;
		
		String[] newWhereArgs = new String[whereArgs.length];
		for(int x = 0; x < whereArgs.length;x++)
			newWhereArgs[x] = ConverterUtil.convertParamValue(whereArgs[x]);
		return newWhereArgs;
	}
	
	@Override
	public boolean isExist(E entity) throws EasyLiteSqlException {
		String pKeyName = table.getPrimaryKeyColumn().getName();
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
		   .append(table.getName())
		   .append(" WHERE ")
		   .append(pKeyName)
		   .append("=?");
		Cursor cursor = db.rawQuery(sql.toString(), new String[]{pkey});
		return cursor.moveToFirst();
	}

	
	@Override
	public boolean isExist() throws EasyLiteSqlException {
		return db.query(table.getName(), null, null, null, null, null, null)
				 .moveToFirst();
	}
	
	public SQLiteDatabase getSqLiteDatabase() {
		return db;
	}
	

	private void setEntityFields (Cursor cursor,Field field,E entity) throws IllegalArgumentException, IllegalAccessException{
		field.setAccessible(true);
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
	
	
	/*
	 * Add field value to contentValue
	 * @author Mario Dennis
	 * @param values
	 * @param field
	 * @param entity
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void putContentValue (ContentValues values,Field field,E entity) throws IllegalArgumentException, IllegalAccessException{
		field.setAccessible(true);
		String name = field.getName();
		Class<?> type = field.getType();
		boolean isPrimaryKey = (field.getAnnotation(Id.class) != null) ? true : false;
		
		if (isPrimaryKey){
			setPrimaryKey(field, name, type,isPrimaryKey, values,entity);
			return;
		}
	
		if (type.isAssignableFrom(int.class) || type.equals(Integer.class))
			values.put(name, field.getInt(entity));

		else if (type.isAssignableFrom(String.class)
				&& field.get(entity) != null)
			values.put(name, (String) field.get(entity));

		else if (type.isAssignableFrom(double.class)
				|| type.equals(Double.class))
			values.put(name, field.getDouble(entity));

		else if (type.isAssignableFrom(long.class)
				|| type.equals(Long.class.getName()))
			values.put(name, field.getLong(entity));

		else if (type.isAssignableFrom(boolean.class)
				|| type.equals(Boolean.class))
			values.put(name, field.getBoolean(entity));

		else if (type.isAssignableFrom(char.class)
				|| type.equals(Character.class))
			values.put(name, Character.toString(field.getChar(entity)));

		else if (type.isAssignableFrom(float.class)
				|| type.equals(Float.class.getName()))
			values.put(name, field.getFloat(entity));

		else if (type.equals(Date.class) && field.get(entity) != null) {
			Date date = (Date) field.get(entity);
			values.put(name, date.getTime());
		}
	}

	/*
	 * Adds primary key value to ContentValue 
	 */
	private void setPrimaryKey (Field field,String fieldName, Class<?> fieldType,Boolean isPrimaryKey,ContentValues contentValues,E entity) throws IllegalArgumentException, IllegalAccessException{
		if (!isPrimaryKey)
			return;
		
		if (fieldType.isAssignableFrom(int.class) || fieldType.equals(Integer.class)){
			int value = field.getInt(entity);
			if (isPrimaryKey && value != 0)
				contentValues.put(fieldName, value);
			else  return;
		}
		
		else if (fieldType.isAssignableFrom(long.class) || fieldType.equals(Long.class)){
			long value = field.getLong(entity);
			if (isPrimaryKey && value != 0)
				contentValues.put(fieldName, value);
			else  return;
		}
		
		else if (fieldType.isAssignableFrom(String.class)){
			String value = (String) field.get(entity);
			if (isPrimaryKey && value != null && !value.isEmpty())
				contentValues.put(fieldName, (String) field.get(entity));
			else 
				throw new NoSuitablePrimaryKeySuppliedException();;
		}
			
		else
			throw new NoSuitablePrimaryKeySuppliedException();
	}

	
}
