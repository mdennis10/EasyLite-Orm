package com.easyliteorm;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.exception.NotEntityException;
import com.easyliteorm.model.Book;
import com.easyliteorm.model.Note;

public class TableRegistryTest {
	private TableRegistry tableRegistry;
	private SqliteTypeRegistry typeRegistry;
	
	@Before public void setup (){
		this.typeRegistry = new SqliteTypeRegistry();
		this.tableRegistry = new TableRegistry(typeRegistry);
	}
	
	
	@Test public void getRegistryTest (){
		Assert.assertNotNull(tableRegistry.getRegistry());
	}
	
	
	@Test public void addTableTest (){		
		tableRegistry.addTable(Book.class);
		
		Map<String, Table> registry = tableRegistry.getRegistry();
		Table result = registry.get(Book.class.getSimpleName());
		Assert.assertNotNull(result);
	}
	
	
	@Test public void getRegisteredTablesTest (){
		tableRegistry.getRegistry()
		             .put("Book", new Table(Book.class,typeRegistry));
		
		Set<Table> result = tableRegistry.getRegisteredTables();
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isEmpty());
	}
	
	
	@Test public void getTableNameTest (){
		/*
		 * Entity class where @Entity does 
		 * not contain name attribute 
		 */
		String result = TableRegistry.getTableName(Book.class);
		Assert.assertNotNull(result);
		Assert.assertEquals(Book.class.getSimpleName(), result);
		
		
		/*
		 * Entity class where @Entity 
		 * contains name attribute 
		 */
		result = TableRegistry.getTableName(Note.class);
		String actual = Note.class.getAnnotation(Entity.class).name();
		Assert.assertNotNull(result);
		Assert.assertEquals(actual, result);
	}
	
	@Test(expected = NotEntityException.class) 
	public void getTableName_throwsNotEntityExceptionTest (){
		TableRegistry.getTableName(String.class);
	}
	
	
	
	@Test(expected = NullPointerException.class)
	public void getTableName_throwsNullExceptionTest (){
		TableRegistry.getTableName(null);
	}
	
	@After public void tearDown (){
		this.tableRegistry = null;
	}
}
