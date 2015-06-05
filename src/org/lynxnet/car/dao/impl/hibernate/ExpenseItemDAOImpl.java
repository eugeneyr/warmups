package org.lynxnet.car.dao.impl.hibernate;

import org.lynxnet.car.dao.ExpenseItemDAO;
import org.lynxnet.car.dao.DAOException;
import org.lynxnet.car.model.util.PageInfo;
import org.lynxnet.car.model.beans.ExpenseItem;
import org.lynxnet.car.model.beans.Expense;
import org.lynxnet.car.model.beans.ExpenseItemCategory;
import org.hibernate.Session;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 5/8/2006
 * Time: 18:16:34
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseItemDAOImpl extends GenericDAOImpl implements ExpenseItemDAO {
    private Session session;

    public ExpenseItemDAOImpl(Session session) {
        this.session = session;
    }

    public ExpenseItem find(long id) throws DAOException {
        return (ExpenseItem) session.load(ExpenseItem.class, id);
    }

    public List findByExpense(Expense expense, PageInfo pageInfo) throws DAOException {
        Query query = session.createQuery("from ExpenseItem where expense.id = :expense order by lineNo");
        query.setLong("expense", expense.getId());
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public List findByCategory(ExpenseItemCategory expItemCat, PageInfo pageInfo) throws DAOException {
        Query query = session.createQuery("from ExpenseItem where itemCategory.id = :category");
        query.setLong("category", expItemCat.getId());
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public List findByExpenseAndCategory(Expense expense, ExpenseItemCategory expItemCat, PageInfo pageInfo) throws DAOException {
        Query query = session.createQuery("from ExpenseItem where expense.id = :expense " +
                "and itemCategory.id = :category order by lineNo");
        query.setLong("expense", expense.getId());
        query.setLong("category", expItemCat.getId());
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public ExpenseItem store(ExpenseItem expItem) throws DAOException {
        return (ExpenseItem) super.store(expItem);
    }

    public void delete(long id) throws DAOException {
        super.delete("ExpenseItem", id);
    }

}
