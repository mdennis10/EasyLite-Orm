package com.easyliteorm.exception;

public class IllegalWhereArgumentException extends IllegalArgumentException {
	private static final long serialVersionUID = -830242728952167825L;

	public IllegalWhereArgumentException() {}
	
	public IllegalWhereArgumentException (IllegalArgumentException t){
		super(t);
	}
	
	@Override
	public String getMessage() {
		return "Invalid WhereClause or WhereArgs supplied";
	}
}
