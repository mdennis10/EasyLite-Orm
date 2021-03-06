package com.easyliteorm;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.exception.NotEntityException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public final class TableRegistry {
	private final Map<String, Table> registry;
	private final SQLiteTypeRegistry sqliteTypeRegistry;
	
	protected TableRegistry(SQLiteTypeRegistry sqliteTypeRegistry) {
		this.sqliteTypeRegistry = sqliteTypeRegistry;
		this.registry = new HashMap<String, Table>();
	}
	

	protected final Map<String, Table> getRegistry() {
		return registry;
	}


	/**
	 * Add Table to registry
	 * @author Mario Dennis 
	 * @param entity class instance
	 */
	protected void addTable(Class<?> entity) {
		if (entity == null)
			throw new NullPointerException("Null argument supplied");
		
		Table table = new Table(entity,sqliteTypeRegistry);
		getRegistry().put(table.getName(), table);
	}


	/**
	 * Get the table name of Entity 
	 * @author Mario Dennis
	 * @param clazz instance
	 * @return name
	 */
	protected static String getTableName(Class<?> clazz) {
		Entity entityAnnotation = clazz.getAnnotation(Entity.class);
		if (entityAnnotation == null)
			throw new NotEntityException();
		
		String name = entityAnnotation.name();
		return (name != null && !name.isEmpty()) ? name : clazz.getSimpleName();
	}
	
	
	/**
	 * Get all registered tables
	 * @author Mario Dennis
	 * @return Set of registered tables
	 */
	protected Set<Table> getRegisteredTables (){
		Set<Table> tables = new HashSet<Table>();
		
		for(Entry<String, Table> entry : getRegistry().entrySet()){
			tables.add(entry.getValue());
		}
		return tables;
	}
}
