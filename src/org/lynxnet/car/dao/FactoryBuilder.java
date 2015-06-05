package org.lynxnet.car.dao;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 17/9/2006
 * Time: 22:54:38
 * To change this template use File | Settings | File Templates.
 */
public class FactoryBuilder {
    private Class factoryClass;

    public FactoryBuilder(String factoryClassName) throws DAOException {
        if (factoryClassName == null) {
            throw new DAOException("The factory class name is null");
        }
        try {
            factoryClass = Class.forName(factoryClassName);
        } catch (ClassNotFoundException e) {
            throw new DAOException(e);

        }
        if (!DAOFactory.class.isAssignableFrom(factoryClass)) {
            throw new DAOException("The class " + factoryClass.getName()
                    + " is not a descendant of " + DAOFactory.class.getName());
        }
    }

    public DAOFactory createInstance() throws DAOException {
        try {
            return (DAOFactory) factoryClass.newInstance();
        } catch (InstantiationException e) {
            throw new DAOException(e);
        } catch (IllegalAccessException e) {
            throw new DAOException(e);
        }
    }
}
