package org.lynxnet.car.dao.impl.hibernate;

import org.hibernate.Session;
import org.hibernate.Query;
import org.lynxnet.car.dao.DAOException;
import org.lynxnet.car.model.util.PageInfo;

import java.util.List;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 4/8/2006
 * Time: 22:23:24
 * To change this template use File | Settings | File Templates.
 */
public class GenericCategoryDAOImpl<T extends Serializable> extends GenericDAOImpl {
    Class<T> clazz;

    public GenericCategoryDAOImpl(Class<T> clazz, Session session) {
        this.session = session;
        this.clazz = clazz;
    }

    public T find(long id) throws DAOException {
        return (T) session.load(clazz, id);
    }

    public List findAll(PageInfo pageInfo) throws DAOException {
        Query query = session.createQuery("from " + clazz.getSimpleName());
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public T store(T category) throws DAOException {
        return (T) super.store(category);
    }

    public void delete(long id) throws DAOException {
        delete(clazz.getName(), id);
    }



}
