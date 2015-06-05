package org.lynxnet.car.dao;

import org.lynxnet.car.model.beans.Expense;
import org.lynxnet.car.model.beans.ExpenseCategory;
import org.lynxnet.car.model.filters.ExpensesFilter;
import org.lynxnet.car.model.util.PageInfo;

import java.util.List;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 22/7/2006
 * Time: 22:51:22
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseDAO extends GenericDAO {
    Expense find(long id) throws DAOException;

    List findAll(PageInfo pageInfo) throws DAOException;

    List findByCategory(ExpenseCategory expCat, PageInfo pageInfo) throws DAOException;

    List findByDateInterval(Timestamp dateFrom, Timestamp dateTo, PageInfo pageInfo) throws DAOException;

    List findByFilter(ExpensesFilter filter, PageInfo pageInfo) throws DAOException;

    List findByCategoryAndDateInterval(ExpenseCategory expCat,
                                       Timestamp dateFrom, Timestamp dateTo, PageInfo pageInfo) throws DAOException;

    int countAll() throws DAOException;

    int countByCategory(ExpenseCategory expCat) throws DAOException;

    int countByDateInterval(Timestamp dateFrom, Timestamp dateTo) throws DAOException;

    int countByFilter(ExpensesFilter filter) throws DAOException;

    int countByCategoryAndDateInterval(ExpenseCategory expCat,
                                       Timestamp dateFrom, Timestamp dateTo) throws DAOException;

    Expense store(Expense expense) throws DAOException;

    void delete(long id) throws DAOException;
}
