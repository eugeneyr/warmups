package org.lynxnet.car.dao;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 22/7/2006
 * Time: 23:05:05
 * To change this template use File | Settings | File Templates.
 */
public class DAOException extends Exception {
    public DAOException(String message) {
        super(message);
    }

    public DAOException(Exception cause) {
        super(cause);
    }

}
