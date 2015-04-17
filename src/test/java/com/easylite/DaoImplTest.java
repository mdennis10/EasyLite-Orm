package com.easylite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.annotation.OrderByType;
import com.easylite.model.Book;
import com.easylite.model.Note;

@RunWith(RobolectricTestRunner.class)
public class DaoImplTest {
	
	private EasyLite dbLite;
	private SQLiteDatabase db;

	public DaoImplTest() {
		Activity activity = Robolectric.buildActivity(Activity.class).create().get();
		
		this.dbLite = EasyLite.getInstance(activity);
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
	
	@Test public void createMethodGetterAndSetterTest (){
		Book book = new Book();
		book.setDateRecieved(new Date());
		book.setRecieved(true);
		book.setReciever("some");
		
		Dao<Integer, Book> dao =  dbLite.getDao(Book.class);
		long id = dao.create(book);
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
	
	
	@Test public void batchCreateOverridableTest (){
		List<Note> notes = new ArrayList<Note>();
		Note note = new Note();
		note.id = 1;
		note.author = "john doe";
		notes.add(note);
		
		Note note2 = new Note();
		note2.id = 2;
		note2.author = "jane doe";
		notes.add(note2);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		dao.batchCreateOverridable(notes);

		
		Cursor cursor = db.query("Note", null, null, null, null, null, null);
		int numInserted = 0;
		while (cursor.moveToNext())
			++numInserted;
		Assert.assertEquals(2, numInserted);
	}
	
	@Test public void batchCreateOverridableDateValueIsSaved(){
		Note note = new Note();
		note.id = 1;
		note.date = new Date();
	
		List<Note> notes = new ArrayList<Note>();
		notes.add(note);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		dao.batchCreateOverridable(notes);
		
		Cursor cursor = db.query("Note", null, null, null, null, null, null);
		cursor.moveToFirst();
		long time = cursor.getLong(cursor.getColumnIndex("date"));
		Assert.assertEquals(note.date.getTime(), time);
	}
	
	@Test public void deleteAll (){
		ContentValues values = new ContentValues();
		values.put("id", 4);
		values.put("body", "text");
		values.put("author", "john doe");
		values.put("date", new Date().toString());
		values.put("sent", true);
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue("Insertion failed", id > 0);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		int rowsAffected = dao.deleteAll();
		Assert.assertEquals(1, rowsAffected);
	}
	
	
	@Test public void deleteAllWhereArgsTest (){
		ContentValues values = new ContentValues();
		values.put("id", 4);
		values.put("body", "text");
		values.put("author", "john doe");
		values.put("date", new Date().toString());
		values.put("sent", true);
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue("Insertion failed", id > 0);
		
		int rowsAffected = dbLite.getDao(Note.class).deleteAll("id=?", new String[]{"4"});
		Assert.assertEquals(1, rowsAffected);
	}
	
	@Test(expected = NullPointerException.class)
	public void updateMethodThrowNullPointException (){
		dbLite.getDao(Note.class).update(null,null,new String[]{});
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
	
	@Test public void privateFieldsGetterAndSetterMethodTest (){
		Book book = new Book();
		book.setId(1);
		book.setReciever("Mario");
		book.setAmountSent(2);
		book.setDateRecieved(new Date());
		book.setRecieved(true);
		
		Dao<Integer, Book> dao = dbLite.getDao(Book.class);
		long rowID = dao.create(book);
		Assert.assertEquals(book.getId(), rowID);
		
		Book bookDB = dao.findById(book.getId());
		Assert.assertEquals(book.getReciever(), bookDB.getReciever());
		Assert.assertEquals(book.getAmountSent(), bookDB.getAmountSent());
		Assert.assertEquals(book.isRecieved(), bookDB.isRecieved());
		Assert.assertEquals(book.getDateRecieved(), bookDB.getDateRecieved());
	}
	
	@Test public void dateEqualityTest () throws ParseException{
		Book book = new Book();
		book.setId(1);
		book.setReciever("Mario");
		book.setAmountSent(2);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
		Date date = sdf.parse("24/11/2014");
		book.setDateRecieved(date);
		
		Dao<Integer, Book> dao = dbLite.getDao(Book.class);
		long rowID = dao.create(book);
		Assert.assertEquals(book.getId(), rowID);
		
		Book book2 = new Book();
		book2.setId(2);
		book2.setReciever("Mario");
		book2.setAmountSent(2);
		book2.setDateRecieved(new Date());

		long rowID2 = dao.create(book2);
		Assert.assertEquals(book2.getId(), rowID2);
		
		List<Book> books = dao.findAll(null, null, "dateRecieved > ?", date);
		Assert.assertTrue(books.size() > 0);
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
		Date date = new Date();
		@SuppressWarnings("deprecation")
		Date laterDate = new Date(date.getYear() + 1,2,4);
		
		ContentValues values = new ContentValues();
		values.put("id", 1);
		values.put("body", "text");
		values.put("date", date.getTime());
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue("Note instance not created",id > 0);
		
		values = new ContentValues();
		values.put("id", 2);
		values.put("body", "text");
		values.put("date", laterDate.getTime());
		
		id = db.insert("Note", null, values);
		Assert.assertTrue("Note instance not created",id > 0);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		List<Note> notes = dao.findAll(null,null,"id=? AND body=?", "1","text");
		Assert.assertTrue("Empty List<Note> Returned",!notes.isEmpty());
		Assert.assertEquals(1, notes.get(0).id);
		
		notes = dao.findAll(null, null, "date >= ?", date);
		Assert.assertTrue("Empty List<Note> Returned",!notes.isEmpty());
		Assert.assertEquals(2, notes.size());
	}
	
	@Test public void findAllOrderByTest (){
		ContentValues values = new ContentValues();
		values.put("id", 11);
		values.put("body", "text");
		values.put("date", new Date().getTime());
		
		long id = db.insert("Note", null, values);
		Assert.assertTrue("Note instance not created",id > 0);
		
		values.put("id", 15);
		values.put("date", new Date().getTime());
		long id2 = db.insert("Note", null, values);
		Assert.assertTrue("Note instance not created",id > 0);
		
		Dao<Integer, Note> dao = dbLite.getDao(Note.class);
		List<Note> notes = dao.findAll("date",OrderByType.ASC,null);
		Assert.assertTrue("Empty List<Note> Returned",!notes.isEmpty());
		Assert.assertEquals(id, notes.get(0).id);
		Assert.assertEquals(id2, notes.get(1).id);
	}
	
	
	@Test public void findAllBooleanWhereValueConvertedTest (){
		ContentValues values = new ContentValues();
		values.put("id", 11);
		values.put("isRecieved", true);
		
		long id = db.insert("Book", null, values);
		Assert.assertTrue("Book instance not created",id > 0);
		
		List<Book> books = dbLite.getDao(Book.class)
								 .findAll(null, null, "isRecieved=?", true);
		Assert.assertFalse(books.isEmpty());
	}
	
	@Test public void formatWhereParamsTest (){
		DaoImpl<Integer, Note> dao = new DaoImpl<Integer, Note>(dbLite.getEasyLiteOpenHelper(), Note.class);
		Date date = new Date ();
		String[] result = dao.formatWhereParams(true,false,"Mario",2.1,1,date,Long.parseLong("1"));
		Assert.assertEquals("1", result[0]); //return String representation of boolean true
		Assert.assertEquals("0", result[1]); //return String representation of boolean false
		Assert.assertEquals("Mario", result[2]);
		Assert.assertEquals("2.1", result[3]);
		Assert.assertEquals("1", result[4]);
		Assert.assertEquals(Long.toString(date.getTime()), result[5]);
		Assert.assertEquals("1", result[6]);
	}
	
	
	@After public void tearDown (){
		db.execSQL("DELETE FROM Note");
		db.execSQL("DELETE FROM Book");
		this.dbLite = null;
	}
}
