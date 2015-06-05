package org.lynxnet.car.dao;

import org.lynxnet.car.model.beans.ExpenseItemCategory;
import org.lynxnet.car.model.util.PageInfo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 23/7/2006
 * Time: 10:10:11
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseItemCategoryDAO extends GenericDAO {
    ExpenseItemCategory find(long id) throws DAOException;

    List findAll(PageInfo pageInfo) throws DAOException;

    ExpenseItemCategory store(ExpenseItemCategory expItemCat) throws DAOException;

    void delete(long id) throws DAOException;

}
