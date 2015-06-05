package org.lynxnet.tetris.controller;

import org.lynxnet.tetris.model.*;

import java.util.Random;

/**
 *
 */
public class Dice {
    public static Piece throwDice(Board board) {
        Random rnd = new Random();
        Direction dir = Direction.values()[rnd.nextInt(Direction.values().length)];
        PieceType pieceType = PieceType.values()[rnd.nextInt(PieceType.values().length)];
        Piece piece = Piece.getNewPiece(pieceType, dir);
        // make sure it is on the board
        while (board.getLeftTop().getY() > Piece.findLeftTopCorner(piece.getCells()).getY()) {
            piece.move(Direction.DOWN);
        }
        while (board.getLeftTop().getX() > Piece.findLeftTopCorner(piece.getCells()).getX()) {
            piece.move(Direction.RIGHT);
        }

        // randomize the horizontal position (a non-canonical stuff, I think)
        Cell leftCorner = Piece.findLeftTopCorner(piece.getCells());
        Cell rightCorner = Piece.findRightTopCorner(piece.getCells());
        int legalDistance = board.getRightBottom().getX() + leftCorner.getX() - rightCorner.getX() + 1;
        int pos = rnd.nextInt(legalDistance);
        while (pos-- > 0) {
            piece.move(Direction.RIGHT);
        }
        return piece;
    }
}
