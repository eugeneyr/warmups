package info.lynxnet.warmups;

import java.util.*;

public class LongestScenicRouteFinder {
    private Map<AtomicVector, RouteInfo> knownRoutes = new LinkedHashMap<AtomicVector, RouteInfo>();

    public AtomicVector getNext(String[] map, AtomicVector av) {
        if (av == null) {
            return null;
        }
        Point point = av.getPoint();
        point = checkBoundaries(map, point);
        if (point == null) {
            return null;
        }
        String line = map[point.getY()];
        char c = line.charAt(point.getX());
        int x = point.getX();
        int y = point.getY();
        Direction newDir = av.getDirection();
        if (c == '.') {
            switch (av.getDirection()) {
                case DOWN:
                    y++;
                    break;
                case UP:
                    y--;
                    break;
                case LEFT:
                    x--;
                    break;
                case RIGHT:
                    x++;
                    break;
            }
        } else if (c == 'R') {
            switch (av.getDirection()) {
                case DOWN:
                    x--;
                    newDir = Direction.LEFT;
                    break;
                case UP:
                    x++;
                    newDir = Direction.RIGHT;
                    break;
                case LEFT:
                    y--;
                    newDir = Direction.UP;
                    break;
                case RIGHT:
                    y++;
                    newDir = Direction.DOWN;
                    break;
            }
        } else if (c == 'L') {
            switch (av.getDirection()) {
                case DOWN:
                    x++;
                    newDir = Direction.RIGHT;
                    break;
                case UP:
                    x--;
                    newDir = Direction.LEFT;
                    break;
                case LEFT:
                    y++;
                    newDir = Direction.DOWN;
                    break;
                case RIGHT:
                    y--;
                    newDir = Direction.UP;
                    break;
            }
        } else if (c == 'T') {
            switch (av.getDirection()) {
                case DOWN:
                    y--;
                    newDir = Direction.UP;
                    break;
                case UP:
                    y++;
                    newDir = Direction.DOWN;
                    break;
                case LEFT:
                    x++;
                    newDir = Direction.RIGHT;
                    break;
                case RIGHT:
                    x--;
                    newDir = Direction.LEFT;
                    break;
            }
        }
        Point newPoint = new Point(x, y);
        newPoint = checkBoundaries(map, newPoint);
        if (newPoint != null) {
            return new AtomicVector(newPoint, newDir);
        }
        return null;
    }

    private Point checkBoundaries(String[] map, Point point) {
        if (point == null) {
            return null;
        }
        if (point.getX() < 0 || point.getY() < 0 || point.getY() >= map.length) {
            return null;
        }
        String line = map[point.getY()];
        if (line == null || line.length() <= point.getX()) {
            return null;
        }
        return point;
    }

    public RouteInfo findMostScenicRoute(String[] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length(); x++) {
                for (Direction dir : Direction.values()) {
                    AtomicVector start = new AtomicVector(new Point(x, y), dir);
                    if (knownRoutes.containsKey(start)) {
                        continue;
                    }
                    LinkedHashSet<AtomicVector> route = new LinkedHashSet<AtomicVector>();
                    LinkedList<RouteInfo> subroutes = new LinkedList<RouteInfo>();
                    AtomicVector current = start;
                    do {
                        if (current == null || route.contains(current)) {
                            // either we fell off the map or we detected a loop
                            break;
                        } else {
                            route.add(current);
                            for (RouteInfo info : subroutes) {
                                info.hitNewPoint(current);
                            }
                            RouteInfo subroute = new RouteInfo(current);
                            subroutes.add(subroute);
                        }
                        current = getNext(map, current);
                    } while (true);
                    if (current == null) {
                        // we fell off the map, so all the accumulated subroutes can be added to the "known" map
                        for (RouteInfo info : subroutes) {
                            if (!knownRoutes.containsKey(info.getStart())) {
                                knownRoutes.put(info.getStart(), info);
                            }
                        }
                    } else {
                        // only the routes that start before the detected loop closes are complete and can be added
                        for (RouteInfo info : subroutes) {
                            if (!knownRoutes.containsKey(info.getStart())) {
                                knownRoutes.put(info.getStart(), info);
                            }
                            if (info.getStart().equals(current)) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        RouteInfo theBest = null;
        int max = 0;
        for (RouteInfo ri : knownRoutes.values()) {
            if (ri.getUniquePoints().size() > max) {
                theBest = ri;
                max = ri.getUniquePoints().size();
            }
        }
        return theBest;
    }

    public static void main(String[] args) {
        LongestScenicRouteFinder finder = new LongestScenicRouteFinder();
        String[] map = {
                "...R...",
                "T.....L",
                "...L..L",
                "......."
        };
        RouteInfo theBest = finder.findMostScenicRoute(map);
        if (theBest != null) {
            System.out.println(theBest.getUniquePoints().size());
            System.out.println(theBest.getRoute());
        } else {
            System.out.println("***ALERT*** ***ALERT*** ***ALERT*** SYSTEM FAILURE PROCEED WITH EVACUATION ACCORDING TO PLAN C");
        }
    }
}
