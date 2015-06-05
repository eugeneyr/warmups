package org.lynxnet.tetris.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Z extends Piece {
    @Override
    public PieceType getPieceType() {
        return PieceType.Z;
    }

    public Z(Direction d) {
        switch (d) {
            case UP:
            case DOWN:
                buildHorizontal(cells, 0, 0);
                break;
            case LEFT:
            case RIGHT:
                buildVertical(cells, 0, 0);
                break;
        }
        orientation = d;
    }

    private void buildHorizontal(List<Cell> cells, int baseX, int baseY) {
        for (int i = 0; i < 2; i++) {
            cells.add(new Cell(baseX + i, baseY));
            cells.add(new Cell(baseX + i + 1, baseY + 1));
        }
    }

    private void buildVertical(List<Cell> cells, int baseX, int baseY) {
        for (int i = 0; i < 2; i++) {
            cells.add(new Cell(baseX, baseY + i + 1));
            cells.add(new Cell(baseX + 1, baseY + i));
        }
    }

    @Override
    public List<Cell> tryToRotate(RotateDirection rd) {
        Direction newDirection = getNewOrientation(rd);
        List<Cell> result = new ArrayList<Cell>(cells.size());
        Cell leftTopCorner = findLeftTopCorner(cells);
        switch (newDirection) {
            case UP:
            case DOWN:
                buildHorizontal(result, leftTopCorner.getX(), leftTopCorner.getY());
                break;
            case LEFT:
            case RIGHT:
                buildVertical(result, leftTopCorner.getX(), leftTopCorner.getY());
                break;
        }
        return result;
    }
}
