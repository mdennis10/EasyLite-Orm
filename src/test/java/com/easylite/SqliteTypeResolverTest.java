package com.easylite;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.easylite.SqliteTypeResolver;

public class SqliteTypeResolverTest {
	
	@Test public void resolveString (){
		String actual = SqliteTypeResolver.resolver(String.class.getName());
		Assert.assertEquals(SqliteTypeResolver.TEXT, actual);
	}
	
	@Test public void resolvBoolean (){
		String actual = SqliteTypeResolver.resolver(Boolean.class.getName());
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolveDate (){
		String actual = SqliteTypeResolver.resolver(Date.class.getName());
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolveChar (){
		String actual = SqliteTypeResolver.resolver(Character.class.getName());
		Assert.assertEquals(SqliteTypeResolver.TEXT, actual);
	}
	
	@Test public void resolveDouble (){
		String actual = SqliteTypeResolver.resolver(Double.class.getName());
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
	}
	
	@Test public void resolveFloat (){
		String actual = SqliteTypeResolver.resolver(Float.class.getName());
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
	}
	
	@Test public void resolveInteger (){
		String actual = SqliteTypeResolver.resolver(Integer.class.getName());
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolveBigInteger(){
		String actual = SqliteTypeResolver.resolver(BigInteger.class.getName());
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
	}
	
	@Test public void resolveBigDecimal(){
		String actual = SqliteTypeResolver.resolver(BigDecimal.class.getName());
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
	}
	
	@Test public void resolvePrimitives (){
		String actual = SqliteTypeResolver.resolver("int");
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
		
		actual = SqliteTypeResolver.resolver("boolean");
		Assert.assertEquals(SqliteTypeResolver.INTEGER, actual);
		
		actual = SqliteTypeResolver.resolver("char");
		Assert.assertEquals(SqliteTypeResolver.TEXT, actual);
		
		actual = SqliteTypeResolver.resolver("double");
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
		
		actual = SqliteTypeResolver.resolver("float");
		Assert.assertEquals(SqliteTypeResolver.REAL, actual);
	}
}
