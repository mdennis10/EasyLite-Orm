package com.easyliteorm;

import java.util.Iterator;

import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.model.Book;

public final class SchemaGenerator {
	protected SchemaGenerator() {}

	protected <T> String createTable(Table<T> table) {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS ")
		       .append(table.getName())
		       .append("(");
		
		Iterator<Column> iterator = table.getColumns().iterator();
		while(iterator.hasNext()){
			Column column = iterator.next();
			builder.append(column.getName())
			       .append(" ")
			       .append(column.getSqliteType().getValue());
			
			if(column.getColumnType() == ColumnType.PRIMARY){
				builder.append(" PRIMARY KEY");
				if (column.getGenerationStrategy() == GenerationType.AUTO)
				       builder.append(" AUTOINCREMENT");
			}
			
			if (iterator.hasNext())
				builder.append(",");
		}
		return builder.append(")").toString();
	}
	

	public String dropTable(Table<Book> table) {
		return String.format("DROP TABLE IF EXISTS %s", table.getName());
	}
	

}
