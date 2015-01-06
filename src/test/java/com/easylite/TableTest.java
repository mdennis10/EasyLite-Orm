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
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.SqliteTypeResolver;
import com.easylite.Table;
import com.easylite.annotation.Entity;
import com.easylite.annotation.Id;
import com.easylite.exception.EasyLiteSqlException;
import com.easylite.exception.NoPrimaryKeyFoundException;
import com.easylite.exception.NotEntityException;
import com.easylite.model.Car;
import com.easylite.model.NoIdEntity;
import com.easylite.model.Note;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TableTest {
	private Table table;
	private SQLiteDatabase db;

	@Before public void setUp (){
		this.table = new Table();
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(FakeDbAttributes.dbName),null);
	}
	
	@Test(expected = NotEntityException.class)
	public void createTableMethodThrowNotEntityExceptionTest (){ 
		table.createTable(Mockito.mock(SQLiteDatabase.class), String.class);
	}
	
	@Test(expected = EasyLiteSqlException.class)
	public void createTableMethodThrowSqlExceptionTest (){
		this.table.createTbSql  = new StringBuffer("CREATE TABLE IF EXIST");
		table.createTable(db, Note.class);
	}
	
	@Test public void createTableMethodTableNameIsExtractedFromEntityAnnotationTest (){
		table.createTable(db, Note.class);
		Assert.assertEquals(Note.class.getAnnotation(Entity.class).name(), table.name);
	}
	
	@Test public void createTableMethodTableNameIsEntityNamebyDefaultTest (){
		table.createTable(db, Car.class);
		Assert.assertEquals(Car.class.getSimpleName(), table.name);
	}

	@Test public void createTableMethodMapsEntityFieldNameAndSqliteDataType (){
		table.createTable(db, Note.class);
		Map<String, String> columns = new HashMap<String, String>();
		for (Field field : Note.class.getFields()){
			String name = field.getName();
			String type = SqliteTypeResolver.resolver(field.getType().getName());
			if (field.getAnnotation(Id.class) == null)
				columns.put(name, type);
			else{
				table.primaryKeyName = name;
				table.primaryKeyType = type;
			}
		}
		Assert.assertEquals("id", table.primaryKeyName);
		Assert.assertEquals("INTEGER", table.primaryKeyType);
		Assert.assertEquals(columns, table.columns);
	}
	
	@Test public void createTableMethodAllTableColumnsCreatedTest (){
		table.createTable(db, Note.class);
		Assert.assertTrue(table.createTbSql.toString().isEmpty());
	}
	
	@Test public void createTbSqlIsClearedAfterCreateTableMethodExecution (){
		table.createTable(db, Note.class);
		Assert.assertTrue(table.createTbSql.toString().isEmpty());
	}
	
	@Test(expected = NoPrimaryKeyFoundException.class) 
	public void createTableMethodThrowNoPrimaryKeyFoundExceptionTest (){
		table.createTable(db, NoIdEntity.class);
	}
	
	@Test public void createTableTest (){
		table.createTable(db, Note.class);
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
		Cursor cursor = db.rawQuery(sql, new String[]{table.name});
		Assert.assertTrue(cursor.moveToFirst());
	}
	
	@Test(expected = NotEntityException.class)
	public void dropTableMethodThrowNotEntityExceptionTest (){
		table.dropTable(db, String.class);
	}
	
	@Test(expected = EasyLiteSqlException.class)
	public void dropTableMethodThrowSqlExceptionTest (){
		table.DROP_TABLE_IF_EXIST = "DROP TABLE IF EXIST";
		table.dropTable(db, Note.class);
	}
	
	@Test public void dropMethodTableNameIsExtractedFromEntityAnnotationTest (){
		table.dropTable(db, Note.class);
		Assert.assertEquals(Note.class.getAnnotation(Entity.class).name(), table.name);
	}
	
	@Test public void dropMethodTableNameIsEntityNamebyDefaultTest (){
		table.dropTable(db, Car.class);
		Assert.assertEquals(Car.class.getSimpleName(), table.name);
	}
	
	@Test public void dropMethodTest (){
		table.dropTable(db, Note.class);
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
		Cursor cursor = db.rawQuery(sql, new String[]{table.name});
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
		if (table.name != null)
			db.execSQL("DROP TABLE IF EXISTS " + table.name);
		
		this.table = null;
		this.db.close();
		this.db    = null;
	}
}
