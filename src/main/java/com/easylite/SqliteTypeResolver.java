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
		if (primitiveTypeName.equals(String.class.getName()) || primitiveTypeName.equals("char") ||
			primitiveTypeName.equals(Character.class.getName()))
			return TEXT;
		
		if (primitiveTypeName.equals("int") || primitiveTypeName.equals(Integer.class.getName())||
			primitiveTypeName.equals(Long.class.getName()) || primitiveTypeName.equals("boolean") ||
			primitiveTypeName.equals(Boolean.class.getName()) || primitiveTypeName.equals(Date.class.getName()) ||
			primitiveTypeName.equals(BigInteger.class.getName()) || primitiveTypeName.equals("long"))
			return INTEGER;

		if (primitiveTypeName.equals("double") || primitiveTypeName.equals(Double.class.getName()) ||
			primitiveTypeName.equals("float") || primitiveTypeName.equals(Float.class.getName()) ||
			primitiveTypeName.equals(BigDecimal.class.getName()))
			return REAL;
		
		return NONE;
	}

}
