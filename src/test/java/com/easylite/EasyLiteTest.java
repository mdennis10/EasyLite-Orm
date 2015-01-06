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
import android.database.sqlite.SQLiteDatabase;

import com.easylite.Dao;
import com.easylite.EasyLite;
import com.easylite.model.Car;
import com.easylite.model.Note;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EasyLiteTest {
	private Activity activity;
	private SQLiteDatabase db;

	@Before public void setUp (){
		this.activity = Robolectric.buildActivity(Activity.class).create().get();
		this.db = SQLiteDatabase.openOrCreateDatabase(new File(FakeDbAttributes.dbName),null);
		Set<Class<?>> entityClasses = new HashSet<Class<?>>();
		entityClasses.add(Note.class);
		entityClasses.add(Car.class);
		EasyLite.getInstance().initialize(activity, FakeDbAttributes.dbName, FakeDbAttributes.version,entityClasses);
	}
	
	@Test public void sameInstanceIsReturnedTest (){
		EasyLite ormDroid  = EasyLite.getInstance ();
		EasyLite ormDroid2 = EasyLite.getInstance();
		
		Assert.assertTrue(ormDroid.equals(ormDroid2));
		Assert.assertEquals(ormDroid.hashCode(), ormDroid2.hashCode());
	}
	
	@Test public void initializePropertiesAreRetreivableTest (){		
		Assert.assertEquals(FakeDbAttributes.dbName, EasyLite.getInstance().dbName);
		Assert.assertEquals(FakeDbAttributes.version, EasyLite.getInstance().version);
		Assert.assertEquals(activity, EasyLite.getInstance().context);
	}
	
	@Test public void instanceOfDaoIsReturnTest (){
		EasyLite dbLite = EasyLite.getInstance();
		Dao<Integer, Note> dao  = dbLite.getDao(Note.class);
		Assert.assertNotNull(dao);
	}
	
	@Test public void loadListOfEntityClass (){
		Assert.assertTrue(EasyLite.getInstance().entityClasses.size() > 0);
	}
	
	@After public void tearDown (){
		this.activity = null;
		this.db.close();
		this.db    = null;
	}
}
