package org.lynxnet.life;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 21/9/2006
 * Time: 23:52:55
 * To change this template use File | Settings | File Templates.
 */
public class Surface {
    private SortedMap<Integer, SortedMap<Integer, Cell>> xAxis;

    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;

    public Surface(Collection<Cell> cells) {
        xAxis = new TreeMap<Integer, SortedMap<Integer, Cell>>();
        if (cells != null) {
            for (Cell cell : cells) {
                addCell(cell);
            }
        }
    }

    public Cell createCell(int x, int y) {
        Cell cell = new Cell(x, y);
        addCell(cell);
        return cell;
    }

    public void addCell(Cell cell) {
        Map<Integer, Cell> yAxis = getYAxis(cell.getX());
        yAxis.put(cell.getY(), cell);
        if (minX > cell.getX()) {
            minX = cell.getX();
        }
        if (maxX < cell.getX()) {
            maxX = cell.getX();
        }
        if (minY > cell.getY()) {
            minY = cell.getY();
        }
        if (maxY < cell.getY()) {
            maxY = cell.getY();
        }
    }

    public SortedMap<Integer, Cell> getYAxis(Integer x) {
        SortedMap<Integer, Cell> yAxis = xAxis.get(x);
        if (yAxis == null) {
            yAxis = new TreeMap<Integer, Cell>();
            xAxis.put(x, yAxis);
        }
        return yAxis;
    }

    Iterator getAxisIterator() {
        return xAxis.entrySet().iterator();
    }

    public void deleteCell(int x, int y) {
        Map<Integer, Cell> yAxis = getYAxis(x);
        yAxis.remove(y);
        if (yAxis.isEmpty()) {
            xAxis.remove(x);
        }
        if ((maxX == x) || (minX == x) || (maxY == y) || (minY == y)) {
            findBoundaries();
        }
    }

    private void findBoundaries() {
        minX = xAxis.firstKey();
        maxX = xAxis.lastKey();

        for (Map.Entry<Integer, SortedMap<Integer, Cell>> entry : xAxis.entrySet()) {
            SortedMap<Integer, Cell> yAxis = entry.getValue();
            if (yAxis != null) {
                if (yAxis.firstKey() > maxY) {
                    maxY = yAxis.firstKey();
                }
                if (yAxis.lastKey() < minY) {
                    minY = yAxis.lastKey();
                }
            }
        }
    }

    public Cell getCell(int x, int y) {
        if (xAxis.get(x) != null) {
            Map<Integer, Cell> yAxis = getYAxis(x);
            return yAxis.get(y);
        }
        return null;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public Collection<Cell> findNeighbors(int x, int y) {
        Collection<Cell> result = new TreeSet<Cell>();
        for (int x1 = x - 1; x1 <= x + 1; x1++) {
            for (int y1 = y - 1; y1 <= y + 1; y1++) {
                if (x == x1 && y == y1) {
                    continue;
                }
                Cell cell = this.getCell(x1, y1);
                if (cell != null) {
                    result.add(cell);
                }
            }
        }
        return result;
    }

    Collection<Cell> extractCells() {
        Set<Cell> result = new TreeSet<Cell>();
        for (SortedMap<Integer, Cell> axis : xAxis.values()) {
            for (Cell cell : axis.values()) {
                result.add(cell);
            }
        }
        return result;
    }
}
