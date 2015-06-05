package info.lynxnet.warmups;

public class AtomicVector {
    private Point point;
    private Direction direction;

    public AtomicVector(Point point, Direction direction) {
        this.point = point;
        this.direction = direction;
    }

    public AtomicVector() {
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
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
        if (!(o instanceof AtomicVector)) return false;

        AtomicVector that = (AtomicVector) o;

        if (direction != that.direction) return false;
        if (point != null ? !point.equals(that.point) : that.point != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = point != null ? point.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "(" + point + ", " + direction + ')';
    }
}
