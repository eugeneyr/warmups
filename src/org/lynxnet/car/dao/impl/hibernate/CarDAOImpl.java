package org.lynxnet.car.dao.impl.hibernate;

import org.lynxnet.car.dao.CarDAO;
import org.lynxnet.car.dao.DAOException;
import org.lynxnet.car.model.util.PageInfo;
import org.lynxnet.car.model.beans.Car;
import org.hibernate.Session;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 23/7/2006
 * Time: 10:48:01
 * To change this template use File | Settings | File Templates.
 */
public class CarDAOImpl extends GenericDAOImpl implements CarDAO {

    CarDAOImpl(Session session) {
        this.session = session;
    }

    public Car find(long id) throws DAOException {
        return (Car) session.load(Car.class, id);
    }

    public List findAll(PageInfo pageInfo) throws DAOException {
        Query query = session.createQuery("from Car");
        applyPageInfo(query, pageInfo);
        return query.list();
    }

    public Car store(Car car) throws DAOException {
        super.store(car);
        return car;
    }

    public void delete(long id) throws DAOException {
        delete("Car", id);
    }
}
