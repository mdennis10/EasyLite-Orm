package com.easyliteorm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.easyliteorm.SqliteTypeResolver;

public class SqliteTypeResolverTest {
	
	@Test public void resolveString (){
		String actual = SqliteTypeResolver.resolver(String.class);
		Assert.assertEquals(SqliteTypeResolver.TEXT, actual);
	}
	
	@Test public void resolvBoolean (){
		String actual = SqliteTypeResolver.resolver(Boolean.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolvePrimitiveBoolean (){
		String actual  = SqliteTypeResolver.resolver(boolean.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolveDate (){
		String actual = SqliteTypeResolver.resolver(Date.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolveCharacter (){
		String actual = SqliteTypeResolver.resolver(Character.class);
		Assert.assertEquals(SqliteTypeResolver.TEXT, actual);
	}
	
	@Test public void resolvePrimitiveChar (){
		String actual = SqliteTypeResolver.resolver(char.class);
		Assert.assertEquals(SqliteTypeResolver.TEXT, actual);
	}
	
	@Test public void resolveDouble (){
		String actual = SqliteTypeResolver.resolver(Double.class);
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
	}
	
	@Test public void resolvePrimitiveDouble (){
		String actual = SqliteTypeResolver.resolver(double.class);
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
	}
	
	@Test public void resolveFloat (){
		String actual = SqliteTypeResolver.resolver(Float.class);
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
	}
	
	@Test public void resolvePrimitiveFloat (){
		String actual = SqliteTypeResolver.resolver(float.class);
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
	}
	
	@Test public void resolveInteger (){
		String actual = SqliteTypeResolver.resolver(Integer.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolvePrimitiveInt (){
		String actual = SqliteTypeResolver.resolver(int.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolveBigInteger(){
		String actual = SqliteTypeResolver.resolver(BigInteger.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolveBigDecimal(){
		String actual = SqliteTypeResolver.resolver(BigDecimal.class);
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
	}
	
	@Test public void resolveLong(){
		String actual = SqliteTypeResolver.resolver(Long.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolvePrimitiveLong(){
		String actual = SqliteTypeResolver.resolver(long.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolveByte(){
		String actual = SqliteTypeResolver.resolver(Byte.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolvePrimitiveByte(){
		String actual = SqliteTypeResolver.resolver(byte.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolvePrimitiveShort(){
		String actual = SqliteTypeResolver.resolver(short.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	@Test public void resolveShort(){
		String actual = SqliteTypeResolver.resolver(Short.class);
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
}
