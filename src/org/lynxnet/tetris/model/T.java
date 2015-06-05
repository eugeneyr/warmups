package org.lynxnet.tetris.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class T extends Piece {
    @Override
    public PieceType getPieceType() {
        return PieceType.T;
    }

    public T(Direction d) {
        switch (d) {
            case UP:
                buildUp(cells, 0, 0);
                break;
            case DOWN:
                buildDown(cells, 0, 0);
                break;
            case LEFT:
                buildLeft(cells, 0, 0);
                break;
            case RIGHT:
                buildRight(cells, 0, 0);
                break;
        }
        orientation = d;
    }

    private void buildRight(List<Cell> cells, int baseX, int baseY) {
        cells.add(new Cell(baseX, baseY + 1));
        for (int i = 0; i < 3; i++) {
            cells.add(new Cell(baseX + 1, baseY + i));
        }
    }

    private void buildLeft(List<Cell> cells, int baseX, int baseY) {
        for (int i = 0; i < 3; i++) {
            cells.add(new Cell(baseX, baseY + i));
        }
        cells.add(new Cell(baseX + 1, baseY  + 1));
    }

    private void buildDown(List<Cell> cells, int baseX, int baseY) {
        cells.add(new Cell(baseX + 1, baseY));
        for (int i = 0; i < 3; i++) {
            cells.add(new Cell(baseX + i, baseY + 1));
        }
    }

    private void buildUp(List<Cell> cells, int baseX, int baseY) {
        for (int i = 0; i < 3; i++) {
            cells.add(new Cell(baseX + i, baseY));
        }
        cells.add(new Cell(baseX + 1, baseY + 1));
    }

    @Override
    public List<Cell> tryToRotate(RotateDirection rd) {
        Direction newDirection = getNewOrientation(rd);
        List<Cell> result = new ArrayList<Cell>(cells.size());
        Cell leftTopCorner = findLeftTopCorner(cells);
        switch (newDirection) {
            case UP:
                buildUp(result, leftTopCorner.getX(), leftTopCorner.getY());
                break;
            case DOWN:
                buildDown(result, leftTopCorner.getX(), leftTopCorner.getY());
                break;
            case LEFT:
                buildLeft(result, leftTopCorner.getX(), leftTopCorner.getY());
                break;
            case RIGHT:
                buildRight(result, leftTopCorner.getX(), leftTopCorner.getY());
                break;
        }
        return result;
    }
}
