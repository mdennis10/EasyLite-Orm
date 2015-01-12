package com.easylite;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.easylite.model.Note;

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
	
	@Test public void sameInstanceIsReturnedTest (){
		EasyLite ormDroid  = EasyLite.getInstance (context);
		EasyLite ormDroid2 = EasyLite.getInstance (context);
		
		Assert.assertTrue(ormDroid.equals(ormDroid2));
		Assert.assertEquals(ormDroid.hashCode(), ormDroid2.hashCode());
	}
	
	@Test public void initializePropertiesAreRetreivableTest (){		
		Assert.assertEquals(ManifestUtil.getDatabaseName(context), easyLite.dbName);
		Assert.assertEquals(ManifestUtil.getDatabaseVersion(context), easyLite.version);
	}
	
	@Test public void instanceOfDaoIsReturnTest (){
		Dao<Integer, Note> dao  = easyLite.getDao(Note.class);
		Assert.assertNotNull(dao);
	}
	
	@After public void tearDown (){
		this.context = null;
		this.db.close();
		this.db    = null;
	}
}
