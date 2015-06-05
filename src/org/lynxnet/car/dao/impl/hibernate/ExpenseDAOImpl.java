package org.lynxnet.car.dao.impl.hibernate;

import org.lynxnet.car.dao.ExpenseDAO;
import org.lynxnet.car.dao.DAOException;
import org.lynxnet.car.model.util.PageInfo;
import org.lynxnet.car.model.beans.Expense;
import org.lynxnet.car.model.beans.ExpenseCategory;
import org.lynxnet.car.model.filters.ExpensesFilter;
import org.hibernate.Session;
import org.hibernate.Query;

import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 30/7/2006
 * Time: 21:32:06
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseDAOImpl extends GenericDAOImpl implements ExpenseDAO {

    public ExpenseDAOImpl(Session session) {
        this.session = session;
    }

    public Expense find(long id) throws DAOException {
        return (Expense) session.load(Expense.class, id);
    }

    public List findAll(PageInfo pageInfo) throws DAOException {
        Query query = session.createQuery("from Expense");
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public List findByCategory(ExpenseCategory expCat, PageInfo pageInfo) throws DAOException {
        Query query = session.createQuery("from Expense where category.id = :category");
        query.setLong("category", expCat.getId());
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public List findByDateInterval(Timestamp dateFrom, Timestamp dateTo, PageInfo pageInfo) throws DAOException {
        StringBuilder queryStr = new StringBuilder("from Expense ");
        Query query = proceedDateIntervalQuery(dateFrom, dateTo, queryStr);
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    private boolean buildDateCondition(Date dateFrom, Date dateTo, StringBuilder queryStr, boolean appendWhere) {
        int length = queryStr.length();
        if (dateFrom != null || dateTo != null) {
            if (appendWhere) {
                queryStr.append("where ");
            }
            if (dateFrom != null) {
                queryStr.append("(dateTime >= :dateFrom) ");
            }
            if (dateTo != null) {
                if (dateFrom != null) {
                    queryStr.append(" and ");
                }
                queryStr.append("(dateTime <= :dateTo) ");
            }
        }
        return length != queryStr.length();
    }

    public List findByCategoryAndDateInterval(ExpenseCategory expCat, Timestamp dateFrom, Timestamp dateTo, PageInfo pageInfo) throws DAOException {
        StringBuilder queryStr = new StringBuilder("from Expense where category.id = :category");
        buildDateCondition(dateFrom, dateTo, queryStr, false);
        Query query = session.createQuery(queryStr.toString());
        query.setLong("category", expCat.getId());
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    private void buildOrCondition(long[] ids, StringBuilder queryStr, String columnName, boolean prependWithAND) {
        assert(columnName != null && columnName.length() > 0);
        int length = queryStr.length();
        if (ids != null && ids.length > 0) {
            if (prependWithAND) {
                queryStr.append(" and ");
            }
            queryStr.append("(");
            for (long carId: ids) {
                if (queryStr.length() > length + 1) {
                    queryStr.append(" or ");
                }
                queryStr.append("(");
                queryStr.append(columnName);
                queryStr.append(" = ");
                queryStr.append(carId);
                queryStr.append(")");
            }
            queryStr.append(")");
        }
    }

    public List findByFilter(ExpensesFilter filter, PageInfo pageInfo) throws DAOException {
        StringBuilder queryStr = new StringBuilder("from Expense ");
        Query query = proceedFilterQuery(filter, queryStr);
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public Expense store(Expense expense) throws DAOException {
        return (Expense) super.store(expense);
    }

    public void delete(long id) throws DAOException {
        delete("Expense", id);
    }

    public int countAll() throws DAOException {
        Query query = session.createQuery("select COUNT(id) as cnt from Expense");
        return (Integer)query.uniqueResult();
    }

    public int countByCategory(ExpenseCategory expCat) throws DAOException {
        Query query = session.createQuery("select COUNT(id) as cnt from Expense where category.id = :category");
        query.setLong("category", expCat.getId());
        return (Integer)query.uniqueResult();
    }

    public int countByDateInterval(Timestamp dateFrom, Timestamp dateTo) throws DAOException {
        StringBuilder queryStr = new StringBuilder("select COUNT(id) from Expense ");
        Query query = proceedDateIntervalQuery(dateFrom, dateTo, queryStr);
        return (Integer)query.uniqueResult();
    }

    private Query proceedDateIntervalQuery(Timestamp dateFrom, Timestamp dateTo, StringBuilder queryStr) {
        buildDateCondition(dateFrom, dateTo, queryStr, true);
        Query query = session.createQuery(queryStr.toString());
        query.setTimestamp("dateFrom", dateFrom);
        query.setTimestamp("dateTo", dateTo);
        return query;
    }

    public int countByFilter(ExpensesFilter filter) throws DAOException {
        StringBuilder queryStr = new StringBuilder("select COUNT(id) from Expense ");
        Query query = proceedFilterQuery(filter, queryStr);
        return (Integer)query.uniqueResult();
    }

    private Query proceedFilterQuery(ExpensesFilter filter, StringBuilder queryStr) {
        if (!filter.isEmpty()) {
            queryStr.append("where (1 = 1) ");
            StringBuilder builder = new StringBuilder();
            if (buildDateCondition(
                    filter.getFrom(),
                    filter.getTo(),
                    builder, false)) {
                queryStr.append(" and ");
                queryStr.append(builder);
            }
            builder.setLength(0);
            buildOrCondition(filter.getCar(), queryStr, "car.id", true);
            buildOrCondition(filter.getCategory(), queryStr, "category.id", true);
            if (filter.getDescription() != null || filter.getDescription().length() > 0) {
                queryStr.append(" and (description like :description)");
            }
        }
        Query query = session.createQuery(queryStr.toString());
        if (filter.getFrom() != null) {
            query.setTimestamp("from", new Timestamp(filter.getFrom().getTime()));
        }
        if (filter.getTo() != null) {
            query.setTimestamp("to", new Timestamp(filter.getTo().getTime()));
        }
        if ((filter.getDescription() != null) && (filter.getDescription().length() > 0)) {
            query.setString("description", "%" + filter.getDescription() + "%");
        }
        return query;
    }

    public int countByCategoryAndDateInterval(ExpenseCategory expCat, Timestamp dateFrom, Timestamp dateTo) throws DAOException {
        StringBuilder queryStr = new StringBuilder("select COUNT(id) from Expense where category.id = :category");
        buildDateCondition(dateFrom, dateTo, queryStr, false);
        Query query = session.createQuery(queryStr.toString());
        query.setLong("category", expCat.getId());
        return (Integer)query.uniqueResult();
    }
}
