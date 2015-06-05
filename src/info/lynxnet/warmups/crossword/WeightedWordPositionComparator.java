package info.lynxnet.warmups.crossword;

import java.util.Comparator;

public class WeightedWordPositionComparator implements Comparator<WeightedWordPosition> {
    public int compare(WeightedWordPosition o1, WeightedWordPosition o2) {
        if (o1.getIntersections() == o2.getIntersections()) {
            return o1.compareTo(o2);
        }
        return o2.getIntersections() - o1.getIntersections();
    }
}
