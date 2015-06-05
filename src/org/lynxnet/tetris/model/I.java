package org.lynxnet.tetris.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class I extends Piece {
    public I(Direction d) {
        switch (d) {
            case UP:
            case DOWN:
                for (int i = 0; i < 4; i++) {
                    cells.add(new Cell(1, i));
                }
                break;
            case LEFT:
            case RIGHT:
                for (int i = 0; i < 4; i++) {
                    cells.add(new Cell(i, 1));
                }
                break;
        }
        orientation = d;
    }

    @Override
    public List<Cell> tryToRotate(RotateDirection rd) {
        List<Cell> result = new ArrayList<Cell>(cells.size());
        if (orientation == Direction.UP || orientation == Direction.DOWN) {
            Cell topmostCell = cells.get(0);
            int newX = topmostCell.getX() - 1;
            int newY = topmostCell.getY() + 2;
            for (int i = 0; i < 4; i++) {
                result.add(new Cell(newX + i, newY));
            }
        } else {
            Cell leftmostCell = cells.get(0);
            int newX = leftmostCell.getX() + 1;
            int newY = leftmostCell.getY() - 2;
            for (int i = 0; i < 4; i++) {
                result.add(new Cell(newX, newY + i));
            }
        }
        return result;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.I;
    }
}
