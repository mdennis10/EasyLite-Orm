package com.easyliteorm;

public enum SQLiteType {

	TEXT("TEXT"),
	INTEGER("INTEGER"),
	REAL("REAL"),
    NONE("NONE");
	private final String value;
	
	SQLiteType(String value) {
		this.value = value;
	}
	
	public String getValue (){
		return value;
	}
	
	@Override
	public String toString (){
		return value;
	}
}
