package org.lynxnet.car.dao.impl.hibernate;

import org.lynxnet.car.dao.ExpenseCategoryDAO;
import org.lynxnet.car.dao.DAOException;
import org.lynxnet.car.model.util.PageInfo;
import org.lynxnet.car.model.beans.ExpenseCategory;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 30/7/2006
 * Time: 21:25:54
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseCategoryDAOImpl extends GenericDAOImpl implements ExpenseCategoryDAO {

    public ExpenseCategoryDAOImpl(Session session) {
        this.session = session;
    }

    public ExpenseCategory find(long id) throws DAOException {
        return (ExpenseCategory) session.load(ExpenseCategory.class, id);
    }

    public List findAll(PageInfo pageInfo) throws DAOException {
        Query query = session.createQuery("from ExpenseCategory");
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public ExpenseCategory store(ExpenseCategory expCat) throws DAOException {
        return (ExpenseCategory) super.store(expCat);
    }

    public void delete(long id) throws DAOException {
        delete("ExpenseCategory", id);
    }

}
