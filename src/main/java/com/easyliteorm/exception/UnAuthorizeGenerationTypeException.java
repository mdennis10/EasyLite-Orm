package com.easyliteorm.exception;

/**
 * Constructs a UnAuthorizeGenerationTypeException with 
 * the specified detail message.
 * @author Mario Dennis
 *
 */
public class UnAuthorizeGenerationTypeException extends RuntimeException {

	private static final long serialVersionUID = -3257069765173561734L;
	private Class<?> type;
	
	public UnAuthorizeGenerationTypeException(Class<?> type) {
		this.type = type;
	}
	
	@Override
	public String getMessage() {
		return type.getSimpleName() + " does not support GenarationType.AUTO";
	}
}
