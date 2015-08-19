package com.easyliteorm;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.easyliteorm.exception.NoPrimaryKeyFoundException;
import com.easyliteorm.exception.NoSuitablePrimaryKeySuppliedException;
import com.easyliteorm.model.Book;
import com.easyliteorm.model.NoIdEntity;
import com.easyliteorm.model.NonNumeric;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.File;

@RunWith(RobolectricTestRunner.class)
public class SchemaGeneratorTest {
	private SchemaGenerator schemaGenerator;
	private SQLiteTypeRegistry typeRegistry;
	private SQLiteDatabase db;
	
	@Before public void setup (){
		this.typeRegistry = new SQLiteTypeRegistry();
		this.schemaGenerator = new SchemaGenerator();
		
		Context context = Robolectric.buildActivity(Activity.class).get();
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(ManifestUtil.getDatabaseName(context)),null);
	}
	
	@Test public void createTableTest () throws NoPrimaryKeyFoundException{
		Table table = new Table(Book.class, typeRegistry);
		String result = schemaGenerator.createTable(table);
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isEmpty());
		
		db.execSQL(result);//ensure statement executes without error
	}
	
	@Test(expected = NoPrimaryKeyFoundException.class)
	public void createTable_throwsNoPrimaryKeyFoundExceptionTest () throws NoPrimaryKeyFoundException{
		Table table = new Table(NoIdEntity.class, typeRegistry);
		schemaGenerator.createTable(table);
	}
	
	@Test(expected = NoSuitablePrimaryKeySuppliedException.class)
	public void createTableTest_throwsNoSuitabePrimaryKeySuppliedExceptionTest () throws NoPrimaryKeyFoundException{
		Table table = new Table(NonNumeric.class, typeRegistry);
		schemaGenerator.createTable(table);
	}
	
	@Test public void dropTableTest (){
		Table table = new Table(Book.class, typeRegistry);
		String result = schemaGenerator.dropTable(table);
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isEmpty());
		
		db.execSQL(result);//ensure statement executes without error
	}

	
	@After public void tearDown (){
		this.schemaGenerator = null;
		this.typeRegistry    = null;
		db.execSQL("DROP TABLE IF EXISTS Book");
		this.db.close();
		this.db    = null;
	}
}
