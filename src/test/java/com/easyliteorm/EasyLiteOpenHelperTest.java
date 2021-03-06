package com.easyliteorm;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.Set;

@RunWith(RobolectricTestRunner.class)
public class EasyLiteOpenHelperTest {
	private SQLiteDatabase db;
	private Activity context;
	private EasyLiteOpenHelper openHelper;
	private Set<Class<?>> entities;
	private SQLiteTypeRegistry typeRegistry;
	
	@Before public void setUp (){
		this.context = Robolectric.buildActivity(Activity.class).create().get();
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(ManifestUtil.getDatabaseName(context)),null);
		this.typeRegistry = new SQLiteTypeRegistry();
		this.openHelper = new EasyLiteOpenHelper(context,typeRegistry);
		
		entities = openHelper.getEntityClasses();
	}
	
	@Test public void invalidEntityClassesAreRemove (){
		int size = entities.size();
		openHelper.onCreate(db);
		
		Assert.assertTrue(size > entities.size());
		Assert.assertFalse(entities.contains(NoIdEntity.class));
		Assert.assertFalse(entities.contains(NonNumeric.class));
	}
	
	@Test public void onCreateTest (){
		openHelper.onCreate(db);	
		for (Class<?> clazz : entities){
			String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
			Cursor cursor = db.rawQuery(sql, new String[]{clazz.getSimpleName()});
			boolean result = cursor.moveToFirst();
			Assert.assertTrue(result);
		}
	}
	
	
//	@Test public void onUpgradeTest (){
//		int version = ManifestUtil.getDatabaseVersion(context);
//		openHelper.onUpgrade(db, version, version + 1);
////		for (Class<?> clazz : e){
////			String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
////			Cursor cursor = db.rawQuery(sql, new String[]{clazz.getSimpleName()});
////			Assert.assertFalse(cursor.moveToFirst());
////		}
//	}
	
	@After public void tearDown (){
		for (Class<?> clazz : entities)
			this.db.execSQL("DROP TABLE IF EXISTS " + new Table(clazz, typeRegistry).getName());
		
		this.db.close();
		this.db    = null;
	}
}
