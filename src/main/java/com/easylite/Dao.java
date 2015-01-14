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
	public boolean batchCreate (List<E> entities) throws EasyLiteSqlException;
	
	
	
	/**
	 * Dispatch batch insert where rows are overwritten 
	 * where already exist.
	 * @author Mario Dennis
	 * @return number of elements inserted successfully 
	 */
	public int batchCreateOverridable (List<E> entities);
	
	
	/**
	 * Delete a record from database
	 * @author Mario Dennis
	 * @param entity to delete
	 * @exception EasyLiteSqlException
	 * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
	 */
	public int delete (E entity) throws EasyLiteSqlException;
	
	
	/**
	 * Delete all records from database
	 * @author Mario Dennis
	 * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
	 */
    public int deleteAll ();
	
    
    /**
     * Delete records where condition is true
     * @author Mario Dennis
     * @param whereClause
     * @param whereArgs
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
     */
	public int deleteAll(String whereClause, String... whereArgs);
	
	
	/**
	 * Update database record
	 * @author Mario Dennis
	 * @param entity
	 * @exception EasyLiteSqlException
	 * @return the number of rows affected
	 */
	public int update (E entity,String whereClause,String... whereArgs) throws EasyLiteSqlException;
	
	
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
	 * Find all records
	 * @author Mario Dennis
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 * @throws EasyLiteSqlException
	 */
	public List<E> findAll (String whereClause,String[] whereArgs) throws EasyLiteSqlException; 
	
	
	/**
	 * Get instance of SQLiteDatabase.
	 * @author Mario Dennis
	 * @return instance of SQLiteDatabase used by DAO
	 */
	public SQLiteDatabase getSqLiteDatabase ();
}
