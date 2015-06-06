package com.easyliteorm.exception;

/**
 * Constructs a NoSuitablePrimaryKeySuppliedException with 
 * the specified detail message.
 * @author Mario Dennis
 *
 */
public class NoSuitablePrimaryKeySuppliedException extends RuntimeException{

	private static final long serialVersionUID = 58548287210517480L;
	
	@Override
	public String getMessage() {
		return "No Primary Key value was supplied while using GenarationType.Manual"
			  +" or unsuitable primary key type. Primary keys should only be a long,int"
			  +" or string value";
	}
}
