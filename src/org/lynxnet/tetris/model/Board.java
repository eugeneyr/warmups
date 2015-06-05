package org.lynxnet.tetris.model;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Board {
    protected Piece currentPiece;
    protected int score;
    protected int removedRows;
    protected Cell leftTop = new Cell(0, 0);
    protected Cell rightBottom = new Cell(9, 23);
    protected Set<FilledCell> filledCells = new TreeSet<FilledCell>();

    public Board(Cell leftTop, Cell rightBottom) {
        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
    }

    public Board() {
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Cell getLeftTop() {
        return leftTop;
    }

    public void setLeftTop(Cell leftTop) {
        this.leftTop = leftTop;
    }

    public Cell getRightBottom() {
        return rightBottom;
    }

    public void setRightBottom(Cell rightBottom) {
        this.rightBottom = rightBottom;
    }

    public Set<FilledCell> getFilledCells() {
        return filledCells;
    }

    public void setFilledCells(Set<FilledCell> filledCells) {
        this.filledCells = filledCells;
    }

    public boolean canMoveLeft() {
        if (currentPiece == null) {
            return false;
        }
        Cell corner = Piece.findLeftBottomCorner(currentPiece.getCells());
        if (corner.getX() == leftTop.getX()) {
            return false;
        }
        Set<Cell> cells = new TreeSet<Cell>(currentPiece.tryToMove(Direction.LEFT));
        cells.retainAll(filledCells);
        return cells.isEmpty();
    }

    public boolean canRotate(RotateDirection rd) {
        if (currentPiece == null) {
            return false;
        }
        List<Cell> newCells = currentPiece.tryToRotate(rd);
        Cell leftBottomCorner = Piece.findRightBottomCorner(newCells);
        if (leftBottomCorner.getX() < leftTop.getX()) {
            return false;
        }
        if (leftBottomCorner.getY() > rightBottom.getY()) {
            return false;
        }
        Cell rightBottomCorner = Piece.findRightBottomCorner(newCells);
        if (rightBottomCorner.getX() > rightBottom.getX()) {
            return false;
        }
        Cell leftTopCorner = Piece.findLeftTopCorner(newCells);
        if (leftTopCorner.getY() < leftTop.getY()) {
            return false;
        }
        Set<Cell> cells = new TreeSet<Cell>(newCells);
        cells.retainAll(filledCells);
        return cells.isEmpty();
    }

    public boolean canMoveRight() {
        if (currentPiece == null) {
            return false;
        }
        Cell corner = Piece.findRightBottomCorner(currentPiece.getCells());
        if (corner.getX() == rightBottom.getX()) {
            return false;
        }
        Set<Cell> cells = new TreeSet<Cell>(currentPiece.tryToMove(Direction.RIGHT));
        cells.retainAll(filledCells);
        return cells.isEmpty();
    }

    public boolean canMoveDown() {
        if (currentPiece == null) {
            return false;
        }
        Cell corner = Piece.findRightBottomCorner(currentPiece.getCells());
        if (corner.getY() == rightBottom.getY()) {
            return false;
        }
        Set<Cell> cells = new TreeSet<Cell>(currentPiece.tryToMove(Direction.DOWN));
        cells.retainAll(filledCells);
        return cells.isEmpty();
    }

    public boolean willFit(Piece piece) {
        Cell corner = Piece.findRightBottomCorner(piece.getCells());
        if (corner.getY() == rightBottom.getY()) {
            return false;
        }
        Set<Cell> cells = new TreeSet<Cell>(piece.getCells());
        cells.retainAll(filledCells);
        return cells.isEmpty();
    }

    public boolean makeLanding() {
        if (currentPiece == null) {
            return false;
        }
        if (!canMoveDown()) {
            score += 10 * currentPiece.getCells().size();
            for (Cell cell : currentPiece.getCells()) {
                FilledCell fc = new FilledCell(cell, currentPiece.getPieceType());
                filledCells.add(fc);
            }
            currentPiece = null;
            return true;
        }
        return false;
    }

    public boolean reduceFilledCells() {
        if (filledCells.isEmpty()) {
            return false;
        }
        FilledCell firstCell = filledCells.iterator().next();
        Set<Integer> rowsToRemove = new TreeSet<Integer>();
        for (int i = firstCell.getY(); i <= rightBottom.getY(); i++) {
            boolean filled = true;
            for (int j = leftTop.getX(); j <= rightBottom.getX(); j++) {
                if (!filledCells.contains(new Cell(j, i))) {
                    filled = false;
                    break;
                }
            }
            if (filled) {
                rowsToRemove.add(i);
            }
        }
        if (rowsToRemove.isEmpty()) {
            return false;
        }
        for (int i : rowsToRemove) {
            removeRow(filledCells, i);
        }
        score += rowsToRemove.size() * 100;
        removedRows += rowsToRemove.size();
        return true;
    }

    public void removeRow(Set<FilledCell> cells, int row) {
        Set<FilledCell> newCells = new TreeSet<FilledCell>();
        for (FilledCell cell : cells) {
            if (cell.getY() == row) {
                continue;
            }
            if (cell.getY() < row) {
                cell.setY(cell.getY() + 1);
            }
            newCells.add(cell);
        }
        cells.clear();
        cells.addAll(newCells);
    }

    public PieceType getCellFilling(int x, int y) {
        if (x < leftTop.getX() || x > rightBottom.getX()) {
            return null;
        }
        if (y < leftTop.getY() || y > rightBottom.getY()) {
            return null;
        }
        if (currentPiece != null) {
            for (Cell cell : currentPiece.getCells()) {
                if (cell.getX() == x && cell.getY() == y) {
                    return currentPiece.getPieceType();
                }
            }
        }
        for (FilledCell cell : filledCells) {
            if (cell.getX() == x && cell.getY() == y) {
                return cell.getPieceType();
            }
        }
        return null;
    }

    public int getRemovedRows() {
        return removedRows;
    }

    public void setRemovedRows(int removedRows) {
        this.removedRows = removedRows;
    }
}
