package com.easylite;

import java.util.Date;


public class ConverterUtil {

	protected static <T> String toString (Class<?> type,T key) {
		if (type.isAssignableFrom(int.class) ||
			type.isAssignableFrom(Integer.class) && key != null)
			return Integer.toString((Integer) key);
		
		if (type.isAssignableFrom(double.class) || 
		    type.isAssignableFrom(Double.class) && key != null)
			return Double.toString((Double)key);
		
		if (type.isAssignableFrom(long.class) ||
			type.isAssignableFrom(Long.class) && key != null)
			return Long.toString((Long) key);
		
		if (type.isAssignableFrom(float.class) ||
			type.isAssignableFrom(Float.class) && key != null)
			return Float.toString((Float) key);
		if (type.isAssignableFrom(boolean.class) || 
			type.isAssignableFrom(Boolean.class) && key != null)
			return (((Boolean)key)) ? "1" : "0";
		
		if (type.isAssignableFrom(Date.class)){
			Date date = (Date) key;
			return (date != null) ? Long.toString(date.getTime()) : "";
		}
		if (key == null)
			return "";
		
		return  key.toString();
	}
}
