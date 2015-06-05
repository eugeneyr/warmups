package org.lynxnet.car.model.beans;

import java.io.Serializable;

public class ExpenseItemCategory implements Serializable {
    private long id;
    private String kind;

    public ExpenseItemCategory() {
    }

    public ExpenseItemCategory(long id, String kind) {
        this.id = id;
        this.kind = kind;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
