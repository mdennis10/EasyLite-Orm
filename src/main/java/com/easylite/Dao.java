package com.easylite;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.easylite.exception.EasyLiteSqlException;

/**
 * Data Access Object interface
 * @author Mario Dennis
 *
 * @param <K> primary key 
 * @param <E> database entity
 */
public interface Dao<K,E> {

	/**
	 * Create new instance of record
	 * @author Mario Dennis
	 * @param entity
	 * @exception EasyLiteSqlException
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long create (E entity) throws EasyLiteSqlException;
	
	
	/**
	 * Dispatch batch insert to database. [NOTE]
	 * Because this method is transactional if any
	 * insert operation fail all fails.
	 * @author Mario Dennis
	 * @param entities
	 * @return true when batch transactions succeeds, otherwise false
	 */
	public boolean batchCreate (List<E> entities);
	
	
	/**
	 * Delete record from database
	 * @author Mario Dennis
	 * @param entity
	 * @exception EasyLiteSqlException
	 * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
	 */
	public int delete (E entity) throws EasyLiteSqlException;
	
	
	/**
	 * Update database record
	 * @author Mario Dennis
	 * @param entity
	 * @exception EasyLiteSqlException
	 * @return the number of rows affected
	 */
	public int update (E entity,String whereClause,String[] whereArgs) throws EasyLiteSqlException;
	
	
	/**
	 * Find record by primary key
	 * @author Mario Dennis
	 * @param key - primary key
	 * @exception EasyLiteSqlException
	 * @return E record
	 */
	public E findById (K key) throws EasyLiteSqlException;
	
	
	/**
	 * Check if record exist
	 * @author Mario Dennis
	 * @param entity
	 * @exception EasyLiteSqlException
	 * @return
	 */
	public boolean isExist (E entity) throws EasyLiteSqlException;
	
	
	/**
	 * Find all records
	 * @author Mario Dennis
	 * @exception EasyLiteSqlException
	 * @return
	 */
	public List<E> findAll () throws EasyLiteSqlException;
	
	/**
	 * Get instance of SQLiteDatabase.
	 * @author Mario Dennis
	 * @return instance of SQLiteDatabase used by DAO
	 */
	public SQLiteDatabase getSqLiteDatabase ();
}
