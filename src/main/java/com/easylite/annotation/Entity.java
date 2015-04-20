package com.easylite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Makes class has Database Entity
 * @author Mario Dennis
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
	/**
	 * Specifies the name of the entity table 
	 * @author Mario Dennis 
	 * @return name of entity table
	 */
	String name() default "";
}
