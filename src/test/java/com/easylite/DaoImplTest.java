package com.easylite;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.exception.NotEntityException;
import com.easylite.model.Note;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DaoImplTest {
	
	private EasyLite dbLite;
	private SQLiteDatabase db;

	public DaoImplTest() {
		Activity activity = Robolectric.buildActivity(Activity.class).create().get();
		
		this.dbLite = EasyLite.getInstance();
		
		Set<Class<?>> entityClasses = new HashSet<Class<?>>();
		entityClasses.add(Note.class);
		
		dbLite.initialize(activity, FakeDbAttributes.dbName, FakeDbAttributes.version,entityClasses);
		db = dbLite.getDao(Note.class).getSqLiteDatabase();
	}

	@Test(expected = NullPointerException.class)
	public void createMethodThrowsNullPointExceptionWhenNullValuePassed (){
		Dao<Integer, Note> noteDao = dbLite.getDao(Note.class);
		noteDao.create(null);
	}
	
	@Test(expected = NotEntityException.class)
	public void createMethodThrowNotEntityExceptionTest (){
		Dao<Integer, String> strDao = dbLite.getDao(String.class);
		strDao.create("entity");
	}
	
	@Test public void createMethodTest (){
		Note note = new Note();
		note.id = 2;
		note.body = "Body Text";
		note.author = "John Doe";
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		long id = dao.create(note);
		Assert.assertTrue(id > 0);
	}
	
	@Test public void findAllMethodInstancesAddedToListTest (){
		ContentValues values = new ContentValues();
		values.put("id", 1);
		values.put("body", "text");

		long id = db.insert("Note", null, values);
		Assert.assertTrue(id > 0);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		List<Note> notes = dao.findAll();
		Assert.assertTrue(notes.size() > 0);
	}
	
	
	@Test public void findAllMethodFieldsPopulatedWithDataTest (){
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		ContentValues values = new ContentValues();
		values.put("id", 4);
		values.put("body", "text");
		values.put("author", "john doe");
		values.put("date", new Date().toString());
		values.put("sent", true);
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue(id > 0);
		
		List<Note> notes = dao.findAll();
		for (Note note : notes){
			Assert.assertNotNull(note.author);
			Assert.assertNotNull(note.date);
			Assert.assertNotNull(note.body);
			Assert.assertTrue(note.id > 0);
			Assert.assertTrue(note.sent);
		}
	}
	
	@Test public void findByIdMethodTest (){
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		ContentValues values = new ContentValues();
		values.put("id", 4);
		values.put("body", "text");
		values.put("author", "john doe");
		values.put("date", new Date().toString());
		values.put("sent", true);
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue(id > 0);
		
		Note note = dao.findById((int)id);
		Assert.assertNotNull(note);
	}
	
	@Test (expected = NullPointerException.class)
	public void deleteMethodThrowsNullPointerExceptionTest (){
		dbLite.getDao(Note.class).delete(null);
	}
	
	@Test public void deleteMethodTest (){
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		ContentValues values = new ContentValues();
		values.put("id", 4);
		values.put("body", "text");
		values.put("author", "john doe");
		values.put("date", new Date().toString());
		values.put("sent", true);
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue(id > 0);
		Note note = new Note ();
		note.id = 4;
		
		int rows = dao.delete(note);
		Assert.assertTrue(rows > 0);
	}
	@After public void tearDown (){
		db.execSQL("DELETE FROM Note");
		this.dbLite = null;
	}
}
