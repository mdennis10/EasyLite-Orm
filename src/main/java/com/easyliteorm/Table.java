package com.easyliteorm;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.annotation.Id;

public class Table<T> {
	private final String name;
	private final Class<T> entity;
	private Set<Column> columns;
	
	protected Table(Class<T> entity,SqliteTypeRegistry typeRegistry) {
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

	private void setColumns(SqliteTypeRegistry typeRegistry) {
		columns = new HashSet<Column>();
		Field[] fields = entity.getDeclaredFields();
		for (Field field : fields)
			setColumnField(field.getName(),typeRegistry.resolve(field.getType()), 
					       resolveColumnType(field),resolveGenerationType(field));
	}
	
	
	protected void setColumnField (String name,SqliteType sqliteType,ColumnType columnType,GenerationType generationType){
		Column column = new Column();
		column.setName(name);
		column.setColumnType(columnType);
		column.setSqliteType(sqliteType);
		column.setGenerationType(generationType);
		columns.add(column);
	}
	
	
	protected ColumnType resolveColumnType (Field field){
		Id id = field.getAnnotation(Id.class);
		if (id != null)
			return ColumnType.PRIMARY;
		
		return ColumnType.REGULAR;
	}	
	
	protected GenerationType resolveGenerationType(Field field){
		Id id = field.getAnnotation(Id.class);
		if (id != null)
			return id.strategy();
		
		return GenerationType.NONE;
	}
}
