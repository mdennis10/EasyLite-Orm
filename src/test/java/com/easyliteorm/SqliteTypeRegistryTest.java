package com.easyliteorm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SqliteTypeRegistryTest {

	private SqliteTypeRegistry sqliteTypeRegistry;
	
	@Before
	public void setup (){
		sqliteTypeRegistry = new SqliteTypeRegistry();
	}
	
	
	@Test public void init_allBasicJavaTypesAreRegisterTest(){
		Assert.assertNotNull(sqliteTypeRegistry.resolve(String.class));
		Assert.assertEquals(SqliteType.TEXT.getValue(), sqliteTypeRegistry.resolve(String.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(char.class));
		Assert.assertEquals(SqliteType.TEXT.getValue(), sqliteTypeRegistry.resolve(char.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Character.class));
		Assert.assertEquals(SqliteType.TEXT.getValue(), sqliteTypeRegistry.resolve(Character.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(char.class));
		Assert.assertEquals(SqliteType.TEXT.getValue(), sqliteTypeRegistry.resolve(char.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(int.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(int.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Integer.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(Integer.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Boolean.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(Boolean.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(boolean.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(boolean.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(long.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(long.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Long.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(Long.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Date.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(Date.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(BigInteger.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(BigInteger.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(byte.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(byte.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Byte.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(Byte.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(short.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(short.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Short.class));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), sqliteTypeRegistry.resolve(Short.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(double.class));
		Assert.assertEquals(SqliteType.REAL.getValue(), sqliteTypeRegistry.resolve(double.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Double.class));
		Assert.assertEquals(SqliteType.REAL.getValue(), sqliteTypeRegistry.resolve(Double.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(float.class));
		Assert.assertEquals(SqliteType.REAL.getValue(), sqliteTypeRegistry.resolve(float.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Float.class));
		Assert.assertEquals(SqliteType.REAL.getValue(), sqliteTypeRegistry.resolve(Float.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(BigDecimal.class));
		Assert.assertEquals(SqliteType.REAL.getValue(), sqliteTypeRegistry.resolve(BigDecimal.class));
	}
	
	
	@Test public void getRegistryTest (){
		Assert.assertNotNull(sqliteTypeRegistry.getRegistry());
	}
	
	
	@Test public void registerTest (){
		sqliteTypeRegistry.register(String.class, SqliteType.TEXT);
		sqliteTypeRegistry.register(int.class, SqliteType.INTEGER);
		Map<String, String> registry = sqliteTypeRegistry.getRegistry();
		
		Assert.assertNotNull(registry);
		
		String key = String.class.getName();
		Assert.assertNotNull(registry.get(key));
		Assert.assertEquals(SqliteType.TEXT.getValue(), registry.get(key));
		
		key = int.class.getName();
		Assert.assertNotNull(registry.get(key));
		Assert.assertEquals(SqliteType.INTEGER.getValue(), registry.get(key));
	}
	
	
	@Test public void resolverTest (){
		Map<String, String> registry = sqliteTypeRegistry.getRegistry();
		registry.put(int.class.getName(),SqliteType.INTEGER.getValue());
		
		String result = sqliteTypeRegistry.resolve(int.class);
		Assert.assertNotNull(result);
		Assert.assertEquals(SqliteType.INTEGER.getValue(), result);
	}
	
	
	@Test public void isRegisteredTest (){
		Map<String, String> registry = sqliteTypeRegistry.getRegistry();
		registry.put(String.class.getName(), SqliteType.TEXT.getValue());
		
		Assert.assertTrue(sqliteTypeRegistry.isRegistered(String.class));
		
		registry.remove(int.class.getName());
		Assert.assertFalse(sqliteTypeRegistry.isRegistered(int.class));
	}
	
	@After
	public void tearDown(){
		this.sqliteTypeRegistry = null;
	}
}
