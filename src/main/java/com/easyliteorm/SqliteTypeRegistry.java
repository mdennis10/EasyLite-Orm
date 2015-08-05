package com.easyliteorm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SqliteTypeRegistry {

	private final Map<String, String> mapRegistry = new HashMap<String, String>();
	
	protected SqliteTypeRegistry() {
		init();
	}

	protected final Map<String, String> getRegistry() {
		return mapRegistry;
	}
	
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
	

	public <T> void register(Class<T> clazz,SqliteType sqliteType) {
		getRegistry().put(clazz.getName(),sqliteType.getValue());
	}

	public <T> String resolve(Class<T> clazz) {
		return getRegistry().get(clazz.getName());
	}

	public <T> boolean isRegistered(Class<T> clazz) {
		return (getRegistry().get(clazz.getName()) != null);
	}
}
