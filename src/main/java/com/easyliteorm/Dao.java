package com.easyliteorm;

import com.easyliteorm.annotation.OrderByType;
import com.easyliteorm.exception.EasyLiteSqlException;

import java.util.List;

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
	 * @param entity instance to save.
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return the row ID of the newly inserted row, or -1 if an error occurred.A value greater than 0 if primary key is of type String
	 */
	long create(E entity) throws EasyLiteSqlException;


	/**
	 * Create new instance of record
	 * @author Mario Dennis
	 * @param entity instance to save.
	 * @param  listener to call once operation is complete
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return the row ID of the newly inserted row, or -1 if an error occurred.A value greater than 0 if primary key is of type String
	 */
	void createAsync(ResponseListener<Long> listener, E entity);
	
	/**
	 * Dispatch batch insert to database. [NOTE]
	 * Because this method is transactional if any
	 * insert operation fail all fails.
	 * @author Mario Dennis
	 * @param entities objects that should be saved
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return true when batch transactions succeeds, otherwise false
	 */
	boolean batchCreate(List<E> entities) throws EasyLiteSqlException;
	
	
	
	/**
	 * Dispatch batch insert. This method overwrites records 
	 * that already exist.
	 * @author Mario Dennis 
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @param entities objects that should be saved
	 */
	void batchCreateOverridable(List<E> entities) throws EasyLiteSqlException;
	
	
	/**
	 * Delete a record from database
	 * @author Mario Dennis
	 * @param entity to delete from database
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return the number of rows affected 
	 */
	int delete(E entity) throws EasyLiteSqlException;


	/**
	 * Delete a record from database
	 * @author Mario Dennis
	 * @param listener to call once operation is complete
	 * @param entity to delete from database
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return the number of rows affected
	 */
	void deleteAsync(ResponseListener<Integer> listener,E entity);
	
	
	/**
	 * Delete all records from database
	 * @author Mario Dennis
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return the number of rows affected.
	 */
    int deleteAll() throws EasyLiteSqlException;


	/**
	 * Delete all records from database
	 * @author Mario Dennis
	 * @param listener to call once operation is complete
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return the number of rows affected.
	 */

	void deleteAllAsync(ResponseListener<Integer> listener);
	
    
    /**
     * Delete records with give where condition.
     * @author Mario Dennis
     * @exception EasyLiteSqlException when error with sql parsing or execution occurs
     * @param whereClause the optional WHERE clause to apply when deleting. Passing null will delete all rows
     * @param whereArgs You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and instantiate a count pass "1" as the whereClause.
     */
	int deleteAll(String whereClause, Object... whereArgs) throws EasyLiteSqlException;
	
	
	/**
	 * Update database record
	 * @author Mario Dennis
	 * @param entity entity to update. This entity should contain corresponding primary key value of record in database
	 * @param whereClause the optional WHERE clause to apply when updating. Passing null will update all rows.
     * @param whereArgs You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings.
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return the number of rows affected
	 */
	int update(E entity, String whereClause, Object... whereArgs) throws EasyLiteSqlException;


	/**
	 * Update database record asynchronously
	 * @author Mario Dennis
	 * @param  listener to call once operation is complete
	 * @param entity entity to update. This entity should contain corresponding primary key value of record in database
	 * @param whereClause the optional WHERE clause to apply when updating. Passing null will update all rows.
	 * @param whereArgs You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings.
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return the number of rows affected
	 */
	void updateAsync(ResponseListener<Integer> listener, E entity, String whereClause, Object... whereArgs) throws EasyLiteSqlException;
	
	/**
	 * Find record by primary key
	 * @author Mario Dennis
	 * @param key - primary key value
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return E entity record
	 */
	E findById(K key) throws EasyLiteSqlException;
	
	
	/**
	 * Check if record exist
	 * @author Mario Dennis
	 * @param entity to check if exist. Must contain the primary key of corresponding record
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return true when entity exist, otherwise false
	 */
	boolean isExist(E entity) throws EasyLiteSqlException;
	
	/**
	 * Check if entity table contains any record
	 * @author Mario Dennis
	 * @return true when table is not empty
	 * @throws EasyLiteSqlException when error with sql parsing or execution occurs
	 */
	boolean isExist() throws EasyLiteSqlException;
	
	
	/**
	 * Find all records
	 * @author Mario Dennis
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return List of all records
	 */
	List<E> findAll() throws EasyLiteSqlException;
	
	
	/**
	 * Find all records
	 * @author Mario Dennis
	 * @param whereClause the optional WHERE clause to apply when deleting. Passing null will delete all rows
     * @param whereArgs You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings
	 * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
	 * @param orderByType specifies the order data will be returned based on orderBy column. Defaults to ASC when null.
	 * @exception EasyLiteSqlException when error with sql parsing or execution occurs
	 * @return List of all records
	 */
	List<E> findAll(String orderBy, OrderByType orderByType, String whereClause, Object... whereArgs) throws EasyLiteSqlException;
}
