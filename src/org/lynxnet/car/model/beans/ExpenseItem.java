package org.lynxnet.car.model.beans;

import java.io.Serializable;

public class ExpenseItem implements Serializable {
    private long id;

    private Expense expense;

    private ExpenseItemCategory itemCategory;

    private int lineNo;

    private String description;

    private String measUnit;

    private double Quantity;

    private double price;

    private double amount;

    public ExpenseItem(long id, Expense expense, ExpenseItemCategory itemCategory,
                       int lineNo, String description, String measUnit,
                       double quantity, double price, double amount) {
        this.id = id;
        this.expense = expense;
        this.itemCategory = itemCategory;
        this.lineNo = lineNo;
        this.description = description;
        this.measUnit = measUnit;
        Quantity = quantity;
        this.price = price;
        this.amount = amount;
    }

    public ExpenseItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public ExpenseItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ExpenseItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeasUnit() {
        return measUnit;
    }

    public void setMeasUnit(String measUnit) {
        this.measUnit = measUnit;
    }

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
