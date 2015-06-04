package com.easyliteorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks field has primary key field.
 * @author Mario Dennis
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
	
	/**
	 * Primary Key generation strategy that
	 * database should use. GenerationType.Auto should only be
	 * used with numeric data types
	 * @author Mario Dennis
	 * @return GenerationType default is GenerationType.MANUAL
	 */
	GenerationType strategy () default GenerationType.MANUAL; 
}
