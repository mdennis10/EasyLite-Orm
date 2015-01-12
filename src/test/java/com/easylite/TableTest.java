package com.easylite;

import java.io.File;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.annotation.GenerationType;
import com.easylite.exception.NoPrimaryKeyFoundException;
import com.easylite.exception.NotTableException;
import com.easylite.model.Car;
import com.easylite.model.NoIdEntity;
import com.easylite.model.Note;

@RunWith(RobolectricTestRunner.class)
public class TableTest {

	private SQLiteDatabase db;

	@Before public void setUp (){
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(FakeDbAttributes.dbName),null);
	}
	
	@Test public void getTableNameTest (){
		String actual = Table.getTableName(Note.class);
		Assert.assertEquals("Note", actual);
		
		actual = Table.getTableName(Car.class);
		Assert.assertEquals("Car", actual);
	}
	
	@Test (expected = NotTableException.class)
	public void getTableNameThrowNotTableException (){
		Table.getTableName(String.class);
	}
	
	@Test public void getTableKeys (){
		Map<String, String> keys = Table.getTableKeys(Note.class);
		Assert.assertFalse("No Keys returned",keys.isEmpty());
		Assert.assertEquals("id", keys.get(Table.P_KEY_NAME));
		Assert.assertEquals(SqliteTypeResolver.INTEGER, keys.get(Table.P_KEY_TYPE));
	}
	
	@Test (expected = NoPrimaryKeyFoundException.class)
	public void getTableKeysThrowNoPrimaryKeyFoundException (){
		Table.getTableKeys(NoIdEntity.class);
	}
	
	
	@Test public void getTableColumnsTest (){
		Map<String, String> columns = Table.getTableColumns (Note.class);
		Assert.assertFalse("No Columns returned",columns.isEmpty());
	}
	
	@Test public void getGenerationStategyTest (){
		GenerationType actual = Table.getGenerationStrategy(Car.class,"id");
		Assert.assertEquals(GenerationType.AUTO, actual);
	}
	
	@Test public void dropTableTest (){
		db.execSQL("CREATE TABLE Note (id INTEGER PRIMARY KEY)");
		Table.dropTable(db,Note.class);
		
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
		Cursor cursor = db.rawQuery(sql, new String[]{"Note"});
		Assert.assertFalse(cursor.moveToFirst());
	}
	
	@Test public void createTableTest (){
		Table.createTable(db, Note.class);
		
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
		Cursor cursor = db.rawQuery(sql, new String[]{"Note"});
		Assert.assertTrue(cursor.moveToFirst());
	}
	
	@After public void tearDown (){
		db.execSQL("DROP TABLE IF EXISTS Note");
		db.execSQL("DROP TABLE IF EXISTS Car");
		this.db.close();
		this.db    = null;
	}
}
