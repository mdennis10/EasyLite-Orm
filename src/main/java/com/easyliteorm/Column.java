package com.easyliteorm;

public class Column {
	private String name;
	private SqliteType sqliteType;
	private ColumnType columnType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SqliteType getSqliteType() {
		return sqliteType;
	}
	
	public void setSqliteType(SqliteType sqliteType) {
		this.sqliteType = sqliteType;
	}
	
	public ColumnType getColumnType() {
		return columnType;
	}
	
	public void setColumnType(ColumnType columnType) {
		this.columnType = columnType;
	}
	
}
