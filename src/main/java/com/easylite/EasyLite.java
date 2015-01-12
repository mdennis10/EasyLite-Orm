package com.easylite;

import android.content.Context;

public final class EasyLite {
	private static EasyLite INSTANCE;
	protected final EasyLiteOpenHelper openHelper;
	
	private EasyLite(Context context) {
		this.openHelper = new EasyLiteOpenHelper(context,ManifestUtil.getDatabaseName(context),ManifestUtil.getDatabaseVersion(context));
	}
	
	
	/**
	 * Gets Singleton instance of EasyLite
	 * @author Mario Dennis
	 * @return OrmDroid
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
	 * @param clazz
	 * @return Dao<K, E>
	 */
	public final <K,E> Dao<K, E> getDao (Class<E> type) {
		return new DaoImpl<K, E>(openHelper,type);
	}
}
