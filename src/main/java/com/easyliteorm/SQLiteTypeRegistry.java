package com.easyliteorm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * SQLiteTypeRegistry contains a registry of all supported
 * data types and their associated Sqlite column types.
 * 
 * @author Mario Dennis
 */
public class SQLiteTypeRegistry {

	private final Map<String, RegisteredType<?>> mapRegistry = new HashMap<String, RegisteredType<?>>();
	
	protected SQLiteTypeRegistry() {
		init();
	}
	

	/**
	 * Get instance of registry.
	 * @author Mario Dennis
	 * @return registry map
	 */
	protected final Map<String, RegisteredType<?>> getRegistry() {
		return mapRegistry;
	}
	
	/*
	 * Registers default data types
	 */
	private void init (){
		register(String.class, SQLiteType.TEXT);
		register(char.class, SQLiteType.TEXT);
		register(Character.class, SQLiteType.TEXT);
		
		register(int.class, SQLiteType.INTEGER);
		register(Integer.class, SQLiteType.INTEGER);
		register(long.class, SQLiteType.INTEGER);
		register(Long.class, SQLiteType.INTEGER);
		register(boolean.class, SQLiteType.INTEGER);
		register(Boolean.class, SQLiteType.INTEGER);
		register(Date.class, SQLiteType.INTEGER);
		register(BigInteger.class, SQLiteType.INTEGER);
		register(byte.class, SQLiteType.INTEGER);
		register(Byte.class, SQLiteType.INTEGER);
		register(short.class, SQLiteType.INTEGER);
		register(Short.class, SQLiteType.INTEGER);
		
		register(double.class, SQLiteType.REAL);
		register(Double.class, SQLiteType.REAL);
		register(float.class, SQLiteType.REAL);
		register(Float.class, SQLiteType.REAL);
		register(BigDecimal.class, SQLiteType.REAL);
	}
	

	/**
	 * Registers data type
	 * @author Mario Dennis
	 * @param <T> type to registered
	 * @param clazz - Class to register
	 * @param sqliteType - associated SQLite data type 
	 */
	public <T> void register(Class<T> clazz,SQLiteType sqliteType) {
		RegisteredType<T> registeredType = new RegisteredType<T>();
		registeredType.setClazz(clazz);
		registeredType.setSqliteType(sqliteType);

		getRegistry().put(clazz.getName(),registeredType);
	}

	
	/**
	 * Gets SQLite type associated with class.
	 * @author Mario Dennis
	 * @param <T> type to resolve
	 * @param clazz  to resolve
	 * @return SQLite data type when class is registered, otherwise returns null
	 */
	public <T> SQLiteType resolve(Class<T> clazz) {
		RegisteredType<?> registeredType = getRegistry().get(clazz.getName());

		return (registeredType != null) ? registeredType.getSqliteType() : null;
	}

	
	/**
	 * Check if a class is registered
	 * @author Mario Dennis
	 * @param  <T> type
	 * @param clazz instance to check for
	 * @return whether or not type is already registered
	 */
	public <T> boolean isRegistered(Class<T> clazz) {
		return (getRegistry().get(clazz.getName()) != null);
	}
}
