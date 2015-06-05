package info.lynxnet.warmups;

import java.util.*;

public class RouteInfo implements RouteUpdateListener {
    private AtomicVector start;
    private List<AtomicVector> route = new LinkedList<AtomicVector>();
    private Set<Point> uniquePoints = new LinkedHashSet<Point>();

    public void hitNewPoint(AtomicVector av) {
        if (av != null && av.getPoint() != null) {
            route.add(av);
            uniquePoints.add(av.getPoint());
            if (start == null) {
                start = av;
            }
        }
    }

    public AtomicVector getStart() {
        return start;
    }

    public void setStart(AtomicVector start) {
        this.start = start;
    }

    public List<AtomicVector> getRoute() {
        return route;
    }

    public Set<Point> getUniquePoints() {
        return uniquePoints;
    }

    public RouteInfo(AtomicVector start) {
        this.start = start;
        hitNewPoint(start);
    }

    public RouteInfo() {
    }

    public RouteInfo concatenate(RouteInfo tail) {
        if (!route.isEmpty() && !tail.getRoute().isEmpty()) {
            AtomicVector thisLast = route.get(route.size() - 1);
            AtomicVector thatFirst = tail.getRoute().get(0);
            if (thisLast.equals(thatFirst)) {
                for (int i = 1; i < tail.getRoute().size(); i++) {
                    AtomicVector next = tail.getRoute().get(i);
                    route.add(next);
                }
            } else {
                route.addAll(tail.getRoute());
            }
        } else {
            route.addAll(tail.getRoute());
        }
        uniquePoints.addAll(tail.getUniquePoints());
        if (start == null) {
            start = tail.getStart();
        }
        return this;
    }
}
