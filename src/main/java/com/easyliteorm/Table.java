package com.easyliteorm;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.annotation.Foreign;
import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.annotation.Id;
import com.easyliteorm.exception.NotEntityException;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Table contains a information about
 * entity class.
 * @author Mario Dennis
 */
public final class Table {
	private boolean CONTAIN_PRIMARY_KEY = false;
	private boolean CONTAIN_FOREIGN_KEY = false;
	private final String name;
	private final Class<?> entity;
	private Set<Column> columns = new HashSet<Column>();
	private Set<Column> foreignKeyColumns = new HashSet<Column>();
	private Column primaryKeyColumn;

	protected Table(Class<?> entity,SQLiteTypeRegistry typeRegistry) {
		this.entity  = entity;
		this.name    = TableRegistry.getTableName(entity);
		setColumns(typeRegistry);
	}


	/**
	 * Get name of table.
	 * @author Mario Dennis
	 * @return table name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Get all columns associated with table.
	 * @author Mario Dennis
	 * @return table columns
	 */
	public Set<Column> getColumns(){
		return columns;
	}


	private final void setColumns(SQLiteTypeRegistry typeRegistry) {
		Field[] fields = entity.getDeclaredFields();
		for (Field field : fields) {
			addColumn(field, typeRegistry);
		}
	}

	/**
	 * Add entity field to table collection
	 * of table columns.
	 * @author Mario Dennis
	 * @param field
	 * @param typeRegistry
	 */
	private final void addColumn(Field field, SQLiteTypeRegistry typeRegistry){
		ColumnType columnType = resolveColumnType(field);

		if (columnType == ColumnType.REGULAR) {
			columns.add(createColumn(field,columnType,typeRegistry));
		}

		else if (columnType == ColumnType.PRIMARY) {
			primaryKeyColumn = createColumn(field, columnType, typeRegistry);
		}

		else if (columnType == ColumnType.FOREIGN) {
			foreignKeyColumns.add(createForeignColumn(field, typeRegistry));
		}
	}

	/**
	 * Create a Column instance for foreign key columns
	 * @author Mario Dennis
	 * @return created column instance
	 */
	private Column createForeignColumn (Field field, SQLiteTypeRegistry typeRegistry){
		Table table = new Table(field.getType(),typeRegistry);
		Column column = table.getPrimaryKeyColumn();
		column.setColumnType(ColumnType.FOREIGN);
		column.setName(String.format("%s_%s",table.getName(),column.getName()));
		return column;
	}


	/**
	 * creates Column instance
	 * @author Mario Dennis
	 * @return created column instance
	 */
	private Column createColumn (Field field, ColumnType columnType,SQLiteTypeRegistry typeRegistry){
		Column column = new Column();
		column.setName(field.getName());
		column.setColumnType(columnType);
		column.setSqliteType(typeRegistry.resolve(field.getType()));
		column.setGenerationType(resolveGenerationType(field));
		return column;
	}



	/**
	 * Resolve fields sql column type.
	 * @author Mario Dennis
	 * @param field
	 * @return field ColumnType
	 */
	protected final ColumnType resolveColumnType (Field field){
		if (field.getAnnotation(Id.class) != null){
			return ColumnType.PRIMARY;
		}
		else if (field.getAnnotation(Foreign.class) != null){
			Class<?> clazz = field.getType();
			if (clazz.getAnnotation(Entity.class) == null)
				throw new NotEntityException();

			return ColumnType.FOREIGN;
		}
		return ColumnType.REGULAR;
	}

	/**
	 * Resolve entity generation strategy
	 * @author Mario Dennis
	 * @param field
	 * @return primary key generation strategy
	 */
	public final GenerationType resolveGenerationType(Field field){
		Id id = field.getAnnotation(Id.class);
		if (id != null)
			return id.strategy();
		
		return GenerationType.NONE;
	}


	/**
	 * Check if table contain primary key column
	 * @author Mario Dennis
	 * @return haves primary key or not
	 */
	protected final boolean containPrimaryKey() {
		return (primaryKeyColumn != null);
	}


	/**
	 * Check if table contain foreign key column
	 * @author Mario Dennis
	 * @return haves primary key or not
	 */
	protected final boolean containForeignKey() {
		return foreignKeyColumns.size() > 0;
	}

	/**
	 * Get the primary key column of table
	 * @author Mario Dennis
	 * @return primary key column or null if none is defined.
	 */
	protected final Column getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}

	/**
	 * Gets Set of foreign key columns
	 * @author Mario Dennis
	 * @return foreign key column or empty collection if none is defined
	 */
	protected final Set<Column> getForeignKeyColumns(){
		return foreignKeyColumns;
	}
}
