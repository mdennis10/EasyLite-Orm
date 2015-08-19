package com.easyliteorm;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

public class SQLiteTypeRegistryTest {

	private SQLiteTypeRegistry sqliteTypeRegistry;
	
	@Before
	public void setup (){
		sqliteTypeRegistry = new SQLiteTypeRegistry();
	}
	
	
	@Test public void init_allBasicJavaTypesAreRegisterTest(){
		Assert.assertNotNull(sqliteTypeRegistry.resolve(String.class));
		Assert.assertEquals(SQLiteType.TEXT, sqliteTypeRegistry.resolve(String.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(char.class));
		Assert.assertEquals(SQLiteType.TEXT, sqliteTypeRegistry.resolve(char.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Character.class));
		Assert.assertEquals(SQLiteType.TEXT, sqliteTypeRegistry.resolve(Character.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(char.class));
		Assert.assertEquals(SQLiteType.TEXT, sqliteTypeRegistry.resolve(char.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(int.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(int.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Integer.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(Integer.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Boolean.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(Boolean.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(boolean.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(boolean.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(long.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(long.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Long.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(Long.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Date.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(Date.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(BigInteger.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(BigInteger.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(byte.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(byte.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Byte.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(Byte.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(short.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(short.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Short.class));
		Assert.assertEquals(SQLiteType.INTEGER, sqliteTypeRegistry.resolve(Short.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(double.class));
		Assert.assertEquals(SQLiteType.REAL, sqliteTypeRegistry.resolve(double.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Double.class));
		Assert.assertEquals(SQLiteType.REAL, sqliteTypeRegistry.resolve(Double.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(float.class));
		Assert.assertEquals(SQLiteType.REAL, sqliteTypeRegistry.resolve(float.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(Float.class));
		Assert.assertEquals(SQLiteType.REAL, sqliteTypeRegistry.resolve(Float.class));
		
		Assert.assertNotNull(sqliteTypeRegistry.resolve(BigDecimal.class));
		Assert.assertEquals(SQLiteType.REAL, sqliteTypeRegistry.resolve(BigDecimal.class));
	}
	
	
	@Test public void getRegistryTest (){
		Assert.assertNotNull(sqliteTypeRegistry.getRegistry());
	}
	
	
	@Test public void registerTest (){
		sqliteTypeRegistry.register(String.class, SQLiteType.TEXT);
		sqliteTypeRegistry.register(int.class, SQLiteType.INTEGER);
		Map<String, RegisteredType<?>> registry = sqliteTypeRegistry.getRegistry();
		
		Assert.assertNotNull(registry);
		
		String key = String.class.getName();
		Assert.assertNotNull(registry.get(key));
		Assert.assertEquals(SQLiteType.TEXT, registry.get(key).getSqliteType());
		
		key = int.class.getName();
		Assert.assertNotNull(registry.get(key));
		Assert.assertEquals(SQLiteType.INTEGER, registry.get(key).getSqliteType());
	}
	
	@Test public void resolve_returnNullWhenTypeIsNotRegistered (){
		Map<String, RegisteredType<?>> registry = sqliteTypeRegistry.getRegistry();
		registry.remove(String.class.getName());
		
		Assert.assertNull(sqliteTypeRegistry.resolve(String.class));
	}
	
	
	@Test public void resolverTest (){
		Map<String, RegisteredType<?>> registry = sqliteTypeRegistry.getRegistry();
		RegisteredType<?> registeredType = new RegisteredType<Integer>();
		registeredType.setSqliteType(SQLiteType.INTEGER);
		registeredType.setClazz(int.class);
		registry.put(int.class.getName(), registeredType);
		
		SQLiteType result = sqliteTypeRegistry.resolve(int.class);
		Assert.assertNotNull(result);
		Assert.assertEquals(SQLiteType.INTEGER, result);
	}
	
	
	@Test public void isRegisteredTest (){
		Map<String, RegisteredType<?>> registry = sqliteTypeRegistry.getRegistry();
		RegisteredType<?> registeredType = new RegisteredType<Integer>();
		registeredType.setClazz(String.class);
		registeredType.setSqliteType(SQLiteType.TEXT);
		registry.put(String.class.getName(), registeredType);
		
		Assert.assertTrue(sqliteTypeRegistry.isRegistered(String.class));
		
		registry.remove(int.class.getName());
		Assert.assertFalse(sqliteTypeRegistry.isRegistered(int.class));
	}
	
	@After
	public void tearDown(){
		this.sqliteTypeRegistry = null;
	}
}
