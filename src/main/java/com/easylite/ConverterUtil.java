package com.easylite;

import java.lang.reflect.Field;

public class ConverterUtil {

	protected static <T> String toString (Class<?> type,T key) {
		if (key == null)
			throw new NullPointerException();
		
		if (type.isAssignableFrom(int.class) ||
			type.isAssignableFrom(Integer.class))
			return Integer.toString((Integer) key);
		
		if (type.isAssignableFrom(double.class) || 
		    type.isAssignableFrom(Double.class))
			return Double.toString((Double)key);
		
		if (type.isAssignableFrom(long.class) ||
			type.isAssignableFrom(Long.class))
			return Long.toString((Long) key);
		
		if (type.isAssignableFrom(float.class) ||
			type.isAssignableFrom(Float.class))
			return Float.toString((Float) key);
		
		return  key.toString();
	}
	
	@SuppressWarnings("unchecked")
	protected static <T> T convert (Field field, Object entity) throws IllegalArgumentException, IllegalAccessException{
		return (T) field.get(entity);
	}
}
