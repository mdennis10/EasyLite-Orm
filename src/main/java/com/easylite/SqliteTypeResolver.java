package com.easylite;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


/**
 * SqliteTypeResolver resolves standard
 * Java types to Sqlite equivalent.
 * @author Mario Dennis
 *
 */
public final class SqliteTypeResolver {
	public static final String TEXT = "TEXT";
	public static final String BLOG = "BLOB";
	public static final String INTEGER = "INTEGER";
	public static final String REAL = "REAL";
	public static final String NONE = "NONE";
	protected SqliteTypeResolver (){}
	
	/**
	 * Resolve data-type to sqlite equivalent
	 * @author Mario Dennis
	 * @param clazz to resolve
	 * @return sqlite data-type 
	 */
	public static String resolver (Class<?> clazz){
		if (clazz.isAssignableFrom(String.class) ||
		    clazz.isAssignableFrom(char.class) ||
		    clazz.isAssignableFrom(Character.class))
			return TEXT;
		
		if (clazz.isAssignableFrom(int.class) ||
			clazz.isAssignableFrom(Integer.class) ||
			clazz.isAssignableFrom(boolean.class) ||
			clazz.isAssignableFrom(Boolean.class) ||
			clazz.isAssignableFrom(long.class) ||
			clazz.isAssignableFrom(Long.class) ||
			clazz.isAssignableFrom(Date.class) ||
			clazz.isAssignableFrom(BigInteger.class)||
			clazz.isAssignableFrom(byte.class) ||
			clazz.isAssignableFrom(Byte.class) ||
			clazz.isAssignableFrom(short.class)||
			clazz.isAssignableFrom(Short.class))
			return INTEGER;
		
		if (clazz.isAssignableFrom(double.class) ||
			clazz.isAssignableFrom(Double.class) ||
			clazz.isAssignableFrom(float.class) ||
			clazz.isAssignableFrom(Float.class) ||
			clazz.isAssignableFrom(BigDecimal.class))
			return REAL;
		return NONE;
	}
}
