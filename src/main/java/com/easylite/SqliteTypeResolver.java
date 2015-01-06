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
	 * @param primitiveTypeName
	 * @return Sqlite data-type 
	 */
	public static String resolver(String primitiveTypeName) {
		if (primitiveTypeName.equals(String.class.getName()))
			return TEXT;
		if (primitiveTypeName.equals("int"))
			return INTEGER;
		if (primitiveTypeName.equals(Integer.class.getName()))
			return INTEGER;
		if (primitiveTypeName.equals("double"))
			return REAL;
		if (primitiveTypeName.equals(Double.class.getName()))
			return REAL;
		if (primitiveTypeName.equals("float"))
			return REAL;
		if (primitiveTypeName.equals(Float.class.getName()))
			return REAL;
		if (primitiveTypeName.equals("boolean"))
			return INTEGER;
		if (primitiveTypeName.equals("char"))
			return TEXT;
		if (primitiveTypeName.equals(Character.class.getName()))
			return TEXT;
		if (primitiveTypeName.equals(Boolean.class.getName()))
			return INTEGER;
		if (primitiveTypeName.equals(Date.class.getName()))
			return INTEGER;
		if (primitiveTypeName.equals(BigInteger.class.getName()))
			return INTEGER;
		if (primitiveTypeName.equals(BigDecimal.class.getName()))
			return REAL;
		
		return NONE;
	}

}
