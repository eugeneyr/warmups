package org.lynxnet.car.dao;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 23/7/2006
 * Time: 10:48:53
 * To change this template use File | Settings | File Templates.
 */
public interface DAOFactory {

    public CarDAO getCarDAO();

    public ExpenseCategoryDAO getExpenseCategoryDAO();

    public ExpenseDAO getExpenseDAO();

    public ExpenseItemCategoryDAO getExpenseItemCategoryDAO();

    public ExpenseItemDAO getExpenseItemDAO();
}
