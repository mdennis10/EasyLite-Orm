package com.easyliteorm;

import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.model.Book;
import com.easyliteorm.model.Note;

public class TableTest {
	private SqliteTypeRegistry typeRegistry;
	
	@Before public void setup (){
		this.typeRegistry = new SqliteTypeRegistry();
	}
	
	@Test public void getNameTest (){
		String tableName = Book.class.getSimpleName();
		
		Table<Book> book = new Table<Book>(Book.class,typeRegistry);
		String result = book.getName();
		Assert.assertNotNull(result);
		Assert.assertEquals(tableName, result);
		
		tableName = Note.class.getAnnotation(Entity.class)
                			  .name();
		
		Table<Note> note = new Table<Note>(Note.class,typeRegistry);
		result = note.getName();
		Assert.assertNotNull(result);
		Assert.assertEquals(tableName, result);
	}
	
	
	@Test public void getColumnsTest (){
		Table<Book> book = new Table<Book>(Book.class,typeRegistry);
		Set<Column> result = book.getColumns();
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isEmpty());
		
		Iterator<Column> iterator = result.iterator();
		while (iterator.hasNext()){
			Column column = iterator.next();
			Assert.assertNotNull(column.getName());
			Assert.assertFalse(column.getName().isEmpty());
			
			Assert.assertNotNull(column.getColumnType());

			Assert.assertNotNull(column.getSqliteType());
			Assert.assertFalse(column.getSqliteType().getValue().isEmpty());
			
			Assert.assertNotNull(column.getGenerationStrategy());
		}
	}
	
	@Test public void resolveColumnTypeTest () throws NoSuchFieldException, SecurityException{
		Table<Book> table = new Table<Book>(Book.class, typeRegistry);
		
		ColumnType columnType = table.resolveColumnType(Book.class.getDeclaredField("id"));
		Assert.assertNotNull(columnType);
		Assert.assertEquals(ColumnType.PRIMARY, columnType);
		
		columnType = table.resolveColumnType(Book.class.getDeclaredField("reciever"));
		Assert.assertNotNull(columnType);
		Assert.assertEquals(ColumnType.REGULAR, columnType);
	}
	
	@Test public void resolveGenerationTypeTest () throws NoSuchFieldException, SecurityException{
		Table<Book> table = new Table<Book>(Book.class, typeRegistry);
		
		GenerationType result = table.resolveGenerationType(Book.class.getDeclaredField("id"));
		Assert.assertNotNull(result);
		Assert.assertEquals(GenerationType.AUTO, result);
		
		result = table.resolveGenerationType(Book.class.getDeclaredField("reciever"));
		Assert.assertNotNull(result);
		Assert.assertEquals(GenerationType.NONE, result);
	}
	
	@After public void tearDown (){
		this.typeRegistry = null;
	}
}
