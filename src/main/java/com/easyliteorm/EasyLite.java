package com.easyliteorm;

import android.content.Context;

public final class EasyLite {
	protected final EasyLiteOpenHelper openHelper;
	private final SqliteTypeRegistry typeRegistry;
	
	public EasyLite(Context context) {
		this.typeRegistry = new SqliteTypeRegistry();
		this.openHelper = new EasyLiteOpenHelper(context,typeRegistry);
	}
	
	
	/**
	 * This method does not return a singleton instance
	 * and will be removed from future releases. Instead
	 * it returns a new instance of EasyLite.
	 * <pre>
	 * {@code
	 * 		EasyLite easyLite = new EasyLite(context);
	 * }
	 * <pre>
	 * @author Mario Dennis
	 * @param context android 
	 * @return singleton instance of EasyLite
	 */
	@Deprecated
	public static EasyLite getInstance(Context context) {
		return new EasyLite(context);
	}
	
	
	/**
	 * Gets Dao for database entity 
	 * @author Mario Dennis
	 * @param  type of DAO
	 * @return Dao instance
	 */
	public final <K,E> Dao<K, E> getDao (Class<E> type) {
		return new DaoImpl<K, E>(openHelper,type,typeRegistry);
	}
	
	
	/**
	 * Gets EasyLiteOpenHelper instance being 
	 * utilized by easylite.
	 * @author Mario Dennis
	 * @return EasyLiteOpenHelper
	 */
	protected final EasyLiteOpenHelper getEasyLiteOpenHelper (){
		return openHelper;
	}
	

	/**
	 * Check if class type registered
	 * @param clazz
	 * @return true if registered, otherwise false
	 */
	public final boolean isTypeRegistered(Class<String> clazz) {
		return getSqlTypeRegistry().isRegistered(clazz);
	}
	
	
	protected final SqliteTypeRegistry getSqlTypeRegistry (){
		return typeRegistry;
	}


	public final void registerType(Class<String> clazz, SqliteType sqliteType) {
		getSqlTypeRegistry().register(clazz, sqliteType);
	}
}
