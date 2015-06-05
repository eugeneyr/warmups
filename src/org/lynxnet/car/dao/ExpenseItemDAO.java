package org.lynxnet.car.dao;

import org.lynxnet.car.model.beans.ExpenseItem;
import org.lynxnet.car.model.beans.Expense;
import org.lynxnet.car.model.beans.ExpenseItemCategory;
import org.lynxnet.car.model.util.PageInfo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 23/7/2006
 * Time: 10:09:59
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseItemDAO extends GenericDAO {
    ExpenseItem find(long id) throws DAOException;

    List findByExpense(Expense expense, PageInfo pageInfo) throws DAOException;

    List findByCategory(ExpenseItemCategory expItemCat, PageInfo pageInfo) throws DAOException;

    List findByExpenseAndCategory(Expense expense, ExpenseItemCategory expItemCat,
                                  PageInfo pageInfo) throws DAOException;

    ExpenseItem store(ExpenseItem expItem) throws DAOException;

    void delete(long id) throws DAOException;

}
