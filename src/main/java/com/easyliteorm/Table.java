package com.easyliteorm;

import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.annotation.Id;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class Table {
	private boolean CONTAIN_PRIMARY_KEY = false;
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

	private void setColumns(SQLiteTypeRegistry typeRegistry) {
		columns = new HashSet<Column>();
		Field[] fields = entity.getDeclaredFields();
		for (Field field : fields)
			setColumnField(field.getName(),typeRegistry.resolve(field.getType()), 
					       resolveColumnType(field),resolveGenerationType(field));
	}
	
	
	protected void setColumnField (String name,SQLiteType sqliteType,ColumnType columnType,GenerationType generationType){
		Column column = new Column();
		column.setName(name);
		column.setColumnType(columnType);
		column.setSqliteType(sqliteType);
		column.setGenerationType(generationType);
		columns.add(column);
		
		if (columnType == ColumnType.PRIMARY)
			primaryKeyColumn = column;
	}
	
	
	protected ColumnType resolveColumnType (Field field){
		Id id = field.getAnnotation(Id.class);
		if (id != null){
			CONTAIN_PRIMARY_KEY = true;
			return ColumnType.PRIMARY;
		}
		
		return ColumnType.REGULAR;
	}	
	
	protected GenerationType resolveGenerationType(Field field){
		Id id = field.getAnnotation(Id.class);
		if (id != null)
			return id.strategy();
		
		return GenerationType.NONE;
	}


	protected boolean containPrimaryKey() {
		return CONTAIN_PRIMARY_KEY;
	}


	protected Column getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}
}
