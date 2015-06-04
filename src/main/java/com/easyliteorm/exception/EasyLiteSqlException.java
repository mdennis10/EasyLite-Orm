package com.easyliteorm.exception;

import android.database.SQLException;

/**
 * Constructs a EasyLiteSqlException with 
 * the specified detail message.
 * @author Mario Dennis
 *
 */
public class EasyLiteSqlException extends RuntimeException {
	private static final long serialVersionUID = -2737483895771520239L;

	public EasyLiteSqlException(SQLException e) {
		super(e);
	}
}
