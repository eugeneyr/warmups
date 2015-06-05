package org.lynxnet.car.dao.impl.hibernate;

import org.lynxnet.car.dao.DAOException;
import org.lynxnet.car.dao.GenericDAO;
import org.lynxnet.car.model.util.PageInfo;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.Transaction;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 30/7/2006
 * Time: 21:22:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class GenericDAOImpl implements GenericDAO {
    protected Session session;

    protected Transaction transaction;

    public Object store(Object entity) throws DAOException {
        session.saveOrUpdate(entity);
        return entity;
    }

    public void delete(String entityName, long id) throws DAOException {
        session.delete(entityName, id);
    }

    protected void applyPageInfo(Query query, PageInfo pageInfo) {
        if (pageInfo != null) {
            query.setFirstResult(pageInfo.getPageNumber() *  pageInfo.getResultsPerPage());
            query.setMaxResults(pageInfo.getResultsPerPage());
        }
    }

    public void beginTransaction() throws DAOException {
        if (transaction != null) {
            if (transaction.isActive()) {
                transaction.commit();
            }
            transaction = null;
        }
        transaction = session.getTransaction();
        transaction.begin();
    }

    public boolean isInTransaction() throws DAOException {
        return (transaction != null) && (transaction.isActive());
    }

    public void commit() throws DAOException {
        if (isInTransaction()) {
            transaction.commit();
            transaction = null;
        }
    }

    public void rollback() throws DAOException {
        if (isInTransaction()) {
            transaction.rollback();
            transaction = null;
        }
    }
}
