package com.easylite;

import android.content.Context;

public final class EasyLite {
	private static EasyLite INSTANCE;
	
	protected final int version;
	protected final String dbName; 
	protected final EasyLiteOpenHelper openHelper;
	
	private EasyLite(Context context) {
		this.dbName  = ManifestUtil.getDatabaseName(context);
		this.version = ManifestUtil.getDatabaseVersion(context);
		this.openHelper = new EasyLiteOpenHelper(context, this.dbName, this.version);
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
