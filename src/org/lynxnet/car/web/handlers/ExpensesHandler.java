package org.lynxnet.car.web.handlers;

import org.lynxnet.car.dao.DAOFactory;
import org.lynxnet.car.dao.ExpenseDAO;
import org.lynxnet.car.model.util.PageInfo;
import org.lynxnet.car.dao.DAOException;
import org.lynxnet.car.model.filters.ExpensesFilter;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 6/8/2006
 * Time: 17:03:01
 * To change this template use File | Settings | File Templates.
 */
public class ExpensesHandler {
    private ExpensesFilter filter;
    private PageInfo pageInfo;

    private DataModel dataModel;

    private DAOFactory factory;

    public DAOFactory getFactory() {
        return factory;
    }

    public void setFactory(DAOFactory factory) {
        this.factory = factory;
    }

    public ExpensesHandler() {
        filter = new ExpensesFilter();
        // temporary hook
        // factory = DAOFactory.createInstance();
    }

    public ExpensesFilter getFilter() {
        return filter;
    }

    public PageInfo getPageInfo() {
        if (pageInfo == null) {
            pageInfo = new PageInfo(0, 20, 0);
        }
        return pageInfo;
    }

    public DataModel getDataModel() throws DAOException {
        if (dataModel == null) {
            dataModel = new ListDataModel();
        }
        ExpenseDAO expDao = factory.getExpenseDAO();
        expDao.beginTransaction();
        try {
            dataModel.setWrappedData(expDao.findByFilter(getFilter(), getPageInfo()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            throw new DAOException(ex);
        }
        finally {
            expDao.commit();
        }
        return dataModel;
    }

    public int getNoOfRows() {
        return pageInfo.getResultsPerPage();
    }

    public void setNoOfRows(int noOfRows) {
        if ((pageInfo.getResultsPerPage() > 0) && (noOfRows > 0)) {
            int startRow = pageInfo.getResultsPerPage() * pageInfo.getPageNumber();
            pageInfo.setPageNumber(startRow / noOfRows);
        }
        pageInfo.setResultsPerPage(noOfRows);
    }

    public int getStartRow() {
        return pageInfo.getResultsPerPage() * pageInfo.getPageNumber();
    }

    public void setStartRow(int startRow) {
        if (pageInfo.getResultsPerPage() > 0) {
            pageInfo.setPageNumber(startRow / pageInfo.getResultsPerPage());
        }
    }
}
