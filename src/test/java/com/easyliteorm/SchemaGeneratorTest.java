package com.easyliteorm;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.easyliteorm.model.Book;

@RunWith(RobolectricTestRunner.class)
public class SchemaGeneratorTest {
	private SchemaGenerator schemaGenerator;
	private SqliteTypeRegistry typeRegistry;
	private SQLiteDatabase db;
	
	@Before public void setup (){
		this.typeRegistry = new SqliteTypeRegistry();
		this.schemaGenerator = new SchemaGenerator();
		
		Context context = Robolectric.buildActivity(Activity.class).get();
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(ManifestUtil.getDatabaseName(context)),null);
	}
	
	@Test public void createTableTest (){
		Table<Book> table = new Table<Book>(Book.class, typeRegistry);
		String result = schemaGenerator.createTable(table);
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isEmpty());
		
		db.execSQL(result);//ensure statement executes without error
	}
	
	@Test public void dropTableTest (){
		Table<Book> table = new Table<Book>(Book.class, typeRegistry);
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
