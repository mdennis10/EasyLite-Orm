package com.easylite;

import java.util.Set;

import android.content.Context;

public final class EasyLite {
	protected Set<Class<?>> entityClasses;
	private static final EasyLite INSTANCE = new EasyLite();
	
	protected Context context;
	protected int version;
	protected String dbName; 
	protected EasyLiteOpenHelper openHelper;

	private EasyLite() {}

	/**
	 * Initialize EasyLite
	 * @author Mario Dennis
	 * @param context - android context
	 * @param dbName  - database name
	 * @param version - database version
	 */
	public final void initialize (Context context,String dbName,int version,Set<Class<?>> entityClasses){
		this.context = context;
		this.dbName  = dbName;
		this.version = version;
		this.entityClasses = entityClasses;
		this.openHelper = new EasyLiteOpenHelper(context, dbName, version,entityClasses);
	}
	

	/**
	 * Gets Singleton instance of EasyLite
	 * @author Mario Dennis
	 * @return OrmDroid
	 */
	public static EasyLite getInstance() {
		return INSTANCE;
	}
	
	
	/**
	 * Gets Dao for database entity 
	 * @author Mario Dennis
	 * @param clazz
	 * @return Dao<K, E>
	 */
	public final <K,E> Dao<K, E> getDao (Class<E> type) {
		return new DaoImpl<K, E>(openHelper,type);
	}
}
