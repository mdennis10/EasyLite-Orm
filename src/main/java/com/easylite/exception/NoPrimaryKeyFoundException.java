package com.easylite.exception;

/**
 * Constructs a NoPrimaryKeyFoundException with 
 * the specified detail message.
 * @author Mario Dennis
 *
 */
public class NoPrimaryKeyFoundException extends RuntimeException {
	private static final long serialVersionUID = -2105458566036231564L;

	@Override
	public String getMessage() {
		return "No Field Found With Id Annotation";
	}
}
