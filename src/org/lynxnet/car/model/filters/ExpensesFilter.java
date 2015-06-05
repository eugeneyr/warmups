package org.lynxnet.car.model.filters;

import java.util.Date;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 7/8/2006
 * Time: 22:12:46
 * To change this template use File | Settings | File Templates.
 */
public class ExpensesFilter implements Serializable {
    private Date from;
    private Date to;
    private long[] car;
    private long[] category;
    private String description;

    public ExpensesFilter() {
    }

    public ExpensesFilter(Date from, Date to, long[] car, long[] category, String description) {
        this.from = from;
        this.to = to;
        this.car = car;
        this.category = category;
        this.description = description;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public long[] getCar() {
        return car;
    }

    public long[] getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public void setCar(long[] car) {
        this.car = car;
    }

    public void setCategory(long[] category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEmpty() {
        return
                (from == null) &&
                (to == null) &&
                (car == null || car.length == 0) &&
                (category == null || category.length == 0) &&
                (description == null || description.length() == 0);
    }
}
