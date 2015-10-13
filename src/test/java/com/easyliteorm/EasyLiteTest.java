package com.easyliteorm;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import com.easyliteorm.model.Note;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.File;

@RunWith(RobolectricTestRunner.class)
public class EasyLiteTest {
	private Activity context;
	private SQLiteDatabase db;
	private EasyLite easyLite;
	
	@Before public void setUp (){
		this.context = Robolectric.buildActivity(Activity.class).create().get();
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(ManifestUtil.getDatabaseName(context)),null);
		this.easyLite = EasyLite.getInstance(context);
	}
	
	
	@Test public void instanceOfDaoIsReturnTest (){
		Dao<Integer, Note> dao  = easyLite.getDao(Note.class);
		Assert.assertNotNull(dao);
	}
	
	@Test public void isTypeRegisteredTest (){
		boolean result = easyLite.isTypeRegistered(String.class);
		Assert.assertTrue(result);
		
		easyLite.getSqlTypeRegistry()
			    .getRegistry()
			    .remove(String.class.getName());
		
		result = easyLite.isTypeRegistered(String.class);
		Assert.assertFalse(result);
	}
	
	
	@Test public void registerTypeTest (){
		easyLite.registerType(String.class, SQLiteType.REAL);
		SQLiteType result = easyLite.getSqlTypeRegistry().resolve(String.class);
		Assert.assertEquals(SQLiteType.REAL, result);
	}
	
	@After public void tearDown (){
		this.context = null;
		this.db.close();
		this.db    = null;
		this.easyLite = null;
	}
}
