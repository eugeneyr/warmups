package info.lynxnet.warmups.crossword;

import info.lynxnet.warmups.Direction;

public class WordPosition implements Comparable, Cloneable {
    protected int x;
    protected int y;
    protected Direction direction;
    protected String word;

    public WordPosition(int x, int y, Direction dir, String word) {
        this.x = x;
        this.y = y;
        this.word = word;
        this.direction = dir;
    }

    public WordPosition(String word) {
        this.word = word;
    }

    public WordPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public WordPosition() {
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordPosition)) return false;

        WordPosition that = (WordPosition) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (direction != that.direction) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (word != null ? word.hashCode() : 0);
        return result;
    }

    public int compareTo(Object o) {
        if (o == null || (! (o instanceof WordPosition))) {
            return -1;
        }
        WordPosition other = (WordPosition) o;
        if (x != other.getX()) {
            return x -  other.getX();
        }
        if (y != other.getY()) {
            return y - other.getY();
        }
        if (other.getDirection() != direction) {
            return direction == Direction.RIGHT ? 1 : -1;
        }
        return word.compareTo(other.getWord());
    }

    @Override
    public String toString() {
        return "WordPosition(" +
                 x +
                ", " + y +
                ", "+ direction +
                ", '" + word + '\'' +
                ')';
    }
}
