package com.easyliteorm;

import com.easyliteorm.annotation.GenerationType;

public class Column{
	private String name;
	private SQLiteType sqliteType;
	private ColumnType columnType;
	private GenerationType generatorType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SQLiteType getSqliteType() {
		return sqliteType;
	}
	
	public void setSqliteType(SQLiteType sqliteType) {
		this.sqliteType = sqliteType;
	}
	
	public ColumnType getColumnType() {
		return columnType;
	}
	
	public void setColumnType(ColumnType columnType) {
		this.columnType = columnType;
	}

	public GenerationType getGenerationStrategy() {
		return generatorType;
	}

	public void setGenerationType(GenerationType generatorStrategy) {
		this.generatorType = generatorStrategy;
	}
}
