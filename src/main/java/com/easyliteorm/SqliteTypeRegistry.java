package com.easyliteorm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * SqliteTypeRegistry contains a registry of all supported 
 * data types and their associated Sqlite column types.
 * 
 * @author Mario Dennis
 */
public class SqliteTypeRegistry {

	private final Map<String, SQLiteType> mapRegistry = new HashMap<String, SQLiteType>();
	
	protected SqliteTypeRegistry() {
		init();
	}
	

	/**
	 * Get instance of registry.
	 * @author Mario Dennis
	 * @return Map<String,String>
	 */
	protected final Map<String, SQLiteType> getRegistry() {
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
	 * @param clazz - Class to register
	 * @param sqliteType - associated SQLite data type 
	 */
	public <T> void register(Class<T> clazz,SQLiteType sqliteType) {
		getRegistry().put(clazz.getName(),sqliteType);
	}

	
	/**
	 * Gets SQLite type associated with class.
	 * @author Mario Dennis
	 * @param clazz 
	 * @return SQLite data type when class is registered, otherwise returns null
	 */
	public <T> SQLiteType resolve(Class<T> clazz) {
		return getRegistry().get(clazz.getName());
	}

	
	/**
	 * Check if a class is registered
	 * @param clazz
	 * @return
	 */
	public <T> boolean isRegistered(Class<T> clazz) {
		return (getRegistry().get(clazz.getName()) != null);
	}
}
