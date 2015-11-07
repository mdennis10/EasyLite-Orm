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
	protected String createTable(Table table) throws NoPrimaryKeyFoundException, NoSuitablePrimaryKeySuppliedException{
		if (!table.containPrimaryKey())
			throw new NoPrimaryKeyFoundException();
		
		Column primaryColumn = table.getPrimaryKeyColumn();

		//Check that primary key constraints are met
		if (primaryColumn.getSqliteType() == null || primaryColumn.getSqliteType() != SQLiteType.INTEGER)
			throw new NoSuitablePrimaryKeySuppliedException();
		
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS ")
		       .append(table.getName())
			   .append("(")
			   .append(primaryColumn.getName())
			   .append(" ")
			   .append(primaryColumn.getSqliteType().getValue())
		       .append(" PRIMARY KEY");


		// add autoincrement generation stategy
		if (primaryColumn.getGenerationStrategy() == GenerationType.AUTO)
				builder.append(" AUTOINCREMENT");

		addForeignColumns(builder,table);
		addNoKeyColumns(builder,table);

		return builder.append(")").toString();
	}


	private void addForeignColumns (StringBuilder builder,Table table){
		if (!table.containForeignKey())
			return;

		Iterator<Column> iterator = table.getForeignKeyColumns().iterator();
		while(iterator.hasNext()){
			Column column = iterator.next();
			builder.append(",")
					.append(column.getName())
					.append(" ")
					.append(column.getSqliteType().getValue());
		}
	}


	private void addNoKeyColumns (StringBuilder builder, Table table){
		Iterator<Column> iterator = table.getColumns().iterator();
		while(iterator.hasNext()){
			Column column = iterator.next();
			builder.append(",")
					.append(column.getName())
					.append(" ")
					.append(column.getSqliteType().getValue());
		}
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
