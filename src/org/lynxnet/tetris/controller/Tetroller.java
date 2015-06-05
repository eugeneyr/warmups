package org.lynxnet.tetris.controller;

import org.lynxnet.tetris.model.*;

/**
 *
 */
public class Tetroller {
    protected Board board;
    protected Piece nextPiece;
    protected boolean gameEnded = false;

    public Tetroller(int cols, int rows) {
        board = new Board(new Cell(0, 0), new Cell(cols - 1, rows - 1));
        board.setCurrentPiece(Dice.throwDice(board));
        nextPiece = Dice.throwDice(board);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Piece getNextPiece() {
        return nextPiece;
    }

    public void setNextPiece(Piece nextPiece) {
        this.nextPiece = nextPiece;
    }

    public synchronized void startNextPiece() {
        board.setCurrentPiece(nextPiece);
        nextPiece = Dice.throwDice(board);
    }

    public synchronized void tick() {
        if (gameEnded) {
            return;
        }
        if (board.canMoveDown()) {
            board.getCurrentPiece().move(Direction.DOWN);
        } else {
            board.makeLanding();
            board.reduceFilledCells();
        }
        if (board.getCurrentPiece() == null) {
            if (board.willFit(nextPiece)) {
                startNextPiece();
            } else {
                // TADA!!!
                gameEnded = true;
            }
        }
    }

    public synchronized boolean moveLeft() {
        if (gameEnded) {
            return false;
        }
        if (board.getCurrentPiece() == null) {
            return false;
        }
        if (board.canMoveLeft()) {
            board.getCurrentPiece().move(Direction.LEFT);
            return true;
        }
        return false;
    }

    public synchronized boolean moveRight() {
        if (gameEnded) {
            return false;
        }
        if (board.getCurrentPiece() == null) {
            return false;
        }
        if (board.canMoveRight()) {
            board.getCurrentPiece().move(Direction.RIGHT);
            return true;
        }
        return false;
    }

    public synchronized boolean moveDown() {
        if (gameEnded) {
            return false;
        }
        if (board.getCurrentPiece() == null) {
            return false;
        }
        if (board.canMoveDown()) {
            board.getCurrentPiece().move(Direction.DOWN);
            return true;
        }
        return false;
    }

    public synchronized boolean drop() {
        if (gameEnded) {
            return false;
        }
        if (board.getCurrentPiece() == null) {
            return false;
        }
        while (board.canMoveDown()) {
            board.getCurrentPiece().move(Direction.DOWN);
        }
        return true;
    }

    public synchronized boolean rotateLeft() {
        if (gameEnded) {
            return false;
        }
        if (board.getCurrentPiece() == null) {
            return false;
        }
        if (board.canRotate(RotateDirection.LEFT)) {
            board.getCurrentPiece().rotate(RotateDirection.LEFT);
            return true;
        }
        return false;
    }

    public synchronized boolean rotateRight() {
        if (gameEnded) {
            return false;
        }
        if (board.getCurrentPiece() == null) {
            return false;
        }
        if (board.canRotate(RotateDirection.RIGHT)) {
            board.getCurrentPiece().rotate(RotateDirection.RIGHT);
            return true;
        }
        return false;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public synchronized void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }
}
