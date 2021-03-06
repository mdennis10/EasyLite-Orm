package com.easyliteorm;

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
	
	
	/**
	 * Converts parameter value to SQLite
	 * column type equivalent.[Note] this 
	 * necessary to convert boolean values.
	 * @author Mario Dennis
	 * @param param to convert
	 * @return converted string value
	 */
	protected static String convertParamValue (Object param){
		if (param == null)
			throw new NullPointerException("Null field instance suppled");
		Class<?> type = param.getClass();
		
		if (type.isAssignableFrom(String.class))
			return (String) param;
		
		else if (type.isAssignableFrom(int.class)|| type.isAssignableFrom(Integer.class))
			return Integer.toString((Integer)param);
		
		else if (type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)||
				 type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class))
			return Double.toString((Double)param);
		
		else if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class))
			return (((Boolean)param) == true) ? "1" : "0";
		
		else if (type.isAssignableFrom(Date.class))
			return Long.toString(((Date)param).getTime());
		
		if (type.isAssignableFrom(long.class) ||
			type.isAssignableFrom(Long.class))
			return Long.toString((Long) param);
		
		else return "NONE";
	}
}
