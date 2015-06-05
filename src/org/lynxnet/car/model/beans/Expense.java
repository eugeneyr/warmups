package org.lynxnet.car.model.beans;

import java.sql.Timestamp;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

public class Expense implements Serializable {
    private long id;

    private Timestamp dateTime;

    private Integer mileage;

    private String description;

    private double totalAmount;

    private ExpenseCategory category;

    private Car car;

    private Set<? extends ExpenseItem> items;

    public Expense() {
        dateTime = new Timestamp(System.currentTimeMillis());
        items = new HashSet<ExpenseItem>();
    }

    public Expense(long id, Timestamp dateTime, Integer mileage, String description,
                   double totalAmount, ExpenseCategory category, Car car, Set<? extends ExpenseItem> items) {
        this.id = id;
        this.dateTime = dateTime;
        this.mileage = mileage;
        this.description = description;
        this.totalAmount = totalAmount;
        this.category = category;
        this.car = car;
        this.items = items;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Set<? extends ExpenseItem> getItems() {
        return items;
    }

    public void setItems(Set<? extends ExpenseItem> items) {
        this.items = items;
    }
}
