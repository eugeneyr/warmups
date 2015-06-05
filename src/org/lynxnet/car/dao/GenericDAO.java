package org.lynxnet.car.dao;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 12/9/2006
 * Time: 22:21:11
 * To change this template use File | Settings | File Templates.
 */
public interface GenericDAO {
    void beginTransaction() throws DAOException;

    boolean isInTransaction() throws DAOException;

    void commit() throws DAOException;

    void rollback() throws DAOException;
  
}
