package com.easylite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.model.Car;
import com.easylite.model.Note;

@RunWith(RobolectricTestRunner.class)
public class EasyLiteOpenHelperTest {
	private SQLiteDatabase db;
	private Activity context;
	List<Class<?>> entityClasses;
	@Before public void setUp (){
		this.context = Robolectric.buildActivity(Activity.class).create().get();
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(ManifestUtil.getDatabaseName(context)),null);
		
		entityClasses = new ArrayList<Class<?>>();
		entityClasses.add(Note.class);
		entityClasses.add(Car.class);
	}
	
	
	@Test public void onCreateTest (){
		EasyLiteOpenHelper openHelper = new EasyLiteOpenHelper(context, ManifestUtil.getDatabaseName(context),
														       ManifestUtil.getDatabaseVersion(context));
		openHelper.onCreate(db);
		for (Class<?> clazz : entityClasses){
			String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
			Cursor cursor = db.rawQuery(sql, new String[]{clazz.getSimpleName()});
			Assert.assertTrue(cursor.moveToFirst());
		}
	}
	
	@Test public void onUpgradeTest (){
		EasyLiteOpenHelper openHelper = new EasyLiteOpenHelper(context, ManifestUtil.getDatabaseName(context),
			       											   ManifestUtil.getDatabaseVersion(context));
		int version = ManifestUtil.getDatabaseVersion(context);
		openHelper.onUpgrade(db, version, version + 1);
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
