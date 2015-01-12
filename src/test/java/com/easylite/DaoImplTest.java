package com.easylite;


import java.util.ArrayList;
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.model.Car;
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
		entityClasses.add(Car.class);
		dbLite.initialize(activity, FakeDbAttributes.dbName, FakeDbAttributes.version,entityClasses);
		db = dbLite.getDao(Note.class).getSqLiteDatabase();
	}

	@Test(expected = NullPointerException.class)
	public void createMethodThrowsNullPointExceptionWhenNullValuePassed (){
		Dao<Integer, Note> noteDao = dbLite.getDao(Note.class);
		noteDao.create(null);
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
	
	@Test public void batchCreateMethodTest (){
		List<Note> notes = new ArrayList<Note>();
		Note note = new Note();
		note.id = 1;
		notes.add(note);
		
		Note note2 = new Note();
		note2.id = 2;
		notes.add(note2);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		boolean result = dao.batchCreate(notes);
		
		Cursor cursor = db.query("Note", null, null, null, null, null, null);
		while (cursor.moveToNext()){
			Note actual = new Note();
			actual.id = cursor.getInt(cursor.getColumnIndex("id"));
			Assert.assertEquals(actual.id, notes.get(cursor.getPosition()).id);
		}
		Assert.assertTrue(result);
	}
	
	@Test public void batchCreateFailsWhenAnyInsertOperationFail (){
		List<Note> notes = new ArrayList<Note>();
		
		Note note = new Note();
		note.id = 1;
		notes.add(note);
		
		Note note2 = new Note();
		note2.id = 1;
		notes.add(note2);//both elements have same primary key,therefore should fail
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		boolean isSuccessful = dao.batchCreate(notes);
		Cursor cursor = db.rawQuery("SELECT * FROM Note WHERE id=?", new String[]{"1"});
		Assert.assertFalse("Batch Create Did Not Fail",isSuccessful);
		Assert.assertFalse("Batch Create Did Not Fail",cursor.moveToFirst());
	}
	
	
	@Test public void batchCreateWhereNotExistTest (){
		List<Note> notes = new ArrayList<Note>();
		Note note = new Note();
		note.id = 1;
		note.author = "john doe";
		notes.add(note);
		
	
		Note note2 = new Note();
		note2.id = 1;
		note2.author = "jane doe";
		notes.add(note2);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		int numInserted = dao.batchCreateWhereNotExist(notes);
		Assert.assertEquals(1, numInserted);
		
		Cursor cursor = db.query("Note", null, null, null, null, null, null);
		numInserted = 0;
		while (cursor.moveToNext())
			++numInserted;
		Assert.assertEquals(1, numInserted);
	}
	
	
	@Test(expected = NullPointerException.class)
	public void updateMethodThrowNullPointException (){
		dbLite.getDao(Note.class).update(null,null,null);
	}
	
	@Test public void updateMethodTest (){
		ContentValues values = new ContentValues();
		values.put("id", 1);
		values.put("author","john doe");
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue("Insertion failed", id > 0);
		
		Note note = new Note();
		note.id = 1;
		note.author = "new author";
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		dao.update(note,"id=?",new String[] {Integer.toString(note.id)});
		
		Cursor cursor = db.rawQuery("SELECT * FROM Note WHERE id=?", new String []{Long.toString(id)});
		if (cursor.moveToFirst()){
			Note actual = new Note ();
			actual.id = cursor.getInt(cursor.getColumnIndex("id"));
			actual.author = cursor.getString(cursor.getColumnIndex("author"));
			Assert.assertEquals(note.author, actual.author);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test public void updateMethodContentValueExtractsDate (){
		Note note = new Note();
		note.id = 1;
		note.date = new Date(2013, 11, 2);
		
		ContentValues values = new ContentValues();
		values.put("id", 1);
		values.put("date", new Date().getTime());
		long id = db.insert("Note", null, values);
		
		Assert.assertEquals(1, id);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		int rowAffected = dao.update(note, "id=?", new String[] {Integer.toString(note.id)});
		Assert.assertTrue("No rows affected, so Date not updated",rowAffected > 0);
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
	
	@Test public void isExistMethodTest (){
		ContentValues values = new ContentValues();
		values.put("id", 1);
		values.put("body", "text");
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue("Note instance not created",id > 0);
		
		Note note = new Note ();
		note.id = 1;
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		Assert.assertTrue("Note instance not found",dao.isExist(note));
	}
	
	@Test public void findAllWithWhereClauseTest (){
		ContentValues values = new ContentValues();
		values.put("id", 1);
		values.put("body", "text");
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue("Note instance not created",id > 0);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		List<Note> notes = dao.findAll("id=?", new String[]{"1"});
		Assert.assertTrue("Empty List<Note> Returned",!notes.isEmpty());
		Assert.assertEquals(1, notes.get(0).id);
	}
	
	@After public void tearDown (){
		db.execSQL("DELETE FROM Note");
		this.dbLite = null;
	}
}
