package org.lynxnet.car.model.util;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 23/7/2006
 * Time: 10:38:07
 * To change this template use File | Settings | File Templates.
 */
public class PageInfo implements Serializable {
    private int pageNumber;
    private int resultsPerPage;
    private int total;

    public PageInfo() {
    }

    public PageInfo(int pageNumber, int resultsPerPage, int total) {
        this.pageNumber = pageNumber;
        this.resultsPerPage = resultsPerPage;
        this.total = total;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
