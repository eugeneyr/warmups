package org.lynxnet.car.dao;

import org.lynxnet.car.model.beans.Car;
import org.lynxnet.car.model.util.PageInfo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 23/7/2006
 * Time: 10:00:34
 * To change this template use File | Settings | File Templates.
 */
public interface CarDAO extends GenericDAO {
    Car find(long id) throws DAOException;

    List findAll(PageInfo pageInfo) throws DAOException;

    Car store(Car car) throws DAOException;

    void delete(long id) throws DAOException;
}
