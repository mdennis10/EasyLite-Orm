package com.easyliteorm;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.easyliteorm.model.Note;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;

@RunWith(RobolectricTestRunner.class)
public class FieldInstantiatorTest {
    private SQLiteTypeRegistry typeRegistry;
    private FieldInstantiator provider;
    private EasyLite dbLite;
    private SQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        this.typeRegistry = new SQLiteTypeRegistry();
        this.provider     = new FieldInstantiator(typeRegistry);

        Activity context = Robolectric.buildActivity(Activity.class).create().get();
        this.dbLite = new EasyLite(context);
        db = ((DaoImpl<Object, Note>) dbLite.getDao(Note.class)).getSqLiteDatabase();
    }


    @Test public void cursorInteger_returnAppropiateNumericValueAndTypeTest () throws NoSuchFieldException{
        Cursor cursor = Mockito.mock(Cursor.class);
        Mockito.when(cursor.getColumnIndex("idInt"))
                .thenReturn(1);

        Mockito.when(cursor.getColumnIndex("idLong"))
                .thenReturn(1);

        Mockito.when(cursor.getColumnIndex("idLONG"))
                .thenReturn(1);
        Mockito.when(cursor.getColumnIndex("idINTEGER"))
                .thenReturn(1);
        Mockito.when(cursor.getLong(1))
                .thenReturn((long) 2);

        Assert.assertEquals(2, (int) provider.cursorInteger(cursor, MyEntity.class.getDeclaredField("idInt")));
        Assert.assertEquals((long) 2, provider.cursorInteger(cursor, MyEntity.class.getDeclaredField("idLong")));
        Assert.assertEquals(2, provider.cursorInteger(cursor, MyEntity.class.getDeclaredField("idLONG")));
        Assert.assertEquals(2, provider.cursorInteger(cursor, MyEntity.class.getDeclaredField("idINTEGER")));

        // Assert that 0 is returned when no value found
        Mockito.when(cursor.getColumnIndex("idLONG"))
                .thenReturn(2);

        Assert.assertEquals(0, provider.cursorInteger(cursor, MyEntity.class.getDeclaredField("idLONG")));
    }

    @Test
    public void cursorIntegerTest () throws NoSuchFieldException {
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("body", "text");
        values.put("date", new Date().getTime());

        long id = db.insert("Note", null, values);
        Assert.assertEquals(1, id);

        Cursor cursor = db.query("Note", null, null, null, null, null, null, null);
        cursor.moveToFirst();
        int result = (int) provider.cursorInteger(cursor, Note.class.getDeclaredField("id"));
        Assert.assertEquals(1, result);
    }

    @Test public void cursorText_returnAppropiateValueTest () throws NoSuchFieldException {
        Cursor cursor = Mockito.mock(Cursor.class);
        Mockito.when(cursor.getColumnIndex("name"))
                .thenReturn(1);

        String actual = "value";
        Mockito.when(cursor.getString(1))
                .thenReturn(actual);

        Assert.assertNotNull(provider.cursorText(cursor, MyEntity.class.getDeclaredField("name")));
        Assert.assertEquals(actual, provider.cursorText(cursor, MyEntity.class.getDeclaredField("name")));

        Mockito.when(cursor.getString(1))
                .thenReturn(null);
        Assert.assertNull(provider.cursorText(cursor, MyEntity.class.getDeclaredField("name")));
    }

    @Test
    public void cursorTextTest () throws NoSuchFieldException {
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("body", "text");
        values.put("date", new Date().getTime());

        long id = db.insert("Note", null, values);
        Assert.assertEquals(1, id);

        Cursor cursor = db.query("Note", null, null, null, null, null, null, null);
        cursor.moveToFirst();
        String result =  provider.cursorText(cursor, Note.class.getDeclaredField("body"));
        Assert.assertEquals("text", result);
    }


    @Test public void cursorReal_returnAppropiateValueTest () throws NoSuchFieldException {
        Cursor cursor = Mockito.mock(Cursor.class);
        Mockito.when(cursor.getColumnIndex("floatingPoint"))
                .thenReturn(1);
        Mockito.when(cursor.getColumnIndex("floatingPoint1"))
                .thenReturn(1);
        Mockito.when(cursor.getColumnIndex("floatingPoint2"))
                .thenReturn(1);
        Mockito.when(cursor.getColumnIndex("floatingPoint3"))
                .thenReturn(1);

        double actual = 1.56;
        Mockito.when(cursor.getDouble(1))
                .thenReturn(actual);

        Assert.assertEquals(actual, provider.cursorReal(cursor, MyEntity.class.getDeclaredField("floatingPoint")), 0.01);
        Assert.assertEquals((float)actual, provider.cursorReal(cursor, MyEntity.class.getDeclaredField("floatingPoint1")),0.01);
        Assert.assertEquals(new Double(actual), provider.cursorReal(cursor, MyEntity.class.getDeclaredField("floatingPoint2")),0.01);
        Assert.assertEquals(new Float(actual), provider.cursorReal(cursor, MyEntity.class.getDeclaredField("floatingPoint3")), 0.01);

        // Assert that 0 is returned when no value found
        Mockito.when(cursor.getColumnIndex("floatingPoint"))
                .thenReturn(6);

        Assert.assertEquals(0, provider.cursorReal(cursor, MyEntity.class.getDeclaredField("floatingPoint")), 0.01);
    }


    @Test
    public void cursorRealTest () throws NoSuchFieldException {
        double actual = 1.56;
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("price", actual);

        long id = db.insert("Note", null, values);
        Assert.assertEquals(1, id);

        Cursor cursor = db.query("Note", null, null, null, null, null, null, null);
        cursor.moveToFirst();
        double result =  provider.cursorReal(cursor, Note.class.getDeclaredField("price"));
        Assert.assertEquals(actual, result,0.01);
    }

    private class MyEntity{
        public long idLong;
        public int idInt;
        public Long idLONG;
        public Integer idINTEGER;
        public String name;
        public double floatingPoint;
        public float floatingPoint1;
        public Double floatingPoint2;
        public Float floatingPoint3;
    }

    @After
    public void tearDown() throws Exception {
        this.typeRegistry = null;
        this.provider     = null;
        db.execSQL("DELETE FROM Note");
        dbLite.getEasyLiteOpenHelper().close();
        this.dbLite = null;
    }
}