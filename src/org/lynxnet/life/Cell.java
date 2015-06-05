package org.lynxnet.life;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 21/9/2006
 * Time: 23:32:08
 * To change this template use File | Settings | File Templates.
 */
public class Cell implements Comparable<Cell> {
    private int x;
    private int y;
    private int age;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.age = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Cell cell = (Cell) o;

        if (x != cell.x) {
            return false;
        }

        return y == cell.y;
    }

    public int hashCode() {
        int result;
        result = x;
        result = 29292929 * result + y;
        return result;
    }

    public int increment() {
        return ++age;
    }

    public int compareTo(Cell c) {
        if (c == null) {
            return -1;
        }
        if (getX() < c.getX()) {
            return -1;
        }
        else if(getX() > c.getX()) {
            return 1;
        }
        if (getY() < c.getY()) {
            return -1;
        }
        else
        if (getY() > c.getY()) {
            return 1;
        }
        return 0;
    }
}
