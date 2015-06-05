package org.lynxnet.tetris.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class Piece {
    protected List<Cell> cells = new ArrayList<Cell>();
    protected Direction orientation;

    public List<Cell> getCells() {
        return cells;
    }

    public abstract List<Cell> tryToRotate(RotateDirection rd);

    public Piece rotate(RotateDirection rd) {
        List<Cell> newCells = tryToRotate(rd);
        cells.clear();
        cells.addAll(newCells);
        orientation = getNewOrientation(rd);
        return this;
    }

    public List<Cell> tryToMove(Direction d) {
        List<Cell> result = new ArrayList<Cell>(cells.size());
        for (Cell cell : cells) {
            Cell newCell = moveCell(cell, d);
            result.add(newCell);
        }
        return result;
    }

    public Cell moveCell(Cell cell, Direction d) {
        int x = cell.getX();
        int y = cell.getY();
        switch (d) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case RIGHT:
                x++;
                break;
            case LEFT:
                x--;
                break;
        }
        return new Cell(x, y);
    }

    public Piece move(Direction d) {
        List<Cell> newCells = tryToMove(d);
        cells.clear();
        cells.addAll(newCells);
        return this;
    }

    protected Direction getNewOrientation(RotateDirection rd) {
        switch (orientation) {
            case UP:
                return rd == RotateDirection.LEFT ? Direction.LEFT : Direction.RIGHT;
            case DOWN:
                return rd == RotateDirection.LEFT ? Direction.RIGHT : Direction.LEFT;
            case LEFT:
                return rd == RotateDirection.LEFT ? Direction.DOWN : Direction.UP;
            case RIGHT:
                return rd == RotateDirection.LEFT ? Direction.UP : Direction.DOWN;
        }
        return orientation;
    }

    public abstract PieceType getPieceType();

    public Direction getOrientation() {
        return orientation;
    }

    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    public static Cell findLeftTopCorner(List<Cell> cells) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (Cell cell : cells) {
            if (cell.getX() < minX) {
                minX = cell.getX();
            }
            if (cell.getY() < minY) {
                minY = cell.getY();
            }
        }
        return new Cell(minX, minY);
    }

    public static Cell findRightTopCorner(List<Cell> cells) {
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        for (Cell cell : cells) {
            if (cell.getX() > maxX) {
                maxX = cell.getX();
            }
            if (cell.getY() < minY) {
                minY = cell.getY();
            }
        }
        return new Cell(maxX, minY);
    }

    public static Cell findRightBottomCorner(List<Cell> cells) {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Cell cell : cells) {
            if (cell.getX() > maxX) {
                maxX = cell.getX();
            }
            if (cell.getY() > maxY) {
                maxY = cell.getY();
            }
        }
        return new Cell(maxX, maxY);
    }

    public static Cell findLeftBottomCorner(List<Cell> cells) {
        int minX = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Cell cell : cells) {
            if (cell.getX() < minX) {
                minX = cell.getX();
            }
            if (cell.getY() > maxY) {
                maxY = cell.getY();
            }
        }
        return new Cell(minX, maxY);
    }

    public static Piece getNewPiece(PieceType p, Direction dir) {
        switch (p) {
            case L: return new L(dir);
            case R: return new R(dir);
            case T: return new T(dir);
            case I: return new I(dir);
            case O: return new O(dir);
            case S: return new S(dir);
            case Z: return new Z(dir);
        }
        return null;
    }
}
