package com.easyliteorm;

import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.exception.NoPrimaryKeyFoundException;
import com.easyliteorm.exception.NoSuitablePrimaryKeySuppliedException;

import java.util.Iterator;

public final class SchemaGenerator {
	protected SchemaGenerator() {}

	/**
	 * Generate a SQL DDL statement to create Entity table.
	 * @author Mario Dennis
	 * @param table
	 * @return SQL statement for entity table creation
	 * @throws NoPrimaryKeyFoundException
	 * @throws NoSuitablePrimaryKeySuppliedException
	 */
	protected synchronized String createTable(Table table) throws NoPrimaryKeyFoundException, NoSuitablePrimaryKeySuppliedException{
		if (!table.containPrimaryKey())
			throw new NoPrimaryKeyFoundException();
		
		Column primaryColumn = table.getPrimaryKeyColumn();

		//Check that primary key constraints met
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

			//add primary key statement
			if(column.getColumnType() == ColumnType.PRIMARY) {
				builder.append(" PRIMARY KEY");

				// add autoincrement generation stategy
				if (column.getGenerationStrategy() == GenerationType.AUTO)
				       builder.append(" AUTOINCREMENT");
			}
			
			if (iterator.hasNext())
				builder.append(",");
		}
		return builder.append(")").toString();
	}


	/**
	 * Generate a SQL DDL statement to drop Entity table.
	 * @author Mario Dennis
	 * @param table
	 * @return SQL statement for entity table removal
	 */
	public String dropTable(Table table) {
		return String.format("DROP TABLE IF EXISTS %s", table.getName());
	}

}
