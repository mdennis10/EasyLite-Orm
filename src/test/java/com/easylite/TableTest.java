package com.easylite;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.annotation.Entity;
import com.easylite.annotation.Id;
import com.easylite.exception.EasyLiteSqlException;
import com.easylite.exception.NoPrimaryKeyFoundException;
import com.easylite.exception.NotEntityException;
import com.easylite.model.Car;
import com.easylite.model.NoIdEntity;
import com.easylite.model.Note;
import com.easylite.model.TableDoesNotExist;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TableTest {

	private SQLiteDatabase db;

	@Before public void setUp (){
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(FakeDbAttributes.dbName),null);
	}
	
	@Test(expected = NotEntityException.class)
	public void tableThrowNotEntityExceptionTest (){ 
		new Table(db,String.class);
	}
	
	
	@Test public void tableNameIsExtractedFromEntityAnnotationTest (){
		Table table = new Table(db,Note.class);
		Assert.assertEquals(Note.class.getAnnotation(Entity.class).name(), table.getTableName());
	}
	
	@Test public void tableNameIsEntityNamebyDefaultTest (){
		Table table = new Table(db,Car.class);
		Assert.assertEquals(Car.class.getSimpleName(), table.getTableName());
	}

	@Test public void keysExtractedFromEntity (){
		Table table = new Table(db,Note.class);
		Map<String, String> keys = table.getTableKeys();
		Assert.assertEquals("id", keys.get(Table.KEY_NAME));
		Assert.assertEquals("INTEGER", keys.get(Table.KEY_TYPE));
	}
	
	@Test public void createTableMethodMapsEntityFieldNameAndSqliteDataType (){
		Table table = new Table(db,Note.class);
		table.createTable();
		Map<String, String> columns = new HashMap<String, String>();
		for (Field field : Note.class.getFields()){
			String name = field.getName();
			String type = SqliteTypeResolver.resolver(field.getType().getName());
			if (field.getAnnotation(Id.class) == null)
				columns.put(name, type);
		}
		Assert.assertEquals(columns, table.getColumns());
	}
	
	@Test public void createTableMethodAllTableColumnsCreatedTest (){
		Table table = new Table(db,Note.class);
		table.createTable();
		Assert.assertFalse("No Table Columns Available",table.getColumns().isEmpty());
	}
	
	
	@Test(expected = NoPrimaryKeyFoundException.class) 
	public void createTableMethodThrowNoPrimaryKeyFoundExceptionTest (){
		Table table = new Table(db,NoIdEntity.class);
		table.createTable();
	}
	
	@Test public void createTableTest (){
		Table table = new Table(db,Note.class);
		table.createTable();
	
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
		Cursor cursor = db.rawQuery(sql, new String[]{table.name});
		Assert.assertTrue(cursor.moveToFirst());
	}
	
	@Test(expected = NotEntityException.class)
	public void dropTableMethodThrowNotEntityExceptionTest (){
		Table table = new Table(db,String.class);
		table.dropTable();
	}
	
	@Test(expected = EasyLiteSqlException.class)
	public void dropTableMethodThrowSqlExceptionTest (){
		Table table = new Table(db,TableDoesNotExist.class);
		table.dropTable();
	}
	
	
	@Test public void dropMethodTest (){
		Table table = new Table(db,Note.class);
		table.dropTable();
		
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
		Cursor cursor = db.rawQuery(sql, new String[]{table.getTableName()});
		Assert.assertFalse(cursor.moveToFirst());
	}
	
	@Test public void getEntityNameTest (){
		Entity entity = Note.class.getAnnotation(Entity.class);
		Assert.assertEquals(entity.name(), Table.getEntityName(Note.class));
	}
	
	@Test public void getEntityNameWithoutNameAttributeTest (){
		Assert.assertEquals(Car.class.getSimpleName(), Table.getEntityName(Car.class));
	}
	
	@Test (expected = NotEntityException.class)
	public void getEntityNameThrowNotEntityExceptionTest (){
		Table.getEntityName(String.class);
	}
	
	@Test (expected = NoPrimaryKeyFoundException.class)
	public void getPrimaryKeyNameMethodThrowNoPrimaryKeyFoundExceptionTest(){
		Table.getPrimaryKeyName(String.class);
	}
	
	@Test public void getPrimaryKeyNameTest (){
		Assert.assertEquals("id", Table.getPrimaryKeyName(Note.class));
	}
	
	@Test (expected = NoPrimaryKeyFoundException.class)
	public void getPrimaryKeyTypeNameMethodThrowNoPrimaryKeyFoundExceptionTest(){
		Table.getPrimaryKeyTypeName(String.class);
	}
	
	@Test public void getPrimaryKeyTypeNameTest (){
		Assert.assertEquals("int", Table.getPrimaryKeyTypeName(Note.class));
	}
	
	@After public void tearDown (){
		db.execSQL("DROP TABLE IF EXISTS Note");
		db.execSQL("DROP TABLE IF EXISTS Car");
		this.db.close();
		this.db    = null;
	}
}
