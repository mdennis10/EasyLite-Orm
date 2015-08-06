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
		Assert.assertEquals(SqliteType.TEXT, sqliteTypeRegistry.resolve(String.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(char.class));
		Assert.assertEquals(SqliteType.TEXT, sqliteTypeRegistry.resolve(char.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Character.class));
		Assert.assertEquals(SqliteType.TEXT, sqliteTypeRegistry.resolve(Character.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(char.class));
		Assert.assertEquals(SqliteType.TEXT, sqliteTypeRegistry.resolve(char.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(int.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(int.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Integer.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(Integer.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Boolean.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(Boolean.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(boolean.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(boolean.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(long.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(long.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Long.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(Long.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Date.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(Date.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(BigInteger.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(BigInteger.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(byte.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(byte.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Byte.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(Byte.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(short.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(short.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Short.class));
		Assert.assertEquals(SqliteType.INTEGER, sqliteTypeRegistry.resolve(Short.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(double.class));
		Assert.assertEquals(SqliteType.REAL, sqliteTypeRegistry.resolve(double.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Double.class));
		Assert.assertEquals(SqliteType.REAL, sqliteTypeRegistry.resolve(Double.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(float.class));
		Assert.assertEquals(SqliteType.REAL, sqliteTypeRegistry.resolve(float.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Float.class));
		Assert.assertEquals(SqliteType.REAL, sqliteTypeRegistry.resolve(Float.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(BigDecimal.class));
		Assert.assertEquals(SqliteType.REAL, sqliteTypeRegistry.resolve(BigDecimal.class));
	}
	
	
	@Test public void getRegistryTest (){
		Assert.assertNotNull(sqliteTypeRegistry.getRegistry());
	}
	
	
	@Test public void registerTest (){
		sqliteTypeRegistry.register(String.class, SqliteType.TEXT);
		sqliteTypeRegistry.register(int.class, SqliteType.INTEGER);
		Map<String, SqliteType> registry = sqliteTypeRegistry.getRegistry();
		
		Assert.assertNotNull(registry);
		
		String key = String.class.getName();
		Assert.assertNotNull(registry.get(key));
		Assert.assertEquals(SqliteType.TEXT, registry.get(key));
		
		key = int.class.getName();
		Assert.assertNotNull(registry.get(key));
		Assert.assertEquals(SqliteType.INTEGER, registry.get(key));
	}
	
	@Test public void resolve_returnNullWhenTypeIsNotRegistered (){
		Map<String, SqliteType> registry = sqliteTypeRegistry.getRegistry();
		registry.remove(String.class.getName());
		
		Assert.assertNull(sqliteTypeRegistry.resolve(String.class));
	}
	
	
	@Test public void resolverTest (){
		Map<String, SqliteType> registry = sqliteTypeRegistry.getRegistry();
		registry.put(int.class.getName(),SqliteType.INTEGER);
		
		SqliteType result = sqliteTypeRegistry.resolve(int.class);
		Assert.assertNotNull(result);
		Assert.assertEquals(SqliteType.INTEGER, result);
	}
	
	
	@Test public void isRegisteredTest (){
		Map<String, SqliteType> registry = sqliteTypeRegistry.getRegistry();
		registry.put(String.class.getName(), SqliteType.TEXT);
		
		Assert.assertTrue(sqliteTypeRegistry.isRegistered(String.class));
		
		registry.remove(int.class.getName());
		Assert.assertFalse(sqliteTypeRegistry.isRegistered(int.class));
	}
	
	@After
	public void tearDown(){
		this.sqliteTypeRegistry = null;
	}
}
