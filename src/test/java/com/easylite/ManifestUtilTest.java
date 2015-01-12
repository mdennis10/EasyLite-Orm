package com.easylite;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.content.Context;

@RunWith(RobolectricTestRunner.class)
public class ManifestUtilTest {

	private Context context;
	
	@Before public void setUp (){
		this.context = Robolectric.buildActivity(Activity.class).get();
	}
	
	@Test public void getDatabaseVersionTest(){
		int actual = ManifestUtil.getDatabaseVersion(context);
		Assert.assertEquals(2, actual);
	}
	
	@Test public void getDatabaseNameTest (){
		String actual = ManifestUtil.getDatabaseName(context);
		Assert.assertEquals("app.db", actual);
	}
	
	@Test public void getModelPackageNameTest(){
		String actual = ManifestUtil.getModelPackageName(context);
		Assert.assertEquals("com.app.model", actual);
	}
	
	@After public void tearDown (){
		this.context = null;
	}
}
