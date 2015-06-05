package info.lynxnet.warmups.crossword;

public class WeightedWordPosition extends WordPosition {
    protected int intersections;

    public int getIntersections() {
        return intersections;
    }

    public void setIntersections(int intersections) {
        this.intersections = intersections;
    }

    @Override
    public String toString() {
        return "WeightedWordPosition(" +
                 x +
                ", " + y +
                ", "+ direction +
                ", "+ intersections +
                ", '" + word + '\'' +
                ')';
    }

}
