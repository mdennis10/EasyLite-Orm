package com.easyliteorm;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.exception.NotEntityException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TableRegistry {
	private final Map<String, Table> registry;
	private final SQLiteTypeRegistry sqliteTypeRegistry;
	
	public TableRegistry(SQLiteTypeRegistry sqliteTypeRegistry) {
		this.sqliteTypeRegistry = sqliteTypeRegistry;
		this.registry = new HashMap<String, Table>();
	}
	

	protected final Map<String, Table> getRegistry() {
		return registry;
	}


	/**
	 * Add Table to registry
	 * @author Mario Dennis 
	 * @param entity
	 */
	public void addTable(Class<?> entity) {
		if (entity == null)
			throw new NullPointerException("Null agrument supplied");
		
		Table table = new Table(entity,sqliteTypeRegistry);
		getRegistry().put(table.getName(), table);
	}


	/**
	 * Get the table name of Entity 
	 * @author Mario Dennis
	 * @param clazz
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
	 * @return Set<Table<?>>
	 */
	public  Set<Table> getRegisteredTables (){
		Set<Table> tables = new HashSet<Table>();
		
		for(Entry<String, Table> entry : getRegistry().entrySet()){
			tables.add(entry.getValue());
		}
		return tables;
	}
}
