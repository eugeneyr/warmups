package info.lynxnet.warmups.crossword.historical;

public class EmptyCell {
    private int x;
    private int y;

    public EmptyCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public EmptyCell() {
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
        if (!(o instanceof EmptyCell)) return false;

        EmptyCell emptyCell = (EmptyCell) o;

        if (x != emptyCell.x) return false;
        if (y != emptyCell.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
