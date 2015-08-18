package com.easyliteorm;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import java.lang.reflect.Field;

public class FieldInstantiator {

    private SqliteTypeRegistry typeRegistry;

    public FieldInstantiator(SqliteTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    /**
     * Gets data from cursor for SQLiteType.INTEGER
     * @param cursor
     * @param field
     * @return
     */
    protected long cursorInteger(Cursor cursor, Field field){
        int index = cursor.getColumnIndex(field.getName());
        try {
            return cursor.getLong(index);
        }catch (CursorIndexOutOfBoundsException e){
            Log.i("EasyLite", String.format("No value was found for Column %s",field.getName()));
            return 0;
        }
    }


    /**
     * Gets data from cursor for SQLiteType.TEXT
     * @param cursor
     * @param field
     * @return
     */
    public String cursorText (Cursor cursor, Field field){
        int index = cursor.getColumnIndex(field.getName());
        try {
            return  cursor.getString(index);
        } catch (CursorIndexOutOfBoundsException e) {
            Log.i("EasyLite", String.format("No value was found for Column %s", field.getName()));
            return null;
        }
    }

    /**
     *
     * @param cursor
     * @param field
     * @return
     */
    public double cursorReal (Cursor cursor, Field field){
        int index = cursor.getColumnIndex(field.getName());
        try {
            return cursor.getDouble(index);
        } catch (CursorIndexOutOfBoundsException e) {
            Log.i("EasyLite", String.format("No value was found for Column %s", field.getName()));
            return 0;
        }
    }
}
