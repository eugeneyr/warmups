package org.lynxnet.tetris.model;

import java.io.Serializable;

/**
 *
 */
public class Cell implements Serializable, Comparable {
    protected int x;
    protected int y;

    public Cell(Cell otherCell) {
        x = otherCell.x;
        y = otherCell.y;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;

        Cell cell = (Cell) o;

        if (x != cell.x) return false;
        if (y != cell.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }
        if (o instanceof Cell) {
            Cell otherCell = (Cell) o;
            if (y == otherCell.y) {
                return x - otherCell.x;
            }
            return y - otherCell.y;
        }
        return 0;
    }
}
