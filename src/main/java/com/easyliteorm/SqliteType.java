package com.easyliteorm;

public enum SqliteType {

	TEXT("TEXT"),
	BLOG("BLOB"),
	INTEGER("INTEGER"),
	REAL("REAL"),
    NONE("NONE");
	private final String value;
	
	SqliteType(String value) {
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
