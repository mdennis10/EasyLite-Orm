package com.easyliteorm;

import com.easyliteorm.annotation.Foreign;
import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.annotation.Id;

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
	private Set<Column> columns;
	private Column primaryKeyColumn;
	
	protected Table(Class<?> entity,SQLiteTypeRegistry typeRegistry) {
		this.entity  = entity;
		this.name    = TableRegistry.getTableName(entity);
		setColumns(typeRegistry);
	}

	
	public String getName() {
		return name;
	}
		
	public Set<Column> getColumns(){
		return columns;
	}


	private final void setColumns(SQLiteTypeRegistry typeRegistry) {
		columns = new HashSet<Column>();
		Field[] fields = entity.getDeclaredFields();
		for (Field field : fields) {
			setColumnField(field,typeRegistry);
		}
	}

	protected final void setColumnField (Field field, SQLiteTypeRegistry typeRegistry){
		ColumnType columnType = resolveColumnType(field);

		Column column = new Column();
		column.setName(field.getName());
		column.setColumnType(columnType);
		column.setSqliteType(typeRegistry.resolve(field.getType()));
		column.setGenerationType(resolveGenerationType(field));

		columns.add(column);

		if (columnType == ColumnType.PRIMARY)
			primaryKeyColumn = column;
	}



	/**
	 * Resolve fields sql column type.
	 * @author Mario Dennis
	 * @param field
	 * @return field ColumnType
	 */
	protected final ColumnType resolveColumnType (Field field){
		if (field.getAnnotation(Id.class) != null){
			CONTAIN_PRIMARY_KEY = true;
			return ColumnType.PRIMARY;
		}
		else if (field.getAnnotation(Foreign.class) != null){
			CONTAIN_FOREIGN_KEY = true;
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


	protected final boolean containPrimaryKey() {
		return CONTAIN_PRIMARY_KEY;
	}


	protected final Column getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}
}
