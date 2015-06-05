package org.lynxnet.car.dao.impl.hibernate;

import org.hibernate.Session;
import org.hibernate.Query;
import org.lynxnet.car.dao.DAOException;
import org.lynxnet.car.model.util.PageInfo;
import org.lynxnet.car.dao.ExpenseItemCategoryDAO;
import org.lynxnet.car.model.beans.ExpenseItemCategory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 4/8/2006
 * Time: 22:22:06
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseItemCategoryDAOImpl extends GenericDAOImpl implements ExpenseItemCategoryDAO {
    public ExpenseItemCategoryDAOImpl(Session session) {
        this.session = session;
    }

    public ExpenseItemCategory find(long id) throws DAOException {
        return (ExpenseItemCategory) session.load(ExpenseItemCategory.class, id);
    }

    public List findAll(PageInfo pageInfo) throws DAOException {
        Query query = session.createQuery("from ExpenseItemCategory");
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public ExpenseItemCategory store(ExpenseItemCategory expCat) throws DAOException {
        return (ExpenseItemCategory) super.store(expCat);
    }

    public void delete(long id) throws DAOException {
        delete("ExpenseCategory", id);
    }

}
