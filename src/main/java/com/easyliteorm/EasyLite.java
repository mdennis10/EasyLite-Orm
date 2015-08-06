package com.easyliteorm;

import android.content.Context;

public final class EasyLite {
	private static EasyLite INSTANCE;
	protected final EasyLiteOpenHelper openHelper;
	private final SqliteTypeRegistry typeRegistry;
	
	private EasyLite(Context context) {
		this.typeRegistry = new SqliteTypeRegistry();
		this.openHelper = new EasyLiteOpenHelper(context,typeRegistry);
	}
	
	
	/**
	 * Gets Singleton instance of EasyLite
	 * @author Mario Dennis
	 * @param context android 
	 * @return singleton instance of EasyLite
	 */
	public synchronized static EasyLite getInstance(Context context) {
		if (INSTANCE == null){
			INSTANCE = new EasyLite(context);
		}
		return INSTANCE;
	}
	
	
	/**
	 * Gets Dao for database entity 
	 * @author Mario Dennis
	 * @param  type of DAO
	 * @return Dao instance
	 */
	public final <K,E> Dao<K, E> getDao (Class<E> type) {
		return new DaoImpl<K, E>(openHelper,type);
	}
	
	
	/**
	 * Gets EasyLiteOpenHelper instance being 
	 * utilized by easylite.
	 * @author Mario Dennis
	 * @return EasyLiteOpenHelper
	 */
	public EasyLiteOpenHelper getEasyLiteOpenHelper (){
		return openHelper;
	}
	

	/**
	 * Check if class type registered
	 * @param clazz
	 * @return true if registered, otherwise false
	 */
	public boolean isTypeRegistered(Class<String> clazz) {
		return getSqlTypeRegistry().isRegistered(clazz);
	}
	
	
	protected final SqliteTypeRegistry getSqlTypeRegistry (){
		return typeRegistry;
	}


	public void registerType(Class<String> clazz, SqliteType sqliteType) {
		getSqlTypeRegistry().register(clazz, sqliteType);
	}
}
