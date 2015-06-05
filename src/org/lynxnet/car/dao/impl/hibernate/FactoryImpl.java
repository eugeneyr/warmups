package org.lynxnet.car.dao.impl.hibernate;

import org.lynxnet.car.dao.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 23/7/2006
 * Time: 10:51:59
 * To change this template use File | Settings | File Templates.
 */
public class FactoryImpl implements DAOFactory {

    private static SessionFactory sessionFactory;

    static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    private Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    public CarDAO getCarDAO() {
        return new CarDAOImpl(getSession());
    }

    public ExpenseCategoryDAO getExpenseCategoryDAO() {
        return new ExpenseCategoryDAOImpl(getSession());
    }

    public ExpenseDAO getExpenseDAO() {
        return new ExpenseDAOImpl(getSession());
    }

    public ExpenseItemCategoryDAO getExpenseItemCategoryDAO() {
        return new ExpenseItemCategoryDAOImpl(getSession());
    }

    public ExpenseItemDAO getExpenseItemDAO() {
        return new ExpenseItemDAOImpl(getSession());
    }

}
