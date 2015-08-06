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

	private final Map<String, SqliteType> mapRegistry = new HashMap<String, SqliteType>();
	
	protected SqliteTypeRegistry() {
		init();
	}
	

	/**
	 * Get instance of registry.
	 * @author Mario Dennis
	 * @return Map<String,String>
	 */
	protected final Map<String, SqliteType> getRegistry() {
		return mapRegistry;
	}
	
	/*
	 * Registers default support data types
	 */
	private void init (){
		register(String.class,SqliteType.TEXT);
		register(char.class,SqliteType.TEXT);
		register(Character.class,SqliteType.TEXT);
		
		register(int.class,SqliteType.INTEGER);
		register(Integer.class,SqliteType.INTEGER);
		register(long.class,SqliteType.INTEGER);
		register(Long.class, SqliteType.INTEGER);
		register(boolean.class, SqliteType.INTEGER);
		register(Boolean.class,SqliteType.INTEGER);
		register(Date.class, SqliteType.INTEGER);
		register(BigInteger.class,SqliteType.INTEGER);
		register(byte.class,SqliteType.INTEGER);
		register(Byte.class,SqliteType.INTEGER);
		register(short.class,SqliteType.INTEGER);
		register(Short.class,SqliteType.INTEGER);
		
		register(double.class,SqliteType.REAL);
		register(Double.class,SqliteType.REAL);
		register(float.class,SqliteType.REAL);
		register(Float.class,SqliteType.REAL);
		register(BigDecimal.class,SqliteType.REAL);
	}
	

	/**
	 * Registers data type
	 * @author Mario Dennis
	 * @param clazz - Class to register
	 * @param sqliteType - associated SQLite data type 
	 */
	public <T> void register(Class<T> clazz,SqliteType sqliteType) {
		getRegistry().put(clazz.getName(),sqliteType);
	}

	
	/**
	 * Gets SQLite type associated with class.
	 * @author Mario Dennis
	 * @param clazz 
	 * @return SQLite data type when class is registered, otherwise returns null
	 */
	public <T> SqliteType resolve(Class<T> clazz) {
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
