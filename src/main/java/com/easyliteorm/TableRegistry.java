package com.easyliteorm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.exception.NotEntityException;

public class TableRegistry {
	private final Map<String, Table<?>> registry;
	private final SqliteTypeRegistry sqliteTypeRegistry;
	
	public TableRegistry(SqliteTypeRegistry sqliteTypeRegistry) {
		this.sqliteTypeRegistry = sqliteTypeRegistry;
		this.registry = new HashMap<String, Table<?>>();
	}
	

	protected final Map<String, Table<?>> getRegistry() {
		return registry;
	}


	/**
	 * Add Table to registry
	 * @author Mario Dennis 
	 * @param entity
	 */
	public <T> void addTable(Class<T> entity) {
		if (entity == null)
			throw new NullPointerException("Null agrument supplied");
		
		Table<T> table = new Table<T>(entity,sqliteTypeRegistry);
		getRegistry().put(table.getName(), table);
	}


	/**
	 * Get the table name of Entity 
	 * @author Mario Dennis
	 * @param entity
	 * @return name
	 */
	protected static final <T> String getTableName(Class<T> clazz) {
		Entity entityAnnotation = clazz.getAnnotation(Entity.class);
		if (entityAnnotation == null)
			throw new NotEntityException();
		
		String name = entityAnnotation.name();
		return (name != null && !name.isEmpty()) ? name : clazz.getSimpleName();
	}
	
	
	/**
	 * Get all registered tables
	 * @author Mario Dennis
	 * @return Set<Table<?>>
	 */
	public  Set<Table<?>> getRegisteredTables (){
		Set<Table<?>> tables = new HashSet<Table<?>>();
		
		for(Entry<String, Table<?>> entry : getRegistry().entrySet()){
			tables.add(entry.getValue());
		}
		return tables;
	}
}
