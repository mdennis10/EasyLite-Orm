package com.easyliteorm;

import java.io.File;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easyliteorm.annotation.Entity;
import com.easyliteorm.annotation.GenerationType;
import com.easyliteorm.annotation.Id;
import com.easyliteorm.exception.NoPrimaryKeyFoundException;
import com.easyliteorm.exception.NoSuitablePrimaryKeySuppliedException;
import com.easyliteorm.exception.NotEntityException;
import com.easyliteorm.exception.UnAuthorizeGenerationTypeException;
import com.easyliteorm.model.Car;
import com.easyliteorm.model.NoIdEntity;
import com.easyliteorm.model.Note;

@RunWith(RobolectricTestRunner.class)
public class TableUtilTest {

	private SQLiteDatabase db;

	@Before public void setUp (){
		Context context = Robolectric.buildActivity(Activity.class).get();
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(ManifestUtil.getDatabaseName(context)),null);
	}
	
	
	@Test public void getTableNameTest (){
		String actual = TableUtil.getTableName(Note.class);
		Assert.assertEquals("Note", actual);

		actual = TableUtil.getTableName(Car.class);
		Assert.assertEquals("Car", actual);
	}
	
	@Entity private class MyModel{@Id boolean key;}
	@Test(expected = NoSuitablePrimaryKeySuppliedException.class)
	public void noSuitablePrimaryKeySuppliedExceptionIsThrownWhenNotIntLongStringTest (){
		TableUtil.getTableKeys(MyModel.class);
	}
	
	@Entity private class MyModel2{@Id(strategy = GenerationType.AUTO) String key;}
	@Test(expected = UnAuthorizeGenerationTypeException.class) 
	public void unAuthorizeGenerationTypeExceptionForAutoStrategyTest (){
		TableUtil.getGenerationStrategy(MyModel2.class, "key");
	}
	
	@Test (expected = NotEntityException.class)
	public void getTableNameThrowNotTableException (){
		TableUtil.getTableName(String.class);
	}
	
	@Test public void getTableKeys (){
		Map<String, String> keys = TableUtil.getTableKeys(Note.class);
		Assert.assertFalse("No Keys returned",keys.isEmpty());
		Assert.assertEquals("id", keys.get(TableUtil.P_KEY_NAME));
		Assert.assertEquals(SqliteTypeResolver.INTEGER, keys.get(TableUtil.P_KEY_TYPE));
	}
	
	@Test (expected = NoPrimaryKeyFoundException.class)
	public void getTableKeysThrowNoPrimaryKeyFoundException (){
		TableUtil.getTableKeys(NoIdEntity.class);
	}
	
	
	@Test public void getTableColumnsTest (){
		Map<String, String> columns = TableUtil.getTableColumns (Note.class);
		Assert.assertFalse("No Columns returned",columns.isEmpty());
	}
	
	@Test public void getGenerationStategyTest (){
		GenerationType actual = TableUtil.getGenerationStrategy(Car.class,"id");
		Assert.assertEquals(GenerationType.AUTO, actual);
	}
	
	@Test public void dropTableTest (){
		db.execSQL("CREATE TABLE Note (id INTEGER PRIMARY KEY)");
		TableUtil table = new TableUtil(new SqliteTypeRegistry());
		table.dropTable(db,Note.class);
		
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
		Cursor cursor = db.rawQuery(sql, new String[]{"Note"});
		Assert.assertFalse(cursor.moveToFirst());
	}
	
	@Test public void createTableTest (){
		TableUtil table = new TableUtil(new SqliteTypeRegistry());
		table.createTable(db, Note.class);
		
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
		Cursor cursor = db.rawQuery(sql, new String[]{"Note"});
		Assert.assertTrue(cursor.moveToFirst());
	}
	
	@After public void tearDown (){
		db.execSQL("DROP TABLE IF EXISTS Note");
		db.execSQL("DROP TABLE IF EXISTS Car");
		db.execSQL("DROP TABLE IF EXISTS MyModel");
		db.execSQL("DROP TABLE IF EXISTS MyModel2");
		this.db.close();
		this.db    = null;
	}
}
