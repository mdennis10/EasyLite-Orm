package com.easylite;

import java.io.File;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.easylite.model.NoIdEntity;
import com.easylite.model.TableDoesNotExist;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@RunWith(RobolectricTestRunner.class)
public class EasyLiteOpenHelperTest {
	private SQLiteDatabase db;
	private Activity context;
	private EasyLiteOpenHelper openHelper;
	private Set<Class<?>> entities;
	
	@Before public void setUp (){
		this.context = Robolectric.buildActivity(Activity.class).create().get();
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(ManifestUtil.getDatabaseName(context)),null);
		this.openHelper = new EasyLiteOpenHelper(context);
		
		entities = openHelper.getEntityClasses();
		entities.remove(TableDoesNotExist.class);
		entities.remove(NoIdEntity.class);	
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
			this.db.execSQL("DROP TABLE IF EXISTS " + Table.getTableName(clazz));
		
		this.db.close();
		this.db    = null;
	}
}
