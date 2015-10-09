package com.easyliteorm;

import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.exception.NoPrimaryKeyFoundException;
import com.easyliteorm.exception.NoSuitablePrimaryKeySuppliedException;

import java.util.Iterator;

public final class SchemaGenerator {
	protected SchemaGenerator() {}

	protected synchronized String createTable(Table table) throws NoPrimaryKeyFoundException, NoSuitablePrimaryKeySuppliedException{
		if (!table.containPrimaryKey())
			throw new NoPrimaryKeyFoundException();
		
		Column primaryColumn = table.getPrimaryKeyColumn();
		if (primaryColumn.getSqliteType() == null || primaryColumn.getSqliteType() != SQLiteType.INTEGER)
			throw new NoSuitablePrimaryKeySuppliedException();
		
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS ")
		       .append(table.getName())
		       .append("(");
		
		Iterator<Column> iterator = table.getColumns().iterator();
		while(iterator.hasNext()){
			Column column = iterator.next();
			SQLiteType sqliteType = column.getSqliteType();
			String value = sqliteType.getValue();
			builder.append(column.getName())
			       .append(" ")
			       .append(value);
			
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
	

	public String dropTable(Table table) {
		return String.format("DROP TABLE IF EXISTS %s", table.getName());
	}
	

}
