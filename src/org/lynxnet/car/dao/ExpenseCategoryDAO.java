package org.lynxnet.car.dao;

import org.lynxnet.car.model.beans.ExpenseCategory;
import org.lynxnet.car.model.util.PageInfo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 23/7/2006
 * Time: 10:09:22
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseCategoryDAO extends GenericDAO {
    ExpenseCategory find(long id) throws DAOException;

    List findAll(PageInfo pageInfo) throws DAOException;

    ExpenseCategory store(ExpenseCategory expCat) throws DAOException;

    void delete(long id) throws DAOException;
}
