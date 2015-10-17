package com.easyliteorm;


import android.content.Context;

public final class EasyLite {
	protected  final EasyLiteOpenHelper openHelper;
	private final SQLiteTypeRegistry typeRegistry;
	private final static EasyLite INSTANCE  = new EasyLite();

	private EasyLite() {
		this.typeRegistry = new SQLiteTypeRegistry();
		this.openHelper = new EasyLiteOpenHelper(EasyliteContext.getEasyliteContext(),typeRegistry);
	}


	/**
	 * Easylite now utilize an internal android context.
	 * Therefore, this method no longer needs a context argument.
	 * It is recommended that you use the following instead:
	 * <pre>
	 * {@code
	 * 		EasyLite easyLite = EasyLite.getInstance ();
	 * }
	 * <pre>
	 * @author Mario Dennis
	 * @param context android 
	 * @return singleton instance of EasyLite
	 */
	@Deprecated
	public static EasyLite getInstance(Context context) {
		return getInstance();
	}

	/**
	 * Gets singleton instance of Easylite class.
	 * @author Mario Dennis
	 * @return singleton instance of EasyLite
	 */
	public final static EasyLite getInstance (){
		return INSTANCE;
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
	
	
	protected final SQLiteTypeRegistry getSqlTypeRegistry (){
		return typeRegistry;
	}


	protected final void registerType(Class<String> clazz, SQLiteType sqliteType) {
		getSqlTypeRegistry().register(clazz, sqliteType);
	}
}
