package com.easylite.exception;

/**
 * Constructs a NotTableException with 
 * the specified detail message.
 * @author Mario Dennis
 */
public class NotTableException extends RuntimeException{

	private static final long serialVersionUID = -8404086083437701272L;

	@Override
	public String getMessage() {
		return "No Entity Annotation Found";
	}
}
