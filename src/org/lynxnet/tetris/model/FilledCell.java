package org.lynxnet.tetris.model;

/**
 *
 */
public class FilledCell extends Cell {
    protected PieceType pieceType;

    public FilledCell(int x, int y, PieceType pieceType) {
        super(x, y);
        this.pieceType = pieceType;
    }

    public FilledCell(Cell otherCell, PieceType pieceType) {
        x = otherCell.x;
        y = otherCell.y;
        this.pieceType = pieceType;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    
}
