package org.lynxnet.tetris.model;

import java.util.List;

/**
 *
 */
public class O extends Piece {
    @Override
    public PieceType getPieceType() {
        return PieceType.O;
    }

    public O(Direction d) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                cells.add(new Cell(i, j));
            }
        }
        orientation = d;
    }

    @Override
    public List<Cell> tryToRotate(RotateDirection rd) {
        return cells;
    }

    @Override
    public Piece rotate(RotateDirection rd) {
        orientation = getNewOrientation(rd);
        return this;
    }
}
