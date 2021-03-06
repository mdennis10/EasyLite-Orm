package com.easyliteorm;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.model.Book;
import com.easyliteorm.model.NoIdEntity;
import com.easyliteorm.model.Note;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;

public class TableTest {
	private SQLiteTypeRegistry typeRegistry;
	
	@Before public void setup (){
		this.typeRegistry = new SQLiteTypeRegistry();
	}
	
	@Test public void getNameTest (){
		String tableName = Book.class.getSimpleName();
		
		Table book = new Table(Book.class,typeRegistry);
		String result = book.getName();
		Assert.assertNotNull(result);
		Assert.assertEquals(tableName, result);
		
		tableName = Note.class.getAnnotation(Entity.class)
                			  .name();
		
		Table note = new Table(Note.class,typeRegistry);
		result = note.getName();
		Assert.assertNotNull(result);
		Assert.assertEquals(tableName, result);
	}
	
	
	@Test public void getColumnsTest (){
		Table book = new Table(Book.class,typeRegistry);
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
		Table table = new Table(Book.class, typeRegistry);
		
		ColumnType columnType = table.resolveColumnType(Book.class.getDeclaredField("id"));
		Assert.assertNotNull(columnType);
		Assert.assertEquals(ColumnType.PRIMARY, columnType);

		
		columnType = table.resolveColumnType(Book.class.getDeclaredField("reciever"));
		Assert.assertNotNull(columnType);
		Assert.assertEquals(ColumnType.REGULAR, columnType);

		columnType = table.resolveColumnType(Note.class.getDeclaredField("book"));
		Assert.assertNotNull(columnType);
		Assert.assertEquals(ColumnType.FOREIGN, columnType);
	}
	
	@Test public void resolveGenerationTypeTest () throws NoSuchFieldException, SecurityException{
		Table table = new Table(Book.class, typeRegistry);
		
		GenerationType result = table.resolveGenerationType(Book.class.getDeclaredField("id"));
		Assert.assertNotNull(result);
		Assert.assertEquals(GenerationType.AUTO, result);
		
		result = table.resolveGenerationType(Book.class.getDeclaredField("reciever"));
		Assert.assertNotNull(result);
		Assert.assertEquals(GenerationType.NONE, result);
	}
	
	@Test public void containsPrimaryKeyTest (){
		Table table = new Table(NoIdEntity.class, typeRegistry);
		boolean result = table.containPrimaryKey();
		Assert.assertFalse(result);
		
		table = new Table(Book.class, typeRegistry);
		result = table.containPrimaryKey();
		Assert.assertTrue(result);
	}
	
	@Test public void getPrimaryKeyColumnsTest (){
		Table table = new Table(Book.class, typeRegistry);
		Column column = table.getPrimaryKeyColumn();
		Assert.assertNotNull(column);
		Assert.assertEquals(ColumnType.PRIMARY, column.getColumnType());
	}


	@Test public void containsForiegnKeyColumnTest (){
		Table table = new Table(Note.class,typeRegistry);
		boolean result = table.containForeignKey();
		Assert.assertTrue("No foreign key",result);
	}

	@Test public void foriegnKeysColumnsAddToTableTest (){
		Table table = new Table(Note.class,typeRegistry);
		Set<Column> foreignColumns = table.getForeignKeyColumns();

		Assert.assertFalse("No Foreign Columns found",foreignColumns.isEmpty());
	}

	@After public void tearDown (){
		this.typeRegistry = null;
	}
}
