package com.easyliteorm;

import android.content.Context;

public final class EasyLite {
	private static EasyLite INSTANCE;
	protected final EasyLiteOpenHelper openHelper;
	
	private EasyLite(Context context) {
		this.openHelper = new EasyLiteOpenHelper(context);
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
}
