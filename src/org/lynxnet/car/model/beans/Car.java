package org.lynxnet.car.model.beans;

import java.io.Serializable;

public class Car implements Serializable {
    private long id;

    private String vin;

    private int year;

    private String make;

    private String model;

    private String trim;


    public Car() {
    }

    public Car(long id, String vin, int year, String make, String model, String trim) {
        this.id = id;
        this.vin = vin;
        this.year = year;
        this.make = make;
        this.model = model;
        this.trim = trim;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
