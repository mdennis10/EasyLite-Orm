package com.easylite.annotation;

/**
 * Defines generation strategy that 
 * will be used to save primary key 
 * associated with database entity.
 * @author Mario Dennis
 *
 */
public enum GenerationType {
	/**
	 * Database will automatically generate
	 * primary key value. [NOTE] field must
	 * be long or int
	 * @author Mario Dennis
	 */
	AUTO,
	
	/**
	 * User must manually supply primary key
	 * for each entity
	 * @author Mario Dennis
	 */
	MANUAL;
}
