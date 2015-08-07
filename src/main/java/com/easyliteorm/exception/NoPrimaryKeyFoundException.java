package com.easyliteorm.exception;

/**
 * Constructs a NoPrimaryKeyFoundException with 
 * the specified detail message.
 * @author Mario Dennis
 *
 */
public class NoPrimaryKeyFoundException extends Exception {
	private static final long serialVersionUID = -2105458566036231564L;

	@Override
	public String getMessage() {
		return "No Field Found With Id Annotation";
	}
}
