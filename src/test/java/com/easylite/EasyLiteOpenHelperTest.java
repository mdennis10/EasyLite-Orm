package com.easylite;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.EasyLiteOpenHelper;
import com.easylite.model.Car;
import com.easylite.model.Note;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EasyLiteOpenHelperTest {
	private SQLiteDatabase db;
	private Set<Class<?>> entityClasses;
	private Activity activity;
	
	@Before public void setUp (){
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(FakeDbAttributes.dbName),null);
		this.activity = Robolectric.buildActivity(Activity.class).create().get();
		
		this.entityClasses = new HashSet<Class<?>>();
		entityClasses.add(Note.class);
		entityClasses.add(Car.class);
	}
	
	
	@Test public void onCreateTest (){
		EasyLiteOpenHelper openHelper = new EasyLiteOpenHelper(activity, FakeDbAttributes.dbName,
														   FakeDbAttributes.version,entityClasses);
		openHelper.onCreate(db);
		for (Class<?> clazz : entityClasses){
			String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
			Cursor cursor = db.rawQuery(sql, new String[]{clazz.getSimpleName()});
			Assert.assertTrue(cursor.moveToFirst());
		}
	}
	
	@Test public void onUpgradeTest (){
		EasyLiteOpenHelper openHelper = new EasyLiteOpenHelper(activity, FakeDbAttributes.dbName,
				   										   FakeDbAttributes.version,entityClasses);
		openHelper.onUpgrade(db, FakeDbAttributes.version, FakeDbAttributes.version + 1);
		for (Class<?> clazz : entityClasses){
			String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
			Cursor cursor = db.rawQuery(sql, new String[]{clazz.getSimpleName()});
			Assert.assertFalse(cursor.moveToFirst());
		}
	}
	
	@After public void tearDown (){
		for (Class<?> clazz : entityClasses)
			this.db.execSQL("DROP TABLE IF EXISTS " + clazz.getSimpleName());
		
		this.db.close();
		this.db    = null;
	}
}
